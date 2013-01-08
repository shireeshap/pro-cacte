package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import gov.nih.nci.ctcae.core.domain.Persistable;
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

    public List<Study> searchStudies(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir, Integer totalRecords) {
    	List<Study> studies = null;
    	
    	// Fetch the records only if totalRecords are determined to be greater than zero
    	if(totalRecords != 0){
    	studies = getObjects(searchStrings, startIndex, results, sort, dir, totalRecords);
    	}
        return studies;
    }

    private List<Study> getObjects(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir,  Integer totalRecords) {
       
    	 StudyQuery studyQuery=null;
        
        
        if(sort.compareToIgnoreCase("fundingSponsorDisplayName")==0){
       	 	 studyQuery = new StudyQuery(QueryStrings.STUDY_QUERY_SORTBY_FSP_DCC,true);
        	 studyQuery.setFirstResult(startIndex);
             studyQuery.setMaximumResults(results);
             studyQuery.filterByFundingSponsor();
             studyQuery.setSortBy("so.organization.name" );
             studyQuery.setSortDirection(dir);
             
        }else if(sort.compareToIgnoreCase("coordinatingCenterDisplayName")==0){
        	 studyQuery = new StudyQuery(QueryStrings.STUDY_QUERY_SORTBY_FSP_DCC,true);
 		     studyQuery.setFirstResult(startIndex);
	         studyQuery.setMaximumResults(results);
	         studyQuery.filterByCoordinatingCenter();
             studyQuery.setSortBy("so.organization.name" );
             studyQuery.setSortDirection(dir);
        	 
    	}else{
    		 studyQuery = new StudyQuery(QueryStrings.STUDY_QUERY_SORTBY_FIELDS, true);
  		     studyQuery.setFirstResult(startIndex);
	         studyQuery.setMaximumResults(results);
	         studyQuery.setSortBy("study." + sort);
	         studyQuery.setSortDirection(dir); 
	         }
    
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (searchStrings != null) {
            int index = 0;
            for (String searchText : searchStrings) {
                if (!StringUtils.isBlank(searchText)) {
                    studyQuery.filterByAll(searchText, "" + index);
                    index++;
                }
            }
        }
        List<Study> studies = (List<Study>) studyRepository.find(studyQuery);
        if (!user.isAdmin() && !user.isCCA()) {
            if (studies.size() == results) {
                return studies;
            } else {
                int index = startIndex;
                while (studies.size() < results && studies.size() < totalRecords-startIndex) {
                    index = startIndex + results;
                    studyQuery.setFirstResult(index);
                    List<Study> l = (List<Study>) studyRepository.find(studyQuery);
                    for (Study study : l) {
                        studies.add(study);
                    }
                    l.clear();
                }
                return studies;
            }
        }
        
        
        return studies;
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
    	
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	List<Integer> objectIds = user.findAccessibleObjectIds(Study.class);
    	/* If user privileges are such that he has an empty list of AccessibleObjectIds associated with him, then return 0 as resultCount
    	 * else get the actual resutCount from the database. 
    	*/
    	boolean groupPrivilege = user.checkGroupPrivilege(Study.class);
    	if((objectIds != null && objectIds.size() > 0) || groupPrivilege){
    		StudyQuery studyQuery = new StudyQuery(QueryStrings.STUDY_QUERY_COUNT,true);
	          if (searchTexts != null) {
	                int index = 0;
	                for (String searchText : searchTexts) {
	                    if (!StringUtils.isBlank(searchText)) {
	                        studyQuery.filterByAll(searchText, "" + index);
	                        index++;
	                    }
	                }
	            }
	        return studyRepository.findWithCount(studyQuery);
    	}
    	
    		return (long) 0;
    }    	
    
}
