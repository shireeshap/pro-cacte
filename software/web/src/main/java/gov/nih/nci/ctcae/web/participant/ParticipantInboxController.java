package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

//
/**
 * The Class ParticipantInboxController.
 *
 * @author Mehul Gulati
 *         Date: Nov 17, 2008
 */
public class ParticipantInboxController extends CtcAeSimpleFormController {

    /**
     * The participant repository.
     */
    private UserRepository userRepository;

    private Properties properties;

    public static final String HELP_VIDEO_URL_EN  = "help.video.url.en";
    public static final String HELP_VIDEO_URL_ES  = "help.video.url.es";

    /**
     * Instantiates a new participant inbox controller.
     */
    public ParticipantInboxController() {
        super();
        setCommandClass(gov.nih.nci.ctcae.core.domain.Participant.class);
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Participant participant = userRepository.findParticipantForUser(user);
        if (participant == null) {
            throw new CtcAeSystemException("Can not find participant for username " + user.getUsername());
        }
        sortSchedules(participant);
        return participant;
    }

    private void sortSchedules(Participant participant){
        List<StudyParticipantCrfSchedule>   inProgressStudyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>();
         List<StudyParticipantCrfSchedule>   scheduledStudyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>();
        for(StudyParticipantAssignment assignment: participant.getStudyParticipantAssignments()){
              for(StudyParticipantCrf crf: assignment.getStudyParticipantCrfs()){
                 for(StudyParticipantCrfSchedule schedule: crf.getStudyParticipantCrfSchedules()){
                     if(schedule.getStatus().equals(CrfStatus.SCHEDULED))
                      scheduledStudyParticipantCrfSchedules.add(schedule);
                     if(schedule.getStatus().equals(CrfStatus.INPROGRESS))
                         inProgressStudyParticipantCrfSchedules.add(schedule);
                 }
              }
        }
        Collections.sort(inProgressStudyParticipantCrfSchedules);
        Collections.sort(scheduledStudyParticipantCrfSchedules);
        inProgressStudyParticipantCrfSchedules.addAll(scheduledStudyParticipantCrfSchedules);

        participant.setSortedStudyParticipantCrfSchedules(inProgressStudyParticipantCrfSchedules);
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map<String, Object> modelAndView = super.referenceData(request,command, errors);
        String lang = request.getParameter("lang");
        if(lang==null || lang.equals("")){
            Locale locale = RequestContextUtils.getLocale(request);
            if(locale != null && locale.equals(new Locale("es"))){
                lang="es";
            }else{
                lang="en";
            }
        }
        String videoUrl = properties.getProperty(HELP_VIDEO_URL_EN);
        if(lang.equals("es"))
            videoUrl = properties.getProperty(HELP_VIDEO_URL_ES);
        modelAndView.put("videoUrl",videoUrl);
        return modelAndView;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
