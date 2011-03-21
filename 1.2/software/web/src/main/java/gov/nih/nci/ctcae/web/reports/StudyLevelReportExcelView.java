package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class StudyLevelReportExcelView extends AbstractExcelView {

    protected void buildExcelDocument(Map map, HSSFWorkbook hssfWorkbook, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        TreeMap<Organization, TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>> results = (TreeMap<Organization, TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>>) request.getSession().getAttribute("sessionResultsMap");
        LinkedHashMap<Participant, ArrayList<Date>> datesMap = (LinkedHashMap<Participant, ArrayList<Date>>) request.getSession().getAttribute("sessionDatesMap");
        Study study = (Study) request.getSession().getAttribute("study");
        CRF crf = (CRF) request.getSession().getAttribute("crf");
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
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Form"));
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(crf.getTitle()));

        //Report run date
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Report run date"));
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(DateUtils.format(new Date())));

        hssfSheet.createRow(rownum++);
        //Legend
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Legend:"));
        cell.setCellStyle(aquaStyle);
        cell = row.createCell((short) 1);
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) 2);
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-9);
        cell = row.createCell((short) 3);
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(-1);
        for (int i = 0; i <= 4; i++) {
            cell = row.createCell((short) (4 + i));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(i);
        }
        cell = row.createCell((short) (9));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(10);
        cell = row.createCell((short) (10));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(11);

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.FREQUENCY.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("N/A"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("no response"));
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
        cell.setCellValue(new HSSFRichTextString("Not active"));

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.SEVERITY.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("N/A"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("no response"));
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
        cell.setCellValue(new HSSFRichTextString("Not active"));

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.INTERFERENCE.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("N/A"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("no response"));
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
        cell.setCellValue(new HSSFRichTextString("Not active"));

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.PRESENT.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("N/A"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("no response"));
        int m = 0;
        for (String value : ProCtcQuestionType.PRESENT.getValidValues()) {
            cell = row.createCell((short) (4 + m));
            cell.setCellStyle(aquaStyle);
            cell.setCellValue(new HSSFRichTextString(value));
            m++;
        }
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
        cell.setCellValue(new HSSFRichTextString("Not active"));

        row = hssfSheet.createRow(rownum++);

        cell = row.createCell((short) (0));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(""));
        cell = row.createCell((short) (1));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString(ProCtcQuestionType.AMOUNT.getDisplayName()));
        cell = row.createCell((short) (2));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("N/A"));
        cell = row.createCell((short) (3));
        cell.setCellStyle(aquaStyle);
        cell.setCellValue(new HSSFRichTextString("no response"));
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
        cell.setCellValue(new HSSFRichTextString("Not active"));

        hssfSheet.createRow(rownum++);


        int numOfColumns = 0;
        for (Organization organization : results.keySet()) {
            //Study Site
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
            cell.setCellValue(new HSSFRichTextString("Participant ID"));
            cell.setCellStyle(greyStyle);
            cell = rowSymptomQuestion.createCell(symptomCellNum++);
            cell.setCellValue(new HSSFRichTextString("Date"));
            cell.setCellStyle(greyStyle);

            TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> participantMap = results.get(organization);
            for (Participant participant : participantMap.keySet()) {
                TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap0 = participantMap.get(participant);
                for (ProCtcTerm proCtcTerm : symptomMap0.keySet()) {
                    LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap0.get(proCtcTerm);
                    for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                        cell = rowSymptomQuestion.createCell(symptomCellNum++);
                        cell.setCellStyle(greyStyle);
                        cell.setCellValue(new HSSFRichTextString(proCtcTerm.getTerm() + "_" + proCtcQuestion.getProCtcQuestionType().getDisplayName()));
                    }
                }
                break;
            }

            for (Participant participant : participantMap.keySet()) {

                TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap = participantMap.get(participant);
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
                        cell = row.createCell((short) 1);
                        cell.setCellStyle(centerStyle);
                        cell.setCellValue(new HSSFRichTextString(DateUtils.format(date)));
                    }
                }

                short cellNum = 2;
                for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                    LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                    for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                        ArrayList<ProCtcValidValue> valuesList = questionMap.get(proCtcQuestion);

                        short index = 0;
                        for (ProCtcValidValue proCtcValidValue : valuesList) {
                            row = hssfSheet.getRow(rownum - (valuesList.size() - index));
                            cell = row.createCell((short) 0);
                            cell.setCellStyle(centerStyle);
                            cell.setCellValue(participant.getStudyParticipantIdentifier());

                            cell = row.createCell(cellNum);
                            cell.setCellStyle(centerStyle);
                            cell.setCellValue(proCtcValidValue.getDisplayOrder());
                            index++;
                        }
                        cellNum++;
                        if (cellNum > numOfColumns) {
                            numOfColumns = cellNum;
                        }
                    }

//                    region = new Region();
//                    region.setRowFrom(symptomCellRow);
//                    region.setRowTo(symptomCellRow);
//                    region.setColumnFrom(symptomIndex);
//                    region.setColumnTo((short) (symptomIndex + questionMap.size() - 1));
//                    hssfSheet.addMergedRegion(region);
//                    symptomIndex = (short) (symptomIndex + questionMap.size());

                }
            }
        }
        for (int i = 0; i < numOfColumns; i++) {
              hssfSheet.setColumnWidth((short)i, (short)3555);
           
//            hssfSheet.autoSizeColumn((short) i);
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

}

