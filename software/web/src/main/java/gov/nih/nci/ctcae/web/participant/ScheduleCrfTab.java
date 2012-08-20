package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//

/**
 * The Class ScheduleCrfTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class ScheduleCrfTab extends Tab<ParticipantCommand> {
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
    public void onDisplay(HttpServletRequest request, ParticipantCommand command) {
        command.lazyInitializeAssignment(genericRepository, false);
    }

    @Override
    public Map<String, Object> referenceData(ParticipantCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("repetitionunits", ListValues.getCalendarRepetitionUnits());
        map.put("duedateunits", ListValues.getCalendarDueDateUnits());
        map.put("repeatuntilunits", ListValues.getCalendarRepeatUntilUnits());
        map.put("cyclelengthunits", ListValues.getCalendarRepetitionUnits());
        int crfsIndex=0;
        List<CRF> crfs = new ArrayList<CRF>();
        if(command.getParticipant().getStudyParticipantAssignments().get(0)!=null){
            for (StudyParticipantCrf studyParticipantCrf : command.getParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs()) {
                CRF crf = studyParticipantCrf.getCrf();
                if (crf.getChildCrf() == null && !crfs.contains(crf)) {
                    crfs.add(crf);
                    crfsIndex++;
                }
            }
        }
        map.put("crfsSize", crfsIndex);
        return map;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}