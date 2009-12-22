package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.Study;

import java.util.Comparator;
import java.util.Collection;
import java.util.SortedSet;

public class StudyDisplayNameComparator implements Comparator<Study> {
    public int compare(Study o1, Study o2) {
        return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
}
