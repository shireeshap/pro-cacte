package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.utils.RuleUtil;
import com.semanticbits.rules.utils.RepositoryCleaner;
import com.semanticbits.rules.api.*;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.beans.factory.annotation.Required;
import org.drools.repository.RulesRepository;
import gov.nih.nci.ctcae.web.rules.ProCtcAERulesService;


public class AbstractRulesTest extends AbstractDependencyInjectionSpringContextTests {

    protected RepositoryService jcrService;
    protected RuleAuthoringService ruleAuthoringService;
    protected RuleDeploymentService ruleDeploymentService;
    protected BusinessRulesExecutionService ruleExecutionService;
    protected RulesEngineService ruleEngineService;
    protected String packageName;
    protected String subject;
    protected RepositoryCleaner repositoryCleaner;
    protected ProCtcAERulesService proCtcAERulesService;
    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-rules-*.xml"
    };

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        String packagePrefix = "gov.nih.nci.cabig.caaers.rules.institution";
        String domainObjectValue = "National Cancer Institute";
        String ruleSetName = "Test RuleSet";
        String ruleType = "Institution rules";

        packageName = RuleUtil.getPackageName(packagePrefix, domainObjectValue, ruleSetName);
        subject = ruleType + "||" + domainObjectValue;
    }

    @Override
    protected String[] getConfigLocations() {
        return context;
    }

    @Required
    public RepositoryService getJcrService() {
        return jcrService;
    }

    @Required
    public void setJcrService(RepositoryService jcrService) {
        this.jcrService = jcrService;
    }

    @Required
    public RuleAuthoringService getRuleAuthoringService() {
        return ruleAuthoringService;
    }

    @Required
    public void setRuleAuthoringService(RuleAuthoringService ruleAuthoringService) {
        this.ruleAuthoringService = ruleAuthoringService;
    }

    @Required
    public RuleDeploymentService getRuleDeploymentService() {
        return ruleDeploymentService;
    }

    @Required
    public void setRuleDeploymentService(RuleDeploymentService ruleDeploymentService) {
        this.ruleDeploymentService = ruleDeploymentService;
    }

    @Required
    public BusinessRulesExecutionService getRuleExecutionService() {
        return ruleExecutionService;
    }

    @Required
    public void setRuleExecutionService(BusinessRulesExecutionService ruleExecutionService) {
        this.ruleExecutionService = ruleExecutionService;
    }

    @Required
    public RulesEngineService getRuleEngineService() {
        return ruleEngineService;
    }

    @Required
    public void setRuleEngineService(RulesEngineService ruleEngineService) {
        this.ruleEngineService = ruleEngineService;
    }

    @Required
    public RepositoryCleaner getRepositoryCleaner() {
        return repositoryCleaner;
    }

    @Required
    public void setRepositoryCleaner(RepositoryCleaner repositoryCleaner) {
        this.repositoryCleaner = repositoryCleaner;
    }

    @Required
    public ProCtcAERulesService getProCtcAERulesService() {
        return proCtcAERulesService;
    }

    @Required
    public void setProCtcAERulesService(ProCtcAERulesService proCtcAERulesService) {
        this.proCtcAERulesService = proCtcAERulesService;
    }

}