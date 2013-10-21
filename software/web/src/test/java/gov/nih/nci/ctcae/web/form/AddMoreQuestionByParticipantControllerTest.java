package gov.nih.nci.ctcae.web.form;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author AmeyS 
 * TestCases for AddMoreQuestionByParticipantController.java
 * Includes testcases to test the forward and backward flow, when participant has finished answering all the survey questions 
 * and is now rendered a page to add more questions in the survey if needed.
 */
public class AddMoreQuestionByParticipantControllerTest extends WebTestCase {

    private AddMoreQuestionByParticipantController controller;
    private SubmitFormCommand command;
    private GenericRepository genericRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    private ProCtcTermRepository proCtcTermRepository;
    private MeddraRepository meddraRepository;
    private static String CONTINUE = "continue";
    private static String crfTitle = "Test CRF";
    private static String BACK = "back";
    private static StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    private static StudyParticipantCrf studyParticipantCrf;
    private static BindException errors;
    private CRF crf;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddMoreQuestionByParticipantController();
        genericRepository = registerMockFor(GenericRepository.class);
        studyParticipantCrfScheduleRepository = registerMockFor(StudyParticipantCrfScheduleRepository.class);
        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        meddraRepository = registerMockFor(MeddraRepository.class);
        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setId(1);
        studyParticipantCrf = new StudyParticipantCrf();
        crf = new CRF();
        crf.setTitle(crfTitle);
        crf.setStatus(CrfStatus.RELEASED);
        crf.setCrfVersion("1.0");
        studyParticipantCrf.setCrf(crf);
        studyParticipantCrfSchedule.setStudyParticipantCrf(studyParticipantCrf);
        errors = new BindException(new Object(), "SubmitFormCommandErrorObject");
    }
    
    public void testOnSubmitWithForwardFlow() throws Exception{
    	expect(studyParticipantCrfScheduleRepository.findById(1)).andReturn(studyParticipantCrfSchedule).anyTimes();
        expect(genericRepository.findById(StudyParticipantCrfSchedule.class, 1)).andReturn(studyParticipantCrfSchedule).anyTimes();
        expect(genericRepository.save(isA(StudyParticipantCrfSchedule.class))).andReturn(studyParticipantCrfSchedule).anyTimes();
        
    	replayMocks();
    	command = new SubmitFormCommand("1", genericRepository, studyParticipantCrfScheduleRepository, proCtcTermRepository, meddraRepository);
    	command.setCurrentPageIndex("5");
    	command.setReviewPageIndex(7);
    	command.setDirection(CONTINUE);
		ModelAndView modelAndView = controller.onSubmit(request, response, command, errors);
        verifyMocks();
        
        assertEquals("expect scheduleId to be 1", 1, request.getSession().getAttribute("id"));
        assertEquals("submit", ((RedirectView)modelAndView.getView()).getUrl());
        command = (SubmitFormCommand)request.getSession().getAttribute(SubmitFormController.class.getName()+".FORM."+"command");
        assertEquals("expect currentPageIndex to be incremented to 6", 6, command.getNewPageIndex());
    }
    
  public void testOnSubmitWithBackwardFlow() throws Exception{
    	
    	expect(studyParticipantCrfScheduleRepository.findById(1)).andReturn(studyParticipantCrfSchedule).anyTimes();
        expect(genericRepository.findById(StudyParticipantCrfSchedule.class, 1)).andReturn(studyParticipantCrfSchedule).anyTimes();
        expect(genericRepository.save(isA(StudyParticipantCrfSchedule.class))).andReturn(studyParticipantCrfSchedule).anyTimes();
        
    	replayMocks();
    	command = new SubmitFormCommand("1", genericRepository, studyParticipantCrfScheduleRepository, proCtcTermRepository, meddraRepository);
    	command.setCurrentPageIndex("5");
    	command.setReviewPageIndex(7);
    	command.setDirection(BACK);
		ModelAndView modelAndView = controller.onSubmit(request, response, command, errors);
        verifyMocks();
        
        assertEquals("expect scheduleId to be 1", 1, request.getSession().getAttribute("id"));
        assertEquals("submit", ((RedirectView)modelAndView.getView()).getUrl());
        command = (SubmitFormCommand)request.getSession().getAttribute(SubmitFormController.class.getName()+".FORM."+"command");
        assertEquals("expect currentPageIndex to be incremented to 3", 4, command.getNewPageIndex());
  }
  
  public void testFormBackingObject() throws Exception{
	  expect(studyParticipantCrfScheduleRepository.findById(1)).andReturn(studyParticipantCrfSchedule).anyTimes();
	    expect(genericRepository.findById(StudyParticipantCrfSchedule.class, 1)).andReturn(studyParticipantCrfSchedule).anyTimes();
	    expect(genericRepository.save(isA(StudyParticipantCrfSchedule.class))).andReturn(studyParticipantCrfSchedule).anyTimes();
	      
	  	replayMocks();
	  	command = (SubmitFormCommand)controller.formBackingObject(request);
		verifyMocks();
		assertNull("expect formBackingObject to be null", command);
	      
	  	command = new SubmitFormCommand("1", genericRepository, studyParticipantCrfScheduleRepository, proCtcTermRepository, meddraRepository);
	    request.getSession().setAttribute(SubmitFormController.class.getName()+".FORM."+"command", command);
	    SubmitFormCommand formBackingObject = (SubmitFormCommand)controller.formBackingObject(request);
		verifyMocks();
		      
		assertEquals("expect formBackingObject to be the command object", formBackingObject, command);
  }
}