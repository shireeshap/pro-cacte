package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author Mehul Gulati
 * @since Mar 18, 2009
 */
public class ParticipantCareReportIntegrationTest extends AbstractWebTestCase {

    ParticipantCareResultsController controller;
    List<StudyParticipantCrfSchedule> schedules;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        StudySite studySite = study.getLeadStudySite();
        StudyParticipantCrf studyParticipantCrf = studySite.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
        Participant participant = studyParticipantCrf.getStudyParticipantAssignment().getParticipant();
        controller = new ParticipantCareResultsController();
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
        assertEquals("reports/participantCareResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(schedules.size(), dates.size());
    }
    public void testParticipantCareResultsController_lastFour() throws Exception {
        request.setParameter("visitRange", "lastFour");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantCareResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(4, dates.size());
    }

    public void testParticipantCareResultsController_currentPrev() throws Exception {
        request.setParameter("visitRange", "currentPrev");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantCareResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(2, dates.size());
    }
    public void testParticipantCareResultsController_custom() throws Exception {
        request.setParameter("visitRange", "custom");
        request.setParameter("forVisits", "5");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantCareResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(5, dates.size());
    }
}