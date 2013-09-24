package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import gov.nih.nci.ctcae.core.domain.QueryStrings;

//

/**
 * The Class ClinicalStaffAjaxFacade.
 *
 * @author Mehul Gulati
 *         Date: Oct 22, 2008
 */
public class ClinicalStaffAjaxFacade {

    protected final Log logger = LogFactory.getLog(getClass());
    /**
     * The clinical staff repository.
     */
    private ClinicalStaffRepository clinicalStaffRepository;
    private OrganizationClinicalStaffRepository organizationClinicalStaffRepository;

    public List<OrganizationClinicalStaff> matchOrganizationClinicalStaffByStudyOrganizationId(final String text, Integer studyOrganizationId, String role) {

        logger.info(String.format("in match matchOrganizationClinicalStaffByOrganizationId method. Search string :%s and studyOrganizationId=%s", text, studyOrganizationId));
        List<OrganizationClinicalStaff> organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId(text, studyOrganizationId, Role.getByCode(role));
        organizationClinicalStaffs = RankBasedSorterUtils.sort(organizationClinicalStaffs, text, new Serializer<OrganizationClinicalStaff>() {
            public String serialize(OrganizationClinicalStaff object) {
                return object.getDisplayName();
            }
        });
        return ObjectTools.reduceAll(organizationClinicalStaffs, "id", "displayName");

    }

    public List<StudyOrganizationClinicalStaff> matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(final String text, Integer studyOrganizationId, final String roles) {

        List<Role> rolesList = new ArrayList<Role>();
        StringTokenizer st = new StringTokenizer(roles, "|");
        while (st.hasMoreTokens()) {
            String roleCode = st.nextToken();
            rolesList.add(Role.getByCode(roleCode));
        }
        logger.info(String.format("in match matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole method. Search string :%s and studyOrganizationId=%s and role=%s", text, studyOrganizationId, roles));
        List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = clinicalStaffRepository.findByStudyOrganizationIdAndRole(text, studyOrganizationId, rolesList);
        studyOrganizationClinicalStaffs = RankBasedSorterUtils.sort(studyOrganizationClinicalStaffs, text, new Serializer<StudyOrganizationClinicalStaff>() {
            public String serialize(StudyOrganizationClinicalStaff object) {
                return object.getDisplayName();
            }
        });
        return ObjectTools.reduceAll(studyOrganizationClinicalStaffs, "id", "displayName", "role");

    }

    /**
     * Search clinical staff.
     *
     * @param searchString
     * @return the string
     */
    public List<ClinicalStaff> searchClinicalStaff(String[] searchString, Integer startIndex, Integer results, String sort, String dir, Long totalRecords) {
        List<ClinicalStaff> clinicalStaffs = getClinicalStaffObjects(searchString, startIndex, results, sort, dir, true, totalRecords);
        return clinicalStaffs;
    }

    public List<ClinicalStaff> getClinicalStaffObjects(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir, boolean showInactive, Long totalRecords) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery(QueryStrings.STAFF_QUERY_SORTBY_FIELDS, true);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String userName = user.getUsername();
        int sIndex = startIndex;
        
        List<ClinicalStaff> clinicalStaffs = new ArrayList<ClinicalStaff>();
        Long searchCount = totalRecords;
        clinicalStaffQuery.setFirstResult(startIndex);
        clinicalStaffQuery.setMaximumResults(results);
        clinicalStaffQuery.setSortBy("cs." + sort);
        clinicalStaffQuery.setSortDirection(dir);
        if (user.isAdmin() || isLeadStaff()) {
            if (searchStrings != null) {
                clinicalStaffQuery.setLeftJoin();
                int index = 0;
                for (String searchString : searchStrings) {
                    clinicalStaffQuery.filterByAll(searchString, "" + index);
                    index++;
                }
            }
            clinicalStaffs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
            return clinicalStaffs;
        } else {
            
            ClinicalStaffQuery clinicalStaffQuery1 = new ClinicalStaffQuery(QueryStrings.STAFF_QUERY_SORTBY_FIELDS, false);
            clinicalStaffQuery1.filterByUserName(userName);
            List<ClinicalStaff> cs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery1);
            List<Integer> orgs = new ArrayList<Integer>();
            for (OrganizationClinicalStaff ocs : cs.get(0).getOrganizationClinicalStaffs()) {
                orgs.add(ocs.getOrganization().getId());
            }
            clinicalStaffQuery.filterByOrganization(orgs);
            if (searchStrings == null) {

                clinicalStaffs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
                if (clinicalStaffs.size() >= results || clinicalStaffs.size() >= searchCount) {
                    return clinicalStaffs;
                } else {
                    while (clinicalStaffs.size() < results && clinicalStaffs.size() < (searchCount-startIndex)) {
                    	sIndex = results + sIndex;
                        clinicalStaffQuery.setFirstResult(sIndex);
                        List<ClinicalStaff> l = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
                        for (ClinicalStaff clinicalStaff : l) {
                            clinicalStaffs.add(clinicalStaff);
                        }
                        l.clear();
                    }
                    return clinicalStaffs;
                }
            } else {
                clinicalStaffQuery.setLeftJoin();
                int i = 0;
                for (String searchString : searchStrings) {
                    clinicalStaffQuery.filterByAll(searchString, "" + i);
                    i++;
                }
                clinicalStaffs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
                return clinicalStaffs;
            }
        }
    }


    public Long resultCount(String[] searchTexts) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery(QueryStrings.STAFF_QUERY_COUNT, true);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String userName = user.getUsername();
        if (!user.isAdmin() && !isLeadStaff()) {
            ClinicalStaffQuery clinicalStaffQuery1 = new ClinicalStaffQuery(QueryStrings.STAFF_QUERY_SORTBY_FIELDS, false);
            clinicalStaffQuery1.filterByUserName(userName);
            List<ClinicalStaff> cs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery1);
            List<Integer> orgs = new ArrayList<Integer>();

            for (OrganizationClinicalStaff ocs : cs.get(0).getOrganizationClinicalStaffs()) {
                orgs.add(ocs.getOrganization().getId());
            }

            clinicalStaffQuery.filterByOrganization(orgs);

        } 
        if (searchTexts != null) {
            clinicalStaffQuery.setLeftJoin();
            int index = 0;
            for (String searchText : searchTexts) {
                if (!StringUtils.isBlank(searchText)) {
                    clinicalStaffQuery.filterByAll(searchText, "" + index);
                    index++;
                }
            }
        }
        return clinicalStaffRepository.findWithCount(clinicalStaffQuery);

    }
    
    private boolean isLeadStaff(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.LEAD_CRA) || userRole.getRole().equals(Role.PI) || userRole.getRole().equals(Role.CCA)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Sets the clinical staff repository.
     *
     * @param clinicalStaffRepository the new clinical staff repository
     */
    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setOrganizationClinicalStaffRepository(OrganizationClinicalStaffRepository organizationClinicalStaffRepository) {
        this.organizationClinicalStaffRepository = organizationClinicalStaffRepository;
    }
}