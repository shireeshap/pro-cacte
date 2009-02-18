package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.ListValues;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//
/**
 * The Class ScheduleCrfTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class ScheduleCrfTab extends Tab<StudyParticipantCommand> {
    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /**
     * Instantiates a new schedule crf tab.
     */
    public ScheduleCrfTab() {
        super("schedulecrf.label.schedule_form", "schedulecrf.label.schedule_form", "participant/schedulecrf");

    }

    @Override
    public void onDisplay(HttpServletRequest request, StudyParticipantCommand command) {
        for (StudyParticipantCrf studyParticipantCrf : command.getStudyParticipantAssignment().getStudyParticipantCrfs()) {
            finderRepository.findById(CRF.class, studyParticipantCrf.getCrf().getId());
        }

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

    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}