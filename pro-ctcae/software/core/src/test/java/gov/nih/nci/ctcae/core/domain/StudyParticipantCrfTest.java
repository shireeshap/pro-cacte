package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.service.ParticipantScheduleService;
import gov.nih.nci.ctcae.core.domain.Question;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @since Dec 12, 2008
 */
public class StudyParticipantCrfTest extends TestDataManager {
    private StudyParticipantCrf studyParticipantCrf;

    public void testConstructor() {
        studyParticipantCrf = new StudyParticipantCrf();
        assertNull(studyParticipantCrf.getId());
        assertEquals(0, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
        assertNull(studyParticipantCrf.getCrf());
        assertNull(studyParticipantCrf.getStudyParticipantAssignment());

    }

    public void testGetterAndSetter() {
        studyParticipantCrf = new StudyParticipantCrf();

        CRF crf = Fixture.createCrf("test", CrfStatus.DRAFT, "1.0");
        crf.addCrfPage(new CRFPage());
        crf.addCrfPage(new CRFPage());

        studyParticipantCrf.setId(1);
        studyParticipantCrf.setCrf(crf);
        studyParticipantCrf.setStudyParticipantAssignment(new StudyParticipantAssignment());
        assertEquals(0, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
        assertEquals(new Integer(1), studyParticipantCrf.getId());
        assertEquals(crf, studyParticipantCrf.getCrf());
        assertNotNull(studyParticipantCrf.getStudyParticipantAssignment());

        studyParticipantCrf.addStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());
        assertEquals(1, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
    }

    public void testGetCrfByStatus() {
        Participant participant = ParticipantTestHelper.getDefaultParticipant();
        StudyParticipantCrf studyParticipantCrf = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        studyParticipantCrf = genericRepository.findById(StudyParticipantCrf.class, studyParticipantCrf.getId());
        List<StudyParticipantCrfSchedule> schedulesS = studyParticipantCrf.getStudyParticipantCrfSchedulesByStatus(CrfStatus.SCHEDULED);
        List<StudyParticipantCrfSchedule> schedulesC = studyParticipantCrf.getStudyParticipantCrfSchedulesByStatus(CrfStatus.COMPLETED);
        assertNotNull(schedulesS);
        assertNotNull(schedulesC);
        assertEquals(5, schedulesC.size());
        assertEquals(10, schedulesS.size());
    }
    
    public void testPutOnHold() throws Exception{
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	List<StudyParticipantCrfSchedule> studyParticipantCrfSchedules = studyParticipantCrf.getStudyParticipantCrfSchedules();
    	//assert schedules 0 to 4 to be having COMPLETED status, before putting ON-Hold
    	for(int i=0; i<5; i++){
    		assertTrue(studyParticipantCrfSchedules.get(i).getStatus().equals(CrfStatus.COMPLETED));
    	}
    	//assert schedules 5 to 8 to be having SCHEDULED status, before putting ON-Hold
    	for(int i=5; i<9; i++){
    		assertTrue(studyParticipantCrfSchedules.get(i).getStatus().equals(CrfStatus.SCHEDULED));
    	}
    	//set crfStatus of schedules 9 to 12 to be IN-PROGRESS, before putting ON-Hold
    	for(int i=9; i<13; i++){
    		studyParticipantCrfSchedules.get(i).setStatus(CrfStatus.INPROGRESS);
    	}
    	
    	
    	//Put the crfSchedules On-Hold on a date corresponding to yesterday.
    	studyParticipantCrf.putOnHold(DateUtils.addDaysToDate(new Date(), -1));
    	//Expect schedules 0 to 4 to be having COMPLETED status, after putting COMPLETED
    	for(int i=0; i<5; i++){
    		assertTrue(studyParticipantCrfSchedules.get(i).getStatus().equals(CrfStatus.COMPLETED));
    	}
    	//Expect schedules 5 to 8 to be having SCHEDULED status, after putting ON-Hold
    	for(int i=5; i<9; i++){
    		assertTrue(studyParticipantCrfSchedules.get(i).getStatus().equals(CrfStatus.ONHOLD));
    	}
    	//Expect schedules 9 to 12 to be having SCHEDULED, after putting ON-Hold
    	for(int i=9; i<13; i++){
    		studyParticipantCrfSchedules.get(i).setStatus(CrfStatus.ONHOLD);
    	}
    	
    	commitAndStartNewTransaction();
    	deleteTestData();
    	createTestData();
    }
    
    public void testRemoveCrfSchedule() throws ParseException{
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	ParticipantScheduleService participantScheduleService = new ParticipantScheduleService();
    	ParticipantSchedule participantSchedule = new ParticipantSchedule();
    	participantScheduleService.setStudyParticipantCrfScheduleRepository(TestDataManager.studyParticipantCrfScheduleRepository);
    	participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
    	List<String> formIds = new ArrayList<String>();
    	formIds.add(studyParticipantCrf.getCrf().getId().toString());
    	Date scheduleDate = DateUtils.addDaysToDate(new Date(), 15);
    	Calendar c = Calendar.getInstance();
    	
    	assertNull(getSchedule(scheduleDate, formIds, studyParticipantCrf));
    	
    	c.setTime(scheduleDate);
    	participantScheduleService.createAndSaveSchedules(c, null, -1, -1, formIds, false, false, participantSchedule);
    	StudyParticipantCrfSchedule crfSchedule = getSchedule(scheduleDate, formIds, studyParticipantCrf);
    	assertNotNull(crfSchedule);
    	
    	studyParticipantCrf.removeCrfSchedule(crfSchedule);
    	assertNull(getSchedule(scheduleDate, formIds, studyParticipantCrf));
    }
    
    public void testRemoveScheduledSpCrfSchedules() throws Exception{
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	studyParticipantCrf.removeScheduledSpCrfSchedules();
		// Out of total 15 scheduled surveys expect, 10 surverys to be deleted
		// and 5 surveys having CrfStatus as COMPLETED not to be deleted.
		assert (studyParticipantCrf.getStudyParticipantCrfSchedules().size() == 5);
    }
    
    public void testStudyParticipantCrfAddedQuestion(){
    	StudyParticipantCrf studyParticipantCrf = getDefaultStudyParticipantCrf();
    	studyParticipantCrf.addStudyParticipantCrfAddedQuestion(getProCtcQuestion(), studyParticipantCrf.getCrf().getCrfPages().size());
    	
    	List<StudyParticipantCrfAddedQuestion> studyParticipantCrfAddedQuestion = studyParticipantCrf.getStudyParticipantCrfAddedQuestions();
    	StudyParticipantCrfAddedQuestion firstAddedQuestion = studyParticipantCrfAddedQuestion.get(0);
    	studyParticipantCrf.removeStudyParticipantCrfAddedQuestion(firstAddedQuestion);
    	assertFalse(studyParticipantCrf.getStudyParticipantCrfAddedQuestions().contains(firstAddedQuestion));
    }
    
    
    public Question getProCtcQuestion(){
    	CtcTerm ctcTerm = new CtcTerm();
        ctcTerm.setTerm("ctc", SupportedLanguageEnum.ENGLISH);
        ProCtcTerm proCtcTerm = new ProCtcTerm();
        proCtcTerm.setTermEnglish("Fatigue", SupportedLanguageEnum.ENGLISH);
        proCtcTerm.setCtcTerm(ctcTerm);
        ProCtcQuestion proCtaddedQuestion = new ProCtcQuestion();
        proCtaddedQuestion.setQuestionText("first question", SupportedLanguageEnum.ENGLISH);
        proCtaddedQuestion.setId(1);
        proCtcTerm.addProCtcQuestion(proCtaddedQuestion);
        return proCtaddedQuestion;
    }
    
    public StudyParticipantCrf getDefaultStudyParticipantCrf(){
    	return StudyTestHelper.getDefaultStudy().getArms().get(0).getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
    }
    
    public StudyParticipantCrfSchedule getSchedule(Date findDate, List<String> formIds, StudyParticipantCrf studyParticipantCrf) throws ParseException{
    	 
    	if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
		        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
		            if(findDate.equals(studyParticipantCrfSchedule.getStartDate()) && studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)){
		            	return studyParticipantCrfSchedule;
		            }
		        }
		  }
    	return null;
    }
    
}