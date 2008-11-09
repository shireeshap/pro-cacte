package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class UniqueTitleForCrfValidator implements Validator<UniqueTitleForCrf> {
    private String message;

    private CRFRepository crfRepository;

    public boolean validate(Object value) {
        if (value instanceof String) {
            CRFQuery query = new CRFQuery();
            query.filterByTitleExactMatch((String) value);
            Collection<CRF> crfs = crfRepository
                    .find(query);
            return (crfs == null || crfs.isEmpty()) ? true : false;
        }
        return true;
    }

    public void initialize(UniqueTitleForCrf parameters) {
        message = parameters.message();
    }

    public String message() {
        return message;
    }

    @Required
    public void setCrfRepository(final CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}