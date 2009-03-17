package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.cabig.ctms.CommonsSystemException;
import gov.nih.nci.cabig.ctms.tools.spring.ControllerUrlResolver;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.security.PrivilegeAuthorizationCheck;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.form.CtcAeSecuredTabbedFlowController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public abstract class UrlAuthorizationIntegrationTestCase extends AbstractWebIntegrationTestCase {

    protected final String EDIT_STUDY_URL = "/pages/study/editStudy";
    protected final String CREATE_STUDY_URL = "/pages/study/createStudy";
    protected final String STUDY_URL = "/pages/study/*";
    protected final String SEARCH_STUDY_URL = "/pages/study/searchStudy";
    protected final String ADD_STUDY_SITE_CLINICAL_STAFF_URL = "/pages/study/addStudyComponent";
    protected final String ADD_STUDY_SITE_URL = "/pages/study/addStudySite";

    protected final String ADD_QUESTION_TO_FORM_URL = "/pages/form/addquestion";
    protected final String ADD_CRF_COMPONENT_URL = "/pages/form/addCrfComponent";
    protected final String ALL_CONDITIONS_URL = "/pages/form/allConditions";
    protected final String ADD_CONDITIONAL_QUESTION_URL = "/pages/form/addConditionalQuestion";
    protected final String REMOVE_CONDITIONS_URL = "/pages/form/removeConditions";
    protected final String SET_NAME_URL = "/pages/form/setName";
    protected final String SUBMIT_FORM_URL = "/pages/form/submit";

    protected final String CREATE_FORM_URL = "/pages/form/basicForm";
    protected final String EDIT_FORM_URL = "/pages/form/editForm";
    protected final String RELEASE_FORM_URL = "/pages/form/releaseForm";
    protected final String MANAGE_FORM_URL = "/pages/form/manageForm";
    protected final String MONITOR_FORM_URL = "/pages/participant/monitorForm";
    protected final String MONITOR_FORM_STATUS_URL = "/pages/participant/monitorFormStatus";
    protected final String VERSION_FORM_URL = "/pages/form/versionForm";
    protected final String SHOW_VERSION_FORM_URL = "/pages/form/showVersionForm";
    protected final String COPY_FORM_URL = "/pages/form/copyForm";
    protected final String DELETE_FORM_URL = "/pages/form/deleteForm";
    protected final String ADD_FORM_SCHEDULE_CYFLE_URL = "/pages/form/addFormScheduleCycle";

    protected final String ADD_ORGANIZATION_CLINICAL_STAFF_URL = "/pages/admin/clinicalStaff/addClinicalStaffComponent";
    protected final String SEARCH_CLINICAL_STAFF_URL = "/pages/admin/clinicalStaff/searchClinicalStaff";
    protected final String CREATE_CLINICAL_STAFF_URL = "/pages/admin/clinicalStaff/createClinicalStaff";
    protected final String CREATE_CCA_URL = "/pages/admin/clinicalStaff/createCCA";


    protected final String CREATE_PARTICIPANT_URL = "/pages/participant/create";
    protected final String ADD_NOTIFICATION_CLINICAL_STAFF_URL = "/pages/participant/addNotificationClinicalStaff";
    protected final String PARTICIPANT_DISPLAY_STUDY_SITES_URL = "/pages/participant/displaystudysites";

    protected final String SEARCH_PARTICIPANT_URL = "/pages/participant/search";

    protected final String SCHEDULE_CRF_URL = "/pages/participant/schedulecrf";
    protected final String ADD_CRF_SCHEDULE_URL = "/pages/participant/addCrfSchedule";
    protected final String DISPLAY_CALENDAR_URL = "/pages/participant/displaycalendar";

    protected final String EDIT_PARTICIPANT_URL = "/pages/participant/*";

    protected final String PARTICIPANT_INBOX_URL = "/pages/participant/participantInbox";

    protected UrlAuthorizationCheck urlAuthorizationCheck;

    protected PrivilegeAuthorizationCheck privilegeAuthorizationCheck;

    protected ControllerUrlResolver urlResolver;
    protected List<String> allUrls = new ArrayList();
    protected List<String> commonAllowedUrls = new ArrayList();
    private List<Tab> allTabs = new ArrayList<Tab>();


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        insertDefaultUsers();

        populateAllUrlsAndTabs();
        commonAllowedUrls.add("/pages/home");
        commonAllowedUrls.add("/j_spring_security_logout");
        commonAllowedUrls.add("/pages/confirmationCheck");
        allUrls.removeAll(commonAllowedUrls);


    }

    public void authorizeUser(User user, List<String> allowedUrls) {

        login(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertFalse("user must have authorities", authentication.getAuthorities().length == 0);

        for (String url : allowedUrls) {

            assertTrue(String.format("user %s can not access url:%s ", authentication.getName(), url), urlAuthorizationCheck.authorize(url));
            allUrls.remove(url);
        }


        //user must not see other urls;
        for (String url : allUrls) {
            assertFalse(String.format("user %s must not access url:%s ", authentication.getName(), url), urlAuthorizationCheck.authorize(url));
        }

    }

    public void authorizeUserForTab(User user, List<SecuredTab> allowedTabs) {
        login(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertFalse("user must have authorities", authentication.getAuthorities().length == 0);

        for (SecuredTab securedTab : allowedTabs) {

            assertTrue(String.format("user %s does not have permission:%s to acess %s tab", authentication.getName(), securedTab.getRequiredPrivilege(), securedTab.getClass()),
                    privilegeAuthorizationCheck.authorize(securedTab.getRequiredPrivilege()));

            removeTab(securedTab);

        }


        // //user must not see other urls;
        for (Tab tab : allTabs) {
            if (tab instanceof SecuredTab) {
                SecuredTab securedTab = (SecuredTab) tab;
                assertFalse(String.format("user %s  must not have permission:%s to access %s tab", authentication.getName(), securedTab.getRequiredPrivilege(), securedTab.getClass()),
                        privilegeAuthorizationCheck.authorize(securedTab.getRequiredPrivilege()));
            }
        }
    }

    private void removeTab(SecuredTab securedTab) {

        List<Tab> tabsToRemove = new ArrayList<Tab>();
        for (Tab tab : allTabs) {
            if (StringUtils.equals(tab.getClass().getName(), securedTab.getClass().getName())) {
                tabsToRemove.add(tab);
            }

        }
        allTabs.removeAll(tabsToRemove);

    }


    public void populateAllUrlsAndTabs() {

        urlResolver = (ControllerUrlResolver) webApplicationContext.getBean("urlResolver");

        Map controllerBeans = webApplicationContext.getBeansOfType(Controller.class);

        assertFalse("must find controller beans", controllerBeans.isEmpty());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication.getAuthorities());
        for (Object controllerBean : controllerBeans.keySet()) {
            try {
                String url = urlResolver.resolve(String.valueOf(controllerBean)).getUrl(true);

                allUrls.add(url);

            } catch (CommonsSystemException e) {

            }
        }

        Map<String, CtcAeSecuredTabbedFlowController> securedTabbedFlowControllers = webApplicationContext.getBeansOfType(CtcAeSecuredTabbedFlowController.class);
        for (CtcAeSecuredTabbedFlowController securedTabbedFlowController : securedTabbedFlowControllers.values()) {

            Flow flow = securedTabbedFlowController.getFlow();
            allTabs.addAll(flow.getTabs());

        }
        assertFalse("must find secured tab", allTabs.isEmpty());


    }


    @Required
    public void setUrlAuthorizationCheck(UrlAuthorizationCheck urlAuthorizationCheck) {
        this.urlAuthorizationCheck = urlAuthorizationCheck;
    }


    @Required
    public void setPrivilegeAuthorizationCheck(PrivilegeAuthorizationCheck privilegeAuthorizationCheck) {
        this.privilegeAuthorizationCheck = privilegeAuthorizationCheck;
    }
}