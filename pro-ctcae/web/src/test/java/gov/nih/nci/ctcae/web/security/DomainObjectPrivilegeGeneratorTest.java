package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.security.DomainObjectPrivilegeGenerator;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 11, 2009
 */
public class DomainObjectPrivilegeGeneratorTest extends AbstractTestCase {

    private Persistable studySite1;
    protected Study study;
    private DomainObjectPrivilegeGenerator privilegeGenerator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        privilegeGenerator = new DomainObjectPrivilegeGenerator();

        study = new Study();
        study.setId(1);
    }

    public void testPrivilegeForStudyOrganizations() {
        studySite1 = new StudySite();
        studySite1.setId(2);
        ((StudySite) studySite1).setStudy(study);
        List<String> strings = privilegeGenerator.generatePrivilege(studySite1);
        assertTrue("gov.nih.nci.ctcae.core.domain.Study.1", strings.contains("gov.nih.nci.ctcae.core.domain.StudySite.2"));
        assertTrue("gov.nih.nci.ctcae.core.domain.Study.1", strings.contains("gov.nih.nci.ctcae.core.domain.Study.1.STUDY_ORGANIZATION_GROUP"));

    }
}
