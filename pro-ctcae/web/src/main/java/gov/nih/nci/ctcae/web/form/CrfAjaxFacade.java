package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Nov 6, 2008
 */
public class CrfAjaxFacade {

    private CRFRepository crfRepository;


    public String searchCrf(Map parameterMap, Integer id, HttpServletRequest request) {

        List<CRF> crfs = getObjects(id);
        CrfTableModel crfTableModel = new CrfTableModel();
        String table = crfTableModel.buildCrfTable(parameterMap, crfs, request);
        return table;
    }

    private List<CRF> getObjects(Integer id) {
        CRFQuery crfQuery = new CRFQuery();

        if (id != null) {
            crfQuery.filterByStudyId(id);
            crfQuery.filterByNullNextVersionId();
        }
        List<CRF> crfs = (List<CRF>) crfRepository.find(crfQuery);
        return crfs;
    }


    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
