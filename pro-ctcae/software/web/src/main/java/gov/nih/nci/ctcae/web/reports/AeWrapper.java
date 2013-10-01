package gov.nih.nci.ctcae.web.reports;

import java.io.Serializable;


/**AeWrapper class
 * @author AmeyS
 * This class is used as valueObject in the process for generating ACCRU AE report
 */
public class AeWrapper implements Serializable{
	private String id;
	private String ctcaeTerm;
	private String meddraCode;
	private boolean lowLevelTerm = false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCtcaeTerm() {
		return ctcaeTerm;
	}
	public void setCtcaeTerm(String symptom) {
		this.ctcaeTerm = symptom;
	}
	public String getMeddraCode() {
		return meddraCode;
	}
	public void setMeddraCode(String meddraCode) {
		this.meddraCode = meddraCode;
	}
	
	public boolean isLowLevelTerm() {
		return lowLevelTerm;
	}
	public void setLowLevelTerm(boolean isLowLevelTerm) {
		this.lowLevelTerm = isLowLevelTerm;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof AeWrapper)) return false;
		
		AeWrapper that = (AeWrapper) o;
		if(id != null ? !id.equals(that.id) : that.id != null) return false;
		if(ctcaeTerm != null ? !ctcaeTerm.equals(that.ctcaeTerm) : that.ctcaeTerm != null) return false;
		if(meddraCode != null ? !meddraCode.equals(that.meddraCode) : that.meddraCode != null) return false;
		// On ACCRU report two ProctcTerms mapping to one ctcaeTerm should be shown as a single unique combined entry
		//if(proCtcTerm != null ? !proCtcTerm.equals(that.proCtcTerm) : that.proCtcTerm != null) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		 int result = (id != null ? id.hashCode() : 0);
	        result = 31 * result + (ctcaeTerm != null ? ctcaeTerm.hashCode() : 0);
	        result = 31 * result + (meddraCode != null ? meddraCode.hashCode() : 0);
	        // On ACCRU report two ProctcTerms mapping to one ctcaeTerm should be shown as a single unique combined entry
	        //result = 31 * result + (proCtcTerm != null ? proCtcTerm.hashCode() : 0);
	        return result;
	}
}
