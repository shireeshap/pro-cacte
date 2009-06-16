package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
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
    public boolean validate(Object value) {
        if (value instanceof String) {
            StudyQuery studyQuery = new StudyQuery();
            studyQuery.filterByAssignedIdentifierExactMatch((String) value);
            Collection<Study> studies = studyRepository
                    .find(studyQuery);
            return (studies == null || studies.isEmpty()) ? true : false;
        }
        return true;
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

