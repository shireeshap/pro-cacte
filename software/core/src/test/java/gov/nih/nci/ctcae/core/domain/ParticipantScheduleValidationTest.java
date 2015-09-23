package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;
import junit.framework.TestCase;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Suneel Allareddy
 * Date: 2/4/11
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantScheduleValidationTest extends TestDataManager {
    Participant participant;
    private ParticipantSchedule participantSchedule;
    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        deleteScheduleTestData();
        commitAndStartNewTransaction();
        saveTestScheduleParticipant();
    }
    
    @Override
    protected void onTearDownInTransaction() throws Exception {
    	super.onTearDownInTransaction();
    }
    
    private void saveTestScheduleParticipant() {
        try{
            Study study = StudyTestHelper.createIVRSStudy();
            commitAndStartNewTransaction();
            CrfTestHelper.createIVRSTestForm(study, this);
        }catch(Exception e){
            e.printStackTrace();
        }
        commitAndStartNewTransaction();
    }
    
    private void deleteScheduleTestData() {
        //delete existing participant
        ParticipantQuery pq = new ParticipantQuery(false);
        pq.filterByUsername("ivrs.participant");
        genericRepository.delete(genericRepository.findSingle(pq));
        UserQuery uq = new UserQuery();
        uq.filterByUserName("ivrs.participant");
        genericRepository.delete(genericRepository.findSingle(uq));
        CrfTestHelper.deleteIVRSTestForm();
        StudyTestHelper.deleteIVRSStudy();
        commitAndStartNewTransaction();
    }
    
    public void testGetDueDateForFormSchedule() throws Exception {
        ParticipantQuery pq = new ParticipantQuery(false);
        pq.filterByUsername("ivrs.participant");
        participant = genericRepository.findSingle(pq);

        participantSchedule=new ParticipantSchedule();
        Calendar cal = new GregorianCalendar();
        Calendar cal2 = (Calendar)cal.clone();
        cal.setTime(new Date());
        
        StudyParticipantCrf spCrf = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        Date dueDate = participantSchedule.getDueDateForFormSchedule(cal, spCrf);
        cal.add(Calendar.DATE,1);
        cal.add(Calendar.HOUR, -1);
        assertEquals(dueDate, cal.getTime());

//        CRFQuery query = new CRFQuery();
//        query.filterByTitleExactMatch("IVRSForm");
//        CRF crf = crfRepository.findSingle(query);
//        crf.getFormArmSchedules().get(0).getCrfCalendars().clear();
//
//        Date dueDateNew = participantSchedule.getDueDateForFormSchedule(cal2, spCrf);
//        cal2.add(Calendar.DATE, 4);
//        assertEquals(dueDateNew,cal2.getTime());
    }

     public void testGetReschedulePastDueForms() throws Exception {
        ParticipantQuery pq = new ParticipantQuery(false);
        pq.filterByUsername("ivrs.participant");
        participant = genericRepository.findSingle(pq);
        participantSchedule=new ParticipantSchedule();

        List<StudyParticipantCrfSchedule> studyParticipantSchedules = participant.getStudyParticipantAssignments().get(0).
                                                                       getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules();
        StudyParticipantCrfSchedule  studyParticipantCrfSchedule = studyParticipantSchedules.get(0);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrfSchedule.getStudyParticipantCrf());
        Calendar cal = new GregorianCalendar();
        cal.setTime(studyParticipantCrfSchedule.getStartDate());
        Calendar newCalender = new GregorianCalendar();
        newCalender.setTime(studyParticipantCrfSchedule.getStartDate());
        CRFQuery query = new CRFQuery();
        query.filterByTitleExactMatch("IVRSForm");
        CRF crf = crfRepository.findSingle(query);
        int offset = DateUtils.daysBetweenDates(studyParticipantCrfSchedule.getStartDate(),studyParticipantCrfSchedule.getDueDate());
        if(offset>0){
        	newCalender.add(Calendar.DATE, -offset);
        }else{
        	newCalender.add(Calendar.DATE, -5);
        }
        	
        List<String>  formids = new ArrayList<String>();
        formids.add(crf.getId().toString());

        List<String> pastdueForms = participantSchedule.getReschedulePastDueForms(cal, newCalender,formids);
         assertEquals(1,pastdueForms.size());
         assertEquals(crf.getTitle(),pastdueForms.get(0));
        newCalender.setTime(studyParticipantCrfSchedule.getStartDate());
        if(offset>0){
        	newCalender.add(Calendar.DATE, offset);
        }else{
        	newCalender.add(Calendar.DATE, 5);
        }
        List<String> noPastDueForms = participantSchedule.getReschedulePastDueForms(cal, newCalender,formids);
        assertEquals(0,noPastDueForms.size());
        deleteScheduleTestData();
    }

    public void testUpdateSchedule() throws Exception {
        ParticipantQuery pq = new ParticipantQuery(false);
        pq.filterByUsername("ivrs.participant");
        participant = genericRepository.findSingle(pq);

        participantSchedule=new ParticipantSchedule();
        List<StudyParticipantCrfSchedule> studyParticipantSchedules = participant.getStudyParticipantAssignments().get(0).
                                                                       getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules();
        StudyParticipantCrfSchedule  studyParticipantCrfSchedule = studyParticipantSchedules.get(0);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrfSchedule.getStudyParticipantCrf());

        Calendar cal = new GregorianCalendar();
        cal.setTime(studyParticipantCrfSchedule.getStartDate());
        Calendar calNew = new GregorianCalendar();
        calNew.setTime(studyParticipantCrfSchedule.getStartDate());
        calNew.add(Calendar.DATE,-2);

        CRFQuery query = new CRFQuery();
        query.filterByTitleExactMatch("IVRSForm");
        CRF crf = crfRepository.findSingle(query);

        List<String>  formids = new ArrayList<String>();
        formids.add(crf.getId().toString());
        LinkedHashMap<String,List<String>> list = new LinkedHashMap<String,List<String>> ();
        participantSchedule.updateSchedule(cal,calNew,formids,list);

        assertEquals(list.size(),2);
        assertEquals(list.get("successForms").size(),1);
        assertEquals(list.get("successForms").get(0),crf.getTitle());
        assertEquals(list.get("failedForms").size(),0);

        StudyParticipantCrfSchedule  studyParticipantCrfSchedule1 = studyParticipantSchedules.get(1);

        StudyParticipantCrfSchedule  studyParticipantCrfSchedule2 = studyParticipantSchedules.get(2);
        cal.setTime(studyParticipantCrfSchedule2.getStartDate());
         calNew.setTime(studyParticipantCrfSchedule1.getStartDate());
        list = new LinkedHashMap<String,List<String>> ();
        participantSchedule.updateSchedule(cal,calNew,formids,list);

        assertEquals(list.size(),2);
        assertEquals(list.get("failedForms").size(),1);
        assertEquals(list.get("failedForms").get(0),crf.getTitle());
        assertEquals(list.get("successForms").size(),0);
    }


}
