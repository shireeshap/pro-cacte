package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
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

        Integer crfIndex = Integer.parseInt(request.getParameter("crfindex"));

        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();

        StudyParticipantCrf studyParticipantCrf =studyParticipantCommand.getStudyParticipantAssignment().getStudyParticipantCrfs().get(crfIndex.intValue());
        studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule);

        int index = studyParticipantCrf.getStudyParticipantCrfSchedules().size() - 1;
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