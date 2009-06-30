package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.reports.ReportParticipantCountQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryWorstResponsesQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryAllResponsesQuery;
import org.springframework.web.servlet.ModelAndView;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.entity.StandardEntityCollection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.text.ParseException;


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
        HashSet<String> allAttributes = new HashSet<String>();
        HashSet<String> selectedAttributes = new HashSet<String>();

        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(request.getParameter("symptom")));
        for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
            allAttributes.add(question.getProCtcQuestionType().getDisplayName());
        }
        JFreeChart worstResponseChart = getWorstResponseChart(request, totalParticipant, proCtcTerm, selectedAttributes);
        JFreeChart allResponseChart = getAllResponseChart(request, totalParticipant, proCtcTerm);


//        allResponses = addEmptyValues(allResponses, ProCtcQuestionType.getByDisplayName(request.getParameter("attribute")));
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String worstResponseChartFileName = ServletUtilities.saveChartAsPNG(worstResponseChart, 700, 400, info, null);
        String worstResponseChartImageMap = ChartUtilities.getImageMap(worstResponseChartFileName, info);

        String allResponseChartFileName = ServletUtilities.saveChartAsPNG(allResponseChart, 700, 400, info, null);
        String allResponseChartImageMap = ChartUtilities.getImageMap(allResponseChartFileName, info);


        ModelAndView modelAndView = new ModelAndView("reports/symptomsummarycharts");
        modelAndView.addObject("worstResponseChartFileName", worstResponseChartFileName);
        modelAndView.addObject("worstResponseChartImageMap", worstResponseChartImageMap);
        modelAndView.addObject("allResponseChartFileName", allResponseChartFileName);
        modelAndView.addObject("allResponseChartImageMap", allResponseChartImageMap);
        modelAndView.addObject("allResponseChart", allResponseChart);
        modelAndView.addObject("worstResponseChart", worstResponseChart);
        modelAndView.addObject("allAttributes", allAttributes);
        modelAndView.addObject("selectedAttributes", selectedAttributes);
        modelAndView.addObject("symptom", proCtcTerm.getTerm());
        return modelAndView;
    }

    private JFreeChart getAllResponseChart(HttpServletRequest request, int totalParticipant, ProCtcTerm proCtcTerm) throws ParseException {
//        String title = "Participant reported responses for symptom " + symptom + " (All responses)";
        String title = "";
        SymptomSummaryAllResponsesQuery allResponsesQuery = new SymptomSummaryAllResponsesQuery();
        parseRequestParametersAndFormQuery(request, allResponsesQuery);
        List allResponses = genericRepository.find(allResponsesQuery);
        addEmptyValues(allResponses, proCtcTerm);
        int i = 0;
        int totalResponses = 0;
        String attribute = "";
        for (Object o : allResponses) {
            Object[] row = (Object[]) o;
            if (i == 0) {
                attribute = ((ProCtcQuestionType) row[2]).getDisplayName();
                totalResponses += (Long) row[0];
                i++;
            } else {
                String temp = ((ProCtcQuestionType) row[2]).getDisplayName();
                if (temp.equals(attribute)) {
                    totalResponses += (Long) row[0];
                }
            }
        }
        String domainAxisLabel = "Response Grade";
        String rangeAxisLabel = "Number of Responses (n=" + totalResponses + ", p=" + totalParticipant + ")";
        SymptomSummaryAllResponsesChartGenerator generator = new SymptomSummaryAllResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, totalResponses, request.getQueryString());
        return generator.getChart(allResponses);

    }

    private JFreeChart getWorstResponseChart(HttpServletRequest request, int totalParticipant, ProCtcTerm proCtcTerm, HashSet<String> selectedAttributes) throws ParseException {
//        String title = "Participant reported responses for symptom " + symptom + " (Worst responses)";
        String title = "";
        String domainAxisLabel = "Response Grade";
        String rangeAxisLabel = "Number of participants (" + totalParticipant + ")";
        SymptomSummaryWorstResponsesQuery worstResponsesQuery = new SymptomSummaryWorstResponsesQuery();
        parseRequestParametersAndFormQuery(request, worstResponsesQuery);
        List worstResponses = genericRepository.find(worstResponsesQuery);

        HashMap<String, TreeMap<Integer, Integer>> results = new HashMap<String, TreeMap<Integer, Integer>>();
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

        SymptomSummaryWorstResponsesChartGenerator generator = new SymptomSummaryWorstResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, totalParticipant, request.getQueryString());
        return generator.getChart(results);
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

    private List addEmptyValues(List results, ProCtcTerm proCtcTerm) {
        List newResults = new ArrayList();

        for (ProCtcQuestion q : proCtcTerm.getProCtcQuestions()) {
            for (ProCtcValidValue validValue : q.getValidValues()) {
                boolean found = false;
                for (Object o : results) {
                    Object[] obj = (Object[]) o;
                    if (obj[1].equals(validValue.getDisplayOrder())) {
                        found = true;
                        newResults.add(o);
                        break;
                    }
                }
                if (!found) {
                    newResults.add(new Object[]{0L, validValue.getDisplayOrder(), q.getProCtcQuestionType()});
                }
            }

        }
        return newResults;
    }


}
