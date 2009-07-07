package gov.nih.nci.ctcae.web.reports;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.*;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class ParticipantCarePdfView extends AbstractPdfView {
    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results = (TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<String> dates = (ArrayList<String>) request.getSession().getAttribute("sessionDates");
        Participant participant = (Participant) request.getSession().getAttribute("participant");
        Study study = (Study) request.getSession().getAttribute("study");
        CRF crf = (CRF) request.getSession().getAttribute("crf");
        StudySite studySite = (StudySite) request.getSession().getAttribute("studySite");
        //Study
        document.add(new Paragraph("Study: " + study.getShortTitle() + " [" + study.getAssignedIdentifier() + "]"));

        //CRF
        document.add(new Paragraph("Form: " + crf.getTitle()));

        //Study Site
        document.add(new Paragraph("Study site: " + studySite.getDisplayName()));

        //Particpant
        document.add(new Paragraph("Participant: " + participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));

        //Report run date
        document.add(new Paragraph("Report run date: " + DateUtils.format(new Date())));

        document.add(new Paragraph(" "));

        Object[] datesArr = dates.toArray();

        int numOfMaxColsInTable = 4;
        int currentIteration = 0;
        while (true) {
            int numOfColsInCurrentTable = 0;
            int alreadyDisplayedDates = currentIteration * numOfMaxColsInTable;
            int remainingDates = datesArr.length - alreadyDisplayedDates;
            if (remainingDates == 0 || remainingDates < 0) {
                break;
            }
            if (remainingDates / numOfMaxColsInTable > 0) {
                numOfColsInCurrentTable = numOfMaxColsInTable;
            } else {
                numOfColsInCurrentTable = remainingDates % numOfMaxColsInTable;
            }

            PdfPTable table = new PdfPTable(numOfColsInCurrentTable + 2);
            PdfPCell cell = new PdfPCell(new Paragraph("Symptom"));
            cell.setBackgroundColor(Color.lightGray);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Attribute"));
            cell.setBackgroundColor(Color.lightGray);
            table.addCell(cell);

            for (int j = 0; j < numOfColsInCurrentTable; j++) {
                int absIndex = currentIteration * numOfMaxColsInTable + j;
                cell = new PdfPCell(new Paragraph(DateUtils.format((Date) datesArr[absIndex])));
                cell.setBackgroundColor(Color.lightGray);
                table.addCell(cell);
            }

            for (ProCtcTerm proCtcTerm : results.keySet()) {
                cell = new PdfPCell(new Paragraph(proCtcTerm.getTerm()));
                cell.setBackgroundColor(Color.lightGray);
                table.addCell(cell);
                for (int i = 0; i < numOfColsInCurrentTable + 1; i++) {
                    table.addCell("");
                }
                HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = results.get(proCtcTerm);
                for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                    table.addCell("");
                    cell = new PdfPCell(new Paragraph(proCtcQuestion.getProCtcQuestionType().getDisplayName()));
                    cell.setBackgroundColor(new Color(161, 218, 215));
                    table.addCell(cell);
                    ArrayList<ProCtcValidValue> validValues = questionMap.get(proCtcQuestion);
                    Object[] validValuesArr = validValues.toArray();
                    for (int k = 0; k < numOfColsInCurrentTable; k++) {
                        int absIndex = currentIteration * numOfMaxColsInTable + k;
                        table.addCell(((ProCtcValidValue) validValuesArr[absIndex]).getValue());
                    }
                }

            }
            document.add(table);
            document.add(new Paragraph(" "));
            currentIteration++;

        }
        float height = PageSize.A4.height() / 2;
        float width = PageSize.A4.width();
        int i = 0;
        for (ProCtcTerm proCtcTerm : results.keySet()) {
            if (i % 2 == 0) {
                document.newPage();
                i = 0;
            }
            ParticipantLevelChartGenerator chartGenerator = new ParticipantLevelChartGenerator();
            PdfContentByte cb = pdfWriter.getDirectContent();
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
            JFreeChart chart = chartGenerator.getChartForSymptom(results, dates, proCtcTerm.getId(), null);
            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height - 20);
            chart.draw(g2, r2D);
            g2.dispose();
            cb.addTemplate(tp, 0, i * height);
            i++;
        }
    }
}