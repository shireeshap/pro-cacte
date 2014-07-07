package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValueVocab;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.csvreader.CsvReader;


/**
 * @author mehul gulati
 *         Date: Jun 28, 2010
 */
public class UpdateProCtcTermsImporterV4 {
    private ProCtcQuestionRepository proCtcQuestionRepository;
    private CtcTermRepository ctcTermRepository;
    private ProCtcRepository proCtcRepository;
    private LoaderHelper loaderHelper;

	private static final String PRO_CTC_TERM_SYS_ID = "PRO-CTCAE System ID";
    private static final String QUESTION_TEXT = "PROCTCAE Wording";
    private static final String PRO_CTC_TERM = "PRO-CTCAE Term";
    private static final String CORE_ITEM = "Core Item";
    private static final String QUESTION_TYPE = "Attribute";
    private static final String PRO_CTC_VALID_VALUES = "Response Options";
    private static final String CTC_TERM = "CTCAE v4 Term";
    private static final String GENDER = "Gender";
    private static final int OnlySpecialCaseDisplayOrderToBeAssigned = -1;
    
    protected static final Log log = LogFactory.getLog(UpdateProCtcTermsImporterV4.class);

    
    public void updateProCtcTerms(ProCtc proCtc) throws IOException {
        HashMap<String, List<CsvLine>> hm = new LinkedHashMap<String, List<CsvLine>>();
        ClassPathResource classPathResource = new ClassPathResource("PRO-CTCAE_items_updated_05.17.2011_formatted_with_IDs.csv");
        CsvReader reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("ISO-8859-1"));
        
        reader.readHeaders();
        String oldProCtcTerm = "";
        int displayOrderI = 0;

        while (reader.readRecord()) {
            CsvLine csvLine;
         	String proCtcTerm = reader.get(PRO_CTC_TERM).trim();
        	if (proCtcTerm.equals(oldProCtcTerm)) {
               displayOrderI++;
        	}else {
    	       displayOrderI = 1;
    	     }
            oldProCtcTerm = proCtcTerm;

            csvLine = fetchRecordAndReturnCsvLine(reader, proCtcTerm, displayOrderI);
            if(csvLine == null){
            	continue;
            }
            
            ProCtcQuestion proCtcQuestion = getProCtcQuestionByProCtcTermAndQuestionType(csvLine.proctcTermSystemId, csvLine.getQuestionType());
            if (proCtcQuestion != null ) {
            	setProCtcQuestionWithCsvLineValuesAndSave(proCtcQuestion, csvLine);
            } else {
                CtcTerm ctcTerm = findCtcTermFromRepository(ctcTermRepository, csvLine.getCtcTerm());
                if (ctcTerm != null) {
                    if (ctcTerm.getProCtcTerms().size() > 0) {
                    	ctcTerm.getProCtcTerms().get(0).setProCtcSystemId(csvLine.getProctcTermSystemId().intValue());
                    	ctcTerm.getProCtcTerms().get(0).getProCtcTermVocab().setTermEnglish(proCtcTerm);
                        ctcTerm.getProCtcTerms().get(0).setGender(csvLine.getGender());
                        ctcTermRepository.save(ctcTerm);
                    } else {
                        loaderHelper.addToProCtcTermHashMap(hm, csvLine);
                    }
                } else {
                	log.error("ctcTerm was null" + csvLine.getCtcTerm());
                }
            }
        }

