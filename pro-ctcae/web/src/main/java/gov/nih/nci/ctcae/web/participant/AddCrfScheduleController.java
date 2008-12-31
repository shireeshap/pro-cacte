package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Harsh Agarwal
 * @crated Nov 6, 2008
 */
public class AddCrfScheduleController extends AbstractController {

    FinderRepository finderRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/oneCrfScheduleSection");

        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);

        Integer crfIndex = Integer.parseInt(request.getParameter("crfindex"));

        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();

        StudyParticipantCrf studyParticipantCrf = studyParticipantCommand.getStudyParticipantAssignment().getStudyParticipantCrfs().get(crfIndex.intValue());

        CRF crf = finderRepository.findById(CRF.class, studyParticipantCrf.getStudyCrf().getCrf().getId());
        studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule, crf);

        int scheduleindex = studyParticipantCrf.getStudyParticipantCrfSchedules().size() - 1;
        modelAndView.addObject("scheduleindex", scheduleindex);
        modelAndView.addObject("crfindex", crfIndex);
        return modelAndView;
    }


    public AddCrfScheduleController() {
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}