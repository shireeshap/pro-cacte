package gov.nih.nci.ctcae.web.clinicalStaff;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Feb 5, 2010
 * Time: 12:09:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClinicalStaffSearchResultsComparator implements Comparator {
    private int index = 0;
    private String sortDir;

    public ClinicalStaffSearchResultsComparator(String sortBy, String sortDir) {
        this.sortDir = sortDir;
        if ("lastName".equals(sortBy)) {
            index = 0;
        }
        if ("firstName".equals(sortBy)) {
            index = 1;
        }
        if ("site".equals(sortBy)) {
            index = 2;
        }
        if ("study".equals(sortBy)) {
            index = 3;
        }
    }


    public int compare(Object o1, Object o2) {
        String[] arr1 = (String[]) o1;
        String[] arr2 = (String[]) o2;
        if (arr1[index] != null && arr2[index] != null) {
            if ("asc".equals(sortDir)) {
                return arr1[index].toLowerCase().compareTo(arr2[index].toLowerCase());
            } else {
                return arr2[index].toLowerCase().compareTo(arr1[index].toLowerCase());
            }
        }
        return 0;
    }
}