        loaderHelper.createProCtcTermsAndProCtcQuestionsFromHashMap(hm, proCtc);
        proCtcRepository.save(proCtc);
        reader.close();
    }
    
    private void setProCtcQuestionWithCsvLineValuesAndSave(ProCtcQuestion proCtcQuestion, CsvLine csvLine){
    	   proCtcQuestion.getProCtcTerm().setGender(csvLine.getGender());
           proCtcQuestion.setQuestionText(csvLine.getQuestionText(), SupportedLanguageEnum.ENGLISH);
           if (ProCtcQuestionType.PRESENT.equals(proCtcQuestion.getProCtcQuestionType())) {
           	 for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
           		 setProCtcValidValueDisplayOrder(proCtcQuestion, proCtcValidValue, OnlySpecialCaseDisplayOrderToBeAssigned);		 
           	 }
           }
           setResponseCode(proCtcQuestion);
           includeNewlyAddedProCtcValidValuesIfAny(csvLine, proCtcQuestion);
           proCtcQuestion.getProCtcTerm().setCurrency("Y");
           proCtcQuestion.getProCtcTerm().setCore(csvLine.getCoreItem());
           proCtcQuestionRepository.save(proCtcQuestion);
    }
    
    private CtcTerm findCtcTermFromRepository(CtcTermRepository ctcTermRepository, String ctcTermEnglishText){
   	 CtcQuery ctcQuery = new CtcQuery();
        ctcQuery.filterByName(ctcTermEnglishText);
        List<CtcTerm> ctcTerms = ctcTermRepository.find(ctcQuery);
        if (ctcTerms != null && ctcTerms.size() > 0) {
            return ctcTerms.get(0);
        }
        return null;
   }
    
    private void includeNewlyAddedProCtcValidValuesIfAny(CsvLine csvLine, ProCtcQuestion proCtcQuestion){
        StringTokenizer st1 = new StringTokenizer(csvLine.getProctcValidValues(), "/");
        Collection<ProCtcValidValue> values = proCtcQuestion.getValidValues();
        Collection<String> validValues1 = new ArrayList<String>();
        for (ProCtcValidValue proValue : values) {
            String value = proValue.getValue(SupportedLanguageEnum.ENGLISH).trim();
            validValues1.add(value);
        }
        int j = validValues1.size();
        while (st1.hasMoreTokens()) {
            String nextToken = st1.nextToken().trim();
            if (!validValues1.contains(nextToken)) {
                ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
                ProCtcValidValueVocab proCtcValidValueVocab = new ProCtcValidValueVocab();
                proCtcValidValueVocab.setValueEnglish(nextToken);
                proCtcValidValueVocab.setProCtcValidValue(proCtcValidValue);
                proCtcValidValue.setProCtcValidValueVocab(proCtcValidValueVocab);
                proCtcValidValue.setDisplayOrder(j);
                j++;
                proCtcQuestion.addValidValue(proCtcValidValue);
            }
        }
    }
    
    private void setProCtcValidValueDisplayOrder(ProCtcQuestion proCtcQuestion, ProCtcValidValue proCtcValidValue, int displayOrder){
    	 if (proCtcQuestion.getProCtcQuestionType().equals(ProCtcQuestionType.PRESENT)) {
             if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).trim().equals("Yes")) {
                 proCtcValidValue.setDisplayOrder(1);
             }
             if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).trim().equals("No")) {
                 proCtcValidValue.setDisplayOrder(0);
             }
             if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).trim().equals("Not applicable")) {
                 proCtcValidValue.setDisplayOrder(2);
             }
             if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).trim().equals("Not sexually active")) {
                 proCtcValidValue.setDisplayOrder(3);
             }
             if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).trim().equals("Prefer not to answer")) {
                 proCtcValidValue.setDisplayOrder(4);
             }
         } else {
        	 if(displayOrder != -1){
        		 proCtcValidValue.setDisplayOrder(displayOrder);
        	 }
         }
    }
    
    private ProCtcQuestion getProCtcQuestionByProCtcTermAndQuestionType(Integer proCtcTermSystemId, String questionType){
   	 ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
        proCtcQuestionQuery.filterByQuestionType(ProCtcQuestionType.getByCode(questionType));
        proCtcQuestionQuery.filterByTermSystemId(proCtcTermSystemId);
        List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        if (proCtcQuestions != null && proCtcQuestions.size() > 0) {
            return proCtcQuestions.get(0);
        }
        return null;
   }
    
    private CsvLine fetchRecordAndReturnCsvLine(CsvReader reader, String proCtcTerm, int displayOrderI) throws IOException{
		CsvLine csvLine = new CsvLine();
		Integer proctcTermSystemId = 0;
		try {
			proctcTermSystemId = Integer.parseInt(reader.get(
					PRO_CTC_TERM_SYS_ID).trim());

		} catch (NumberFormatException nfe) {
			log.error("System Id should be a number. Found this instead : "
					+ reader.get(PRO_CTC_TERM_SYS_ID));
			return null;
		}

		String question = reader.get(QUESTION_TEXT).trim();
		String core = reader.get(CORE_ITEM).trim();
		String attribute = reader.get(QUESTION_TYPE).trim();
		String validValues = reader.get(PRO_CTC_VALID_VALUES).trim();
		String ctcTerm = reader.get(CTC_TERM).trim();
		String displayOrder = "" + displayOrderI;
		String questionType = attribute.substring(attribute.indexOf('-') + 1);
		String firstLetter = question.substring(0, 1);
		question = firstLetter.toUpperCase() + question.substring(1);
		String gender = reader.get(GENDER).trim();
		csvLine = setCsvLine(proCtcTerm, ctcTerm, displayOrder, questionType,
				question, validValues, core, gender, proctcTermSystemId);
		return csvLine;
    }
    
    private CsvLine setCsvLine(String proCtcTerm, String ctcTerm, String displayOrder, String questionType, String question, String validValues, String core, String gender, Integer proctcTermSystemId){
    	CsvLine csvLine = new CsvLine();
    	 csvLine.setProctcTerm(proCtcTerm);
         csvLine.setCtcTerm(ctcTerm);
         csvLine.setDisplayOrder(displayOrder);
         csvLine.setQuestionType(questionType);
         csvLine.setQuestionText(question);
         csvLine.setProctcValidValues(validValues);
         csvLine.setCoreItem(!StringUtils.isBlank(core));
         csvLine.setGender(gender);
         csvLine.setProctcTermSystemId(proctcTermSystemId);
         return csvLine;
    }
    

    public void setResponseCode(ProCtcQuestion proCtcQuestion) {
        int j = 0;
        for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
            proCtcValidValue.setResponseCode(j);
            j++;
        }
        for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
            if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).toLowerCase().trim().equals("not sexually active")) {
                proCtcValidValue.setResponseCode(-66);
            }
            if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).toLowerCase().trim().equals("not applicable")) {
                proCtcValidValue.setResponseCode(-88);
            }
            if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).toLowerCase().trim().equals("prefer not to answer")) {
                proCtcValidValue.setResponseCode(-77);
            }
        }
    }
    

    public void setLoaderHelper(LoaderHelper loaderHelper) {
		this.loaderHelper = loaderHelper;
	}

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }

    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
        this.proCtcRepository = proCtcRepository;
    }

}
