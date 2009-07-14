package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class DisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class DisplayOrderComparator implements Comparator {

    /**
     * Compare crf page item.
     *
     * @param object  the object
     * @param object1 the object1
     * @return the int
     */
    private int compareCrfPageItem(CrfPageItem object, CrfPageItem object1) {
        return object.getDisplayOrder().compareTo(object1.getDisplayOrder());

    }

    /* (non-Javadoc)
      * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
      */
    public int compare(final Object object, final Object object1) {
        if (object instanceof CrfPageItem) {
            return compareCrfPageItem((CrfPageItem) object, (CrfPageItem) object1);
        } else if (object instanceof CRFPage) {
            return compareCrfPage((CRFPage) object, (CRFPage) object1);
        } else if (object instanceof ProCtcValidValue) {
            compareValidValue((ProCtcValidValue) object, (ProCtcValidValue) object1);
        } else if (object instanceof StudyParticipantCrfItem) {
            compareCrfPageItem(((StudyParticipantCrfItem) object).getCrfPageItem(), ((StudyParticipantCrfItem) object1).getCrfPageItem());
        } else if (object instanceof StudyParticipantCrfScheduleAddedQuestion) {
            compareStudyParticipantCrfScheduleAddedQuestion((StudyParticipantCrfScheduleAddedQuestion) object, (StudyParticipantCrfScheduleAddedQuestion) object1);
        }

        return 0;

    }

    private int compareStudyParticipantCrfScheduleAddedQuestion(StudyParticipantCrfScheduleAddedQuestion object, StudyParticipantCrfScheduleAddedQuestion object1) {
        Integer fDisplayOrder = 0;
        Integer sDisplayOrder = 0;

        if (object.getProCtcQuestion() == null && object.getMeddraQuestion() == null) {
            return 0;
        }

        if (object.getProCtcQuestion() == null) {
            fDisplayOrder = object.getMeddraQuestion().getDisplayOrder();
        } else {
            fDisplayOrder = object.getProCtcQuestion().getDisplayOrder();
        }

        if (object1.getProCtcQuestion() == null) {
            sDisplayOrder = object1.getMeddraQuestion().getDisplayOrder();
        } else {
            sDisplayOrder = object1.getProCtcQuestion().getDisplayOrder();
        }
        return fDisplayOrder.compareTo(sDisplayOrder);
    }

    private int compareValidValue(ProCtcValidValue proCtcValidValue, ProCtcValidValue proCtcValidValue1) {
        return proCtcValidValue.getDisplayOrder().compareTo(proCtcValidValue1.getDisplayOrder());
    }

    /**
     * Compare crf page.
     *
     * @param crfPage  the crf page
     * @param crfPage1 the crf page1
     * @return the int
     */
    private int compareCrfPage(final CRFPage crfPage, final CRFPage crfPage1) {
        return crfPage.getPageNumber().compareTo(crfPage1.getPageNumber());


    }
}
