package gov.nih.nci.ctcae.core.utils.ranking;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Suneel Allareddy
 * @since 08-Feb-2011
 */
public class RankerTest extends TestCase {

    
    
    public void testRank() throws Exception {

     
        Ranker ranker = new Ranker("suneel Allareddy");

        List<RankedObject<String>> rankedList = new ArrayList<RankedObject<String>>();

        Serializer<String> serializer = new Serializer<String>(){
            public String serialize(String object) {
                return object;
            }
        };

        assertEquals(100000, ranker.rank("Suneel aLLAREDdy", serializer).getRank());
        assertEquals(9995, ranker.rank("Joel Suneel Allareddy", serializer).getRank());
        assertEquals(9986, ranker.rank("Some one name Suneel Allareddy has it", serializer).getRank());
        assertEquals(485, ranker.rank("Last day was resuneel allareddy day in here",  serializer).getRank());
        assertEquals(472, ranker.rank("Come here for resuneel on resuneel allareddy day sometime in the evening says suneel today.", serializer).getRank());
        assertEquals(50000, ranker.rank("Suneel aLLAREDdy's world",  serializer).getRank());
        assertEquals(0, ranker.rank("some one stop me",  serializer).getRank());
        assertEquals(982, ranker.rank("This sentence has suneel allareddy's name mentioned suneel allareddy says again suneel allareddy and cannot say suneel jose",serializer).getRank());
        assertEquals(0, ranker.rank("hello can I call suneel jose for a quick meeting allareddy said", serializer).getRank());

        ranker = new Ranker("MD39");

        assertEquals(9977, ranker.rank("M.D Anderson Hospital (MD39)",serializer).getRank());

        ranker = new Ranker("5876");
        assertEquals(49999, ranker.rank("(5876) Test Study",  serializer).getRank());
        assertEquals(9995, ranker.rank("jai (5876) Test Study",  serializer).getRank());
        assertEquals(9990, ranker.rank("jai hind (5876)",  serializer).getRank());

        ranker = new Ranker("study");
        assertEquals(9988, ranker.rank("(5876) Test Study",  serializer).getRank());

        ranker = new Ranker("(5");
        assertEquals(50000, ranker.rank("(5876) Test Study",  serializer).getRank());


        ranker = new Ranker("76)");
        assertEquals(497, ranker.rank("(5876) Test Study",  serializer).getRank());

        ranker = new Ranker("md5");
        assertEquals(49999, ranker.rank("(MD53) hello suneel",  serializer).getRank());

    }
}
