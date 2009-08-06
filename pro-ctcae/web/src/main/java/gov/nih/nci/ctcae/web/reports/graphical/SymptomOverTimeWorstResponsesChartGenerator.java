package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomOverTimeWorstResponsesChartGenerator extends AbstractChartGenerator {
    public SymptomOverTimeWorstResponsesChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, String queryString, boolean multipleArms) {
        super(title, domainAxisLabel, rangeAxisLabel, queryString, "SYMPTOM_OVER_TIME_BAR_CHART", multipleArms);
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<String, TreeMap<String, ArrayList<Integer>>> attributeMap = (TreeMap<String, TreeMap<String, ArrayList<Integer>>>) results;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String attribute : attributeMap.keySet()) {
            TreeMap<String, ArrayList<Integer>> periodMap = attributeMap.get(attribute);
            for (String period : periodMap.keySet()) {
                ArrayList<Integer> grades = periodMap.get(period);
                float average = calculateListAverage(grades);
                int n = grades.size();
                dataset.addValue(average, attribute, period + "[N=" + n + "]");
            }
        }
        return dataset;
    }

    private TreeMap<String, TreeMap<String, Float>> calculateAverages(TreeMap<String, TreeMap<String, ArrayList<Integer>>> results) {
        //Treemap structure - Attribute, Period, Average
        //eg. Severity, Week 1, 2.4
        TreeMap<String, TreeMap<String, Float>> out = new TreeMap<String, TreeMap<String, Float>>();
        for (String attribute : results.keySet()) {
            TreeMap<String, Float> mapOut = new TreeMap<String, Float>();
            TreeMap<String, ArrayList<Integer>> mapIn = results.get(attribute);
            for (String period : mapIn.keySet()) {
                mapOut.put(period, calculateListAverage(mapIn.get(period)));
            }
            out.put(attribute, mapOut);
        }
        return out;
    }

    private float calculateListAverage(List<Integer> list) {
        float sum = 0;
        for (Integer j : list) {
            sum += j;
        }
        return sum / list.size();
    }

}