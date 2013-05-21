package gov.nih.nci.ctcae.web.participant;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * @author Harsh Agarwal
 * @since Nov 25, 2008
 */
public class DisplayCalendarControllerTest extends WebTestCase {
    DisplayCalendarController controller;
    ParticipantSchedule participantSchedule;
    ProCtcAECalendar calendar;
    StudyParticipantAssignment spa;
    StudyParticipantCommand studyParticipantCommand;
    ParticipantCommand participantCommand;
    private User user;
    Map<String, List<Role>> userSpecificPrivilegeRoleMap;
    List<Role> roles;
    private Participant participant;
    private AuthorizationServiceImpl authorizationServiceImpl;
    private String PRIVILEGE_ENTER_PARTICIPANT_RESPONSE = "PRIVILEGE_ENTER_PARTICIPANT_RESPONSE";
    private String PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR = "PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR";
    

    public void reset() {
        resetMocks();
        controller = new DisplayCalendarController();
        request.setMethod("GET");
        request.setParameter("index", "0");
        studyParticipantCommand = registerMockFor(StudyParticipantCommand.class);
        participantCommand = registerMockFor(ParticipantCommand.class);
        participantSchedule = registerMockFor(ParticipantSchedule.class);
        spa = registerMockFor(StudyParticipantAssignment.class);
        authorizationServiceImpl = registerMockFor(AuthorizationServiceImpl.class);
        
        calendar = registerMockFor(ProCtcAECalendar.class);
        List l = new ArrayList();
        l.add(participantSchedule);
        request.getSession().setAttribute(ScheduleCrfController.class.getName() + ".FORM." + "command", studyParticipantCommand);
        request.getSession().setAttribute(CreateParticipantController.class.getName() + ".FORM." + "command", participantCommand);
        expect(participantCommand.getParticipantSchedules()).andReturn(l).anyTimes();
        expect(participantCommand.getSelectedStudyParticipantAssignment()).andReturn(spa).anyTimes();
        roles = new ArrayList<Role>();
        roles.add(Role.ADMIN);
        user = new User();
        userSpecificPrivilegeRoleMap = new HashMap<String, List<Role>>();
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR, roles);
        user.setUserSpecificPrivilegeRoleMap(userSpecificPrivilegeRoleMap);
        UsernamePasswordAuthenticationToken token = registerMockFor(UsernamePasswordAuthenticationToken.class);
        SecurityContextHolder.getContext().setAuthentication(token);
        participant =  new Participant();
        controller.setAuthorizationServiceImpl(authorizationServiceImpl);
    }

    public void testController() throws Exception {
        reset();
        request.setParameter("dir", "prev");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        expect(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).andReturn(user).anyTimes();
        expect(participantCommand.getParticipant()).andReturn(participant).anyTimes();
        expect(authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR)).andReturn(roles).anyTimes();
        expect(authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_ENTER_PARTICIPANT_RESPONSE)).andReturn(roles).anyTimes();
        expect(authorizationServiceImpl.hasRole(null, roles, user)).andReturn(true).anyTimes();
        participantCommand.lazyInitializeAssignment(null, false);
        calendar.add(-1);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("dir", "next");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        expect(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).andReturn(user).anyTimes();
        expect(participantCommand.getParticipant()).andReturn(participant).anyTimes();
        expect(authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR)).andReturn(roles).anyTimes();
        expect(authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_ENTER_PARTICIPANT_RESPONSE)).andReturn(roles).anyTimes();
        expect(authorizationServiceImpl.hasRole(null, roles, user)).andReturn(true).anyTimes();
        participantCommand.lazyInitializeAssignment(null, false);
        calendar.add(1);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

        reset();
        request.setParameter("dir", "refresh");
        expect(participantSchedule.getProCtcAECalendar()).andReturn(calendar);
        expect(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).andReturn(user).anyTimes();
        expect(participantCommand.getParticipant()).andReturn(participant).anyTimes();
        expect(authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR)).andReturn(roles).anyTimes();
        expect(authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_ENTER_PARTICIPANT_RESPONSE)).andReturn(roles).anyTimes();
        expect(authorizationServiceImpl.hasRole(null, roles, user)).andReturn(true).anyTimes();
        participantCommand.lazyInitializeAssignment(null, false);
        calendar.add(0);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();

    }
}