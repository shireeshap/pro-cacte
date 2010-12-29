package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Suneel Allareddy
 * @since Dec 20, 2010
 */

public class StudyParticipantReportingModeHistoryIntegrationTest extends TestDataManager{
    private StudyParticipantReportingModeHistory studyParticipantReportingModeHistory;
    private Participant participant;


    private void saveParticipant() {
        ParticipantQuery pq = new ParticipantQuery();
        pq.filterByUsername("1");
        genericRepository.delete(genericRepository.findSingle(pq));
        UserQuery uq = new UserQuery();
        uq.filterByUserName("1");
        genericRepository.delete(genericRepository.findSingle(uq));
        commitAndStartNewTransaction();
        participant = Fixture.createParticipant("John", "Dow", "1234");
        participant.getUser().setUsername("1");
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(StudyTestHelper.getDefaultStudy().getLeadStudySite());
        studyParticipantAssignment.setStudyParticipantIdentifier("abc");
        studyParticipantAssignment.setArm(StudyTestHelper.getDefaultStudy().getArms().get(0));
        StudyParticipantReportingModeHistory studyItem = new StudyParticipantReportingModeHistory();
        studyItem.setMode(AppMode.HOMEBOOKLET);
        studyParticipantAssignment.addStudyParticipantModeHistory(studyItem);

        StudyParticipantReportingModeHistory studyItem1 = new StudyParticipantReportingModeHistory();
        studyItem1.setMode(AppMode.CLINICWEB);
        studyParticipantAssignment.addStudyParticipantModeHistory(studyItem1);
        
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        participant = participantRepository.save(participant);
    }

