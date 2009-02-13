package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//
/**
 * The Class StudyCommand.
 *
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class StudyCommand {

    /**
     * The study.
     */
    private Study study;

    private ClinicalStaffAssignment clinicalStaffAssignment;

    private List<ClinicalStaffAssignment> clinicalStaffAssignments = new ArrayList<ClinicalStaffAssignment>();

    private String clinicalStaffAssignmentIndexToRemove = "";
    private String clinicalStaffAssignmentRoleIndexToRemove = "";

    /**
     * The objects ids to remove.
     */
    private String objectsIdsToRemove;

    private Integer selectedStudySiteId;

    /**
     * Instantiates a new study command.
     */
    public StudyCommand() {
        super();
        this.study = new Study();
        study.setStudyFundingSponsor(new StudyFundingSponsor());
        study.setStudyCoordinatingCenter(new StudyCoordinatingCenter());
        clinicalStaffAssignment = new ClinicalStaffAssignment();

    }


    /**
     * Sets the study.
     *
     * @param study the new study
     */
    public void setStudy(Study study) {
        this.study = study;
    }

    /**
     * Gets the objects ids to remove.
     *
     * @return the objects ids to remove
     */
    public String getObjectsIdsToRemove() {
        return objectsIdsToRemove;
    }

    /**
     * Sets the objects ids to remove.
     *
     * @param objectsIdsToRemove the new objects ids to remove
     */
    public void setObjectsIdsToRemove(String objectsIdsToRemove) {
        this.objectsIdsToRemove = objectsIdsToRemove;
    }

    /**
     * Gets the study.
     *
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

    /**
     * Removes the study sites.
     */
    public void removeStudySites() {
        Set<String> indexes = org.springframework.util.StringUtils.commaDelimitedListToSet(objectsIdsToRemove);
        List<StudySite> studySitesToRemove = new ArrayList<StudySite>();
        for (String index : indexes) {
            StudySite studySite = study.getStudySites().get(Integer.parseInt(index));
            studySitesToRemove.add(studySite);
        }

        for (StudySite studySite : studySitesToRemove) {
            study.removeStudySite(studySite);
        }
    }

    public ClinicalStaffAssignment getClinicalStaffAssignment() {
        return clinicalStaffAssignment;
    }

    public void setClinicalStaffAssignment(ClinicalStaffAssignment clinicalStaffAssignment) {
        this.clinicalStaffAssignment = clinicalStaffAssignment;
    }

    public void addClinicalStaffAssignment(ClinicalStaffAssignment clinicalStaffAssignment) {
        getClinicalStaffAssignments().add(clinicalStaffAssignment);
    }

    public List<ClinicalStaffAssignment> getClinicalStaffAssignments() {
        return clinicalStaffAssignments;
    }

    public void setClinicalStaffAssignments(List<ClinicalStaffAssignment> clinicalStaffAssignments) {
        this.clinicalStaffAssignments = clinicalStaffAssignments;
    }

    public void updateDisplayNameOfClinicalStaff(FinderRepository finderRepository) {
        //now update the display name of clinical staff assignments

        List<ClinicalStaffAssignment> clinicalStaffAssignments = getClinicalStaffAssignments();
        for (ClinicalStaffAssignment clinicalStaffAssignment : clinicalStaffAssignments) {
            if (org.apache.commons.lang.StringUtils.equals(clinicalStaffAssignment.getDomainObjectClass(), StudySite.class.getName())) {
                StudySite studySite = finderRepository.findById(StudySite.class, clinicalStaffAssignment.getDomainObjectId());
                clinicalStaffAssignment.setDisplayName(studySite.getOrganization().getDisplayName());
            }
        }
    }

    public void apply() {

        

        setClinicalStaffAssignmentRoleIndexToRemove("");


        setClinicalStaffAssignmentIndexToRemove("");


    }

    public String getClinicalStaffAssignmentIndexToRemove() {
        return clinicalStaffAssignmentIndexToRemove;
    }

    public void setClinicalStaffAssignmentIndexToRemove(String clinicalStaffAssignmentIndexToRemove) {
        this.clinicalStaffAssignmentIndexToRemove = clinicalStaffAssignmentIndexToRemove;
    }

    public String getClinicalStaffAssignmentRoleIndexToRemove() {
        return clinicalStaffAssignmentRoleIndexToRemove;
    }

    public void setClinicalStaffAssignmentRoleIndexToRemove(String clinicalStaffAssignmentRoleIndexToRemove) {
        this.clinicalStaffAssignmentRoleIndexToRemove = clinicalStaffAssignmentRoleIndexToRemove;
    }

    public Integer getSelectedStudySiteId() {
        return selectedStudySiteId;
    }

    public void setSelectedStudySiteId(Integer selectedStudySiteId) {
        this.selectedStudySiteId = selectedStudySiteId;
    }
}
