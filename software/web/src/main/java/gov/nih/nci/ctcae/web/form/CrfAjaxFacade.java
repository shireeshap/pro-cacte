package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Class CrfAjaxFacade.
 *
 * @author Mehul Gulati
 *         Date: Nov 6, 2008
 */
public class CrfAjaxFacade {

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
//            crfQuery.filterByHidden(false);
        }
        List<CRF> crfs = (List<CRF>) crfRepository.find(crfQuery);
        return crfs;
    }

    public List<CRF> searchCrfs(String[] searchStrings, Integer startIndex, Integer results, String sortField, String direction, Long totalRecords) {
        CRFQuery crfQuery = new CRFQuery(true, false);
        crfQuery.setFirstResult(startIndex);
        crfQuery.setMaximumResults(results);
        crfQuery.setSortBy("o." + sortField);
        crfQuery.setSortDirection(direction);
        crfQuery.filterByHidden(false);
        crfQuery.filterByNullNextVersionId();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (searchStrings != null) {
            int index = 0;
            for (String searchString : searchStrings) {
                crfQuery.filterByAll(searchString, "" + index);
                index++;
            }
        }

        List<CRF> crfs = (List<CRF>) crfRepository.find(crfQuery);
        if (!user.isAdmin()) {
            if (crfs.size() == results) {
                return crfs;
            } else {
                int i = 0;
                int index = startIndex;
                while (crfs.size() < results && crfs.size() < totalRecords-startIndex) {
                    index = results + index;
                    crfQuery.setFirstResult(index);
                    List<CRF> l = (List<CRF>) crfRepository.find(crfQuery);
                    if (l != null && l.size() > 0) {
                        crfs.addAll(l);
                    }
                    l.clear();
                    i++;
                }
                return crfs;
            }
        }
        return crfs;
    }

    public Long resultCount(String[] searchTexts) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = user.getUsername();
        CRFQuery crfQuery = new CRFQuery(true);
        crfQuery.filterByHidden(false);
        crfQuery.filterByNullNextVersionId();
        if (!user.isAdmin()) {
            crfQuery.filterByUsername(userName);
        }
        if (searchTexts != null) {
            int index = 0;
            for (String searchText : searchTexts) {
                if (!StringUtils.isBlank(searchText)) {
                    crfQuery.filterByAll(searchText, "" + index);
                    index++;
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
        List<CRF> releasedCrfs = new ArrayList<CRF>();
        for (CRF crf : crfs) {
            if (crf.getStatus().equals(CrfStatus.RELEASED)) {
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
        Set<ProCtcTerm> terms = new TreeSet<ProCtcTerm>(new ProCtcTermComparator());
        for (CrfPageItem i : crf.getAllCrfPageItems()) {
            terms.add(i.getProCtcQuestion().getProCtcTerm());
        }
        return ObjectTools.reduceAll(new ArrayList<ProCtcTerm>(terms), "id", "term");
    }

    public List<String> getAttributesForSymptom(Integer id) {
        ProCtcTerm term = genericRepository.findById(ProCtcTerm.class, id);
        ArrayList<String> attributes = new ArrayList<String>();
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
