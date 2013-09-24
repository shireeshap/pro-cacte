package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

//

/**
 * The Class UniqueIdentifierForStudyValidator.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
public class UniqueIdentifierForStudyValidator extends AbstractValidator<UniqueIdentifierForStudy> {

    /**
     * The message.
     */
    private String message;

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object)
     */

    public boolean validateUniqueIdentifier(String studyId, String studyIdentifier) {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch((String) studyIdentifier);
        Collection<Study> studies = studyRepository.find(studyQuery);
        boolean flag=false;
        if (studies != null && !studies.isEmpty()) {
            if (studyId == null || studyId.equals("")) {
                return true;
            } else {
                for (Study study : studies) {
                    if (study.getId().equals(Integer.parseInt(studyId))) {
                        flag = true;
                    }
                }
                if(!flag){
                    return true;
                }
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
     */

    public void initialize(UniqueIdentifierForStudy parameters) {
        message = parameters.message();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
     */

    public String message() {
        return message;
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

