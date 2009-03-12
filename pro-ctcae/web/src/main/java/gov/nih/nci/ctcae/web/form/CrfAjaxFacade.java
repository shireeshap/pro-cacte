package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//
/**
 * The Class CrfAjaxFacade.
 *
 * @author Mehul Gulati
 *         Date: Nov 6, 2008
 */
public class CrfAjaxFacade {

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;


    /**
     * Search crf.
     *
     * @param parameterMap the parameter map
     * @param id           the id
     * @param request      the request
     * @return the string
     */
    public String searchCrf(Map parameterMap, Integer id, HttpServletRequest request) {

        List<CRF> crfs = getObjects(id);
        CrfTableModel crfTableModel = new CrfTableModel();
        String table = crfTableModel.buildCrfTable(parameterMap, crfs, request);
        return table;
    }


    /**
     * Gets the objects.
     *
     * @param id the id
     * @return the objects
     */
    public List<CRF> getObjects(Integer id) {
        CRFQuery crfQuery = new CRFQuery();

        if (id != null) {
            crfQuery.filterByStudyId(id);
            crfQuery.filterByNullNextVersionId();
        }
        List<CRF> crfs = (List<CRF>) crfRepository.find(crfQuery);
        return crfs;
    }

    /**
     * Gets the objects.
     *
     * @param id the id
     * @return the objects
     */
    public List<CRF> getReducedCrfs(Integer id) {
        List<CRF> crfs = getObjects(id);
        return ObjectTools.reduceAll(crfs, "id", "title");
    }


    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
