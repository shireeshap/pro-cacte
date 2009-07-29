package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesQuery;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.text.ParseException;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomOverTimeReportResultsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String group = request.getParameter("group");
        String filter = request.getParameter("filter");
        if (StringUtils.isBlank(group)) {
            group = "cycle";
        }
        if (!StringUtils.isBlank(filter)) {
            group = filter;
        }
        ModelAndView modelAndView = new ModelAndView("reports/symptomovertimecharts");
        Long totalParticipants = getParticipantCount(request);
        HashSet<String> selectedAttributes = new HashSet<String>();
        HashSet<Integer> selectedArms = getSelectedArms(request);
        String queryString = ControllersUtils.removeParameterFromQueryString(request.getQueryString(), "group");
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

        JFreeChart worstResponseChart = getWorstResponseChart(request, group, totalParticipants.intValue(), selectedAttributes, queryString, selectedArms);
        String worstReponseFileName = ServletUtilities.saveChartAsPNG(worstResponseChart, 700, 400, info, null);
        modelAndView.addObject("worstResponseChartFileName", worstReponseFileName);
        modelAndView.addObject("worstResponseChartImageMap", ChartUtilities.getImageMap(worstReponseFileName, info));
        modelAndView.addObject("worstResponseChart", worstResponseChart);


        for (String attribute : selectedAttributes) {
            JFreeChart stackedBarChart = getStackedBarChart(request, group, queryString, attribute, selectedArms);
            String stackedBarFileName = ServletUtilities.saveChartAsPNG(stackedBarChart, 700, 400, info, null);
            String append = StringUtils.replace(StringUtils.replace(attribute, " ", ""), "/", "");
            modelAndView.addObject(append + "StackedBarChartFileName", stackedBarFileName);
            modelAndView.addObject(append + "StackedBarChartImageMap", ChartUtilities.getImageMap(stackedBarFileName, info));
            modelAndView.addObject(append + "StackedBarChart", stackedBarChart);
        }
        modelAndView.addObject("group", group);
        modelAndView.addObject("selectedAttributes", selectedAttributes);
        modelAndView.addObject("arms", getArms(request));
        modelAndView.addObject("selectedArms", selectedArms);
        addAllAttributesToModelAndView(request, modelAndView);
        return modelAndView;
    }


    private JFreeChart getStackedBarChart(HttpServletRequest request, String group, String queryString, String attribute, HashSet<Integer> selectedArms) throws ParseException {
        if (selectedArms.size() == 0) {
            selectedArms.add(-1);
        }
        TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> results = new TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>();
        for (Integer armid : selectedArms) {
            SymptomOverTimeWorstResponsesQuery query = getQuery(request, group, attribute);
            if (armid != -1) {
                query.filterByArm(armid);
            }
            List worstResponses = genericRepository.find(query);
            TreeMap<String, TreeMap<Integer, Integer>> temp = getChartDataForArm(worstResponses);
            results.put(armid, temp);
        }

        String title = "";
        String rangeAxisLabel = group + " [N]";
        String domainAxisLabel = "%";
        SymptomOverTimeStackedBarChartGenerator chartGenerator = new SymptomOverTimeStackedBarChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString + "&att=" + attribute);
        chartGenerator.setGenericRepository(genericRepository);
        return chartGenerator.getChartA(results, selectedArms);
    }

    private TreeMap<String, TreeMap<Integer, Integer>> getChartDataForArm(List worstResponses) {
        TreeMap<String, TreeMap<Integer, Integer>> results = new TreeMap<String, TreeMap<Integer, Integer>>();
        for (Object obj : worstResponses) {
            Object[] a = (Object[]) obj;
            Integer grade = (Integer) a[0];
            String period = (String) a[3];
            TreeMap<Integer, Integer> map = results.get(period);
            if (map == null) {
                map = new TreeMap<Integer, Integer>();
                for (int i = 0; i < 5; i++) {
                    map.put(i, 0);
                }
                results.put(period, map);
            }
            Integer count = map.get(grade);
            count++;
            map.put(grade, count);
        }
        return results;
    }

    private SymptomOverTimeWorstResponsesQuery getQuery(HttpServletRequest request, String group, String attribute) throws ParseException {
        SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery(group);
        parseRequestParametersAndFormQuery(request, query);
        query.filterBySingleAttribute(ProCtcQuestionType.getByDisplayName(attribute));
        return query;
    }

    private JFreeChart getWorstResponseChart(HttpServletRequest request, String group, int totalParticipants, HashSet<String> selectedAttributes, String queryString, HashSet<Integer> selectedArms) throws ParseException {
        List worstResponses = new ArrayList();
        if (selectedArms.size() > 1) {
            worstResponses = getResponsesForMultipleArms(request, group, selectedAttributes, selectedArms);
        } else {
            SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery(group);
            parseRequestParametersAndFormQuery(request, query);
            worstResponses = genericRepository.find(query);
        }
        TreeMap<String, TreeMap<String, ArrayList<Integer>>> avgCalculationHelper = arrangeDataForAverageCalculation(selectedAttributes, worstResponses);
        TreeMap<String, TreeMap<String, Float>> out = calculateAverages(avgCalculationHelper);

        String title = "";
        String domainAxisLabel = group + "#";
        String rangeAxisLabel = "Average Worst Response (p=" + totalParticipants + ")";
        AbstractChartGenerator chartGenerator;
        if (selectedArms.size() > 1) {
            chartGenerator = new SymptomOverTimeWorstResponsesLineChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString);
        } else {
            chartGenerator = new SymptomOverTimeWorstResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString);
        }
        return chartGenerator.getChart(out);

    }

    private TreeMap<String, TreeMap<String, Float>> calculateAverages(TreeMap<String, TreeMap<String, ArrayList<Integer>>> results) {
        TreeMap<String, TreeMap<String, Float>> out = new TreeMap<String, TreeMap<String, Float>>();
        for (String attribute : results.keySet()) {
            TreeMap<String, ArrayList<Integer>> map = results.get(attribute);
            TreeMap<String, Float> m = new TreeMap<String, Float>();
            for (String i : map.keySet()) {
                ArrayList<Integer> l = map.get(i);
                float sum = 0;
                for (Integer j : l) {
                    sum += j;
                }
                float avg = sum / l.size();
                m.put(i, avg);
            }
            out.put(attribute, m);
        }
        return out;
    }

    private TreeMap<String, TreeMap<String, ArrayList<Integer>>> arrangeDataForAverageCalculation(HashSet<String> selectedAttributes, List worstResponses) {
        TreeMap<String, TreeMap<String, ArrayList<Integer>>> results = new TreeMap<String, TreeMap<String, ArrayList<Integer>>>();
        for (Object obj : worstResponses) {
            Object[] a = (Object[]) obj;
            Integer level = (Integer) a[0];

            String attribute = "";
            if (a[1] instanceof ProCtcQuestionType) {
                attribute = ((ProCtcQuestionType) a[1]).getDisplayName();
                selectedAttributes.add(attribute);
            } else {
                attribute = (String) a[1];
            }
            String period = (String) a[3];
            TreeMap<String, ArrayList<Integer>> map = results.get(attribute);
            if (map == null) {
                map = new TreeMap<String, ArrayList<Integer>>();
                results.put(attribute, map);
            }
            ArrayList avgL = map.get(period);
            if (avgL == null) {
                avgL = new ArrayList();
            }
            avgL.add(level);
            map.put(period, avgL);
        }
        return results;
    }

    private List getResponsesForMultipleArms(HttpServletRequest request, String group, HashSet<String> selectedAttributes, HashSet<Integer> selectedArms) throws ParseException {
        List worstResponses = new ArrayList();
        for (Integer i : selectedArms) {
            SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery(group);
            parseRequestParametersAndFormQuery(request, query);
            query.filterByArm(i);
            Arm arm = genericRepository.findById(Arm.class, i);
            List armWorstResponses = genericRepository.find(query);
            for (Object obj : armWorstResponses) {
                Object[] a = (Object[]) obj;
                String attribute = ((ProCtcQuestionType) a[1]).getDisplayName();
                selectedAttributes.add(attribute);
                a[1] = attribute + "-" + arm.getTitle();
                worstResponses.add(a);
            }
        }
        return worstResponses;
    }


}