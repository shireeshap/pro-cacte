package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
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
    CtcTermRepository ctcTermRepository;
    ProCtcRepository proCtcRepository;
    ProCtcTermRepository proCtcTermRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        UpdateProCtcTermsImporterV4 updateProCtcTerms = new UpdateProCtcTermsImporterV4();
        updateProCtcTerms.setProCtcQuestionRepository(proCtcQuestionRepository);
        updateProCtcTerms.setCtcTermRepository(ctcTermRepository);
        updateProCtcTerms.setProCtcTermRepository(proCtcTermRepository);
        updateProCtcTerms.setProCtcRepository(proCtcRepository);
        ProCtcQuery query = new ProCtcQuery();
        query.filterByProCtcVersion("4.0");
        ProCtc proCtc = proCtcRepository.findSingle(query);
        updateProCtcTerms.updateProCtcTerms(proCtc);
        System.out.println("ProCtcTerms Updated");
        
        return new ModelAndView("proCtcTermsLoaded");
    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }

    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        this.proCtcRepository = proCtcRepository;
    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }
}
