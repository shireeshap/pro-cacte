package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.RequestContextUtils;

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
    public static final String BASE_URL = "base.url";

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
        String videoUrl = properties.getProperty(BASE_URL) + "/public/showVideo";
        if(lang.equals("es"))
            videoUrl = properties.getProperty(BASE_URL) + "/public/showVideo";
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
