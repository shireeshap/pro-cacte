package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class StudyLevelFullReportExcelView extends AbstractExcelView {

	private static String BLANK = "Not Available";
	private static String DEFAULT_POSITION = "";
	
    @SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map map, HSSFWorkbook hssfWorkbook, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
    	
		TreeMap<Organization, TreeMap<CRF,TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> results = (TreeMap<Organization, TreeMap<CRF,TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>>) request.getSession().getAttribute("sessionResultsMap");
    	TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDateMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>>) request.getSession().getAttribute("sessionCRFDatesMap");
    	TreeMap<CRF, LinkedHashMap<Participant, ArrayList<String>>> crfModeMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<String>>>) request.getSession().getAttribute("sessionCRFModeMap");
    	TreeMap<CRF, LinkedHashMap<Participant, ArrayList<CrfStatus>>> crfStatusMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<CrfStatus>>>) request.getSession().getAttribute("sessionCRFStatusMap");
    	TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping = (TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionProCtcQuestionMapping");
        TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping = (TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionMeddraQuestionMapping");
        ArrayList<String> proCtcTermHeaders = (ArrayList<String>) request.getSession().getAttribute("sessionProCtcTermHeaders");
        ArrayList<String> meddraTermHeaders = (ArrayList<String>) request.getSession().getAttribute("sessionMeddraTermHeaders");  	
        Study study = (Study) request.getSession().getAttribute("study");
        short rownum = 0;
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setLocked(true);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        HSSFCellStyle greyStyle = getGreyStyle(hssfWorkbook);
        HSSFCellStyle centerStyle = getCenterStyle(hssfWorkbook);

        HSSFCellStyle aquaStyle = getAquaStyle(hssfWorkbook);
        //Study
        HSSFRow row = hssfSheet.createRow(rownum++);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Study"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(study.getDisplayName()));

        //Report run date
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Report run date"));
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(DateUtils.format(new Date())));

        //Blank row
        hssfSheet.createRow(rownum++);
        //Legend
        rownum = buildLegend(hssfSheet, rownum, aquaStyle);

        //Blank row
        hssfSheet.createRow(rownum++);
        int numOfColumns = 0;
        //Create main table header
        rownum = createTableHeaders(hssfSheet, greyStyle, rownum, proCtcTermHeaders, meddraTermHeaders);
        //Create main table content
        for(Organization organization : results.keySet()){
        	TreeMap<CRF,TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> crfMap = results.get(organization);
        	for(CRF crf: crfMap.keySet()){ 
        		TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> participantMap = crfMap.get(crf);
                for (Participant participant : participantMap.keySet()) {
                	try{
	                    TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap = participantMap.get(participant);
	                    short symptomCellRow = rownum;
	                    short questionCellNum = 0;
	
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
	                    if (dates != null && appModes!= null) {
	                    	int index = 0;
	                        for (Date date : dates) {
	                            row = hssfSheet.createRow(rownum++);
	                            markDefaultNotAdministered(row, (proCtcTermHeaders.size() + meddraTermHeaders.size()), centerStyle);
	                            cell = row.createCell((short) 4);
	                            cell.setCellStyle(centerStyle);
	                            cell.setCellValue(new HSSFRichTextString(DateUtils.format(date)));
	                            cell = row.createCell((short) 5);
	                            cell.setCellStyle(centerStyle);
	                            if(appModes.get(index) != null){
	                            	cell.setCellValue(new HSSFRichTextString(appModes.get(index)));
	                            } else {
	                            	cell.setCellValue(new HSSFRichTextString(BLANK));
	                            }
	                            cell = row.createCell((short) 6);
	                            cell.setCellStyle(centerStyle);
	                            cell.setCellValue(new HSSFRichTextString(statusList.get(index).getDisplayName()));
	                            index++;
	                        }
	                    }
	
	                    short cellNum = 7;
	                    short posNotFound = (short)(proCtcTermHeaders.size() + meddraTermHeaders.size() + 8);
	                    for (String proCtcTerm : symptomMap.keySet()) {
	                        LinkedHashMap<Question, ArrayList<ValidValue>> questionMap = symptomMap.get(proCtcTerm);
	                        for (Question proCtcQuestion : questionMap.keySet()) {
	                            ArrayList<ValidValue> valuesList = questionMap.get(proCtcQuestion);
	                            ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
	                            MeddraValidValue meddraValidValue = new MeddraValidValue();
	                            short index = 0;
	                            for (ValidValue validValue : valuesList) {
	
	                                row = hssfSheet.getRow(rownum - (valuesList.size() - index));
	                                
	                                createCurrentRowPrefix(row, participant.getStudyParticipantIdentifier(), study.getShortTitle(), organization.getDisplayName(), crf.getTitle(), centerStyle);
	                                String pos;
	                                int tempPos;
	                                if (validValue instanceof ProCtcValidValue) {
	                                    proCtcValidValue = (ProCtcValidValue) validValue;
	                                     pos = getCellNumberForProCtcValidValue(proCtcQuestionMapping, (ProCtcQuestion) proCtcQuestion);
	                                     tempPos = Integer.valueOf(pos) + 7;
	                                     if(!pos.equals(DEFAULT_POSITION)){
	                                    	 cellNum = (short) tempPos;
	                                     } else {
	                                    	 cellNum = posNotFound++;
	                                     }
	                                    cell = row.createCell(cellNum);
		                                cell.setCellStyle(centerStyle);
	                                    cell.setCellValue(proCtcValidValue.getResponseCode());
	                                }
	                                if (validValue instanceof MeddraValidValue) {
	                                    meddraValidValue = (MeddraValidValue) validValue;
	                                    pos = getCellNumberForMeddraValidValue(meddraQuestionMapping, (MeddraQuestion) proCtcQuestion);
	                                    tempPos = Integer.valueOf(pos) + 7;
	                                     if(!pos.equals(DEFAULT_POSITION)){
	                                    	 cellNum = (short) tempPos;
	                                     } else {
	                                    	 cellNum = posNotFound++;
	                                     }
	                                    cell = row.createCell(cellNum);
		                                cell.setCellStyle(centerStyle);
	                                    cell.setCellValue(meddraValidValue.getDisplayOrder());
	                                }
	                                index++;
	                            }
	                            cellNum++;
	                            if (cellNum > numOfColumns) {
	                                numOfColumns = cellNum;
	                            }
	                        }
	
	                    }
              
                	}catch (Exception e) {
                		e.printStackTrace();
                	}
                }
        		
        	}
        }
        
        for (int i = 0; i < numOfColumns; i++) {
            hssfSheet.setColumnWidth((short) i, (short) 3555);

        }
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
    
	private short createTableHeaders(HSSFSheet hssfSheet, HSSFCellStyle greyStyle, short rownum, 
							ArrayList<String> proCtcTermHeaders, ArrayList<String> meddraTermHeaders) {
		
		 HSSFRow headerRow = hssfSheet.createRow(rownum++);
         HSSFCell headerCell = headerRow.createCell((short) 0);
         headerCell.setCellValue(new HSSFRichTextString("Participant ID"));
         headerCell.setCellStyle(greyStyle);
         headerCell = headerRow.createCell((short) 1);
         headerCell.setCellValue(new HSSFRichTextString("Study"));
         headerCell.setCellStyle(greyStyle);
         headerCell = headerRow.createCell((short) 2);
         headerCell.setCellValue(new HSSFRichTextString("Study Site"));
         headerCell.setCellStyle(greyStyle);
         headerCell = headerRow.createCell((short) 3);
         headerCell.setCellValue(new HSSFRichTextString("Survey name"));
         headerCell.setCellStyle(greyStyle);
         headerCell = headerRow.createCell((short) 4);
         headerCell.setCellValue(new HSSFRichTextString("Survey start date"));
         headerCell.setCellStyle(greyStyle);
         headerCell = headerRow.createCell((short) 5);
         headerCell.setCellValue(new HSSFRichTextString("Mode"));
         headerCell.setCellStyle(greyStyle);
         headerCell = headerRow.createCell((short) 6);
         headerCell.setCellValue(new HSSFRichTextString("Status"));
         headerCell.setCellStyle(greyStyle);
         short col = 7;
         for(int i = 0; i<proCtcTermHeaders.size(); i++){
        	 headerCell = headerRow.createCell(col++);
             headerCell.setCellValue(new HSSFRichTextString(proCtcTermHeaders.get(i)));
             headerCell.setCellStyle(greyStyle);
         }
         for(int i = 0; i<meddraTermHeaders.size(); i++){
        	 headerCell = headerRow.createCell(col++);
             headerCell.setCellValue(new HSSFRichTextString(meddraTermHeaders.get(i)));
             headerCell.setCellStyle(greyStyle);
         }
         return rownum;
    }
    
    
    @SuppressWarnings("deprecation")
	private void createCurrentRowPrefix(HSSFRow row, String participant, String study, String studySite, String formName, HSSFCellStyle centerStyle){
    	HSSFCell cell;
    	cell = row.createCell((short) 0);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(participant);
        
        cell = row.createCell((short) 1);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(study);
    	
        cell = row.createCell((short) 2);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(studySite);
        
        cell = row.createCell((short) 3);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(formName);
    }
    
    private void markDefaultNotAdministered(HSSFRow row, int length, HSSFCellStyle centerStyle){
    	String col;
    	HSSFCell cell;
    	
    	for (int i = 7; i<length + 7; i++){
    		col = String.valueOf(i);
    		cell = row.createCell(Short.valueOf(col));
    		cell.setCellStyle(centerStyle);
    		cell.setCellValue(-2000);
    	}
    }

    private short buildLegend(HSSFSheet hssfSheet, short rownum, HSSFCellStyle aquaStyle) {
    	HSSFRow row;
    	HSSFCell cell;
    	
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Legend:"));
        cell.setCellStyle(aquaStyle);
        cell = row.createCell((short) 1);
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) 2);
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-99);
        cell = row.createCell((short) 3);
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-55);
        for (int i = 0; i <= 4; i++) {
            cell = row.createCell((short) (4 + i));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(i);
        }
        cell = row.createCell((short) (9));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-77);
        cell = row.createCell((short) (10));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-88);
        cell = row.createCell((short) (11));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-66);
        cell = row.createCell((short) (12));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-2000);
        
        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.FREQUENCY.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not answered"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Manual skip"));
        int j = 0;
        for (String value : ProCtcQuestionType.FREQUENCY.getValidValues()) {
            cell = row.createCell((short) (4 + j));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(new HSSFRichTextString(value));
            j++;
        }
        cell = row.createCell((short) (9));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Prefer not to answer"));
        cell = row.createCell((short) (10));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not applicable"));
        cell = row.createCell((short) (11));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not sexually active"));
        cell = row.createCell((short) (12));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not asked"));
        

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.SEVERITY.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not answered"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Manual skip"));
        int k = 0;
        for (String value : ProCtcQuestionType.SEVERITY.getValidValues()) {
            cell = row.createCell((short) (4 + k));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(new HSSFRichTextString(value));
            k++;
        }
        cell = row.createCell((short) (9));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Prefer not to answer"));
        cell = row.createCell((short) (10));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not applicable"));
        cell = row.createCell((short) (11));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not sexually active"));
        cell = row.createCell((short) (12));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not asked"));

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.INTERFERENCE.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not answered"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Manual skip"));
        int l = 0;
        for (String value : ProCtcQuestionType.INTERFERENCE.getValidValues()) {
            cell = row.createCell((short) (4 + l));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(new HSSFRichTextString(value));
            l++;
        }
        cell = row.createCell((short) (9));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Prefer not to answer"));
        cell = row.createCell((short) (10));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not applicable"));
        cell = row.createCell((short) (11));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not sexually active"));
        cell = row.createCell((short) (12));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not asked"));


        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.PRESENT.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not answered"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Manual skip"));

        cell = row.createCell((short) (4));
        cell.setCellStyle((aquaStyle));
        cell.setCellValue(new HSSFRichTextString("No"));

        cell = row.createCell((short) (5));
        cell.setCellStyle((aquaStyle));
        cell.setCellValue(new HSSFRichTextString("Yes"));


        for (int n = 0; n <= 2; n++) {
            cell = row.createCell((short) (6 + n));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(new HSSFRichTextString(""));
        }
        cell = row.createCell((short) (9));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Prefer not to answer"));
        cell = row.createCell((short) (10));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not applicable"));
        cell = row.createCell((short) (11));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not sexually active"));
        cell = row.createCell((short) (12));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not asked"));

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.AMOUNT.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not answered"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Manual skip"));
        int p = 0;
        for (String value : ProCtcQuestionType.AMOUNT.getValidValues()) {
            cell = row.createCell((short) (4 + p));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(new HSSFRichTextString(value));
            p++;
        }
        cell = row.createCell((short) (9));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Prefer not to answer"));
        cell = row.createCell((short) (10));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not applicable"));
        cell = row.createCell((short) (11));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not sexually active"));
        cell = row.createCell((short) (12));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Not asked"));
		
        return rownum;
	}

	private HSSFCellStyle getCenterStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setWrapText(true);
        return style;

    }

    private HSSFCellStyle getAquaStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.AQUA.index);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setWrapText(true);
        return style;

    }

    private HSSFCellStyle getGreyStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setWrapText(true);
        return style;

    }

}

