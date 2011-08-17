package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.domain.rules.NotificationRule;
import gov.nih.nci.ctcae.core.domain.rules.SiteCRFNotificationRule;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//

/**
 * The Class StudyOrganization.
 *
 * @author
 * @since Oct 7, 2008
 */

@Entity
@Table(name = "STUDY_ORGANIZATIONS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_organizations_id")})
public abstract class StudyOrganization extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

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

    /**
     * The study participant assignments.
     */
    @OneToMany(mappedBy = "studySite", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantAssignment> studyParticipantAssignments = new ArrayList<StudyParticipantAssignment>();

    @OneToMany(mappedBy = "studySite", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<SiteCRFNotificationRule> siteCRFNotificationRules = new ArrayList<SiteCRFNotificationRule>();


    @OneToMany(mappedBy = "studyOrganization", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = new ArrayList<StudyOrganizationClinicalStaff>();

    /**
     * The organization.
     */
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    /**
     * The study.
     */
    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Transient
    private String displayName;

    /**
     * Gets the study participant assignments.
     *
     * @return the study participant assignments
     */
    public List<StudyParticipantAssignment> getStudyParticipantAssignments() {
        return studyParticipantAssignments;
    }

    /**
     * Gets the study.
     *
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

    /**
     * Gets the organization.
     *
     * @return the organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Sets the organization.
     *
     * @param organization the new organization
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * Sets the study.
     *
     * @param study the new study
     */
    public void setStudy(Study study) {
        this.study = study;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) return false;

        StudyOrganization that = (StudyOrganization) o;
        if(getId() != null && that.getId() != null){
            return getId().equals(that.getId());
        }

        if (organization != null ? !organization.equals(that.organization) : that.organization != null) return false;
        if (study != null ? !study.equals(that.study) : that.study != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        int result = organization != null ? organization.hashCode() : 0;
        result = 31 * result + (study != null ? study.hashCode() : 0);
        return result;
    }

    public void addOrUpdateStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        if (studyOrganizationClinicalStaff != null) {

            Organization expectedOrganization = studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getOrganization();
            if (!expectedOrganization.equals(this.getOrganization())) {
                String errorMessage = String.format(studyOrganizationClinicalStaff.getDisplayName() + " does not belong to %s", this.getOrganization().getDisplayName());
                logger.error(errorMessage);
                getStudyOrganizationClinicalStaffs().remove(studyOrganizationClinicalStaff);
                throw new CtcAeSystemException(errorMessage);

            }

            studyOrganizationClinicalStaff.setStudyOrganization(this);
            if (!studyOrganizationClinicalStaff.isPersisted()) {
                getStudyOrganizationClinicalStaffs().add(studyOrganizationClinicalStaff);
                logger.debug(String.format("added study organization clinical staff %s to study organization %s", studyOrganizationClinicalStaff.toString(), toString()));
            } else {
                logger.debug(String.format("skipping adding of %s because it is already persisted", studyOrganizationClinicalStaff.toString()));
            }
        }

    }

    public List<StudyOrganizationClinicalStaff> getStudyOrganizationClinicalStaffs() {
        return studyOrganizationClinicalStaffs;
    }

    public String getDisplayName() {
        if (StringUtils.isBlank(displayName)) {
            displayName = organization.getName();
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<StudyOrganizationClinicalStaff> getStudyOrganizationClinicalStaffByRole(Role role) {
        Date today = new Date();
        List<StudyOrganizationClinicalStaff> l = new ArrayList<StudyOrganizationClinicalStaff>();
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganizationClinicalStaffs) {
            if (studyOrganizationClinicalStaff.getRole().equals(role) && studyOrganizationClinicalStaff.getRoleStatus().equals(RoleStatus.ACTIVE) && studyOrganizationClinicalStaff.getNotify() && studyOrganizationClinicalStaff.getStatusDate().before(today))
                l.add(studyOrganizationClinicalStaff);
        }
        return l;
    }

    public List<StudyOrganizationClinicalStaff> getStudyOrganizationClinicalStaffByRoleNotify(Role role) {
        Date today = new Date();
        List<StudyOrganizationClinicalStaff> l = new ArrayList<StudyOrganizationClinicalStaff>();
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganizationClinicalStaffs) {
            if (studyOrganizationClinicalStaff.getRole().equals(role) && studyOrganizationClinicalStaff.getRoleStatus().equals(RoleStatus.ACTIVE) && studyOrganizationClinicalStaff.getStatusDate().before(today))
                l.add(studyOrganizationClinicalStaff);
        }
        return l;
    }
    public List<StudyOrganizationClinicalStaff> getSitePIs() {
        return getStudyOrganizationClinicalStaffByRoleNotify(Role.SITE_PI);
    }

    public List<StudyOrganizationClinicalStaff> getSiteCRAs() {
        return getStudyOrganizationClinicalStaffByRoleNotify(Role.SITE_CRA);
    }

    public List<StudyOrganizationClinicalStaff> getResearchNurses() {
        return getStudyOrganizationClinicalStaffByRoleNotify(Role.NURSE);
    }

    public List<StudyOrganizationClinicalStaff> getTreatingPhysicians() {
        return getStudyOrganizationClinicalStaffByRoleNotify(Role.TREATING_PHYSICIAN);
    }

    public List<SiteCRFNotificationRule> getSiteCRFNotificationRules() {
        return siteCRFNotificationRules;
    }

    public void setSiteCRFNotificationRules(List<SiteCRFNotificationRule> siteCRFNotificationRules) {
        this.siteCRFNotificationRules = siteCRFNotificationRules;
    }

    public void addSiteCRFNotificationRules(SiteCRFNotificationRule siteCRFNotificationRule) {
        if (siteCRFNotificationRule != null) {
            siteCRFNotificationRule.setStudySite((StudySite) this);
            siteCRFNotificationRules.add(siteCRFNotificationRule);
        }
    }

    public List<NotificationRule> getNotificationRules() {
        List<NotificationRule> notificationRules = new ArrayList<NotificationRule>();
        for (SiteCRFNotificationRule siteCRFNotificationRule : getSiteCRFNotificationRules()) {
            notificationRules.add(siteCRFNotificationRule.getNotificationRule());
        }
        return notificationRules;

    }
}