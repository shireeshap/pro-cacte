package gov.nih.nci.ctcae.core.ivrs;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Suneel Allareddy
 * @since Jan 11, 2011
 */

public class IVRSApiTest extends TestDataManager{
    Participant participant;
    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        deleteIVRSTestData();
         saveIVRSParticipant();
    }
    private void saveIVRSParticipant() {
        //create IVRS test Form and Study, ready the schedule form
        try{
            Study study = StudyTestHelper.createIVRSStudy();

            CrfTestHelper.createIVRSTestForm(study,this);
        }catch(Exception e){
            e.printStackTrace();
        }
        commitAndStartNewTransaction();
        ParticipantQuery pq = new ParticipantQuery();
        pq.filterByUsername("ivrs.participant");
        participant = genericRepository.findSingle(pq);
    }
    private void deleteIVRSTestData() {
        //delete existing participant
        ParticipantQuery pq = new ParticipantQuery();
        pq.filterByUsername("ivrs.participant");
        genericRepository.delete(genericRepository.findSingle(pq));
        UserQuery uq = new UserQuery();
        uq.filterByUserName("ivrs.participant");
        genericRepository.delete(genericRepository.findSingle(uq));
        CrfTestHelper.deleteIVRSTestForm();
        StudyTestHelper.deleteIVRSStudy();         
        commitAndStartNewTransaction();
    }
    public void testIVRSApi(){
        //saveIVRSParticipant();
        ParticipantQuery pq = new ParticipantQuery();
        pq.filterByUsername("ivrs.participant");
        participant = genericRepository.findSingle(pq);
        
        IVRSApiTestHelper helper = new IVRSApiTestHelper();
        helper.setDataSource(jdbcTemplate.getDataSource());
        Integer userId = helper.ivrsLogin(1234,1234);
        //checking the user information correct
        assertEquals(participant.getUser().getId(),userId);
        assertEquals(participant.getUser().getId(),userId);

        Integer isUserNew = helper.ivrsIsUserNew(participant.getUser().getId());
        assertEquals(1,isUserNew.intValue());
        //passing wrong information
        Integer userId1 = helper.ivrsLogin(1234,9999);
        assertEquals(0,userId1.intValue());
        Date currentDate = new Date();
        int formsCount = 0;
        StudyParticipantCrfSchedule currentSchedule=null;
        List<StudyParticipantCrfSchedule> studyParticipantSchedules = participant.getStudyParticipantAssignments().get(0).
                                                                       getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules();
        for (StudyParticipantCrfSchedule sched:studyParticipantSchedules){
            if(sched.getStatus().equals(CrfStatus.SCHEDULED) && sched.getStartDate().getTime()<=currentDate.getTime()){
                currentSchedule  = sched;
                    formsCount++;
            }
        }
        assertEquals(1,formsCount);

        int numberOfForms = helper.ivrsNumberOfForms(participant.getUser().getId());
        assertEquals(numberOfForms,formsCount);

        Integer schedFormId = helper.ivrsGetForm(participant.getUser().getId(),numberOfForms);
        assertEquals(currentSchedule.getId(),schedFormId);

        //check for the recall period
        assertEquals(1,helper.ivrsGetFormRecallPeriod(participant.getUser().getId(),schedFormId).intValue());
        Integer fistQuestionId = helper.ivrsGetFirstQuestion(participant.getUser().getId(),schedFormId);

        assertEquals(fistQuestionId,currentSchedule.getStudyParticipantCrfItems().get(0).getCrfPageItem().getId());


        assertEquals(-2,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,fistQuestionId).intValue());

        String firstQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,fistQuestionId);
        assertEquals(firstQuestionText,currentSchedule.getStudyParticipantCrfItems().get(0).getCrfPageItem().getProCtcQuestion().getQuestionText());
        String firstQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,fistQuestionId);
        assertEquals(firstQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(0).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().getCode().toLowerCase());

        //skipping next 2 questions as answering the first question with none as answer,checking the skipping questions
        //So next question id will be 4th one
        Integer fourthQuestionId = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,fistQuestionId,1);
        assertEquals(fourthQuestionId,currentSchedule.getStudyParticipantCrfItems().get(3).getCrfPageItem().getId());

        isUserNew = helper.ivrsIsUserNew(participant.getUser().getId());
        assertEquals(0,isUserNew.intValue());

        assertEquals(fistQuestionId,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,fourthQuestionId));
        assertEquals(1,helper.ivrsGetQuestionAnswer(participant.getUser().getId(),schedFormId,fistQuestionId).intValue());

        String fourthQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,fourthQuestionId);
        assertEquals(fourthQuestionText,currentSchedule.getStudyParticipantCrfItems().get(3).getCrfPageItem().getProCtcQuestion().getQuestionText());
        String fourthQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,fourthQuestionId);
        assertEquals(fourthQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(3).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().getCode().toLowerCase());

        //Answer the fourth question such a way that next 2 questions will come
        Integer fifthQuestionId = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,fourthQuestionId,3);
        assertEquals(fifthQuestionId,currentSchedule.getStudyParticipantCrfItems().get(4).getCrfPageItem().getId());
        String fifthQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,fifthQuestionId);
        assertEquals(fifthQuestionText,currentSchedule.getStudyParticipantCrfItems().get(4).getCrfPageItem().getProCtcQuestion().getQuestionText());
        String fifthQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,fifthQuestionId);
        assertEquals(fifthQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(4).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().getCode().toLowerCase());

         assertEquals(fourthQuestionId,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,fifthQuestionId));
        assertEquals(3,helper.ivrsGetQuestionAnswer(participant.getUser().getId(),schedFormId,fourthQuestionId).intValue());

        Integer sixthQuestionId = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,fifthQuestionId,3);
        assertEquals(sixthQuestionId,currentSchedule.getStudyParticipantCrfItems().get(5).getCrfPageItem().getId());
        String sixthQuestionText =  helper.ivrsGetQuestionText(participant.getUser().getId(),schedFormId,sixthQuestionId);
        assertEquals(sixthQuestionText,currentSchedule.getStudyParticipantCrfItems().get(5).getCrfPageItem().getProCtcQuestion().getQuestionText());
        String sixthQuestionType =  helper.ivrsGetQuestionType(participant.getUser().getId(),schedFormId,sixthQuestionId);
        assertEquals(sixthQuestionType.toLowerCase(),currentSchedule.getStudyParticipantCrfItems().get(5).getCrfPageItem().getProCtcQuestion().getProCtcQuestionType().getCode().toLowerCase());

        assertEquals(fifthQuestionId,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,sixthQuestionId));
        assertEquals(3,helper.ivrsGetQuestionAnswer(participant.getUser().getId(),schedFormId,fifthQuestionId).intValue());

       Integer nextQuestionId = helper.ivrsGetAnswerQuestion(participant.getUser().getId(),schedFormId,sixthQuestionId,3);
       assertEquals(nextQuestionId.intValue(),0);

       assertEquals(sixthQuestionId,helper.ivrsGetPreviousQuestion(participant.getUser().getId(),schedFormId,0));

       //committed the total session 
       Integer result = helper.ivrsCommitSession(participant.getUser().getId(),schedFormId,participant.getPinNumber());
       assertEquals(result.intValue(),1);
        commitAndStartNewTransaction();
        StudyParticipantCrfSchedule finalSchedule = studyParticipantCrfScheduleRepository.findById(currentSchedule.getId());
        assertEquals(CrfStatus.COMPLETED,finalSchedule.getStatus());
        assertEquals(AppMode.IVRS,finalSchedule.getFormSubmissionMode());
        assertNotNull(finalSchedule.getStudyParticipantCrfScheduleNotification());
        assertNotNull(finalSchedule.getStudyParticipantCrfScheduleNotification().getId());
        assertNull(finalSchedule.getStudyParticipantCrfScheduleNotification().getCompletionDate());
        assertNotNull(finalSchedule.getStudyParticipantCrfScheduleNotification().getCreationDate());
        assertEquals(CrfStatus.SCHEDULED,finalSchedule.getStudyParticipantCrfScheduleNotification().getStatus());
        assertEquals(false,finalSchedule.getStudyParticipantCrfScheduleNotification().isMailSent());

        isUserNew = helper.ivrsIsUserNew(participant.getUser().getId());
        assertEquals(0,isUserNew.intValue());
        //deleteIVRSTestData();

    }


}