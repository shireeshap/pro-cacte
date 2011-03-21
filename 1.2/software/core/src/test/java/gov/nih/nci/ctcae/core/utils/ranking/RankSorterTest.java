package gov.nih.nci.ctcae.core.utils.ranking;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Suneel Allareddy
 * @since 08-Feb-2011
 */
public class RankSorterTest extends TestCase {
    public void testSort() throws Exception {
        RankedObject o1 = new RankedObject("rank 1");
        o1.setRank(1);
        RankedObject o2 = new RankedObject("rank 5");
        o2.setRank(5);
        RankedObject o3 = new RankedObject("rank 2");
        o3.setRank(2);
        RankedObject o4 = new RankedObject("rank 3");
        o4.setRank(3);
        RankedObject o5 = new RankedObject("rank 5");
        o5.setRank(5);
        RankedObject o6 = new RankedObject("rank 3");
        o6.setRank(3);

        List<RankedObject<String>> l = new ArrayList<RankedObject<String>>();
        l.add(o1);
        l.add(o2);
        l.add(o3);
        l.add(o4);
        l.add(o5);
        l.add(o6);
        new RankSorter().sort(l);
        assertEquals(5, l.get(0).getRank());
        assertEquals(1, l.get(5).getRank());

        
    }
}