    private void updateParticipantModeHistory(StudyParticipantAssignment studyParticipantAssignment) {
        String participantMode = "IVRS";
        String participantClinicMode = "CLINICBOOKLET";
        boolean blFlgAddHomeMode = true;
        boolean blFlgAddClinicMode = true;
        if (participantMode == null) {
            blFlgAddHomeMode = false;
        }
        if (participantClinicMode == null) {
            blFlgAddClinicMode = false;
        }
        if(blFlgAddHomeMode || blFlgAddClinicMode){
            for (StudyParticipantReportingModeHistory studyParticipantReportingModeHistory: studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems()) {
                if(studyParticipantReportingModeHistory.getEffectiveEndDate()==null){
                     if(studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEWEB) || studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEBOOKLET)|| studyParticipantReportingModeHistory.getMode().equals(AppMode.IVRS)){
                                  if(blFlgAddHomeMode){
                                      AppMode mode = AppMode.valueOf(participantMode);
                                      if(studyParticipantReportingModeHistory.getMode().equals(mode)){
                                             blFlgAddHomeMode = false;
                                      }else{
                                           studyParticipantReportingModeHistory.setEffectiveEndDate(new Date());
                                      }
                                  }

                     }else {
                             if(blFlgAddClinicMode){
                                          AppMode mode = AppMode.valueOf(participantClinicMode);
                                          if(studyParticipantReportingModeHistory.getMode().equals(mode)){
                                                 blFlgAddClinicMode = false;
                                          }else{
                                               studyParticipantReportingModeHistory.setEffectiveEndDate(new Date());
                                          }
                             }


                     }
                }
            }
        }
        if(blFlgAddClinicMode){
                 StudyParticipantReportingModeHistory hist = new StudyParticipantReportingModeHistory();
                 AppMode mode = AppMode.valueOf(participantClinicMode);
                 hist.setMode(mode);
                 studyParticipantAssignment.addStudyParticipantModeHistory(hist);
        }
        if(blFlgAddHomeMode){
                 StudyParticipantReportingModeHistory hist = new StudyParticipantReportingModeHistory();
                 AppMode mode = AppMode.valueOf(participantMode);
                 hist.setMode(mode);
                 studyParticipantAssignment.addStudyParticipantModeHistory(hist);
        }

    }

    public void testGetReportingModeHistory(){
         saveParticipant();
        StudyParticipantAssignment studyParticipantAssignment = participant.getStudyParticipantAssignments().get(0);
        List<StudyParticipantReportingModeHistory> histItems = studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems();
        assertEquals(2,histItems.size());

        //Categorizing into clinic and home reporting modes
        List<StudyParticipantReportingModeHistory> homeModeHistItems = new ArrayList<StudyParticipantReportingModeHistory>();
        List<StudyParticipantReportingModeHistory> clinicModeHistItems = new ArrayList<StudyParticipantReportingModeHistory>();
        for (StudyParticipantReportingModeHistory studyParticipantReportingModeHistory : histItems) {
            if(studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEWEB) ||studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEBOOKLET)|| studyParticipantReportingModeHistory.getMode().equals(AppMode.IVRS)){
                homeModeHistItems.add(studyParticipantReportingModeHistory);
            }else{
                clinicModeHistItems.add(studyParticipantReportingModeHistory);
            }
        }

        assertEquals(1,clinicModeHistItems.size());
        assertEquals(AppMode.CLINICWEB,clinicModeHistItems.get(0).getMode());
        assertNull(clinicModeHistItems.get(0).getEffectiveEndDate());
        assertNotNull(clinicModeHistItems.get(0).getEffectiveStartDate());
        assertEquals(1,homeModeHistItems.size());
        assertEquals(AppMode.HOMEBOOKLET,homeModeHistItems.get(0).getMode());
        assertNull(homeModeHistItems.get(0).getEffectiveEndDate());
        assertNotNull(homeModeHistItems.get(0).getEffectiveStartDate());
    }

    public void testUpdateReportingModeHistory(){
        saveParticipant();
        StudyParticipantAssignment studyParticipantAssignment = participant.getStudyParticipantAssignments().get(0);
        updateParticipantModeHistory(studyParticipantAssignment);
        assertEquals(4,studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems().size());

        //Categorizing into clinic and home reporting modes
        List<StudyParticipantReportingModeHistory> homeModeHistItems = new ArrayList<StudyParticipantReportingModeHistory>();
        List<StudyParticipantReportingModeHistory> clinicModeHistItems = new ArrayList<StudyParticipantReportingModeHistory>();
        for (StudyParticipantReportingModeHistory studyParticipantReportingModeHistory : studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems()) {
            if(studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEWEB) ||studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEBOOKLET)|| studyParticipantReportingModeHistory.getMode().equals(AppMode.IVRS)){
                homeModeHistItems.add(studyParticipantReportingModeHistory);
            }else{
                clinicModeHistItems.add(studyParticipantReportingModeHistory);
            }
        }

        assertEquals(2,clinicModeHistItems.size());
        assertEquals(2,homeModeHistItems.size());

        assertEquals(AppMode.CLINICWEB,clinicModeHistItems.get(0).getMode());
        assertNotNull(clinicModeHistItems.get(0).getEffectiveEndDate());
        assertNotNull(clinicModeHistItems.get(0).getEffectiveStartDate());

        assertEquals(AppMode.CLINICBOOKLET,clinicModeHistItems.get(1).getMode());
        assertNull(clinicModeHistItems.get(1).getEffectiveEndDate());
        assertNotNull(clinicModeHistItems.get(1).getEffectiveStartDate());

        assertEquals(AppMode.HOMEBOOKLET,homeModeHistItems.get(0).getMode());
        assertNotNull(homeModeHistItems.get(0).getEffectiveEndDate());
        assertNotNull(homeModeHistItems.get(0).getEffectiveStartDate());

        assertEquals(AppMode.IVRS,homeModeHistItems.get(1).getMode());
        assertNull(homeModeHistItems.get(1).getEffectiveEndDate());
        assertNotNull(homeModeHistItems.get(1).getEffectiveStartDate());

        assertNotNull(studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems().get(0).getEffectiveEndDate());
        assertNotNull(studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems().get(1).getEffectiveEndDate());
        assertNull(studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems().get(2).getEffectiveEndDate());
        assertNull(studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems().get(3).getEffectiveEndDate());
    }


}
