package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryWorstResponsesQuery;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.reports.graphical.SymptomSummaryReportResultsController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Agarwal
 * @since Mar 18, 2009
 */
public class SymptomSummaryReportTest extends AbstractWebTestCase {


    public void testController() throws Exception {
        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
	        SymptomSummaryReportResultsController controller = new SymptomSummaryReportResultsController();
	        controller.setGenericRepository(genericRepository);
	        request.setParameter("crf", crf.getId().toString());
	        request.setParameter("symptom", symptomId.toString());
	        request.setParameter("attributes", "_Severity_Frequency");
	        request.setMethod("GET");
	
	        ModelAndView modelAndView = controller.handleRequest(request, response);
	        Map m = modelAndView.getModel();
	        assertNotNull(m.get("results"));
	        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
	        GraphicalReportTestHelper.showCharts(charts);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testControllerWithSingleArm() throws Exception {
        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
	        SymptomSummaryReportResultsController controller = new SymptomSummaryReportResultsController();
	        controller.setGenericRepository(genericRepository);
	        request.setParameter("crf", crf.getId().toString());
	        request.setParameter("symptom", symptomId.toString());
	        request.setParameter("attributes", "_Severity_Frequency");
	        request.setParameter("arms", study.getArms().get(0).getId() + "_");
	        request.setMethod("GET");
	
	        ModelAndView modelAndView = controller.handleRequest(request, response);
	        Map m = modelAndView.getModel();
	        assertNotNull(m.get("results"));
	        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
	        GraphicalReportTestHelper.showCharts(charts);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testControllerWithMultipleArms() throws Exception {
        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();        String arms = "";
	        for (Arm arm : study.getArms()) {
	            arms += arm.getId() + "_";
	        }
	        SymptomSummaryReportResultsController controller = new SymptomSummaryReportResultsController();
	        controller.setGenericRepository(genericRepository);
	        request.setParameter("crf", crf.getId().toString());
	        request.setParameter("symptom", symptomId.toString());
	        request.setParameter("attributes", "_Severity_Frequency");
	        request.setParameter("arms", arms);
	        request.setMethod("GET");
	
	        ModelAndView modelAndView = controller.handleRequest(request, response);
	        Map m = modelAndView.getModel();
	        assertNotNull(m.get("results"));
	        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
	        GraphicalReportTestHelper.showCharts(charts);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testController_table() throws Exception {
        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);

        SymptomSummaryReportResultsController controller = new SymptomSummaryReportResultsController();
        controller.setGenericRepository(genericRepository);
        request.setParameter("crf", crf.getId().toString());
        request.setMethod("GET");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map m = modelAndView.getModel();
        assertNotNull(m.get("results"));
        assertEquals("reports/symptomsummarytable", modelAndView.getViewName());
    }

    public void testQuery() {
        SymptomSummaryWorstResponsesQuery query = new SymptomSummaryWorstResponsesQuery();
        query.filterByCrf(StudyTestHelper.getDefaultStudy().getCrfs().get(0).getId());
        List<Persistable> l = genericRepository.find(query);
        assertTrue(l.size() > 0);
    }

}
