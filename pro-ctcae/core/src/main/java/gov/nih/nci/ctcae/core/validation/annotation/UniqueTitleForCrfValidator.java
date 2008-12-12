package gov.nih.nci.ctcae.core.validation.annotation;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class UniqueTitleForCrfValidator extends AbstractValidator<UniqueTitleForCrf> {
	private String message;

	private CRFRepository crfRepository;


	@Override
	public boolean validate(final Object bean, final Object value) {
		if (value instanceof String && bean instanceof CRF) {
			CRF crfBean = (CRF) bean;
			CRFQuery query = new CRFQuery();
			query.filterByTitleExactMatch((String) value);
			query.filterByNotHavingCrfId(crfBean.getId());
			List<CRF> crfs = new ArrayList<CRF>(crfRepository.find(query));
			return (crfs == null || crfs.isEmpty()) ? true : false;
		}
		return super.validate(bean, value);


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