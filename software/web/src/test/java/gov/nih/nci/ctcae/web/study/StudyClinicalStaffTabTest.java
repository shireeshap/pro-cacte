package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import org.springframework.validation.BindException;

/**
 * @author Amey
 * StudyClinicalStaffTabTest class
 */
public class StudyClinicalStaffTabTest extends AbstractWebIntegrationTestCase{
	StudyClinicalStaffTab tab;
	StudyCommand command;
	BindException errors;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		tab = new StudyClinicalStaffTab();
		command = new StudyCommand();
		tab.setGenericRepository(genericRepository);
		errors = registerMockFor(BindException.class);
	}
	
	public void testValidate() {
		command.setStudy(StudyTestHelper.getDefaultStudy());
		command.getLCRAIndexesToRemove().add(0);
		command.getOdcIndexesToRemove().add(0);
		command.getPiIndexesToRemove().add(0);
		int lcraCount = command.getStudy().getLeadCRAs().size();
		int odcCount = command.getStudy().getOverallDataCoordinators().size();
		int piCount = command.getStudy().getPrincipalInvestigators().size();
		
		tab.validate(command, errors);
		
		assertEquals((lcraCount - 1) , command.getStudy().getLeadCRAs().size());
		assertEquals((odcCount - 1), command.getStudy().getOverallDataCoordinators().size());
		assertEquals((piCount - 1), command.getStudy().getPrincipalInvestigators().size());
	}
}
