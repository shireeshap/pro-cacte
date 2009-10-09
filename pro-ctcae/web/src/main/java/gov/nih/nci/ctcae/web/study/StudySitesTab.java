package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;

//
/**
 * The Class SitesTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class StudySitesTab extends SecuredTab<StudyCommand> {
    private GenericRepository genericRepository;

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
            if (tempStudySite != null) {
                if (tempStudySite.getStudyParticipantAssignments().size() > 0 || tempStudySite.getStudyOrganizationClinicalStaffs().size() > 0) {
                    errors.reject("NON_EMPTY_STUDY_SITE", "Cannot delete site " + studySite.getDisplayName() + " as there are participants and clinical staff assigned to it.");
                } else {
                    command.getStudy().getStudyOrganizations().remove(studySite);
                }
            }
        }
        command.getSiteIndexesToRemove().clear();
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#postProcess(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
    */
    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_STUDY_SITE;


    }
}
