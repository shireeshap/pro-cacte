package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

/**
 * @author Harsh Agarwal
 * @since Nov 25, 2008
 */
public class SubmitFormControllerTest extends AbstractWebTestCase {
    SubmitFormController controller = null;
    List<ListOrderedMap> l;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        controller = new SubmitFormController();
        controller.setGenericRepository(genericRepository);
        controller.setWebControllerValidator(new WebControllerValidatorImpl());
        l = jdbcTemplate.queryForList("select s.id from sp_crf_schedules s");
    }

    public void testController() throws Exception {

        request.setParameter("id", l.get(0).getValue(0).toString());
        request.setMethod("GET");
        ModelAndView mv = controller.handleRequest(request, response);
        SubmitFormCommand sfc = (SubmitFormCommand) mv.getModel().get("command");
        assertNotNull(sfc);

        assertEquals(10, sfc.getTotalPages());
        assertEquals(11, sfc.getParticipantAddedQuestionIndex());
        assertEquals(1, sfc.getCurrentPageIndex());
        assertNotNull(sfc.getDisplayRules());
        assertEquals(controller.getFormView(), mv.getViewName());
        assertNull(sfc.getSortedSymptoms());

        moveNext(sfc.getCurrentPageIndex());
        assertEquals(controller.getFormView(), mv.getViewName());

        sfc.setCurrentPageIndex(sfc.getTotalPages());

        mv = moveNext(sfc.getCurrentPageIndex());
        assertTrue(mv.getView() instanceof RedirectView);
        assertTrue(((RedirectView) mv.getView()).getUrl().equals("addquestion"));
//
//        AddQuestionByParticipantController controller1 = new AddQuestionByParticipantController();
//        request.setMethod("POST");
//        controller1.handleRequest(request,response);



    }

    private ModelAndView moveNext(int currentIndex) throws Exception {
        request.setMethod("POST");
        request.setParameter("direction", "continue");
        ModelAndView mv = controller.handleRequest(request, response);
        SubmitFormCommand sfc = (SubmitFormCommand) mv.getModel().get("command");
        assertEquals(currentIndex + 1, sfc.getCurrentPageIndex());
        assertNotNull(sfc.getDisplayRules());
        assertTrue(sfc.getStudyParticipantCrfSchedule().getStatus().equals(CrfStatus.INPROGRESS));
        return mv;
    }


}