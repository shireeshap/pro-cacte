package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

//

/**
 * @author Harsh Agarwal
 * @created Feb 12, 2008
 */
public class DisplayStudySitesController extends AbstractController {

    private StudyOrganizationRepository studyOrganizationRepository;
    private ParticipantRepository participantRepository;


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView("participant/ajax/displaystudysites");
        Integer organizationId = Integer.parseInt(request.getParameter("organizationId"));
        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByOrganizationId(organizationId);
        query.filterByStudySiteAndLeadSiteOnly();
        Collection<StudyOrganization> studySites = studyOrganizationRepository.find(query);

        String participantId = request.getParameter("id");
        Participant participant = null;
        ParticipantCommand command = (ParticipantCommand) request.getSession().getAttribute(CreateParticipantController.class.getName() + ".FORM." + "command");
        if (!StringUtils.isBlank(participantId)) {
            participant = participantRepository.findById(Integer.parseInt(participantId));
        } else {
            participant = command.getParticipant();
        }
        List<Integer> times = new ArrayList();
            for (int j = 1; j <= 12; j++) {
                    times.add(j);
                }

           


        if (participant != null) {
            List<StudyParticipantAssignment> studyParticipantAssignments = new ArrayList<StudyParticipantAssignment>();

            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                StudyOrganization studySite = studyParticipantAssignment.getStudySite();
                studySites.remove(studySite);
                studySite.getStudy().getCrfs();
                studySite.getStudy().getStudySponsor();
                studyParticipantAssignments.add(studyParticipantAssignment);
            }
            String[] timeZones = TimeZone.getAvailableIDs();

            modelAndView.addObject("studyparticipantassignments", studyParticipantAssignments);
            modelAndView.addObject("times", times);
            modelAndView.addObject("timezones", timeZones);
        }
        List<String> participantModes = new ArrayList();

        modelAndView.addObject("unselectedstudysites", studySites);
        return modelAndView;
    }


    public DisplayStudySitesController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}