package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
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
        TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> results = (TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>) request.getSession().getAttribute("sessionResultsMap");
        LinkedHashMap<Participant, ArrayList<Date>> datesMap = (LinkedHashMap<Participant, ArrayList<Date>>) request.getSession().getAttribute("sessionDatesMap");
        Study study = (Study) request.getSession().getAttribute("study");
        CRF crf = (CRF) request.getSession().getAttribute("crf");
        StudySite studySite = (StudySite) request.getSession().getAttribute("studySite");
        short rownum = 0;
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();

        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setLocked(true);

        //Study
        HSSFRow row = hssfSheet.createRow(rownum++);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Study");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(study.getShortTitle() + " [" + study.getAssignedIdentifier() + "]");

        //CRF
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue("Form");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(crf.getTitle());

        //Study Site
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellStyle(style);
        cell.setCellValue("Study site");
        cell = row.createCell((short) 1);
        cell.setCellValue(studySite.getDisplayName());

        //Report run date
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue("Report run date");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(DateUtils.format(new Date()));

        row = hssfSheet.createRow(rownum++);

        style = getGreyStyle(hssfWorkbook);
        HSSFCellStyle centerStyle = getCenterStyle(hssfWorkbook);

        HSSFCellStyle styleAqua = getAquaStyle(hssfWorkbook);

        row = hssfSheet.createRow(rownum++);
        int numOfColumns = 0;
        for (Participant participant : results.keySet()) {

            row = hssfSheet.createRow(rownum++);
            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString("Participant"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue(new HSSFRichTextString(participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));

            TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap = results.get(participant);
            short symptomCellRow = rownum;
            HSSFRow rowSymptom = hssfSheet.createRow(rownum++);
            HSSFRow rowQuestion = hssfSheet.createRow(rownum++);

            short symptomCellNum = 0;
            short questionCellNum = 0;
            short symptomIndex = 1;

            cell = rowSymptom.createCell(symptomCellNum++);
            cell.setCellValue(new HSSFRichTextString(""));
            cell = rowQuestion.createCell(questionCellNum++);
            cell.setCellValue(new HSSFRichTextString(""));

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
                    cell = row.createCell((short) 0);
                    cell.setCellValue(new HSSFRichTextString(DateUtils.format(date)));
                }
            }

            short cellNum = 1;
            for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                    ArrayList<ProCtcValidValue> valuesList = questionMap.get(proCtcQuestion);
                    cell = rowSymptom.createCell(symptomCellNum++);
                    cell.setCellValue(new HSSFRichTextString(proCtcTerm.getTerm()));
                    cell.setCellStyle(centerStyle);

                    cell = rowQuestion.createCell(questionCellNum++);
                    cell.setCellValue(new HSSFRichTextString(proCtcQuestion.getProCtcQuestionType().getDisplayName()));
                    cell.setCellStyle(styleAqua);

                    short index = 0;
                    for (ProCtcValidValue proCtcValidValue : valuesList) {
                        row = hssfSheet.getRow(rownum - (valuesList.size() - index));
                        cell = row.createCell(cellNum);
                        cell.setCellValue(new HSSFRichTextString(proCtcValidValue.getValue()));
                        index++;
                    }
                    cellNum++;
                    if (cellNum > numOfColumns) {
                        numOfColumns = cellNum;
                    }
                }

                Region region = new Region();
                region.setRowFrom(symptomCellRow);
                region.setRowTo(symptomCellRow);
                region.setColumnFrom(symptomIndex);
                region.setColumnTo((short) (symptomIndex + questionMap.size() - 1));
                hssfSheet.addMergedRegion(region);
                symptomIndex = (short) (symptomIndex + questionMap.size());

            }
            row = hssfSheet.createRow(rownum++);
        }

        for (int i = 0; i < numOfColumns; i++) {
            hssfSheet.autoSizeColumn((short) i);
        }
    }

    private HSSFCellStyle getCenterStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return style;

    }

    private HSSFCellStyle getAquaStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.AQUA.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return style;

    }

    private HSSFCellStyle getGreyStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return style;

    }
}

