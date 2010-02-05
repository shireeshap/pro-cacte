package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//
/**
 * The Class StudyAjaxFacade.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
public class StudyAjaxFacade {

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;

    /**
     * The participant repository.
     */

    /**
     * Match study.
     *
     * @param text the text
     * @return the list< study>
     */
    public List<Study> matchStudy(final String text) {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesWithMatchingText(text);
        studyQuery.setMaximumResults(30);
        List<Study> studies = new ArrayList<Study>(studyRepository.find(studyQuery));
        return ObjectTools.reduceAll(studies, "id", "shortTitle", "assignedIdentifier");

    }

    /**
     * Search studies.
     *
     * @param type the type
     * @param text the text
     * @return the string
     */
    public List<Study> searchStudies(String type, String text) {
        List<Study> studies = getObjects(type, text);
        return studies;
    }


    /**
     * Gets the objects.
     *
     * @param type the type
     * @param text the text
     * @return the objects
     */
    private List<Study> getObjects(String type, String text) {
        StudyQuery studyQuery = new StudyQuery();

        if (!StringUtils.isBlank(text)) {
            if ("shortTitle".equals(type)) {
                studyQuery.filterStudiesByShortTitle(text);
            } else if ("assignedIdentifier".equals(type)) {
                studyQuery.filterStudiesByAssignedIdentifier(text);
            }
        }
        List<Study> studies = (List<Study>) studyRepository.find(studyQuery);

        return studies;
    }

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }


}
