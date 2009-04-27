package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.view.document.AbstractPdfView;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.jfree.chart.JFreeChart;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.model.Workbook;

import java.util.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class ParticipantCareExcelView extends AbstractExcelView {

    protected void buildExcelDocument(Map map, HSSFWorkbook hssfWorkbook, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results = (TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<Date> dates = (ArrayList<Date>) request.getSession().getAttribute("sessionDates");
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

        //Particpant
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue("Participant");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]");

        //Report run date
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue("Report run date");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue(DateUtils.format(new Date()));

        row = hssfSheet.createRow(rownum++);

        style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        row = hssfSheet.createRow(rownum++);
        cell = row.createCell((short) 0);
        cell.setCellValue("Symptom");
        cell.setCellStyle(style);


        cell = row.createCell((short) 1);
        cell.setCellValue("Attribute");
        cell.setCellStyle(style);

        short i = 2;
        for (Date date : dates) {
            cell = row.createCell(i++);
            cell.setCellValue(DateUtils.format(date));
            cell.setCellStyle(style);
        }

        row = hssfSheet.createRow(rownum++);
        HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
        style1.setFillForegroundColor(HSSFColor.AQUA.index);
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        for (ProCtcTerm proCtcTerm : results.keySet()) {
            row = hssfSheet.createRow(rownum++);
            cell = row.createCell((short) 0);
            cell.setCellValue(proCtcTerm.getTerm());
            cell.setCellStyle(style);


            HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = results.get(proCtcTerm);
            for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                i = 0;
                row = hssfSheet.createRow(rownum++);

                cell = row.createCell(i++);
                cell.setCellValue("");

                cell = row.createCell(i++);
                cell.setCellValue(proCtcQuestion.getProCtcQuestionType().getDisplayName());
                cell.setCellStyle(style1);
                ArrayList<ProCtcValidValue> validValues = questionMap.get(proCtcQuestion);
                for (ProCtcValidValue proCtcValidValue : validValues) {
                    cell = row.createCell(i++);
                    cell.setCellValue(proCtcValidValue.getValue());
                }
            }
        }


//        document.add(table);
//        float height = PageSize.A4.height() / 2;
//        float width = PageSize.A4.width();
//        int i = 0;
//        for (ProCtcTerm proCtcTerm : results.keySet()) {
//            if (i % 2 == 0) {
//                document.newPage();
//                i = 0;
//            }
//            ChartGenerator chartGenerator = new ChartGenerator();
//            PdfContentByte cb = pdfWriter.getDirectContent();
//            PdfTemplate tp = cb.createTemplate(width, height);
//            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
//            JFreeChart chart = chartGenerator.getChartForSymptom(results, dates, proCtcTerm.getId(), null);
//            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height - 20);
//            chart.draw(g2, r2D);
//            g2.dispose();
//            cb.addTemplate(tp, 0, i * height);
//            i++;
//        }
    }
}