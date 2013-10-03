package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.csv.loader.ProctcaeGradeMappingsLoader;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccruResponseGradesAtutomatedTest extends TestDataManager{
	Participant participant;
	ProctcaeGradeMappingVersion proctcaeGradeMappingVersion;
	List<Integer> accruCrfIds = new ArrayList<Integer>();
	
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		deleteAndCreateTestData();
		commitAndStartNewTransaction();
		
		proctcaeGradeMappingVersion = getDefaultProctcaeGradeMappingVersion();
		ProctcaeGradeMappingsLoader proctcaeGradeMappingsLoader = new ProctcaeGradeMappingsLoader();
		proctcaeGradeMappingsLoader.setGenericRepository(genericRepository);
		proctcaeGradeMappingsLoader.setProCtcTermRepository(proCtcTermRepository);
		proctcaeGradeMappingsLoader.loadProctcaeGradeMappings();
		
		// create CRFs for each proCtcTerm present in Db.
		accruCrfIds = CrfTestHelper.createAccruFormsForEachProCtcTerm();
		genericRepository.save(StudyTestHelper.getDefaultStudy());
		
		// create participant on this study and create schedules for all CRFs 
		ParticipantTestHelper.createParticipant("ACCRU", "participant", "ny0AFN", StudyTestHelper.getDefaultStudy().getLeadStudySite(), 0);
		participant = ParticipantTestHelper.getAccruParticipant();
		ParticipantTestHelper.createSchedulesForAllAccruCrfs(participant, StudyTestHelper.getDefaultStudy().getLeadStudySite());
		commitAndStartNewTransaction();
	}
	
	public void testGenerateStudyParticipantCrfGrades(){
		participant = ParticipantTestHelper.getAccruParticipant();
		// tempCount for debugging
		//int tempCount = 0;
		
		for(StudyParticipantCrf studyParticipantCrf : participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs()){
			if(accruCrfIds.contains(studyParticipantCrf.getCrf().getId())){
				// get inputResponse permutation for given proCtcTerm
				Map<Integer, Map<ProCtcQuestionType, Integer>> inputResponseMap = new HashMap<Integer, Map<ProCtcQuestionType, Integer>>();
				List<String> expectedGrades = new ArrayList<String>();
				populateInputResponseMapAndGrade(studyParticipantCrf.getCrf().getCrfPages().get(0).getProCtcTerm(), inputResponseMap, expectedGrades);
				int index = 0;
				int noOfSchedules = studyParticipantCrf.getStudyParticipantCrfSchedules().size();
				for(StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()){
					
					if((index > noOfSchedules - 1) || (index > inputResponseMap.size() - 1)){
						break;
					} 
					Map<ProCtcQuestionType, Integer> responseMap = inputResponseMap.get(index);
					for(StudyParticipantCrfItem spCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()){
						List<ProCtcValidValue> validValues = (List<ProCtcValidValue>) spCrfItem.getCrfPageItem().getProCtcQuestion().getValidValues();
						if(spCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().equals(ProCtcQuestionType.FREQUENCY)){
							int indx = responseMap.get(ProCtcQuestionType.FREQUENCY);
							validValues.get(indx).setResponseCode(validValues.get(indx).getDisplayOrder());
							spCrfItem.setProCtcValidValue(validValues.get(indx));
						} 
						if(spCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().equals(ProCtcQuestionType.SEVERITY)){
							int indx = responseMap.get(ProCtcQuestionType.SEVERITY);
							validValues.get(indx).setResponseCode(validValues.get(indx).getDisplayOrder());
							spCrfItem.setProCtcValidValue(validValues.get(indx));
						}
						if(spCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().equals(ProCtcQuestionType.INTERFERENCE)){
							int indx = responseMap.get(ProCtcQuestionType.INTERFERENCE);
							validValues.get(indx).setResponseCode(validValues.get(indx).getDisplayOrder());
							spCrfItem.setProCtcValidValue(validValues.get(indx));
						}
						if(spCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().equals(ProCtcQuestionType.PRESENT)){
							int indx = responseMap.get(ProCtcQuestionType.PRESENT);
							// Some Prsent/Absent questions have more than two valid values like "Prefer Not to Answer" etc.
							// Hence, find index of validValue in validValue list, having response as "Yes"
							if(indx == 1){
								indx = getValidValueWithYesResponse(validValues);
							}
							validValues.get(indx).setResponseCode(validValues.get(indx).getDisplayOrder());
							spCrfItem.setProCtcValidValue(validValues.get(indx));
						}
						if(spCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().equals(ProCtcQuestionType.AMOUNT)){
							int indx = responseMap.get(ProCtcQuestionType.AMOUNT);
							validValues.get(indx).setResponseCode(validValues.get(indx).getDisplayOrder());
							spCrfItem.setProCtcValidValue(validValues.get(indx));
						}
					}
					studyParticipantCrfSchedule.generateStudyParticipantCrfGrades(proctcaeGradeMappingVersion);
					for(int i=0; i<studyParticipantCrfSchedule.getStudyParticipantCrfGrades().size(); i++){
						Integer proCtcTermId = studyParticipantCrfSchedule.getStudyParticipantCrfGrades().get(i).getProCtcTerm().getId();
						String evaluatedGrade = studyParticipantCrfSchedule.getStudyParticipantCrfGrades().get(i).getGrade();
						assertEquals(expectedGrades.get(index), evaluatedGrade);
						//System.out.println((tempCount++) + ". CRF: " + studyParticipantCrf.getCrf() + " Expected Grade: " + expectedGrades.get(index) + " Generated Grade: " + evaluatedGrade);
					}
					index++;
				}
			}
			
		}
	}
	
	private Integer getValidValueWithYesResponse(List<ProCtcValidValue> validValues){
		int index = 0;
		for(ValidValue value : validValues){
			if(value.getValue(SupportedLanguageEnum.ENGLISH).equals("Yes")){
				return index;
			}
			index++;
		}
		return null;
	}
	
	
	public void populateInputResponseMapAndGrade(ProCtcTerm proCtcTerm, Map<Integer, Map<ProCtcQuestionType, Integer>> inputResponseMap, List<String> expectedGrades){
		List<ProctcaeGradeMapping> proctcGradeMappings = proCtcTerm.getProCtcGradeMappings();
		int index = 0;
		for(ProctcaeGradeMapping gradeMapping : proctcGradeMappings){
			Map<ProCtcQuestionType, Integer> responseMap = new HashMap<ProCtcQuestionType, Integer>();
			inputResponseMap.put(index++, responseMap);
			if(gradeMapping.getFrequency() != null){
				responseMap.put(ProCtcQuestionType.FREQUENCY, gradeMapping.getFrequency());
			}
			if(gradeMapping.getSeverity() != null){
				responseMap.put(ProCtcQuestionType.SEVERITY, gradeMapping.getSeverity());
			}
			if(gradeMapping.getInterference() != null){
				responseMap.put(ProCtcQuestionType.INTERFERENCE, gradeMapping.getInterference());
			}
			if(gradeMapping.getPresent_absent() != null){
				responseMap.put(ProCtcQuestionType.PRESENT, gradeMapping.getPresent_absent());
			}
			if(gradeMapping.getAmount() != null){
				responseMap.put(ProCtcQuestionType.AMOUNT, gradeMapping.getAmount());
			}
			
			expectedGrades.add(gradeMapping.getProCtcGrade());
		}
	}
	
	
	@Override
	protected void onTearDownInTransaction() throws Exception {
		super.onTearDownInTransaction();
		if(participant != null){
			for(StudyParticipantCrf studyParticipantCrf : participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs()){
				for(StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()){
					studyParticipantCrfSchedule.getStudyParticipantCrfGrades().clear();
				}
			}
		}
		commitAndStartNewTransaction();
		deleteAndCreateTestData();
	}

}
