package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class ClinicalStaffCommandTest extends WebTestCase {
    private ClinicalStaffCommand clinicalStaffCommand;
    private OrganizationClinicalStaff organizationClinicalStaff1;
    private OrganizationClinicalStaff organizationClinicalStaff2;
    private OrganizationClinicalStaff organizationClinicalStaff3;
    private OrganizationClinicalStaff organizationClinicalStaff4;


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        clinicalStaffCommand = new ClinicalStaffCommand();
        organizationClinicalStaff1 = new OrganizationClinicalStaff();
        organizationClinicalStaff1.setOrganization(Fixture.createOrganization("org1", "nci1"));

        organizationClinicalStaff2 = new OrganizationClinicalStaff();
        organizationClinicalStaff2.setOrganization(Fixture.createOrganization("org2", "nci2"));

        organizationClinicalStaff3 = new OrganizationClinicalStaff();
        organizationClinicalStaff3.setOrganization(Fixture.createOrganization("org3", "nci3"));

        organizationClinicalStaff4 = new OrganizationClinicalStaff();
        organizationClinicalStaff4.setOrganization(Fixture.createOrganization("org4", "nci4"));

    }

    public void testEmptyConstructor() {
        assertNotNull("must instantiate site", clinicalStaffCommand.getClinicalStaff());
    }

    public void testRemoveOrganizationClinicalStaffs() {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff1);
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff2);
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff3);
        assertEquals("must be 4 clinical staff sites", 4, clinicalStaff.getOrganizationClinicalStaffs().size());

        clinicalStaffCommand.setOrganizationClinicalStaffIndexToRemove(String.valueOf(2));

        clinicalStaffCommand.apply();

        assertEquals("must remove 3 clinical staff sites", 3, clinicalStaff.getOrganizationClinicalStaffs().size());

        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getOrganizationClinicalStaffs().contains(organizationClinicalStaff1));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getOrganizationClinicalStaffs().contains(organizationClinicalStaff3));


    }


}
