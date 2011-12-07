package gov.nih.nci.ctcae.web.clinicalStaff;

/**
 * Created by IntelliJ IDEA.
 * User: c3pr
 * Date: 12/5/11
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchClinicalStaffDTO {
    private String firstName;
    private String lastName;
    private String identifier;
    private String status;
    private String study;
    private String actions;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }
}
