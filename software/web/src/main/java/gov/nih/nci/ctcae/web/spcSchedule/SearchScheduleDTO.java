package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.core.domain.CrfStatus;

import java.util.Date;

/**
 * @author mehul
 * Date: 1/24/12
 */

public class SearchScheduleDTO {

    private String startDate;
    private String dueDate;
    private CrfStatus status;
    private String studyTitle;
    private String formTitle;
    private String participantName;
    private String actions;
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public CrfStatus getStatus() {
        return status;
    }

    public void setStatus(CrfStatus status) {
        this.status = status;
    }

    public String getStudyTitle() {
        return studyTitle;
    }

    public void setStudyTitle(String studyTitle) {
        this.studyTitle = studyTitle;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }
}
