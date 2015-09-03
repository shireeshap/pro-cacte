package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author AmeyS
 * Testcases for ParticipantLevelReportPdfController
 *
 */
public class ParticipantLevelReportPdfControllerTest extends AbstractWebTestCase{
	
	private static String reportType = "worstSymptom";
	private ParticipantLevelReportPdfController controller;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new ParticipantLevelReportPdfController();
	}
	
	public void testHandleRequestInternal_worstSymptom() throws Exception{
		request.setParameter("rt", reportType);
		
		ModelAndView modelAndView =  controller.handleRequestInternal(request, response);
		assertTrue(modelAndView.getView() instanceof ParticipantLevelWorstSymptomReportPdfView);
	}
	
	public void testHandleRequestInternal() throws Exception{
		request.setParameter("rt", "");
		
		ModelAndView modelAndView =  controller.handleRequestInternal(request, response);
		assertTrue(modelAndView.getView() instanceof ParticipantLevelReportPdfView);
	}
}
