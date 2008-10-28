package gov.nih.nci.ctcae.core.validation.annotation;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Collection;

import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

/**
 * @author Saurabh Agrawal
 * @crated Oct 27, 2008
 */
public class UniqueIdentifierForStudyValidator implements Validator<UniqueIdentifierForStudy> {
    private String message;

    private StudyRepository studyRepository;

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

    public void initialize(UniqueIdentifierForStudy parameters) {
        message = parameters.message();
    }

    public String message() {
        return message;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}

