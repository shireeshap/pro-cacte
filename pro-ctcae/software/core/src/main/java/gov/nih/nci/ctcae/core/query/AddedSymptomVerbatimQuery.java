package gov.nih.nci.ctcae.core.query;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.QueryStrings;

/**
 * @author Amey
 * AddedSymptomVerbatimQuery class
 * Queries AddedSymptomVerbatim domain objects
 */
public class AddedSymptomVerbatimQuery extends AbstractQuery {

	public AddedSymptomVerbatimQuery(QueryStrings query) {
		super(query);
	}

	 public void filterByCrfs(List<Integer> crfIds) {
	        andWhere("addedVerbatim.studyParticipantCrfSchedule.studyParticipantCrf.crf.id in (:crfs)");
	        setParameterList("crfs", crfIds);
	    }

	 public void filterByStudyId(Integer id){
   	 if (id != null) {
            andWhere("addedVerbatim.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.studySite.study.id=:studyId");
            setParameter("studyId", id);
        }
   }

    public void filterByStudySite(Integer id) {
        andWhere("addedVerbatim.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.studySite.id=:studySiteId");
        setParameter("studySiteId", id);
    }

}