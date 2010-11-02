package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mehul gulati
 *         Date: Oct 26, 2010
 */
public class ParticipantOffHoldController extends CtcAeSimpleFormController {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ParticipantOffHoldController() {
        super();
        setCommandClass(StudyParticipantAssignment.class);
        setFormView("participant/offHoldDate");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String spaId = request.getParameter("id");
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(Integer.parseInt(spaId));
        return studyParticipantAssignment;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyParticipantAssignment studyParticipantAssignment = (StudyParticipantAssignment) command;
        studyParticipantAssignment.setOnHoldTreatmentDate(null);
        studyParticipantAssignmentRepository.save(studyParticipantAssignment);
        studyParticipantAssignment = studyParticipantAssignmentRepository.findById(studyParticipantAssignment.getId());
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                if (studyParticipantAssignment.getOffHoldTreatmentDate().getTime() < studyParticipantCrfSchedule.getStartDate().getTime()) {
//                    if ((CrfStatus.ONHOLD).equals(studyParticipantCrfSchedule.getStatus())) {
                        studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
                        studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
//                    }
                }
            }
        }

        RedirectView redirectView = new RedirectView("edit?id=" + studyParticipantAssignment.getParticipant().getId());
        return new ModelAndView(redirectView);

    }

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}

