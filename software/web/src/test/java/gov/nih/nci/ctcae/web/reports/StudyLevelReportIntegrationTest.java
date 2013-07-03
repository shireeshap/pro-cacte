package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Mehul Gulati
 * @since Mar 18, 2009
 */
public class StudyLevelReportIntegrationTest extends AbstractWebTestCase {

    StudyLevelReportResultsController controller;
    List<StudyParticipantCrfSchedule> schedules;
    StudySite studySite;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        login(StudyTestHelper.getDefaultStudy().getLeadCRAs().get(0).getOrganizationClinicalStaff().getClinicalStaff().getUser().getUsername());
        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        studySite = study.getLeadStudySite();
        StudyParticipantCrf studyParticipantCrf = studySite.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        controller = new StudyLevelReportResultsController();
        schedules = studyParticipantCrf.getStudyParticipantCrfSchedulesByStatus(CrfStatus.COMPLETED);

        request.setMethod("GET");
        request.setParameter("study", study.getId().toString());
        request.setParameter("crf", crf.getId().toString());

        controller.setGenericRepository(genericRepository);

    }

    public void testParticipantCareResultsController_all() throws Exception {

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/studyLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        TreeMap<Organization, TreeMap<Participant, String>> results = (TreeMap<Organization, TreeMap<Participant, String>>) map.get("table");
        assertEquals(2, results.size());
    }

    public void testParticipantCareResultsController_studySite() throws Exception {

        request.setParameter("studySite", studySite.getId().toString());
        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/studyLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        TreeMap<Organization, TreeMap<Participant, String>> results = (TreeMap<Organization, TreeMap<Participant, String>>) map.get("table");
        assertEquals(1, results.size());
    }

}