package gov.nih.nci.ctcae.web.reports;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Element;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: c3pr
 * Date: 11/22/11
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantLevelWorstSymptomReportPdfView extends AbstractPdfView {
    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>> results = (TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        List<ProCtcQuestionType> questionTypes = (List<ProCtcQuestionType>) request.getSession().getAttribute("questionTypes");
        Participant participant = (Participant) request.getSession().getAttribute("participant");
        Study study = (Study) request.getSession().getAttribute("study");

        document.add(new Paragraph("Study: " + study.getShortTitle() + " [" + study.getAssignedIdentifier() + "]"));
        //Participant
        document.add(new Paragraph("Participant: " + participant.getDisplayName()));
        //Report run date
        document.add(new Paragraph("Report run date: " + DateUtils.format(new Date())));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        PdfPCell cell = new PdfPCell(new Paragraph("Symptom"));
        cell.setBackgroundColor(Color.lightGray);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        for (ProCtcQuestionType proCtcQuestionType : questionTypes) {
            cell = new PdfPCell(new Paragraph(proCtcQuestionType.getDisplayName()));
            cell.setBackgroundColor(Color.lightGray);
            table.addCell(cell);
        }

        for (String[] term : results.keySet()) {
            cell = new PdfPCell(new Paragraph(term[1]));
            cell.setBackgroundColor(Color.lightGray);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            HashMap<Question, ArrayList<ProCtcValidValue>> questionMap = results.get(term);
            boolean addCellFlag = false;
            // For frequency type
            String frequencyValue = "-";
            for (Question question : questionMap.keySet()) {
                if (question.getQuestionType().getDisplayName().equals("Frequency")) {
                    List<ProCtcValidValue> proCtcValidValues = questionMap.get(question);
                    frequencyValue = proCtcValidValues.get(0).getValue(SupportedLanguageEnum.ENGLISH);
                }
            }
            cell = new PdfPCell(new Paragraph(frequencyValue));
            cell.setBackgroundColor(new Color(161, 218, 215));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            // For Interference type
            String interferenceValue = "-";
            for (Question question : questionMap.keySet()) {
                if (question.getQuestionType().getDisplayName().equals("Interference")) {
                    List<ProCtcValidValue> proCtcValidValues = questionMap.get(question);
                    interferenceValue = proCtcValidValues.get(0).getValue(SupportedLanguageEnum.ENGLISH);
                }
            }
            cell = new PdfPCell(new Paragraph(interferenceValue));
            cell.setBackgroundColor(new Color(161, 218, 215));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            // For Severity type
            String severityValue = "-";
            for (Question question : questionMap.keySet()) {
                if (question.getQuestionType().getDisplayName().equals("Severity")) {
                    List<ProCtcValidValue> proCtcValidValues = questionMap.get(question);
                    severityValue = proCtcValidValues.get(0).getValue(SupportedLanguageEnum.ENGLISH);
                }
            }
            cell = new PdfPCell(new Paragraph(severityValue));
            cell.setBackgroundColor(new Color(161, 218, 215));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            // For Amount type
            String amountValue = "-";
            for (Question question : questionMap.keySet()) {
                if (question.getQuestionType().getDisplayName().equals("Amount")) {
                    List<ProCtcValidValue> proCtcValidValues = questionMap.get(question);
                    amountValue = proCtcValidValues.get(0).getValue(SupportedLanguageEnum.ENGLISH);
                }
            }
            cell = new PdfPCell(new Paragraph(amountValue));
            cell.setBackgroundColor(new Color(161, 218, 215));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            // For Amount type
            String presentValue = "-";
            for (Question question : questionMap.keySet()) {
                if (question.getQuestionType().getDisplayName().equals("Present/Absent")) {
                    List<ProCtcValidValue> proCtcValidValues = questionMap.get(question);
                    presentValue = proCtcValidValues.get(0).getValue(SupportedLanguageEnum.ENGLISH);
                }
            }
            cell = new PdfPCell(new Paragraph(presentValue));
            cell.setBackgroundColor(new Color(161, 218, 215));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        document.add(table);


//        Object[] datesArr = dates.toArray();
//
//        int numOfMaxColsInTable = 4;
//        int currentIteration = 0;
//        while (true) {
//            int numOfColsInCurrentTable;
//            int alreadyDisplayedDates = currentIteration * numOfMaxColsInTable;
//            int remainingDates = datesArr.length - alreadyDisplayedDates;
//            if (remainingDates == 0 || remainingDates < 0) {
//                break;
//            }
//            if (remainingDates / numOfMaxColsInTable > 0) {
//                numOfColsInCurrentTable = numOfMaxColsInTable;
//            } else {
//                numOfColsInCurrentTable = remainingDates % numOfMaxColsInTable;
//            }
//
//            PdfPTable table = new PdfPTable(numOfColsInCurrentTable + 2);
//            PdfPCell cell = new PdfPCell(new Paragraph("Symptom"));
//            cell.setBackgroundColor(Color.lightGray);
//            table.addCell(cell);
//
//            cell = new PdfPCell(new Paragraph("Attribute"));
//            cell.setBackgroundColor(Color.lightGray);
//            table.addCell(cell);
//
//            for (int j = 0; j < numOfColsInCurrentTable; j++) {
//                int absIndex = currentIteration * numOfMaxColsInTable + j;
//                cell = new PdfPCell(new Paragraph((String) datesArr[absIndex]));
//                cell.setBackgroundColor(Color.lightGray);
//                table.addCell(cell);
//            }
//
//            for (String[] term : results.keySet()) {
//                cell = new PdfPCell(new Paragraph(term[1]));
//                cell.setBackgroundColor(Color.lightGray);
//                table.addCell(cell);
//                boolean first = true;
//                HashMap<Question, ArrayList<ProCtcValidValue>> questionMap = results.get(term);
//                for (Question question : questionMap.keySet()) {
//                    if (!first) {
//                        table.addCell("");
//                    }
//                    first = false;
//                    cell = new PdfPCell(new Paragraph(question.getQuestionType().getDisplayName()));
//                    cell.setBackgroundColor(new Color(161, 218, 215));
//                    table.addCell(cell);
//                    ArrayList<ProCtcValidValue> validValues = questionMap.get(question);
//                    Object[] validValuesArr = validValues.toArray();
//                    for (int k = 0; k < numOfColsInCurrentTable; k++) {
//                        int absIndex = currentIteration * numOfMaxColsInTable + k;
//                        if (validValuesArr.length > absIndex && validValuesArr[absIndex]!=null) {
//                            table.addCell(((ProCtcValidValue) validValuesArr[absIndex]).getValue(SupportedLanguageEnum.ENGLISH));
//                        } else {
//                            table.addCell("");
//                        }
//                    }
//                }
//            }
//            document.add(table);
//            document.add(new Paragraph(" "));
//            currentIteration++;
//
//        }
//        float height = PageSize.A4.height() / 2;
//        float width = PageSize.A4.width();
//        int i = 0;
//        for (String[] term : results.keySet()) {
//            if (i % 2 == 0) {
//                document.newPage();
//                i = 0;
//            }
//            ParticipantLevelChartGenerator chartGenerator = new ParticipantLevelChartGenerator();
//            PdfContentByte cb = pdfWriter.getDirectContent();
//            PdfTemplate tp = cb.createTemplate(width, height);
//            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
//            JFreeChart chart = chartGenerator.getChartForSymptom(results, dates, term[0], null, baselineDate);
//            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height - 20);
//            chart.draw(g2, r2D);
//            g2.dispose();
//            cb.addTemplate(tp, 0, i * height);
//            i++;
//        }
    }
}
