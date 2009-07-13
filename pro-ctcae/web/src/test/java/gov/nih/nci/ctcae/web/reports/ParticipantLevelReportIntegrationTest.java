package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author Mehul Gulati
 * @since Mar 18, 2009
 */
public class ParticipantLevelReportIntegrationTest extends AbstractWebTestCase {

    ParticipantLevelReportResultsController controller;
    List<StudyParticipantCrfSchedule> schedules;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        StudySite studySite = study.getLeadStudySite();
        StudyParticipantCrf studyParticipantCrf = studySite.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        Participant participant = studyParticipantCrf.getStudyParticipantAssignment().getParticipant();
        controller = new ParticipantLevelReportResultsController();
        schedules = studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED);

        request.setMethod("GET");
        request.setParameter("studyId", study.getId().toString());
        request.setParameter("studySiteId", studySite.getId().toString());
        request.setParameter("crfId", crf.getId().toString());
        request.setParameter("participantId", participant.getId().toString());

        controller.setGenericRepository(genericRepository);

    }

    public void testParticipantCareResultsController_all() throws Exception {
        request.setParameter("visitRange", "all");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(schedules.size(), dates.size());
    }

    public void testParticipantCareResultsController_lastFour() throws Exception {
        request.setParameter("visitRange", "lastFour");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(4, dates.size());
    }

    public void testParticipantCareResultsController_currentPrev() throws Exception {
        request.setParameter("visitRange", "currentPrev");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(2, dates.size());
    }

    public void testParticipantCareResultsController_custom() throws Exception {
        request.setParameter("visitRange", "custom");
        request.setParameter("forVisits", "5");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(5, dates.size());
    }

    public void testController_WithId() throws Exception {
        login(StudyTestHelper.getNonLeadSiteStaffByRole(Role.SITE_CRA).getUser().getUsername());
        request.setMethod("GET");

        Integer id = ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0).getId();
        ParticipantLevelReportController controller = new ParticipantLevelReportController();
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        request.setParameter("sid", id.toString());
        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantLevelReport", modelAndView.getViewName());
        assertEquals("ALL", modelAndView.getModel().get("visits"));

        Study study = StudyTestHelper.getDefaultStudy();
        Participant participant = ParticipantTestHelper.getDefaultParticipant();
        assertEquals(study, modelAndView.getModel().get("study"));
        assertEquals(participant, modelAndView.getModel().get("participant"));
        assertEquals(study.getCrfs().get(0), modelAndView.getModel().get("crf"));
        assertEquals(participant.getStudyParticipantAssignments().get(0).getStudySite(), modelAndView.getModel().get("studySite"));
    }

    public void testController_WithoutId() throws Exception {
        request.setMethod("GET");
        ParticipantLevelReportController controller = new ParticipantLevelReportController();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantLevelReport", modelAndView.getViewName());
        assertEquals(0, modelAndView.getModel().get("visits"));
        assertNull(modelAndView.getModel().get("study"));
    }

}