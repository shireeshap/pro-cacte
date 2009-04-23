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

//        ModelAndView modelAndView = super.handleRequestInternal(request, response);
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
        HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> dataForChart = null;
        ProCtcTerm selectedTerm = null;
        for (ProCtcTerm proCtcTerm : results.keySet()) {
            if (proCtcTerm.getId().equals(inputSymptomId)) {
                selectedTerm = proCtcTerm;
                dataForChart = results.get(selectedTerm);
                break;
            }
        }
        response.setContentType("image/png");
        CategoryDataset dataset = createDataset(dataForChart, dates, arrSelectedTypes);
        JFreeChart chart = createChart(dataset, selectedTerm);

        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 700, 500);
        response.getOutputStream().close();

        return null;
    }

    /**
     * Creates the dataset.
     *
     * @param dataForChart
     * @param dates
     * @param arrSelectedTypes
     * @return the category dataset
     */
    private static CategoryDataset createDataset(HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> dataForChart, ArrayList<Date> dates, ArrayList<String> arrSelectedTypes) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();


        int i = 0;
        for (Date date : dates) {
            for (ProCtcQuestion proCtcQuestion : dataForChart.keySet()) {
                ArrayList<ProCtcValidValue> proCtcValidValues = dataForChart.get(proCtcQuestion);
                ProCtcValidValue proCtcValidValue = proCtcValidValues.get(i);
                String questionType = proCtcValidValue.getProCtcQuestion().getProCtcQuestionType().getDisplayName();
                if (arrSelectedTypes == null) {
                    dataset.addValue(proCtcValidValue.getDisplayOrder(), questionType, DateUtils.format(date));
                } else {
                    if (arrSelectedTypes.contains(questionType)) {
                        dataset.addValue(proCtcValidValue.getDisplayOrder(), questionType, DateUtils.format(date));
                    }
                }
            }
            i++;
        }

        return dataset;
    }

    /**
     * Creates the chart.
     *
     * @param dataset      the dataset
     * @param selectedTerm
     * @return the j free chart
     */

    private static JFreeChart createChart(CategoryDataset dataset, ProCtcTerm selectedTerm) {

        String title = "";
        if (selectedTerm != null) {
            title = selectedTerm.getTerm();
        }
        JFreeChart chart = ChartFactory.createBarChart(
                title,       // chart title
                "Date",               // domain axis label
                "Value",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);


        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        Range range = new Range(0, 4);
        rangeAxis.setRange(range);
        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        CategoryItemRenderer categoryItemRenderer = plot.getRenderer();
        categoryItemRenderer.setBaseItemLabelsVisible(true);
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
                0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
                0.0f, 0.0f, new Color(64, 0, 0));
        GradientPaint gp3 = new GradientPaint(0.0f, 0.0f, Color.cyan,
                0.0f, 0.0f, new Color(64, 0, 0));
        GradientPaint gp4 = new GradientPaint(0.0f, 0.0f, Color.orange,
                0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        renderer.setSeriesPaint(4, gp4);
        renderer.setSeriesShape(0, new Rectangle(100, 1));
        renderer.setSeriesShape(1, new Rectangle(100, 100));
        renderer.setSeriesShape(2, new Rectangle(100, 200));
        renderer.setSeriesShape(3, new Rectangle(100, 300));
        renderer.setSeriesShape(4, new Rectangle(100, 400));

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }
}