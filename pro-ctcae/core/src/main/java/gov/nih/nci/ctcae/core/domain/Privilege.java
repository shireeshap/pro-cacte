package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * @author Vinay Kumar
 */

@Entity
@Table(name = "PROCTCAE_PRIVILEGES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "SEQ_PROCTCAE_PRIVILEGES_ID")})
public class Privilege extends BasePersistable {

    public static final String PRIVILEGE_CREATE_FORM = "PRIVILEGE_CREATE_FORM";
    public static final String PRIVILEGE_EDIT_FORM = "PRIVILEGE_EDIT_FORM";
    public static final String PRIVILEGE_SEARCH_FORM = "PRIVILEGE_SEARCH_FORM";
    public static final String PRIVILEGE_COPY_FORM = "PRIVILEGE_COPY_FORM";
    public static final String PRIVILEGE_DELETE_FORM = "PRIVILEGE_DELETE_FORM";
    public static final String PRIVILEGE_VERSION_FORM = "PRIVILEGE_VERSION_FORM";
    public static final String PRIVILEGE_RELEASE_FORM = "PRIVILEGE_RELEASE_FORM";

    public static final String PRIVILEGE_CREATE_STUDY = "PRIVILEGE_CREATE_STUDY";
    public static final String PRIVILEGE_VIEW_STUDY = "PRIVILEGE_VIEW_STUDY";
    public static final String PRIVILEGE_ADD_STUDY_SITE = "PRIVILEGE_ADD_STUDY_SITE";
    public static final String PRIVILEGE_ADD_STUDY_CLINICAL_STAFF = "PRIVILEGE_ADD_STUDY_CLINICAL_STAFF";
    public static final String PRIVILEGE_ADD_STUDY_SITE_CLINICAL_STAFF = "PRIVILEGE_ADD_STUDY_SITE_CLINICAL_STAFF";
    public static final String PRIVILEGE_SEARCH_STUDY = "PRIVILEGE_SEARCH_STUDY";
    public static final String PRIVILEGE_EDIT_STUDY = "PRIVILEGE_EDIT_STUDY";


    public static final String PRIVILEGE_CREATE_PARTICIPANT = "PRIVILEGE_CREATE_PARTICIPANT";
    public static final String PRIVILEGE_VIEW_PARTICIPANT = "PRIVILEGE_VIEW_PARTICIPANT";
    public static final String PRIVILEGE_PARTICIPANT_SCHEDULE_CRF = "PRIVILEGE_PARTICIPANT_SCHEDULE_CRF";
    public static final String PRIVILEGE_PARTICIPANT_INBOX = "PRIVILEGE_PARTICIPANT_INBOX";
    public static final String PRIVILEGE_PARTICIPANT_ADD_CRF_SCHEDULE = "PRIVILEGE_PARTICIPANT_ADD_CRF_SCHEDULE";
    public static final String PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR = "PRIVILEGE_PARTICIPANT_DISPLAY_CALENDAR";
    public static final String PRIVILEGE_PARTICIPANT_DISPLAY_STUDY_SITES = "PRIVILEGE_PARTICIPANT_DISPLAY_STUDY_SITES";
    public static final String PRIVILEGE_PARTICIPANT_ADD_NOTIFICATION_CLINICAL_STAFF = "PRIVILEGE_PARTICIPANT_ADD_NOTIFICATION_CLINICAL_STAFF";
    public static final String PRIVILEGE_ADD_FORM_RULES = "PRIVILEGE_ADD_FORM_RULES";
    public static final String PRIVILEGE_ADD_SITE_RULES = "PRIVILEGE_ADD_SITE_RULES";

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "privilege_name", nullable = false, unique = true)
    private String privilegeName;

    @Column(name = "display_name", nullable = false)
    private String displayName;


    /* (non-Javadoc)
    * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
    */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Privilege privilege = (Privilege) o;

        if (privilegeName != null ? !privilegeName.equals(privilege.privilegeName) : privilege.privilegeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = privilegeName != null ? privilegeName.hashCode() : 0;
        return result;
    }
}