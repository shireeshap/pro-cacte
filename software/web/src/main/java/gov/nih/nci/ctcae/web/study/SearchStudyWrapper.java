package gov.nih.nci.ctcae.web.study;

public class SearchStudyWrapper {

	SearchStudyDTO[] searchStudyDTO;
	
	int recordsReturned;
	int totalRecords;
	int startIndex = 0;
    int pageSize;
    String dir;
    String sort;
	
	
	public SearchStudyDTO[] getSearchStudyDTO() {
		return searchStudyDTO;
	}
	public void setSearchStudyDTO(SearchStudyDTO[] searchStudyDTO) {
		this.searchStudyDTO = searchStudyDTO;
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
