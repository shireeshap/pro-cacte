package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.ConfigAttributeDefinition;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

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
    protected UserRepository userRepository;
    protected AuthorizationServiceImpl authorizationServiceImpl;


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
        authorizationServiceImpl = registerMockFor(AuthorizationServiceImpl.class);
        privilegeAuthorizationCheck = registerMockFor(PrivilegeAuthorizationCheck.class);
        expect(privilegeAuthorizationCheck.authorize(isA(String.class))).andReturn(true).anyTimes();
        expect(privilegeAuthorizationCheck.authorize(isA(ConfigAttributeDefinition.class))).andReturn(true).anyTimes();

        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);
        studyParticipantCrfScheduleRepository = registerMockFor(StudyParticipantCrfScheduleRepository.class);
    }
}
