package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Participant.
 * 
 * @author mehul
 */

@Entity
@Table(name = "PARTICIPANTS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_participants_id")})
public class Participant extends Person {

    /** The maiden name. */
    @Column(name = "maiden_name", nullable = true)
    private String maidenName;

    /** The birth date. */
    @Column(name = "birth_date", nullable = true)
    private Date birthDate;

    /** The race. */
    @Column(name = "race", nullable = true)
    private String race;

    /** The ethnicity. */
    @Column(name = "ethnicity", nullable = true)
    private String ethnicity;

    /** The gender. */
    @Column(name = "gender", nullable = true)
    private String gender;

    /** The assigned identifier. */
    @Column(name = "mrn_identifier", nullable = false)
    private String assignedIdentifier;

    /**
     * Gets the assigned identifier.
     * 
     * @return the assigned identifier
     */
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

    /** The study participant assignments. */
    @OneToMany(mappedBy = "participant", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantAssignment> studyParticipantAssignments = new ArrayList<StudyParticipantAssignment>();

    /**
     * Gets the maiden name.
     * 
     * @return the maiden name
     */
    public String getMaidenName() {
        return maidenName;
    }

    /**
     * Sets the maiden name.
     * 
     * @param maidenName the new maiden name
     */
    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    /**
     * Gets the birth date.
     * 
     * @return the birth date
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date.
     * 
     * @param birthDate the new birth date
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the race.
     * 
     * @return the race
     */
    public String getRace() {
        return race;
    }

    /**
     * Sets the race.
     * 
     * @param race the new race
     */
    public void setRace(String race) {
        this.race = race;
    }

    /**
     * Gets the ethnicity.
     * 
     * @return the ethnicity
     */
    public String getEthnicity() {
        return ethnicity;
    }

    /**
     * Sets the ethnicity.
     * 
     * @param ethnicity the new ethnicity
     */
    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    /**
     * Gets the gender.
     * 
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender.
     * 
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Adds the study participant assignment.
     * 
     * @param spa the spa
     */
    public void addStudyParticipantAssignment(StudyParticipantAssignment spa) {
        if (spa != null) {
            spa.setParticipant(this);
            studyParticipantAssignments.add(spa);
        }
    }

    /**
     * Gets the study participant assignments.
     * 
     * @return the study participant assignments
     */
    public List<StudyParticipantAssignment> getStudyParticipantAssignments() {
        return studyParticipantAssignments;
    }

    /**
     * Removes the all study participant assignments.
     */
    public void removeAllStudyParticipantAssignments() {
        studyParticipantAssignments.clear();
    }

    /**
     * Gets the display name.
     * 
     * @return the display name
     */
    @Transient
    public String getDisplayName() {
        StringBuilder name = new StringBuilder();
        boolean hasLastName = getLastName() != null;
        if (getFirstName() != null) {
            name.append(getFirstName());
            if (hasLastName) {
                name.append(' ');
            }
        }
        if (hasLastName) {
            name.append(getLastName());
        }
        return name.toString();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Person#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Participant))
            return false;
        if (!super.equals(o))
            return false;

        Participant that = (Participant) o;

        if (birthDate != null ? !birthDate.equals(that.birthDate)
                : that.birthDate != null)
            return false;
        if (ethnicity != null ? !ethnicity.equals(that.ethnicity)
                : that.ethnicity != null)
            return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null)
            return false;
        if (maidenName != null ? !maidenName.equals(that.maidenName)
                : that.maidenName != null)
            return false;
        if (race != null ? !race.equals(that.race) : that.race != null)
            return false;

        return true;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Person#hashCode()
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (maidenName != null ? maidenName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (race != null ? race.hashCode() : 0);
        result = 31 * result + (ethnicity != null ? ethnicity.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        return result;
    }
}
