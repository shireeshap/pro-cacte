package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class ParticipantController.
 *
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */

public class CreateParticipantController extends ParticipantController {
	
	@Override
	protected String getFormSessionAttributeName(HttpServletRequest request) {
		return CreateParticipantController.class.getName() + ".FORM." + "command";
	}
}