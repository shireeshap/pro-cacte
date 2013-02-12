package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionDisplayRule;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcTermVocab;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.csvreader.CsvReader;


/**
 * @author Mehul Gulati
 *         Date: Jan 15, 2009
 */

public class ProCtcTermsImporterV4 {
    private CtcTermRepository ctcTermRepository;
    private static final String QUESTION_TEXT = "PRO-CTCAE Wording";
    private static final String PRO_CTC_TERM = "PRO-CTCAE Term";
    private static final String CORE_ITEM = "Core Item";
    private static final String QUESTION_TYPE = "Attribute";
    private static final String PRO_CTC_VALID_VALUES = "Response Options";
    private static final String CTC_TERM = "CTCAE v4 Term";
    private static final String CATEGORY = "CTCAE v4 System Organ Class (SOC)";
    private static LoaderHelper loaderHelper;

    public ProCtc loadProCtcTerms(boolean fromTestCase) throws IOException {
    	loaderHelper = new LoaderHelper();
    	loaderHelper.setCtcTermRepository(ctcTermRepository);
        CsvReader reader;
        HashMap<String, List<CsvLine>> hm = new LinkedHashMap<String, List<CsvLine>>();
        if (fromTestCase) {
            Resource resource = new FileSystemResource("core/src/main/resources/");
        	Resource resource1 = resource.createRelative("ProCtcTerms_V4.csv");
            File f = new File(resource1.getFile().getCanonicalPath());
            System.out.println(f.getCanonicalPath());
            reader = new CsvReader(new FileInputStream(f), Charset.forName("ISO-8859-1"));
        } else {
            ClassPathResource classPathResource = new ClassPathResource("ProCtcTerms_V4.csv");
            reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("ISO-8859-1"));
        }

        hm = generateProCtcTermsHashMapFromCsvFile(reader);
        ProCtc proCtc = new ProCtc();
        loaderHelper.createProCtcTermsAndProCtcQuestionsFromHashMap(hm, proCtc);
        proCtc.setProCtcVersion("4.0");
        proCtc.setReleaseDate(new Date());
        return proCtc;
    }
    
    private HashMap<String, List<CsvLine>> generateProCtcTermsHashMapFromCsvFile(CsvReader reader ) throws IOException{
    	HashMap<String, List<CsvLine>> hm = new LinkedHashMap<String, List<CsvLine>>();
        
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
            csvLine = fetchRecordAndReturnCsvLine(reader, proCtcTerm, displayOrderI);
            oldProCtcTerm = proCtcTerm;
            loaderHelper.addToProCtcTermHashMap(hm, csvLine);
        }
    	return hm;
    }
    
    
    private CsvLine fetchRecordAndReturnCsvLine(CsvReader reader, String proCtcTerm, int displayOrderI) throws IOException{
    	CsvLine csvLine = new CsvLine();
    	String question = reader.get(QUESTION_TEXT).trim();
	     String core = reader.get(CORE_ITEM).trim();
	     String attribute = reader.get(QUESTION_TYPE).trim();
	     String validValues = reader.get(PRO_CTC_VALID_VALUES).trim();
	     String ctcTerm = reader.get(CTC_TERM).trim();
	     String ctcCategory = reader.get(CATEGORY).trim();
	     String displayOrder = "" + displayOrderI;
	     String questionType = attribute.substring(attribute.indexOf('-') + 1);
	     String firstLetter = question.substring(0, 1);
	     question = firstLetter.toUpperCase() + question.substring(1);
	     csvLine = setCsvLine(proCtcTerm, ctcTerm, displayOrder, questionType, questionType, validValues, core);
	     return csvLine;
    }
    
    private CsvLine setCsvLine(String proCtcTerm, String ctcTerm, String displayOrder, String questionType, String question, String validValues, String core){
    	CsvLine csvLine = new CsvLine();
    	 csvLine.setProctcTerm(proCtcTerm);
         csvLine.setCtcTerm(ctcTerm);
         csvLine.setDisplayOrder(displayOrder);
         csvLine.setQuestionType(questionType);
         csvLine.setQuestionText(question);
         csvLine.setProctcValidValues(validValues);
         csvLine.setCoreItem(!StringUtils.isBlank(core));
         
         return csvLine;
    }
    
    
    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

}