package gov.nih.nci.ctcae.web.reports;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * @author mehul
 *         Date: Apr 18, 2011
 */
public class OverallStudyDataExcelView extends AbstractExcelView {

    protected void buildExcelDocument(Map map, HSSFWorkbook hssfWorkbook, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        List<Object[]> list = (List<Object[]>) request.getSession().getAttribute("list");
        short rownum = 0;
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        style.setLocked(true);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        HSSFRow row = hssfSheet.createRow(rownum++);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("Study"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 1);
        cell.setCellValue(new HSSFRichTextString("Site"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 2);
        cell.setCellValue(new HSSFRichTextString("Form"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 3);
        cell.setCellValue(new HSSFRichTextString("First name"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 4);
        cell.setCellValue(new HSSFRichTextString("Last name"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 5);
        cell.setCellValue(new HSSFRichTextString("Start date"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 6);
        cell.setCellValue(new HSSFRichTextString("Due date"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 7);
        cell.setCellValue(new HSSFRichTextString("Symptom"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 8);
        cell.setCellValue(new HSSFRichTextString("Question type"));
        cell.setCellStyle(style);

        cell = row.createCell((short) 9);
        cell.setCellValue(new HSSFRichTextString("Response"));
        cell.setCellStyle(style);
        short rownumber = 1;
        for (Object[] rowData : list) {
            HSSFRow newRow = hssfSheet.createRow(rownumber++);
            short cellnum = 0;
            for (Object data : rowData) {
               HSSFCell newCell = newRow.createCell(cellnum++);
                newCell.setCellValue(new HSSFRichTextString(""+data));
            }
        }

    }

}
