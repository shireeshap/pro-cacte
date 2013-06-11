package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.reports.StudyLevelReportExcelView;
import gov.nih.nci.ctcae.web.reports.StudyLevelReportResultsController;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author mehul gulati
 *         Date: Oct 19, 2010
 */
public class StudyLevelReportExcelViewTest extends AbstractWebTestCase {

    StudyLevelReportExcelView excelView;

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

    public void testBuildExcelDocument() throws Exception {

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/studyLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        TreeMap<Organization, TreeMap<Participant, String>> results = (TreeMap<Organization, TreeMap<Participant, String>>) map.get("table");
        assertEquals(2, results.size());

    }



}
