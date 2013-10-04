package gov.nih.nci.ctcae.web.form;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.LeadStudySite;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.rules.CRFNotificationRule;
import gov.nih.nci.ctcae.core.domain.rules.NotificationRule;
import gov.nih.nci.ctcae.core.domain.rules.NotificationRuleCondition;
import gov.nih.nci.ctcae.core.domain.rules.NotificationRuleOperator;
import gov.nih.nci.ctcae.core.domain.rules.SiteCRFNotificationRule;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author AmeyS 
 * TestCases for AddFormRuleController.
 * Includes tests for adding new notificationRules to a CRF and 
 * adding new notificationRuleConditions to a notificationRule associated with a CRF
 */
public class AddFormRuleControllerTest extends WebTestCase {

    private AddFormRuleController controller;

    private CreateFormCommand command;
    private StudyOrganizationRepository studyOrganizationRepository;
    private List<CRFNotificationRule> crfNotificationRules;
    private CRFNotificationRule crfNotificationRule;
    private List<SiteCRFNotificationRule> siteCRFNotificationRules;
    private SiteCRFNotificationRule siteCRFNotificationRule;
    private NotificationRule notificationRule;
    private NotificationRuleCondition notificationRuleCondition;
    private Set<ProCtcQuestionType> proCtcQuestionTypes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studyOrganizationRepository = registerMockFor(StudyOrganizationRepository.class);
        controller = new AddFormRuleController();
        command = new CreateFormCommand();
        StudySite studySite = new StudySite();
        command.setMyOrg(studySite);
        crfNotificationRules = new ArrayList<CRFNotificationRule>();
        crfNotificationRule = new CRFNotificationRule();
        siteCRFNotificationRules = new ArrayList<SiteCRFNotificationRule>();
        siteCRFNotificationRule = new SiteCRFNotificationRule();
        notificationRule = new NotificationRule();
        proCtcQuestionTypes = new LinkedHashSet<ProCtcQuestionType>();
    }
    
    public void testAddCrfNotificationRules() throws Exception{
    	request.addParameter("action", "addRule");
    	request.addParameter("isSite", "false");
    	request.getSession().setAttribute(FormController.class.getName()+".FORM."+ "command", command);
    	
    	replayMocks();
    	ModelAndView modelAndView = controller.handleRequest(request, response);
    	verifyMocks();
    	
    	assertEquals("form/ajax/formRule", modelAndView.getViewName());
    	assertNotNull(modelAndView.getModel().get("rule"));
    	assertEquals(Boolean.TRUE, modelAndView.getModel().get("showOr"));
    	assertEquals(Integer.valueOf(0), modelAndView.getModel().get("ruleIndex"));
    	assertEquals(0, command.getMyOrg().getNotificationRules().size());
    	assertEquals(1, command.getCrf().getNotificationRules().size());
    }
    
    public void testAddSiteCRFNotificationRules() throws Exception{
    	request.addParameter("action", "addRule");
    	request.addParameter("isSite", "true");
    	request.getSession().setAttribute(FormController.class.getName()+".FORM."+ "command", command);
    	controller.setStudyOrganizationRepository(studyOrganizationRepository);
    	StudyOrganization studyOrganization = new LeadStudySite();
    	studyOrganization.setSiteCRFNotificationRules(siteCRFNotificationRules);
    	expect(studyOrganizationRepository.save(command.getMyOrg())).andReturn(command.getMyOrg());
    	
    	replayMocks();
    	ModelAndView modelAndView = controller.handleRequest(request, response);
    	verifyMocks();
    	
    	assertEquals("form/ajax/formRule", modelAndView.getViewName());
    	assertNotNull(modelAndView.getModel().get("rule"));
    	assertEquals(Boolean.TRUE, modelAndView.getModel().get("showOr"));
    	assertEquals(Integer.valueOf(0), modelAndView.getModel().get("ruleIndex"));
    	assertEquals(0, command.getCrf().getNotificationRules().size());
    	assertEquals(1, command.getMyOrg().getNotificationRules().size());
    }
    
    @SuppressWarnings("unchecked")
	public void testAddCrfNotificationRuleCondition() throws Exception{
    	request.addParameter("action", "addCondition");
    	request.addParameter("isSite", "false");
    	request.addParameter("ruleIndex", "0");
    	request.getSession().setAttribute(FormController.class.getName()+".FORM."+ "command", command);
    	crfNotificationRules.add(crfNotificationRule);
    	crfNotificationRule.setNotificationRule(notificationRule);
    	command.setCrf(registerMockFor(CRF.class));
    	proCtcQuestionTypes.add(ProCtcQuestionType.SEVERITY);
    	
    	assertEquals("expect no notificationRuleCondiditons", 0, notificationRule.getNotificationRuleConditions().size());
    	
    	expect(command.getCrf().getCrfNotificationRules()).andReturn(crfNotificationRules).anyTimes();
    	expect(command.getCrf().getAllQuestionTypes()).andReturn(proCtcQuestionTypes).anyTimes();
    	replayMocks();
    	ModelAndView modelAndView = controller.handleRequest(request, response);
    	verifyMocks();
    	
    	assertEquals("form/ajax/formRuleCondition", modelAndView.getViewName());
    	assertEquals(Boolean.TRUE, modelAndView.getModel().get("showOr"));
    	notificationRuleCondition = (NotificationRuleCondition) modelAndView.getModel().get("condition");
    	assertNotNull("expect one notificationRuleCondition being added to the notificationRule", notificationRuleCondition);
    	assertEquals("expect the notificationRuleCondition's proCtcQuestionType to be SEVERITY", ProCtcQuestionType.SEVERITY, notificationRuleCondition.getProCtcQuestionType());
    	assertEquals("expect 5 valid values correspondig to ProCtcQuestionType.SEVERITY", 5, ((List<NotificationRuleOperator>)modelAndView.getModel().get("thresholds")).size());
    }

    
    @SuppressWarnings("unchecked")
	public void testAddSiteCrfNotificationRuleCondition() throws Exception{
    	request.addParameter("action", "addCondition");
    	request.addParameter("isSite", "true");
    	request.addParameter("ruleIndex", "0");
    	request.getSession().setAttribute(FormController.class.getName()+".FORM."+ "command", command);
    	siteCRFNotificationRules.add(siteCRFNotificationRule);
    	siteCRFNotificationRule.setNotificationRule(notificationRule);
    	command.setMyOrg(registerMockFor(LeadStudySite.class));
    	command.setCrf(registerMockFor(CRF.class));
    	 proCtcQuestionTypes.add(ProCtcQuestionType.FREQUENCY);
    	
    	assertEquals("expect no notificationRuleCondiditons", 0, notificationRule.getNotificationRuleConditions().size());
    	
    	expect(command.getMyOrg().getSiteCRFNotificationRules()).andReturn(siteCRFNotificationRules).anyTimes();
    	expect(command.getCrf().getAllQuestionTypes()).andReturn(proCtcQuestionTypes).anyTimes();
    	replayMocks();
    	ModelAndView modelAndView = controller.handleRequest(request, response);
    	verifyMocks();
    	
    	assertEquals("form/ajax/formRuleCondition", modelAndView.getViewName());
    	assertEquals(Boolean.TRUE, modelAndView.getModel().get("showOr"));
    	notificationRuleCondition = (NotificationRuleCondition) modelAndView.getModel().get("condition");
    	assertNotNull("expect one notificationRuleCondition being added to the notificationRule", notificationRuleCondition);
    	assertEquals("expect the notificationRuleCondition's proCtcQuestionType to be FREQUENCY", ProCtcQuestionType.FREQUENCY, notificationRuleCondition.getProCtcQuestionType());
    	assertEquals("expect 5 valid values correspondig to ProCtcQuestionType.FREQUENCY", 5, ((List<NotificationRuleOperator>)modelAndView.getModel().get("thresholds")).size());
    }

    
}