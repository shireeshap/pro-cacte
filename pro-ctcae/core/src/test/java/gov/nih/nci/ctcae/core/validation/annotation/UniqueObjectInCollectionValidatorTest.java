package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;

/**
 * @author Vinay Kumar
 * @crated Oct 28, 2008
 */
public class UniqueObjectInCollectionValidatorTest extends AbstractTestCase {

    private UniqueObjectInCollectionValidator validator;

    private StudySite studySite1, studySite2, duplicateStudySite;
    private ClinicalStaffAssignment clinicalStaff1Assignment, clinicalStaff2Assignment, duplicateClinicalStaffAssignment;

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
        clinicalStaff1Assignment = new ClinicalStaffAssignment();
        clinicalStaff1Assignment.setDomainObjectClass(Organization.class.getName());
        duplicateClinicalStaffAssignment = new ClinicalStaffAssignment();
        duplicateClinicalStaffAssignment.setDomainObjectClass(Organization.class.getName());

        clinicalStaff2Assignment = new ClinicalStaffAssignment();
        clinicalStaff2Assignment.setDomainObjectClass(StudySite.class.getName());
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff1Assignment);
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff2Assignment);


    }


    public void testValidationForNoDuplicateStudySite() {
        assertTrue("no duplicate study sites", validator.validate(study.getStudySites()));

    }

    public void testValidationForNoDuplicateSiteClinicalStaff() {
        assertTrue("no duplicate  sites", validator.validate(clinicalStaff.getClinicalStaffAssignments()));

    }

    public void testValidationForDuplicateStudySite() {
        study.addStudySite(duplicateStudySite);
        assertFalse("duplicate study sites found", validator.validate(study.getStudySites()));

    }

    public void testValidationForDuplicateSiteClinicalStaff() {
        clinicalStaff.addClinicalStaffAssignment(duplicateClinicalStaffAssignment);
        assertFalse("duplicate  sites found", validator.validate(clinicalStaff.getClinicalStaffAssignments()));

    }
}
