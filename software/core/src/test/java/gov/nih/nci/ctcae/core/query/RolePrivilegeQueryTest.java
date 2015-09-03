package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author Harsh Agarwal
 * @since Oct 7, 2008
 */
public class RolePrivilegeQueryTest extends AbstractDependencyInjectionSpringContextTests {

    private GenericRepository genericRepository;

    private static final String[] context = new String[]{
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-core.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-datasource.xml",
            "classpath*:gov/nih/nci/ctcae/core/applicationContext-util.xml",
            "classpath*:gov/nih/nci/ctcae/core/testapplicationContext-util.xml"
    };

    @Override
    protected String[] getConfigLocations() {
        return context;
    }



    public void testDetailsQuery() throws Exception {

        RolePrivilegeQuery rolePrivilegeQuery = new RolePrivilegeQuery();
        assertTrue(genericRepository.find(rolePrivilegeQuery).size()>0);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}