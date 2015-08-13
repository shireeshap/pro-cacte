package gov.nih.nci.ctcae.web.study;

public class SearchStudyDTO {

	public String shortTitle;
	public String assignedIdentifier;
	public String fundingSponsorDisplayName;
	public String coordinatingCenterDisplayName;
	public String actions;
	
	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getFundingSponsorDisplayName() {
		return fundingSponsorDisplayName;
	}

	public void setFundingSponsorDisplayName(String fundingSponsorDisplayName) {
		this.fundingSponsorDisplayName = fundingSponsorDisplayName;
	}

	public String getCoordinatingCenterDisplayName() {
		return coordinatingCenterDisplayName;
	}

	public void setCoordinatingCenterDisplayName(
			String coordinatingCenterDisplayName) {
		this.coordinatingCenterDisplayName = coordinatingCenterDisplayName;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getAssignedIdentifier() {
		return assignedIdentifier;
	}

	public void setAssignedIdentifier(String assignedIdentifier) {
		this.assignedIdentifier = assignedIdentifier;
	}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchStudyDTO that = (SearchStudyDTO) o;

        if (!shortTitle.equals(that.shortTitle)) return false;
        if (!assignedIdentifier.equals(that.assignedIdentifier)) return false;
        if (!fundingSponsorDisplayName.equals(that.fundingSponsorDisplayName)) return false;
        return coordinatingCenterDisplayName.equals(that.coordinatingCenterDisplayName);

    }

    @Override
    public int hashCode() {
        int result = shortTitle.hashCode();
        result = 31 * result + assignedIdentifier.hashCode();
        result = 31 * result + fundingSponsorDisplayName.hashCode();
        result = 31 * result + coordinatingCenterDisplayName.hashCode();
        return result;
    }
}
