package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class StudyOrganization.
 *
 * @author
 * @crated Oct 7, 2008
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
        if (o == null || getClass() != o.getClass()) return false;

        StudyOrganization that = (StudyOrganization) o;

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
                String errorMessage = String.format("clinical staff belongs to %s. It does not belongs to study orgaization %s %s. So this clincal staff can not be added",
                        expectedOrganization.getDisplayName(), this.getStudy().getAssignedIdentifier(), this.getOrganization().getDisplayName());
                logger.error(errorMessage);
                throw new CtcAeSystemException(errorMessage);

            }

            studyOrganizationClinicalStaff.setStudyOrganization(this);
            getStudyOrganizationClinicalStaffs().add(studyOrganizationClinicalStaff);
            logger.debug(String.format("added study organization clinical staff %s to study organization %s", studyOrganizationClinicalStaff.toString(), toString()));

        }

    }

    public List<StudyOrganizationClinicalStaff> getStudyOrganizationClinicalStaffs() {
        return studyOrganizationClinicalStaffs;
    }
}