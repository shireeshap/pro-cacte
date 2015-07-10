package gov.nih.nci.ctcae.core.ivrs;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

/**
 * @author Suneel Allareddy
 * @since Jan 11, 2011
 */

public class IVRSApiTest extends TestDataManager{
    Participant participant;
    IVRSApiTestHelper helper;
    DataSource datasource;
    
    public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	@Override
    protected void onSetUpInTransaction() throws Exception {
    	System.out.println("Starting onSetUpInTransaction in IVRSApiTest");
        super.onSetUpInTransaction();
        helper = new IVRSApiTestHelper();
        jdbcTemplate.execute("delete from ivrs_sch_core_sym_count");
        deleteIVRSTestData();
        saveIVRSParticipant();
        System.out.println("Ending onSetUpInTransaction in IVRSApiTest");
    }
    
    private void saveIVRSParticipant() {
        //create IVRS test Form and Study,ready the schedule form
    	System.out.println("Starting saveIVRSParticipant");
        try{
            Study study = StudyTestHelper.createIVRSStudy();
            CrfTestHelper.createIVRSTestForm(study,this);
            //stored functions need the pro_ctc_questions to have file_names
            helper.ivrsUpdateFileNames(datasource);
        }catch(Exception e){
            e.printStackTrace();
        }
        commitAndStartNewTransaction();
        
        ParticipantQuery pq = new ParticipantQuery(false);
        pq.filterByUsername("ivrs.participant");
        participant = genericRepository.findSingle(pq);
        
        System.out.println("p.spa: "+participant.getStudyParticipantAssignments().size());
        System.out.println("p.spa.spcrf: "+participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().size());
        System.out.println("Ending saveIVRSParticipant");
    }
    
    private void deleteIVRSTestData() {
    	System.out.println("Starting deleteIVRSTestData");
        ParticipantQuery pq = new ParticipantQuery(false);
        pq.filterByUsername("ivrs.participant");
        genericRepository.delete(genericRepository.findSingle(pq));
        UserQuery uq = new UserQuery();
        uq.filterByUserName("ivrs.participant");
        genericRepository.delete(genericRepository.findSingle(uq));
        CrfTestHelper.deleteIVRSTestForm();
        StudyTestHelper.deleteIVRSStudy();
        commitAndStartNewTransaction();
        System.out.println("Ending deleteIVRSTestData");
    }
    
    public void testIVRSApi(){
        ParticipantQuery pq = new ParticipantQuery(true);
        pq.filterByUsername("ivrs.participant");
        participant = genericRepository.findSingle(pq);
    
        helper.setDataSource(jdbcTemplate.getDataSource());
        
        Integer userId = helper.ivrsLogin("1201201200",1234);
        //checking the user information correct
        assertEquals(participant.getUser().getId(),userId);
        //assertEquals(participant.getUser().getId(),userId);

        Integer isUserNew = helper.ivrsIsUserNew(participant.getUser().getId());
        assertEquals(1,isUserNew.intValue());
        //passing wrong information
        Integer userId1 = helper.ivrsLogin("1201201200",9999);
        assertEquals(0,userId1.intValue());
        Date currentDate = new Date();
        int formsCount = 0;
        Integer currentScheduleId = null;
        StudyParticipantCrfSchedule currentSchedule=null;
        List<StudyParticipantCrfSchedule> studyParticipantSchedules = participant.getStudyParticipantAssignments().get(0).
                                                                       getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules();
        for (StudyParticipantCrfSchedule sched:studyParticipantSchedules){
            if(sched.getStatus().equals(CrfStatus.SCHEDULED) && sched.getStartDate().getTime()<=currentDate.getTime()){
                currentSchedule  = sched;
                currentScheduleId = sched.getId();
                    formsCount++;
            }
        }
        assertEquals(1,formsCount);

        int numberOfForms = helper.ivrsNumberOfForms(participant.getUser().getId());
        assertEquals(numberOfForms,formsCount);

        Integer schedFormId = helper.ivrsGetForm(participant.getUser().getId(),numberOfForms);
        assertEquals(currentSchedule.getId(),schedFormId);
        // get the form title
        String formTitle = helper.ivrsGetformtitle(participant.getUser().getId(),schedFormId);
        assertEquals(currentSchedule.getStudyParticipantCrf().getCrf().getTitle(),formTitle);

        //check for the recall period
        assertEquals(1,helper.ivrsGetFormRecallPeriod(participant.getUser().getId(),schedFormId).intValue());
        // get the first question for the selected form
        String fistQuestionIdCategory = helper.ivrsGetFirstQuestion(participant.getUser().getId(),schedFormId); //8_1
        // split the returned text
        String splitText[]=fistQuestionIdCategory.split("_");
        Integer fistQuestionId = Integer.parseInt(splitText[0]);
        Integer questionCategory = Integer.parseInt(splitText[1]);

        assertEquals(fistQuestionId,currentSchedule.getStudyParticipantCrfItems().get(0).getCrfPageItem().getProCtcQuestion().getId());


        assertEquals("-2",helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,fistQuestionId,questionCategory));

