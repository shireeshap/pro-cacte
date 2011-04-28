package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
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
public class ParticipantLevelReportExcelView extends AbstractExcelView {

    protected void buildExcelDocument(Map map, HSSFWorkbook hssfWorkbook, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>> results = (TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<String> dates = (ArrayList<String>) request.getSession().getAttribute("sessionDates");
        Participant participant = (Participant) request.getSession().getAttribute("participant");
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
        cell.setCellValue(new HSSFRichTextString("Study"));
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(study.getDisplayName()));

        //CRF
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Form"));
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(crf.getTitle()));

        //Study Site
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("Study site"));
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(studySite.getDisplayName()));

        //Particpant
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Participant"));
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(participant.getDisplayName()));

        //Report run date
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Report run date"));
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString(DateUtils.format(new Date())));

        hssfSheet.createRow(rownum++);

        style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Symptom"));
        cell.setCellStyle(style);


        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString("Attribute"));
        cell.setCellStyle(style);

        short i = 2;
        for (String date : dates) {
            cell = row.createCell(i++);
            cell.setCellValue(new HSSFRichTextString(date));
            cell.setCellStyle(style);
        }

        hssfSheet.createRow(rownum++);
        HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
        style1.setFillForegroundColor(HSSFColor.AQUA.index);
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        for (String[] term : results.keySet()) {
            row = hssfSheet.createRow(rownum++);
            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString(term[1]));
            cell.setCellStyle(style);


            HashMap<Question, ArrayList<ProCtcValidValue>> questionMap = results.get(term);
            for (Question question : questionMap.keySet()) {
                i = 1;
                cell = row.createCell(i++);
                cell.setCellValue(new HSSFRichTextString(question.getQuestionType().getDisplayName()));
                cell.setCellStyle(style1);
                ArrayList<ProCtcValidValue> validValues = questionMap.get(question);
                for (ProCtcValidValue proCtcValidValue : validValues) {
                    cell = row.createCell(i++);
                    cell.setCellValue(new HSSFRichTextString(proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH)));
                }
                row = hssfSheet.createRow(rownum++);
            }
        }
        for (short j = 0; j < dates.size() + 2; j++) {
            hssfSheet.autoSizeColumn(j);
        }
    }
}