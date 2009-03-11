package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//
/**
 * The Class SelectStudyParticipantTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class SelectStudyParticipantTab extends SecuredTab<StudyParticipantCommand> {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;

    private CRFRepository crfRepository;

    /**
     * Instantiates a new select study participant tab.
     */
    public SelectStudyParticipantTab() {
        super("schedulecrf.label.schedule_form", "schedulecrf.label.select_study_participant", "participant/selectstudyparticipant");

    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_PARTICIPANT_SCHEDULE_CRF;


    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#onDisplay(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    @Override
    public void onDisplay(final HttpServletRequest request, final StudyParticipantCommand command) {

        super.onDisplay(request, command);

        if (!StringUtils.isBlank(request.getParameter("crfId")) && command.getStudy() == null) {
            CRF crf = crfRepository.findById(Integer.valueOf(request.getParameter("crfId")));
            command.setStudy(crf.getStudy());
        }

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#postProcess(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    public void postProcess(HttpServletRequest httpServletRequest, StudyParticipantCommand studyParticipantCommand, Errors errors) {
        StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
        query.filterByParticipantId(studyParticipantCommand.getParticipant().getId());
        query.filterByStudyId(studyParticipantCommand.getStudy().getId());
        List<StudyParticipantAssignment> persistables = (List<StudyParticipantAssignment>) studyParticipantAssignmentRepository.find(query);
        StudyParticipantAssignment studyParticipantAssignment = persistables.get(0);
        List<StudyParticipantCrf> studyParticipantCrfs = studyParticipantAssignment.getStudyParticipantCrfs();
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            studyParticipantCrf.getStudyParticipantCrfSchedules();
        }
        studyParticipantCommand.setStudyParticipantAssignment(studyParticipantAssignment);
        studyParticipantCommand.getParticipantSchedules();
    }


    @Required

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}