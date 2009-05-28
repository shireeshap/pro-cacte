package gov.nih.nci.ctcae.core.domain.meddra;


import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "meddra_llt")
public class LowLevelTerm extends AbstractMeddraDomainObject {

    private PreferredTerm preferredTerm;



    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "meddra_pt_id")
    @Cascade(value = { CascadeType.LOCK })
    public PreferredTerm getPreferredTerm(){
    	return preferredTerm;
    }

    public void setPreferredTerm(PreferredTerm preferredTerm){
    	this.preferredTerm = preferredTerm;
    }

    @Transient
    public String getFullName() {
        return getMeddraCode() + " - " + getMeddraTerm();
    }


}