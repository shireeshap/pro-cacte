package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Ramakrishna Gundala
 *         Date: Sep 16, 2015
 */
public class PrintParticipantCompleteSurveyScheduleController extends AbstractController {
	ParticipantRepository participantRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PrintParticipantCompleteSurveySchedulePdfView printParticipantCompleteSurveySchedulePdfView = new PrintParticipantCompleteSurveySchedulePdfView();
    	printParticipantCompleteSurveySchedulePdfView.setParticipantRepository(participantRepository);
    	return new ModelAndView(printParticipantCompleteSurveySchedulePdfView);
    }

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}
}
