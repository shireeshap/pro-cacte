package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.reports.graphical.SymptomOverTimeReportResultsController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Harsh Agarwal
 * @since Mar 18, 2009
 */
public class SymptomOverTimeReportTest extends AbstractWebTestCase {


    public void testReportController() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
            SymptomOverTimeReportResultsController controller = new SymptomOverTimeReportResultsController();
            controller.setGenericRepository(genericRepository);
            request.setParameter("crf", crf.getId().toString());
            request.setParameter("symptom", symptomId.toString());
            request.setParameter("attributes", "_Severity_Frequency");
            request.setParameter("group", "cycle");
            request.setParameter("study", study.getId().toString());
            request.setMethod("GET");

            ModelAndView modelAndView = controller.handleRequest(request, response);
            Map m = modelAndView.getModel();
            ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
            GraphicalReportTestHelper.showCharts(charts);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testReportControllerMultipleArms() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        String arms = "";
        for (Arm arm : study.getArms()) {
            arms += arm.getId() + "_";
        }
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
	
	        SymptomOverTimeReportResultsController controller = new SymptomOverTimeReportResultsController();
	        controller.setGenericRepository(genericRepository);
	        request.setParameter("crf", crf.getId().toString());
	        request.setParameter("symptom", symptomId.toString());
	        request.setParameter("attributes", "_Severity_Frequency");
	        request.setParameter("group", "cycle");
	        request.setParameter("arms", arms);
	        request.setParameter("study", study.getId().toString());
	        request.setMethod("GET");
	
	        ModelAndView modelAndView = controller.handleRequest(request, response);
	        Map m = modelAndView.getModel();
	        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
	        GraphicalReportTestHelper.showCharts(charts);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testReportControllerMultipleArmsLineChart() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        String arms = "";
        for (Arm arm : study.getArms()) {
            arms += arm.getId() + "_";
        }
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
	        SymptomOverTimeReportResultsController controller = new SymptomOverTimeReportResultsController();
	        controller.setGenericRepository(genericRepository);
	        request.setParameter("crf", crf.getId().toString());
	        request.setParameter("symptom", symptomId.toString());
	        request.setParameter("attributes", "_Severity_Frequency");
	        request.setParameter("group", "cycle");
	        request.setParameter("arms", arms);
	        request.setParameter("chartType", "line");
	        request.setParameter("study", study.getId().toString());
	        request.setMethod("GET");
	
	        ModelAndView modelAndView = controller.handleRequest(request, response);
	        Map m = modelAndView.getModel();
	        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
	        GraphicalReportTestHelper.showCharts(charts);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testReportControllerSingleArm() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        String arms = study.getArms().get(0).getId() + "_";
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
	        SymptomOverTimeReportResultsController controller = new SymptomOverTimeReportResultsController();
	        controller.setGenericRepository(genericRepository);
	        request.setParameter("crf", crf.getId().toString());
	        request.setParameter("symptom", symptomId.toString());
	        request.setParameter("attributes", "_Severity_Frequency");
	        request.setParameter("group", "cycle");
	        request.setParameter("arms", arms);
	        request.setParameter("study", study.getId().toString());
	        request.setMethod("GET");
	
	        ModelAndView modelAndView = controller.handleRequest(request, response);
	        Map m = modelAndView.getModel();
	        ArrayList<Object[]> charts = (ArrayList<Object[]>) m.get("results");
	        GraphicalReportTestHelper.showCharts(charts);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testReportDetailsController() throws Exception {

        Study study = StudyTestHelper.getDefaultStudy();
        CRF crf = study.getCrfs().get(0);
        Integer symptomId;
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
	        ReportDetailsController controller = new ReportDetailsController();
	        controller.setGenericRepository(genericRepository);
	        request.setParameter("crf", crf.getId().toString());
	        request.setParameter("symptom", symptomId.toString());
	        request.setParameter("attributes", "_Severity_Frequency");
	        request.setParameter("group", "week");
	        request.setParameter("col", "Week 26");
	        request.setParameter("ser", "Frequency");
	        request.setParameter("type", "WOR");
	        request.setParameter("sum", "2");
	        request.setMethod("GET");
	
	//        ModelAndView modelAndView = controller.handleRequest(request, response);
	        controller.handleRequest(request, response);
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }


}