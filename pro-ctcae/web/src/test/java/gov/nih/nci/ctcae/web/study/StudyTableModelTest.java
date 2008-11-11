package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.table.AbstractTableModelTestCase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class StudyTableModelTest extends AbstractTableModelTestCase {

    private StudyTableModel model;
    private Collection studies;
    private Organization nci;
    private StudySite nciStudySite;
    private StudyFundingSponsor studyFundingSponsor;
    private StudyCoordinatingCenter studyCoordinatingCenter;
    private Study studyWithStudyOrganizations;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        model = new StudyTableModel();

        studies = new ArrayList<Study>();

        nci = Fixture.createOrganization("National Cancer Institute", "NCI");


        nciStudySite = new StudySite();
        nciStudySite.setOrganization(nci);

        studyFundingSponsor = new StudyFundingSponsor();
        studyFundingSponsor.setOrganization(nci);

        studyCoordinatingCenter = new StudyCoordinatingCenter();
        studyCoordinatingCenter.setOrganization(nci);

        studyWithStudyOrganizations = Fixture.createStudy("study short title", "study long title", "assigned identifier");
        studyWithStudyOrganizations.setStudyFundingSponsor(studyFundingSponsor);
        studyWithStudyOrganizations.setStudyCoordinatingCenter(studyCoordinatingCenter);
        studyWithStudyOrganizations.addStudySite(nciStudySite);
        studyWithStudyOrganizations.setId(1);

        studies.add(studyWithStudyOrganizations);


    }

    public void testCreateTable() {

        String table = model.buildStudyTable(parameterMap, studies, request);
        validateTable(table);
        assertTrue("table must contains short title", table.contains("shortTitle"));
        assertTrue("table must contains studyFundingSponsor", table.contains("studyFundingSponsor."));
        assertTrue("table must contains studyCoordinatingCenter", table.contains("studyCoordinatingCenter"));
        assertTrue("table must contains assignedIdentifier", table.contains("assignedIdentifier"));

        assertTrue("table must contains correct value of short title", table.contains(studyWithStudyOrganizations.getShortTitle()));
        assertTrue("table must contains correct value of assignedIdentifier", table.contains(studyWithStudyOrganizations.getAssignedIdentifier()));
        assertTrue("table must contains correct value of studyCoordinatingCenter", table.contains(studyWithStudyOrganizations.getStudyCoordinatingCenter().getOrganization().getNciInstituteCode()));
        assertTrue("table must contains correct value of studyFundingSponsor.", table.contains(studyWithStudyOrganizations.getStudyFundingSponsor().getOrganization().getNciInstituteCode()));


    }

    public void testCreateTableForParticipant() {

        String table = model.buildStudyTableForSelection(parameterMap, studies, request);
        validateTable(table);
        assertTrue("table must contains short title", table.contains("shortTitle"));
        assertTrue("table must contains studyFundingSponsor", table.contains("studyFundingSponsor."));
        assertTrue("table must contains studyCoordinatingCenter", table.contains("studyCoordinatingCenter"));
        assertTrue("table must contains assignedIdentifier", table.contains("assignedIdentifier"));
        assertTrue("table must contains assignedIdentifier", table.contains("assignedIdentifier"));
        assertTrue("table must contains participantStudyIdentifier", table.contains("participantStudyIdentifier"));

        assertTrue("table must contains correct value of short title", table.contains(studyWithStudyOrganizations.getShortTitle()));
        assertTrue("table must contains correct value of assignedIdentifier", table.contains(studyWithStudyOrganizations.getAssignedIdentifier()));
        assertTrue("table must contains correct value of studyCoordinatingCenter", table.contains(studyWithStudyOrganizations.getStudyCoordinatingCenter().getOrganization().getNciInstituteCode()));
        assertTrue("table must contains correct value of studyFundingSponsor.", table.contains(studyWithStudyOrganizations.getStudyFundingSponsor().getOrganization().getNciInstituteCode()));


    }

}
