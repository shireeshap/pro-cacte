package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.query.SymptomOverTimeReportQuery;
import gov.nih.nci.ctcae.core.query.SymptomSummaryParticipantCountQuery;
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


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class SymptomOverTimeReportResultsController extends AbstractReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String dateRange = "All";
        if (!StringUtils.isBlank(request.getParameter("startDate"))) {
            dateRange = request.getParameter("startDate") + " - " + request.getParameter("endDate");
        }
        SymptomOverTimeReportQuery query = new SymptomOverTimeReportQuery();
        parseRequestParametersAndFormQuery(request, query);
        List results = genericRepository.find(query);

        SymptomSummaryParticipantCountQuery query1 = new SymptomSummaryParticipantCountQuery();
        parseRequestParametersAndFormQuery(request, query1);
        List list = genericRepository.find(query1);
        Long l = (Long) list.get(0);

        TreeMap<Integer, Float> out = transformData(results);

        SymptomOverTimeChartGenerator
                chartGenerator = new SymptomOverTimeChartGenerator();
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(request.getParameter("symptom")));
        JFreeChart chart = chartGenerator.getChart(out, proCtcTerm.getTerm(), request.getParameter("attribute"), dateRange, request.getQueryString(), l);

        //  Write the chart image to the temporary directory
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String filename = ServletUtilities.saveChartAsPNG(chart, 700, 400, info, null);

        String imageMap = ChartUtilities.getImageMap(filename, info);

        Map model = new HashMap();
        model.put("filename", filename);
        model.put("imagemap", imageMap);
        ModelAndView modelAndView = new ModelAndView("reports/bar_chart", model);
        return modelAndView;
    }

    private TreeMap<Integer, Float> transformData(List results) {
        TreeMap<Integer, ArrayList<Integer>> temp = new TreeMap<Integer, ArrayList<Integer>>();
        TreeMap<Integer, Float> out = new TreeMap<Integer, Float>();
        Calendar c = Calendar.getInstance();
        for (Object obj : results) {
            ArrayList<Integer> holderForAvg;
            StudyParticipantCrfItem item = (StudyParticipantCrfItem) obj;
            Integer val = item.getProCtcValidValue().getDisplayOrder();
            Date startDate = item.getStudyParticipantCrfSchedule().getStartDate();
            c.setTime(startDate);
            int week = c.get(Calendar.WEEK_OF_YEAR);

            if (temp.containsKey(week)) {
                holderForAvg = temp.get(week);
            } else {
                holderForAvg = new ArrayList<Integer>();
                temp.put(week, holderForAvg);
            }
            holderForAvg.add(val.intValue());
        }

        for (Integer i : temp.keySet()) {
            ArrayList<Integer> l = temp.get(i);
            float sum = 0;
            for (Integer j : l) {
                sum += j;
            }
            float avg = sum / l.size();
            out.put(i, avg);
        }
        return out;
    }

}