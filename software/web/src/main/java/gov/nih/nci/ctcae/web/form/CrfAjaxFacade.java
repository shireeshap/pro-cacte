package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.CrfPageItemQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.context.SecurityContextHolder;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
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
        CRFQuery crfQuery = new CRFQuery(QueryStrings.CRF_QUERY_SORTBY_FIELDS);
        crfQuery.setFirstResult(startIndex);
        crfQuery.setMaximumResults(results);
        if(sortField.equalsIgnoreCase("studyShortTitle")){
        	crfQuery.setSortBy("o.study.shortTitle");
        }else if(sortField.equalsIgnoreCase("version")){
        	crfQuery.setSortBy("o.crfVersion");
        }else{
        	crfQuery.setSortBy("o." + sortField);
        }
        crfQuery.setSortDirection(direction);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        buidFilteredQuery(crfQuery, user);

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
    
    private CRFQuery buidFilteredQuery(CRFQuery crfQuery, User user){
    	crfQuery.filterByHidden(false);
        crfQuery.filterByNullNextVersionId();
        if (!user.isAdmin()) {
            crfQuery.filterByUsername(user.getUsername());
        }
        
        return crfQuery;
    }

    public Long resultCount(String[] searchTexts) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CRFQuery crfQuery = new CRFQuery(QueryStrings.CRF_QUERY_COUNT);
        buidFilteredQuery(crfQuery, user);
        
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
       return ObjectTools.reduceAll(releasedCrfs, "id", "title", "eq5d");
    }
    
    public List<CRF> getNonEQ5DCrfs(Integer id) throws Exception{
    	List<CRF> crfs = getReducedCrfs(id);
    	List<CRF> nonEq5dCrfList = new ArrayList<CRF>();
    	for(CRF crf : crfs){
    		if(!crf.isEq5d()){
    			nonEq5dCrfList.add(crf);
    		}
    	}
    	return nonEq5dCrfList;
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
        Set<ProCtcTerm> terms = new TreeSet<ProCtcTerm>();
        for (CrfPageItem i : crf.getAllCrfPageItems()) {
            terms.add(i.getProCtcQuestion().getProCtcTerm());
        }
        return ObjectTools.reduceAll(new ArrayList<ProCtcTerm>(terms), "id", "term");
    }

    /**Get symptoms from all the released forms for the study except the
     * EQ5D symptoms.
     * @param id
     */
    public List<ProCtcTerm> getAllSymptomsForStudy(Integer id) {
    	Set<ProCtcTerm> terms = new TreeSet<ProCtcTerm>();
    	try {
			List<CRF> crfList = getReducedCrfs(id);
			List<Integer> crfIds = new ArrayList<Integer>();
			
			for(CRF crf : crfList){
				if(!crf.isEq5d()){
					crfIds.add(crf.getId());
				}
			}
			CrfPageItemQuery query = new CrfPageItemQuery();
			query.filterByCrfIds(crfIds);
			List<CrfPageItem> crfPageItems = genericRepository.find(query);

			for(CrfPageItem cpi : crfPageItems){
				terms.add(cpi.getProCtcQuestion().getProCtcTerm());
			}
		} catch (Exception e) {
			e.printStackTrace();
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
