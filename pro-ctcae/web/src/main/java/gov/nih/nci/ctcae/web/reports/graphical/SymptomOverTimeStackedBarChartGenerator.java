package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.KeyToGroupMap;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;

import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.domain.Arm;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomOverTimeStackedBarChartGenerator extends AbstractChartGenerator {
    private GenericRepository genericRepository;
    Color level0 = new Color(222, 216, 216);
    Color level1 = new Color(240, 249, 78);
    Color level2 = new Color(244, 187, 89);
    Color level3 = new Color(235, 122, 107);
    Color level4 = new Color(255, 54, 54);

    public SymptomOverTimeStackedBarChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString) {
        super(title, domainAxisLabel, rangeAxisLabel, true, -1, queryString, "SYMPTOM_OVER_TIME_STACKED_BAR_CHART");
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> data = (TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DecimalFormat df = new DecimalFormat("0");
        for (Integer armid : data.keySet()) {
            TreeMap<String, TreeMap<Integer, Integer>> temp = data.get(armid);
            for (String period : temp.keySet()) {
                TreeMap<Integer, Integer> map = temp.get(period);
                Integer sum = 0;
                for (Integer grade : map.keySet()) {
                    sum += map.get(grade);
                }
                for (Integer grade : map.keySet()) {
                    Float percentage = new Float(df.format(map.get(grade) * 100 / sum));
                    if (data.keySet().size() > 1) {
                        dataset.addValue(percentage, armid + " - " + grade, period);
                    } else {
                        dataset.addValue(percentage, armid + " - " + grade, period + " [N=" + sum + "]");
                    }
                }
            }
        }
        return dataset;
    }

    public JFreeChart getChartA(TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> results, HashSet<Integer> selectedArms) {
        return createChart(createDataSet(results), selectedArms);
    }

    private JFreeChart createChart(CategoryDataset dataset, HashSet<Integer> selectedArms) {
        JFreeChart chart = ChartFactory.createStackedBarChart3D(
                "",  // chart title
                rangeAxisLabel,                   // domain axis label
                domainAxisLabel,                      // range axis label
                dataset,                      // data
                PlotOrientation.VERTICAL,     // the plot orientation
                true,                         // include legend
                true,                         // tooltips
                false                         // urls
        );
        CategoryPlot plot = chart.getCategoryPlot();
        if (selectedArms.size() > 1) {
            GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
            KeyToGroupMap map = null;
            SubCategoryAxis domainAxis = new SubCategoryAxis(rangeAxisLabel);
            domainAxis.setCategoryMargin(0.10);
            for (Integer armid : selectedArms) {
                if (map == null) {
                    map = new KeyToGroupMap("G" + armid);
                }
                domainAxis.addSubCategory(genericRepository.findById(Arm.class, armid).getTitle());
                for (int i = 0; i < 5; i++) {
                    map.mapKeyToGroup(armid + " - " + i, "G" + armid);
                }
            }
            renderer.setSeriesToGroupMap(map);
            renderer.setItemMargin(0.10);
            plot.setDomainAxis(domainAxis);
            plot.setRenderer(renderer);
        }
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        formatChart(dataset, chart);
        for (int i = 0; i < selectedArms.size(); i++) {
            renderer.setSeriesPaint(i * 5, level0);
            renderer.setSeriesPaint(i * 5 + 1, level1);
            renderer.setSeriesPaint(i * 5 + 2, level2);
            renderer.setSeriesPaint(i * 5 + 3, level3);
            renderer.setSeriesPaint(i * 5 + 4, level4);
        }
        plot.setFixedLegendItems(createLegendItems());

        return chart;
    }

    private LegendItemCollection createLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("0", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level0);
        LegendItem item2 = new LegendItem("1", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level1);
        LegendItem item3 = new LegendItem("2", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level2);
        LegendItem item4 = new LegendItem("3", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level3);
        LegendItem item5 = new LegendItem("4", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, level4);
        result.add(item1);
        result.add(item2);
        result.add(item3);
        result.add(item4);
        result.add(item5);
        return result;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}