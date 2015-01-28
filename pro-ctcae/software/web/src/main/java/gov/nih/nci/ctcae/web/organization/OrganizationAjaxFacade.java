package gov.nih.nci.ctcae.web.organization;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//
/**
 * The Class OrganizationAjaxFacade.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class OrganizationAjaxFacade {
	private final String ALL_STUDY_SITES="GetAllStudySites"; 
	private String PRIVILEGE_CREATE_CLINICAL_STAFF = "PRIVILEGE_CREATE_CLINICAL_STAFF";
	private String BLANK = "";
	private final static String ORGANIZATION_NAME_SORT = "organizationName";
	private final static String STUDY_SORT = "study";

    /**
     * The organization repository.
     */
    private OrganizationRepository organizationRepository;
    private StudyOrganizationRepository studyOrganizationRepository;
    private GenericRepository genericRepository;
    private UserRepository userRepository;
    private AuthorizationServiceImpl authorizationServiceImpl;
    /**
     * The log.
     */
    protected final Log logger = LogFactory.getLog(getClass());


    /**
     * Match organization.
     *
     * @param text the text
     * @return the list< organization>
     */
    public List<Organization> matchOrganization(final String text) {
        logger.info("in match organization method. Search string :" + text);
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationNameOrNciInstituteCode(text);
        //organizationQuery.setMaximumResults(25);
        List<Organization> organizations = (List<Organization>) organizationRepository.find(organizationQuery);
        organizations = RankBasedSorterUtils.sort(organizations, text, new Serializer<Organization>() {
            public String serialize(Organization object) {
                return object.toString();
            }
        });
        return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");

    }

    public List<Organization> matchOrganizationForStudySites(final String text, String value) {
        logger.info("in match organization method. Search string :" + text);
        OrganizationQuery organizationQuery;
        if(!value.equalsIgnoreCase(ALL_STUDY_SITES)){
        	organizationQuery = new OrganizationQuery(QueryStrings.ORGANIZATION_QUERY_FILTER_STUDYSITES, false);
        	//Filter the list of studySites already assigned to a study and not populate them in Autocompleter list.
        	organizationQuery.whereToFilterDuplicateSites(value);
        }else
        	organizationQuery = new OrganizationQuery(QueryStrings.ORGANIZATION_QUERY_BASIC,false);
        
        organizationQuery.filterByOrganizationNameOrNciInstituteCode(text);
        //organizationQuery.setMaximumResults(25);
        List<Organization> organizations = genericRepository.find(organizationQuery);
        organizations = RankBasedSorterUtils.sort(organizations, text, new Serializer<Organization>() {
            public String serialize(Organization object) {
                return object.toString();
            }
        });
        return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");

    }
    
    public List<Organization> matchOrganizationForLeadSites(final String text, String value) {
        logger.info("in match organization method. Search string :" + text);
        OrganizationQuery organizationQuery = new OrganizationQuery(QueryStrings.ORGANIZATION_QUERY_BASIC,false);
        if(value !=null && !BLANK.equals(value)){
        	organizationQuery.filterStudySiteIfParticipantPresent(value);
        }
        organizationQuery.filterByOrganizationNameOrNciInstituteCode(text);

        List<Organization> organizations = genericRepository.find(organizationQuery);
        organizations = RankBasedSorterUtils.sort(organizations, text, new Serializer<Organization>() {
            public String serialize(Organization object) {
                return object.toString();
            }
        });
        return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");

    }

    public List<Organization> matchOrganizationForStudySitesWithSecurity(final String text) {
        List<Organization> organizations;
        boolean hasRoleNotOnTheList = false;
        boolean hasAllowedRole = false;
        Set<Organization> siteToRemove = new HashSet<Organization>();
        List<Role> roles = new ArrayList<Role>();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isAdmin()) {
            return matchOrganization(text);
        } else {
            ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
            if (clinicalStaff != null) {
            	roles = authorizationServiceImpl.findRolesForPrivilege(user, PRIVILEGE_CREATE_CLINICAL_STAFF);
                Set<Organization> orgSet = new HashSet<Organization>();
                for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {
                	hasRoleNotOnTheList = false;
                	hasAllowedRole = false;
                    Organization organization = organizationClinicalStaff.getOrganization();
                    if ("%".equals(text) || organization.getDisplayName().toLowerCase().indexOf(text.toLowerCase()) > 1) {
                        orgSet.add(organizationClinicalStaff.getOrganization());
                    }
                    for (StudyOrganizationClinicalStaff socs : organizationClinicalStaff.getStudyOrganizationClinicalStaff()) {
                        if ((socs.getRole().equals(Role.LEAD_CRA) || socs.getRole().equals(Role.PI)) &&
                                socs.getRoleStatus().equals(RoleStatus.ACTIVE) && socs.getStatusDate().before(new Date())) {
                            for (StudyOrganization studyOrganization : socs.getStudyOrganization().getStudy().getStudyOrganizations()) {
                                organization = studyOrganization.getOrganization();
                                if ("%".equals(text) || organization.getDisplayName().toLowerCase().indexOf(text.toLowerCase()) > 1) {
                                    orgSet.add(studyOrganization.getOrganization());
                                }
                            }
                        }
                        
                        if(!roles.contains(socs.getRole())){
                        	hasRoleNotOnTheList = true;
                        } else {
                        	hasAllowedRole = true;
                        }
                    }
                    
                    if(hasRoleNotOnTheList && !hasAllowedRole){
                    	siteToRemove.add(organizationClinicalStaff.getOrganization());
                    }
                }
                
                for(Organization organization : siteToRemove){
                	orgSet.remove(organization);
                }
                organizations = new ArrayList(orgSet);
                return ObjectTools.reduceAll(organizations, "id", "name", "nciInstituteCode");
            } else {
                return matchOrganizationForStudySites(text,ALL_STUDY_SITES);
            }

        }
    }
    
    public List<StudyOrganization> matchOrganizationByStudyId(final String text, Integer studyId) {
        List<StudyOrganization> studyOrganizations = studyOrganizationRepository.findByStudyId(text, studyId);
        studyOrganizations = RankBasedSorterUtils.sort(studyOrganizations, text, new Serializer<StudyOrganization>() {
            public String serialize(StudyOrganization object) {
                return object.toString();
            }
        });
        return ObjectTools.reduceAll(studyOrganizations, "id", "organization.name", "organization.nciInstituteCode");

    }
    
    /**Match organization by studyId and UserRole
     * @param text
     * @param studyId
     * Applies instanceLevelSecurity for the studySite autoCompleter in studyReports section.
     * If the logged in user has site level role on the selected study (selected from study drop down), then do not show all the studySites but 
     * only the loggedin user's studySite.
     * For other non site level roles (like admin, lead_pi, lead_cra..) show all the studySites associated with the selected study.
     */
    public List<StudyOrganization> matchOrganizationByStudyIdAndUserRole(final String text, Integer studyId) {
    	List<StudyOrganization> studyOrganizations = new ArrayList<StudyOrganization>();
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	if(AuthorizationServiceImpl.isInstanceLevelSecurityRequired(user)){
    		Study study = (Study) genericRepository.findById(Study.class, studyId);
        	if(study != null){
        		List<StudyOrganizationClinicalStaff> socsList = study.getStudySiteLevelStudyOrganizationClinicalStaffs();
            	for(StudyOrganizationClinicalStaff socs : socsList){
            		User socsUser = socs.getOrganizationClinicalStaff().getClinicalStaff().getUser();
            		if( socsUser!= null){
            			if(socsUser.equals(user)){
            				studyOrganizations.add(socs.getStudyOrganization());
            				break;
            			}
            		}
            	}
        	}
    	}
    
    	if(studyOrganizations.isEmpty()){
    		studyOrganizations = studyOrganizationRepository.findByStudyId(text, studyId);
    	}
    	
        studyOrganizations = RankBasedSorterUtils.sort(studyOrganizations, text, new Serializer<StudyOrganization>() {
            public String serialize(StudyOrganization object) {
                return object.toString();
            }
        });
        return ObjectTools.reduceAll(studyOrganizations, "id", "organization.name", "organization.nciInstituteCode");

    }
    
    /**
     * Search clinical staff.
     *
     * @param searchString
     * @return the string
     */
    public List<Organization> searchOrganizations(String[] searchString, Integer startIndex, Integer results, String sort, String dir) {
        List<Organization> organizations = getOrganizationObjects(searchString, startIndex, results, sort, dir, true);
        return organizations;
    }

    public List<Organization> getOrganizationObjects(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir, boolean showInactive) {
    	OrganizationQuery organizationQuery  = new OrganizationQuery(QueryStrings.ORGANIZATION_QUERY_SORTBY_FIELDS, true);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String userName = user.getUsername();
        int sIndex = startIndex;
        
        List<Organization> organizations = new ArrayList<Organization>();
        organizationQuery.setFirstResult(startIndex);
        organizationQuery.setMaximumResults(results);
        
        //TBD: Implement sort on right database attribute when users clicks on dataTable column on-screen
        organizationQuery.setSortBy("o." + sort);
        organizationQuery.setSortDirection(dir);
        
        if (user.isAdmin() || user.isCCA()) {
            if (searchStrings != null) {
                organizationQuery.leftJoinStudy();
                int index = 0;
                for (String searchString : searchStrings) {
                	organizationQuery.filterByAll(searchString, "" + index);
                    index++;
                }
            }
            organizations = (List<Organization>) organizationRepository.find(organizationQuery);
        }
		return organizations;
    }
    
    public Long resultCount(String[] searchTexts) {
    	Long resultCount = (long) 0;
    	OrganizationQuery organizationQuery = new OrganizationQuery(QueryStrings.ORGANIZATION_QUERY_COUNT, true);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = user.getUsername();
        
        if (user.isAdmin() || user.isCCA()) {
            if (searchTexts != null) {
                organizationQuery.leftJoinStudy();
                int index = 0;
                for (String searchString : searchTexts) {
                	organizationQuery.filterByAll(searchString, "" + index);
                    index++;
                }
            }
            resultCount = organizationRepository.findWithCount(organizationQuery);
        }
        return resultCount;
    }
    
    


    /**
     * Sets the organization repository.
     *
     * @param organizationRepository the new organization repository
     */
    @Required
    public void setOrganizationRepository(final OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setStudyOrganizationRepository(final StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Required
    public void setAuthorizationServiceImpl(AuthorizationServiceImpl authorizationServiceImpl) {
        this.authorizationServiceImpl = authorizationServiceImpl;
    }
}
