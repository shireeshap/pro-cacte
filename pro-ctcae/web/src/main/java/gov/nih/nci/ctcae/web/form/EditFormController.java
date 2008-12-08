package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class EditFormController extends FormController {

	private FinderRepository finderRepository;

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CreateFormCommand command = (CreateFormCommand) ControllersUtils.getFormCommand(request, this);
		if (command == null) {
			command = new CreateFormCommand();
		}
		StudyCrf studyCrf = finderRepository.findAndInitializeStudyCrf(ServletRequestUtils.getRequiredIntParameter(request, "studyCrfId"));
		if (studyCrf == null) {
			throw new CtcAeSystemException("No form found for given id" + ServletRequestUtils.getRequiredIntParameter(request, "studyCrfId"));
		}
		command.setStudyCrf(studyCrf);
		if (CrfStatus.DRAFT.equals(studyCrf.getCrf().getStatus())) {
			return command;
		}

		throw new CtcAeSystemException("You can not only edit DRAFT forms. The status of this form is:" + studyCrf.getCrf().getStatus());


	}

	@Override
	protected int getInitialPage(HttpServletRequest request) {
		return 1;

	}

	public void setFinderRepository(final FinderRepository finderRepository) {
		this.finderRepository = finderRepository;
	}
}