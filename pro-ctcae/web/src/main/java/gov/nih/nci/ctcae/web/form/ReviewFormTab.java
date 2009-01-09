package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.repository.FinderRepository;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class ReviewFormTab extends Tab<CreateFormCommand> {

	public ReviewFormTab() {
		super("form.tab.form", "form.tab.review_form", "form/reviewForm");

	}

	private FinderRepository finderRepository;

	public void setFinderRepository(FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}

	@Override
	public Map<String, Object> referenceData(CreateFormCommand command) {
		Map<String, Object> map = super.referenceData(command);
		finderRepository.updateStudyCrfForCrfDisplayRules(command.getStudyCrf());
		return map;


	}

}