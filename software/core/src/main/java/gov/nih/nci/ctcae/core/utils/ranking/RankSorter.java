package gov.nih.nci.ctcae.core.utils.ranking;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Suneel Allareddy
 * @since 08-Feb-2011
 */
public class RankSorter {

    /**
     * Will sort based on the rank
     * @param list
     */
    public  <T extends Object>  void sort(List<RankedObject<T>> list){

      //sorts based on descending order of rank i.e. lowest rank first. 
       Collections.sort(list, new Comparator<RankedObject<T>>(){
        public int compare(RankedObject o1, RankedObject o2) {
            return o2.getRank() - o1.getRank();  
        }
      });
      
    }
}
