package gov.nih.nci.ctcae.web.clinicalStaff;

/**
 * Created by IntelliJ IDEA.
 * User: c3pr
 * Date: 12/5/11
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchClinicalStaffWrapper {

    SearchClinicalStaffDTO[] searchClinicalStaffDTOs;
    int recordsReturned;
    Long totalRecords;
    int startIndex;
    int pageSize;
    String dir;
    String sort;

    public SearchClinicalStaffDTO[] getSearchClinicalStaffDTOs() {
        return searchClinicalStaffDTOs;
    }

    public void setSearchClinicalStaffDTOs(SearchClinicalStaffDTO[] searchClinicalStaffDTOs) {
        this.searchClinicalStaffDTOs = searchClinicalStaffDTOs;
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
