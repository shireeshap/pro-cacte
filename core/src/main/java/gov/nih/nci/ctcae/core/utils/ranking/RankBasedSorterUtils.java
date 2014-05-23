package gov.nih.nci.ctcae.core.utils.ranking;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.nih.nci.ctcae.core.jdbc.support.MeddraAutoCompleterWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Suneel Allareddy
 * @since 08-Feb-2011
 */
public class RankBasedSorterUtils {
    private static final Log log = LogFactory.getLog(RankBasedSorterUtils.class);
    /**
     * Will sort the input list based on the rank.
     * 
     * @param orginalList - The list to sort
     * @param searchString - The search string, on which the sorting to be applied.
     * @param seralizer  - The serializer that knows how to get a string version of the object being sorted
     * @return  The rank based sorted list is returned. 
     */
    public static <T extends Object> List<T> sort(List<T> orginalList, String searchString, Serializer<T> seralizer){
        try{
            if(CollectionUtils.isEmpty(orginalList)) return orginalList;

            List<RankedObject<T>> rankedList = new ArrayList(orginalList.size());
            Ranker ranker = new Ranker(searchString);
            RankSorter rankSorter = new RankSorter();

            for(T o : orginalList){
                RankedObject<T> rankedObject =  ranker.rank(o, seralizer);
                rankedList.add(rankedObject);
            }
            rankSorter.sort(rankedList);
            return toList(rankedList);
        }catch(Exception e){
            log.warn("unable to compile the pattern", e);
            return orginalList;
        }
    }
    
    /**
     * Sorts meddraTerms using MeddraTermRanker. 
     * Result is displayed in AutoCompleter for reporting additional symptoms in participant interface.
     */
    public static List<String> sortMeddraTerms(List<MeddraAutoCompleterWrapper> wrappedResult, String searchString, Serializer<String> seralizer){
    	List<String> sortedMeddraTerms = new ArrayList<String>();
        try{
            if(CollectionUtils.isEmpty(wrappedResult)) return sortedMeddraTerms;

            List<RankedObject<MeddraAutoCompleterWrapper>> rankedList = new ArrayList(wrappedResult.size());
            MeddraTermRanker ranker = new MeddraTermRanker(searchString);
            RankSorter rankSorter = new RankSorter();

            for(MeddraAutoCompleterWrapper o : wrappedResult){
                RankedObject<MeddraAutoCompleterWrapper> rankedObject =  ranker.rank(o, seralizer);
                rankedList.add(rankedObject);
            }
            rankSorter.sort(rankedList);
            return toMeddraTermList(rankedList);
        }catch(Exception e){
            log.warn("unable to compile the pattern", e);
            return sortedMeddraTerms;
        }
    }


    private static <T extends Object> List<T> toList(List<RankedObject<T>> tList){
        ArrayList<T> list = new ArrayList<T>();
        for(RankedObject<T> r : tList){
            list.add(r.getObject());
        }
        return list;
    }
    
    private static List<String> toMeddraTermList(List<RankedObject<MeddraAutoCompleterWrapper>> tList){
        ArrayList<String> list = new ArrayList<String>();
        for(RankedObject<MeddraAutoCompleterWrapper> r : tList){
            list.add(r.getObject().getMeddraTerm());
        }
        return list;
    }
}