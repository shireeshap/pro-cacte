package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.csv.loader.CsvLineEq5dTerms;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.csvreader.CsvReader;

/**
 * @author VinayG
 */
public class LoadEq5dTermsController extends AbstractController {

	public static final String PRO_CTC_TERM_SYS_ID = "PRO-CTCAE System ID";
	public static final String CATEGORY = "EQ5D";
	public static final String TERM_ENG = "term_eng"; 
	public static final String TERM_SPANISH = "term_spanish";  
	public static final String VALUE_ENG = "value_eng"; 
	public static final String VALUE_SPANISH = "value_spanish";
	private static final String FILE_NAME = "EQ5D-v1.csv";
	
	Log log = LogFactory.getLog(LoadEq5dTermsController.class);
	
    ProCtcTermRepository proCtcTermRepository;
    CtcTermRepository ctcTermRepository;
    ProCtcRepository proCtcRepository;
    
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        CsvReader reader = getReaaderForEq5dCsv();
        reader.readHeaders();
        ProCtc proCtc = getProCtc(); 
        
        try{
        	
        	Map<Integer, List<CsvLineEq5dTerms>> threeLMap = processCsvAndPopulateMaps(reader);

	        for(Integer proCtcTermSystemId : threeLMap.keySet()){
	        	
	        	List<CsvLineEq5dTerms> csvLineList = threeLMap.get(proCtcTermSystemId);
	        	ProCtcTerm existingProCtcTerm = getExistingProCtcTerm(proCtcTermSystemId);
	        	
	         	if(existingProCtcTerm != null){
	         		//update the existing proctcTerm .. unplugging the edit existing term functionality. Was making dupe entries in valid values vocab.
//	         		buildProCtcTerm(existingProCtcTerm, csvLineList);
//	         		addValidValuesToProCtcTerm(existingProCtcTerm, csvLineList);
//	         		saveUpdatedProCtcTerm(existingProCtcTerm);
	         	} else {
	         		String categoryName = csvLineList.get(0).categoryName;
	         		String termEng = csvLineList.get(0).termEnglish;
	         		CtcTerm existingCtcTerm = getExistingCtcTerm(termEng, categoryName);
	         		if(existingCtcTerm != null){
	         			//link new propCtcTerm to this ctcTerm
	         			ProCtcTerm proCtcTerm = new ProCtcTerm();
	         			proCtcTerm.setCtcTerm(existingCtcTerm);
	         			proCtcTerm.setProCtcSystemId(proCtcTermSystemId);
	         			proCtcTerm.setProCtc(proCtc);
	         			proCtcTerm.setCore(false);
	         			proCtcTerm.setGender("both");
	         			proCtcTerm.setCurrency("Y");
	         			
	         			buildProCtcTerm(proCtcTerm, csvLineList);
	             		addValidValuesToProCtcTerm(proCtcTerm, csvLineList);
	
	             		saveUpdatedProCtcTerm(proCtcTerm);
	         		} else {
	         			log.error("Skippping. No ProCtcTerm or CtcTerm found for: " + proCtcTermSystemId);
	         		}
	         	}
	        }
        } catch (IOException ioe){
        	log.error("Error accessing the CSV :"+ ioe.getMessage());
        }
        
