package gov.nih.nci.ctcae.web.study;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.LeadStudySite;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.HashSet;
import java.util.Set;

import org.easymock.classextension.EasyMock;
import org.springframework.validation.BindException;

public class StudySiteTabTest extends AbstractWebTestCase {
	StudySitesTab tab;
	Study study;
	StudyCommand command;
	BindException errors;
	StudyOrganizationRepository studyOrganizationRepository;
	protected Set<Object> mocks = new HashSet<Object>();
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		tab  = new StudySitesTab();
		study = StudyTestHelper.getDefaultStudy();
		command = new StudyCommand();
		command.setStudy(study);
		request.setParameter("studyId", "1");
		tab.setGenericRepository(genericRepository);
		errors = new BindException(command, this.getClass().getName());
		studyOrganizationRepository = registerMockFor(StudyOrganizationRepository.class);
		request.setMethod("");
	}
	
	public void testGetRequiredPrivilege(){
		login("ethan.basch@demo.com");
		assertEquals("PRIVILEGE_ADD_STUDY_SITE.study.1", tab.getRequiredPrivilege());
	}
	
	public void testValidate_remoteSite() {
		Integer siteIndexToRemove = getStudySiteId();
		command.getSiteIndexesToRemove().add(siteIndexToRemove);
		
		tab.validate(command, errors);
		
		assertTrue(errors.hasErrors());
		assertTrue(errors.getMessage().contains("NON_EMPTY_STUDY_SITE"));
	}
	
	public void testValidate_addLeadSite() {
		StudyOrganization studyOrganization = buildStudySiteWithLeadOrg();
		command.getStudy().getStudyOrganizations().add(studyOrganization);
		
		tab.validate(command, errors);
		
		assertTrue(errors.hasErrors());
		assertTrue(errors.getMessage().contains("LEAD_STUDY_SITE"));
	}
	
	public void testPostProcess() {
		StudyOrganization studyOrg = getExsistingStudySite();
		command.getStudy().addStudySite((StudySite) studyOrg);
		
		tab.postProcess(request, command, errors);
		
		assertTrue(errors.hasErrors());
	}
	
	public void testPostProcess_postAddingNewSite() {
		StudySite studySite = new StudySite();
		OrganizationQuery query = new OrganizationQuery();
		Organization org = organizationRepository.findSingle(query);
		
		OrganizationClinicalStaff ocs = new OrganizationClinicalStaff();
		ocs.setOrganization(org);
		studySite.setOrganization(org);
		command.getStudyOrganizationClinicalStaffs().get(0).getOrganizationClinicalStaff().getClinicalStaff().addOrganizationClinicalStaff(ocs);
		tab.setStudyOrganizationRepository(studyOrganizationRepository);
		expect(studyOrganizationRepository.save(studySite)).andReturn(new StudySite()).atLeastOnce();
		command.getStudy().addStudySite(studySite);
		
		replayMocks();
		tab.postProcess(request, command, errors);
		verifyMocks();
		
		assertFalse(errors.hasErrors());
	}
	
	private Integer getStudySiteId() {
		int indx = 0;
		for(StudySite studySite : command.getStudy().getStudySites()) {
			if(!(studySite instanceof LeadStudySite)) {
				return indx;
			}
			indx++;
		}
		return null;
	}
	
	private StudyOrganization buildStudySiteWithLeadOrg() {
		for(StudySite studySite : command.getStudy().getStudySites()) {
			if(studySite instanceof LeadStudySite) {
				StudySite ss = new StudySite();
				ss.setOrganization(studySite.getOrganization());
				return ss;
			}
		}
		return null;
	}
	
	private StudyOrganization getExsistingStudySite() {
		for(StudySite studySite : command.getStudy().getStudySites()) {
			if(!(studySite instanceof LeadStudySite)) {
				return studySite;
			}
		}
		return null;
	}
	
	 ////// MOCK REGISTRATION AND HANDLING
    public <T> T registerMockFor(Class<T> forClass) {
        return registered(EasyMock.createMock(forClass));
    }

    public void replayMocks() {
        for (Object mock : mocks) EasyMock.replay(mock);
    }

    public void verifyMocks() {
        for (Object mock : mocks) EasyMock.verify(mock);
    }

    public void resetMocks() {
        for (Object mock : mocks) EasyMock.reset(mock);
    }

    private <T> T registered(T mock) {
        mocks.add(mock);
        return mock;
    }

	
} 