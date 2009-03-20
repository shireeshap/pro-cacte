package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

import java.text.ParseException;
import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Mar 13, 2009
 */
public class SecurityTestDataIntegrationTest extends AbstractHibernateIntegrationTestCase {

    protected Study study2, study3, study5, study6;

    protected User leadCRA1, leadCRA2, siteCRA1, siteCRA2;

    protected CRF defaultCRF, crf1, crf2;

    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        study5 = new Study();
        study5.setShortTitle("Study 5");
        study5.setLongTitle("study 5");
        study5.setAssignedIdentifier(-1001+"");

        dataCoordinatingCenter1 = new DataCoordinatingCenter();
        dataCoordinatingCenter1.setOrganization(defaultOrganization);
        studySponsor1 = new StudySponsor();
        studySponsor1.setOrganization(sydney);
        studySite2 = new StudySite();
        studySite2.setOrganization(duke);
        studySite3 = new StudySite();
        studySite3.setOrganization(nci);
        fundingSponsor1 = new FundingSponsor();
        fundingSponsor1.setOrganization(rawson);
        leadStudySite1 = new LeadStudySite();
        leadStudySite1.setOrganization(wake);

        study5.setDataCoordinatingCenter(dataCoordinatingCenter1);
        study5.setStudySponsor(studySponsor1);
        study5.addStudySite(studySite2);
        study5.addStudySite(studySite3);
        study5.setFundingSponsor(fundingSponsor1);
        study5.setLeadStudySite(leadStudySite1);
        study5 = studyRepository.save(study5);


        study6 = new Study();
        study6.setShortTitle("Study 6");
        study6.setLongTitle("study 6");
        study6.setAssignedIdentifier(-1002+"");

        dataCoordinatingCenter2 = new DataCoordinatingCenter();
        dataCoordinatingCenter2.setOrganization(queens);
        studySponsor2 = new StudySponsor();
        studySponsor2.setOrganization(wake);
        studySite4 = new StudySite();
        studySite4.setOrganization(nci);
        studySite5 = new StudySite();
        studySite5.setOrganization(cerim);
        fundingSponsor2 = new FundingSponsor();
        fundingSponsor2.setOrganization(barkers);
        leadStudySite2 = new LeadStudySite();
        leadStudySite2.setOrganization(orange);

        study6.setDataCoordinatingCenter(dataCoordinatingCenter2);
        study6.setStudySponsor(studySponsor2);
        study6.addStudySite(studySite4);
        study6.addStudySite(studySite5);
        study6.setFundingSponsor(fundingSponsor2);
        study6.setLeadStudySite(leadStudySite2);
        study6 = studyRepository.save(study6);


        commitAndStartNewTransaction();



        ClinicalStaff clinicalStaff1 = Fixture.createClinicalStaffWithOrganization("cs1mskcc", "cs1mskcc", "-12345", defaultOrganization);
        clinicalStaff1 = clinicalStaffRepository.save(clinicalStaff1);
        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("cs1wake", "cs1wake", "-12345", wake);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);
        ClinicalStaff clinicalStaff3 = Fixture.createClinicalStaffWithOrganization("cs2wake", "cs2wake", "-12345", wake);
        clinicalStaff3 = clinicalStaffRepository.save(clinicalStaff3);
        ClinicalStaff clinicalStaff4 = Fixture.createClinicalStaffWithOrganization("cs1duke", "cs1duke", "-12345", duke);
        clinicalStaff4 = clinicalStaffRepository.save(clinicalStaff4);
        ClinicalStaff clinicalStaff5 = Fixture.createClinicalStaffWithOrganization("cs2duke", "cs2duke", "-12345", duke);
        clinicalStaff5 = clinicalStaffRepository.save(clinicalStaff5);
        ClinicalStaff clinicalStaff6 = Fixture.createClinicalStaffWithOrganization("cs3duke", "cs3duke", "-12345", duke);
        clinicalStaff6 = clinicalStaffRepository.save(clinicalStaff6);
        ClinicalStaff clinicalStaff7 = Fixture.createClinicalStaffWithOrganization("cs4duke", "cs4duke", "-12345", duke);
        clinicalStaff7 = clinicalStaffRepository.save(clinicalStaff7);
        ClinicalStaff clinicalStaff8 = Fixture.createClinicalStaffWithOrganization("cs1nci", "cs1nci", "-12345", nci);
        clinicalStaff8 = clinicalStaffRepository.save(clinicalStaff8);
        ClinicalStaff clinicalStaff9 = Fixture.createClinicalStaffWithOrganization("cs2nci", "cs2nci", "-12345", nci);
        clinicalStaff9 = clinicalStaffRepository.save(clinicalStaff9);
        ClinicalStaff clinicalStaff10 = Fixture.createClinicalStaffWithOrganization("cs3nci", "cs3nci", "-12345", nci);
        clinicalStaff10 = clinicalStaffRepository.save(clinicalStaff10);
        ClinicalStaff clinicalStaff11 = Fixture.createClinicalStaffWithOrganization("cs4nci", "cs4nci", "-12345", nci);
        clinicalStaff11 = clinicalStaffRepository.save(clinicalStaff11);
        ClinicalStaff clinicalStaff12 = Fixture.createClinicalStaffWithOrganization("cs1queens", "cs1queens", "-12345", queens);
        clinicalStaff12 = clinicalStaffRepository.save(clinicalStaff12);
        ClinicalStaff clinicalStaff13 = Fixture.createClinicalStaffWithOrganization("cs1orange", "cs1orange", "-12345", orange);
        clinicalStaff13 = clinicalStaffRepository.save(clinicalStaff13);
        ClinicalStaff clinicalStaff14 = Fixture.createClinicalStaffWithOrganization("cs2orange", "cs2orange", "-12345", orange);
        clinicalStaff14 = clinicalStaffRepository.save(clinicalStaff14);
        ClinicalStaff clinicalStaff15 = Fixture.createClinicalStaffWithOrganization("cs1cerim", "cs1cerim", "-12345", cerim);
        clinicalStaff15 = clinicalStaffRepository.save(clinicalStaff15);
        ClinicalStaff clinicalStaff16 = Fixture.createClinicalStaffWithOrganization("cs2cerim", "cs2cerim", "-12345", cerim);
        clinicalStaff16 = clinicalStaffRepository.save(clinicalStaff16);
        ClinicalStaff clinicalStaff17 = Fixture.createClinicalStaffWithOrganization("cs3cerim", "cs3cerim", "-12345", cerim);
        clinicalStaff17 = clinicalStaffRepository.save(clinicalStaff17);
        ClinicalStaff clinicalStaff18 = Fixture.createClinicalStaffWithOrganization("cs4cerim", "cs4cerim", "-12345", cerim);
        clinicalStaff18 = clinicalStaffRepository.save(clinicalStaff18);

        ClinicalStaff clinicalStaff19 = Fixture.createClinicalStaffWithOrganization("cca", "cca", "-12345", defaultOrganization);
        UserRole userRole = new UserRole();
        userRole.setRole(Role.CCA);
        clinicalStaff19.getUser().addUserRole(userRole);
        clinicalStaff19 = clinicalStaffRepository.save(clinicalStaff19);

        commitAndStartNewTransaction();


    }

    public void testInsertData() throws ParseException {


    }


}
