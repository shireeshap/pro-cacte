package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMapping;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMappingVersion;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProctcaeGradeMappingVersionQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.csvreader.CsvReader;

/**This loader is responsible for parsing the gradeMapping csv file and generating the gradeMappings in the DB.
 * It is idempotent and also picks up edits to the csv file and updates existing records if such a case arises.
 * Currently assumes the presence of one and only one version.
 * 
 * @author VinayG
 */
public class ProctcaeGradeMappingsLoader  {

	GenericRepository genericRepository;
	ProCtcTermRepository proCtcTermRepository;

	private static final String PRO_CTC_SYS_ID = "PRO-CTCAE System ID";
    private static final String FREQ = "FREQ";
    private static final String SEV = "SEV";
    private static final String INT = "INT";
    private static final String AMT = "AMT";
    private static final String PRES_ABS = "PRES/ABS";
    private static final String pCTCAE = "pCTCAE";
	private static final String NA = "n/a";
    
    protected static final Log log = LogFactory.getLog(ProctcaeGradeMappingsLoader.class);


    public void loadProctcaeGradeMappings() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("PRO-CTCAE-GradeMapping_v1.1_08.29.13.csv");
        CsvReader reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("UTF8"));
        reader.readHeaders();
        
        ProctcaeGradeMappingVersion pgmv = getLatestProctcaeGradeMappingVersion();
        ProCtcTerm existingProctcaeTerm = null;
        while (reader.readRecord()) {
        	try{
                String proctcaeTermSystemId = reader.get(PRO_CTC_SYS_ID).trim();
                String frequency = reader.get(FREQ).trim();
                String severity = reader.get(SEV).trim();
                String interference = reader.get(INT).trim();
                String amount = reader.get(AMT).trim();
                String presentAbsent = reader.get(PRES_ABS).trim();
                String grade = reader.get(pCTCAE).trim();
                
        		Integer systemId;
        		try{
        			systemId = Integer.valueOf(proctcaeTermSystemId);
        		} catch (NumberFormatException nfe){
        			log.error("Number expected: found-" + proctcaeTermSystemId);
        			continue;
        		}
                
                if(existingProctcaeTerm == null || existingProctcaeTerm.getProCtcSystemId() == null ||
                		!existingProctcaeTerm.getProCtcSystemId().equals(systemId)){
                    existingProctcaeTerm = getExistingProCtcTerm(systemId);
                }
                ProctcaeGradeMapping pgMapping = buildGradeMapping(frequency, severity, interference, amount, presentAbsent, grade, existingProctcaeTerm, pgmv);
                
                if(existingProctcaeTerm != null && pgMapping != null) {
                	//If pgMapping does not already exist then add it.
                	if(!existingProctcaeTerm.getProCtcGradeMappings().contains(pgMapping)){
                        existingProctcaeTerm.getProCtcGradeMappings().add(pgMapping);
                	} else {
                		//if pgMapping does exist then edit it if the grade from the csv is different from the existing grade.
                		int indexOfExistingPgMapping = existingProctcaeTerm.getProCtcGradeMappings().indexOf(pgMapping);
                		ProctcaeGradeMapping existingPgMapping = existingProctcaeTerm.getProCtcGradeMappings().get(indexOfExistingPgMapping);
                		if(!existingPgMapping.getProCtcGrade().equalsIgnoreCase(pgMapping.getProCtcGrade())){
                			existingPgMapping.setProCtcGrade(pgMapping.getProCtcGrade());
                		}
                	}
                }
        	} catch (Exception e){
        		log.error("Failed to load record with proctcae system ID: " + reader.get(PRO_CTC_SYS_ID).trim());
        	}
        }
        
        reader.close();
    }
    
	private ProctcaeGradeMappingVersion getLatestProctcaeGradeMappingVersion() {
		ProctcaeGradeMappingVersionQuery pgmvQuery = new ProctcaeGradeMappingVersionQuery();
		pgmvQuery.filterByDefaultVersion();
		ProctcaeGradeMappingVersion defaultProctcaeGradeMappingVersion = genericRepository.findSingle(pgmvQuery);
		return defaultProctcaeGradeMappingVersion;
	}


	private ProctcaeGradeMapping buildGradeMapping(String frequencyStr,
			String severityStr, String interferenceStr, String amountStr,
			String presentAbsentStr, String grade, ProCtcTerm existingProctcaeTerm, ProctcaeGradeMappingVersion pgmv) {
		
		ProctcaeGradeMapping pgMapping;
		try{
			Integer freq = getValue(frequencyStr);
			Integer sev = getValue(severityStr);
			Integer interference = getValue(interferenceStr);
			Integer amt = getValue(amountStr);
			Integer presentAbsent = getValue(presentAbsentStr);
			
			if(presentAbsent != null){
				pgMapping = new ProctcaeGradeMapping(presentAbsent, false, grade, pgmv, existingProctcaeTerm);
			} else if(amt != null){
				pgMapping = new ProctcaeGradeMapping(amt, true, grade, pgmv, existingProctcaeTerm);
			} else {
				pgMapping = new ProctcaeGradeMapping(freq, sev, interference, grade, pgmv, existingProctcaeTerm);				
			}
		} catch(NumberFormatException nfe){
			return null;
		}
		return pgMapping;
	}


	private Integer getValue(String valueStr) throws NumberFormatException{
		if(valueStr == null || StringUtils.isEmpty(valueStr) 
				|| valueStr.trim().equalsIgnoreCase(NA)){
			return null;
		}
		return Integer.parseInt(valueStr);
	}


	private ProCtcTerm getExistingProCtcTerm(Integer systemId) {
		ProCtcTermQuery pQuery = new ProCtcTermQuery();
		pQuery.findByProCtcSystemId(systemId);
		ProCtcTerm pTerm = proCtcTermRepository.findSingle(pQuery);
		return pTerm;
	}
	
	public void setGenericRepository(GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}

	public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
		this.proCtcTermRepository = proCtcTermRepository;
	}

}
