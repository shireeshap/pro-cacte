package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.ValidValue;

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
        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results = (TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
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
        insideTable.setWidths(new int[]{45, 65});
        insideTable.setPadding(5);

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

        headerCell = new Cell(new Paragraph("  Reporting period begin date(mm/dd/yyyy): ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
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

        headerCell = new Cell(new Paragraph("  Reporting period end date(mm/dd/yyyy): ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        headerCell.setBorderWidthBottom(0);
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

        Paragraph instructionalParagraph = new Paragraph("INSTRUCTIONS: ", FontFactory.getFont("Times-Roman", 10, Font.BOLD));
        instructionalParagraph.add(new Phrase("Complete and submit this form as required by the protocol.  Grade each symptom for the reporting period dates specified above. Please record additional symptoms under the section titled \"Clinician Reported Symptoms\".\n" +
                "Use " , FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        instructionalParagraph.add(new Phrase(" NCI CTCAE v4.x " , FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        instructionalParagraph.add(new Phrase("or most current version with MedDRA codes to grade each adverse event.  Grade = (-1) category not evaluated.", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));

        
        instructionalParagraph.add(new Paragraph("TREATMENT ATTRIBUTION CODES:" , FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        instructionalParagraph.add(new Phrase("1 = unrelated, 2 = unlikely, 3 = possible, 4 = probable, 5 = definite     ", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        headerCell = new Cell(instructionalParagraph);
        headerCell.setBorderWidth(0);
        instructionsTable.addCell(headerCell);
        document.add(instructionsTable);
        document.add(Chunk.NEWLINE);

        // survey solicited symptoms section header
        Table sssTitleTable = new Table(1);
        sssTitleTable.setWidth(110);
        sssTitleTable.setBorderWidth(0);
        Paragraph p = new Paragraph("SURVEY SOLICITED SYMPTOMS ", FontFactory.getFont("Times-Roman", 11, Font.BOLD | com.lowagie.text.Font.UNDERLINE));
        
        headerCell = new Cell(p);
        headerCell.setBorderWidth(0);
        sssTitleTable.addCell(headerCell);
        document.add(sssTitleTable);

        // Data Table
        Table table = new Table(7);
        table.setWidth(110);
        table.setWidths(new int[]{12, 32, 14, 14, 14, 10, 10});
        table.setAlignment(Element.ALIGN_CENTER);
        table.setAlignment(Element.ALIGN_MIDDLE);
        table.setPadding(1);
        table.setCellsFitPage(true);

        //metadata row
        Cell cell = new Cell(new Paragraph(" ", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setColspan(2);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph("Patient Reported", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(3);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph("Clinicians: Please complete", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(2);
        table.addCell(cell);
        //metadata row
        
        cell = new Cell(new Paragraph("MedDRA Code", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Symptom", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph("Severity", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Frequency", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Interference", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Grade", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Attribution", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        boolean additionalQuestionsPresent = false;

        for (String[] term : results.keySet()) {
            String symptomId = term[0];
            if (symptomId.startsWith("A_")) {
                additionalQuestionsPresent = true;
            } else {
                cell = new Cell(new Paragraph(term[2], FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
                cell.setBackgroundColor(new Color(221, 221, 221));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new Cell(new Paragraph(term[1], FontFactory.getFont("Times-Roman", 10)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                HashMap<Question, ArrayList<ValidValue>> questionMap = results.get(term);
                
                // For Severity type
                String severityValue = "-";
                for (Question question : questionMap.keySet()) {
                    if (question.getQuestionType().getDisplayName().equals("Severity")) {
                        List<ValidValue> proCtcValidValues = questionMap.get(question);
                        severityValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                    }
                    if (question.getQuestionType().getDisplayName().equals("Amount")) {
                        List<ValidValue> proCtcValidValues = questionMap.get(question);
                        severityValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                    }
                }
                cell = new Cell(new Paragraph(severityValue, FontFactory.getFont("Times-Roman", 10)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                
                
                // For frequency type
                String frequencyValue = "-";
                for (Question question : questionMap.keySet()) {
                    if (question.getQuestionType().getDisplayName().equals("Frequency")) {
                        List<ValidValue> proCtcValidValues = questionMap.get(question);
                        frequencyValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                    }
                    if (question.getQuestionType().getDisplayName().equals("Present/Absent")) {
                        List<ValidValue> proCtcValidValues = questionMap.get(question);
                        String presentValue = "";
                        if(proCtcValidValues.get(0) instanceof ProCtcValidValue){
                        	presentValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                        } else if(proCtcValidValues.get(0) instanceof MeddraValidValue){
                            presentValue = ((MeddraValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                        }
                        if (presentValue.trim().equals("Yes")) {
                            frequencyValue = "Present";
                        } else {
                            frequencyValue = "Absent";
                        }
                    }
                }
                cell = new Cell(new Paragraph(frequencyValue, FontFactory.getFont("Times-Roman", 10)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                // For Interference type
                String interferenceValue = "-";
                for (Question question : questionMap.keySet()) {
                    if (question.getQuestionType().getDisplayName().equals("Interference")) {
                        List<ValidValue> proCtcValidValues = questionMap.get(question);
                        interferenceValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                    }
                }
                cell = new Cell(new Paragraph(interferenceValue, FontFactory.getFont("Times-Roman", 10)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new Cell(new Paragraph(" "));
                table.addCell(cell);

                cell = new Cell(new Paragraph(" "));
                table.addCell(cell);
            }
        }
        document.add(table);

        // Patient Added Symptoms
        document.add(Chunk.NEWLINE);
        if (additionalQuestionsPresent) {
            // patient added symptoms section header
            Table patientTitleTable = new Table(1);
            patientTitleTable.setWidth(110);
            patientTitleTable.setBorderWidth(0);
            Paragraph patientTitle = new Paragraph("PATIENT ADDED SYMPTOMS ", FontFactory.getFont("Times-Roman", 11, Font.BOLD | com.lowagie.text.Font.UNDERLINE));
            
            headerCell = new Cell(patientTitle);
            headerCell.setBorderWidth(0);
            patientTitleTable.addCell(headerCell);
            document.add(patientTitleTable);
            
            table = new Table(7);
            table.setWidth(110);
            table.setWidths(new int[]{12, 32, 14, 14, 14, 10, 10});
            table.setAlignment(Element.ALIGN_CENTER);
            table.setAlignment(Element.ALIGN_MIDDLE);
            table.setPadding(1);
            table.setCellsFitPage(true);
            
            //metadata row
            cell = new Cell(new Paragraph(" ", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setColspan(2);
            table.addCell(cell);
            
            cell = new Cell(new Paragraph("Patient Reported", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(3);
            table.addCell(cell);
            
            cell = new Cell(new Paragraph("Clinicians: Please complete", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            table.addCell(cell);
            //metadata row

            cell = new Cell(new Paragraph("MedDRA Code", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new Cell(new Paragraph("Symptom", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            
            cell = new Cell(new Paragraph("Severity", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new Cell(new Paragraph("Frequency", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new Cell(new Paragraph("Interference", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new Cell(new Paragraph("Grade", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new Cell(new Paragraph("Attribution", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
            cell.setBackgroundColor(new Color(201, 201, 201));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            for (String[] term : results.keySet()) {
                String symptomId = term[0];
                if (symptomId.startsWith("A_")) {
                    cell = new Cell(new Paragraph(term[2], FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
                    cell.setBackgroundColor(new Color(221, 221, 221));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new Cell(new Paragraph(term[1], FontFactory.getFont("Times-Roman", 10)));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    HashMap<Question, ArrayList<ValidValue>> questionMap = results.get(term);
                    // For Severity type
                    String severityValue = "-";
                    for (Question question : questionMap.keySet()) {
                        if (question.getQuestionType().getDisplayName().equals("Severity")) {
                            List<ValidValue> proCtcValidValues = questionMap.get(question);
                            severityValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                        }
                        if (question.getQuestionType().getDisplayName().equals("Amount")) {
                            List<ValidValue> proCtcValidValues = questionMap.get(question);
                            severityValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                        }
                    }
                    cell = new Cell(new Paragraph(severityValue, FontFactory.getFont("Times-Roman", 10)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    
                    // For frequency type
                    String frequencyValue = "-";
                    for (Question question : questionMap.keySet()) {
                        if (question.getQuestionType().getDisplayName().equals("Frequency")) {
                            List<ValidValue> proCtcValidValues = questionMap.get(question);
                            frequencyValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                        }
                        if (question.getQuestionType().getDisplayName().equals("Present/Absent")) {
                            List<ValidValue> proCtcValidValues = questionMap.get(question);
                            String presentValue = "";
                            if(proCtcValidValues.get(0) instanceof ProCtcValidValue){
                            	presentValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                            } else if(proCtcValidValues.get(0) instanceof MeddraValidValue){
                                presentValue = ((MeddraValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                            }
                            
                            if (presentValue.equals("Yes")) {
                                frequencyValue = "Present";
                            } else {
                                frequencyValue = "Absent";
                            }
                        }
                    }
                    cell = new Cell(new Paragraph(frequencyValue, FontFactory.getFont("Times-Roman", 10)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    // For Interference type
                    String interferenceValue = "-";
                    for (Question question : questionMap.keySet()) {
                        if (question.getQuestionType().getDisplayName().equals("Interference")) {
                            List<ValidValue> proCtcValidValues = questionMap.get(question);
                            interferenceValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
                        }
                    }
                    cell = new Cell(new Paragraph(interferenceValue, FontFactory.getFont("Times-Roman", 10)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new Cell(new Paragraph(" "));
                    table.addCell(cell);

                    cell = new Cell(new Paragraph(" "));
                    table.addCell(cell);
                }
            }
            document.add(table);

        }


        // Next Page - Clinician reported symptoms
        //document.newPage();
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        // feedback Questions
        Table sfqTitleTable = new Table(1);
        sfqTitleTable.setWidth(110);
        sfqTitleTable.setBorderWidth(0);
        Paragraph sfqTitle = new Paragraph("STAFF FEEDBACK QUESTIONS ", FontFactory.getFont("Times-Roman", 11, Font.BOLD | com.lowagie.text.Font.UNDERLINE));
        
        headerCell = new Cell(sfqTitle);
        headerCell.setBorderWidth(0);
        sfqTitleTable.addCell(headerCell);
        document.add(sfqTitleTable);
        
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
        document.add(new Paragraph("4. Was the patients medical chart used to complete this form?", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O YES             O NO", FontFactory.getFont("Times-Roman", 10)));
        
        Table noteTitleTable = new Table(1);
        noteTitleTable.setWidth(110);
        noteTitleTable.setBorderWidth(0);
        Paragraph noteTitle = new Paragraph(new Paragraph("NOTE: ", FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        noteTitle.add(new Phrase("Please use the \"CLINICIAN REPORTED SYMPTOMS\" section below to document additional symptoms that were not reported by the patient.", FontFactory.getFont("Times-Roman", 11)));
        headerCell = new Cell(noteTitle);
        headerCell.setBorderWidth(0);
        noteTitleTable.addCell(headerCell);
        
        document.add(noteTitleTable);
        document.add(new Paragraph(" "));

        Table crsTitleTable = new Table(1);
        crsTitleTable.setWidth(110);
        crsTitleTable.setBorderWidth(0);
        Paragraph crsTitle = new Paragraph("CLINICIAN REPORTED SYMPTOMS", FontFactory.getFont("Times-Roman", 11, Font.BOLD | com.lowagie.text.Font.UNDERLINE));
        headerCell = new Cell(crsTitle);
        headerCell.setBorderWidth(0);
        crsTitleTable.addCell(headerCell);
        
        document.add(crsTitleTable);

        table = new Table(4);
        table.setWidth(110);
        table.setWidths(new int[]{12, 73, 13, 12});
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

        cell = new Cell(new Paragraph("Grade", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Attribution", FontFactory.getFont("Times-Roman", 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(201, 201, 201));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        for (int i = 0; i < 80; i++) {
            PdfPCell pdfPCell = new PdfPCell(new Paragraph(" a", FontFactory.getFont("Times-Roman", 10, Font.BOLD, Color.GRAY)));
            pdfPCell.setFixedHeight(40);
            pdfPCell.setMinimumHeight(40);
            cell = new Cell(new Paragraph(" a", FontFactory.getFont("Times-Roman", 10, Font.BOLD, Color.WHITE)));
            table.addCell(cell);
        }
        document.add(table);

    }
}
