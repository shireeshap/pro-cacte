package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.domain.CRFCalendar;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment;
import gov.nih.nci.ctcae.core.domain.CRFCycle;
import gov.nih.nci.ctcae.core.ListValues;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.springframework.validation.Errors;
import org.apache.commons.lang.StringUtils;

//
/**
 * The Class CalendarTemplateTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class CalendarTemplateTab extends Tab<CreateFormCommand> {

    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /**
     * Instantiates a new calendar template tab.
     */
    public CalendarTemplateTab() {
        super("form.tab.form", "form.tab.calendar_template", "form/calendar_template");
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
    }

    @Override
    public void postProcess(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        if (!StringUtils.isBlank(command.getCrfCycleIndexToRemove())) {
            Integer crfCycleIndex = Integer.valueOf(command.getCrfCycleIndexToRemove());
            CRFCycle crfCycle = command.getCrf().getCrfCycles().get(crfCycleIndex);
            command.getCrf().getCrfCycles().remove(crfCycle);
        }
        super.postProcess(request, command, errors);
    }

    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}