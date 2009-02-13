package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.ListValues;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.ClinicalStaffAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 11, 2009
 */
public class StudyInvestigatorsTab extends Tab<StudyCommand> {
    public StudyInvestigatorsTab() {
        super("study.tab.study_investigator", "study.tab.study_investigator", "study/study_investigators");
    }

    private FinderRepository finderRepository;
    private ClinicalStaffRepository clinicalStaffRepository;


    @Override
    public Map<String, Object> referenceData(StudyCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);

        List<StudySite> studySites = command.getStudy().getStudySites();

        List<ListValues> studySiteListValues = new ArrayList<ListValues>();


        for (StudySite studySite : studySites) {
            studySiteListValues.add(new ListValues(String.valueOf(studySite.getId()), studySite.getOrganization().getDisplayName()));
        }
        referenceData.put("studySites", studySiteListValues);

        List<Integer> studySiteIds = new ArrayList<Integer>();
        for (StudySite studySite : studySites) {

            studySiteIds.add(studySite.getId());
        }
        if (!studySiteIds.isEmpty()) {

            ClinicalStaffAssignment clinicalStaffAssignment = command.getClinicalStaffAssignment();

            if (command.getSelectedStudySiteId() == null) {
                StudySite studySite = studySites.get(0);
                command.setSelectedStudySiteId(studySite.getId());
                clinicalStaffAssignment.setDisplayName(studySite.getOrganization().getDisplayName());
                clinicalStaffAssignment.setDomainObjectId(studySite.getOrganization().getId());

            } else {
                StudySite studySite = command.getStudy().getStudySiteById(command.getSelectedStudySiteId());
                clinicalStaffAssignment.setDisplayName(studySite.getOrganization().getDisplayName());
                clinicalStaffAssignment.setDomainObjectId(studySite.getOrganization().getId());

            }

            //get the clinical staff assignments
            ClinicalStaffAssignmentQuery query = new ClinicalStaffAssignmentQuery();
            query.filterByStudySiteIds(studySiteIds);
            List<ClinicalStaffAssignment> clinicalStaffAssignments = (List<ClinicalStaffAssignment>) finderRepository.find(query);
            command.setClinicalStaffAssignments(clinicalStaffAssignments);
            command.updateDisplayNameOfClinicalStaff(finderRepository);

        }

        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {

        if (!org.apache.commons.lang.StringUtils.isBlank(command.getClinicalStaffAssignmentIndexToRemove())) {
            Integer clinicalStaffAssignmentIndex = Integer.valueOf(command.getClinicalStaffAssignmentIndexToRemove());
            ClinicalStaffAssignment clinicalStaffAssignment = command.getClinicalStaffAssignments().get(clinicalStaffAssignmentIndex);
            clinicalStaffRepository.removeClinicalStaffAssignment(clinicalStaffAssignment);

        } else if (!org.apache.commons.lang.StringUtils.isBlank(command.getClinicalStaffAssignmentRoleIndexToRemove())) {

            String[] investigatorIndexVsRoleIndex = StringUtils.split(command.getClinicalStaffAssignmentRoleIndexToRemove(), "-");

            Integer clinicalStaffAssignmentIndex = Integer.valueOf(investigatorIndexVsRoleIndex[0]);
            Integer clinicalStaffAssignmentRoleIndex = Integer.valueOf(investigatorIndexVsRoleIndex[1]);
            ClinicalStaffAssignment clinicalStaffAssignment = command.getClinicalStaffAssignments().get(clinicalStaffAssignmentIndex);

            clinicalStaffAssignment.removeClinicalStaffAssignmentRoleRole(clinicalStaffAssignmentRoleIndex);

        }
        command.apply();

        super.postProcess(request, command, errors);


    }

    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

}
