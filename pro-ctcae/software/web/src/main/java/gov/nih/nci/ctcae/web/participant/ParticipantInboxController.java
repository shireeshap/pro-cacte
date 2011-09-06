package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    protected Map referenceData(HttpServletRequest request) throws Exception {
        String lang = request.getParameter("lang");
        return super.referenceData(request);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
