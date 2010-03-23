package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

/**
 * @author Vinay Kumar
 * @since Nov 5, 2008
 */
public class StudyControllerTest extends WebTestCase {
    private StudyController controller;
    private WebControllerValidator validator;
    private UserRepository userRepository;
    private Study study;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new CreateStudyController();
        validator = new WebControllerValidatorImpl();
        controller.setStudyRepository(studyRepository);
        controller.setWebControllerValidator(validator);
        controller.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        userRepository = registerMockFor(UserRepository.class);
        controller.setUserRepository(userRepository);
        UsernamePasswordAuthenticationToken token = registerMockFor(UsernamePasswordAuthenticationToken.class);
        SecurityContextHolder.getContext().setAuthentication(token);
        study = new Study();
        User user = new User();
        expect(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).andReturn(user).anyTimes();
        ClinicalStaff clinicalStaff = registerMockFor(ClinicalStaff.class);
        expect(userRepository.findClinicalStaffForUser(user)).andReturn(clinicalStaff);
        ArrayList<Organization> organizations = new ArrayList<Organization>();
        organizations.add(new Organization());
        expect(clinicalStaff.getOrganizationsWithCCARole()).andReturn(organizations);


    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        replayMocks();
        ((StudyDetailsTab) (controller.getFlow().getTab(0))).setUserRepository(userRepository);
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof StudyCommand);

    }

    public void testPostRequest() throws Exception {
        request.setMethod("GET");

        replayMocks();
        ((StudyDetailsTab) (controller.getFlow().getTab(0))).setUserRepository(userRepository);
        controller.handleRequest(request, response);
        verifyMocks();
        resetMocks();
        assertNotNull("must have command in session", ControllersUtils.getStudyCommand(request));
        request.setMethod("POST");
        request.setParameter("_finish", "true");
        expect(studyRepository.save(isA(Study.class))).andReturn(study);
        expect(privilegeAuthorizationCheck.authorize(isA(String.class))).andReturn(true).anyTimes();
        expect(privilegeAuthorizationCheck.authorize(isA(ConfigAttributeDefinition.class))).andReturn(true).anyTimes();

        replayMocks();

        ModelAndView modelAndView = controller.handleRequest(request, response);

        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must  find command object", command);


    }

    public void testPostRequestForSite() throws Exception {
        request.setMethod("GET");

        replayMocks();
        ((StudyDetailsTab) (controller.getFlow().getTab(0))).setUserRepository(userRepository);
        
        controller.handleRequest(request, response);
        verifyMocks();
        resetMocks();
        assertNotNull("must have command in session", ControllersUtils.getStudyCommand(request));

        expect(privilegeAuthorizationCheck.authorize(isA(String.class))).andReturn(true).anyTimes();
        expect(privilegeAuthorizationCheck.authorize(isA(ConfigAttributeDefinition.class))).andReturn(true).anyTimes();

        request.setMethod("POST");
        request.setAttribute(controller.getClass().getName() + ".PAGE." + controller.getCommandName(), 1);
        expect(studyRepository.save(study)).andReturn(study);
        replayMocks();

        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must  find command object", command);


    }
}