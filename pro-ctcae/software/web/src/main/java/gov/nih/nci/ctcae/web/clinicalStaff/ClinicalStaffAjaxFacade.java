package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

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

    public List<OrganizationClinicalStaff> matchOrganizationClinicalStaffByStudyOrganizationId(final String text, Integer studyOrganizationId) {

        logger.info(String.format("in match matchOrganizationClinicalStaffByOrganizationId method. Search string :%s and studyOrganizationId=%s", text, studyOrganizationId));
        List<OrganizationClinicalStaff> organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId(text, studyOrganizationId);
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
    public List<ClinicalStaff> searchClinicalStaff(String[] searchString, Integer startIndex, Integer results, String sort, String dir) {
        List<ClinicalStaff> clinicalStaffs = getClinicalStaffObjects(searchString, startIndex, results, sort, dir, true);
        return clinicalStaffs;
    }

    public List<ClinicalStaff> getClinicalStaffObjects(String[] searchStrings, Integer startIndex, Integer results, String sort, String dir, boolean showInactive) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery(true, true, true);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String userName = user.getUsername();
        List<ClinicalStaff> clinicalStaffs = new ArrayList<ClinicalStaff>();
        clinicalStaffQuery.setFirstResult(startIndex);
        clinicalStaffQuery.setMaximumResults(results);
        clinicalStaffQuery.setSortBy("cs." + sort);
        clinicalStaffQuery.setSortDirection(dir);
        if (user.isAdmin()) {
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
            Long searchCount = resultCount(searchStrings);
            int index = startIndex;
            ClinicalStaffQuery clinicalStaffQuery1 = new ClinicalStaffQuery(false, false, false);
            clinicalStaffQuery1.filterByUserName(userName);
            List<ClinicalStaff> cs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery1);
            List<Integer> orgs = new ArrayList();
            for (OrganizationClinicalStaff ocs : cs.get(0).getOrganizationClinicalStaffs()) {
                orgs.add(ocs.getOrganization().getId());
            }
            clinicalStaffQuery.filterByOrganization(orgs);
            if (searchStrings == null) {

                clinicalStaffs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
                if (clinicalStaffs.size() == results || clinicalStaffs.size() == searchCount) {
                    return clinicalStaffs;
                } else {
                    while (clinicalStaffs.size() != results && clinicalStaffs.size() != searchCount) {
                        index = results + index;
                        clinicalStaffQuery.setFirstResult(index);
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

//        if (!user.isAdmin()) {
//            Long searchCount = resultCount(searchStrings);
//            if (clinicalStaffs.size() == results) {
//                return clinicalStaffs;
//            } else {
//                int index = startIndex;
//                while (clinicalStaffs.size() != results && clinicalStaffs.size() != searchCount) {
//                    index = results + index;
//                    clinicalStaffQuery.setFirstResult(index);
//                    List<ClinicalStaff> l = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery);
//                    for (ClinicalStaff clinicalStaff : l) {
//                        clinicalStaffs.add(clinicalStaff);
//                    }
//                    l.clear();
//                }
//                return clinicalStaffs;
//            }
//        }
//        return clinicalStaffs;
    }


    public Long resultCount(String[] searchTexts) {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery(true, true);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = user.getUsername();
        if (!user.isAdmin() && searchTexts == null) {
            ClinicalStaffQuery clinicalStaffQuery1 = new ClinicalStaffQuery(false, false, false);
            clinicalStaffQuery1.filterByUserName(userName);
            List<ClinicalStaff> cs = (List<ClinicalStaff>) clinicalStaffRepository.find(clinicalStaffQuery1);
            List<Integer> orgs = new ArrayList();

            for (OrganizationClinicalStaff ocs : cs.get(0).getOrganizationClinicalStaffs()) {
                orgs.add(ocs.getOrganization().getId());
            }

            clinicalStaffQuery.filterByOrganization(orgs);

        } else {
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
        }
        return clinicalStaffRepository.findWithCount(clinicalStaffQuery);

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