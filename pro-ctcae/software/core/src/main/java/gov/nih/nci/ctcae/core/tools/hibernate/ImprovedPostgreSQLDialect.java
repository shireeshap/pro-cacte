package gov.nih.nci.ctcae.core.tools.hibernate;

import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.id.IdentityGenerator;

//
/**
 * The Class ImprovedPostgreSQLDialect.
 *
 * @author Vinay Kumar
 * @since Dec 9, 2008
 */
public class ImprovedPostgreSQLDialect extends PostgreSQLDialect {

    /* (non-Javadoc)
      * @see org.hibernate.dialect.PostgreSQLDialect#getNativeIdentifierGeneratorClass()
      */
    public Class getNativeIdentifierGeneratorClass() {
        return IdentityGenerator.class;
    }
}

