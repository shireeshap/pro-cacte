package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.csv.loader.UpdateMeddraLoader;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraLoaderRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mehul
 *         Date: 5/25/11
 */
public class UpdateMeddraTermsController extends AbstractController {

    MeddraLoaderRepository meddraLoaderRepository;
    UpdateMeddraLoader updateMeddraLoader;
    GenericRepository genericRepository;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

//        UpdateMeddraLoader updateMeddraLoader = new UpdateMeddraLoader();
        updateMeddraLoader.setMeddraLoaderRepository(meddraLoaderRepository);
        updateMeddraLoader.setGenericRepository(genericRepository);
        updateMeddraLoader.updateMeddraTerms();
        System.out.println("Meddra terms updated");
        return new ModelAndView("proCtcTermsLoaded");
    }

    public void setMeddraLoaderRepository(MeddraLoaderRepository meddraLoaderRepository) {
        this.meddraLoaderRepository = meddraLoaderRepository;
    }

    public void setUpdateMeddraLoader(UpdateMeddraLoader updateMeddraLoader) {
        this.updateMeddraLoader = updateMeddraLoader;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
