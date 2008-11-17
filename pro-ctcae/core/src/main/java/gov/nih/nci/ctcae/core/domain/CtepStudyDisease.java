package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author Mehul Gulati
 * Date: Nov 12, 2008
 */
@Entity
@DiscriminatorValue("ctep")
public class CtepStudyDisease extends AbstractStudyDisease<DiseaseTerm> {

    @Column(name = "lead_disease", nullable = true)    
    private Boolean leadDisease;

    public Boolean getLeadDisease() {
        return leadDisease;
    }

    public void setLeadDisease(Boolean leadDisease) {
        this.leadDisease = leadDisease;
    }

    public DiseaseTerm getTerm() {
        return super.getTerm();
    }

    @Transient
    public DiseaseTerm getDiseaseTerm() {
        return super.getTerm();
    }

    @Transient
    public void setDiseaseTerm(DiseaseTerm diseaseTerm) {
        super.setTerm(diseaseTerm);
    }

    @Transient
    @Override
    public String getTermName() {
        return getTerm().getFullName();
    }

    }

