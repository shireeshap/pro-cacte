package gov.nih.nci.ctcae.core;

import org.springframework.test.jpa.AbstractJpaTests;

import java.util.StringTokenizer;

public abstract class AbstractJpaIntegrationTestCase extends AbstractJpaTests {


    @Override
    protected String[] getConfigLocations() {

        return new String[]{
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",

                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-mail.xml",
                "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
                "classpath*:gov/nih/nci/ctcae/core/resourceContext-job.xml",
//                "classpath*:gov/nih/nci/ctcae/core/applicationContext-core-security.xml",
                "classpath*:" + "/*-context-test.xml"};
    }

    private String getFullyQualifiedPackageName(String packageName) {
        StringBuffer buffer = new StringBuffer("");
        StringTokenizer tokenizer = new StringTokenizer(packageName, ".");
        while (tokenizer.hasMoreTokens()) {
            buffer = buffer.append("/" + tokenizer.nextToken());
        }

        return buffer.toString();
    }

    protected void login() {
//        user = userRepository.loadUserByUsername("saurabh1@abc.com");
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "password");
//
//        SecurityContextHolder.getContext().setAuthentication(token);


    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


    }


}
