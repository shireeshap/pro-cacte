package gov.nih.nci.ctcae.web.meddra;


import gov.nih.nci.ctcae.core.dao.MeddraVersionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ImportMeddraController extends ParameterizableViewController {

    private static Log log = LogFactory.getLog(ImportMeddraController.class);

    private MeddraVersionDao meddraVersionDao;

    public ImportMeddraController() {
        setViewName("meddra/meddra_import");
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request,
                    HttpServletResponse response) throws Exception {

        setViewName("meddra/meddra_import");
        ModelAndView mav = new ModelAndView("meddra/meddra_import");
        mav.addObject("meddraVersions", meddraVersionDao.getAll());
        log.debug("modelAndView" + mav.getViewName());

        return mav;
    }

    public void setMeddraVersionDao(MeddraVersionDao meddraVersionDao) {
        this.meddraVersionDao = meddraVersionDao;
    }
}