        String firstQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,fistQuestionId);

        assertEquals(firstQuestionText,currentSchedule.getStudyParticipantCrfItems().get(0).getCrfPageItem().getProCtcQuestion().getQuestionText(SupportedLanguageEnum.ENGLISH));
        String firstQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,fistQuestionId);
        assertEquals(firstQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(0).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().name().toLowerCase()+"_1");

        //skipping next 2 questions as answering the first question with none as answer,checking the skipping questions
        //So next question id will be 4th one
        String fourthQuestionIdCategory = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,fistQuestionId,0,questionCategory);
        splitText=fourthQuestionIdCategory.split("_");
        Integer fourthQuestionId = Integer.parseInt(splitText[0]);
        questionCategory = Integer.parseInt(splitText[1]);
        
        assertEquals(fourthQuestionId,currentSchedule.getStudyParticipantCrfItems().get(3).getCrfPageItem().getProCtcQuestion().getId());

        isUserNew = helper.ivrsIsUserNew(participant.getUser().getId());
        assertEquals(0,isUserNew.intValue());
        assertEquals(fistQuestionIdCategory,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,fourthQuestionId,questionCategory));
        assertEquals(0,helper.ivrsGetQuestionAnswer(participant.getUser().getId(),schedFormId,fistQuestionId,questionCategory).intValue());

        String fourthQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,fourthQuestionId);
        assertEquals(fourthQuestionText,currentSchedule.getStudyParticipantCrfItems().get(3).getCrfPageItem().getProCtcQuestion().getQuestionText(SupportedLanguageEnum.ENGLISH));
        String fourthQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,fourthQuestionId);
        assertEquals(fourthQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(3).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().name().toLowerCase()+"_1");

        //Answer the fourth question such a way that next 2 questions will come
        String fifthQuestionIdCategory = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,fourthQuestionId,3,questionCategory);
        splitText=fifthQuestionIdCategory.split("_");
        Integer fifthQuestionId = Integer.parseInt(splitText[0]);
        questionCategory= Integer.parseInt(splitText[1]);

        assertEquals(fifthQuestionId,currentSchedule.getStudyParticipantCrfItems().get(4).getCrfPageItem().getProCtcQuestion().getId());
        String fifthQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,fifthQuestionId);
        assertEquals(fifthQuestionText,currentSchedule.getStudyParticipantCrfItems().get(4).getCrfPageItem().getProCtcQuestion().getQuestionText(SupportedLanguageEnum.ENGLISH));
        String fifthQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,fifthQuestionId);
        assertEquals(fifthQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(4).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().name().toLowerCase()+"_1");

        assertEquals(fourthQuestionIdCategory,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,fifthQuestionId,questionCategory));
        assertEquals(3,helper.ivrsGetQuestionAnswer(participant.getUser().getId(),schedFormId,fourthQuestionId,questionCategory).intValue());

        String sixthQuestionIdCategory = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,fifthQuestionId,3,questionCategory);
        splitText=sixthQuestionIdCategory.split("_");
        Integer sixthQuestionId=Integer.parseInt(splitText[0]);
        questionCategory = Integer.parseInt(splitText[1]);

        assertEquals(sixthQuestionId,currentSchedule.getStudyParticipantCrfItems().get(5).getCrfPageItem().getProCtcQuestion().getId());
        String sixthQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,sixthQuestionId);
        assertEquals(sixthQuestionText,currentSchedule.getStudyParticipantCrfItems().get(5).getCrfPageItem().getProCtcQuestion().getQuestionText(SupportedLanguageEnum.ENGLISH));
        String sixthQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,sixthQuestionId);
        assertEquals(sixthQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(5).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().name().toLowerCase()+"_1");

        assertEquals(fifthQuestionIdCategory,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,sixthQuestionId,1));
        assertEquals(3,helper.ivrsGetQuestionAnswer(participant.getUser().getId(),schedFormId,fifthQuestionId,questionCategory).intValue());
        String nextQuestionIdCategory = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,sixthQuestionId,1,questionCategory);
        Integer nextQuestionId=0;
        if (!nextQuestionIdCategory.equals("0")){
            splitText=nextQuestionIdCategory.split("_");
            nextQuestionId=Integer.parseInt(splitText[0]);
            questionCategory =Integer.parseInt(splitText[1]);
        }
       assertEquals(nextQuestionId.intValue(),0);

       assertEquals(sixthQuestionIdCategory,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,0,questionCategory));

        //committed the total session
       Integer result = helper.ivrsCommitSession(participant.getUser().getId(),schedFormId,participant.getPinNumber());
       commitTransaction(currentSchedule);
       commitAndStartNewTransaction();
       assertEquals(result.intValue(),1);
       currentSchedule = studyParticipantCrfScheduleRepository.findById(currentScheduleId);
       assertEquals(CrfStatus.COMPLETED,currentSchedule.getStatus());
       assertEquals(AppMode.IVRS,currentSchedule.getFormSubmissionMode());
       assertNotNull(currentSchedule.getStudyParticipantCrfScheduleNotification());
       assertNotNull(currentSchedule.getStudyParticipantCrfScheduleNotification().getId());
       assertNull(currentSchedule.getStudyParticipantCrfScheduleNotification().getCompletionDate());
       assertNotNull(currentSchedule.getStudyParticipantCrfScheduleNotification().getCreationDate());
       assertEquals(CrfStatus.SCHEDULED,currentSchedule.getStudyParticipantCrfScheduleNotification().getStatus());
       assertEquals(false,currentSchedule.getStudyParticipantCrfScheduleNotification().isMailSent());

        isUserNew = helper.ivrsIsUserNew(participant.getUser().getId());
        assertEquals(0,isUserNew.intValue());
    }
    
    private void commitTransaction(StudyParticipantCrfSchedule studyParticipantCrfSchedule){
    	jdbcTemplate.execute("commit");
        genericRepository.save(studyParticipantCrfSchedule);
    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
    	// TODO Auto-generated method stub
    	super.onTearDownInTransaction();
        deleteIVRSTestData();

    }

}