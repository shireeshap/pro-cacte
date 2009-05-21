package gov.nih.nci.ctcae.web.reports;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class StudyLevelReportPdfView extends AbstractPdfView {

    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> results = (TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>) request.getSession().getAttribute("sessionResultsMap");
        LinkedHashMap<Participant, ArrayList<Date>> datesMap = (LinkedHashMap<Participant, ArrayList<Date>>) request.getSession().getAttribute("sessionDatesMap");
        Study study = (Study) request.getSession().getAttribute("study");
        CRF crf = (CRF) request.getSession().getAttribute("crf");
        StudySite studySite = (StudySite) request.getSession().getAttribute("studySite");

        //Study
        document.add(new Paragraph("Study: " + study.getShortTitle() + " [" + study.getAssignedIdentifier() + "]"));

        //CRF
        document.add(new Paragraph("Form: " + crf.getTitle()));

        //Study Site
        document.add(new Paragraph("Study site: " + studySite.getDisplayName()));


        //Report run date
        document.add(new Paragraph("Report run date: " + DateUtils.format(new Date())));


        for (Participant participant : results.keySet()) {
            document.add(new Paragraph(" "));
            //Particpant
            document.add(new Paragraph("Participant: " + participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));
            document.add(new Paragraph(" "));

            ArrayList valuesLists = new ArrayList();

            int numOfColumns = 1;
            TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap = results.get(participant);
            int i = 0;
            for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                numOfColumns = numOfColumns + questionMap.keySet().size();
                for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                    ArrayList<ProCtcValidValue> valuesList = questionMap.get(proCtcQuestion);
                    valuesLists.add(valuesList);
                }
            }

            PdfPTable table = new PdfPTable(numOfColumns);
            PdfPCell cell = new PdfPCell(new Paragraph(""));
            table.addCell(cell);
            for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                cell = new PdfPCell(new Paragraph(proCtcTerm.getTerm()));
                cell.setColspan(questionMap.keySet().size());
                cell.setBackgroundColor(Color.lightGray);
                table.addCell(cell);
            }
            cell = new PdfPCell(new Paragraph(""));
            table.addCell(cell);
            for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
                LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = symptomMap.get(proCtcTerm);
                for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                    cell = new PdfPCell(new Paragraph(proCtcQuestion.getProCtcQuestionType().getDisplayName()));
                    cell.setBackgroundColor(new Color(161, 218, 215));
                    table.addCell(cell);
                }
            }
            ArrayList<Date> dates = null;
            for (Participant participantT : datesMap.keySet()) {
                if (participant.equals(participantT)) {
                    dates = datesMap.get(participant);
                    break;
                }
            }

            int index = 0;
            for (Date date : dates) {
                cell = new PdfPCell(new Paragraph(DateUtils.format(date)));
                table.addCell(cell);
                for (Object obj : valuesLists) {
                    ArrayList<ProCtcValidValue> valueList = (ArrayList<ProCtcValidValue>) obj;
                    cell = new PdfPCell(new Paragraph(valueList.get(index).getValue()));
                    table.addCell(cell);
                }
                index++;
            }
            document.add(table);

        }
    }


}

