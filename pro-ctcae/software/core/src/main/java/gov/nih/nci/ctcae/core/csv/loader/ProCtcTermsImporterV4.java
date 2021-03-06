package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private ProCtcTermRepository proCtcTermRepository;
	private ProCtcRepository proCtcRepository;
	private static final String PRO_CTC_SYS_ID = "PRO-CTCAE System ID";
    private static final String QUESTION_TEXT = "PRO-CTCAE Wording";
    private static final String PRO_CTC_TERM = "PRO-CTCAE Term";
    private static final String CORE_ITEM = "Core Item";
    private static final String QUESTION_TYPE = "Attribute";
    private static final String PRO_CTC_VALID_VALUES = "Response Options";
    private static final String CTC_TERM = "CTCAE v4 Term";
    private static final String CATEGORY = "CTCAE v4 System Organ Class (SOC)";
    private static LoaderHelper loaderHelper;
    
    protected static final Log logger = LogFactory.getLog(ProCtcTermsImporterV4.class);


    public ProCtc loadProCtcTerms(boolean fromTestCase) throws IOException {
    	loaderHelper = new LoaderHelper();
    	loaderHelper.setCtcTermRepository(ctcTermRepository);
    	loaderHelper.setProCtcTermRepository(proCtcTermRepository); 
        CsvReader reader;
        HashMap<String, List<CsvLine>> hm = new LinkedHashMap<String, List<CsvLine>>();
        if (fromTestCase) {
            Resource resource = new FileSystemResource("core/src/main/resources/");
        	Resource resource1 = resource.createRelative("ProCtcTerms_V4_formatted_with_IDs.csv");
            File f = new File(resource1.getFile().getCanonicalPath());
            System.out.println(f.getCanonicalPath());
            reader = new CsvReader(new FileInputStream(f), Charset.forName("ISO-8859-1"));
        } else {
            ClassPathResource classPathResource = new ClassPathResource("ProCtcTerms_V4_formatted_with_IDs.csv");
            reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("ISO-8859-1"));
        }

        hm = generateProCtcTermsHashMapFromCsvFile(reader);
        
        ProCtc proCtc = getProCtc();
        if(proCtc == null){
            proCtc = new ProCtc();
            proCtc.setProCtcVersion("4.0");
            proCtc.setReleaseDate(new Date());
        }
        loaderHelper.createProCtcTermsAndProCtcQuestionsFromHashMap(hm, proCtc);
        reader.close();
        return proCtc;
    }
    
    private ProCtc getProCtc(){
        ProCtcQuery query = new ProCtcQuery();
        query.filterByProCtcVersion("4.0");
        return proCtcRepository.findSingle(query);
    }

    
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
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
            if(csvLine != null){
                oldProCtcTerm = proCtcTerm;
                loaderHelper.addToProCtcTermHashMap(hm, csvLine);
            }
        }
    	return hm;
    }
    
    
    private CsvLine fetchRecordAndReturnCsvLine(CsvReader reader, String proCtcTerm, int displayOrderI) throws IOException{
    	CsvLine csvLine = new CsvLine();
    	Integer proCtcTermSystemId = null;
    	try{
    		proCtcTermSystemId = Integer.parseInt(reader.get(PRO_CTC_SYS_ID));
    	} catch (NumberFormatException nfe){
    		logger.error("Skipping Record. Number expected but found : " + proCtcTermSystemId);
    		return null;
    	}
    	String question = reader.get(QUESTION_TEXT).trim();
	     String core = reader.get(CORE_ITEM).trim();
	     String attribute = reader.get(QUESTION_TYPE).trim();
	     String validValues = reader.get(PRO_CTC_VALID_VALUES).trim();
	     String ctcTerm = reader.get(CTC_TERM).trim();
	     //String ctcCategory = reader.get(CATEGORY).trim();
	     String displayOrder = "" + displayOrderI;
	     String questionType = attribute.substring(attribute.indexOf('-') + 1);
	     String firstLetter = question.substring(0, 1);
	     question = firstLetter.toUpperCase() + question.substring(1);
	     csvLine = setCsvLine(proCtcTerm, ctcTerm, displayOrder, questionType, question, validValues, core, proCtcTermSystemId);
	    	 
	     return csvLine;
    }
    
    private CsvLine setCsvLine(String proCtcTerm, String ctcTerm, String displayOrder, String questionType, String question, String validValues, String core, Integer proCtcTermSystemId){
    	CsvLine csvLine = new CsvLine();
    	 csvLine.setProctcTerm(proCtcTerm);
         csvLine.setCtcTerm(ctcTerm);
         csvLine.setDisplayOrder(displayOrder);
         csvLine.setQuestionType(questionType);
         csvLine.setQuestionText(question);
         csvLine.setProctcValidValues(validValues);
         csvLine.setCoreItem(!StringUtils.isBlank(core));
         csvLine.setProctcTermSystemId(proCtcTermSystemId);
         
         return csvLine;
    }
    

    public void setProCtcRepository(ProCtcRepository proCtcRepository) {
		this.proCtcRepository = proCtcRepository;
	}

	public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

}