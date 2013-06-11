package gov.nih.nci.ctcae.web.participant;

/**
 * @author mehul
 * Date: 11/29/11
 */

public class SearchParticipantDTO {

    private String firstName;
    private String lastName;
    private String studyParticipantIdentifier;
    private String organizationName;
    private String studyShortTitle;
    private String actions;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudyParticipantIdentifier() {
        return studyParticipantIdentifier;
    }

    public void setStudyParticipantIdentifier(String studyParticipantIdentifier) {
        this.studyParticipantIdentifier = studyParticipantIdentifier;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
}
