package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaffRole;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class ClinicalStaffCommandTest extends WebTestCase {
    private ClinicalStaffCommand clinicalStaffCommand;
    private SiteClinicalStaff siteClinicalStaff1;
    private SiteClinicalStaff siteClinicalStaff2;
    private SiteClinicalStaff siteClinicalStaff3;
    private SiteClinicalStaff siteClinicalStaff4;


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        clinicalStaffCommand = new ClinicalStaffCommand();
        siteClinicalStaff1 = new SiteClinicalStaff();
        siteClinicalStaff1.setOrganization(Fixture.createOrganization("org1", "nci1"));

        siteClinicalStaff2 = new SiteClinicalStaff();
        siteClinicalStaff2.setOrganization(Fixture.createOrganization("org2", "nci2"));

        siteClinicalStaff3 = new SiteClinicalStaff();
        siteClinicalStaff3.setOrganization(Fixture.createOrganization("org3", "nci3"));

        siteClinicalStaff4 = new SiteClinicalStaff();
        siteClinicalStaff4.setOrganization(Fixture.createOrganization("org4", "nci4"));

    }

    public void testEmptyConstructor() {
        assertNotNull("must instantiate site", clinicalStaffCommand.getClinicalStaff());
    }

    public void testRemoveSiteClinicalStaffs() {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff1);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff2);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff3);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff4);
        assertEquals("must be 4 clinical staff sites", 4, clinicalStaff.getSiteClinicalStaffs().size());

        clinicalStaffCommand.setSiteClinicalStaffIndexToRemove(String.valueOf(2));

        clinicalStaffCommand.apply();

        assertEquals("must remove 3 clinical staff sites", 3, clinicalStaff.getSiteClinicalStaffs().size());

        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getSiteClinicalStaffs().contains(siteClinicalStaff1));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getSiteClinicalStaffs().contains(siteClinicalStaff2));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getSiteClinicalStaffs().contains(siteClinicalStaff4));


    }

    public void testRemoveSiteClinicalStaffRole() {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        siteClinicalStaff1.addSiteClinicalStaffRole(new SiteClinicalStaffRole());
        siteClinicalStaff1.addSiteClinicalStaffRole(new SiteClinicalStaffRole());
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff1);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff2);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff3);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff4);

        SiteClinicalStaff siteClinicalStaff = clinicalStaff.getSiteClinicalStaffs().get(0);
        assertEquals("must be 4 clinical staff sites", 4, clinicalStaff.getSiteClinicalStaffs().size());
        assertEquals("must have 2 clinical staff site roles", 2, siteClinicalStaff.getSiteClinicalStaffRoles().size());

        // clinicalStaffCommand.setObjectsIdsToRemove("1,3");

        clinicalStaffCommand.setSiteClinicalStaffRoleIndexToRemove("0-1");
        clinicalStaffCommand.apply();

        assertEquals("must not remove any clinical staff sites", 4, clinicalStaff.getSiteClinicalStaffs().size());
        siteClinicalStaff = clinicalStaff.getSiteClinicalStaffs().get(0);

        assertEquals("must remove 1 clinical staff site roles", 1, siteClinicalStaff.getSiteClinicalStaffRoles().size());

        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getSiteClinicalStaffs().contains(siteClinicalStaff1));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getSiteClinicalStaffs().contains(siteClinicalStaff2));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getSiteClinicalStaffs().contains(siteClinicalStaff3));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getSiteClinicalStaffs().contains(siteClinicalStaff4));


    }


}
