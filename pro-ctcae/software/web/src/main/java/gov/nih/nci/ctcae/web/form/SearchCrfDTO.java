package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfStatus;

import java.util.Date;

/**
 * @author mehul
 * Date: 12/7/11
 */

public class SearchCrfDTO {

    private String title;
    private String version;
    private String effectiveStartDate;
    private String status;
    private String studyShortTitle;
    private String actions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(String effectiveDate) {
        this.effectiveStartDate = effectiveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudyShortTitle() {
        return studyShortTitle;
    }

    public void setStudyShortTitle(String studyShortTitle) {
        this.studyShortTitle = studyShortTitle;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchCrfDTO that = (SearchCrfDTO) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (effectiveStartDate != null ? !effectiveStartDate.equals(that.effectiveStartDate) : that.effectiveStartDate != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return !(studyShortTitle != null ? !studyShortTitle.equals(that.studyShortTitle) : that.studyShortTitle != null);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (effectiveStartDate != null ? effectiveStartDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (studyShortTitle != null ? studyShortTitle.hashCode() : 0);
        return result;
    }
}
