package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Mehul Gulati
 *         Date: Jul 30, 2009
 */
public class PrintFormController extends AbstractController {
    private CRFRepository crfRepository;
    private static String US_ENGLISH = "en";
	private static String US_SPANISH = "es";
	private static String EQ_5D_SPANISH_DOCUMENT = "/proctcae/images/EQ-5D_5L_US_Spanish.doc";
	private static String EQ_5D_ENGLISH_DOCUMENT = "/proctcae/images/EQ-5D-5L_US_English.doc";
	
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    	Integer id = Integer.parseInt(request.getParameter("crfId"));
        CRF crf = crfRepository.findById(id);
        // For EQ-5L surveys, provide link for empty EQ-5D paper form, for instant download.
        if(crf.isEq5d()){
        	String language = request.getParameter("lang");
            if (StringUtils.isEmpty(language)) {
                language = US_ENGLISH;
            }
            if(crf.isEq5d5L()){
            	if(US_SPANISH.equals(language)){
            		return new ModelAndView(new RedirectView(EQ_5D_SPANISH_DOCUMENT));
            	} else {
            		return new ModelAndView(new RedirectView(EQ_5D_ENGLISH_DOCUMENT));
            	}
            }
        }
        //For non EQ-5D surveys, generate a pdf view of the form.
    	PrintFormPdfView printSchedulePdfView = new PrintFormPdfView();
    	printSchedulePdfView.setCRFRepository(crfRepository);
        return new ModelAndView(printSchedulePdfView);
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
