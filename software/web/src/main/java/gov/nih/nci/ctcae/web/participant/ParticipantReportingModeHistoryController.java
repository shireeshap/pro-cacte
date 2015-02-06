package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantReportingModeHistory;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantAssignmentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//

/**
 * The Class AddCrfScheduleController.
 *
 * @author Suneel Allareddy
 * @created Dec 14, 2010
 * Controller class called via Ajax. Used to show the Participant reporting mode history
 */
public class ParticipantReportingModeHistoryController extends AbstractController {


    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String participantId = request.getParameter("id");
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.findById(Integer.parseInt(participantId));
        
        ModelAndView mv = new ModelAndView("participant/reportmodehistory");
        List<StudyParticipantReportingModeHistory> homeModeHistItems = new ArrayList<StudyParticipantReportingModeHistory>();
        List<StudyParticipantReportingModeHistory> clinicModeHistItems = new ArrayList<StudyParticipantReportingModeHistory>();
        for (StudyParticipantReportingModeHistory studyParticipantReportingModeHistory : studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems()) {
            if(studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEWEB) ||studyParticipantReportingModeHistory.getMode().equals(AppMode.HOMEBOOKLET)|| studyParticipantReportingModeHistory.getMode().equals(AppMode.IVRS)){
                homeModeHistItems.add(studyParticipantReportingModeHistory);
                
            } else{
                   clinicModeHistItems.add(studyParticipantReportingModeHistory);
            }
        }

        mv.addObject("homeHistItems", homeModeHistItems);
        mv.addObject("clinicHistItems", clinicModeHistItems);

       
        return mv;
    }


    /**
     * Instantiates a new adds the participant report mode history controller.
     */
    public ParticipantReportingModeHistoryController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required    
    public void setStudyParticipantAssignmentRepository(StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }
   
}