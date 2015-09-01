package gov.nih.nci.ctcae.web.alert;

/**
 * Created by IntelliJ IDEA.
 * User: c3pr
 * Date: 12/5/11
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchAlertWrapper {

    SearchAlertDTO[] searchAlertDTOs;
    int recordsReturned;
	Long totalRecords;
	int startIndex;
    int pageSize;
    String dir;
    String sort;
    
    public String getDir() {
        return dir;
    }
    
    public int getPageSize() {
        return pageSize;
    }

    public int getRecordsReturned() {
        return recordsReturned;
    }

    public SearchAlertDTO[] getSearchAlertDTOs() {
		return searchAlertDTOs;
	}

    public String getSort() {
        return sort;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRecordsReturned(int recordsReturned) {
        this.recordsReturned = recordsReturned;
    }

    public void setSearchAlertDTOs(SearchAlertDTO[] searchAlertDTOs) {
		this.searchAlertDTOs = searchAlertDTOs;
	}

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }
}