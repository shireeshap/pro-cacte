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
        if (object.getCrfPage().getPageNumber() == object1.getCrfPage().getPageNumber()) {
            return object.getDisplayOrder().compareTo(object1.getDisplayOrder());
        } else {
            return object.getCrfPage().getPageNumber().compareTo(object1.getCrfPage().getPageNumber());
        }

    }

    /* (non-Javadoc)
      * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
      */
    public int compare(final Object object, final Object object1) {
        if (object instanceof CrfPageItem) {
            return compareCrfPageItem((CrfPageItem) object, (CrfPageItem) object1);
        } else if (object instanceof CRFPage) {
            return compareCrfPage((CRFPage) object, (CRFPage) object1);
        } else if (object instanceof ProCtcValidValue || object instanceof MeddraValidValue) {
            return compareValidValue((ValidValue) object, (ValidValue) object1);
        } else if (object instanceof StudyParticipantCrfItem) {
            return compareCrfPageItem(((StudyParticipantCrfItem) object).getCrfPageItem(), ((StudyParticipantCrfItem) object1).getCrfPageItem());
        } else if (object instanceof StudyParticipantCrfScheduleAddedQuestion) {
            return compareStudyParticipantCrfScheduleAddedQuestion((StudyParticipantCrfScheduleAddedQuestion) object, (StudyParticipantCrfScheduleAddedQuestion) object1);
        }

        try {
            throw new Exception("Unsupported object type for DisplayOrderComparator");
        } catch (Exception e) {
            e.printStackTrace();
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

    private int compareValidValue(ValidValue validValue, ValidValue validValue1) {
        return validValue.getDisplayOrder().compareTo(validValue1.getDisplayOrder());
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
