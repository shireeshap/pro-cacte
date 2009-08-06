package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomOverTimeReportResultsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/symptomovertimecharts");
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        HashSet<String> selectedAttributes = ReportResultsHelper.getSelectedAttributesNames(request);

        SymptomOverTimeReportHelper helper = new SymptomOverTimeReportHelper();
        helper.setGenericRepository(genericRepository);
        ArrayList<HashMap<String, JFreeChart>> charts = helper.getCharts(request, selectedAttributes, modelAndView);
        ArrayList<Object[]> results = new ArrayList<Object[]>();
        for (HashMap<String, JFreeChart> map : charts) {
            for (String title : map.keySet()) {
                JFreeChart chart = map.get(title);
                String fileName = ServletUtilities.saveChartAsPNG(chart, 700, 400, info, null);
                String imageMap = ChartUtilities.getImageMap(fileName, info);
                Object[] chartInfo = new Object[]{title, fileName, imageMap, chart};
                results.add(chartInfo);
            }
        }

        modelAndView.addObject("selectedAttributes", selectedAttributes);
        modelAndView.addObject("arms", getArms(request));
        modelAndView.addObject("results", results);
        addAllAttributesToModelAndView(request, modelAndView);
        return modelAndView;
    }


//    private List getResponsesForMultipleArms(HttpServletRequest request, String group, HashSet<String> selectedAttributes, HashSet<Integer> selectedArms) throws ParseException {
//        List worstResponses = new ArrayList();
//        for (Integer i : selectedArms) {
//            SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery(group);
//            parseRequestParametersAndFormQuery(request, query);
//            query.filterByArm(i);
//            Arm arm = genericRepository.findById(Arm.class, i);
//            List armWorstResponses = genericRepository.find(query);
//            for (Object obj : armWorstResponses) {
//                Object[] a = (Object[]) obj;
//                String attribute = ((ProCtcQuestionType) a[1]).getDisplayName();
//                selectedAttributes.add(attribute);
//                a[1] = attribute + "-" + arm.getTitle();
//                worstResponses.add(a);
//            }
//        }
//        return worstResponses;
//    }


}