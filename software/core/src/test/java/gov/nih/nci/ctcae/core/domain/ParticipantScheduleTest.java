package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import junit.framework.TestCase;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ProCtcAECalendar otherProCtcAECalendar;
    private StudyParticipantCrf studyParticipantCrf;
    private StudyParticipantCrf otherStudyParticipantCrf;
    private List<StudyParticipantCrf> studyParticipantCrfs;
    StudyParticipantAssignment studyParticipantAssignment;
    private CRF crf;
    private CRF childCrf;
    private Arm arm;
    private CRFCycle cycle1;
    private CRFPage crfPage;
    List<CRFPage> crfPages;
    CRFCycleDefinition defA;
    private Study study;
    private StudySite studySite;
    private static String CRF_TITLE1 = 	"Parent Crf";
    private static String CRF_TITLE2 = "Child Crf";
    private static String CRF_VERSION1 = "1";
    private static String CRF_VERSION2 = "2";

    @Override
    public void setUp() throws Exception {
        study = new Study();
        study.setId(1);
        study.setCallBackHour(1);
        studySite = new StudySite();
        studySite.setId(1);
        studySite.setStudy(study);
        arm = new Arm();
        arm.setDefaultArm(true);
        arm.setStudy(study);
        arm.setId(1);
    	studyParticipantAssignment = new StudyParticipantAssignment();
    	StudyParticipantMode spm = new StudyParticipantMode();
    	spm.setMode(AppMode.IVRS);
    	setValuesForStudyParticipantAssignment(studyParticipantAssignment, spm, "am", 10, 0, StudyParticipantAssignment.PACIFIC, studySite, arm);
    	
        participantSchedule=new ParticipantSchedule();
        proCtcAECalendar = new ProCtcAECalendar();
        crf = createCRf(1, CRF_TITLE1, CRF_VERSION1);
        crfPage=new CRFPage();
        crfPage.setPageNumber(1);
        crfPages = new ArrayList<CRFPage>();
        crfPages.add(crfPage);
        crf.setCrfPages(crfPages);
        crf.setEffectiveStartDate(DateUtils.parseDate("01/16/2011"));
        
        studyParticipantCrf = new StudyParticipantCrf();
        setValuesForStudyParticipantCrf(studyParticipantCrf, crf, 1, DateUtils.parseDate("01/18/2011"), studyParticipantAssignment,  studyParticipantAssignment.getArm());
        
        studyParticipantCrfs =new ArrayList<StudyParticipantCrf>();
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.setProCtcAECalendar(proCtcAECalendar);
        defA = createCycleDefinition(14, "Days", "2", 0, "Days", "3");
        cycle1 = createCrfCycle(0, ",1,8,13", defA);
        
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
        participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.CYCLE, false);
    }

    public void testCreateSchedule() throws Exception {
        proCtcAECalendar.setCycleParameters(cycle1,DateUtils.parseDate("01/18/2011"),1);
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
        participantSchedule.createSchedule(proCtcAECalendar.getCalendar(), new Date(), 1, 1, null, false, false);
        assertEquals(1, studyParticipantCrf.getStudyParticipantCrfSchedules().size());
        assertEquals(Integer.valueOf(1), studyParticipantCrf.getStudyParticipantCrfSchedules().get(0).getCycleNumber());

    }
    
    public void testGetCurrentMonthSchedules() throws Exception{
    	proCtcAECalendar.setCycleParameters(cycle1,DateUtils.parseDate("01/18/2011"),1);
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
        participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.CYCLE, false);
    	
    	List<StudyParticipantCrfSchedule> allCurrentMothSchedules = participantSchedule.getCurrentMonthSchedules();
    	assertEquals(3, allCurrentMothSchedules.size());
    	assertEquals(DateUtils.parseDate("01/18/2011"), allCurrentMothSchedules.get(0).getStartDate());
    	assertEquals(DateUtils.parseDate("01/25/2011"), allCurrentMothSchedules.get(1).getStartDate());
    	assertEquals(DateUtils.parseDate("01/30/2011"), allCurrentMothSchedules.get(2).getStartDate());
    }
    
    public void testGetPreviousSchedules() throws Exception{
    	
    	proCtcAECalendar.setCycleParameters(cycle1,DateUtils.parseDate("01/18/2011"),1);
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
        participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.CYCLE, false);
        
    	childCrf = createCRf(2, CRF_TITLE2, CRF_VERSION2);
        childCrf.setEffectiveStartDate(DateUtils.parseDate("01/18/2011"));
        childCrf.setParentCrf(crf);
         
        otherStudyParticipantCrf = new StudyParticipantCrf();
        setValuesForStudyParticipantCrf(otherStudyParticipantCrf, childCrf, 2, DateUtils.parseDate("01/20/2011"), studyParticipantAssignment, studyParticipantAssignment.getArm());
        studyParticipantCrfs.add(otherStudyParticipantCrf);
        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);
        studyParticipantAssignment.addStudyParticipantCrf(otherStudyParticipantCrf);
         
        otherProCtcAECalendar = new ProCtcAECalendar();
        otherProCtcAECalendar.setRepeatUntilUnit("2");
        otherProCtcAECalendar.setDueDateUnit("Days");
        otherProCtcAECalendar.setDueDateAmount(3);
        otherProCtcAECalendar.setStartDate(DateUtils.parseDate("01/20/2011"));
        otherProCtcAECalendar.setCycleParameters(cycle1,DateUtils.parseDate("01/20/2011"),1);
        List<StudyParticipantCrfSchedule> previousSchedules = participantSchedule.getPreviousSchedules(studyParticipantAssignment, otherStudyParticipantCrf);
    	assertEquals(3, previousSchedules.size());
    	assertEquals(DateUtils.parseDate("01/18/2011"), previousSchedules.get(0).getStartDate());
    	assertEquals(DateUtils.parseDate("01/25/2011"), previousSchedules.get(1).getStartDate());
    	assertEquals(DateUtils.parseDate("01/30/2011"), previousSchedules.get(2).getStartDate());
    }
    
    public void testGetDueDateForFormSchedule() throws ParseException{
        FormArmSchedule formArmSchedule = new FormArmSchedule();
        formArmSchedule.setCrf(crf);
        List<FormArmSchedule> formArmSchedules = new ArrayList<FormArmSchedule>();
        formArmSchedules.add(formArmSchedule);
        crf.setFormArmSchedules(formArmSchedules);
        formArmSchedule.addCrfCycleDefinition(defA);
        formArmSchedule.setArm(studyParticipantCrf.getArm());
        
        Date dueDate = participantSchedule.getDueDateForFormSchedule(proCtcAECalendar.getCalendar(), studyParticipantCrf);
        assertEquals(DateUtils.parseDate("01/20/2011"), dueDate);
    }
    
    public void testRemoveSchedule() throws ParseException{
    	proCtcAECalendar.setCycleParameters(cycle1,DateUtils.parseDate("01/18/2011"),1);
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
        participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.CYCLE, false);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate("01/30/2011"));
        List<String> formIds = new ArrayList<String>();
        formIds.add(crf.getId().toString());
        
        participantSchedule.removeSchedule(calendar, formIds);
    	assertEquals(2, participantSchedule.getCurrentMonthSchedules().size());
    }
    
    public void testRemoveAllSchedules() throws ParseException{
    	Date today = new Date();
    	proCtcAECalendar.setCycleParameters(cycle1, today, 1);
        studyParticipantCrfs.add(studyParticipantCrf);
        participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
        // 3 surveys are scheduled ahead, starting today's date
        participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.CYCLE, false);
        
        List<String> formIds = new ArrayList<String>();
        formIds.add(crf.getId().toString());
        
        // expect all the 3 scheduled schedules to be removed
        participantSchedule.removeAllSchedules(formIds);
    	assertEquals(0, participantSchedule.getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().size());
    	
    	proCtcAECalendar.setCycleParameters(cycle1, DateUtils.addDaysToDate(today, -4), 1);
    	// 2 surverys are scheduled and 1 survey is NotApplicable
    	participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.CYCLE, false);
    	participantSchedule.removeAllSchedules(formIds);
    	// expect 2 scheduled schedules to be removed
    	assertEquals(1, participantSchedule.getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().size());
    }
    
    private CRF createCRf(Integer id, String title, String crfVersion){
    	CRF crf = new CRF();
        crf.setId(id);
        crf.setTitle(title);
        crf.setCrfVersion(crfVersion);
    	return crf;
    }
    
    private void setValuesForStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment,StudyParticipantMode spm, String callAmPm, Integer callHour, Integer callMinute, String callTimeZone, StudySite studySite, Arm arm){
		studyParticipantAssignment.getStudyParticipantModes().add(spm);
    	studyParticipantAssignment.setCallAmPm(callAmPm);
    	studyParticipantAssignment.setCallHour(callHour);
    	studyParticipantAssignment.setCallMinute(callMinute);
    	studyParticipantAssignment.setCallTimeZone(StudyParticipantAssignment.PACIFIC);
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setArm(arm);
	}
    
    private void setValuesForStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf, CRF crf, Integer id, Date date, StudyParticipantAssignment studyParticipantAssignment, Arm arm){
    	studyParticipantCrf.setCrf(crf);
        studyParticipantCrf.setId(id);
        studyParticipantCrf.setStartDate(date);
        studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
        studyParticipantCrf.setArm(arm);
    }
    
    private CRFCycleDefinition createCycleDefinition(Integer cycleLength, String cycleLengthUnit, String repeatTimes, Integer order, String dueDateUnit, String dueDateValue){
    	CRFCycleDefinition crfCycleDef = new CRFCycleDefinition();
    	
    	crfCycleDef.setCycleLength(cycleLength);
    	crfCycleDef.setCycleLengthUnit(cycleLengthUnit);
    	crfCycleDef.setRepeatTimes(repeatTimes);
    	crfCycleDef.setOrder(order);
    	crfCycleDef.setDueDateUnit(dueDateUnit);// optional
    	crfCycleDef.setDueDateValue(dueDateValue);  //optional
    	return crfCycleDef;
    }
    
    private CRFCycle createCrfCycle(Integer order, String cycleDays, CRFCycleDefinition crfCycleDefinition){
    	CRFCycle crfCycle = new CRFCycle();
    	
    	crfCycle.setOrder(order);
    	crfCycle.setCycleDays(cycleDays);
    	crfCycle.setCrfDefinition(crfCycleDefinition);
    	return crfCycle;
    }
}
