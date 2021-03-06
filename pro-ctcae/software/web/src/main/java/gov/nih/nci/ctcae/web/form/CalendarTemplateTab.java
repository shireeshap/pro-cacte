package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//
/**
 * The Class CalendarTemplateTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class CalendarTemplateTab extends SecuredTab<CreateFormCommand> {

    private StudyRepository studyRepository;
    private CRFRepository crfRepository;

    /**
     * Instantiates a new calendar template tab.
     */
    public CalendarTemplateTab() {
        super("form.tab.form", "form.tab.calendar_template", "form/calendar_template");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_CREATE_FORM;


    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#onDisplay(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    @Override
    public void onDisplay(HttpServletRequest request, CreateFormCommand command) {
        if (command.getCrf().getTitle() == null) {
            command.getCrf().setTitle(command.getUniqueTitleForCrf());
        }
        if (command.getSelectedFormArmSchedule() == null) {
            command.setAllArms(true);
            CRF crf = command.getCrf();
            Study study = studyRepository.findById(crf.getStudy().getId());
            List<Arm> arms = study.getArms();
            for (Arm arm : arms) {
                FormArmSchedule armScheduleForArm = crf.getFormArmScheduleForArm(arm);
                if (armScheduleForArm == null) {
                    FormArmSchedule formArmSchedule = crf.addFormArmSchedule(arm);
                    CRFCalendar crfCalendar = new CRFCalendar();
                    formArmSchedule.addCrfCalendar(crfCalendar);
                } else {
                    if (armScheduleForArm.getCrfCalendars().size() == 0) {
                        CRFCalendar crfCalendar = new CRFCalendar();
                        armScheduleForArm.addCrfCalendar(crfCalendar);
                    }
                }
            }
            
            CRF savedCrf = crfRepository.save(command.getCrf());
            command.setCrf(savedCrf);
            command.setSelectedFormArmSchedule(savedCrf.getFormArmSchedules().get(0));
            command.setNewSelectedFormArmSchedule(savedCrf.getFormArmSchedules().get(0));	
        } else {
            if (command.getNewSelectedFormArmSchedule() != null) {
                command.setSelectedFormArmSchedule(command.getNewSelectedFormArmSchedule());
                command.setNewSelectedFormArmSchedule(null);
            }
        }

    }

    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("repetitionunits", ListValues.getCalendarRepetitionUnits());
        map.put("duedateunits", ListValues.getCalendarDueDateUnits());
        map.put("repeatuntilunits", ListValues.getCalendarRepeatUntilUnits());
        map.put("cyclelengthunits", ListValues.getCalendarRepetitionUnits());
        map.put("cycleplannedrepetitions", ListValues.getCyclePlannedRepetitions());

        return map;
    }

    @Override
    public void validate(CreateFormCommand command, Errors errors) {
    	 super.validate(command, errors);
    	 List<FormArmSchedule> formArmSchedules= command.getCrf().getFormArmSchedules();
    	 for(FormArmSchedule formArmSchedule : formArmSchedules){
    		 List<CRFCycleDefinition> crfCycleDefinitions = formArmSchedule.getCrfCycleDefinitions();
    		 for(CRFCycleDefinition crfCycleDefinition : crfCycleDefinitions){
    			 int index=0;
    			 String dueDateValue = crfCycleDefinition.getDueDateValue();
    			 if(!StringUtils.isEmpty(dueDateValue)){
    				 if(dueDateValue.contains(".") ){
    					 String field="cycle_due_"+index;
    					 errors.reject(field, "Value for form expiration should be a whole number greater than 0");
    				 }else if (Integer.parseInt(dueDateValue) <= 0){
    					 String field="cycle_due_"+index;
    					 errors.reject(field, "Value for form expiration should be a whole number greater than 0");
    				 }
    			 }
    			 
    			 index++;
    		 }
    	 }
    }

    @Override
    public void postProcess(HttpServletRequest request, CreateFormCommand command, Errors errors) {

        String scheduleType = request.getParameter("scheduleType");
        if (!StringUtils.isBlank(scheduleType)) {
            FormArmSchedule formArmSchedule = command.getSelectedFormArmSchedule();
            CRFCalendar crfCalendar = formArmSchedule.getCrfCalendars().get(0);
            if ("calendarBased".equals(scheduleType)) {
                formArmSchedule.getCrfCycleDefinitions().clear();
                if ("Number".equals(crfCalendar.getRepeatUntilUnit())) {
                    crfCalendar.setRepeatUntilValue(request.getParameter("crfCalendar_number_0.repeatUntilValue"));
                }
                if ("Date".equals(crfCalendar.getRepeatUntilUnit())) {
                    crfCalendar.setRepeatUntilValue(request.getParameter("crfCalendar_date_0.repeatUntilValue"));
                }
                if ("Indefinitely".equals(crfCalendar.getRepeatUntilUnit())) {
                    crfCalendar.setRepeatUntilValue("" + 100);
                }
            }
            if ("cycleBased".equals(scheduleType)) {
                crfCalendar.makeInvalid();
                command.createCycles(request);
                for (CRFCycleDefinition invalidCrfCycleDefinition : command.getInvalidCycleDefinitions()) {
                    command.getSelectedFormArmSchedule().getCrfCycleDefinitions().remove(invalidCrfCycleDefinition);
                }
                command.getInvalidCycleDefinitions().clear();
            }
            
            for (FormArmSchedule fasMain: command.getCopySelectedArmScheduleIds()){
                 for (FormArmSchedule fas : command.getCrf().getFormArmSchedules()) {
                    if(fasMain.equals(fas)){
                         if (!fas.equals(formArmSchedule)) {
                            formArmSchedule.copySchedulesInto(fas);
                            break; 
                        }
                    }
                }
            }
            command.getCopySelectedArmScheduleIds().clear();
        }
        
        if(!errors.hasErrors()){
        	command.setCrf(crfRepository.save(command.getCrf()));
        }
        super.postProcess(request, command, errors);

    }


    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

}