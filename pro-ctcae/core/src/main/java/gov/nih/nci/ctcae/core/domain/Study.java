package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.UniqueObjectInCollection;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//
/**
 * The Class Study.
 *
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "STUDIES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_studies_id")})

public class Study extends BasePersistable {

    /**
     * The Constant DEFAULT_SITE_NAME.
     */
    public static final String DEFAULT_SITE_NAME = "default";

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The short title.
     */
    @Column(name = "SHORT_TITLE", nullable = false)
    private String shortTitle;

    /**
     * The long title.
     */
    @Column(name = "LONG_TITLE", nullable = false)
    private String longTitle;

    /**
     * The description.
     */
    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    /**
     * The assigned identifier.
     */
    @Column(name = "ASSIGNED_IDENTIFIER", nullable = false)
    private String assignedIdentifier;

    /**
     * The study funding sponsor.
     */
    @Transient
    private StudySponsor studySponsor;

    /**
     * The study coordinating center.
     */
    @Transient
    private DataCoordinatingCenter dataCoordinatingCenter;
    @Transient
    private LeadStudySite leadStudySite;
    @Transient
    private FundingSponsor fundingSponsor;


    /**
     * The study organizations.
     */
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyOrganization> studyOrganizations = new LinkedList<StudyOrganization>();


    public List<CRF> getCrfs() {
        return crfs;
    }

    public void setCrfs(List<CRF> crfs) {
        this.crfs = crfs;
    }

    /**
     * The Crfs assigned to study.
     */
    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<CRF> crfs = new LinkedList<CRF>();


    /**
     * Gets the study sites.
     *
     * @return the study sites
     */
    @UniqueObjectInCollection(message = "Duplicate Site")
    public List<StudySite> getStudySites() {
        List<StudySite> studySites = new LinkedList<StudySite>();
        for (StudyOrganization studyOrganization : getStudyOrganizations()) {
            if ((studyOrganization instanceof StudySite) && !(studyOrganization instanceof LeadStudySite)) {
                studySites.add((StudySite) studyOrganization);
            }
        }
        return studySites;
    }

    /**
     * Gets the study funding sponsor.
     *
     * @return the study funding sponsor
     */
    public StudySponsor getStudySponsor() {

        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization instanceof StudySponsor) {
                studySponsor = (StudySponsor) studyOrganization;
            }
        }

        return studySponsor;

    }

    /**
     * Sets the study funding sponsor.
     *
     * @param studySponsor the new study funding sponsor
     */
    public void setStudySponsor(StudySponsor studySponsor) {
        this.studySponsor = studySponsor;
        if (!studySponsor.isPersisted()) {
            addStudyOrganization(studySponsor);
        }
    }


    public LeadStudySite getLeadStudySite() {
        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization instanceof LeadStudySite) {
                leadStudySite = (LeadStudySite) studyOrganization;
            }
        }
        return leadStudySite;
    }

    public void setLeadStudySite(LeadStudySite leadStudySite) {
        this.leadStudySite = leadStudySite;
        if (!leadStudySite.isPersisted()) {
            addStudyOrganization(leadStudySite);
        }
    }


    public FundingSponsor getFundingSponsor() {
        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization instanceof FundingSponsor) {
                fundingSponsor = (FundingSponsor) studyOrganization;
            }
        }
        return fundingSponsor;
    }

    public void setFundingSponsor(FundingSponsor fundingSponsor) {
        this.fundingSponsor = fundingSponsor;
        if (!fundingSponsor.isPersisted()) {
            addStudyOrganization(fundingSponsor);
        }
    }

    /**
     * Gets the study coordinating center.
     *
     * @return the study coordinating center
     */
    public DataCoordinatingCenter getDataCoordinatingCenter() {
        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization instanceof DataCoordinatingCenter) {
                dataCoordinatingCenter = (DataCoordinatingCenter) studyOrganization;
            }
        }
        return dataCoordinatingCenter;
    }

    /**
     * Sets the study coordinating center.
     *
     * @param dataCoordinatingCenter the new study coordinating center
     */
    public void setDataCoordinatingCenter(
            DataCoordinatingCenter dataCoordinatingCenter) {
        this.dataCoordinatingCenter = dataCoordinatingCenter;
        if (!dataCoordinatingCenter.isPersisted()) {
            addStudyOrganization(dataCoordinatingCenter);
        }
    }


    /**
     * Gets the study organizations.
     *
     * @return the study organizations
     */
    public List<StudyOrganization> getStudyOrganizations() {
        return studyOrganizations;
    }

    /**
     * Gets the short title.
     *
     * @return the short title
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * Sets the short title.
     *
     * @param shortTitle the new short title
     */
    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    /**
     * Gets the long title.
     *
     * @return the long title
     */
    public String getLongTitle() {
        return longTitle;
    }

    /**
     * Sets the long title.
     *
     * @param longTitle the new long title
     */
    public void setLongTitle(String longTitle) {
        this.longTitle = longTitle;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return getShortTitle()
                + (getAssignedIdentifier() == null ? "" : " ("
                + getAssignedIdentifier() + ")");
    }

    /**
     * Gets the assigned identifier.
     *
     * @return the assigned identifier
     */
