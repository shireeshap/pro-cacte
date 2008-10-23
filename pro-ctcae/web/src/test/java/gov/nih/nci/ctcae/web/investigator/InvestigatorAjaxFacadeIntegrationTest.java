package gov.nih.nci.ctcae.web.investigator;

import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.core.repository.InvestigatorRepository;
import gov.nih.nci.ctcae.core.domain.Investigator;
import gov.nih.nci.ctcae.core.Fixture;

import java.util.Map;
import java.util.HashMap;

import org.springframework.webflow.core.collection.ParameterMap;


/**
 * @author Mehul Gulati
 * Date: Oct 23, 2008
 */
public class InvestigatorAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private InvestigatorAjaxFacade investigatorAjaxFacade;
    protected Map parameterMap;
    private InvestigatorRepository investigatorRepository;
    private Investigator investigator;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        investigator = Fixture.createInvestigator("Mehul", "Gulati", "1234");

        investigator = investigatorRepository.save(investigator);
    }

    public void testSearchInvestigatorByFirstName() {



                        
        String table = investigatorAjaxFacade.searchInvestigator(parameterMap,"meh","gul","12", request);
        assertNotNull(table);
        assertTrue("must find atleast investigator matching with first name", table.contains(investigator.getFirstName()));
        assertTrue("must find atleast investigator matching with last name", table.contains(investigator.getLastName()));
        assertTrue("must find atleast investigator matching with nci identifier", table.contains(investigator.getNciIdentifier()));


        table = investigatorAjaxFacade.searchInvestigator(parameterMap,"fff","fff","ff", request);
        assertNotNull(table);
        assertFalse("must find atleast investigator matching with first name", table.contains(investigator.getFirstName()));


    }

    public void setInvestigatorAjaxFacade(InvestigatorAjaxFacade investigatorAjaxFacade) {
        this.investigatorAjaxFacade = investigatorAjaxFacade;
    }

    public void setInvestigatorRepository(InvestigatorRepository investigatorRepository) {
        this.investigatorRepository = investigatorRepository;
    }
}
