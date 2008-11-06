package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Harsh Agarwal
 * @crated Nov 6, 2008
 */
public class AddCrfScheduleController extends AbstractController {

    protected FinderRepository finderRepository;

    public FinderRepository getFinderRepository() {
        return finderRepository;
    }


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/oneCrfScheduleSection");

        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);

        Integer studyCrfId = Integer.parseInt(request.getParameter("studycrfid"));
        StudyCrf studyCrf = finderRepository.findById(StudyCrf.class, studyCrfId);

        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf(studyCrf);

        studyParticipantCommand.getStudyParticipantCrfs().add(studyParticipantCrf);

        int index = studyParticipantCommand.getStudyParticipantCrfs().size() - 1;
        modelAndView.addObject("index", index);
        return modelAndView;
    }


    public AddCrfScheduleController() {
        setSupportedMethods(new String[]{"GET"});

    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

}