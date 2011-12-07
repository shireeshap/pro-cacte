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
    private String nciIdentifier;
    private String status;
    private String study;
    private String site;
    private String actions;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNciIdentifier() {
        return nciIdentifier;
    }

    public void setNciIdentifier(String nciIdentifier) {
        this.nciIdentifier = nciIdentifier;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
