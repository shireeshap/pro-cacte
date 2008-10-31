package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.Investigator;
import gov.nih.nci.ctcae.core.domain.SiteInvestigator;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;

/**
 * @author Saurabh Agrawal
 * @crated Oct 28, 2008
 */
public class UniqueObjectInCollectionValidatorTest extends AbstractTestCase {

    private UniqueObjectInCollectionValidator validator;

    private StudySite studySite1, studySite2, duplicateStudySite;
    private SiteInvestigator siteInvestigator1, siteInvestigator2, duplicateSiteInvestigator;

    private Study study;

    private Investigator investigator;


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

        investigator = new Investigator();
        siteInvestigator1 = new SiteInvestigator();
        siteInvestigator1.setOrganization(nci);
        duplicateSiteInvestigator = new SiteInvestigator();
        duplicateSiteInvestigator.setOrganization(nci);

        siteInvestigator2 = new SiteInvestigator();
        siteInvestigator2.setOrganization(duke);
        investigator.addSiteInvestigator(siteInvestigator1);
        investigator.addSiteInvestigator(siteInvestigator2);


    }


    public void testValidationForNoDuplicateStudySite() {
        assertTrue("no duplicate study sites", validator.validate(study.getStudySites()));

    }

    public void testValidationForNoDuplicateSiteInvestigator() {
        assertTrue("no duplicate  sites", validator.validate(investigator.getSiteInvestigators()));

    }

    public void testValidationForDuplicateStudySite() {
        study.addStudySite(duplicateStudySite);
        assertFalse("duplicate study sites found", validator.validate(study.getStudySites()));

    }

    public void testValidationForDuplicateSiteInvestigator() {
        investigator.addSiteInvestigator(duplicateSiteInvestigator);
        assertFalse("duplicate  sites found", validator.validate(investigator.getSiteInvestigators()));

    }
}
