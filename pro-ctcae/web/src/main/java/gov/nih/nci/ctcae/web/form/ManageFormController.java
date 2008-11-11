package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class ManageFormController extends AbstractController {

    private FinderRepository finderRepository;


    public ManageFormController() {
        setSupportedMethods(new String[]{"GET"});
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {


        ModelAndView modelAndView = new ModelAndView("form/manageForm");
        String studyCrfId = request.getParameter("studyCrfId");
        if (!StringUtils.isBlank(studyCrfId)) {
            StudyCrf studyCrf = finderRepository.findById(StudyCrf.class, Integer.parseInt(studyCrfId));
            if (studyCrf != null) {
                Study study = studyCrf.getStudy();
                modelAndView.getModel().put("study", study);
            }
        }
        return modelAndView;
    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

}
