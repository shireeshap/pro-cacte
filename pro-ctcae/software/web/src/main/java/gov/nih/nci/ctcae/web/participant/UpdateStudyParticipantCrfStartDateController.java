package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.validation.BindException;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;

//
/**
 * The Class ReleaseFormController.
 *
 * @author Vinay Kumar
 * @since Nov 5, 2008
 */
public class UpdateStudyParticipantCrfStartDateController extends CtcAeSimpleFormController {

    /**
     * Instantiates a new delete form controller.
     */
    StudyParticipantCrfRepository studyParticipantCrfRepository;
    GenericRepository genericRepository;

    protected UpdateStudyParticipantCrfStartDateController() {
        super();
        setCommandClass(StudyParticipantCrf.class);
        setFormView("participant/updateStudyParticipantCrfStartDate");
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String spcrfid = request.getParameter("spcrfid");
        StudyParticipantCrf spCrf = studyParticipantCrfRepository.findById(Integer.parseInt(spcrfid));
        return spCrf;
    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyParticipantCrf participantCrf = (StudyParticipantCrf) command;
        participantCrf = studyParticipantCrfRepository.findById(participantCrf.getId());
        String newStartDate = request.getParameter("effectiveStartDate");
        if (!StringUtils.isBlank(newStartDate)) {
            participantCrf.setStartDate(DateUtils.parseDate(newStartDate));
            participantCrf.removeCrfSchedules(CrfStatus.SCHEDULED);
            participantCrf.removeCrfSchedules(CrfStatus.PASTDUE);
            participantCrf.createSchedules();
            participantCrf = studyParticipantCrfRepository.save(participantCrf);
        }
        participantCrf = studyParticipantCrfRepository.findById(participantCrf.getId());
        Participant participant = genericRepository.findById(Participant.class, participantCrf.getStudyParticipantAssignment().getParticipant().getId());
        RedirectView redirectView = new RedirectView("schedulecrf?pId=" + participantCrf.getStudyParticipantAssignment().getParticipant().getId());
        return new ModelAndView(redirectView);
    }

    @Required
    public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
        this.studyParticipantCrfRepository = studyParticipantCrfRepository;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}