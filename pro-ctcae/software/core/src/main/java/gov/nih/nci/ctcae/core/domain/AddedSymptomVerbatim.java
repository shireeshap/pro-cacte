package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

@Entity
@Table(name="added_symptoms_verbatim_mapping")
@GenericGenerator(name="id-generator", strategy="native", 
				  parameters={@Parameter(name="sequence", value="seq_ added_symptoms_verbatim_mapping_id")})
public class AddedSymptomVerbatim extends BaseVersionable implements Comparable<AddedSymptomVerbatim>{
	
	@Id
	@GeneratedValue(generator="id-generator")
	@Column(name="id")
	private Integer id;
	
	@JoinColumn(name="pro_ctc_term_id", referencedColumnName="id", nullable=true)
	@ManyToOne
	private ProCtcTerm proctcTerm;

	@JoinColumn(name="meddra_llt_id", referencedColumnName="id", nullable=true)
	@ManyToOne
	private LowLevelTerm lowLevelTerm;

	@JoinColumn(name="sp_crf_schedules_id", referencedColumnName="id", nullable=false)
	@ManyToOne
	private StudyParticipantCrfSchedule studyParticipantCrfSchedule;
	
	@Column(name="verbatim")
	private String verbatim;

	@Override
	public Integer getId() {
		return id;
	}
	
	public LowLevelTerm getLowLevelTerm() {
		return lowLevelTerm;
	}
	
	public ProCtcTerm getProctcTerm() {
		return proctcTerm;
	}
	
	public StudyParticipantCrfSchedule getStudyParticipantCrfSchedule() {
		return studyParticipantCrfSchedule;
	}

	public String getVerbatim() {
		return verbatim;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLowLevelTerm(LowLevelTerm lowLevelTerm) {
		this.lowLevelTerm = lowLevelTerm;
	}
	
	public void setProctcTerm(ProCtcTerm proctcTerm) {
		this.proctcTerm = proctcTerm;
	}
	
	public void setStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		this.studyParticipantCrfSchedule = studyParticipantCrfSchedule;
	}

	public void setVerbatim(String verbatim) {
		this.verbatim = verbatim;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		
		if(this == null || this.getClass() != o.getClass()) return false;
		
		AddedSymptomVerbatim that = (AddedSymptomVerbatim) o;
		
		if(this.proctcTerm != null?
				!this.proctcTerm.equals(that.getProctcTerm()): that.getProctcTerm() != null) return false;			
		
		if(this.lowLevelTerm != null? 
				!this.lowLevelTerm.equals(that.getLowLevelTerm()) : that.getLowLevelTerm() != null) return false;
		
		if(this.verbatim != null?
				!this.equals(that.getVerbatim()) : that.getVerbatim() != null) return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		
		result = 31 * result + (this.proctcTerm != null? this.proctcTerm.hashCode() : 0);
		result = 31 * result + (this.lowLevelTerm != null? this.lowLevelTerm.hashCode() : 0);
		result = 31 * result + (this.verbatim != null? this.verbatim.hashCode() : 0);
		
		return result;
	}
	
	@Override
	public int compareTo(AddedSymptomVerbatim o) {
		return this.verbatim.toLowerCase().compareTo(o.getVerbatim().toLowerCase());
	}
	
}