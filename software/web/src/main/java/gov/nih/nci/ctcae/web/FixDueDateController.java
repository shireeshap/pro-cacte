package gov.nih.nci.ctcae.web;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Jan 21, 2011
 * Time: 7:53:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixDueDateController extends AbstractController {

    private ProductionDataCorrector productionDataCorrector;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String updateStatus = " success.";

        try{
            productionDataCorrector.correctDueDateOfScheduleInProdDb();
         } catch (Exception ex) {
            ex.printStackTrace();
            updateStatus = " failed : " + ex.getMessage();
            throw ex;
        }
        ModelAndView mv = new ModelAndView("dueDateUpdated");
        mv.addObject("updateStatus",updateStatus);
        return mv;
    }

    public ProductionDataCorrector getProductionDataCorrector() {
        return productionDataCorrector;
    }

    public void setProductionDataCorrector(ProductionDataCorrector productionDataCorrector) {
        this.productionDataCorrector = productionDataCorrector;
    }
}
