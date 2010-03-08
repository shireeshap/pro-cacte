package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//

/**
 * The Class ScheduleCrfTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class ScheduleCrfTab extends SecuredTab<StudyParticipantCommand> {
    /**
     * The finder repository.
     */
    private CRFRepository crfRepository;
    private GenericRepository genericRepository;

    /**
     * Instantiates a new schedule crf tab.
     */
    public ScheduleCrfTab() {
        super("schedulecrf.label.schedule_form", "schedulecrf.label.schedule_form", "participant/schedulecrf");

    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_PARTICIPANT_SCHEDULE_CRF;


    }

    @Override
    public void onDisplay(HttpServletRequest request, StudyParticipantCommand command) {
        command.lazyInitializeAssignment(genericRepository, false);
    }

    @Override
    public Map<String, Object> referenceData(StudyParticipantCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("repetitionunits", ListValues.getCalendarRepetitionUnits());
        map.put("duedateunits", ListValues.getCalendarDueDateUnits());
        map.put("repeatuntilunits", ListValues.getCalendarRepeatUntilUnits());
        map.put("cyclelengthunits", ListValues.getCalendarRepetitionUnits());
        return map;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}