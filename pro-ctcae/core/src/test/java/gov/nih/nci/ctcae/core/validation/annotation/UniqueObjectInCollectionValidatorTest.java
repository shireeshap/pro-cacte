package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.SiteClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;

/**
 * @author Saurabh Agrawal
 * @crated Oct 28, 2008
 */
public class UniqueObjectInCollectionValidatorTest extends AbstractTestCase {

    private UniqueObjectInCollectionValidator validator;

    private StudySite studySite1, studySite2, duplicateStudySite;
    private SiteClinicalStaff siteClinicalStaff1, siteClinicalStaff2, duplicateSiteClinicalStaff;

    private Study study;

    private ClinicalStaff clinicalStaff;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        validator = new UniqueObjectInCollectionValidator();

        study = new Study();
        studySite1 = new StudySite();
        studySite1.setOrganization(nci);
        duplicateStudySite = new StudySite();
        duplicateStudySite.setOrganization(nci);

        studySite2 = new StudySite();
        studySite2.setOrganization(duke);
        study.addStudySite(studySite1);
        study.addStudySite(studySite2);

        clinicalStaff = new ClinicalStaff();
        siteClinicalStaff1 = new SiteClinicalStaff();
        siteClinicalStaff1.setOrganization(nci);
        duplicateSiteClinicalStaff = new SiteClinicalStaff();
        duplicateSiteClinicalStaff.setOrganization(nci);

        siteClinicalStaff2 = new SiteClinicalStaff();
        siteClinicalStaff2.setOrganization(duke);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff1);
        clinicalStaff.addSiteClinicalStaff(siteClinicalStaff2);


    }


    public void testValidationForNoDuplicateStudySite() {
        assertTrue("no duplicate study sites", validator.validate(study.getStudySites()));

    }

    public void testValidationForNoDuplicateSiteClinicalStaff() {
        assertTrue("no duplicate  sites", validator.validate(clinicalStaff.getSiteClinicalStaffs()));

    }

    public void testValidationForDuplicateStudySite() {
        study.addStudySite(duplicateStudySite);
        assertFalse("duplicate study sites found", validator.validate(study.getStudySites()));

    }

    public void testValidationForDuplicateSiteClinicalStaff() {
        clinicalStaff.addSiteClinicalStaff(duplicateSiteClinicalStaff);
        assertFalse("duplicate  sites found", validator.validate(clinicalStaff.getSiteClinicalStaffs()));

    }
}
