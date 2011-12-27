package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

//
/**
 * The Class StudyAjaxFacade.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
public class StudyAjaxFacade {

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;

    /**
     * The participant repository.
     */

    /**
     * Match study.
     *
     * @param text the text
     * @return the list< study>
     */
    public List<Study> matchStudy(final String text) {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesWithMatchingText(text);
        List<Study> studies = new ArrayList<Study>(studyRepository.find(studyQuery));
        studies = RankBasedSorterUtils.sort(studies, text, new Serializer<Study>() {
            public String serialize(Study object) {
                return object.getDisplayName();
            }
        });
        return ObjectTools.reduceAll(studies, "id", "shortTitle", "assignedIdentifier");

    }

    public List<Study> searchStudies(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir) {
        List<Study> studies = getObjects(searchStrings, startIndex, results, sort, dir);
        return studies;
    }

    private List<Study> getObjects(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir) {
        StudyQuery studyQuery = new StudyQuery(true, true);
        
        studyQuery.setFirstResult(startIndex);
        studyQuery.setMaximumResults(results);
        studyQuery.setSortBy("study." + sort);
        studyQuery.setSortDirection(dir);
        
        if (searchStrings != null) {
        	studyQuery.setLeftJoin();
             int index = 0;
             for(String searchText: searchStrings){
                 if (!StringUtils.isBlank(searchText)) {
                	 studyQuery.filterByAll(searchText,""+index);
                     index++;
                 }
             }
        }
        
        return (List<Study>) studyRepository.find(studyQuery);
    }
    

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public Long resultCount(String[] searchTexts) {
    	StudyQuery studyQuery = new StudyQuery(true);
        if (searchTexts != null) {
        	studyQuery.setLeftJoin();
             int index = 0;
             for(String searchText: searchTexts){
                 if (!StringUtils.isBlank(searchText)) {
                	 studyQuery.filterByAll(searchText,""+index);
                     index++;
                 }
             }
        }
        return studyRepository.findWithCount(studyQuery);
    }

}
