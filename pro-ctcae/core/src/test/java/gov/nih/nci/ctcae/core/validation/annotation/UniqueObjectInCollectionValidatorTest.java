package gov.nih.nci.ctcae.core.validation.annotation;

import junit.framework.TestCase;
import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.Study;

/**
 * @author Saurabh Agrawal
 * @crated Oct 28, 2008
 */
public class UniqueObjectInCollectionValidatorTest extends AbstractTestCase {

    private UniqueObjectInCollectionValidator validator;

    private StudySite studySite1, studySite2, duplicateStudySite;

    private Study study;

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

    }


    public void testValidationForNoDuplicateStudySite() {
        assertTrue("no duplicate study sites", validator.validate(study.getStudySites()));

    }

    public void testValidationForDuplicateStudySite() {
        study.addStudySite(duplicateStudySite);
        assertFalse("duplicate study sites found", validator.validate(study.getStudySites()));

    }
}
