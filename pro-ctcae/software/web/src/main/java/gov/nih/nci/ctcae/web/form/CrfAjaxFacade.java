package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    private GenericRepository genericRepository;


    /**
     * Search crf.
     *
     * @param studyId the id
     * @return the string
     */
    public List<CRF> searchCrf(Integer studyId) throws Exception {
        CRFQuery crfQuery = new CRFQuery();
        if (studyId != null) {
            crfQuery.filterByStudyId(studyId);
            crfQuery.filterByNullNextVersionId();
            crfQuery.filterByHidden(false);
        }
        List<CRF> crfs = (List<CRF>) crfRepository.find(crfQuery);
        return crfs;
    }

    public List<CRF> searchCrfs(String[] searchStrings, Integer startIndex, Integer results, String sortField, String direction) {
        CRFQuery crfQuery = new CRFQuery(true, false);
        crfQuery.setFirstResult(startIndex);
        crfQuery.setMaximumResults(results);
        crfQuery.setSortBy("o." + sortField);
        crfQuery.setSortDirection(direction);

        if (searchStrings != null) {
            int index = 0;
            for (String searchString : searchStrings) {
                crfQuery.filterByAll(searchString, "" + index, false);
                index++;
            }
        }
        return (List<CRF>) crfRepository.find(crfQuery);
    }

    public Long resultCount(String[] searchTexts) {
        CRFQuery crfQuery = new CRFQuery(true);
        if (searchTexts != null) {
            int index = 0;
            for (String searchText : searchTexts) {
                if (!StringUtils.isBlank(searchText)) {
                    crfQuery.filterByAll(searchText, "" + index, false);
                }
            }
        }

        return crfRepository.findWithCount(crfQuery);
    }

    /**
     * Gets the objects.
     *
     * @param id the id
     * @return the objects
     */
    public List<CRF> getReducedCrfs(Integer id) throws Exception {
        List<CRF> crfs = searchCrf(id);
        List<CRF> releasedCrfs = new ArrayList();
        for (CRF crf : crfs) {
            if (crf.getStatus().equals(CrfStatus.RELEASED) && !crf.isHidden()) {
                releasedCrfs.add(crf);
            }
        }
        return ObjectTools.reduceAll(releasedCrfs, "id", "title");
    }

    public List<CRF> getHiddenCrfs() throws Exception {
        CRFQuery crfQuery = new CRFQuery();
        crfQuery.filterByNullNextVersionId();
        crfQuery.filterByHidden(true);

        List<CRF> crfs = (List<CRF>) crfRepository.find(crfQuery);
        return crfs;
    }

    public List<ProCtcTerm> getSymptomsForCrf(Integer id) {
        CRF crf = crfRepository.findById(id);
        Set<ProCtcTerm> terms = new TreeSet(new ProCtcTermComparator());
        for (CrfPageItem i : crf.getAllCrfPageItems()) {
            terms.add(i.getProCtcQuestion().getProCtcTerm());
        }
        return ObjectTools.reduceAll(new ArrayList(terms), "id", "term");
    }

    public List<String> getAttributesForSymptom(Integer id) {
        ProCtcTerm term = genericRepository.findById(ProCtcTerm.class, id);
        ArrayList<String> attributes = new ArrayList();
        for (ProCtcQuestion proCtcQuestion : term.getProCtcQuestions()) {
            attributes.add(proCtcQuestion.getProCtcQuestionType().getDisplayName());
        }
        return attributes;
    }


    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
