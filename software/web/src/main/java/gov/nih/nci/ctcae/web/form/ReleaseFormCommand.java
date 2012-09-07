package gov.nih.nci.ctcae.web.form;

import java.util.Date;

/**
 * @author mehul
 *         Date: 2/16/12
 */
public class ReleaseFormCommand {
    private Date effectiveStartDate;
    private String title;
    private Integer studyId;
    private Integer crfId;
    private boolean isReleased;

    public Date getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(Date effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public Integer getCrfId() {
        return crfId;
    }

    public void setCrfId(Integer crfId) {
        this.crfId = crfId;
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void setReleased(boolean released) {
        isReleased = released;
    }
}
