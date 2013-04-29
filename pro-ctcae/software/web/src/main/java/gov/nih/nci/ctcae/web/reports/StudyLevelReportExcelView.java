package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;

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
public class StudyLevelReportExcelView extends AbstractExcelView {

    protected void buildExcelDocument(Map map, HSSFWorkbook hssfWorkbook, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
    	TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> results = (TreeMap<Organization,TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>>) request.getSession().getAttribute("sessionResultsMap");
    	TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDatesMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>>) request.getSession().getAttribute("sessionDatesMap");
        Study study = (Study) request.getSession().getAttribute("study");
        CRF selectedCrf = (CRF) request.getSession().getAttribute("crf");
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
//        Region region = new Region(row.getRowNum(), (short) 1, row.getRowNum(), (short) 20);
//        hssfSheet.addMergedRegion(region);

        //CRF
        if(selectedCrf != null){
        	row = hssfSheet.createRow(rownum++);
            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString("Form"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(selectedCrf.getTitle()));
        }

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
        for (Organization organization : results.keySet()) {
        	TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> crfMap = results.get(organization);
        	for(CRF crf : crfMap.keySet()){
                row = hssfSheet.createRow(rownum++);
                cell = row.createCell((short) 0);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString("Study site"));
                cell = row.createCell((short) 1);
                cell.setCellValue(new HSSFRichTextString(organization.getDisplayName()));
                hssfSheet.createRow(rownum++);

                HSSFRow rowSymptomQuestion = hssfSheet.createRow(rownum++);
                short symptomCellNum = 0;
                cell = rowSymptomQuestion.createCell(symptomCellNum++);
                cell.setCellValue(new HSSFRichTextString("Survey name"));
                cell.setCellStyle(greyStyle);
                cell = rowSymptomQuestion.createCell(symptomCellNum++);
                cell.setCellValue(new HSSFRichTextString("Participant ID"));
                cell.setCellStyle(greyStyle);
                cell = rowSymptomQuestion.createCell(symptomCellNum++);
                cell.setCellValue(new HSSFRichTextString("Date"));
                cell.setCellStyle(greyStyle);

                TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> participantMap = crfMap.get(crf);
                for (Participant participant : participantMap.keySet()) {
                    TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap0 = participantMap.get(participant);
                    for (String proCtcTerm : symptomMap0.keySet()) {
                        LinkedHashMap<Question, ArrayList<ValidValue>> questionMap = symptomMap0.get(proCtcTerm);
                        for (Question question : questionMap.keySet()) {
                            ProCtcQuestion proQuestion = new ProCtcQuestion();
                            MeddraQuestion meddraQuestion = new MeddraQuestion();
                            cell = rowSymptomQuestion.createCell(symptomCellNum++);
                            cell.setCellStyle(greyStyle);
                            if (question instanceof ProCtcQuestion) {
                                proQuestion = (ProCtcQuestion) question;
                                cell.setCellValue(new HSSFRichTextString(proCtcTerm + "_" + proQuestion.getProCtcQuestionType().getDisplayName()));
                            }
                            if (question instanceof MeddraQuestion) {
                                meddraQuestion = (MeddraQuestion) question;
                                cell.setCellValue(new HSSFRichTextString(proCtcTerm + "_" + meddraQuestion.getProCtcQuestionType().getDisplayName()));
                            }


                        }
                    }
                    break;
                }

                LinkedHashMap<Participant, ArrayList<Date>> datesMap = crfDatesMap.get(crf);
                for (Participant participant : participantMap.keySet()) {

                    TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap = participantMap.get(participant);
                    short symptomCellRow = rownum;
                    short questionCellNum = 0;

                    ArrayList<Date> dates = null;
                    for (Participant participantT : datesMap.keySet()) {
                        if (participant.equals(participantT)) {
                            dates = datesMap.get(participant);
                            break;
                        }
                    }
                    if (dates != null) {
                        for (Date date : dates) {
                            row = hssfSheet.createRow(rownum++);
                            cell = row.createCell((short) 2);
                            cell.setCellStyle(centerStyle);
                            cell.setCellValue(new HSSFRichTextString(DateUtils.format(date)));
                        }
                    }

                    short cellNum = 3;
                    for (String proCtcTerm : symptomMap.keySet()) {
                        LinkedHashMap<Question, ArrayList<ValidValue>> questionMap = symptomMap.get(proCtcTerm);
                        for (Question proCtcQuestion : questionMap.keySet()) {
                            ArrayList<ValidValue> valuesList = questionMap.get(proCtcQuestion);
                            ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
                            MeddraValidValue meddraValidValue = new MeddraValidValue();
                            short index = 0;
                            for (ValidValue validValue : valuesList) {

                                row = hssfSheet.getRow(rownum - (valuesList.size() - index));
                                cell = row.createCell((short) 0);
                                cell.setCellStyle(centerStyle);
                                cell.setCellValue(crf.getTitle());
                                cell = row.createCell((short) 1);
                                cell.setCellStyle(centerStyle);
                                cell.setCellValue(participant.getStudyParticipantIdentifier());

                                cell = row.createCell(cellNum);
                                cell.setCellStyle(centerStyle);
                                if (validValue instanceof ProCtcValidValue) {
                                    proCtcValidValue = (ProCtcValidValue) validValue;
                                    cell.setCellValue(proCtcValidValue.getResponseCode());
                                }
                                if (validValue instanceof MeddraValidValue) {
                                    meddraValidValue = (MeddraValidValue) validValue;
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
                }
        	}
 
        }
        for (int i = 0; i < numOfColumns; i++) {
            hssfSheet.setColumnWidth((short) i, (short) 3555);
        }
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

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.FREQUENCY.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Forced skip"));
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

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.SEVERITY.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Forced skip"));
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

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.INTERFERENCE.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Forced skip"));
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


        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.PRESENT.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Forced skip"));
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

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.AMOUNT.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("Forced skip"));
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
        return rownum;
    }

}

