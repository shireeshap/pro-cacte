package gov.nih.nci.ctcae.web.form;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.CrfCreationMode;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.LeadStudySite;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.domain.rules.CRFNotificationRule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vinay Kumar
 * @since Nov 4, 2008
 */
public class CreateFormCommandTest extends WebTestCase {

    private ProCtcQuestion firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQustion, sixthQuestion;
    private CrfPageItem crfItem1Page, crfItem2Page, crfItem3Page, crfItem4Page;
    private ProCtcTerm proCtcTerm1, proCtcTerm2;
    ProCtcQuestionRepository proCtcQuestionRepository;
    private CreateFormCommand command;
    private GenericRepository genericRepository;
    private CRFNotificationRule crfNotificationRule;
    private CRF crf;
    private LeadStudySite leadStudySite;
    private OrganizationClinicalStaff organizationClinicalStaff;
    private ClinicalStaff clinicalStaff;
    private User user;
    private StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;
    private List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs;
    private List<UserRole> userRoles;
    private UserRole userRole;
    private Study study;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        command = new CreateFormCommand();
        command.setCrfPageNumbers("0,1");
        command.getCrf().setCrfCreationMode(CrfCreationMode.ADVANCE);
        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);

        firstQuestion = new ProCtcQuestion();
        firstQuestion.setId(11);
        firstQuestion.setQuestionText("sample question1", SupportedLanguageEnum.ENGLISH);

        secondQuestion = new ProCtcQuestion();
        secondQuestion.setQuestionText("sample question2", SupportedLanguageEnum.ENGLISH);
        secondQuestion.setId(12);

        thirdQuestion = new ProCtcQuestion();
        thirdQuestion.setQuestionText("sample question3", SupportedLanguageEnum.ENGLISH);
        thirdQuestion.setId(13);

        fourthQuestion = new ProCtcQuestion();
        fourthQuestion.setQuestionText("sample question4", SupportedLanguageEnum.ENGLISH);
        fourthQuestion.setId(14);

        crfItem1Page = new CrfPageItem();
        crfItem1Page.setProCtcQuestion(firstQuestion);
        crfItem2Page = new CrfPageItem();
        crfItem2Page.setProCtcQuestion(secondQuestion);
        crfItem3Page = new CrfPageItem();
        crfItem3Page.setProCtcQuestion(thirdQuestion);
        crfItem4Page = new CrfPageItem();
        crfItem4Page.setProCtcQuestion(fourthQuestion);

        fifthQustion = new ProCtcQuestion();
        fifthQustion.setId(15);
        fifthQustion.setQuestionText("sample question1", SupportedLanguageEnum.ENGLISH);

        sixthQuestion = new ProCtcQuestion();
        sixthQuestion.setId(16);
        sixthQuestion.setQuestionText("sample question6", SupportedLanguageEnum.ENGLISH);

        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTermEnglish("Fatigue", SupportedLanguageEnum.ENGLISH);
        proCtcTerm1.addProCtcQuestion(firstQuestion);
        proCtcTerm1.addProCtcQuestion(secondQuestion);
        proCtcTerm1.addProCtcQuestion(thirdQuestion);

        proCtcTerm2 = new ProCtcTerm();
        proCtcTerm2.setTermEnglish("Constipation", SupportedLanguageEnum.ENGLISH);
        proCtcTerm2.addProCtcQuestion(fourthQuestion);
        proCtcTerm2.addProCtcQuestion(sixthQuestion);
        
        genericRepository = registerMockFor(GenericRepository.class);
        crf = new CRF();
        crfNotificationRule = new CRFNotificationRule();
        leadStudySite = new LeadStudySite();
        leadStudySite.setId(11);
        
        user = new User();
        userRole = new UserRole();
        userRole.setRole(Role.PI);
        userRole.setUser(user);
        userRoles = new ArrayList<UserRole>();
        userRoles.add(userRole);
        user.setUserRoles(userRoles);
        clinicalStaff = new ClinicalStaff();
        clinicalStaff.setUser(user);
        
        
        organizationClinicalStaff = new OrganizationClinicalStaff();
        organizationClinicalStaff.setClinicalStaff(clinicalStaff);
        studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(organizationClinicalStaff);
        studyOrganizationClinicalStaff.setStudyOrganization(leadStudySite);
        
        studyOrganizationClinicalStaffs = new ArrayList<StudyOrganizationClinicalStaff>();
        studyOrganizationClinicalStaffs.add(studyOrganizationClinicalStaff);
    }
    
    public void testProcessRulesForForm(){
    	request.setParameter("ruleIndices", new String[]{"1"});
    	request.setParameter("notifications_1", "ADMIN");
    	request.setParameter("symptoms_1", "1");
    	request.setParameter("conditions_1", "1");
    	request.setParameter("questiontype_1_1", "SEVERITY");
    	request.setParameter("operator_1_1", "GREATER");
    	request.setParameter("threshold_1_1", "3");
    	command.setCrf(crf);
    	
    	expect(genericRepository.findById(ProCtcTerm.class, 1)).andReturn(proCtcTerm1).anyTimes();
    	expect(genericRepository.save(crf)).andReturn(crf).anyTimes();
    	
    	assertEquals(0, crf.getCrfNotificationRules().size());
    	replayMocks();
    	command.processRulesForForm(request, genericRepository);
    	verifyMocks();
    	assertEquals(1, crf.getCrfNotificationRules().size());
    }
    
    public void testProcessRulesForSite() throws Exception{
    	request.setParameter("ruleIds", new String[]{"1"});
    	request.setParameter("notifications_1", "ADMIN");
    	request.setParameter("symptoms_1", "1");
    	request.setParameter("conditions_1", "1");
    	request.setParameter("questiontype_1_1", "Severity");
    	request.setParameter("operator_1_1", "GREATER");
    	request.setParameter("threshold_1_1", "3");
    	command.setCrf(crf);
    	command.setMyOrg(leadStudySite);
    	
    	expect(genericRepository.findById(ProCtcTerm.class, 1)).andReturn(proCtcTerm1).anyTimes();
    	expect(genericRepository.save(leadStudySite)).andReturn(leadStudySite).anyTimes();
    	
    	assertEquals(0, command.getMyOrg().getSiteCRFNotificationRules().size());
    	replayMocks();
    	command.processRulesForSite(request, genericRepository);
    	verifyMocks();
    	assertEquals(1, command.getMyOrg().getSiteCRFNotificationRules().size());
    }
    
    public void testGetOrganizationForUser(){
    	LeadStudySite myOrg = new LeadStudySite();
    	study = registerMockFor(Study.class);
        command.getCrf().setStudy(study);
        expect(command.getCrf().getStudy().getStudySiteLevelStudyOrganizationClinicalStaffsByRole(Role.PI)).andReturn(studyOrganizationClinicalStaffs).anyTimes();
        
    	assertNotSame(myOrg, leadStudySite);
    	replayMocks();
    	myOrg = (LeadStudySite) command.getOrganizationForUser(user, Arrays.asList(Role.PI));
    	verifyMocks();
    	assertEquals(myOrg, leadStudySite);
    }


    public void testGetSelectedProCtcTerms() {
        assertFalse("both terms must be different", proCtcTerm1.equals(proCtcTerm2));
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        CRF crf = command.getCrf();
        command.updateCrfItems(proCtcQuestionRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());
        assertEquals("must have 5 page items", 5, crf.getAllCrfPageItems().size());

        validateCrfPageAndCrfPageItemOrder(crf);

        List<Integer> selectedProCtcTerms = command.getSelectedProCtcTerms();

        assertEquals("must have both terms as selected terms", 0, selectedProCtcTerms.size());
    }

    public void testGetSelectedProCtcTermsIfProCtCtermHasBeenAddedPartially() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        command.setQuestionIdToRemove(String.valueOf(fourthQuestion.getId()));
        CRF crf = command.getCrf();
        expect(proCtcQuestionRepository.findById(fourthQuestion.getId())).andReturn(fourthQuestion);
        replayMocks();

        command.updateCrfItems(proCtcQuestionRepository);
        verifyMocks();
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());

        validateCrfPageAndCrfPageItemOrder(crf);

        List<Integer> selectedProCtcTerms = command.getSelectedProCtcTerms();

        assertEquals("must not have 2nd term as all questions of second term have not been added yet", 0, selectedProCtcTerms.size());
    }

    public void testAddProCtcTermInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);
    }

    public void testAddMultipleProCtcTermInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        CRF crf = command.getCrf();
        command.updateCrfItems(proCtcQuestionRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());

        validateCrfPageAndCrfPageItemOrder(crf);
    }

    public void testReOrderCrfPagesInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        command.addProCtcTerm(proCtcTerm2);
        command.setCrfPageNumbers("1,0");

        CRF crf = command.getCrf();
        command.updateCrfItems(proCtcQuestionRepository);
        assertEquals("must have 2 pages", 2, crf.getCrfPagesSortedByPageNumber().size());

        CRFPage crfPage = crf.getCrfPageItemByQuestion(proCtcTerm1.getProCtcQuestions().iterator().next()).getCrfPage();
        assertEquals("must reorder crf page number", Integer.valueOf(1), crfPage.getPageNumber());

        crfPage = crf.getCrfPageItemByQuestion(proCtcTerm2.getProCtcQuestions().iterator().next()).getCrfPage();

        assertEquals("must reorder crf page number", Integer.valueOf(0), crfPage.getPageNumber());

        validateCrfPageAndCrfPageItemOrder(crf);
    }


    public void testRemoveQuestionInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        assertEquals("must have 1 pages", 1, crf.getCrfPagesSortedByPageNumber().size());

        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);

        //now remove 1 question
        command.setQuestionIdToRemove(String.valueOf(secondQuestion.getId()));
        expect(proCtcQuestionRepository.findById(secondQuestion.getId())).andReturn(secondQuestion);
        replayMocks();
        command.updateCrfItems(proCtcQuestionRepository);
        verifyMocks();
        assertEquals("must remove 1 crf page item", 2, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);
    }

    public void testRemoveAndAddSameQuestionInBasicMode() {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        command.addProCtcTerm(proCtcTerm1);
        CRF crf = command.getCrf();

        List<CrfPageItem> crfPageItems = crf.getCrfPagesSortedByPageNumber().get(0).getCrfPageItems();
        assertEquals("must have 3 crf page items", 3, crfPageItems.size());

        //now remove 1 question
        command.setQuestionIdToRemove(String.valueOf(secondQuestion.getId()));

        expect(proCtcQuestionRepository.findById(secondQuestion.getId())).andReturn(secondQuestion);
        replayMocks();
        command.updateCrfItems(proCtcQuestionRepository);
        verifyMocks();

        resetMocks();
        assertEquals("must remove 1 crf page item", 2, crfPageItems.size());

        //now add same question again
        assertEquals("must not add any more page", 1, crf.getCrfPagesSortedByPageNumber().size());

        command.addProCtcTerm(proCtcTerm1);
        assertEquals("must add only 1 question", 3, crfPageItems.size());
        validateCrfPageAndCrfPageItemOrder(crf);
    }


    public void testConstructor() {
        CRF crf = command.getCrf();
        assertNotNull("study crf must not be null", crf);
        assertNotNull("crf must not be null", crf);
    }

    public void testTitle() {
        assertEquals("Click here to name", command.getTitle());
    }
}
