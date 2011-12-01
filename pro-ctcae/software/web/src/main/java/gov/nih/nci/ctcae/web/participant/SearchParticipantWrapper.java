package gov.nih.nci.ctcae.web.participant;

/**
 * @author mehul
 *         Date: 11/29/11
 */
public class SearchParticipantWrapper {

    SearchParticipantDTO[] searchParticipantDTOs;

    int recordsReturned;
    int totalRecords;
    int startIndex = 0;

    public SearchParticipantDTO[] getSearchParticipantDTOs() {
        return searchParticipantDTOs;
    }

    public void setSearchParticipantDTOs(SearchParticipantDTO[] searchParticipantDTOs) {
        this.searchParticipantDTOs = searchParticipantDTOs;
    }

    public int getRecordsReturned() {
        return recordsReturned;
    }

    public void setRecordsReturned(int recordsReturned) {
        this.recordsReturned = recordsReturned;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
