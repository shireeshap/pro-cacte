package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.core.domain.*;
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

        HashSet<Integer> selectedArms = ReportResultsHelper.getSelectedArms(request);
        if (selectedArms.size() == 0) {
            selectedArms.add(-1);
        }
        String symptom = request.getParameter("symptom");

        ModelAndView modelAndView = null;
        if (StringUtils.isBlank(symptom)) {
            modelAndView = generateTabularReport(request, selectedArms);
        } else {
            modelAndView = generateGraphicalReport(request, symptom, request.getQueryString(), selectedArms);
            addAllAttributesToModelAndView(request, modelAndView);
        }
        modelAndView.addObject("arms", getArms(request));
        modelAndView.addObject("selectedArms", selectedArms);
        return modelAndView;
    }


    private ModelAndView generateGraphicalReport(HttpServletRequest request, String symptom, String queryString, HashSet<Integer> selectedArms) throws IOException, ParseException {
        HashSet<String> selectedAttributes = new HashSet<String>();
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(symptom));
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String chartType = request.getParameter("chartType");
        ArrayList<Object[]> charts = new ArrayList<Object[]>();
        if ("line".equals(chartType)) {
            TreeMap<String, TreeMap<String, Integer>> results = new TreeMap<String, TreeMap<String, Integer>>();
            String countString = getCountString(request, selectedArms);
            for (Integer armid : selectedArms) {
                Arm arm = genericRepository.findById(Arm.class, armid);
                List queryResults = getQueryResults(request, arm);
                doCalculationsForOneSymptom(proCtcTerm, selectedAttributes, queryResults, arm, results, selectedArms.size() > 1);
            }
            JFreeChart chart = getWorstResponseChart(results, queryString, selectedArms.size() > 1, countString);
            String fileName = ServletUtilities.saveChartAsPNG(chart, 700, 400, info, null);
            String imageMap = ChartUtilities.getImageMap(fileName, info);
            Object[] chartInfo = new Object[]{"", fileName, imageMap, chart};
            charts.add(chartInfo);
        } else {
            for (Integer armid : selectedArms) {
                TreeMap<String, TreeMap<String, Integer>> results = new TreeMap<String, TreeMap<String, Integer>>();
                HashSet<Integer> temp = new HashSet<Integer>();
                temp.add(armid);
                String countString = getCountString(request, temp);
                Arm arm = genericRepository.findById(Arm.class, armid);
                String title = "";
                if (arm != null) {
                    title = " - " + arm.getTitle();
                }
                List queryResults = getQueryResults(request, arm);
                queryString = queryString + "&arm=" + armid;
                doCalculationsForOneSymptom(proCtcTerm, selectedAttributes, queryResults, arm, results, false);
                JFreeChart chart = getWorstResponseChart(results, queryString, false, countString);
                String fileName = ServletUtilities.saveChartAsPNG(chart, 700, 400, info, null);
                String imageMap = ChartUtilities.getImageMap(fileName, info);
                Object[] chartInfo = new Object[]{title, fileName, imageMap, chart};
                charts.add(chartInfo);
            }
        }
        ModelAndView modelAndView = new ModelAndView("reports/symptomsummarycharts");
        modelAndView.addObject("selectedAttributes", selectedAttributes);
        modelAndView.addObject("results", charts);
        modelAndView.addObject("chartType", chartType);
        return modelAndView;
    }


    private List getQueryResults(HttpServletRequest request, Arm arm) throws ParseException {
        SymptomSummaryWorstResponsesQuery query = new SymptomSummaryWorstResponsesQuery();
        ReportResultsHelper.parseRequestParametersAndFormQuery(request, query);
        query.filterByArm(arm);
        return genericRepository.find(query);
    }

    private ModelAndView generateTabularReport(HttpServletRequest request, HashSet<Integer> selectedArms) throws ParseException {
        HashSet<String> selectedAttributes = new HashSet<String>();

        TreeMap<String, Object[]> results = new TreeMap<String, Object[]>();
        for (Integer armid : selectedArms) {
            Arm arm = genericRepository.findById(Arm.class, armid);
            HashMap<String, ArrayList<Object[]>> symptomMap = getRecordsForAllSymptoms(request, arm);
            int numOfParticipantsOnArm = getParticipantCount(request, arm).intValue();
            TreeMap<ProCtcTerm, TreeMap<String, TreeMap<String, Integer>>> output = new TreeMap<ProCtcTerm, TreeMap<String, TreeMap<String, Integer>>>(new ProCtcTermComparator());
            for (String symptom : symptomMap.keySet()) {
                ProCtcTermQuery query = new ProCtcTermQuery();
                query.filterByTerm(symptom);
                ProCtcTerm proCtcTerm = genericRepository.findSingle(query);
                TreeMap<String, TreeMap<String, Integer>> map = new TreeMap<String, TreeMap<String, Integer>>();
                doCalculationsForOneSymptom(proCtcTerm, selectedAttributes, symptomMap.get(symptom), arm, map, false);
                output.put(proCtcTerm, map);
            }
            String armTitle = "";
            if (arm != null) {
                armTitle = arm.getTitle();
            }
            Object[] oArr = new Object[2];
            oArr[0] = numOfParticipantsOnArm;
            oArr[1] = output;
            results.put(armTitle, oArr);
        }
        ModelAndView modelAndView = new ModelAndView("reports/symptomsummarytable");
        modelAndView.addObject("results", results);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
        return modelAndView;
    }

    private HashMap<String, ArrayList<Object[]>> getRecordsForAllSymptoms(HttpServletRequest request, Arm arm) throws ParseException {
        HashMap<String, ArrayList<Object[]>> symptomMap = new HashMap<String, ArrayList<Object[]>>();
        List queryResults = getQueryResults(request, arm);
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
        return symptomMap;
    }


    private JFreeChart getWorstResponseChart(TreeMap<String, TreeMap<String, Integer>> results, String queryString, boolean multipleArms, String countString) throws ParseException {
        String title = "";
        String domainAxisLabel = "Response Grade";
        String rangeAxisLabel = "Number of participants ";
        int totalParticipant = -1;
        if (!multipleArms) {
            totalParticipant = Integer.parseInt(countString);
            rangeAxisLabel = rangeAxisLabel + "[N=" + countString + "]";

        }
        SymptomSummaryWorstResponsesChartGenerator generator = new SymptomSummaryWorstResponsesChartGenerator(title, domainAxisLabel, rangeAxisLabel, queryString, multipleArms, totalParticipant, countString);
        return generator.getChart(results);
    }

    private void doCalculationsForOneSymptom(ProCtcTerm proCtcTerm, HashSet<String> selectedAttributes, List worstResponses, Arm arm, TreeMap<String, TreeMap<String, Integer>> results, boolean multipleArms) {
        for (Object obj : worstResponses) {
            Object[] a = (Object[]) obj;
            String level = a[0].toString();
            ProCtcQuestionType qType = (ProCtcQuestionType) a[2];
            String attribute = (qType).getDisplayName();
            if (arm != null && multipleArms) {
                attribute = arm.getTitle() + "-" + attribute;
            }
            TreeMap<String, Integer> map = results.get(attribute);
            selectedAttributes.add(attribute);
            if (map == null) {
                map = new TreeMap<String, Integer>();
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
    }


    private void addEmptyValues(TreeMap<String, Integer> map, ProCtcTerm proCtcTerm, ProCtcQuestionType qType) {
        for (ProCtcQuestion q : proCtcTerm.getProCtcQuestions()) {
            if (q.getProCtcQuestionType().equals(qType)) {
                for (ProCtcValidValue validValue : q.getValidValues()) {
                    map.put(validValue.getDisplayOrder().toString(), 0);
                }
            }
        }
    }

}
