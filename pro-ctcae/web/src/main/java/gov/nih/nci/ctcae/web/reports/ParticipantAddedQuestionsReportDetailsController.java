package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.query.SymptomOverTimeReportQuery;
import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsReportQuery;
import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsDetailsQuery;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfAddedQuestionRepository;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
            Object[] o = (Object[]) obj;

            StudyParticipantCrfAddedQuestion q = (StudyParticipantCrfAddedQuestion) o[0];
            StudyParticipantCrfScheduleAddedQuestion sq = (StudyParticipantCrfScheduleAddedQuestion) o[1];

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
            s[1] = sq.getProCtcValidValue() == null ? "" : sq.getProCtcValidValue().getValue();
            ls.add(s);
        }


        modelAndView.addObject("results", results);
        modelAndView.addObject("symptom", request.getParameter("symptom"));
        return modelAndView;
    }
}