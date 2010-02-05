package gov.nih.nci.ctcae.web.study;

import org.apache.commons.lang.StringUtils;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Feb 5, 2010
 * Time: 12:09:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudySearchResultsComparator implements Comparator {
    private int index = 0;
    private String sortDir;

    public StudySearchResultsComparator(String sortBy, String sortDir) {
        this.sortDir = sortDir;
        if ("studyIdentifier".equals(sortBy)) {
            index = 0;
        }
        if ("shortTitle".equals(sortBy)) {
            index = 1;
        }
        if ("fundingSponsor".equals(sortBy)) {
            index = 2;
        }
        if ("coordinatingCenter".equals(sortBy)) {
            index = 3;
        }
    }


    public int compare(Object o1, Object o2) {
        String[] arr1 = (String[]) o1;
        String[] arr2 = (String[]) o2;
        if ("asc".equals(sortDir)) {
            return arr1[index].compareTo(arr2[index]);
        } else {
            return arr2[index].compareTo(arr1[index]);
        }
    }
}