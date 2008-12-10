package gov.nih.nci.ctcae.core;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.Date;

public abstract class AbstractHibernateIntegrationTestCase extends AbstractTransactionalDataSourceSpringContextTests {


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


	protected void login() {
//        user = userRepository.loadUserByUsername("saurabh1@abc.com");
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "password");
//
//        SecurityContextHolder.getContext().setAuthentication(token);


	}

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
		DataAuditInfo.setLocal(auditInfo);

	}

	@Override
	protected void onTearDownInTransaction() throws Exception {
		DataAuditInfo.setLocal(null);

		super.onTearDownInTransaction();


	}


}
