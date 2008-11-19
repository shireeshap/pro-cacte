package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class SelectStudyParticipantTab extends Tab<StudyParticipantCommand> {
    private FinderRepository finderRepository;

    public SelectStudyParticipantTab() {
        super("schedulecrf.label.schedule_form", "schedulecrf.label.select_study_participant", "participant/selectstudyparticipant");

    }

    @Override
    public void onDisplay(final HttpServletRequest request, final StudyParticipantCommand command) {

        super.onDisplay(request, command);

        if (!StringUtils.isBlank(request.getParameter("studyCrfId")) && command.getStudy() == null) {
            StudyCrf studyCrf = finderRepository.findById(StudyCrf.class, Integer.valueOf(request.getParameter("studyCrfId")));
            command.setStudy(studyCrf.getStudy());
        }

    }

    @Override
    public void postProcess(HttpServletRequest httpServletRequest, StudyParticipantCommand studyParticipantCommand, Errors errors) {
        StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
        query.filterByParticipantId(studyParticipantCommand.getParticipant().getId());
        query.filterByStudyId(studyParticipantCommand.getStudy().getId());
        List<StudyParticipantAssignment> persistables = (List<StudyParticipantAssignment>) finderRepository.find(query);
        StudyParticipantAssignment studyParticipantAssignment = persistables.get(0);

        studyParticipantCommand.setStudyParticipantAssignment(studyParticipantAssignment);

    }


    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}