package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.csv.loader.ProctcaeGradeMappingsLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author VinayG
 */
public class LoadProctcaeGradeMappingsController extends AbstractController {

	ProctcaeGradeMappingsLoader proctcaeGradeMappingsLoader;

	Log log = LogFactory.getLog(LoadProctcaeGradeMappingsController.class);

	@Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        proctcaeGradeMappingsLoader.loadProctcaeGradeMappings();
        return new ModelAndView("proctcaeGradeMappingsLoaded");
    }
	

    public void setProctcaeGradeMappingsLoader(
			ProctcaeGradeMappingsLoader proctcaeGradeMappingsLoader) {
		this.proctcaeGradeMappingsLoader = proctcaeGradeMappingsLoader;
	}

}
