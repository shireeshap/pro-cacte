package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantClinicalStaff;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;

//
/**
 * @author Harsh Agarwal
 * @created Feb 25, 2008
 */
public class AddNotificationClinicalStaffController extends AbstractController {

    /**
     * The finder repository.
     */
    FinderRepository finderRepository;


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/notificationClinicalStaff");
        Integer studyParticipantAssignmentIndex = Integer.parseInt(request.getParameter("spa_index"));
        ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
        StudyParticipantAssignment studyParticipantAssignment = command.getParticipant().getStudyParticipantAssignments().get(studyParticipantAssignmentIndex);

        List<StudyParticipantClinicalStaff> clinicalStaffList = command.getMapClinicalStaff().get(studyParticipantAssignment.getId());
        if (clinicalStaffList == null) {
            clinicalStaffList = new ArrayList<StudyParticipantClinicalStaff>();
            //command.getMapClinicalStaff()
        }

        return modelAndView;
    }


    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public AddNotificationClinicalStaffController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}