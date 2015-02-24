package gov.nih.nci.ctcae.web.organization;

/**
 * @author Amey
 * SearchOrganizationWrapper class.
 * Used with data transfer object on search organization screen.
 */
public class SearchOrganizationWrapper {

    SearchOrganizationDTO[] searchOrganizationDTOs;
    int recordsReturned;
    Long totalRecords;
    int startIndex;
    int pageSize;
    String dir;
    String sort;

    public SearchOrganizationDTO[] getSearchOrganizationDTOs() {
        return searchOrganizationDTOs;
    }

    public void setSearchOrganizationDTOs(SearchOrganizationDTO[] searchOrganizationDTOs) {
        this.searchOrganizationDTOs = searchOrganizationDTOs;
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
