package gov.nih.nci.ctcae.web;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.csv.loader.UpdateProCtcTermsImporterV4;

/**
 * @author mehul gulati
 * Date: Jun 29, 2010
 */

public class UpdateProCtcTermController extends AbstractController {

    ProCtcQuestionRepository proCtcQuestionRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        UpdateProCtcTermsImporterV4 updateProCtcTerms = new UpdateProCtcTermsImporterV4();
        updateProCtcTerms.setProCtcQuestionRepository(proCtcQuestionRepository);
        updateProCtcTerms.updateProCtcTerms();
        System.out.println("ProCtcTerms Updated");
        
        return new ModelAndView("proCtcTermsLoaded");
    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
