package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//

/**
 * The Class AddCrfScheduleController.
 *
 * @author Harsh Agarwal
 * @created Nov 6, 2008
 * Controller class called via Ajax. Used to add / delete schedule based on the date parameter
 */
public class AddFormScheduleController extends AbstractController {


    GenericRepository genericRepository;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ParticipantCommand participantCommand = ParticipantControllerUtils.getParticipantCommand(request);
        ParticipantSchedule participantSchedule = participantCommand.getParticipantSchedules().get(Integer.parseInt(request.getParameter("index")));
        Calendar c = new GregorianCalendar();
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());
        c.set(Calendar.DATE, Integer.parseInt(request.getParameter("date")));
      
        ModelAndView mv = new ModelAndView("participant/addSchedule");
        String sids = request.getParameter("sids");
        List<String> listExistingCrfs = new ArrayList<String>();
        if (sids != null) {
            String[] sidArr = sids.split("_");
            listExistingCrfs = Arrays.asList(sidArr);
        }
        LinkedHashMap<CRF, Boolean> crfListMap = new LinkedHashMap<CRF, Boolean>();
        CRF crf;
        boolean crfExists;
        boolean cExists;
        for (StudyParticipantCrf studyParticipantCrf : participantCommand.getSelectedStudyParticipantAssignment().getStudyParticipantCrfs()) {
            crf = studyParticipantCrf.getCrf();
            crfExists = false;
            cExists = false;
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            	//check to ensure crf isnt already scheduled for the selected date.
                if (listExistingCrfs.contains(studyParticipantCrfSchedule.getId().toString())) {
                    crfExists = true;
                    break;
                }
            }
            if ((!hasChildCrf(crf) || hasDraftedChildCrf(crf)) && !crf.isHidden()) {
                for (CRF cr : crfListMap.keySet()) {
                    if (cr.equals(studyParticipantCrf.getCrf()) && crfListMap.get(cr)) {
                        cExists = true;
                    }
                }
                if (!cExists) {
                	//ensure crf schedule date is greater than crf effective date and participant start date.
                	//not checking for crf effective end date.
                	if(c.getTime().after(crf.getEffectiveStartDate()) && c.getTime().after(participantCommand.getParticipant().getStudyParticipantAssignments().get(0).getStudyStartDate())){
                		crfListMap.put(crf, crfExists);
                	}
                }
            }
        }

        mv.addObject("crfs", crfListMap);
        if (crfListMap.size() > 0) {
            mv.addObject("firstCrf", (CRF) crfListMap.keySet().iterator().next());
        } else {
            mv.addObject("firstCrf", null);
        }

        mv.addObject("day", request.getParameter("date"));
        mv.addObject("index", request.getParameter("index"));
        mv.addObject("date", DateUtils.format(c.getTime()));
        mv.addObject("participant", participantCommand.getParticipant());
        
        String participantId = request.getParameter(ParticipantController.PARTICIPANT_ID);
        if(StringUtils.isNotEmpty(participantId)) {
        	mv.addObject("pid", participantId);
        }
        
        return mv;
    }
    
    public boolean hasChildCrf(CRF crf){
    	if(crf.getChildCrf() != null){
    		return true;
    	}
    	return false;
    }
    
    public boolean hasDraftedChildCrf(CRF crf){
    	if(hasChildCrf(crf)){
    		CRF childCrf = crf.getChildCrf();
    		if(hasChildCrf(childCrf)){
    			return false;
    		} else {
    			if(CrfStatus.DRAFT.equals(childCrf.getStatus())){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public AddFormScheduleController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}