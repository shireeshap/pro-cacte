package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Mehul Gulati
 *         Date: Jun 24, 2009
 */
public class EnterParticipantResponsesController extends CtcAeSimpleFormController {

    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    public EnterParticipantResponsesController() {
        super();
        setCommandClass(StudyParticipantCrfSchedule.class);
        setFormView("participant/inputParticipantResponses");
        setSuccessView("/participant/inputParticipantResponses");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        if (!StringUtils.isBlank(id)) {
            studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(id));
        }
        return studyParticipantCrfSchedule;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        StudyParticipantCrfSchedule spcSchedule = (StudyParticipantCrfSchedule) command;
        String study = spcSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getStudy().getShortTitle();
        String crf = spcSchedule.getStudyParticipantCrf().getCrf().getTitle();
        String spId = spcSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudyParticipantIdentifier();
        String participantName = spcSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName();
        Date startDate = spcSchedule.getStartDate();
        Date dueDate = spcSchedule.getDueDate();
        Map<String, Object> map = super.referenceData(request, command, errors);
        String language = request.getParameter("lang");
        if (language == null || language == "") {
            language = "en";
        }
        spcSchedule.setLanguage(language);
        if (spcSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
            map.put("isSadEditable", "false");
        } else {
            map.put("isSadEditable", "true");
        }
        map.put("language", language);
        map.put("study", study);
        map.put("crf", crf);
        map.put("spId", spId);
        map.put("participantName", participantName);
        map.put("startDate", startDate);
        map.put("dueDate", dueDate);
        return map;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = (StudyParticipantCrfSchedule) oCommand;
        String submitType = request.getParameter("submitType");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        String language = studyParticipantCrfSchedule.getLanguage();
        if (language == null || language == "") {
            language = "en";
        }
        if ("save".equals(submitType) || "saveandback".equals(submitType)) {
            studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        } else {
            studyParticipantCrfSchedule.setFormSubmissionMode(AppMode.CLINICWEB);
            studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
            List<StudyParticipantCrfItem> spcItems = studyParticipantCrfSchedule.getStudyParticipantCrfItems();
            if (spcItems != null) {
                for (StudyParticipantCrfItem spcCrfItem : spcItems) {
                    if (spcCrfItem.getResponseMode() == null) {
                        spcCrfItem.setReponseDate(studyParticipantCrfSchedule.getCompletionDate());
                        spcCrfItem.setResponseMode(AppMode.CLINICWEB);
                        spcCrfItem.setUpdatedBy(user.getUsername());
                    }
                }
            }
            List<StudyParticipantCrfScheduleAddedQuestion> spcsAdded = studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions();
            if (spcsAdded != null) {
                for (StudyParticipantCrfScheduleAddedQuestion spcsaq : spcsAdded) {
                    if (spcsaq.getResponseMode() == null) {
                        spcsaq.setReponseDate(studyParticipantCrfSchedule.getCompletionDate());
                        spcsaq.setResponseMode(AppMode.CLINICWEB);
                        spcsaq.setUpdatedBy(user.getUsername());
                    }
                }
            }
        }
        studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
        ModelAndView modelAndView = new ModelAndView(new RedirectView("enterResponses?id=" + studyParticipantCrfSchedule.getId() + "&lang=" + language));
        if ("saveandback".equals(submitType)) {
            modelAndView = new ModelAndView(new RedirectView("edit?id=" + studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getId() + "&tab=3"));
        }
        modelAndView.addObject("successMessage", "true");
        return modelAndView;
    }


    @Required
    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
