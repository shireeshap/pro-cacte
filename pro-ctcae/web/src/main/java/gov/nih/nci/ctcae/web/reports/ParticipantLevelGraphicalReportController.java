package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * User: Harsh
 * Date: Jan 8, 2009
 * Time: 12:12:32 PM.
 */
public class ParticipantLevelGraphicalReportController extends ParticipantLevelReportResultsController {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer inputSymptomId = Integer.parseInt(request.getParameter("symptomId"));
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, inputSymptomId);
        HashSet<String> allAttributes = new HashSet<String>();
        for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
            allAttributes.add(question.getProCtcQuestionType().getDisplayName());
        }
        String selectedTypes = request.getParameter("selectedTypes");
        ArrayList<String> arrSelectedTypes = new ArrayList<String>();
        if (!StringUtils.isBlank(selectedTypes)) {
            StringTokenizer st = new StringTokenizer(selectedTypes, ",");
            while (st.hasMoreTokens()) {
                arrSelectedTypes.add(st.nextToken());
            }
        }
        if (arrSelectedTypes.size() == 0) {
            allAttributes.addAll(allAttributes);
        }
        ParticipantLevelChartGenerator chartGenerator = new ParticipantLevelChartGenerator();
        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results = (TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<String> dates = (ArrayList<String>) request.getSession().getAttribute("sessionDates");
        String baselineDate = (String) request.getSession().getAttribute("baselineDate");

        JFreeChart chart = chartGenerator.getChartForSymptom(results, dates, inputSymptomId, arrSelectedTypes, baselineDate);

        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String worstResponseChartFileName = ServletUtilities.saveChartAsPNG(chart, 700, 400, info, null);
        ModelAndView modelAndView = new ModelAndView("reports/participantreportcharts");
        modelAndView.addObject("participantReportChartFileName", worstResponseChartFileName);
        modelAndView.addObject("participantReportChart", chart);
        modelAndView.addObject("allAttributes", allAttributes);
        modelAndView.addObject("selectedAttributes", arrSelectedTypes);
        modelAndView.addObject("symptom", proCtcTerm);
        return modelAndView;
    }


}