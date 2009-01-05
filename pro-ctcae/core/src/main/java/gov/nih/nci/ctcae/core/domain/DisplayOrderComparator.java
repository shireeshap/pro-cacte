package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class DisplayOrderComparator implements Comparator {
	private int compareCrfPageItem(CrfPageItem object, CrfPageItem object1) {
		return object.getDisplayOrder().compareTo(object1.getDisplayOrder());

	}

	public int compare(final Object object, final Object object1) {
		if (object instanceof CrfPageItem) {
			return compareCrfPageItem((CrfPageItem) object, (CrfPageItem) object1);
		} else if (object instanceof CRFPage) {
			return compareCrfPage((CRFPage) object, (CRFPage) object1);
		}

		return 0;

	}

	private int compareCrfPage(final CRFPage crfPage, final CRFPage crfPage1) {
		return crfPage.getPageNumber().compareTo(crfPage1.getPageNumber());


	}
}