        reader.close();
        return new ModelAndView("eq5dTermsLoaded");
    }

    private String removeQuotations(String termEng) {
		String cleanTermEng = termEng.replace("\"","");
		cleanTermEng = cleanTermEng.replace("'","");
		return cleanTermEng;
	}

	/**
     * Built Map looks like the following:
     * key -> Value
     * 10083 -> CsvLineObject (EQ5D-3L,'MOBILITY', 'MOVILIDAD','I have no problems in walking about', 'No tengo problemas para caminar sobre');
     * 10083 -> CsvLineObject (EQ5D-3L,'MOBILITY', 'MOVILIDAD','I have some problems in walking about', 'Tengo algunos problemas para caminar sobre');
     * 10083 -> CsvLineObject (EQ5D-3L,'MOBILITY', 'MOVILIDAD','I am confined to bed', 'Estoy confinado a la cama');
     * 
     * As in the example above, the category and term/question repeat in every value against the key.
     * 
     * @param reader
     * @return
     * @throws IOException
     */
	private Map<Integer, List<CsvLineEq5dTerms>> processCsvAndPopulateMaps(CsvReader reader) throws IOException {
	    //Map that maintains the questions and valid values for every pro-term using the pro-term-sys-id as key.
	    Map<Integer, List<CsvLineEq5dTerms>> threeLMap = new LinkedHashMap<Integer, List<CsvLineEq5dTerms>>();
	    
        while (reader.readRecord()) {
            String proCtcTermSystemIdStr = null;
            Integer proCtcTermSystemId;
            try{
             	proCtcTermSystemIdStr = reader.get(PRO_CTC_TERM_SYS_ID).trim();
             	proCtcTermSystemId = Integer.parseInt(proCtcTermSystemIdStr);
            } catch (NumberFormatException nfe){
            	log.error("Ignoring record. Expected a number for systemId but got  :" + proCtcTermSystemIdStr);
            	continue;
            }
            String categoryName = removeQuotations(reader.get(CATEGORY).trim());
            String termEng =  removeQuotations(reader.get(TERM_ENG).trim());
            String termSpanish =  removeQuotations(reader.get(TERM_SPANISH).trim());
            String valueEng =  removeQuotations(reader.get(VALUE_ENG).trim());
            String valueSpanish =  removeQuotations(reader.get(VALUE_SPANISH).trim());
            
            
        	CsvLineEq5dTerms csvValueObj = new CsvLineEq5dTerms(categoryName, termEng, termSpanish, valueEng, valueSpanish);
    		if(threeLMap.get(proCtcTermSystemId) == null){
    			List<CsvLineEq5dTerms> eq5dVoList = new ArrayList<CsvLineEq5dTerms>();
    			eq5dVoList.add(csvValueObj);
    			threeLMap.put(proCtcTermSystemId, eq5dVoList);
    		} else {
    			threeLMap.get(proCtcTermSystemId).add(csvValueObj);
    		}
        }	
        return threeLMap;
	}



	private void addValidValuesToProCtcTerm(ProCtcTerm proCtcTerm,
			List<CsvLineEq5dTerms>csvLineList) {
		
		int displayOrder_responseCode = 1;
		for(CsvLineEq5dTerms csvObj : csvLineList){
			String valueEnglish = csvObj.valueEnglish;
			String valueSpanish = csvObj.valueSpanish;
			
			ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
			proCtcValidValue.setDisplayOrder(displayOrder_responseCode);
			proCtcValidValue.setResponseCode(displayOrder_responseCode);
			proCtcValidValue.setValue(valueEnglish, SupportedLanguageEnum.ENGLISH);
			proCtcValidValue.setValue(valueSpanish, SupportedLanguageEnum.SPANISH);
			
			if(!((ProCtcQuestion)proCtcTerm.getProCtcQuestions().get(0)).getValidValues().contains(proCtcValidValue)){
				((ProCtcQuestion)proCtcTerm.getProCtcQuestions().get(0)).addValidValue(proCtcValidValue);
			}
			displayOrder_responseCode++;
		}
	}


	private void buildProCtcTerm(ProCtcTerm proCtcTermToBeUpdated, List<CsvLineEq5dTerms>csvLineList) {
		
		int displayOrder = 1;
		//only need one obj from the list as all the question text is same in all list elements.
		CsvLineEq5dTerms csvObj = csvLineList.get(0);
		String termEng = csvObj.termEnglish;
		String termSpanish = csvObj.termSpanish;
		
		proCtcTermToBeUpdated.setTermEnglish(termEng, SupportedLanguageEnum.ENGLISH);
		proCtcTermToBeUpdated.setTermEnglish(termSpanish, SupportedLanguageEnum.SPANISH);
		
		ProCtcQuestion proCtcQuestion;
		if(proCtcTermToBeUpdated.getProCtcQuestions().isEmpty()){
			 proCtcQuestion = new ProCtcQuestion();
			 proCtcTermToBeUpdated.addProCtcQuestion(proCtcQuestion);
		} else {
			proCtcQuestion = proCtcTermToBeUpdated.getProCtcQuestions().get(0);
		}
		proCtcQuestion.setDisplayOrder(displayOrder);
		proCtcQuestion.setProCtcTerm(proCtcTermToBeUpdated);
		proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
		proCtcQuestion.setQuestionText(termEng.toUpperCase(), SupportedLanguageEnum.ENGLISH);
		proCtcQuestion.setQuestionText(termSpanish.toUpperCase(), SupportedLanguageEnum.SPANISH);
		
	}


	private CsvReader getReaaderForEq5dCsv() throws IOException {
    	ClassPathResource classPathResource = new ClassPathResource(FILE_NAME);
        return new CsvReader(classPathResource.getInputStream(), Charset.forName("UTF-8"));
	}
	

	private void saveUpdatedProCtcTerm(ProCtcTerm proCtcTermToBeSaved) {
    	proCtcTermRepository.save(proCtcTermToBeSaved);
	}
	
	 
	private ProCtc getProCtc() {
		ProCtcQuery proCtcQuery = new ProCtcQuery();
		proCtcQuery.filterByProCtcVersion("4.0");
		return proCtcRepository.findSingle(proCtcQuery);
	}

	private CtcTerm getExistingCtcTerm(String termEng, String categoryName) {
    	CtcQuery ctcQuery = new CtcQuery();
    	ctcQuery.filterByName(termEng);
    	ctcQuery.filterByCtcCategoryName(categoryName);
		return ctcTermRepository.findSingle(ctcQuery);
	}

	private ProCtcTerm getExistingProCtcTerm(Integer proCtcTermSystemId) {
    	return proCtcTermRepository.findBySystemId(proCtcTermSystemId);
	}


	@Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    @Required
    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    @Required
    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        this.proCtcRepository = proCtcRepository;
    }
}
