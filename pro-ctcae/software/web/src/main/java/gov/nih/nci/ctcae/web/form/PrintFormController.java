package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.participant.PrintSchedulePdfView;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 *         Date: Jul 30, 2009
 */
public class PrintFormController extends AbstractController {

    private CRFRepository crfRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    	PrintFormPdfView printSchedulePdfView = new PrintFormPdfView();
    	printSchedulePdfView.setCRFRepository(crfRepository);
         return new ModelAndView(printSchedulePdfView);
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }


}
