package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.LeadStudySite;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
//
/**
 * The Class SitesTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class StudySitesTab extends SecuredTab<StudyCommand> {
    private GenericRepository genericRepository;
	private StudyOrganizationRepository studyOrganizationRepository;


	public StudyOrganizationRepository getStudyOrganizationRepository() {
		return studyOrganizationRepository;
	}

	public void setStudyOrganizationRepository(
			StudyOrganizationRepository studyOrganizationRepository) {
		this.studyOrganizationRepository = studyOrganizationRepository;
	}

	public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    /**
     * Instantiates a new sites tab.
     */
    public StudySitesTab() {
        super("study.tab.sites", "study.tab.sites", "study/study_sites");
    }


    @Override
    public void validate(StudyCommand command, Errors errors) {

        List<StudySite> studySitesToRemove = new ArrayList<StudySite>();
        for (Integer index : command.getSiteIndexesToRemove()) {
            studySitesToRemove.add(command.getStudy().getStudySites().get(index));
        }
        for (StudySite studySite : studySitesToRemove) {
            StudySite tempStudySite = genericRepository.findById(StudySite.class, studySite.getId());
            boolean delete = true;
            if (tempStudySite != null) {
                if (tempStudySite.getStudyParticipantAssignments().size() > 0 || tempStudySite.getStudyOrganizationClinicalStaffs().size() > 0) {
                    errors.reject("NON_EMPTY_STUDY_SITE", "Cannot delete site " + studySite.getDisplayName() + " as there are participants and clinical staff assigned to it.");
                    delete = false;
                }
            }
            if (delete) {
                command.getStudy().getStudyOrganizations().remove(studySite);
            }
        }
        command.getSiteIndexesToRemove().clear();

        for (StudyOrganization studyOrganization : command.getStudy().getStudyOrganizations()) {
            if ((studyOrganization instanceof StudySite) && !(studyOrganization instanceof LeadStudySite)) {
                if (studyOrganization.getOrganization().equals(command.getStudy().getLeadStudySite().getOrganization())) {
                    errors.reject("LEAD_STUDY_SITE", "Cannot add study site " + studyOrganization.getOrganization() + ". It is the lead site on this study.");
                }
            }
        }
    }
    
    /* (non-Javadoc)
    * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#postProcess(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
    */
    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);
        
        //add all clinicians from the newly added site to the study site, if they are already on some other study site for this study.
        for(StudySite studySite : command.getStudy().getStudySites()){
        	if(!studySite.isPersisted()){
            	List<Role> roleList = Arrays.asList(Role.SITE_CRA, Role.SITE_PI);

            	List<StudyOrganizationClinicalStaff> socsAlreadyOnStudyList = getAllSocsAssignedToStudyWithGivenRole(command.getStudy(), roleList);
            	Set<StudyOrganizationClinicalStaff> socsSet = new HashSet<StudyOrganizationClinicalStaff>();

        		//build socsList of all socs on study with SCRA/SPI role
        		for(StudyOrganizationClinicalStaff socsOnStudy : socsAlreadyOnStudyList){

        			ClinicalStaff clinicalStaff = socsOnStudy.getOrganizationClinicalStaff().getClinicalStaff();
        			for(OrganizationClinicalStaff ocsToAttach : clinicalStaff.getOrganizationClinicalStaffs()){
        			    if(ocsToAttach.getOrganization().equals(studySite.getOrganization())){
        			    	StudyOrganizationClinicalStaff newSocs = buildNewSocs(socsOnStudy, studySite, ocsToAttach);
        			    	socsSet.add(newSocs);
        			    }
        			}
        		}
        		if(!socsSet.isEmpty()){
                    studySite.getStudyOrganizationClinicalStaffs().addAll(socsSet);
    	    		studyOrganizationRepository.save(studySite);
        		}
        	}
        }
        
        HashSet<StudySite> studySites = new HashSet<StudySite>();
        int index = 0;
        for(StudySite studySite : command.getStudy().getStudySites()){
        	if(!studySites.add(studySite)){
        		String duplicateStudySiteErrorMsg = "Duplicate study site" + ": " + studySite.getOrganization().getName();
        		errors.rejectValue("study.studySites[" + index + "]", "" , duplicateStudySiteErrorMsg);        		
        	}
        	index++;
        }
    }

    private StudyOrganizationClinicalStaff buildNewSocs(StudyOrganizationClinicalStaff socs, StudySite ss, OrganizationClinicalStaff ocs) {
		StudyOrganizationClinicalStaff newSocs = new StudyOrganizationClinicalStaff();
		newSocs.setStudyOrganization(ss);
        newSocs.setRole(socs.getRole());
        newSocs.setNotify(socs.getNotify());
        newSocs.setOrganizationClinicalStaff(ocs);
        return newSocs;
	}

	private List<StudyOrganizationClinicalStaff> getAllSocsAssignedToStudyWithGivenRole(Study study, List<Role> roleList) {
    	List<StudyOrganizationClinicalStaff> socsList  = new ArrayList<StudyOrganizationClinicalStaff>();
    	for(StudyOrganizationClinicalStaff socs : study.getAllStudyOrganizationClinicalStaffs()){
    		if(roleList.contains(socs.getRole())){
    			if(socs.getRoleStatus().equals(RoleStatus.ACTIVE)){
        			socsList.add(socs);
    			}
    		}
    	}
    	return socsList;
	}

	public String getRequiredPrivilege() {
    	String privilege = Privilege.PRIVILEGE_ADD_STUDY_SITE;
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if(!user.isAdmin()){
    		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    		String studyId = attr.getRequest().getParameter("studyId");
        	if(!StringUtils.isEmpty(studyId) ){
        		privilege = privilege + AuthorizationServiceImpl.getStudyInstanceSpecificPrivilege(Integer.parseInt(studyId));
        	}
    	}
        return privilege;
    }
}
