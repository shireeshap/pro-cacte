package gov.nih.nci.ctcae.web.reports;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.*;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Element;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.Font;
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
        document.newPage();

        FontFactory.getFont("Times-Roman", 12, Font.BOLD);

        document.add(new Paragraph("STAFF FEEDBACK QUESTIONS", FontFactory.getFont("Times-Roman", 12, Font.BOLD)));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("1. Was the patient-reported symptom information used by clinical staff to information the CTCAE grading?"));
        document.add(new Paragraph("    O YES             O NO"));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("2. Who completed this form?"));
        document.add(new Paragraph("    O Clinician (MD/RN/PA)"));
        document.add(new Paragraph("    O Non-Clinical Research Staff"));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("3. Did the person completing this form see the patient?"));
        document.add(new Paragraph("    O YES             O NO"));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("4. Was the patient?s medical chart used to complete this form?"));
        document.add(new Paragraph("    O YES             O NO"));
    }
}
