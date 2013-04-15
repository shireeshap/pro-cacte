package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author AmeyS
 * Testcases for GetParticipantCrfItemsController.java
 */
public class GetParticipantCrfItemsControllerTest extends AbstractWebTestCase{
	private static GetParticipantCrfItemsController controller;
	private static Participant participant;
	Integer participantId;
	Integer displayOrder;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new GetParticipantCrfItemsController();
		controller.setGenericRepository(genericRepository);
		participant = ParticipantTestHelper.getDefaultParticipant();
		participantId = ParticipantTestHelper.getDefaultParticipant().getId();
		displayOrder = 3;
		request.setParameter("pid", participantId.toString());
		request.setParameter("grade", displayOrder.toString());
		request.setParameter("crf", getCrfIdForDefaultParticipant().toString());
		request.setParameter("studySite", getStudyStiteForDefaultParticipant().toString());
	}
	
	@SuppressWarnings("unchecked")
	public void testHandleRequestInternal() throws Exception{
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		
		assertEquals("reports/reportDetails_participantItems", modelAndView.getViewName());
		assertNotNull(((List<StudyParticipantCrfItem>) modelAndView.getModelMap().get("results")));
		assertEquals(participantId, modelAndView.getModelMap().get("pid"));
	}
	
	private Integer getCrfIdForDefaultParticipant(){
		return participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getCrf().getId();
	}
	
	private Integer getStudyStiteForDefaultParticipant(){
		return participant.getStudyParticipantAssignments().get(0).getStudySite().getId();
	}

}
