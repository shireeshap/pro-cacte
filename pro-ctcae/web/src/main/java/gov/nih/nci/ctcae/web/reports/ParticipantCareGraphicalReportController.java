package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
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
public class ParticipantCareGraphicalReportController extends ParticipantCareResultsController {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer inputSymptomId = Integer.parseInt(request.getParameter("symptomId"));
        String selectedTypes = request.getParameter("selectedTypes");
        String angle = request.getParameter("angle");
        ArrayList<String> arrSelectedTypes = null;
        if (!StringUtils.isBlank(selectedTypes)) {
            StringTokenizer st = new StringTokenizer(selectedTypes, ",");
            arrSelectedTypes = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                arrSelectedTypes.add(st.nextToken());
            }
        }

        ChartGenerator chartGenerator = new ChartGenerator();
        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results = (TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<Date> dates = (ArrayList<Date>) request.getSession().getAttribute("sessionDates");
        response.setContentType("image/png");
        JFreeChart chart = chartGenerator.getChartForSymptom(results,dates, inputSymptomId, arrSelectedTypes);
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 700, 450);
        response.getOutputStream().close();

        return null;
    }


}