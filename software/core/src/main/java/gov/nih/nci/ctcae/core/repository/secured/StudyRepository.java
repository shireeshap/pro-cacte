package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.LeadStudySite;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyMode;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import gov.nih.nci.ctcae.core.security.SecurityHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//
/**
 * The Class StudyRepository.
 *
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class StudyRepository implements Repository<Study, StudyQuery> {
    private GenericRepository genericRepository;

	public void delete(Study study) {
        genericRepository.delete(study);
    }

    public Collection<Study> find(StudyQuery query) {
        return genericRepository.find(query);


    }

    public Study findSingle(StudyQuery query) {
        return genericRepository.findSingle(query);


    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Study save(Study study) {
        removeLeadStudySiteFromStudySites(study);
        Study savedStudy = genericRepository.save(study);
        initialzeStudy(savedStudy);
        SecurityHelper.updateLoadedStudyPrivileges(study);
        return savedStudy;

    }


	private void removeLeadStudySiteFromStudySites(Study study) {
        List<StudySite> studySitesToRemove = new ArrayList<StudySite>();
        for (StudySite studySite : study.getStudySites()) {
            if (studySite != null && studySite.getOrganization() != null) {
                if (studySite.getOrganization().equals(study.getLeadStudySite().getOrganization())) {
                    if (!(studySite instanceof LeadStudySite)) {
                        studySitesToRemove.add(studySite);
                    }
                }
            }
        }
        for (StudySite studySite : studySitesToRemove) {
            study.removeStudySite(studySite);
        }
    }

    public Study findById(Integer id) {
        Study study = genericRepository.findById(Study.class, id);
        if (study != null) {
            initialzeStudy(study);
        }
        return study;


    }

    public void addStudySite(Study study) {
        StudySite studySite = new StudySite();
        study.addStudySite(studySite);
    }

    private void initialzeStudy(Study savedStudy) {
        List<StudyOrganization> studyOrganizations = savedStudy.getStudyOrganizations();
        for (StudyOrganization studyOrganization : studyOrganizations) {
            List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffList = studyOrganization.getStudyOrganizationClinicalStaffs();
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganizationClinicalStaffList) {
                studyOrganizationClinicalStaff.getStudyOrganization();
                studyOrganizationClinicalStaff.getOrganizationClinicalStaff();
            }
        }

        for(StudyMode studyMode:savedStudy.getStudyModes()){
            studyMode.getMode();
        }


    }

    public void addStudyOrganizationClinicalStaff(List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs) {
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganizationClinicalStaffs) {
            if (studyOrganizationClinicalStaff != null) {
                studyOrganizationClinicalStaff.getStudyOrganization().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
            }
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }


}