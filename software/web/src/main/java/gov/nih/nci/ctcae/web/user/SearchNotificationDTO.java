package gov.nih.nci.ctcae.web.user;

/**
 * @author mehul
 * Date: 2/9/12
 */

public class SearchNotificationDTO {

    private String date;
    private String studyTitle;
    private String participantName;
    private String actions;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudyTitle() {
        return studyTitle;
    }

    public void setStudyTitle(String studyTitle) {
        this.studyTitle = studyTitle;
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
