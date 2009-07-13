package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.reports.ReportParticipantCountQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryWorstResponsesQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import org.springframework.web.servlet.ModelAndView;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.text.ParseException;
import java.io.IOException;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomSummaryReportResultsController extends AbstractReportResultsController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ReportParticipantCountQuery participantCount = new ReportParticipantCountQuery();
        parseRequestParametersAndFormQuery(request, participantCount);
        List list = genericRepository.find(participantCount);
        int totalParticipant = ((Long) list.get(0)).intValue();

        SymptomSummaryWorstResponsesQuery query = new SymptomSummaryWorstResponsesQuery();
        parseRequestParametersAndFormQuery(request, query);
        List queryResults = genericRepository.find(query);
        String symptom = request.getParameter("symptom");
        if (StringUtils.isBlank(symptom)) {
            return generateTablularReport(queryResults, totalParticipant);
        } else {
            return generateGraphicalReport(queryResults, symptom, request.getQueryString(), totalParticipant);
        }
    }

    private ModelAndView generateGraphicalReport(List queryResults, String symptom, String queryString, int totalParticipant) throws IOException, ParseException {
        HashSet<String> allAttributes = new HashSet<String>();
        HashSet<String> selectedAttributes = new HashSet<String>();
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(symptom));
        for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
            allAttributes.add(question.getProCtcQuestionType().getDisplayName());
        }

        JFreeChart worstResponseChart = getWorstResponseChart(queryResults, totalParticipant, proCtcTerm, selectedAttributes, queryString);

        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String worstResponseChartFileName = ServletUtilities.saveChartAsPNG(worstResponseChart, 700, 400, info, null);
        String worstResponseChartImageMap = ChartUtilities.getImageMap(worstResponseChartFileName, info);
        ModelAndView modelAndView = new ModelAndView("reports/symptomsummarycharts");
        modelAndView.addObject("worstResponseChartFileName", worstResponseChartFileName);
        modelAndView.addObject("worstResponseChartImageMap", worstResponseChartImageMap);
        modelAndView.addObject("worstResponseChart", worstResponseChart);
        modelAndView.addObject("allAttributes", allAttributes);
        modelAndView.addObject("selectedAttributes", selectedAttributes);
        modelAndView.addObject("symptom", proCtcTerm.getTerm());
        return modelAndView;
    }

    private ModelAndView generateTablularReport(List queryResults, int totalParticipant) {
        HashSet<String> selectedAttributes = new HashSet<String>();
        HashMap<String, ArrayList<Object[]>> symptomMap = new HashMap<String, ArrayList<Object[]>>();
        for (Object obj : queryResults) {
            Object[] o = (Object[]) obj;
            String symptom = (String) o[3];
            ArrayList<Object[]> l = symptomMap.get(symptom);
            if (l == null) {
                l = new ArrayList<Object[]>();
                symptomMap.put(symptom, l);
            }
            l.add(o);
        }

        TreeMap<ProCtcTerm, TreeMap<String, TreeMap<Integer, Integer>>> output = new TreeMap<ProCtcTerm, TreeMap<String, TreeMap<Integer, Integer>>>(new ProCtcTermComparator());
        for (String symptom : symptomMap.keySet()) {
            ProCtcTermQuery query = new ProCtcTermQuery();
            query.filterByTerm(symptom);
            ProCtcTerm proCtcTerm = genericRepository.findSingle(query);
            TreeMap<String, TreeMap<Integer, Integer>> map = doCalculationsForOneSymptom(proCtcTerm, selectedAttributes, symptomMap.get(symptom));
            output.put(proCtcTerm, map);
        }
        ModelAndView modelAndView = new ModelAndView("reports/symptomsummarytable");
        modelAndView.addObject("results", output);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
        modelAndView.addObject("total", totalParticipant);
        return modelAndView;
    }


    private JFreeChart getWorstResponseChart(List queryResults, int totalParticipant, ProCtcTerm proCtcTerm, HashSet<String> selectedAttributes, String queryString) throws ParseException {
//        String title = "Participant reported responses for symptom " + symptom + " (Worst responses)";
        String title = "";
        String domainAxisLabel = "Response Grade";
        String rangeAxisLabel = "Number of participants (" + totalParticipant + ")";
        TreeMap<String, TreeMap<Integer, Integer>> results = doCalculationsForOneSymptom(proCtcTerm, selectedAttributes, queryResults);

        SymptomSummaryWorstResponsesChartGenerator generator = new SymptomSummaryWorstResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, totalParticipant, queryString);
        return generator.getChart(results);
    }

    private TreeMap<String, TreeMap<Integer, Integer>> doCalculationsForOneSymptom(ProCtcTerm proCtcTerm, HashSet<String> selectedAttributes, List worstResponses) {
        TreeMap<String, TreeMap<Integer, Integer>> results = new TreeMap<String, TreeMap<Integer, Integer>>();
        for (Object obj : worstResponses) {
            Object[] a = (Object[]) obj;
            Integer level = (Integer) a[0];
            ProCtcQuestionType qType = (ProCtcQuestionType) a[2];
            String attribute = (qType).getDisplayName();
            TreeMap<Integer, Integer> map = results.get(attribute);
            selectedAttributes.add(attribute);
            if (map == null) {
                map = new TreeMap<Integer, Integer>();
                addEmptyValues(map, proCtcTerm, qType);
                results.put(attribute, map);
            }
            Integer count = map.get(level);
            if (count == null) {
                count = 0;
                map.put(level, count);
            }
            map.put(level, count + 1);
        }
        return results;
    }


    private void addEmptyValues(TreeMap<Integer, Integer> map, ProCtcTerm proCtcTerm, ProCtcQuestionType qType) {
        for (ProCtcQuestion q : proCtcTerm.getProCtcQuestions()) {
            if (q.getProCtcQuestionType().equals(qType)) {
                for (ProCtcValidValue validValue : q.getValidValues()) {
                    map.put(validValue.getDisplayOrder(), 0);
                }
            }
        }
    }

}
