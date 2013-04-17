package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.reports.SymptomSummaryWorstResponsesQuery;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Harsh Agarwal
 * @since Mar 18, 2009
 */
public class ParticipantLevelGraphicalReportTest extends ParticipantLevelReportIntegrationTest {

	private ParticipantLevelGraphicalReportController controller;
	private static Integer symptomId;
	TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results;
	HashMap<Question, ArrayList<ValidValue>> questions;
	
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new ParticipantLevelGraphicalReportController();
		controller.setGenericRepository(genericRepository);
        controller.setProCtcTermRepository(proCtcTermRepository);
        buildResult();
        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDates", new ArrayList<String>());
        request.getSession().setAttribute("baselineDate", "");
	}
	
    public void testHandleRequestInternal() throws Exception {
        int size = crf.getAllCrfPageItems().size();
        if(size > 0){
        	symptomId = crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getProCtcTerm().getId();
        	request.setParameter("symptomId", "P_"+symptomId.toString());

        	ParticipantLevelChartGenerator participantLevelChartGenerator = new ParticipantLevelChartGenerator();//.createMock(ParticipantLevelChartGenerator.class);
        	ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        	
        	assertEquals("reports/participantreportcharts", modelAndView.getViewName());
            HashSet<String> allAttributes = (HashSet<String>) modelAndView.getModelMap().get("allAttributes");
            assertTrue(allAttributes.contains(crf.getAllCrfPageItems().get(size - 1).getProCtcQuestion().getQuestionType().getDisplayName()));
            assertEquals("P_"+symptomId.toString(), modelAndView.getModelMap().get("symptom"));
        } else {
        	fail("Insufficient data in DB while running this test");
        }
    }

    public void testQuery() {
        SymptomSummaryWorstResponsesQuery query = new SymptomSummaryWorstResponsesQuery();
        query.filterByCrf(StudyTestHelper.getDefaultStudy().getCrfs().get(0).getId());
        List<Persistable> l = genericRepository.find(query);
        assertTrue(l.size() > 0);
    }
    private void buildResult(){
    	results = new TreeMap<String[], HashMap<Question,ArrayList<ValidValue>>>(new MyArraySorter());
    	questions = new HashMap<Question, ArrayList<ValidValue>>();
    	List values = new ArrayList<ValidValue>();
    	values.add(new ProCtcValidValue());
    	ProCtcTerm proCtcTerm = crf.getAllCrfPageItems().get(11).getProCtcQuestion().getProCtcTerm();
    	for(ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()){
    		questions.put(proCtcQuestion, (ArrayList<ValidValue>) values);
    	}
    	String[] terms = new String[2];
    	terms[0] = proCtcTerm.getProCtcTermVocab().getTermEnglish();
    	terms[1] = proCtcTerm.getProCtcTermVocab().getTermEnglish();
    	
    	results.put(terms, questions);
    }
    
    private class MyArraySorter implements Comparator {
        public int compare(Object o1, Object o2) {
            if (o1 != null & o2 != null) {
                String[] o1Arr = (String[]) o1;
                String[] o2Arr = (String[]) o2;
                return o1Arr[1].compareTo(o2Arr[1]);
            }
            return 0;
        }
    }
}