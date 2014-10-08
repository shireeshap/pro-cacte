package gov.nih.nci.ctcae.web.form;

import static org.easymock.EasyMock.expect;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author AmeyS 
 * TestCase for AudioFileController.java
 */
public class AudioFileControllerTest extends WebTestCase {

    private AudioFileController controller;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    private static StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private static StudyParticipantCrf studyParticipantCrf;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AudioFileController();
        studyParticipantCrfScheduleRepository = registerMockFor(StudyParticipantCrfScheduleRepository.class);
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setId(1);
        studyParticipantCrfSchedule.setFilePath("testFileName");
        studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
    }
    
    public void testHandleRequestInternal() throws Exception{
    	request.setParameter("id", "1");
    	controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
    	controller.setContentType("testContentType");
    	expect(studyParticipantCrfScheduleRepository.findById(1)).andReturn(studyParticipantCrfSchedule);
    	
    	replayMocks();
    	ModelAndView modelAndView = controller.handleRequestInternal(request, response);
    	verifyMocks();
    	
    	assertNull(modelAndView);
    	assertEquals("<b>System Error while loading file. Please try again later.</b>", response.getContentAsString());
    }
}