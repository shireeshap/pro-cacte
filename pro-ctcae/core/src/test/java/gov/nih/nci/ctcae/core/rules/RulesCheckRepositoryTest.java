package gov.nih.nci.ctcae.core.rules;

import org.drools.repository.RulesRepository;


public class RulesCheckRepositoryTest extends AbstractRulesTest {

    public void testRepositoryCreation() throws Exception {
        RulesRepository rulesRepository = jcrService.getRulesRepository();
        assertNotNull(rulesRepository);
    }
}
