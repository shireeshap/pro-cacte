package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//
/**
 * The Class Participant.
 *
 * @author mehul
 */

@Entity
@Table(name = "PARTICIPANTS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_participants_id")})
public class Participant extends Person {

    /**
     * The maiden name.
     */
    @Column(name = "maiden_name", nullable = true)
    private String maidenName;

    /**
     * The birth date.
     */
    @Column(name = "birth_date", nullable = true)
    private Date birthDate;

    /**
     * The gender.
     */
    @Column(name = "gender", nullable = true)
    private String gender;

    /**
     * The assigned identifier.
     */
    @Column(name = "mrn_identifier", nullable = false)
    private String assignedIdentifier;

    @Column(name = "email_address", nullable = true)
    private String emailAddress;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
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

    /**
     * The study participant assignments.
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        if (!super.equals(o)) return false;

        Participant that = (Participant) o;

        if (assignedIdentifier != null ? !assignedIdentifier.equals(that.assignedIdentifier) : that.assignedIdentifier != null)
            return false;
        if (birthDate != null ? !birthDate.equals(that.birthDate) : that.birthDate != null) return false;
        if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (maidenName != null ? !maidenName.equals(that.maidenName) : that.maidenName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (maidenName != null ? maidenName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (assignedIdentifier != null ? assignedIdentifier.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
