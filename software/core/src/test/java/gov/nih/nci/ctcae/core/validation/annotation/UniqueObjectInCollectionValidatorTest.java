package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.Fixture;

/**
 * @author Vinay Kumar
 * @since Oct 28, 2008
 */
public class UniqueObjectInCollectionValidatorTest extends AbstractTestCase {

    private UniqueObjectInCollectionValidator validator;

    private StudySite studySite1, studySite2, duplicateStudySite;
    private OrganizationClinicalStaff organizationClinicalStaff1, organizationClinicalStaff2, duplicateOrganizationClinicalStaff;

    private Study study;

    private ClinicalStaff clinicalStaff;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        validator = new UniqueObjectInCollectionValidator();

        study = new Study();
        studySite1 = new StudySite();
        studySite1.setOrganization(Fixture.NCI);
        duplicateStudySite = new StudySite();
        duplicateStudySite.setOrganization(Fixture.NCI);

        studySite2 = new StudySite();
        studySite2.setOrganization(Fixture.DUKE);
        study.addStudySite(studySite1);
        study.addStudySite(studySite2);

        clinicalStaff = new ClinicalStaff();
        organizationClinicalStaff1 = new OrganizationClinicalStaff();
        organizationClinicalStaff1.setOrganization(Fixture.NCI);
        duplicateOrganizationClinicalStaff = new OrganizationClinicalStaff();
        duplicateOrganizationClinicalStaff.setOrganization(Fixture.NCI);

        organizationClinicalStaff2 = new OrganizationClinicalStaff();
        organizationClinicalStaff2.setOrganization(Fixture.DUKE);
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff1);
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff2);


    }


    public void testValidationForNoDuplicateStudySite() {
        assertTrue("no duplicate study sites", validator.validate(study.getStudySites()));

    }

    public void testValidationForNoDuplicateOrganizationClinicalStaff() {
        assertTrue("no duplicate  sites", validator.validate(clinicalStaff.getOrganizationClinicalStaffs()));

    }

    public void testValidationForDuplicateStudySite() {
        study.addStudySite(duplicateStudySite);
        assertFalse("duplicate study sites found", validator.validate(study.getStudySites()));

    }

    public void testValidationForDuplicateOrganizationClinicalStaff() {
        clinicalStaff.addOrganizationClinicalStaff(duplicateOrganizationClinicalStaff);
        assertFalse("duplicate  sites found", validator.validate(clinicalStaff.getOrganizationClinicalStaffs()));

    }
}
