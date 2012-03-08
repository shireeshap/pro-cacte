package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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

    @Column(name = "creation_date", nullable = true)
    private Date creationDate;

    /**
     * The gender.                                                
     */
    @Column(name = "gender", nullable = true)
    private String gender;

    /**
     * The assigned identifier.
     */
    @Column(name = "mrn_identifier", nullable = true)
    private String assignedIdentifier;

    @Column(name = "email_address", nullable = true,unique = true)
    private String emailAddress;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = false)
    private User user;

    @Transient
    private String displayName;

    @Column(name = "user_number", nullable = true)
    private String userNumber;

    @Column(name = "pin_number", nullable = true)
    private Integer pinNumber;

    @Transient
    private Integer confirmPinNumber;
    
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

    private transient  List<StudyParticipantCrfSchedule> sortedStudyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>();

    public List<StudyParticipantCrfSchedule> getSortedStudyParticipantCrfSchedules() {
        return sortedStudyParticipantCrfSchedules;
    }

    public void setSortedStudyParticipantCrfSchedules(List<StudyParticipantCrfSchedule> sortedStudyParticipantCrfSchedules) {
        this.sortedStudyParticipantCrfSchedules = sortedStudyParticipantCrfSchedules;
    }




    public Participant() {
        this.creationDate = new Date();
    }

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

    public String getStudyParticipantIdentifier() {
        String spIdentifier = "";
        if (studyParticipantAssignments != null && studyParticipantAssignments.size() > 0) {
            spIdentifier = studyParticipantAssignments.get(0).getStudyParticipantIdentifier();
        }
        return spIdentifier;
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

        if (displayName == null) {
            StringBuilder name = new StringBuilder();

            name.append(StringUtils.isBlank(assignedIdentifier) ? "" : "(" + assignedIdentifier + ")");
            if (studyParticipantAssignments != null && studyParticipantAssignments.size() > 0) {
                String spid = studyParticipantAssignments.get(0).getStudyParticipantIdentifier();
                if (!StringUtils.isBlank(spid)) {
                    name.append("(" + spid + ") ");
                }
            }
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

            this.displayName = name.toString();
        }
        return displayName.replace(")(", ", ");
    }

    @Transient
    public String getDisplayNameForReports() {

        if (displayName == null) {
            StringBuilder name = new StringBuilder();

            if (studyParticipantAssignments != null && studyParticipantAssignments.size() > 0) {
                String spid = studyParticipantAssignments.get(0).getStudyParticipantIdentifier();
                if (!StringUtils.isBlank(spid)) {
                    name.append("(" + spid + ") ");
                }
            }
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

            this.displayName = name.toString();
        }
        return displayName.replace(")(", ", ");
    }
    
    
    @Transient
    public String getNameInitialsForReports() {
        StringBuilder name = new StringBuilder();
        if (getLastName() != null) {
            name.append(getLastName().charAt(0));
        }
        
        if (getFirstName() != null) {
        	name.append(' ');
            name.append(getFirstName().charAt(0));
        }

        if (getMiddleName() != null) {
        	name.append(' ');
            name.append(getMiddleName().charAt(0));
        }
        return name.toString().toUpperCase();
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(Integer pinNumber) {
        this.pinNumber = pinNumber;
    }

    public Integer getConfirmPinNumber() {
        return confirmPinNumber;
    }

    public void setConfirmPinNumber(Integer confirmPinNumber) {
        this.confirmPinNumber = confirmPinNumber;
    }
}
