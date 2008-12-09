package gov.nih.nci.ctcae.core.tools.hibernate;

import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.id.IdentityGenerator;

/**
 * @author Vinay Kumar
 * @crated Dec 9, 2008
 */
public class ImprovedPostgreSQLDialect extends PostgreSQLDialect {
	public Class getNativeIdentifierGeneratorClass() {
		return IdentityGenerator.class;
	}
}

