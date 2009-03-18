package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.ConfigAttributeDefinition;

/**
 * @author
 */
public abstract class WebTestCase extends AbstractTestCase {
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletContext servletContext;
    protected MockHttpSession session;
    protected ControllerTools controllerTools;
    protected StudyRepository studyRepository;
    protected PrivilegeAuthorizationCheck privilegeAuthorizationCheck;
    protected ProCtcQuestionRepository proCtcQuestionRepository;
    protected StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = new MockServletContext();
        session = new MockHttpSession(servletContext);
        request = new MockHttpServletRequest(servletContext);
        request.setMethod("POST");
        request.setSession(session);
        response = new MockHttpServletResponse();
        controllerTools = new ControllerTools();
        studyRepository = registerMockFor(StudyRepository.class);
        privilegeAuthorizationCheck = registerMockFor(PrivilegeAuthorizationCheck.class);
        expect(privilegeAuthorizationCheck.authorize(isA(String.class))).andReturn(true).anyTimes();
        expect(privilegeAuthorizationCheck.authorize(isA(ConfigAttributeDefinition.class))).andReturn(true).anyTimes();

        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);
        studyParticipantCrfScheduleRepository = registerMockFor(StudyParticipantCrfScheduleRepository.class);


    }

//    protected void login(Object currentLoggedInUser) {
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(currentLoggedInUser, "password");
//        SecurityContextHolder.getContext().setAuthentication(token);
//    }
}
