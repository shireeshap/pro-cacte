package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Gaurav Gupta
 * Date: 11/22/11
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantLevelWorstSymptomReportPdfView extends AbstractPdfView {

    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document dupe = new Document();
        PdfWriter dupePdfWriter = PdfWriter.getInstance(dupe, bos);
        int totalPages = 0;
        dupe.open();
        buildDocument(map, dupe, dupePdfWriter, request, httpServletResponse, totalPages);
        totalPages = dupePdfWriter.getPageNumber();
        buildDocument(map, document, pdfWriter, request, httpServletResponse, totalPages);
    }


    private void buildDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse, int totalPages) throws Exception {
        int total = totalPages;

        TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>> results = (TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        List<ProCtcQuestionType> questionTypes = (List<ProCtcQuestionType>) request.getSession().getAttribute("questionTypes");
        Participant participant = (Participant) request.getSession().getAttribute("participant");
        Study study = (Study) request.getSession().getAttribute("study");
        String reportStartDate = (String) request.getSession().getAttribute("reportStartDate");
        String reportEndDate = (String) request.getSession().getAttribute("reportEndDate");


        document.setPageSize(PageSize.A4);
        HeaderFooter footer = new HeaderFooter(new Phrase(" CONFIDENTIAL                                                                                                                          Page ", new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.NORMAL)), new Phrase(" of " + total, new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 12, com.lowagie.text.Font.NORMAL)));
        document.setFooter(footer);

        // Header table
        Table insideTable = new Table(2);
        insideTable.setWidth(110);
        insideTable.setWidths(new int[]{40, 70});
        insideTable.setPadding(1);

        Cell headerCell = new Cell(new Paragraph("  Study: ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);

        headerCell = new Cell(new Paragraph(study.getShortTitle() + " [" + study.getAssignedIdentifier() + "]", FontFactory.getFont("Times-Roman", 11)));
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthLeft(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);

        headerCell = new Cell(new Paragraph("  Participant: ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);

        headerCell = new Cell(new Paragraph(participant.getDisplayName(), FontFactory.getFont("Times-Roman", 11)));
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthLeft(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);

        headerCell = new Cell(new Paragraph("  Report begin date(mm/dd/yyyy): ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);


        headerCell = new Cell(new Paragraph(reportStartDate, FontFactory.getFont("Times-Roman", 11)));
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthLeft(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);

        headerCell = new Cell(new Paragraph("  Report end date(mm/dd/yyyy): ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);


        headerCell = new Cell(new Paragraph(reportEndDate, FontFactory.getFont("Times-Roman", 11)));
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthLeft(0);
        insideTable.addCell(headerCell);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        document.add(insideTable);

        Phrase blankParagraph = new Phrase(" ");
        document.add(blankParagraph);

        Table instructionsTable = new Table(1);
        instructionsTable.setWidth(110);
        instructionsTable.setBorderWidth(0);

        Paragraph instructionalParagraph = new Paragraph("INSTRUCTIONS: Complete and submit this form as required by the protocol.  Information in the upper right box must be completed.\n" +
                "1.MedDRA code: Use NCI CTCAE v4.x or most current version with MedDRA codes to grade each adverse event.  Grade = (-1) if category not evaluated.  Grade = 0 if category evaluated by event not reported.\n" +
                "2.AE is defined as adverse event.\n" +
                "3.TREATMENT ATTRIBUTION CODES: \n" +
                "1 = unrelated, 2 = unlikely, 3 = possible, 4 = probable, 5 = definite     ", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        headerCell = new Cell(instructionalParagraph);
        headerCell.setBorderWidth(0);
        instructionsTable.addCell(headerCell);
        document.add(instructionsTable);
    //    document.add(blankParagraph);
        document.add(Chunk.NEWLINE);

        // Data Table
        Table table = new Table(9);
        table.setWidth(110);
        table.setWidths(new int[]{11, 29, 12, 12, 10, 10, 7, 9, 10});
        table.setAlignment(Element.ALIGN_CENTER);
        table.setAlignment(Element.ALIGN_MIDDLE);
        table.setPadding(1);
        table.setCellsFitPage(true);

        Cell cell = new Cell(new Paragraph("MedDRA code", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Symptom", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        for (ProCtcQuestionType proCtcQuestionType : questionTypes) {
            cell = new Cell(new Paragraph(" " + proCtcQuestionType.getDisplayName(), FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
        cell = new Cell(new Paragraph("Clinician\n" +
                "CTC AE grade", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("CTC AE \n" +
                "Attribution", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        for (String[] term : results.keySet()) {

            cell = new Cell(new Paragraph(term[2], FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(221, 221, 221));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new Cell(new Paragraph(term[1], FontFactory.getFont("Times-Roman", 10)));
            //    cell.setBackgroundColor(Color.lightGray);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            HashMap<Question, ArrayList<ProCtcValidValue>> questionMap = results.get(term);
            // For frequency type
            String frequencyValue = "-";
            for (Question question : questionMap.keySet()) {
                if (question.getQuestionType().getDisplayName().equals("Frequency")) {
                    List<ProCtcValidValue> proCtcValidValues = questionMap.get(question);
                    frequencyValue = proCtcValidValues.get(0).getValue(SupportedLanguageEnum.ENGLISH);
                }
            }
            cell = new Cell(new Paragraph(frequencyValue, FontFactory.getFont("Times-Roman", 10)));
            //    cell.setBackgroundColor(new Color(161, 218, 215));
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
            cell = new Cell(new Paragraph(interferenceValue, FontFactory.getFont("Times-Roman", 10)));
            //    cell.setBackgroundColor(new Color(161, 218, 215));
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
            cell = new Cell(new Paragraph(severityValue, FontFactory.getFont("Times-Roman", 10)));
            //    cell.setBackgroundColor(new Color(161, 218, 215));
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
            cell = new Cell(new Paragraph(amountValue, FontFactory.getFont("Times-Roman", 10)));
            //   cell.setBackgroundColor(new Color(161, 218, 215));
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
            cell = new Cell(new Paragraph(presentValue, FontFactory.getFont("Times-Roman", 10)));
            //    cell.setBackgroundColor(new Color(161, 218, 215));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new Cell(new Paragraph(" "));
            table.addCell(cell);

            cell = new Cell(new Paragraph(" "));
            table.addCell(cell);
        }
        document.add(table);

        // Next Page
        document.newPage();
        // Extra symptom Table
        table = new Table(9);
        table.setWidth(110);
        table.setWidths(new int[]{11, 29, 12, 12, 10, 10, 7, 9, 10});
        table.setAlignment(Element.ALIGN_CENTER);
        table.setAlignment(Element.ALIGN_MIDDLE);
        table.setPadding(1);
        table.setCellsFitPage(true);

        cell = new Cell(new Paragraph("MedDRA code", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Symptom", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        for (ProCtcQuestionType proCtcQuestionType : questionTypes) {
            cell = new Cell(new Paragraph(" " + proCtcQuestionType.getDisplayName(), FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
        cell = new Cell(new Paragraph("Clinician\n" +
                "CTC AE grade", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("CTC AE \n" +
                "Attribution", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        for (int i = 0; i < 90; i++) {
            PdfPCell pdfPCell = new PdfPCell(new Paragraph(" a",FontFactory.getFont("Times-Roman", 10, Font.BOLD, Color.GRAY)));
            pdfPCell.setFixedHeight(40);
            pdfPCell.setMinimumHeight(40);
            cell = new Cell(new Paragraph(" a",FontFactory.getFont("Times-Roman", 10, Font.BOLD, Color.WHITE)));
            table.addCell(cell);
        }
        document.add(table);
        document.add(new Paragraph(" "));

        // feedback Questions
        document.add(new Paragraph("STAFF FEEDBACK QUESTIONS ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));

//        document.add(new Paragraph(" "));
        document.add(new Paragraph("1. Was the patient-reported symptom information used by clinical staff to information the CTCAE grading?", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O YES             O NO", FontFactory.getFont("Times-Roman", 10)));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("2. Who completed this form?", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O Clinician (MD/RN/PA)", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O Non-Clinical Research Staff", FontFactory.getFont("Times-Roman", 10)));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("3. Did the person completing this form see the patient?", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O YES             O NO", FontFactory.getFont("Times-Roman", 10)));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("4. Was the patient?s medical chart used to complete this form?", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O YES             O NO", FontFactory.getFont("Times-Roman", 10)));
    }
}
