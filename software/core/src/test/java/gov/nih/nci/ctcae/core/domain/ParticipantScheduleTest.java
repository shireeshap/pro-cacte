package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Jan 18, 2011
 * Time: 1:55:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantScheduleTest extends TestCase {
    private ParticipantSchedule participantSchedule;
    private ProCtcAECalendar proCtcAECalendar;
    private StudyParticipantCrf studyParticipantCrf;
    private List<StudyParticipantCrf> studyParticipantCrfs;
    StudyParticipantAssignment studyParticipantAssignment;
    private CRF crf;
    private CRFCycle cycle1;
    private CRFPage crfPage;
    List<CRFPage> crfPages;
    CRFCycleDefinition defA;
    private Study study;
    private StudySite studySite;

    @Override
    public void setUp() throws Exception {
        study = new Study();
        study.setId(1);
        study.setCallBackHour(1);
        studySite = new StudySite();
        studySite.setId(1);
        studySite.setStudy(study);
    	studyParticipantAssignment = new StudyParticipantAssignment();
    	StudyParticipantMode spm = new StudyParticipantMode();
    	spm.setMode(AppMode.IVRS);
    	studyParticipantAssignment.getStudyParticipantModes().add(spm);
    	studyParticipantAssignment.setCallAmPm("am");
    	studyParticipantAssignment.setCallHour(10);
    	studyParticipantAssignment.setCallMinute(0);
    	studyParticipantAssignment.setCallTimeZone(StudyParticipantAssignment.PACIFIC);
        studyParticipantAssignment.setStudySite(studySite);
        participantSchedule=new ParticipantSchedule();
        proCtcAECalendar = new ProCtcAECalendar();
        crf = new CRF();
        crf.setId(1);
        crfPage=new CRFPage();
        crfPage.setPageNumber(1);
        crfPages = new ArrayList<CRFPage>();
        crfPages.add(crfPage);
        crf.setCrfPages(crfPages);
        studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setCrf(crf);
        studyParticipantCrf.setId(1);
        studyParticipantCrf.setStartDate(DateUtils.parseDate("01/18/2011"));
        studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
        studyParticipantCrfs =new ArrayList<StudyParticipantCrf>();
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.setProCtcAECalendar(proCtcAECalendar);
        cycle1 = new CRFCycle();
        cycle1.setOrder(0);
        cycle1.setCycleDays(",1,8,13");
        defA = new CRFCycleDefinition();
        defA.setCycleLength(14);
        defA.setCycleLengthUnit("Days");
        defA.setRepeatTimes("2");
        defA.setOrder(0);
        defA.setDueDateUnit("Days");// optional
        defA.setDueDateValue("3");  //optional
        cycle1.setCrfDefinition(defA);
        proCtcAECalendar.setRepeatUntilUnit("2");
        proCtcAECalendar.setDueDateUnit("Days");
        proCtcAECalendar.setDueDateAmount(3);
        proCtcAECalendar.setStartDate(DateUtils.parseDate("01/18/2011"));
        proCtcAECalendar.setCycleParameters(cycle1,DateUtils.parseDate("01/18/2011"),1);

    }
    public void testCreateSchedules() throws Exception {
        proCtcAECalendar.setCycleParameters(cycle1,DateUtils.parseDate("01/18/2011"),1);
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
        participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.CYCLE);
        System.out.println("for break point purposes");

    }
}
