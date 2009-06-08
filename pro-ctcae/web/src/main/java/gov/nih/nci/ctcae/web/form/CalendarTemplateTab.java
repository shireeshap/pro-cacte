package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRFCalendar;
import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

//
/**
 * The Class CalendarTemplateTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class CalendarTemplateTab extends SecuredTab<CreateFormCommand> {


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

        List<CRFCalendar> crfCalendars = command.getCrf().getCrfCalendars();

        if (crfCalendars == null || crfCalendars.size() == 0) {
            CRFCalendar crfCalendar = new CRFCalendar();

            command.getCrf().addCrfCalendar(crfCalendar);
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
        if (command.getCrf().getCrfCalendars() != null) {

            for (int i = 0; i < command.getCrf().getCrfCalendars().size(); i++) {
                CRFCalendar crfCalendar = command.getCrf().getCrfCalendars().get(i);
                if ("Number".equals(crfCalendar.getRepeatUntilUnit())) {
                    crfCalendar.setRepeatUntilAmount(request.getParameter("crfCalendar_number_" + i + ".repeatUntilAmount"));
                }
                if ("Date".equals(crfCalendar.getRepeatUntilUnit())) {
                    crfCalendar.setRepeatUntilAmount(request.getParameter("crfCalendar_date_" + i + ".repeatUntilAmount"));
                }
            }
        }
    }


    @Override
    public void validate(CreateFormCommand command, Errors errors) {
        super.validate(command, errors);
        if (command.getCrf().getCrfCalendars() != null) {
            for (CRFCalendar crfCalendar : command.getCrf().getCrfCalendars()) {
                if (!StringUtils.isBlank(crfCalendar.getRepeatEveryAmount())) {
                    if (!"Indefinitely".equals(crfCalendar.getRepeatUntilUnit()) && StringUtils.isBlank(crfCalendar.getRepeatUntilAmount())) {
                        errors.reject(
                                "repeatuntil", "Please provide value for 'Repeat Until' field.");

                    }
                    if ("Number".equals(crfCalendar.getRepeatUntilUnit()) && !StringUtils.isNumeric(crfCalendar.getRepeatUntilAmount())) {
                        errors.reject(
                                "repeatuntil", "Please provide numeric value for 'Repeat Until' field.");

                    }
                    if ("Date".equals(crfCalendar.getRepeatUntilUnit()) && !StringUtils.isBlank(crfCalendar.getRepeatUntilAmount())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        try {
                            sdf.parse(crfCalendar.getRepeatUntilAmount());
                        } catch (ParseException e) {
                            errors.reject(
                                    "repeatuntil", "Please provide valid date for 'Repeat Until' field (mm/dd/yyyy).");
                        }

                    }
                    if (StringUtils.isBlank(crfCalendar.getDueDateAmount())) {
                        errors.reject(
                                "duedate", "Please provide value for 'Form is due after' field.");
                    }
                    if (!StringUtils.isBlank(crfCalendar.getDueDateAmount()) && !StringUtils.isNumeric(crfCalendar.getDueDateAmount())) {
                        errors.reject(
                                "duedate", "Please provide numeric value for 'Form is due after' field.");
                    }
                }
            }
        }

        if (command.getCrf().getCrfCycleDefinitions() != null) {
            for (int i = 0; i < command.getCrf().getCrfCycleDefinitions().size(); i++) {
                CRFCycleDefinition crfCycleDefinition = command.getCrf().getCrfCycleDefinitions().get(i);

                if (crfCycleDefinition.getCycleLength() == null || StringUtils.isBlank("" + crfCycleDefinition.getCycleLength()) || !StringUtils.isNumeric("" + crfCycleDefinition.getCycleLength())) {
                    errors.reject("cyclelength", "Please provide a valid numeric value for the length of cycle definition " + (i + 1) + ".");
                } else {
                    if (crfCycleDefinition.getRepeatTimes() == null || StringUtils.isBlank("" + crfCycleDefinition.getRepeatTimes()) || (!StringUtils.isNumeric(crfCycleDefinition.getRepeatTimes().toString()) && !crfCycleDefinition.getRepeatTimes().toString().equals("-1"))) {
                        errors.reject("repeat", "Please provide a valid numeric value for number of planned repetitions for cycle definition " + (i + 1) + ".");
                    }
                }
            }
        }
    }

    @Override
    public void postProcess(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        if (!StringUtils.isBlank(command.getCrfCycleDefinitionIndexToRemove())) {
            Integer crfCycleDefinitionIndex = Integer.valueOf(command.getCrfCycleDefinitionIndexToRemove());
            CRFCycleDefinition crfCycleDefinition = command.getCrf().getCrfCycleDefinitions().get(crfCycleDefinitionIndex);
            command.getCrf().getCrfCycleDefinitions().remove(crfCycleDefinition);
            command.setCrfCycleDefinitionIndexToRemove("");
        }
        command.createCycles(request);
        super.postProcess(request, command, errors);
    }

}