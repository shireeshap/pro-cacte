package gov.nih.nci.ctcae.web;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.csv.loader.CsvImporter;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Jan 7, 2010
 * Time: 2:45:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadProCtcTermController extends AbstractController {

    ProCtcTermRepository proCtcTermRepository;
    CtcTermRepository ctcTermRepository;
    ProCtcRepository proCtcRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
            Collection<ProCtcTerm> proCtcTerms = proCtcTermRepository.find(proCtcTermQuery);
            logger.debug("ProCtcTerms Found = " + proCtcTerms.size());
            if (proCtcTerms.size() == 0) {
                logger.debug("Loading ProCtcTerms");
                CsvImporter csvImporter = new CsvImporter();
                csvImporter.setCtcTermRepository(ctcTermRepository);
                ProCtc proctc = csvImporter.readCsv();
                proCtcRepository.save(proctc);
                logger.debug("ProCtcTerms Loaded");
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

    @Required
    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    @Required
    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        this.proCtcRepository = proCtcRepository;
    }
}
