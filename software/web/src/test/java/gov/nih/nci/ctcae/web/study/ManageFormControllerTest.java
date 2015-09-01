package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.form.CrfAjaxFacade;
import gov.nih.nci.ctcae.web.form.FetchCrfController;
import gov.nih.nci.ctcae.web.form.ManageFormController;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import static org.easymock.EasyMock.*;


/**
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class ManageFormControllerTest extends WebTestCase {

    private ManageFormController controller;
    private Study study;
    CrfAjaxFacade crfAjaxFacade;
    StudyAjaxFacade studyAjaxFacade;
    private FetchCrfController fetchCrfController;
    private CRF crf1;
    private CRF crf2;
    private List<CRF> crfs= new ArrayList<CRF>();
    private UsernamePasswordAuthenticationToken currentUser;
    private User user;
    Map<String, List<Role>> userSpecificPrivilegeRoleMap;
    List<Role> roles;
    private static String PRIVILEGE_VERSION_FORM = "PRIVILEGE_VERSION_FORM";
    private static String PRIVILEGE_VIEW_FORM = "PRIVILEGE_VIEW_FORM";
    private static String PRIVILEGE_COPY_FORM = "PRIVILEGE_COPY_FORM";
    private static String PRIVILEGE_RELEASE_FORM = "PRIVILEGE_RELEASE_FORM";
    private static String PRIVILEGE_DELETE_FORM = "PRIVILEGE_DELETE_FORM";
    private static String PRIVILEGE_EDIT_FORM = "PRIVILEGE_EDIT_FORM";


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        crfAjaxFacade = registerMockFor(CrfAjaxFacade.class);
        studyAjaxFacade = registerMockFor(StudyAjaxFacade.class);

        controller = new ManageFormController();
        fetchCrfController = new FetchCrfController();
        controller.setStudyRepository(studyRepository);
        controller.setCrfAjaxFacade(crfAjaxFacade);
        controller.setStudyAjaxFacade(studyAjaxFacade);
        fetchCrfController.setCrfAjaxFacade(crfAjaxFacade);
        fetchCrfController.setAuthorizationServiceImpl(authorizationServiceImpl);
        roles = new ArrayList<Role>();
        user = new User();
        userSpecificPrivilegeRoleMap = new HashMap<String, List<Role>>();
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_VERSION_FORM, roles);
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_VIEW_FORM, roles);
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_COPY_FORM, roles);
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_RELEASE_FORM, roles);
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_DELETE_FORM, roles);
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_EDIT_FORM, roles);
        user.setUserSpecificPrivilegeRoleMap(userSpecificPrivilegeRoleMap);
        currentUser = new UsernamePasswordAuthenticationToken(user, "Password@2");
        study = new Study();
        study.setId(1);
        study.setShortTitle("fake study");
        crf1 = new CRF();
        crf1.setId(1);
        crf1.setTitle("Alerts");
        crf1.setStudy(study);
        crf2 = new CRF();
        crf2.setId(2);
        crf2.setTitle("Form 1");
        crf2.setStudy(study);
        crfs.add(crf1);
        crfs.add(crf2);

    }


    public void testHandleRequest() throws Exception {

        request.addParameter("startIndex", "0");
        request.addParameter("results", "5");
        request.addParameter("sort", "title");
        request.addParameter("dir", "asc");
        String[] searchStrings = null;
        SecurityContextHolder.getContext().setAuthentication(currentUser);

        String searchString = (String) request.getSession().getAttribute("crfSearchString");
        if (!StringUtils.isBlank(searchString)) {
            searchString.trim();
            searchStrings = searchString.split("\\s+");
        }
        request.getSession().setAttribute("crfSearchString", "Alerts fake");
        expect(crfAjaxFacade.resultCount(isA(String[].class))).andReturn(2L);
        expect(crfAjaxFacade.searchCrfs(isA(String[].class), eq(0), eq(5), eq("title"), eq("asc"), eq(2L))).andReturn(crfs);
        expect(authorizationServiceImpl.hasRole(study, new ArrayList<Role>(), user)).andReturn(true).anyTimes();
        expect(authorizationServiceImpl.hasAccessForStudyInstance(user, study, PRIVILEGE_VERSION_FORM)).andReturn(true).anyTimes();
        expect(authorizationServiceImpl.hasAccessForStudyInstance(user, study, PRIVILEGE_VIEW_FORM)).andReturn(true).anyTimes();
        expect(authorizationServiceImpl.hasAccessForStudyInstance(user, study, PRIVILEGE_COPY_FORM)).andReturn(true).anyTimes();
        expect(authorizationServiceImpl.hasAccessForStudyInstance(user, study, PRIVILEGE_RELEASE_FORM)).andReturn(true).anyTimes();
        expect(authorizationServiceImpl.hasAccessForStudyInstance(user, study, PRIVILEGE_DELETE_FORM)).andReturn(true).anyTimes();
        expect(authorizationServiceImpl.hasAccessForStudyInstance(user, study, PRIVILEGE_EDIT_FORM)).andReturn(true).anyTimes();
        
        replayMocks();
        ModelAndView modelAndView = fetchCrfController.handleRequest(request, response);
        Object src = modelAndView.getModel().get("shippedRecordSet");

        verifyMocks();
        assertNotNull(modelAndView);
        assertNotNull(src);

    }

}