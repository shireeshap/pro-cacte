package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class UniqueTitleForCrfValidator.
 *
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class UniqueTitleForCrfValidator extends AbstractValidator<UniqueTitleForCrf> {

    /**
     * The message.
     */
    private String message;

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;


    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.AbstractValidator#validate(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean validate(final Object bean, final Object value) {
        if (value instanceof String && bean instanceof CRF) {
            CRF crfBean = (CRF) bean;
            CRFQuery query = new CRFQuery();
            query.filterByTitleExactMatch((String) value);
            query.filterByNotHavingCrfId(crfBean.getId());
            query.filterByCrfVersion(crfBean.getCrfVersion());
            List<CRF> crfs = new ArrayList<CRF>(crfRepository.find(query));
            return (crfs == null || crfs.isEmpty()) ? true : false;
        }
        return super.validate(bean, value);


    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(UniqueTitleForCrf parameters) {
        message = parameters.message();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.validation.annotation.Validator#message()
     */
    public String message() {
        return message;
    }

    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    @Required
    public void setCrfRepository(final CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}