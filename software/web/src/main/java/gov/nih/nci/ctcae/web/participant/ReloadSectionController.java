package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author AmeyS
 * ReloadSectionController class.
 * Description: This controller is used to support making actions like putting the participants on-hold/off-hold
 * on ParticipantDetailsTab, Ajax and reload the <Div> with appropriate message accordingly. 
 *
 */
public class ReloadSectionController extends AbstractController {

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		ParticipantCommand command = ParticipantControllerUtils.getParticipantCommand(request);
		command.lazyInitializeAssignment(null, false);
		ModelAndView mv = new ModelAndView("participant/ajax/reloadParticipantDetailsSection");
		mv.addObject("command", command);
		mv.addObject("isCreateFlow", !command.getParticipant().isPersisted());
		return mv;
	}
}