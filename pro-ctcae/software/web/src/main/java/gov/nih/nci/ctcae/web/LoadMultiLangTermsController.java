package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.csv.loader.ProTermsMultiLangImporterV4;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mehul
 * Date: 5/6/11
 */
public class LoadMultiLangTermsController extends AbstractController {

    ProCtcQuestionRepository proCtcQuestionRepository;
    ProCtcTermRepository proCtcTermRepository;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        ProTermsMultiLangImporterV4 proTermsMultiLangImporter = new ProTermsMultiLangImporterV4();
        proTermsMultiLangImporter.setProCtcQuestionRepository(proCtcQuestionRepository);
        proTermsMultiLangImporter.setProCtcTermRepository(proCtcTermRepository);
        proTermsMultiLangImporter.updateMultiLangProTerms();
        System.out.println("Multi language terms updated");

        return new ModelAndView("proCtcTermsLoaded");  //To change body of implemented methods use File | Settings | File Templates.

    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }
}
