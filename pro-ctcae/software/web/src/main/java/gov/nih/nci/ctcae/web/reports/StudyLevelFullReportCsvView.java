package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.MeddraQuestion;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	CSVWriter writer = null;
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
	
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		TreeMap<Organization, TreeMap<CRF,TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> results = (TreeMap<Organization, TreeMap<CRF,TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>>) request.getSession().getAttribute("sessionResultsMap");
    	TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDateMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>>) request.getSession().getAttribute("sessionCRFDatesMap");
    	TreeMap<CRF, LinkedHashMap<Participant, ArrayList<String>>> crfModeMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<String>>>) request.getSession().getAttribute("sessionCRFModeMap");
    	TreeMap<CRF, LinkedHashMap<Participant, ArrayList<CrfStatus>>> crfStatusMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<CrfStatus>>>) request.getSession().getAttribute("sessionCRFStatusMap");
    	TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping = (TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionProCtcQuestionMapping");
        TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping = (TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionMeddraQuestionMapping");
        ArrayList<String> proCtcTermHeaders = (ArrayList<String>) request.getSession().getAttribute("sessionProCtcTermHeaders");
        ArrayList<String> meddraTermHeaders = (ArrayList<String>) request.getSession().getAttribute("sessionMeddraTermHeaders");  	
        Study study = (Study) request.getSession().getAttribute("study");
        maxRowSize = (7+ proCtcTermHeaders.size() + meddraTermHeaders.size());
        
		emptyRow = new String[maxRowSize];
		
		try{
			response.setHeader("Content-Disposition", "attachment;filename=\"StudyLevelFullCSVReport.csv\"");
			response.setContentType("text/csv");
			writer = new CSVWriter(response.getWriter());
			
			reportInformation = new ArrayList<String>();
			reportInformation.add(0, "Study");
			reportInformation.add(1,study.getDisplayName());
			writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
			
			reportInformation = new ArrayList<String>();
			reportInformation.add(0, "Report run date");
			reportInformation.add(1, DateUtils.format(new Date()));
			writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));

			//Blank row
			writer.writeNext(emptyRow);
			//Legend
			buildLegend();
			
			//Blank row
			writer.writeNext(emptyRow);
	        int numOfColumns = 0;
	        //Create main table header
	        createTableHeaders(proCtcTermHeaders, meddraTermHeaders);
	        
	        for(Organization organization : results.keySet()){
	        	TreeMap<CRF,TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> crfMap = results.get(organization);
	        	for(CRF crf: crfMap.keySet()){ 
	        		TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> participantMap = crfMap.get(crf);
	                for (Participant participant : participantMap.keySet()) {
	                	try{
		                    TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap = participantMap.get(participant);
		                    int questionCellNum = 0;
		
		                    LinkedHashMap<Participant, ArrayList<Date>> datesMap = crfDateMap.get(crf); 
		                    LinkedHashMap<Participant, ArrayList<String>> modesMap = crfModeMap.get(crf);
		                    LinkedHashMap<Participant, ArrayList<CrfStatus>> statusMap = crfStatusMap.get(crf);
		                    ArrayList<Date> dates = null;
		                    ArrayList<String> appModes = null;
		                    ArrayList<CrfStatus> statusList = null;
		                    
		                    for (Participant participantD : datesMap.keySet()) {
		                        if (participant.equals(participantD)) {
		                            dates = datesMap.get(participant);
		                            break;
		                        }
		                    }
		                    for(Participant participantM : modesMap.keySet()){
		                    	if(participant.equals(participantM)){
		                    		appModes = modesMap.get(participant);
		                    		break;
		                    	}
		                    }
		                    for(Participant participantS : statusMap.keySet()){
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
		                            createCurrentRowPrefix(row, participant.getStudyParticipantIdentifier(), study.getShortTitle(), organization.getDisplayName(), crf.getTitle());
		                            row.add(4, DateUtils.format(date));
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
		                    for (String proCtcTerm : symptomMap.keySet()) {
		                        LinkedHashMap<Question, ArrayList<ValidValue>> questionMap = symptomMap.get(proCtcTerm);
		                        for (Question proCtcQuestion : questionMap.keySet()) {
		                            ArrayList<ValidValue> valuesList = questionMap.get(proCtcQuestion);
		                            ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
		                            MeddraValidValue meddraValidValue = new MeddraValidValue();
		                            int index = 0;
		                            for (ValidValue validValue : valuesList) {
		
		                                row = getRow(rowSet, (rownum - (valuesList.size() - index)));
		                                
		                                String pos;
		                                int tempPos;
		                                if (validValue instanceof ProCtcValidValue) {
		                                    proCtcValidValue = (ProCtcValidValue) validValue;
		                                     pos = getCellNumberForProCtcValidValue(proCtcQuestionMapping, (ProCtcQuestion) proCtcQuestion);
		                                     tempPos = Integer.valueOf(pos) + 7;
		                                     if(!pos.equals(DEFAULT_POSITION)){
		                                    	 cellNum = tempPos;
		                                    	 row.set(cellNum, proCtcValidValue.getResponseCode().toString());
		                                     } else {
		                                    	 logger.debug("No column found for "+ proCtcQuestion);
		                                    	 continue;
		                                     }
		                                }
		                                if (validValue instanceof MeddraValidValue) {
		                                    meddraValidValue = (MeddraValidValue) validValue;
		                                    pos = getCellNumberForMeddraValidValue(meddraQuestionMapping, (MeddraQuestion) proCtcQuestion);
		                                    tempPos = Integer.valueOf(pos) + 7;
		                                     if(!pos.equals(DEFAULT_POSITION)){
		                                    	 cellNum = tempPos;
		                                    	 row.set(cellNum, meddraValidValue.getDisplayOrder().toString());
		                                     } else {
		                                    	 logger.debug("No column found for "+ proCtcQuestion);
		                                    	 continue;
		                                     }
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
		                    	writer.writeNext(row.toArray(new String[row.size()]));
		                    }
	              
	                	}catch (Exception e) {
	                		e.printStackTrace();
	                	}
	                }
	        		
	        	}
	        }
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			writer.flush();
			writer.close();
		}
		
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
    	writer.writeNext((String[]) row.toArray(new String[row.size()]));
    	
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
    	writer.writeNext((String[]) row.toArray(new String[row.size()]));
    	
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
    	writer.writeNext((String[]) row.toArray(new String[row.size()]));
    	
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
    	writer.writeNext((String[]) row.toArray(new String[row.size()]));
    	
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
    	writer.writeNext((String[]) row.toArray(new String[row.size()]));
    	
    	
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
    	
    	writer.writeNext((String[]) row.toArray(new String[row.size()]));
	}
    
	private void createTableHeaders(ArrayList<String> proCtcTermHeaders, ArrayList<String> meddraTermHeaders) {
		row.clear();
    	row.add(0, "Participant ID");
    	row.add(1, "Study");
    	row.add(2, "Study Site");
    	row.add(3, "Survey name");
    	row.add(4, "Survey start date");
    	row.add(5, "Mode");
    	row.add(6, "Status");
		int col = 7;
		for(int i = 0; i<proCtcTermHeaders.size(); i++){
			row.add(col++, proCtcTermHeaders.get(i));
		}
		for(int i = 0; i<meddraTermHeaders.size(); i++){
			row.add(col++, meddraTermHeaders.get(i));
		}
		writer.writeNext((String[]) row.toArray(new String[row.size()]));
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
    
    private String getCellNumberForProCtcValidValue(TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping, ProCtcQuestion proCtcQuestion){
    	TreeMap<ProCtcQuestionType, String> typeMap = proCtcQuestionMapping.get(proCtcQuestion.getProCtcTerm());
    	if(typeMap != null){
    		if(typeMap.get(proCtcQuestion.getProCtcQuestionType()) != null){
    			return typeMap.get(proCtcQuestion.getProCtcQuestionType());
    		}
    	}
    	return DEFAULT_POSITION;
    }
    
    private String getCellNumberForMeddraValidValue(TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping, MeddraQuestion meddraQuestion){
    	TreeMap<ProCtcQuestionType, String> typeMap = meddraQuestionMapping.get(meddraQuestion.getLowLevelTerm());
    	if(typeMap != null){
    		if(typeMap.get(meddraQuestion.getProCtcQuestionType()) != null){
    			return typeMap.get(meddraQuestion.getProCtcQuestionType());
    		}
    	}
    	return DEFAULT_POSITION;
    }
}

