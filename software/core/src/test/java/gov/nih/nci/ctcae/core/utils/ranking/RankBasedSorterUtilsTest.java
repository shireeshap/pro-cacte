package gov.nih.nci.ctcae.core.utils.ranking;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Suneel Allareddy
 * @since 08-Feb-2011
 */
public class RankBasedSorterUtilsTest extends TestCase {

    
    public void testSort() throws Exception {
        List<String> l = new ArrayList<String>();
          l.add("hello can I call suneel jose for a quick meeting allareddy said");
          l.add("Joel Suneel Allareddy");
          l.add("Some one name Suneel Allareddy has it");
          l.add("Come here for resuneel on resuneel allareddy day sometime in the evening says suneel today.");
          l.add("suneel allareddy's world");
          l.add("some body stop me");
          l.add("Suneel Allareddy");
          l.add("This sentence has suneel allareddy's name mentioned suneel allareddy says again suneel allareddy and cannot say suneel jose");
          l.add("Last day was resuneel allareddy day in here.");

        List<String> sortedList = RankBasedSorterUtils.sort(l, "suneel allareddy", new Serializer<String>(){
            public String serialize(String object) {
                return object;  
            }
        });

        assertEquals("Suneel Allareddy", sortedList.get(0));
        assertEquals("suneel allareddy's world", sortedList.get(1));
        assertEquals("some body stop me", sortedList.get(8));
        assertEquals("Some one name Suneel Allareddy has it", sortedList.get(3));
    }


    public void testSort2() throws Exception {
        List<String> l = new ArrayList<String>();
        l.add("hello suneel");
        l.add("( MD5 ) hello suneel");
        l.add("(MD53) hello suneel");
        l.add("Some one name Suneel Allareddy has it");
        l.add("There is a problem with MD5 checksum");
        l.add("checksum (MD5)");
        l.add("hello ( MD5 )");
        l.add("(MD5) cannot be empty");

        List<String> sortedList = RankBasedSorterUtils.sort(l, "md5", new Serializer<String>(){
            public String serialize(String object) {
                return object;
            }
        });


        assertEquals("(MD53) hello suneel", sortedList.get(0));
        assertEquals("(MD5) cannot be empty", sortedList.get(1));
        assertEquals("Some one name Suneel Allareddy has it", sortedList.get(7));
        assertEquals("( MD5 ) hello suneel", sortedList.get(2));
        assertEquals("hello ( MD5 )", sortedList.get(3));
    }

    public void testSort3() throws Exception {
        List<String> l = new ArrayList<String>();
        l.add("Some one name Suneel Allareddy has it");
        l.add("james said ( MD5 ) hello suneel");
        l.add("Hello (MD53) hello suneel");
        l.add("Oh my god");
        l.add("There is a problem with MD5 checksum");
        l.add("checksum (MD5)");
        l.add("do you see what i see");
        l.add("hello ( MD5 )");
        l.add("The (MD54) cannot be empty");
        l.add("comeon lets go");

        List<String> sortedList = RankBasedSorterUtils.sort(l, "md5", new Serializer<String>(){
            public String serialize(String object) {
                return object;
            }
        });
        assertEquals("hello ( MD5 )", sortedList.get(0));
        assertEquals("checksum (MD5)", sortedList.get(1));
        assertEquals("james said ( MD5 ) hello suneel", sortedList.get(2));
        assertEquals("There is a problem with MD5 checksum",sortedList.get(3));
        assertEquals("The (MD54) cannot be empty",sortedList.get(4));
        assertEquals("Hello (MD53) hello suneel",sortedList.get(5));

    }
}
