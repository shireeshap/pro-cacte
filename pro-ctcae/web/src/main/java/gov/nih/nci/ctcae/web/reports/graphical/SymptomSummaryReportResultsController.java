package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.reports.ReportParticipantCountQuery;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryWorstResponsesQuery;
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


//        allResponses = addEmptyValues(allResponses, ProCtcQuestionType.getByDisplayName(request.getParameter("attribute")));
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



    private JFreeChart getWorstResponseChart(HttpServletRequest request, int totalParticipant, ProCtcTerm proCtcTerm, HashSet<String> selectedAttributes) throws ParseException {
//        String title = "Participant reported responses for symptom " + symptom + " (Worst responses)";
        String title = "";
        String domainAxisLabel = "Response Grade";
        String rangeAxisLabel = "Number of participants (" + totalParticipant + ")";
        SymptomSummaryWorstResponsesQuery worstResponsesQuery = new SymptomSummaryWorstResponsesQuery();
        parseRequestParametersAndFormQuery(request, worstResponsesQuery);
        List worstResponses = genericRepository.find(worstResponsesQuery);

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
