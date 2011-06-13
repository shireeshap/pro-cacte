package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.csv.loader.UpdateMeddraSpanishLoader;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mehul
 *         Date: 6/1/11
 */
public class UpdateMeddraTermSpanishController extends AbstractController {

    MeddraLoaderRepository meddraLoaderRepository;
    UpdateMeddraSpanishLoader updateMeddraSpanishLoader;
    GenericRepository genericRepository;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        updateMeddraSpanishLoader.setMeddraLoaderRepository(meddraLoaderRepository);
        updateMeddraSpanishLoader.setGenericRepository(genericRepository);
        updateMeddraSpanishLoader.updateMeddraTerms();
        System.out.println("Spanish Meddra terms updated");
        return new ModelAndView("proCtcTermsLoaded");
    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

    public void setUpdateMeddraSpanishLoader(UpdateMeddraSpanishLoader updateMeddraSpanishLoader) {
        this.updateMeddraSpanishLoader = updateMeddraSpanishLoader;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
