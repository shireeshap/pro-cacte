package gov.nih.nci.ctcae.web.reports;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.Range;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.awt.*;

import gov.nih.nci.ctcae.core.domain.CtcCategory;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

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
        ArrayList<String> arrSelectedTypes = null;
        if (!StringUtils.isBlank(selectedTypes)) {
            StringTokenizer st = new StringTokenizer(selectedTypes, ",");
            arrSelectedTypes = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                arrSelectedTypes.add(st.nextToken());
            }
        }


        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results = (TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<Date> dates = (ArrayList<Date>) request.getSession().getAttribute("sessionDates");
        response.setContentType("image/png");
        JFreeChart chart = ChartGenerator.getChartForSymptom(results,dates, inputSymptomId, arrSelectedTypes);
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 700, 500);
        response.getOutputStream().close();

        return null;
    }


}