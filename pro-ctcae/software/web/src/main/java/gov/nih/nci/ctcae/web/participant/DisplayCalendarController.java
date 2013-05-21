package gov.nih.nci.ctcae.web.participant;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//
/**
 * The Class DisplayCalendarController.
 *
 * @author Harsh Agarwal
 * @since Nov 5, 2008
 */
public class DisplayCalendarController extends AbstractController {

    GenericRepository genericRepository;
    AuthorizationServiceImpl authorizationServiceImpl;
    private String PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR = "PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR";
    private String PRIVILEGE_ENTER_PARTICIPANT_RESPONSE = "PRIVILEGE_ENTER_PARTICIPANT_RESPONSE";

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/displaycalendar");
        ParticipantCommand participantCommand = ParticipantControllerUtils.getParticipantCommand(request);
        participantCommand.lazyInitializeAssignment(genericRepository,false);
        Integer index = Integer.parseInt(request.getParameter("index"));
        ParticipantSchedule participantSchedule = participantCommand.getParticipantSchedules().get(index);
        String direction = request.getParameter("dir");

        if (direction.equals("prev")) {
            participantSchedule.getProCtcAECalendar().add(-1);
        }
        if (direction.equals("next")) {
            participantSchedule.getProCtcAECalendar().add(1);
        }
        if (direction.equals("refresh")) {
            participantSchedule.getProCtcAECalendar().add(0);
        }
        
        boolean hasShowCalendarActionsPrivilege = false;
        boolean hasEnterResponsePrivilege = false;

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Study currentStudy = AuthorizationServiceImpl.getStudy(participantCommand.getParticipant());

        List<Role> roles = authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR);
        hasShowCalendarActionsPrivilege = authorizationServiceImpl.hasRole(currentStudy, roles, user);
        roles = authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_ENTER_PARTICIPANT_RESPONSE);
        hasEnterResponsePrivilege = authorizationServiceImpl.hasRole(currentStudy, roles, user);
        
       	modelAndView.addObject("hasShowCalendarActionsPrivilege",hasShowCalendarActionsPrivilege);
        modelAndView.addObject("hasEnterResponsePrivilege", hasEnterResponsePrivilege);
        
        modelAndView.addObject("participantSchedule", participantCommand.getParticipantSchedules().get(index));
        modelAndView.addObject("spa", participantCommand.getSelectedStudyParticipantAssignment());
        modelAndView.addObject("index", index);

        return modelAndView;
    }


    /**
     * Instantiates a new display calendar controller.
     */
    public DisplayCalendarController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
    
    @Required
    public void setAuthorizationServiceImpl(AuthorizationServiceImpl authorizationServiceImpl) {
        this.authorizationServiceImpl = authorizationServiceImpl;
    }
}