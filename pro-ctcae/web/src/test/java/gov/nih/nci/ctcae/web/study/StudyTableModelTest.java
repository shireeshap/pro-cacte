package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.helper.Fixture;
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
    private StudySponsor studySponsor;
    private DataCoordinatingCenter dataCoordinatingCenter;
    private Study studyWithStudyOrganizations;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        model = new StudyTableModel();

        studies = new ArrayList<Study>();

        nci = Fixture.createOrganization("National Cancer Institute", "NCI");


        nciStudySite = new StudySite();
        nciStudySite.setOrganization(nci);

        studySponsor = new StudySponsor();
        studySponsor.setOrganization(nci);

        dataCoordinatingCenter = new DataCoordinatingCenter();
        dataCoordinatingCenter.setOrganization(nci);

        studyWithStudyOrganizations = Fixture.createStudy("study short title", "study long title", "assigned identifier");
        studyWithStudyOrganizations.setStudySponsor(studySponsor);
        studyWithStudyOrganizations.setDataCoordinatingCenter(dataCoordinatingCenter);
        studyWithStudyOrganizations.addStudySite(nciStudySite);
        studyWithStudyOrganizations.setId(1);

        studies.add(studyWithStudyOrganizations);


    }

    public void testCreateTable() {

        String table = model.buildStudyTable(parameterMap, studies, request);
        validateTable(table);
        assertTrue("table must contains short title", table.contains("shortTitle"));
        assertTrue("table must contains studySponsor", table.contains("studySponsor."));
        assertTrue("table must contains dataCoordinatingCenter", table.contains("dataCoordinatingCenter"));
        assertTrue("table must contains assignedIdentifier", table.contains("assignedIdentifier"));

        assertTrue("table must contains correct value of short title", table.contains(studyWithStudyOrganizations.getShortTitle()));
        assertTrue("table must contains correct value of assignedIdentifier", table.contains(studyWithStudyOrganizations.getAssignedIdentifier()));
        assertTrue("table must contains correct value of dataCoordinatingCenter", table.contains(studyWithStudyOrganizations.getDataCoordinatingCenter().getOrganization().getNciInstituteCode()));
        assertTrue("table must contains correct value of studySponsor.", table.contains(studyWithStudyOrganizations.getStudySponsor().getOrganization().getNciInstituteCode()));


    }


}
