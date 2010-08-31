package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.csv.loader.ProCtcTermsImporterV4;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Aug 27, 2010
 * Time: 4:48:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteProCtcTermController extends AbstractController {
    ProCtcTermRepository proCtcTermRepository;

       protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String proCtcTerm = request.getParameter("proCtcTerm");
            ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
            proCtcTermQuery.filterByTerm(proCtcTerm);
            List<ProCtcTerm> proCtcTerms = (List)proCtcTermRepository.find(proCtcTermQuery);
            if (proCtcTerms.size() > 0) {
                proCtcTermRepository.delete(proCtcTerms.get(0));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new ModelAndView("proCtcTermsLoaded");
    }

    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }




}
