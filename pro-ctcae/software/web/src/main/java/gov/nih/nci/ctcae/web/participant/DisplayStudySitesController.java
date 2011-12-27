package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//

/**
 * @author Harsh Agarwal
 * @created Feb 12, 2008
 */
public class DisplayStudySitesController extends AbstractController {

    private StudyOrganizationRepository studyOrganizationRepository;
    private ParticipantRepository participantRepository;

    private Properties properties;

    public static final String IVRS_BLACKOUT_START_TIME  = "ivrs.blackout.start";
    public static final String IVRS_BLACKOUT_END_TIME  = "ivrs.blackout.end";


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

        ParticipantCommand command = null;

        if (!StringUtils.isBlank(participantId)) {
            command = (ParticipantCommand) request.getSession().getAttribute(EditParticipantController.class.getName() + ".FORM." + "command");
            
        } else {
            command = (ParticipantCommand) request.getSession().getAttribute(CreateParticipantController.class.getName() + ".FORM." + "command");

        }
        participant = command.getParticipant();
        if(!organizationId.equals(command.getOrganizationId())) {
            participant.removeAllStudyParticipantAssignments();
        }
        List<Integer> times = new ArrayList();
        for (int j = 1; j <= 12; j++) {
            times.add(j);
        }
        List<Integer> minutes = new ArrayList();
        for (int i = 0; i <60; i += 15) {
            minutes.add(i);
        }

        if (participant != null) {
            List<StudyParticipantAssignment> studyParticipantAssignments = new ArrayList<StudyParticipantAssignment>();
            studyParticipantAssignments = participant.getStudyParticipantAssignments();
            for(StudyParticipantAssignment spa : studyParticipantAssignments){
               StudySite existingStudySite = spa.getStudySite();
               if(existingStudySite != null) studySites.remove(existingStudySite);
            }
            
            String[] timeZones = TimeZone.getAvailableIDs();
            boolean showTime = false;
            boolean showWeb = false;
            if (participant.getStudyParticipantAssignments().size() > 0) {
                for (AppMode appMode : participant.getStudyParticipantAssignments().get(0).getSelectedAppModes()) {
                    if (appMode.equals(AppMode.IVRS)) {
                        showTime = true;
                    }
                    if (appMode.equals(AppMode.HOMEWEB)) {
                        showWeb = true;
                    }
                }
            }

            String blackoutStartTime = properties.getProperty(IVRS_BLACKOUT_START_TIME);
            String blackoutEndTime = properties.getProperty(IVRS_BLACKOUT_END_TIME);
             // Assigning default value for blackout period
            if(blackoutStartTime == null || blackoutStartTime.equals("") || blackoutEndTime ==null || blackoutEndTime.equals("") || !blackoutStartTime.contains(":") || !blackoutEndTime.contains(":")){
                blackoutStartTime = "21:00";
                blackoutEndTime = "04:59";
            }
            modelAndView.addObject("blackoutStartTime", blackoutStartTime);
            modelAndView.addObject("blackoutEndTime", blackoutEndTime);

            modelAndView.addObject("studyparticipantassignments", studyParticipantAssignments);
            modelAndView.addObject("hours", times);
            modelAndView.addObject("timezones", timeZones);
            modelAndView.addObject("minutes", minutes);
            modelAndView.addObject("showTime", showTime);
            modelAndView.addObject("showWeb", showWeb);
            modelAndView.addObject("command",command);
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

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}