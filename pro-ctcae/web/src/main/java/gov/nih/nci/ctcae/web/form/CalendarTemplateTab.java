package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;

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
        if (command.getSelectedFormArmSchedule() == null) {
            CRF crf = command.getCrf();
            Study study = studyRepository.findById(crf.getStudy().getId());
            List<Arm> arms = study.getArms();
            for (Arm arm : arms) {
                if (crf.getFormArmScheduleForArm(arm) == null) {
                    FormArmSchedule formArmSchedule = crf.addFormArmSchedule(arm);
                    CRFCalendar crfCalendar = new CRFCalendar();
                    formArmSchedule.addCrfCalendar(crfCalendar);
                }
            }
            command.setSelectedFormArmSchedule(crf.getFormArmSchedules().get(0));
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
    public void onBind(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        super.onBind(request, command, errors);
        FormArmSchedule formArmSchedule = command.getSelectedFormArmSchedule();
        CRFCalendar crfCalendar = formArmSchedule.getCrfCalendars().get(0);
        if ("Number".equals(crfCalendar.getRepeatUntilUnit())) {
            crfCalendar.setRepeatUntilValue(request.getParameter("crfCalendar_number_0.repeatUntilAmount"));
        }
        if ("Date".equals(crfCalendar.getRepeatUntilUnit())) {
            crfCalendar.setRepeatUntilValue(request.getParameter("crfCalendar_date_0.repeatUntilAmount"));
        }
    }


    @Override
    public void postProcess(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        if (!StringUtils.isBlank(command.getCrfCycleDefinitionIndexToRemove())) {
            Integer crfCycleDefinitionIndex = Integer.valueOf(command.getCrfCycleDefinitionIndexToRemove());
            CRFCycleDefinition crfCycleDefinition = command.getSelectedFormArmSchedule().getCrfCycleDefinitions().get(crfCycleDefinitionIndex);
            command.getSelectedFormArmSchedule().getCrfCycleDefinitions().remove(crfCycleDefinition);
            command.setCrfCycleDefinitionIndexToRemove("");
        }
        command.createCycles(request);
        super.postProcess(request, command, errors);
    }

    public StudyRepository getStudyRepository() {
        return studyRepository;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}