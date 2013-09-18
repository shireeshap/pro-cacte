package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.csv.loader.UpdateProCtcTermsImporterV4;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author mehul gulati
 * Date: Jun 29, 2010
 */

public class UpdateProCtcTermController extends AbstractController {

    private ProCtcRepository proCtcRepository;
    private UpdateProCtcTermsImporterV4 updateProCtcTermsImporterV4;


	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProCtcQuery query = new ProCtcQuery();
        query.filterByProCtcVersion("4.0");
        ProCtc proCtc = proCtcRepository.findSingle(query);
        updateProCtcTermsImporterV4.updateProCtcTerms(proCtc);
        System.out.println("ProCtcTerms Updated");
        
        return new ModelAndView("proCtcTermsLoaded");
    }


    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        this.proCtcRepository = proCtcRepository;
    }


	public void setUpdateProCtcTermsImporterV4(
			UpdateProCtcTermsImporterV4 updateProCtcTermsImporterV4) {
		this.updateProCtcTermsImporterV4 = updateProCtcTermsImporterV4;
	}

}
