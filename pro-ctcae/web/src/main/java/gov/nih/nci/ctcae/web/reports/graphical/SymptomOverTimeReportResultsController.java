package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeAllResponsesQuery;
import gov.nih.nci.ctcae.core.query.reports.ReportParticipantCountQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomOverTimeWorstResponsesQuery;
import gov.nih.nci.ctcae.web.reports.graphical.AbstractReportResultsController;
import gov.nih.nci.ctcae.web.reports.graphical.SymptomOverTimeAllResponsesChartGenerator;
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
            group = "week";
        }
        ReportParticipantCountQuery query = new ReportParticipantCountQuery();
        parseRequestParametersAndFormQuery(request, query);
        List list = genericRepository.find(query);
        Long totalParticipants = (Long) list.get(0);
        HashSet<String> selectedAttributes = new HashSet<String>();
        HashSet<String> allAttributes = new HashSet<String>();

        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(request.getParameter("symptom")));
        for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
            allAttributes.add(question.getProCtcQuestionType().getDisplayName());
        }

        JFreeChart allResponseChart = getAllResponseChart(request, group, proCtcTerm.getTerm(), totalParticipants.intValue());
        JFreeChart worstResponseChart = getWorstResponseChart(request, group, proCtcTerm.getTerm(), totalParticipants.intValue(), selectedAttributes);

        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String allReponseFileName = ServletUtilities.saveChartAsPNG(allResponseChart, 700, 400, info, null);
        String allReponseImageMap = ChartUtilities.getImageMap(allReponseFileName, info);
        String worstReponseFileName = ServletUtilities.saveChartAsPNG(worstResponseChart, 700, 400, info, null);
        String worstReponseImageMap = ChartUtilities.getImageMap(worstReponseFileName, info);

        ModelAndView modelAndView = new ModelAndView("reports/symptomovertimecharts");
        modelAndView.addObject("allResponseChartFileName", allReponseFileName);
        modelAndView.addObject("allResponseChartImageMap", allReponseImageMap);
        modelAndView.addObject("worstResponseChartFileName", worstReponseFileName);
        modelAndView.addObject("worstResponseChartImageMap", worstReponseImageMap);
        modelAndView.addObject("group", group);
        modelAndView.addObject("allResponseChart", allResponseChart);
        modelAndView.addObject("worstResponseChart", worstResponseChart);
        modelAndView.addObject("allAttributes", allAttributes);
        modelAndView.addObject("selectedAttributes", selectedAttributes);
        modelAndView.addObject("symptom", proCtcTerm.getTerm());
        return modelAndView;
    }

    private JFreeChart getWorstResponseChart(HttpServletRequest request, String group, String symptom, int totalParticipants, HashSet<String> selectedAttributes) throws ParseException {
//        String title = "Average Participant Reported Responses vs. Time for " + symptom + " symptom ( Worst responses)";
        String title = "";
        String domainAxisLabel = group + "#";

        SymptomOverTimeWorstResponsesQuery query = new SymptomOverTimeWorstResponsesQuery(group);
        parseRequestParametersAndFormQuery(request, query);
        List worstResponses = genericRepository.find(query);
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
        SymptomOverTimeWorstResponsesChartGenerator chartGenerator = new SymptomOverTimeWorstResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, request.getQueryString() + "&sum=" + smallest);
        return chartGenerator.getChart(out);

    }

    private JFreeChart getAllResponseChart(HttpServletRequest request, String group, String symptom, int totalParticipants) throws ParseException {
//        String title = "Average Participant Reported Responses vs. Time for " + symptom + " symptom ( All responses)";
        String title = "";
        String domainAxisLabel = group + "#";

        SymptomOverTimeAllResponsesQuery query = new SymptomOverTimeAllResponsesQuery(group);
        parseRequestParametersAndFormQuery(request, query);
        List results = genericRepository.find(query);
        String rangeAxisLabel = "Average Overall Response (p=" + totalParticipants + ")";
        Integer smallest = getSmallest(results, 2);
        for (Object obj : results) {
            Object[] a = (Object[]) obj;
            String period = getNewPeriod(a[2], smallest, group);
            a[2] = period;
        }
        SymptomOverTimeAllResponsesChartGenerator chartGenerator = new SymptomOverTimeAllResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, request.getQueryString() + "&sum=" + smallest);
        return chartGenerator.getChart(results);
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