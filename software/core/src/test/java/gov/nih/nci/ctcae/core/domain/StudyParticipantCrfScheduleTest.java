package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.csv.loader.ProctcaeGradeMappingsLoader;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author Harsh Agarwal
 * @since Dec 12, 2008
 */
public class StudyParticipantCrfScheduleTest extends TestDataManager {
    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private ProctcaeGradeMappingVersion proctcaeGradeMappingVersion;
    private Map<Integer, String> ctcaeGradeMap;
    
    @Override
    protected void onSetUpInTransaction() throws Exception {
    	super.onSetUpInTransaction();
    	proctcaeGradeMappingVersion = getDefaultProctcaeGradeMappingVersion();
    	ProctcaeGradeMappingsLoader proctcaeGradeMappingsLoader = new ProctcaeGradeMappingsLoader();
    	proctcaeGradeMappingsLoader.setGenericRepository(genericRepository);
    	proctcaeGradeMappingsLoader.setProCtcTermRepository(proCtcTermRepository);
    	proctcaeGradeMappingsLoader.loadProctcaeGradeMappings();
    }
    
    public void testConstructor() {
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        assertEquals(0, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());
        assertEquals(0, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());
        assertNull(studyParticipantCrfSchedule.getId());
        assertNull(studyParticipantCrfSchedule.getDueDate());
        assertNull(studyParticipantCrfSchedule.getStartDate());
        assertEquals(CrfStatus.SCHEDULED, studyParticipantCrfSchedule.getStatus());
        assertNull(studyParticipantCrfSchedule.getStudyParticipantCrf());
    }

    public void testGetterAndSetter() {
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        Date d = new Date();
        studyParticipantCrfSchedule.setId(1);
        StudyParticipantCrf crf = new StudyParticipantCrf();
        crf.setStartDate(new Date());
        studyParticipantCrfSchedule.setStudyParticipantCrf(crf);
        studyParticipantCrfSchedule.setDueDate(d);
        studyParticipantCrfSchedule.setStartDate(d);
        studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(null);

        assertEquals(0, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());
        assertEquals(d, studyParticipantCrfSchedule.getDueDate());
        assertEquals(d, studyParticipantCrfSchedule.getStartDate());
        assertEquals(CrfStatus.INPROGRESS, studyParticipantCrfSchedule.getStatus());
        assertNotNull(studyParticipantCrfSchedule.getStudyParticipantCrf());

        studyParticipantCrfSchedule.addStudyParticipantCrfItem(new StudyParticipantCrfItem());
        assertEquals(1, studyParticipantCrfSchedule.getStudyParticipantCrfItems().size());
    }

    public void testEqualsHashCode() {
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        StudyParticipantCrfSchedule studyParticipantCrfSchedule2 = new StudyParticipantCrfSchedule();
        Date d = new Date();
        StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setStartDate(new Date());
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
        studyParticipantCrfSchedule2.setStudyParticipantCrf(studyParticipantCrf);

        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);

        studyParticipantCrfSchedule.setDueDate(d);
        assertFalse(studyParticipantCrfSchedule.equals(studyParticipantCrfSchedule2));
        studyParticipantCrfSchedule2.setDueDate(d);
        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


