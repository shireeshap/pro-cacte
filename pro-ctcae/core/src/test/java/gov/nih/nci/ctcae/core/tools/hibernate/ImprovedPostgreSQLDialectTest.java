package gov.nih.nci.ctcae.core.tools.hibernate;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import org.hibernate.id.IdentityGenerator;

/**
 * @author Vinay Kumar
 * @crated Dec 11, 2008
 */
public class ImprovedPostgreSQLDialectTest extends AbstractTestCase {
	private ImprovedPostgreSQLDialect improvedPostgreSQLDialect;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		improvedPostgreSQLDialect = new ImprovedPostgreSQLDialect();


	}

	public void testGeneratorClass() {
		assertEquals(IdentityGenerator.class, improvedPostgreSQLDialect.getNativeIdentifierGeneratorClass());
	}
}
