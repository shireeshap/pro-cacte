package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomSummaryWorstResponsesChartGenerator extends AbstractChartGenerator {

    public SymptomSummaryWorstResponsesChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, Integer total,String queryString) {
        super(title, domainAxisLabel, rangeAxisLabel, true, total, queryString+"&type=WOR");
    }

    public CategoryDataset createDataSet(Object results) {
        TreeMap<String, TreeMap<Integer, Integer>> temp = (TreeMap<String, TreeMap<Integer, Integer>>) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String a : temp.keySet()) {
            TreeMap<Integer, Integer> map = temp.get(a);
            ArrayList<Integer> l = new ArrayList(map.keySet());
            Collections.sort(l);
            for (Integer i : l) {
                dataset.addValue(map.get(i), a, i);
            }
        }
        return dataset;
    }
}