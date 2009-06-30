package gov.nih.nci.ctcae.web.reports.graphical;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.urls.CategoryURLGenerator;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 2:42:11 PM
 */
public class SymptomSummaryAllResponsesChartGenerator extends AbstractChartGenerator {

    public SymptomSummaryAllResponsesChartGenerator(String title, String domainAxisLabel, String rangeAxisLabel, Integer total,String queryString) {
        super(title, domainAxisLabel, rangeAxisLabel, true, total, queryString+"&type=ALL");
    }

    public CategoryDataset createDataSet(Object results) {
        List temp = (List) results;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object obj : temp) {
            Object[] o = (Object[]) obj;
            dataset.addValue((Long) o[0], ((ProCtcQuestionType) o[2]).getDisplayName(), (Integer) o[1]);
        }
        return dataset;
    }

   

}