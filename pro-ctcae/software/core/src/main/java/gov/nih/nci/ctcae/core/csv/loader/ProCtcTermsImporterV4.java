package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;
import java.nio.charset.Charset;


/**
 * @author Mehul Gulati
 *         Date: Jan 15, 2009
 */

public class ProCtcTermsImporterV4 {

    public ProCtc loadProCtcTerms(boolean fromTestCase) throws IOException {
        File file;
        if (fromTestCase) {
            file = new File("/Users/Harsh/workspace/pro-ctcae/software/core/src/main/resources/ProCtcTerms_V4.xls");
        } else {
            file = new ClassPathResource("ProCtcTerms_V4.xls").getFile();
        }
        InputStream xls = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(xls);
        HSSFSheet sheet = wb.getSheetAt(0);
        int rowIndex = 2;
        short cellIndex = 0;
        HSSFRow row;
        while (true) {
            row = sheet.getRow(rowIndex);
            if (row == null) {
                break;
            }
            String question = row.getCell(cellIndex).getRichStringCellValue().getString();
            String proCtcTerm = row.getCell(++cellIndex).getRichStringCellValue().getString();
            String core = row.getCell(++cellIndex) == null ? "" : row.getCell(cellIndex).getRichStringCellValue().getString();
            String attribute = row.getCell(++cellIndex).getRichStringCellValue().getString();
            String validValues = row.getCell(++cellIndex).getRichStringCellValue().getString();
            String ctcTerm = row.getCell(++cellIndex).getRichStringCellValue().getString();
            String ctcCategory = row.getCell(++cellIndex).getRichStringCellValue().getString();
            System.out.println(question + " | " + proCtcTerm + " | " + core + " | " + attribute + " | " + validValues + " | " + ctcTerm + " | " + ctcCategory);

            cellIndex = 0;
            rowIndex++;
        }

        return null;
    }
}