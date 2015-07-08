package gov.nih.nci.ctcae.web.user;

/**
 * @author mehul
 * Date: 2/9/12
 */
public class SearchNotificationWrapper {

    SearchNotificationDTO[] searchNotificationDTOs;

    int recordsReturned;
    Long totalRecords;
    int startIndex;
    int pageSize;
    String dir;
    String sort;

    public SearchNotificationDTO[] getSearchNotificationDTOs() {
        return searchNotificationDTOs;
    }

    public void setSearchNotificationDTOs(SearchNotificationDTO[] searchNotificationDTOs) {
        this.searchNotificationDTOs = searchNotificationDTOs;
    }

    public int getRecordsReturned() {
        return recordsReturned;
    }

    public void setRecordsReturned(int recordsReturned) {
        this.recordsReturned = recordsReturned;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
