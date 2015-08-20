package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;

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
    private AuthorizationServiceImpl authorizationServiceImpl;

    private Log logger = LogFactory.getLog(StudyAjaxFacade.class);
    /**
     * The participant repository.
     */

    /**Match study method.
     * @param text
     * @param privilege: Used for instanceLevelSecurity. The passed in privilegeName is first used to find all the possible roles which can own this privilege.
     * This list of roles is then used to filter the studies (based on, whether the logged in user has one of these roles on the study.) 
     */
    public List<Study> matchStudy(final String text, String privilege) {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesWithMatchingText(text);
        List<Study> studies = new ArrayList<Study>(studyRepository.find(studyQuery));
        
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Study> studiesWithRequiredRole = null;
        if(!StringUtils.isEmpty(privilege) && AuthorizationServiceImpl.isInstanceLevelSecurityRequired(user)){
        	 studiesWithRequiredRole = new HashSet<Study>();

             List<Role> roles = authorizationServiceImpl.findRolesForPrivilege(user, privilege);
             for(Study study : studies){
             	if(authorizationServiceImpl.hasRole(study, roles, user)){
             		studiesWithRequiredRole.add(study);
             	}
             }
             
             studies = new ArrayList<Study>(studiesWithRequiredRole);
        }
       
        
        studies = RankBasedSorterUtils.sort(studies, text, new Serializer<Study>() {
            public String serialize(Study object) {
                return object.getDisplayName();
            }
        });

        return ObjectTools.reduceAll(studies, "id", "shortTitle", "assignedIdentifier");
    }
    
    public List<Study> searchStudies(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir, Integer totalRecords) {

        // Fetch the records only if totalRecords are determined to be greater than zero
        if (totalRecords != 0) {
            return getObjects(searchStrings, startIndex, results, sort, dir, totalRecords);
        }
        return null;
    }

    private StudyQuery buildCountQuery(String sort) {
        return buildStudyQuery(null, null, sort, null, Boolean.FALSE);
    }

    private StudyQuery buildStudyQuery(Integer startIndex, Integer results, String sort, String dir,  Boolean searchQuery) {

        StudyQuery studyQuery;
        if(StringUtils.equalsIgnoreCase(sort, "fundingSponsorDisplayName")) {
            studyQuery = searchQuery ? new StudyQuery(QueryStrings.STUDY_QUERY_SORTBY_FSP_DCC,true) : new StudyQuery(QueryStrings.STUDY_QUERY_COUNT,true);
            studyQuery.filterByFundingSponsor();
            studyQuery.setLeftJoin();
            if(searchQuery){
                studyQuery.setSortBy("sso.organization.name");
            }
        } else if(StringUtils.equalsIgnoreCase(sort, "coordinatingCenterDisplayName")) {
            studyQuery = searchQuery ? new StudyQuery(QueryStrings.STUDY_QUERY_SORTBY_FSP_DCC,true) : new StudyQuery(QueryStrings.STUDY_QUERY_COUNT,true);
            studyQuery.filterByCoordinatingCenter();
            studyQuery.setLeftJoin();
            if(searchQuery) {
                studyQuery.setSortBy("sso.organization.name");
            }
        } else {
            studyQuery = searchQuery ? new StudyQuery(QueryStrings.STUDY_QUERY_SORTBY_FIELDS,true) : new StudyQuery(QueryStrings.STUDY_QUERY_COUNT,true);
            if(searchQuery) {
                studyQuery.setSortBy("study." + sort);
            }
        }

        if (searchQuery) {
            studyQuery.setFirstResult(startIndex);
            studyQuery.setMaximumResults(results);
            studyQuery.setSortDirection(dir);
        }

        return studyQuery;
    }

    private List<Study> getObjects(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir, Integer totalRecords ) {
//        StudyQuery studyQuery = buildStudyQuery(startIndex, results, sort, dir, Boolean.TRUE);
//
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (searchStrings != null) {
//            int index = 0;
//            for (String searchText : searchStrings) {
//                if (!StringUtils.isBlank(searchText)) {
//                    studyQuery.filterByAll(searchText, "" + index);
//                    index++;
//                }
//            }
//        }


//        List<Study> studies = (List<Study>) studyRepository.find(studyQuery);
        return studyRepository.x(searchStrings[0], sort, "'FSP'");
//        if (!user.isAdmin() && !user.isCCA()) {x/
//            if (studies.size() == results) {
//                return studies;
//            } else {
//                int index;
//                while (studies.size() < results && studies.size() < totalRecords-startIndex) {
//                    index = startIndex + results;
//                    studyQuery.setFirstResult(index);
//                    studies.addAll( studyRepository.find(studyQuery));
//                }
//                return studies;
//            }
//        }
        
        
//        return studies;
    }

    public Long resultCount(String[] searchTexts, String sort) {
    	
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	List<Integer> objectIds = user.findAccessibleObjectIds(Study.class);
    	/* If user privileges are such that he has an empty list of AccessibleObjectIds associated with him, then return 0 as resultCount
    	 * else get the actual resutCount from the database. 
    	*/
        StudyQuery studyQuery = buildCountQuery(sort);
    	boolean groupPrivilege = user.checkGroupPrivilege(Study.class);
    	if((objectIds != null && objectIds.size() > 0) || groupPrivilege){
	          if (searchTexts != null) {
	                int index = 0;
	                for (String searchText : searchTexts) {
	                    if (!StringUtils.isBlank(searchText)) {
                            studyQuery.filterByAll(StringUtils.trimToNull(searchText), "" + index);
	                        index++;
	                    }
	                }
	            }
	        return studyRepository.findWithCount(studyQuery);
    	}
    	
    		return 0L;
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
    
    @Required
    public void setAuthorizationServiceImpl(AuthorizationServiceImpl authorizationServiceImpl) {
    	this.authorizationServiceImpl = authorizationServiceImpl;
    }
    
}
