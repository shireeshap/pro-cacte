package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsDetailsQuery;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public class ParticipantAddedQuestionsReportDetailsController extends ParticipantAddedQuestionsReportResultsController {


    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantAddedQuestionsDetails");

        ParticipantAddedQuestionsDetailsQuery query = new ParticipantAddedQuestionsDetailsQuery();
        parseRequestParametersAndFormQuery(request, query);

        List l = genericRepository.find(query);
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
            s[0] = DateUtils.format(sq.getStudyParticipantCrfSchedule().getStartDate());
            s[1] = sq.getProCtcValidValue() == null ? "" : sq.getProCtcValidValue().getValue(SupportedLanguageEnum.ENGLISH);
            ls.add(s);
        }


        modelAndView.addObject("results", results);
        modelAndView.addObject("symptom", request.getParameter("symptom"));
        return modelAndView;
    }
}