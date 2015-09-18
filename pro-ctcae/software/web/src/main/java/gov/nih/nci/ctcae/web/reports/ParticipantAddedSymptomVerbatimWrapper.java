package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

/**
 * @author Amey
 * ParticipantAddedSymptomVerbatimWrapper class.
 * Wrapper class for export link on Participant added symptom report
 *
 */
public class ParticipantAddedSymptomVerbatimWrapper {
	String studyParticipantIdentifier;
	String verbatim;
	LowLevelTerm lowLevelTerm;
	ProCtcTerm proCtcTerm;
	
	public LowLevelTerm getLowLevelTerm() {
		return lowLevelTerm;
	}
	
	public ProCtcTerm getProCtcTerm() {
		return proCtcTerm;
	}
	
	public String getStudyParticipantIdentifier() {
		return studyParticipantIdentifier;
	}
	
	public String getVerbatim() {
		return verbatim;
	}
	
	public void setLowLevelTerm(LowLevelTerm lowLevelTerm) {
		this.lowLevelTerm = lowLevelTerm;
	}
	
	public void setProCtcTerm(ProCtcTerm proCtcTerm) {
		this.proCtcTerm = proCtcTerm;
	}
	
	public void setStudyParticipantIdentifier(String studyParticipantIdentifier) {
		this.studyParticipantIdentifier = studyParticipantIdentifier;
	}
	
	public void setVerbatim(String verbatim) {
		this.verbatim = verbatim;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		
		if(o == null || getClass() != o.getClass() ) return false;
		
		ParticipantAddedSymptomVerbatimWrapper verbatimWrapper = (ParticipantAddedSymptomVerbatimWrapper) o;
		
		if(studyParticipantIdentifier != null? !studyParticipantIdentifier.equals(verbatimWrapper.getStudyParticipantIdentifier()) :
			verbatimWrapper.getStudyParticipantIdentifier() != null) return false;
		
		if(verbatim != null? !verbatim.equals(verbatimWrapper.getVerbatim()) : 
			verbatimWrapper.getVerbatim() != null) return false;
		
		if(lowLevelTerm != null? !lowLevelTerm.equals(verbatimWrapper.getLowLevelTerm()) : 
			verbatimWrapper.getLowLevelTerm() != null) return false;
		
		if(proCtcTerm != null? !proCtcTerm.equals(verbatimWrapper.getProCtcTerm()) :
			verbatimWrapper.getProCtcTerm() != null) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = (studyParticipantIdentifier != null? studyParticipantIdentifier.hashCode() : 0); 
		
		result = 31 * result + (verbatim != null? verbatim.hashCode(): 0);
		result = 31 * result + (lowLevelTerm != null? lowLevelTerm.hashCode(): 0);
		result = 31 * result + (proCtcTerm != null? proCtcTerm.hashCode(): 0);
		
		return result;
	}
}
