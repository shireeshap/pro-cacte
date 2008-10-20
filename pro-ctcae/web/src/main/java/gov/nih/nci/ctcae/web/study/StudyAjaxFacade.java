package gov.nih.nci.ctcae.web.study;

import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class StudyAjaxFacade {
    private StudyRepository studyRepository;

    public String searchStudies(Map parameterMap, String type, String text, HttpServletRequest request) {
        List<Study> studies = getObjects(type, text);

        StudyTableModel studyTableModel = new StudyTableModel();
        String table = studyTableModel.buildStudyTable(parameterMap, studies, request);
        return table;
    }

    private List<Study> getObjects(String type, String text) {
        StudyQuery studyQuery = new StudyQuery();


        if ("shortTitle".equals(type)) {
            studyQuery.filterStudiesByShortTitle(text);
        } else if ("assignedIdentifier".equals(type)) {
            studyQuery.filterStudiesByAssignedIdentifier(text);
        }

        List<Study> studies = (List<Study>) studyRepository.find(studyQuery);
        return studies;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
