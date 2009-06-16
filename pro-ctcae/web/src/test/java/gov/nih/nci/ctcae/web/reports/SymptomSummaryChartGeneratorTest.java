package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.web.WebTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SymptomSummaryChartGeneratorTest extends WebTestCase {


    public void setUp() throws Exception {
        super.setUp();

    }

    public void testGetChart() throws Exception {
        List l = new ArrayList();
        Object[] a = new Object[]{2L,"Mild"};
        Object[] b = new Object[]{2L,"Moderate"};
        Object[] c = new Object[]{2L,"Severe"};
        Object[] d = new Object[]{2L,"Very Severe"};
        Object[] e = new Object[]{2L,"Extremely Severe"};

        l.add(a);
        l.add(b);
        l.add(c);
        l.add(d);
        l.add(e);

        SymptomSummaryChartGenerator ss = new SymptomSummaryChartGenerator();
        ss.getChart(l,"Pain","Severity","All", "", 0L);
    }
}