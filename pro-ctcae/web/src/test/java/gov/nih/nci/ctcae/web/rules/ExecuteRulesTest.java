package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.brxml.RuleSet;

import java.rmi.RemoteException;


public class ExecuteRulesTest extends AbstractRulesTest {

    RuleSet ruleSet;

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNotNull(ruleSet);

    }


    public void testDeploy() {
        try {
            ruleDeploymentService.registerRuleSet("aa", ruleSet.getName());
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

//    public void testImort() throws IOException {
//        String fileName = "C:\\etc\\ctcae\\gov.nih.nci.cabig.caaers.rules.institution.national_cancer_institute.test_ruleset.xml";
//        BufferedReader br = null;
//        File f = new File(fileName);
//        StringBuilder sb = new StringBuilder();
//        FileReader fr = new FileReader(f);
//        br = new BufferedReader(fr);
//        String line;
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        String xml = sb.toString();
//        RuleSet ruleSet = (RuleSet) XMLUtil.unmarshal(xml);
//        ruleEngineService.
//    }

}