package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryWorstResponsesQuery;

import java.util.*;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Harsh Agarwal
 * @since Mar 18, 2009
 */
public class ParticipantLevelGraphicalReportTest extends ParticipantLevelReportIntegrationTest {


    public void testController() throws Exception {
        request.setParameter("visitRange", "all");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantLevelReportResults", modelAndView.getViewName());
        Map map = modelAndView.getModel();
        List<String> dates = (List<String>) map.get("dates");
        assertEquals(schedules.size(), dates.size());

        Integer symptomId = crf.getAllCrfPageItems().get(13).getProCtcQuestion().getProCtcTerm().getId();
        request.setParameter("symptomId", symptomId.toString());

        ParticipantLevelGraphicalReportController controller1 = new ParticipantLevelGraphicalReportController();
        controller1.setGenericRepository(genericRepository);
        modelAndView = controller1.handleRequest(request, response);
        Map m = modelAndView.getModel();
        assertNotNull(m.get("participantReportChartFileName"));
        assertNotNull(m.get("participantReportChart"));
        JFreeChart participantReportChart = (JFreeChart) m.get("participantReportChart");
        GraphicalReportTestHelper.showCharts(participantReportChart);


    }

    public void testQuery() {
        SymptomSummaryWorstResponsesQuery query = new SymptomSummaryWorstResponsesQuery();
        query.filterByCrf(StudyTestHelper.getDefaultStudy().getCrfs().get(0).getId());
        List<Persistable> l = genericRepository.find(query);
        assertTrue(l.size() > 0);
    }

}