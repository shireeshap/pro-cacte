package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.JstlView;

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
        }
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
