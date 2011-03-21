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
    private CRF crf;
    private CRFCycle cycle1;
    private CRFPage crfPage;
    List<CRFPage> crfPages;
    CRFCycleDefinition defA;

    @Override
    public void setUp() throws Exception {
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
        

    }
}
