package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.csv.loader.UpdateProctcaeVerbatimLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Amey
 * UpdateSpcrfGradesVerbatimController class.
 * Used to populate proctcae_verbatim to studyParticipantCrfGrade entries created using mapping document version v1.0
 */

public class UpdateSpcrfGradesVerbatimController extends AbstractController {
	UpdateProctcaeVerbatimLoader updateProctcaeVerbatimLoader;
	Log log = LogFactory.getLog(UpdateSpcrfGradesVerbatimController.class);

	@Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		updateProctcaeVerbatimLoader.updateProctcaeVerbatim();
        return new ModelAndView("proctcaeVerbatimUpdated");
    }
	

    public void setUpdateProctcaeVerbatimLoader(UpdateProctcaeVerbatimLoader updateProctcaeVerbatimLoader) {
		this.updateProctcaeVerbatimLoader = updateProctcaeVerbatimLoader;
	}
}