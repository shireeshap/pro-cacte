package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.csvreader.CsvReader;

/**
 * User: mehul
 * Date: 5/4/11
 */

public class ProTermsMultiLangImporterV4 {
    ProCtcTermRepository proCtcTermRepository;
    ProCtcQuestionRepository proCtcQuestionRepository;

    private static final String PRO_CTC_TERM_SYS_ID = "PRO-CTCAE System ID";
    private static final String QUESTION_TEXT = "Language specific PRO-CTCAE Question Display";
    private static final String PRO_CTC_TERM_LANG = "Language specific PRO-CTCAE Term Display";
    private static final String QUESTION_TYPE = "Attribute";
    private static final String VALID_VALUE1 = "Response Option - 1";
    private static final String VALID_VALUE2 = "Response Option - 2";
    private static final String VALID_VALUE3 = "Response Option - 3";
    private static final String VALID_VALUE4 = "Response Option - 4";
    private static final String VALID_VALUE5 = "Response Option - 5";
    private static final String VALID_VALUE6 = "Response Option - 6";
    private static final String VALID_VALUE7 = "Response Option - 7";
    
    protected static final Log log = LogFactory.getLog(ProTermsMultiLangImporterV4.class);


    public void updateMultiLangProTerms() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("Spanish_Pro-CTCAE_items_08.10.2011_with_IDs.csv");
        CsvReader reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("UTF-8"));
        reader.readHeaders();

        while (reader.readRecord()) {
        	
            Integer proCtcTermSystemId;
    		try {
    			proCtcTermSystemId = Integer.parseInt(reader.get(
    					PRO_CTC_TERM_SYS_ID).trim());
    		} catch (NumberFormatException nfe) {
    			log.error("Skkipping record. System Id should be a number. Found this instead : "
    					+ reader.get(PRO_CTC_TERM_SYS_ID));
    			continue;
    		}
            String questionSpanishText = reader.get(QUESTION_TEXT).trim();
            String attribute = reader.get(QUESTION_TYPE).trim();
            String proCtcTermLang = reader.get(PRO_CTC_TERM_LANG).trim();
            String firstLetter = questionSpanishText.substring(0, 1);
            questionSpanishText = firstLetter.toUpperCase() + questionSpanishText.substring(1);
            String questionType = attribute.substring(attribute.indexOf('-') + 1);
            List<String> spanishValidValuesList = new ArrayList<String>();
            spanishValidValuesList = getValidValueListFromCurrentRecord(reader);

            ProCtcQuestion proCtcQuestion = getProCtcQuestionByProCtcTermSystemIdAndQuestionType(proCtcTermSystemId, questionType);
            if (proCtcQuestion != null ) {
                updateSpanishTextForProCtcQuestionAndProCtcValidValues(proCtcQuestion, spanishValidValuesList, questionSpanishText);
                proCtcQuestionRepository.save(proCtcQuestion);
            }

            ProCtcTerm proCtcTerm = findProCtcTermFromRepository(proCtcTermSystemId);
            if (proCtcTerm != null ) {
            	proCtcTerm.getProCtcTermVocab().setTermSpanish(proCtcTermLang);
                proCtcTermRepository.save(proCtcTerm);
            }
        }
        reader.close();
    }
    
    private void updateSpanishTextForProCtcQuestionAndProCtcValidValues(ProCtcQuestion proCtcQuestion, List<String> spanishValidValuesList, String questionSpanishText){
    	 proCtcQuestion.getProCtcQuestionVocab().setQuestionTextSpanish(questionSpanishText);
         int i = 0;
         for (ProCtcValidValue validValue : proCtcQuestion.getValidValues()) {
             if (StringUtils.isNotBlank(spanishValidValuesList.get(i))) {
                 validValue.getProCtcValidValueVocab().setValueSpanish(spanishValidValuesList.get(i));
             }
             i++;
         }
    }
    
    private ProCtcTerm findProCtcTermFromRepository(Integer proCtcTermSystemId){
         ProCtcTerm proTerm = proCtcTermRepository.findBySystemId(proCtcTermSystemId);
    	 return proTerm;
    }
    
    private ProCtcQuestion getProCtcQuestionByProCtcTermSystemIdAndQuestionType(Integer proCtcTermSystemId, String questionType){
    	 ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
         proCtcQuestionQuery.filterByQuestionType(ProCtcQuestionType.getByCode(questionType));
         proCtcQuestionQuery.filterByTermSystemId(proCtcTermSystemId);
         List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
         if (proCtcQuestions != null && proCtcQuestions.size() > 0) {
             return proCtcQuestions.get(0);
         }
         return null;
    }
    
    private List<String> getValidValueListFromCurrentRecord(CsvReader reader) throws IOException{
    	List<String> values = new ArrayList<String>();
    	String value1 = reader.get(VALID_VALUE1).trim();
        String value2 = reader.get(VALID_VALUE2).trim();
        String value3 = reader.get(VALID_VALUE3).trim();
        String value4 = reader.get(VALID_VALUE4).trim();
        String value5 = reader.get(VALID_VALUE5).trim();
        String value6 = reader.get(VALID_VALUE6).trim();
        String value7 = reader.get(VALID_VALUE7).trim();
        values.add(value1);
        values.add(value2);
        values.add(value3);
        values.add(value4);
        values.add(value5);
        values.add(value6);
        values.add(value7);
        return values;
    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
