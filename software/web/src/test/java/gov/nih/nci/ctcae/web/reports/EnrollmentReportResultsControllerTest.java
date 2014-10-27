package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author AmeyS
 * Test cases for EnrollmentReportResultsController
 */
public class EnrollmentReportResultsControllerTest extends AbstractWebTestCase{
	
	private static Integer studyId;
	private static EnrollmentReportResultsController controller;
	List<EnrollmentReportLine> enrollmentReport;
	int numberOfParticipant;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		studyId = StudyTestHelper.getDefaultStudy().getId();
		controller = new EnrollmentReportResultsController();
		request.setParameter("study", studyId.toString());
		controller.setStudyOrganizationRepository(studyOrganizationRepository);
		enrollmentReport = new ArrayList<EnrollmentReportLine>();
		numberOfParticipant = 0;
	}
	
	@SuppressWarnings("unchecked")
	public void testHandleRequestInternal() throws Exception{
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		assertEquals("reports/enrollmentReport", modelAndView.getViewName());
		
		List<EnrollmentReportLine> enrollmentReport = (List<EnrollmentReportLine>) modelAndView.getModelMap().get("results");
		assertEquals(2, enrollmentReport.size());
		
		for(int i = 0; i<enrollmentReport.size(); i++){
			numberOfParticipant += enrollmentReport.get(i).getNumberOfParticipants();
		}
		assertEquals(15, numberOfParticipant);
	}
}
