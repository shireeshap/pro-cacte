package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.Study;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class StudyLevelFullReportCsvView extends AbstractView {

	private static String NOT_AVAILABLE = "Not Available";
	private static String DEFAULT_POSITION = "";
	private static String FORCE_SKIP = "Forced skip";
	private static String MANUAL_SKIP =	"Not answered";	
	CSVWriter proCsvWriter = null;
	CSVWriter eq5DCsvWriter = null;
	private static List reportInformation;
	private static List legend;
	private static List<String> row;
	private static List<List<String>> rowSet; 
	String[] emptyRow;
	private static int maxRowSize;
	private static String FREQUENCY = "FRQ";
	private static String INTERFERENCE = "INT";
	private static String SEVERITY = "SEV";
	private static String PRESENT =	"PRES";
	private static String AMOUNT =	"AMT";
	private static String OVERALL_STUDY_REPORT = "OverallStudyReport";
	private static String EQ_5D = "EQ-5D";
	File overallStudyReport;
	File eq5DReport;
	ZipOutputStream zipOutputStream ;
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		Map<String, Map<String,Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> results = (Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>>) request.getSession().getAttribute("sessionResultsMap");
		Map<String, LinkedHashMap<String, List<Date>>> crfDateMap = (Map<String, LinkedHashMap<String, List<Date>>>) request.getSession().getAttribute("sessionCRFDatesMap");
		Map<String, LinkedHashMap<String, List<String>>> crfModeMap = (Map<String, LinkedHashMap<String, List<String>>>) request.getSession().getAttribute("sessionCRFModeMap");
		Map<String, LinkedHashMap<String, List<CrfStatus>>> crfStatusMap = (Map<String, LinkedHashMap<String, List<CrfStatus>>>) request.getSession().getAttribute("sessionCRFStatusMap");
		Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping = (Map<String, Map<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionProCtcQuestionMapping");
		Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping = (Map<String, Map<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionMeddraQuestionMapping");
		List<String> proCtcTermHeaders = (List<String>) request.getSession().getAttribute("sessionProCtcTermHeaders");
		List<String> meddraTermHeaders = (List<String>) request.getSession().getAttribute("sessionMeddraTermHeaders");
        Map<String, String> participantInfoMap = (Map<String, String>) request.getSession().getAttribute("participantInfoMap");
        Study study = (Study) request.getSession().getAttribute("study");
        
        overallStudyReport = new File(OVERALL_STUDY_REPORT+".csv");
    	eq5DReport = new File(EQ_5D+".csv");
    	zipOutputStream = new ZipOutputStream(response.getOutputStream());

    	maxRowSize = (7+ proCtcTermHeaders.size() + meddraTermHeaders.size());
		emptyRow = new String[maxRowSize];
		PrintWriter proWriter = new PrintWriter(overallStudyReport);
		PrintWriter eq5DWriter = new PrintWriter(eq5DReport);
		
		
		try{
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition", "attachment; filename=\"ProCtcaeReports.zip\"");
			response.addHeader("Content-Transfer-Encoding", "binary");
			  
			proCsvWriter = new CSVWriter(proWriter);
			eq5DCsvWriter = new CSVWriter(eq5DWriter);
			
			reportInformation = new ArrayList<String>();
			reportInformation.add(0, "Study");
			reportInformation.add(1,study.getDisplayName());
			proCsvWriter.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
			reportInformation.clear();
			reportInformation.add(0, "Type");
			reportInformation.add(1,"This is a test EQ-5D report");
			eq5DCsvWriter.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));

			
			reportInformation = new ArrayList<String>();
			reportInformation.add(0, "Report run date");
			reportInformation.add(1, DateUtils.format(new Date()));
			proCsvWriter.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
			eq5DCsvWriter.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
			
			String studySiteParam = (String) request.getSession().getAttribute("organizationName");
			if(!StringUtils.isEmpty(studySiteParam)){
				reportInformation = new ArrayList<String>();
				reportInformation.add(0, "Study Site");
				reportInformation.add(1, studySiteParam);
				proCsvWriter.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
			}
			
			String crfIdParam = (String) request.getSession().getAttribute("crfTitle");
			if(!StringUtils.isEmpty(crfIdParam)){
				reportInformation = new ArrayList<String>();
				reportInformation.add(0, "Form");
				reportInformation.add(1, crfIdParam);
				proCsvWriter.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
			}

			//Blank row
			proCsvWriter.writeNext(emptyRow);
			//Legend
			buildLegend();
			
			//Blank row
			proCsvWriter.writeNext(emptyRow);
	        int numOfColumns = 0;
	        //Create main table header
	        createTableHeaders(proCtcTermHeaders, meddraTermHeaders);
	        
	        for(String organization : results.keySet()){
	        	Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>> crfMap = results.get(organization);
	        	for(String crf: crfMap.keySet()){ 
	        		Map<String, Map<String, LinkedHashMap<String, List<String>>>> participantMap = crfMap.get(crf);
	                for (String participant : participantMap.keySet()) {
	                	try{
	                		Map<String, LinkedHashMap<String, List<String>>> symptomMap = (Map<String, LinkedHashMap<String, List<String>>>) participantMap.get(participant);
		                    int questionCellNum = 0;
		
		                    LinkedHashMap<String,List<Date>> datesMap = crfDateMap.get(crf); 
		                    LinkedHashMap<String,List<String>> modesMap = crfModeMap.get(crf);
		                    LinkedHashMap<String, List<CrfStatus>> statusMap = crfStatusMap.get(crf);
		                    List<Date> dates = null;
		                    List<String> appModes = null;
		                    List<CrfStatus> statusList = null;
		                    
		                    for (String participantD : datesMap.keySet()) {
		                        if (participant.equals(participantD)) {
		                            dates = datesMap.get(participant);
		                            break;
		                        }
		                    }
		                    for(String participantM : modesMap.keySet()){
		                    	if(participant.equals(participantM)){
		                    		appModes = modesMap.get(participant);
		                    		break;
		                    	}
		                    }
		                    for(String participantS : statusMap.keySet()){
		                    	if(participant.equals(participantS)){
		                    		statusList = statusMap.get(participant);
		                    		break;
		                    	}
		                    }
		                    AppMode appModeForSurvery;
		                    rowSet = new ArrayList<List<String>>();
		                    int rownum = 0;
		                    if (dates != null && appModes!= null) {
		                    	int index = 0;
		                        for (Date date : dates) {
		                            row = createRow(rowSet, rownum++);
		                            createCurrentRowPrefix(row, participantInfoMap.get(participant), study.getShortTitle(), organization, crf);
		                            if(date != null){
		                            	row.add(4, DateUtils.format(date));
		                            } else {
		                            	row.add(4, NOT_AVAILABLE);
		                            }
		                            if(appModes.get(index) != null){
		                            	row.add(5, appModes.get(index));
		                            } else {
		                            	row.add(5, NOT_AVAILABLE);
		                            }
		                            row.add(6, statusList.get(index).getDisplayName());
		                            markDefaultNotAdministered(row, (proCtcTermHeaders.size() + meddraTermHeaders.size()));
		                            index++;
		                        }
		                    }
		
		                    int cellNum = 7;
		                    int posNotFound = maxRowSize + 1;
		                    for (String termEnglish : symptomMap.keySet()) {
		                        LinkedHashMap<String, List<String>> questionMap = symptomMap.get(termEnglish);
		                        for (String proCtcQuestionType : questionMap.keySet()) {
		                        	List<String> valuesList = questionMap.get(proCtcQuestionType);
		                            int index = 0;
		                            for (String validValue : valuesList) {
		                            	try{
		                            		row = getRow(rowSet, (rownum - (valuesList.size() - index)));
		                            	}catch (Exception e) {
		                            		e.getStackTrace();
		                            	}
		                                
		                                String pos;
		                                int tempPos;
		                                pos = getCellNumberValidValue(proCtcQuestionMapping, meddraQuestionMapping, termEnglish, ProCtcQuestionType.getByCode(proCtcQuestionType));
	                                     tempPos = Integer.valueOf(pos) + 7;
	                                     if(!pos.equals(DEFAULT_POSITION)){
	                                    	 cellNum = tempPos;
	                                    	 row.set(cellNum, validValue);
	                                     } else {
	                                    	 logger.debug("No column found for Term: "+ termEnglish + " and question type: " +proCtcQuestionType);
	                                    	 continue;
	                                     }
		                                index++;
		                            }
		                            cellNum++;
		                            if (cellNum > numOfColumns) {
		                                numOfColumns = cellNum;
		                            }
		                        }
		
		                    }
		                    
		                    for(int i = 0; i<rowSet.size(); i++){
		                    	row = rowSet.get(i);
		                    	proCsvWriter.writeNext(row.toArray(new String[row.size()]));
		                    }
	              
	                	}catch (Exception e) {
	                		e.printStackTrace();
	                		logger.debug("Debugging on error1: " + e.getStackTrace());
	                	}
	                }
	        		
	        	}
	        }
	        
	        proCsvWriter.flush();
	        proWriter.flush();
	        byte[] fileContent = fetchBytesFromFile(overallStudyReport);
	        addToZip(zipOutputStream, fileContent, OVERALL_STUDY_REPORT + ".csv");
	        
	        fileContent = null;
	        eq5DWriter.flush();
	        eq5DCsvWriter.flush();
	        fileContent = fetchBytesFromFile(eq5DReport);
	        addToZip(zipOutputStream, fileContent, EQ_5D + ".csv");
	        zipOutputStream.flush();
			
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("Debugging on error2: " + e.getStackTrace());
		}
		finally{
			proCsvWriter.close();
			proWriter.close();
			eq5DCsvWriter.close();
			eq5DWriter.close();
			zipOutputStream.close();
		}
		
	}
	
	public byte[] fetchBytesFromFile(File file) throws IOException{
		InputStream inputStream = new FileInputStream(file);
		byte[] fileContent = new byte[(int) file.length()];
		inputStream.read(fileContent);
		
		return fileContent;
	}
	
	public void addToZip(ZipOutputStream zipOutputStream, byte[] fileContent, String fileName) throws IOException{
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOutputStream.putNextEntry(zipEntry);
		zipOutputStream.write(fileContent);
		zipOutputStream.closeEntry();
	}
	
    private void buildLegend() {
    	int col = 0;
    	row = new ArrayList<String>();
    	row.add(col++, "Legend:");
    	row.add(col++, "");
    	
    	for (int i = 0; i <= 4; i++) {
    		row.add((col + i), String.valueOf(i));
    	}
    	col = 7;
    	row.add(col++, "-77");
    	row.add(col++, "-88");
    	row.add(col++, "-66");
    	row.add(col++, "-2000");
    	row.add(col++, "-99");
    	row.add(col++, "-55");
    	proCsvWriter.writeNext((String[]) row.toArray(new String[row.size()]));
    	
    	row.clear();
    	col = 0;
    	row.add(col++, "");
    	row.add(col++, ProCtcQuestionType.FREQUENCY.getDisplayName()+" ("+FREQUENCY+")");
    	
    	int j = 0;
        for (String value : ProCtcQuestionType.FREQUENCY.getValidValues()) {
        	row.add((col + j), value);
            j++;
        }
        col = 7;
    	row.add(col++, "Prefer not to answer");
    	row.add(col++, "Not applicable");
    	row.add(col++, "Not sexually active");
    	row.add(col++, "Not asked");
    	row.add(col++, FORCE_SKIP);
    	row.add(col++, MANUAL_SKIP);
    	proCsvWriter.writeNext((String[]) row.toArray(new String[row.size()]));
    	
    	row.clear();
    	col = 0;
    	row.add(col++, "");
    	row.add(col++, ProCtcQuestionType.SEVERITY.getDisplayName()+" ("+SEVERITY+")");
    	int k = 0;
        for (String value : ProCtcQuestionType.SEVERITY.getValidValues()) {
        	row.add((col + k), value);
            k++;
        }
        col = 7;
    	row.add(col++, "Prefer not to answer");
    	row.add(col++, "Not applicable");
    	row.add(col++, "Not sexually active");
    	row.add(col++, "Not asked");
    	row.add(col++, FORCE_SKIP);
    	row.add(col++, MANUAL_SKIP);
    	proCsvWriter.writeNext((String[]) row.toArray(new String[row.size()]));
    	
    	row.clear();
    	col = 0;
    	row.add(col++, "");
    	row.add(col++, ProCtcQuestionType.INTERFERENCE.getDisplayName()+" ("+INTERFERENCE+")");
    	int l = 0;
        for (String value : ProCtcQuestionType.INTERFERENCE.getValidValues()) {
        	row.add((col + l), value);
            l++;
        }
        col = 7;
    	row.add(col++, "Prefer not to answer");
    	row.add(col++, "Not applicable");
    	row.add(col++, "Not sexually active");
    	row.add(col++, "Not asked");
    	row.add(col++, FORCE_SKIP);
    	row.add(col++, MANUAL_SKIP);
    	proCsvWriter.writeNext((String[]) row.toArray(new String[row.size()]));
    	
    	row.clear();
    	col = 0;
    	row.add(col++, "");
    	row.add(col++, ProCtcQuestionType.PRESENT.getDisplayName()+" ("+PRESENT+")");
    	row.add(col++, "No");
    	row.add(col++, "Yes");
    	for (int n = 0; n <= 2; n++) {
    		row.add((col + n), "");
        }
    	col = 7;
    	row.add(col++, "Prefer not to answer");
    	row.add(col++, "Not applicable");
    	row.add(col++, "Not sexually active");
    	row.add(col++, "Not asked");
    	row.add(col++, FORCE_SKIP);
    	row.add(col++, MANUAL_SKIP);
    	proCsvWriter.writeNext((String[]) row.toArray(new String[row.size()]));
    	
    	
    	row.clear();
    	col = 0;
    	row.add(col++, "");
    	row.add(col++, ProCtcQuestionType.AMOUNT.getDisplayName()+" ("+AMOUNT+")");
    	int p = 0;
        for (String value : ProCtcQuestionType.AMOUNT.getValidValues()) {
        	row.add((col + p), value);
            p++;
        }
        col = 7;
    	row.add(col++, "Prefer not to answer");
    	row.add(col++, "Not applicable");
    	row.add(col++, "Not sexually active");
    	row.add(col++, "Not asked");
    	row.add(col++, FORCE_SKIP);
    	row.add(col++, MANUAL_SKIP);
    	
    	proCsvWriter.writeNext((String[]) row.toArray(new String[row.size()]));
	}
    
	private void createTableHeaders(List<String> proCtcTermHeaders, List<String> meddraTermHeaders) {
		row.clear();
    	row.add(0, "Participant ID");
    	row.add(1, "Study");
    	row.add(2, "Study Site");
    	row.add(3, "Survey name");
    	row.add(4, "First response date");
    	row.add(5, "Mode");
    	row.add(6, "Status");
		int col = 7;
		for(int i = 0; i<proCtcTermHeaders.size(); i++){
			row.add(col++, proCtcTermHeaders.get(i));
		}
		for(int i = 0; i<meddraTermHeaders.size(); i++){
			row.add(col++, meddraTermHeaders.get(i));
		}
		proCsvWriter.writeNext((String[]) row.toArray(new String[row.size()]));
	}
	
	private List<String> createRow(List<List<String>> rowSet, int rownum){
		List<String> newRow = new ArrayList<String>(maxRowSize);
		rowSet.add(rownum, newRow);
		return newRow;
	}
	
	private void markDefaultNotAdministered(List<String> row, int length){
    	String col;
    	
    	for (int i = 7; i<length + 7; i++){
    		row.add(i, String.valueOf(-2000));
    	}
    }
	
	private List<String> getRow(List<List<String>> rowSet, int rowNum){
		return rowSet.get(rowNum);
	}
	
    @SuppressWarnings("deprecation")
	private void createCurrentRowPrefix(List<String> row, String participant, String study, String studySite, String formName){
    	row.add(0, participant);
    	row.add(1, study);
    	row.add(2, studySite);
    	row.add(3, formName);
    }
    
    private String getCellNumberValidValue(Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping, 
    		Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping, String termEnglish, ProCtcQuestionType questionType){
    	
    	Map<ProCtcQuestionType, String> proTypeMap = proCtcQuestionMapping.get(termEnglish);
    	if(proTypeMap != null){
    		if(proTypeMap.get(questionType) != null){
    			return proTypeMap.get(questionType);
    		}
    	}
    	Map<ProCtcQuestionType, String> meddraTypeMap = meddraQuestionMapping.get(termEnglish);
    	if(meddraTypeMap != null){
    		if(meddraTypeMap.get(questionType) != null){
    			return meddraTypeMap.get(questionType);
    		}
    	}
    	return DEFAULT_POSITION;
    }
}

