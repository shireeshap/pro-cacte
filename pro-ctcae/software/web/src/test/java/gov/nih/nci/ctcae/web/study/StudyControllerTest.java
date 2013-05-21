package gov.nih.nci.ctcae.web.study;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.service.UserRoleService;
import gov.nih.nci.ctcae.core.service.UserRoleServiceImpl;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueIdentifierForStudyValidator;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @since Nov 5, 2008
 */
public class StudyControllerTest extends WebTestCase {
    private StudyController controller;
    private WebControllerValidator validator;
    private UserRoleService userRoleService;
    private Study study;
    private UniqueIdentifierForStudyValidator uniqueIdentifierForStudyValidator;
    private User user;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new CreateStudyController();
        validator = new WebControllerValidatorImpl();

        UserRole userRole = new UserRole();
        userRole.setRole(Role.ADMIN);
        user = new User();
        user.addUserRole(userRole);

        controller.setStudyRepository(studyRepository);

        controller.setWebControllerValidator(validator);
        controller.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        userRoleService = registerMockFor(UserRoleServiceImpl.class);
        userRepository = registerMockFor(UserRepository.class);
        uniqueIdentifierForStudyValidator = registerMockFor(UniqueIdentifierForStudyValidator.class);
        controller.setUserRoleService(userRoleService);
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
        request.setSession(session);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
        expect(uniqueIdentifierForStudyValidator.validateUniqueIdentifier("",null)).andReturn(true);
        expect(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).andReturn(user).anyTimes();
        replayMocks();

        ((StudyDetailsTab) (controller.getFlow().getTab(0))).setUniqueIdentifierForStudyValidator(uniqueIdentifierForStudyValidator);

        ModelAndView modelAndView = controller.handleRequest(request, response);

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
        expect(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).andReturn(user).anyTimes();

        request.setMethod("POST");
        request.setAttribute(controller.getClass().getName() + ".PAGE." + controller.getCommandName(), 1);
        userRoleService.addUserRoleForUpdatedLCRAorPI(study);
        expect(studyRepository.save(study)).andReturn(study);
        replayMocks();


        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must  find command object", command);


    }
}