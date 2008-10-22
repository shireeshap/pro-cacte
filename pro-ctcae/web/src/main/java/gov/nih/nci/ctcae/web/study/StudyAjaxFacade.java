package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class StudyAjaxFacade {
    private StudyRepository studyRepository;
    private OrganizationRepository organizationRepository;

    public String searchStudies(Map parameterMap, String type, String text, HttpServletRequest request) {
        List<Study> studies = getObjects(type, text);

        StudyTableModel studyTableModel = new StudyTableModel();
        String table = studyTableModel.buildStudyTable(parameterMap, studies, request);
        return table;
    }

    private List<Study> getObjects(String type, String text) {
        StudyQuery studyQuery = new StudyQuery();
        List<Study> studies = new ArrayList<Study>();

        if ("shortTitle".equals(type)) {
            studyQuery.filterStudiesByShortTitle(text);
            studies = (List<Study>) studyRepository.find(studyQuery);
        } else if ("assignedIdentifier".equals(type)) {
            studyQuery.filterStudiesByAssignedIdentifier(text);
            studies = (List<Study>) studyRepository.find(studyQuery);
        }else if ("site".equals(type)) {
        	studies = organizationRepository.findStudiesForOrganization(text);
        }

        return studies;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }    
    
    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
}
