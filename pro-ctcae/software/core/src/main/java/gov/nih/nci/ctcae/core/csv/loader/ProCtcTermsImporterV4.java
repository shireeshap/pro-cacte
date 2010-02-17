package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;


/**
 * @author Mehul Gulati
 *         Date: Jan 15, 2009
 */

public class ProCtcTermsImporterV4 {
    private CtcTermRepository ctcTermRepository;

    public ProCtc loadProCtcTerms(boolean fromTestCase) throws IOException {
        InputStream xls;
        xls = new ClassPathResource("ProCtcTerms_V4.xls").getURL().openStream();
        HSSFWorkbook wb = new HSSFWorkbook(xls);
        HSSFSheet sheet = wb.getSheetAt(0);
        int rowIndex = 2;
        short cellIndex = 0;
        HSSFRow row;
        HashMap<String, List<CsvLine>> hm = new HashMap<String, List<CsvLine>>();
        HashMap<String, ProCtcQuestion> firstQuestions = new HashMap<String, ProCtcQuestion>();
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
            CsvLine csvLine = new CsvLine();

            String displayOrder = attribute.substring(0, attribute.indexOf('-'));
            String questionType = attribute.substring(attribute.indexOf('-') + 1);

            csvLine.setProctcTerm(proCtcTerm);
            csvLine.setCtcTerm(ctcTerm);
            csvLine.setDisplayOrder(displayOrder);
            csvLine.setQuestionType(questionType);
            csvLine.setQuestionText(question);
            csvLine.setProctcValidValues(validValues);
            csvLine.setCoreItem(!StringUtils.isBlank(core));

            if (hm.containsKey(csvLine.getProctcTerm())) {
                List list = hm.get(csvLine.getProctcTerm());
                list.add(csvLine);
            } else {
                ArrayList list = new ArrayList();
                list.add(csvLine);
                hm.put(csvLine.getProctcTerm(), list);
            }
            System.out.println(csvLine);

            cellIndex = 0;
            rowIndex++;
        }

        ProCtc proCtc = new ProCtc();
        proCtc.setProCtcVersion("4.0");
        proCtc.setReleaseDate(new Date());

        for (String hmKey : hm.keySet()) {
            List<CsvLine> list = hm.get(hmKey);
            String ctcTerm = list.get(0).getCtcTerm();
            String proCtcTerm = hmKey;
            CtcQuery ctcQuery = new CtcQuery();
            ctcQuery.filterByName(ctcTerm);
            List<CtcTerm> ctcTerms = ctcTermRepository.find(ctcQuery);
            CtcTerm objCtcTerm = null;
            if (ctcTerms.size() == 0) {
                System.out.println("Could not find ctc term for " + proCtcTerm + ". Skipping " + proCtcTerm);
            } else {
                if (ctcTerms.size() > 1) {
                    System.out.println("Multiple ctc terms found for " + proCtcTerm + ". Skipping " + proCtcTerm);
                } else {
                    objCtcTerm = ctcTerms.get(0);
                }
            }

            if (objCtcTerm != null) {
                ProCtcTerm objProCtcTerm = new ProCtcTerm();
                objProCtcTerm.setTerm(proCtcTerm);
                proCtc.addProCtcTerm(objProCtcTerm);
                objProCtcTerm.setCtcTerm(objCtcTerm);
                for (CsvLine hmValue : list) {
                    ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
                    proCtcQuestion.setQuestionText(hmValue.getQuestionText());
                    proCtcQuestion.setDisplayOrder(new Integer(hmValue.getDisplayOrder()));
                    proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.getByDisplayName(hmValue.getQuestionType()));
                    objProCtcTerm.addProCtcQuestion(proCtcQuestion);
                    objProCtcTerm.setCore(hmValue.isCoreItem());
                    StringTokenizer st1 = new StringTokenizer(hmValue.getProctcValidValues(), "/");
                    int j = 0;
                    while (st1.hasMoreTokens()) {
                        ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
                        proCtcValidValue.setValue(st1.nextToken());
                        if (proCtcQuestion.getProCtcQuestionType().equals(ProCtcQuestionType.PRESENT)) {
                            if (proCtcValidValue.getValue().equals("Yes")) {
                                proCtcValidValue.setDisplayOrder(1);
                            } else {
                                proCtcValidValue.setDisplayOrder(0);
                            }
                        } else {
                            proCtcValidValue.setDisplayOrder(j);
                        }
                        j++;
                        proCtcQuestion.addValidValue(proCtcValidValue);
                    }
                }
            }

        }
        return proCtc;
    }


    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

}