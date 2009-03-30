package gov.nih.nci.ctcae.web.rules;

import org.drools.repository.RulesRepository;
import gov.nih.nci.ctcae.web.rules.AbstractRulesTest;


public class RulesCheckRepositoryTest extends AbstractRulesTest {

    public void testRepositoryCreation() throws Exception {
        RulesRepository rulesRepository = jcrService.getRulesRepository();
        assertNotNull(rulesRepository);
    }
}