//    @UniqueIdentifierForStudy(message = "Identifier already exists.")
    public String getAssignedIdentifier() {
        return assignedIdentifier;
    }

    /**
     * Sets the assigned identifier.
     *
     * @param assignedIdentifier the new assigned identifier
     */
    public void setAssignedIdentifier(String assignedIdentifier) {
        this.assignedIdentifier = assignedIdentifier;
    }

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
     * Adds the study site.
     *
     * @param studySite the study site
     */
    public void addStudySite(StudySite studySite) {
        addStudyOrganization(studySite);

    }

    /**
     * Adds the study organization.
     *
     * @param studyOrganization the study organization
     */
    private void addStudyOrganization(StudyOrganization studyOrganization) {
        if (studyOrganization != null) {
            studyOrganization.setStudy(this);
            studyOrganizations.add(studyOrganization);
        }
    }

    /**
     * Removes the study organization.
     *
     * @param studyOrganization the study organization
     */
    private void removeStudyOrganization(StudyOrganization studyOrganization) {
        studyOrganizations.remove(studyOrganization);

    }

    /**
     * Removes the study site.
     *
     * @param studySite the study site
     */
    public void removeStudySite(StudySite studySite) {
        removeStudyOrganization(studySite);

    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Study study = (Study) o;

        if (assignedIdentifier != null ? !assignedIdentifier.equals(study.assignedIdentifier) : study.assignedIdentifier != null)
            return false;
        if (longTitle != null ? !longTitle.equals(study.longTitle) : study.longTitle != null) return false;
        if (shortTitle != null ? !shortTitle.equals(study.shortTitle) : study.shortTitle != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = shortTitle != null ? shortTitle.hashCode() : 0;
        result = 31 * result + (longTitle != null ? longTitle.hashCode() : 0);
        result = 31 * result + (assignedIdentifier != null ? assignedIdentifier.hashCode() : 0);
        return result;
    }

    public StudySite getStudySiteById(Integer studySiteId) {
        for (StudySite studySite : getStudySites()) {
            if (studySite.getId().equals(studySiteId)) {
                return studySite;
            }
        }
        return null;


    }

    public StudyOrganization getStudyOrganization(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        StudyOrganization expectedStudyOrganization = studyOrganizationClinicalStaff.getStudyOrganization();
        for (StudyOrganization studyOrganization : studyOrganizations) {
            if (studyOrganization.getId().equals(expectedStudyOrganization.getId())) {
                return studyOrganization;
            }
        }


        return null;
    }


    public StudyOrganizationClinicalStaff getStudyOrganizationClinicalStaffByRole(Role role) {
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffList = getAllStudyOrganizationClinicalStaffs();
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganizationClinicalStaffList) {
            if (studyOrganizationClinicalStaff.getRole().equals(role)) {
                return studyOrganizationClinicalStaff;
            }
        }
        return null;
    }


    public StudyOrganizationClinicalStaff getOverallDataCoordinator() {
        StudyOrganizationClinicalStaff studyOrganizationClinicalStaff = getStudyOrganizationClinicalStaffByRole(Role.ODC);
        if (studyOrganizationClinicalStaff == null) {
            studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
            studyOrganizationClinicalStaff.setRole(Role.ODC);

        }

        return studyOrganizationClinicalStaff;
    }

    public StudyOrganizationClinicalStaff getPrincipalInvestigator() {
        StudyOrganizationClinicalStaff principalInvestigator = getStudyOrganizationClinicalStaffByRole(Role.PI);

        if (principalInvestigator == null) {
            principalInvestigator = new StudyOrganizationClinicalStaff();
            principalInvestigator.setRole(Role.PI);

        }
        return principalInvestigator;
    }

    public StudyOrganizationClinicalStaff getLeadCRA() {
        StudyOrganizationClinicalStaff leadCRA = getStudyOrganizationClinicalStaffByRole(Role.LEAD_CRA);

        if (leadCRA == null) {
            leadCRA = new StudyOrganizationClinicalStaff();
            leadCRA.setRole(Role.LEAD_CRA);

        }
        return leadCRA;
    }

    public List<StudyOrganizationClinicalStaff> getStudySiteLevelStudyOrganizationClinicalStaffs() {
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffList = new ArrayList<StudyOrganizationClinicalStaff>();
        for (StudyOrganization studyOrganization : studyOrganizations) {
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganization.getStudyOrganizationClinicalStaffs()) {
                if (studyOrganizationClinicalStaff.getRole().equals(Role.SITE_CRA)
                        || studyOrganizationClinicalStaff.getRole().equals(Role.SITE_PI)
                        || studyOrganizationClinicalStaff.getRole().equals(Role.TREATING_PHYSICIAN)
                        || studyOrganizationClinicalStaff.getRole().equals(Role.NURSE)) {
                    studyOrganizationClinicalStaffList.add(studyOrganizationClinicalStaff);
                }
            }
        }

        return studyOrganizationClinicalStaffList;
    }

    public List<StudyOrganizationClinicalStaff> getAllStudyOrganizationClinicalStaffs() {
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffList = new ArrayList<StudyOrganizationClinicalStaff>();
        for (StudyOrganization studyOrganization : studyOrganizations) {
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganization.getStudyOrganizationClinicalStaffs()) {
                studyOrganizationClinicalStaffList.add(studyOrganizationClinicalStaff);
            }
        }

        return studyOrganizationClinicalStaffList;
    }


}