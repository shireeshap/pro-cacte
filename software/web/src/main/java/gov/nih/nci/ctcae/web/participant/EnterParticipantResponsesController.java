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
import java.util.HashMap;
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
    	Map<Integer, Boolean> unAnsweredProItemMap;
    	Map<Integer, Boolean> unAnsweredMeddraMap;

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
        
        unAnsweredProItemMap = new HashMap<Integer, Boolean>();
        unAnsweredMeddraMap = new HashMap<Integer, Boolean>();
        // Record id's of all answered proCtcae questions in unAnsweredProItemMap
        List<StudyParticipantCrfItem> spcItems = studyParticipantCrfSchedule.getStudyParticipantCrfItems();
        for(StudyParticipantCrfItem item : spcItems){
        	if(item.getProCtcValidValue() == null){
        		unAnsweredProItemMap.put(item.getId(), true);
        	}
        }
        
        // Record id's of all answered added questions in unAnsweredMeddraMap/unAnsweredProItemMap accordingly
        List<StudyParticipantCrfScheduleAddedQuestion> spcsAdded = studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions();
        for(StudyParticipantCrfScheduleAddedQuestion addedItem : spcsAdded){
        	if(addedItem.getProCtcQuestion() != null && addedItem.getProCtcValidValue() == null){
        		unAnsweredProItemMap.put(addedItem.getId(), true);
        	} else if(addedItem.getMeddraQuestion() != null && addedItem.getMeddraValidValue() == null)
        		unAnsweredMeddraMap.put(addedItem.getId(), true);
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
        String prevTab = request.getParameter("prevTab");
        
        String language = request.getParameter("lang");
        if (language == null) {
        	 language = "en";
             if ("SPANISH".equals(spcSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getHomeWebLanguage())) {
                 language = "es";
             } 
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
        map.put("prevTab", prevTab);
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
        	// mark the schedule as In-Progress
            studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
            
            // capture responseDate, responseMode, updatedBy information for newly answered pro questions
            List<StudyParticipantCrfItem> spcItems = studyParticipantCrfSchedule.getStudyParticipantCrfItems();
            if (spcItems != null) { 
                for (StudyParticipantCrfItem spcCrfItem : spcItems) {
                    if (unAnsweredProItemMap.get(spcCrfItem.getId()) != null && spcCrfItem.getProCtcValidValue() != null) {
                    	updateSpcrfItem(spcCrfItem, studyParticipantCrfSchedule, user);
                    	unAnsweredProItemMap.remove(spcCrfItem.getId());
                    }
                }
            }
            
            List<StudyParticipantCrfScheduleAddedQuestion> spcsAdded = studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions();
            if (spcsAdded != null) {
                for (StudyParticipantCrfScheduleAddedQuestion spcsaq : spcsAdded) {
                    // capture responseDate, responseMode, updatedBy information for newly answered added pro questions
                	if(spcsaq.getProCtcQuestion() != null){
                		if (unAnsweredProItemMap.get(spcsaq.getId()) != null && spcsaq.getProCtcValidValue() != null) {
                			updateSpcsaq(spcsaq, studyParticipantCrfSchedule, user);
                			unAnsweredProItemMap.remove(spcsaq.getId());
                		}
                	} 
                    // capture responseDate, responseMode, updatedBy information for newly answered added meddra questions
                	 else if(spcsaq.getMeddraQuestion() != null){
                		if (unAnsweredMeddraMap.get(spcsaq.getId()) != null && spcsaq.getMeddraValidValue() != null) {
                			updateSpcsaq(spcsaq, studyParticipantCrfSchedule, user);
                			unAnsweredMeddraMap.remove(spcsaq.getId());
                		}
                	}
                }
            }
            
        } else {
            studyParticipantCrfSchedule.setFormSubmissionMode(AppMode.CLINICWEB);
            studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
            List<StudyParticipantCrfItem> spcItems = studyParticipantCrfSchedule.getStudyParticipantCrfItems();
            if (spcItems != null) {
                for (StudyParticipantCrfItem spcCrfItem : spcItems) {
                    if (spcCrfItem.getResponseMode() == null) {
                    	updateSpcrfItem(spcCrfItem, studyParticipantCrfSchedule, user);
                    }
                }
            }
            List<StudyParticipantCrfScheduleAddedQuestion> spcsAdded = studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions();
            if (spcsAdded != null) {
                for (StudyParticipantCrfScheduleAddedQuestion spcsaq : spcsAdded) {
                    if (spcsaq.getResponseMode() == null) {
                    	updateSpcsaq(spcsaq, studyParticipantCrfSchedule, user);
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
    
    private void updateSpcrfItem(StudyParticipantCrfItem spcCrfItem, StudyParticipantCrfSchedule studyParticipantCrfSchedule, User user){
    	 spcCrfItem.setReponseDate(studyParticipantCrfSchedule.getCompletionDate());
         spcCrfItem.setResponseMode(AppMode.CLINICWEB);
         spcCrfItem.setUpdatedBy(user.getUsername());
    }

    private void updateSpcsaq(StudyParticipantCrfScheduleAddedQuestion spcsaq, StudyParticipantCrfSchedule studyParticipantCrfSchedule, User user){
    	spcsaq.setReponseDate(studyParticipantCrfSchedule.getCompletionDate());
        spcsaq.setResponseMode(AppMode.CLINICWEB);
        spcsaq.setUpdatedBy(user.getUsername());
    }

    @Required
    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
