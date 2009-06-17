package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author Mehul Gulati
 * Date: Apr 7, 2009
 */

public class ParticipantOffStudyController extends CtcAeSimpleFormController {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ParticipantOffStudyController() {
        super();
        setCommandClass(StudyParticipantAssignment.class);
        setFormView("participant/offStudyDate");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        String spaId = request.getParameter("id");
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(Integer.parseInt(spaId));
        studyParticipantAssignment.setOffTreatmentDate(new Date());
        return studyParticipantAssignment;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyParticipantAssignment studyParticipantAssignment = (StudyParticipantAssignment) command;
        Date offTreatmentDate = studyParticipantAssignment.getOffTreatmentDate();
        studyParticipantAssignment = studyParticipantAssignmentRepository.findById(studyParticipantAssignment.getId());
        studyParticipantAssignment.setOffTreatmentDate(offTreatmentDate);
        studyParticipantAssignmentRepository.save(studyParticipantAssignment);

        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                if (studyParticipantAssignment.getOffTreatmentDate().getTime() < studyParticipantCrfSchedule.getStartDate().getTime()) {
                    studyParticipantCrfSchedule.setStatus(CrfStatus.OFFSTUDY);
                    studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
                }
            }
        }

        RedirectView redirectView = new RedirectView("create?id=" + studyParticipantAssignment.getParticipant().getId());
        return new ModelAndView(redirectView);
    }

    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }


    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
