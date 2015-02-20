package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsDetailsQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class ParticipantAddedQuestionsReportDetailsController extends ParticipantAddedQuestionsReportResultsController {
	private static String NOT_ANSWERED = "Not answered"; 

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantAddedQuestionsDetails");

        boolean isMeddraAddedQuestionQuery = false;
        ParticipantAddedQuestionsDetailsQuery query = new ParticipantAddedQuestionsDetailsQuery();
        parseRequestParametersAndFormQuery(request, query, isMeddraAddedQuestionQuery);
        List l = genericRepository.find(query);
        
        if(l.isEmpty()){
        	isMeddraAddedQuestionQuery = true;
        	query = new ParticipantAddedQuestionsDetailsQuery();
            parseRequestParametersAndFormQuery(request, query, isMeddraAddedQuestionQuery);
            l = genericRepository.find(query);
        }
        
        HashMap<Participant, List<String[]>> results = new HashMap<Participant, List<String[]>>();
        for (Object obj : l) {
            StudyParticipantCrfScheduleAddedQuestion sq = (StudyParticipantCrfScheduleAddedQuestion) obj;

            Participant p = sq.getStudyParticipantCrfSchedule().getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
            List<String[]> ls;
            if (results.containsKey(p)) {
                ls = results.get(p);
            } else {
                ls = new ArrayList<String[]>();
                results.put(p, ls);
            }
            String[] s = new String[2];
            s[0] = (sq.getReponseDate() != null? DateUtils.format(sq.getReponseDate()) : DateUtils.format(sq.getStudyParticipantCrfSchedule().getStartDate()));
            String value;
            if(sq.getProCtcQuestion() != null){
            	value = sq.getProCtcValidValue() == null ? "" : sq.getProCtcValidValue().getValue(SupportedLanguageEnum.ENGLISH);
            	s[1] = sq.getProCtcQuestion().getProCtcQuestionType().getDisplayName() + ": " + (value.isEmpty()? NOT_ANSWERED : value);
            } else {
            	value = sq.getMeddraValidValue() == null ? "" : sq.getMeddraValidValue().getValue(SupportedLanguageEnum.ENGLISH);
            	s[1] = sq.getMeddraQuestion().getQuestionType().getDisplayName() + ": " + (value.isEmpty()? NOT_ANSWERED : value);
            }
            
            ls.add(s);
        }


        modelAndView.addObject("results", results);
        modelAndView.addObject("symptom", request.getParameter("symptom"));
        return modelAndView;
    }
}