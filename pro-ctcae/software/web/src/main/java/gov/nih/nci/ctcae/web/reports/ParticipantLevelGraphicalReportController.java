package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

        String inputSymptom = request.getParameter("symptomId");
        HashSet<String> allAttributes = new HashSet<String>();
        if (inputSymptom.startsWith("P_")) {
            ProCtcTerm proCtcTerm = proCtcTermRepository.findById(Integer.parseInt(inputSymptom.substring(2)));
            if (proCtcTerm != null) {
                for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
                    allAttributes.add(question.getProCtcQuestionType().getDisplayName());
                }
            }
        }
        if (inputSymptom.startsWith("M_")) {
            LowLevelTerm lowLevelterm = genericRepository.findById(LowLevelTerm.class, Integer.parseInt(inputSymptom.substring(2)));
            if (lowLevelterm != null) {
                for (MeddraQuestion question : lowLevelterm.getMeddraQuestions()) {
                    allAttributes.add(question.getProCtcQuestionType().getDisplayName());
                }
            }
        }

        String selectedTypes = request.getParameter("selectedTypes");
        ArrayList<String> arrSelectedTypes = new ArrayList<String>();
        if (!StringUtils.isBlank(selectedTypes)) {
            StringTokenizer st = new StringTokenizer(selectedTypes, "_");
            while (st.hasMoreTokens()) {
                arrSelectedTypes.add(st.nextToken());
            }
        }
        if (arrSelectedTypes.size() == 0) {
            arrSelectedTypes.addAll(allAttributes);
        }
        ParticipantLevelChartGenerator chartGenerator = new ParticipantLevelChartGenerator();
        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results = (TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<String> dates = (ArrayList<String>) request.getSession().getAttribute("sessionDates");
        String baselineDate = (String) request.getSession().getAttribute("baselineDate");

        JFreeChart chart = chartGenerator.getChartForSymptom(results, dates, inputSymptom, arrSelectedTypes, baselineDate);

        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String worstResponseChartFileName = ServletUtilities.saveChartAsPNG(chart, 700, 450, info, null);
        ModelAndView modelAndView = new ModelAndView("reports/participantreportcharts");
        modelAndView.addObject("participantReportChartFileName", worstResponseChartFileName);
        modelAndView.addObject("participantReportChart", chart);
        modelAndView.addObject("allAttributes", allAttributes);
        modelAndView.addObject("selectedAttributes", arrSelectedTypes);
        modelAndView.addObject("symptom", inputSymptom);
        return modelAndView;
    }


}