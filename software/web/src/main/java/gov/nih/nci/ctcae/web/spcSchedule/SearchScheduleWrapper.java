package gov.nih.nci.ctcae.web.spcSchedule;

/**
 * @author mehul
 * Date: 1/24/12
 */
public class SearchScheduleWrapper {

    SearchScheduleDTO[] searchScheduleDTOs;

    int recordsReturned;
    Long totalRecords;
    int startIndex;
    int pageSize;
    String dir;
    String sort;

    public SearchScheduleDTO[] getSearchScheduleDTOs() {
        return searchScheduleDTOs;
    }

    public void setSearchScheduleDTOs(SearchScheduleDTO[] searchScheduleDTOs) {
        this.searchScheduleDTOs = searchScheduleDTOs;
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
