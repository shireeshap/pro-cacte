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

/**
 * The Class ProCtcTerm.
 *
 * @author VinayG
 */

@Entity
@Table(name = "PROCTCAE_GRADE_MAPPING")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_proctcae_grade_mapping_id")})
public class ProctcaeGradeMapping extends BasePersistable {

	public static final String ZERO = "0";
	public static final String ONE = "1";
	public static final String TWO = "2";
	public static final String THREE = "3";
	public static final String FOUR = "4";
	public static final String PRESENT_CLINICIAN_ASSESS = "Present, Clinician Assess";

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "frequency", nullable = true)
    private Integer frequency;
    
    @Column(name = "severity", nullable = true)
    private Integer severity;
    
    @Column(name = "interference", nullable = true)
    private Integer interference;
    
    @Column(name = "amount", nullable = true)
    private Integer amount;
    
    @Column(name = "present_absent", nullable = true)
    private Integer present_absent;
    
    @Column(name = "pro_ctc_grade", nullable = false)
    private String proCtcGrade;
    
    @Column(name="proctcae_verbatim", nullable = false)
    private String proctcaeVerbatim;

	@JoinColumn(name = "proctcae_grade_mapping_version_id", referencedColumnName = "id")
    @ManyToOne
    private ProctcaeGradeMappingVersion proctcaeGradeMappingVersion;
    
    @JoinColumn(name = "pro_ctc_terms_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcTerm proCtcTerm;
    
    public ProctcaeGradeMapping(){
    	
    }
    
	public ProctcaeGradeMapping(Integer amountOrPresentAbsent, boolean isAmount, String proCtcGrade, 
			String proctcaeVerbatim, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion,
			ProCtcTerm proCtcTerm) {
		super();
		if(isAmount){
			this.amount = amountOrPresentAbsent;
		} else {
			this.present_absent = amountOrPresentAbsent;
		}
		this.proCtcGrade = proCtcGrade;
		this.proctcaeVerbatim = proctcaeVerbatim;
		this.proctcaeGradeMappingVersion = proctcaeGradeMappingVersion;
		this.proCtcTerm = proCtcTerm;
	}

	public ProctcaeGradeMapping(Integer frequency, Integer severity, Integer interference, 
			String proCtcGrade, String proctcaeVerbatim, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion,
			ProCtcTerm proCtcTerm) {
		super();
		this.frequency = frequency;
		this.severity = severity;
		this.interference = interference;
		this.proCtcGrade = proCtcGrade;
		this.proctcaeVerbatim = proctcaeVerbatim;
		this.proctcaeGradeMappingVersion = proctcaeGradeMappingVersion;
		this.proCtcTerm = proCtcTerm;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Integer getSeverity() {
		return severity;
	}

	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

	public Integer getInterference() {
		return interference;
	}

	public void setInterference(Integer interference) {
		this.interference = interference;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getPresent_absent() {
		return present_absent;
	}

	public void setPresent_absent(Integer present_absent) {
		this.present_absent = present_absent;
	}

	public String getProCtcGrade() {
		return proCtcGrade;
	}

	public void setProCtcGrade(String proCtcGrade) {
		this.proCtcGrade = proCtcGrade;
	}
	
	public String getProctcaeVerbatim() {
		return proctcaeVerbatim;
	}

	public void setProctcaeVerbatim(String proctcaeVerbatim) {
		this.proctcaeVerbatim = proctcaeVerbatim;
	}

	public ProctcaeGradeMappingVersion getProCtcGradeMappingVersion() {
		return proctcaeGradeMappingVersion;
	}

	public void setProCtcGradeMappingVersion(
			ProctcaeGradeMappingVersion proctcaeGradeMappingVersion) {
		this.proctcaeGradeMappingVersion = proctcaeGradeMappingVersion;
	}

	public ProCtcTerm getProCtcTerm() {
		return proCtcTerm;
	}

	public void setProCtcTerm(ProCtcTerm proCtcTerm) {
		this.proCtcTerm = proCtcTerm;
	}
    

    @Override
    public String toString() {
        return frequency + " - " + severity + " - " +  interference + " - " +  amount + " - " +  present_absent + ":" + proCtcGrade;
    }

    /**
     * equals and hashcode do not take grade into acount. This means that 2 pgMapping objects with same configuration and 
     * different grades are considered equal. This is because the following attributes form a composite unique key.
     * 
     * proCtcTerm, version, freq, sev, int, amt, pres_absent.
     * 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProctcaeGradeMapping)) return false;

        ProctcaeGradeMapping that = (ProctcaeGradeMapping) o;

        if (!proCtcTerm.equals(that.proCtcTerm)) return false;
        if (!proctcaeGradeMappingVersion.equals(that.proctcaeGradeMappingVersion)) return false;
        
        if (frequency != null ? !frequency.equals(that.frequency) : that.frequency != null) return false;
        if (severity != null ? !severity.equals(that.severity) : that.severity != null) return false;
        if (interference != null ? !interference.equals(that.interference) : that.interference != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (present_absent != null ? !present_absent.equals(that.present_absent) : that.present_absent != null) return false;
        
        //if (!proCtcGrade.equals(that.proCtcGrade)) return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        int result;
        result = proCtcTerm.hashCode();

        result = 31 * result + (proctcaeGradeMappingVersion != null ? proctcaeGradeMappingVersion.hashCode() : 0);
        result = 31 * result + (frequency != null ? frequency.hashCode() : 0);
        result = 31 * result + (severity != null ? severity.hashCode() : 0);
        result = 31 * result + (interference != null ? interference.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (present_absent != null ? present_absent.hashCode() : 0);

        //result = 31 * result + proCtcGrade.hashCode();
        return result;
    }
    

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}
