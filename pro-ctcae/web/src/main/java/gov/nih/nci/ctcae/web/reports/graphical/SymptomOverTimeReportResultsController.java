package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.query.reports.ReportParticipantCountQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesQuery;
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
        if (StringUtils.isBlank(group)) {
            group = "cycle";
        }
        ReportParticipantCountQuery cquery = new ReportParticipantCountQuery();
        parseRequestParametersAndFormQuery(request, cquery);
        List list = genericRepository.find(cquery);
        Long totalParticipants = (Long) list.get(0);
        HashSet<String> selectedAttributes = new HashSet<String>();
        HashSet<String> allAttributes = new HashSet<String>();

        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(request.getParameter("symptom")));
        for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
            allAttributes.add(question.getProCtcQuestionType().getDisplayName());
        }

        SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery(group);
        parseRequestParametersAndFormQuery(request, query);
        List worstResponses = genericRepository.find(query);

        JFreeChart worstResponseChart = getWorstResponseChart(worstResponses, group, totalParticipants.intValue(), selectedAttributes, request.getQueryString());
        JFreeChart stackedBarChart = getStackedBarChart(worstResponses, group, request.getQueryString());

        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String worstReponseFileName = ServletUtilities.saveChartAsPNG(worstResponseChart, 700, 400, info, null);
        String worstReponseImageMap = ChartUtilities.getImageMap(worstReponseFileName, info);
        String stackedBarFileName = ServletUtilities.saveChartAsPNG(stackedBarChart, 700, 400, info, null);
        String stackedBarImageMap = ChartUtilities.getImageMap(stackedBarFileName, info);

        ModelAndView modelAndView = new ModelAndView("reports/symptomovertimecharts");
        modelAndView.addObject("worstResponseChartFileName", worstReponseFileName);
        modelAndView.addObject("worstResponseChartImageMap", worstReponseImageMap);
        modelAndView.addObject("worstResponseChart", worstResponseChart);
        modelAndView.addObject("stackedBarChartFileName", stackedBarFileName);
        modelAndView.addObject("stackedBarChartImageMap", stackedBarImageMap);
        modelAndView.addObject("stackedBarChart", stackedBarChart);
        modelAndView.addObject("group", group);
        modelAndView.addObject("allAttributes", allAttributes);
        modelAndView.addObject("selectedAttributes", selectedAttributes);
        modelAndView.addObject("symptom", proCtcTerm.getTerm());
        return modelAndView;
    }

    private JFreeChart getStackedBarChart(List worstResponses, String group, String queryString) {

        TreeMap<String, TreeMap<Integer, Integer>> results = new TreeMap<String, TreeMap<Integer, Integer>>();
        Integer smallest = getSmallest(worstResponses, 3);
        for (Object obj : worstResponses) {
            Object[] a = (Object[]) obj;
            Integer grade = (Integer) a[0];
            String period = getNewPeriod(a[3], smallest, group);
//            String attribute = ((ProCtcQuestionType) a[1]).getDisplayName();

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
        String title = "";
        String rangeAxisLabel = group + " [N]";
        String domainAxisLabel = "%";
        SymptomOverTimeStackedBarChartGenerator chartGenerator = new SymptomOverTimeStackedBarChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString + "&sum=" + smallest);
        return chartGenerator.getChart(results);
    }

    private JFreeChart getWorstResponseChart(List worstResponses, String group, int totalParticipants, HashSet<String> selectedAttributes, String queryString) throws ParseException {
//        String title = "Average Participant Reported Responses vs. Time for " + symptom + " symptom ( Worst responses)";
        String title = "";
        String domainAxisLabel = group + "#";

        TreeMap<String, TreeMap<String, ArrayList<Integer>>> results = new TreeMap<String, TreeMap<String, ArrayList<Integer>>>();
        Integer smallest = getSmallest(worstResponses, 3);

        for (Object obj : worstResponses) {
            Object[] a = (Object[]) obj;
            Integer level = (Integer) a[0];
            String attribute = ((ProCtcQuestionType) a[1]).getDisplayName();
            String period = getNewPeriod(a[3], smallest, group);
            TreeMap<String, ArrayList<Integer>> map = results.get(attribute);
            selectedAttributes.add(attribute);
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


        String rangeAxisLabel = "Average Worst Response (p=" + totalParticipants + ")";
        SymptomOverTimeWorstResponsesChartGenerator chartGenerator = new SymptomOverTimeWorstResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString + "&sum=" + smallest);
        return chartGenerator.getChart(out);

    }


    private Integer getSmallest(List worstResponses, int i) {
        Integer smallest = -1;
        for (Object obj : worstResponses) {
            Object[] a = (Object[]) obj;
            String period = (String) a[i];
            Integer p = Integer.parseInt(period.substring(period.indexOf(' ') + 1));
            if (smallest < 0 || p < smallest) {
                smallest = p;
            }
        }
        return smallest;
    }

    private String getNewPeriod(Object objPeriod, Integer smallest, String group) {
        String period = (String) objPeriod;
        Integer p = Integer.parseInt(period.substring(period.indexOf(' ') + 1));
        period = StringUtils.capitalize(group) + " " + (p - smallest + 1);
        return period;
    }
}