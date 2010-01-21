package gov.nih.nci.ctcae.core.domain.meddra;


import gov.nih.nci.ctcae.core.domain.MeddraQuestion;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meddra_llt")
public class LowLevelTerm extends AbstractMeddraDomainObject {


    private List<MeddraQuestion> meddraQuestions = new ArrayList<MeddraQuestion>();
    private Boolean participantAdded = false;


    @Transient
    public String getFullName() {
        return getMeddraCode() + " - " + getMeddraTerm();
    }


    @OneToMany(mappedBy = "lowLevelTerm")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    public List<MeddraQuestion> getMeddraQuestions() {
        return meddraQuestions;
    }

    public void setMeddraQuestions(List<MeddraQuestion> meddraQuestions) {
        this.meddraQuestions = meddraQuestions;
    }

    @Column(name = "participant_added")
    public Boolean isParticipantAdded() {
        if (participantAdded == null) {
            return false;
        }
        return participantAdded;
    }

    public void setParticipantAdded(Boolean participantAdded) {
        this.participantAdded = participantAdded;
    }

}