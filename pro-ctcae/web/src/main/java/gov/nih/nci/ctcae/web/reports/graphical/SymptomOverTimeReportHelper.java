package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

public class SymptomOverTimeReportHelper {
    GenericRepository genericRepository;

    public ArrayList<HashMap<String, JFreeChart>> getCharts(HttpServletRequest request, HashSet<String> selectedAttributes, ModelAndView modelAndView) throws ParseException {
        String group = request.getParameter("group");
        String filter = request.getParameter("filter");
        if (!StringUtils.isBlank(filter)) {
            group = filter;
        } else {
            if (StringUtils.isBlank(group)) {
                group = "cycle";
            }
        }
        String queryString = ControllersUtils.removeParameterFromQueryString(request.getQueryString(), "group");
        HashSet<Integer> selectedArms = ReportResultsHelper.getSelectedArms(request);
        HashMap<String, JFreeChart> normalBarCharts = getNormalBarCharts(request, queryString, group, selectedArms);
        HashMap<String, JFreeChart> stackedBarCharts = getStackedBarCharts(request, queryString, group, selectedArms, selectedAttributes);
        ArrayList<HashMap<String, JFreeChart>> charts = new ArrayList<HashMap<String, JFreeChart>>();
        charts.add(normalBarCharts);
        charts.add(stackedBarCharts);

        modelAndView.addObject("group", group);
        modelAndView.addObject("selectedArms", selectedArms);

        return charts;
    }

    private HashMap<String, JFreeChart> getNormalBarCharts(HttpServletRequest request, String queryString, String group, HashSet<Integer> selectedArms) throws ParseException {
        HashMap<String, JFreeChart> normalBarCharts = new HashMap<String, JFreeChart>();
        String title = "Average participant reported Worst Responses vs. Time";
        for (Integer armId : selectedArms) {
            Arm arm = genericRepository.findById(Arm.class, armId);
            queryString = ControllersUtils.removeParameterFromQueryString(queryString, "arms");
            queryString += "&arms=" + armId + ",";
            JFreeChart normalBarChartForArm = getWorstResponseChartForArm(request, queryString, group, arm);
            String armTitle = "";
            if (arm != null) {
                armTitle = " (" + arm.getTitle() + ")";
            }
            normalBarCharts.put(title + armTitle, normalBarChartForArm);
        }
        return normalBarCharts;
    }

    private HashMap<String, JFreeChart> getStackedBarCharts(HttpServletRequest request, String queryString, String group, HashSet<Integer> selectedArms, HashSet<String> selectedAttributes) throws ParseException {
        HashMap<String, JFreeChart> stackedBarCharts = new HashMap<String, JFreeChart>();
        for (String attribute : selectedAttributes) {
            JFreeChart stackedBarChart = getStackedBarChart(request, queryString, group, selectedArms, attribute);
            stackedBarCharts.put(attribute, stackedBarChart);
        }
        return stackedBarCharts;
    }

    private JFreeChart getStackedBarChart(HttpServletRequest request, String queryString, String group, HashSet<Integer> selectedArms, String attribute) throws ParseException {
        //TreeMap structure - Arm id, Period, grade(0-4), count
        //eg. Arm1, Week1, 0, 10
        TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> results = new TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>();
        for (Integer armId : selectedArms) {
            Arm arm = genericRepository.findById(Arm.class, armId);
            List worstResponses = getRawDataForArm(request, group, arm, attribute);
            TreeMap<String, TreeMap<Integer, Integer>> temp = getCountDataForStackedChart(worstResponses);
            results.put(armId, temp);
        }
        String title = "";
        String rangeAxisLabel = group + "#";
        String domainAxisLabel = "%";
        SymptomOverTimeStackedBarChartGenerator chartGenerator = new SymptomOverTimeStackedBarChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString + "&att=" + attribute);
        chartGenerator.setGenericRepository(genericRepository);
        return chartGenerator.getChartA(results, selectedArms);
    }


    private TreeMap<String, TreeMap<Integer, Integer>> getCountDataForStackedChart(List rawDataForArm) {
        //TreeMap structure - period, grade, count
        //eg. Week1, 0, 10
        TreeMap<String, TreeMap<Integer, Integer>> results = new TreeMap<String, TreeMap<Integer, Integer>>();
        for (Object obj : rawDataForArm) {
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

    private JFreeChart getWorstResponseChartForArm(HttpServletRequest request, String queryString, String group, Arm arm) throws ParseException {
        List dataForArm = getRawDataForArm(request, group, arm, null);
        TreeMap<String, TreeMap<String, ArrayList<Integer>>> avgDataForNormalBarChart = getAvgDataForNormalBarChart(dataForArm);
        String title = "";
        String domainAxisLabel = group + "#";
        String rangeAxisLabel = "Average Worst Response";
        SymptomOverTimeWorstResponsesChartGenerator chartGenerator = new SymptomOverTimeWorstResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString, false);
        return chartGenerator.getChart(avgDataForNormalBarChart);

    }

    private List getRawDataForArm(HttpServletRequest request, String group, Arm arm, String attribute) throws ParseException {
        SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery(group);
        ReportResultsHelper.parseRequestParametersAndFormQuery(request, query);
        query.filterByArm(arm);
        query.filterBySingleAttribute(ProCtcQuestionType.getByDisplayName(attribute));
        return genericRepository.find(query);
    }


    private TreeMap<String, TreeMap<String, ArrayList<Integer>>> getAvgDataForNormalBarChart(List dataForArm) {
        //Treemap structure - Attribute, Period, ArrayList<response level(0-4)>
        //eg. Severity, Week 1, <0,4,6,2,6>
        TreeMap<String, TreeMap<String, ArrayList<Integer>>> results = new TreeMap<String, TreeMap<String, ArrayList<Integer>>>();
        for (Object obj : dataForArm) {
            Object[] a = (Object[]) obj;
            Integer level = (Integer) a[0];

            String attribute = "";
            if (a[1] instanceof ProCtcQuestionType) {
                attribute = ((ProCtcQuestionType) a[1]).getDisplayName();
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


    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}