        studyParticipantCrfSchedule.setStartDate(d);
        assertFalse(studyParticipantCrfSchedule.equals(studyParticipantCrfSchedule2));
        studyParticipantCrfSchedule2.setStartDate(d);
        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);

        studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        assertFalse(studyParticipantCrfSchedule.equals(studyParticipantCrfSchedule2));
        studyParticipantCrfSchedule2.setStatus(CrfStatus.INPROGRESS);
        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


        assertEquals(studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals(studyParticipantCrfSchedule, studyParticipantCrfSchedule2);


        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem);
        assertEquals("must not consider crf item", studyParticipantCrfSchedule.hashCode(), studyParticipantCrfSchedule2.hashCode());
        assertEquals("must not consider crf item", studyParticipantCrfSchedule, studyParticipantCrfSchedule2);
    }
    
    public void testAddParticipantAddedQuestions(){
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	StudyParticipantCrfAddedQuestion spcrf_addedQuestion = studyParticipantCrf.addStudyParticipantCrfAddedQuestion(getProCtcQuestionFromRepository().get(11), studyParticipantCrf.getCrf().getCrfPages().size());
    	genericRepository.save(spcrf_addedQuestion);
    	
    	StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
    	
    	studyParticipantCrfSchedule.addParticipantAddedQuestions();
    	genericRepository.save(studyParticipantCrfSchedule);
    	StudyParticipantCrfScheduleAddedQuestion studyParticipantCrSchedulefAddedQuestion_spcrfs = studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions().get(0);
    	StudyParticipantCrfScheduleAddedQuestion fetchedAddedQuestion = fetchAddedQuestionFromRepository().get(0);
    	assertTrue(fetchedAddedQuestion.equals(studyParticipantCrSchedulefAddedQuestion_spcrfs));
    	assertEquals(fetchedAddedQuestion.hashCode(), studyParticipantCrSchedulefAddedQuestion_spcrfs.hashCode());
    }
   
    public void testGetParticipantAddedSymptoms() throws Exception{
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	studyParticipantCrf.addStudyParticipantCrfAddedQuestion(getProCtcQuestion(), studyParticipantCrf.getCrf().getCrfPages().size());
    	StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
    	studyParticipantCrfSchedule.addParticipantAddedQuestions();
    	
    	Set symptoms = studyParticipantCrfSchedule.getParticipantAddedSymptoms();
    	assertTrue(symptoms.contains("Fatigue, tiredness, or lack of energy"));
    }
    
    public void testGetDisplayRules(){
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
    	Hashtable<Integer, String> displayRules = studyParticipantCrfSchedule.getDisplayRules();
    	List<CrfPageItem> crfPageItems = getCrfPageItemsForCrf(studyParticipantCrf.getCrf().getId());
    	for(CrfPageItem cpi: crfPageItems){
    		assert(displayRules.containsKey(cpi.getId()));
    	}
    }
    
    public void testGenerateStudyParticipantCrfGradesForNonEmpty(){
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
    	
    	Random random = new Random();
    	for(StudyParticipantCrfItem spCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()){
    		 List<ProCtcValidValue> validValues = (List<ProCtcValidValue>) spCrfItem.getCrfPageItem().getProCtcQuestion().getValidValues();
    		 spCrfItem.setProCtcValidValue(validValues.get(random.nextInt(validValues.size())));
    	}
    	studyParticipantCrfSchedule.generateStudyParticipantCrfGrades(proctcaeGradeMappingVersion);
    	assertFalse(studyParticipantCrfSchedule.getStudyParticipantCrfGrades().isEmpty());
    }
    
    @Override
    protected void onTearDownInTransaction() throws Exception {
    	super.onTearDownInTransaction();
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
    	studyParticipantCrfSchedule.getStudyParticipantCrfGrades().clear();
    }
    
    public Question getProCtcQuestion(){
        ProCtcQuestionQuery query = new ProCtcQuestionQuery();
        query.filterByTerm("Fatigue, tiredness, or lack of energy");
        ProCtcQuestion proCtcQuestion = genericRepository.findSingle(query);
        return proCtcQuestion;
    }
    
    
    public List<StudyParticipantCrfScheduleAddedQuestion> fetchAddedQuestionFromRepository(){
    	return hibernateTemplate.find("select spcrfs_addedQs from StudyParticipantCrfScheduleAddedQuestion spcrfs_addedQs");
    }
    
    public List<CrfPageItem> getCrfPageItemsForCrf(Integer id){
    	return hibernateTemplate.find("select cpi from CrfPageItem cpi left join cpi.crfPage as cp where cp.crf.id =?", new Object[]{id});
    }
    
    public List<ProCtcQuestion> getProCtcQuestionFromRepository(){
    	return hibernateTemplate.find("from ProCtcQuestion");
    }
    
    public StudyParticipantCrf getDefaultStudyParticipantCrf(){
    	return StudyTestHelper.getDefaultStudy().getArms().get(0).getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
    }

}