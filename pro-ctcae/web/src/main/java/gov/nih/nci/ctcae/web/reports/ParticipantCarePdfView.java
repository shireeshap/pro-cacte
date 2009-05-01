package gov.nih.nci.ctcae.web.reports;

import com.lowagie.text.*;
import com.lowagie.text.Font;
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
        ArrayList<Date> dates = (ArrayList<Date>) request.getSession().getAttribute("sessionDates");
        Participant participant = (Participant) request.getSession().getAttribute("participant");
        Study study = (Study) request.getSession().getAttribute("study");
        CRF crf = (CRF) request.getSession().getAttribute("crf");
        StudySite studySite = (StudySite) request.getSession().getAttribute("studySite");

        PdfPTable table = new PdfPTable(dates.size() + 2);
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


        PdfPCell cell = new PdfPCell(new Paragraph("Symptom"));
        cell.setBackgroundColor(Color.lightGray);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Attribute"));
        cell.setBackgroundColor(Color.lightGray);
        table.addCell(cell);
        for (Date date : dates) {
            cell = new PdfPCell(new Paragraph(DateUtils.format(date)));
            cell.setBackgroundColor(Color.lightGray);
            table.addCell(cell);
        }

        for (ProCtcTerm proCtcTerm : results.keySet()) {
            cell = new PdfPCell(new Paragraph(proCtcTerm.getTerm()));
            cell.setBackgroundColor(Color.lightGray);
            table.addCell(cell);

            for (int i = 0; i < dates.size() + 1; i++) {
                table.addCell("");
            }
            HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> questionMap = results.get(proCtcTerm);
            for (ProCtcQuestion proCtcQuestion : questionMap.keySet()) {
                table.addCell("");
                cell = new PdfPCell(new Paragraph(proCtcQuestion.getProCtcQuestionType().getDisplayName()));
                cell.setBackgroundColor(new Color(161, 218, 215));
                table.addCell(cell);
                ArrayList<ProCtcValidValue> validValues = questionMap.get(proCtcQuestion);
                for (ProCtcValidValue proCtcValidValue : validValues) {
                    table.addCell(proCtcValidValue.getValue());
                }
            }
        }
        document.add(table);
        float height = PageSize.A4.height() / 2;
        float width = PageSize.A4.width();
        int i = 0;
        for (ProCtcTerm proCtcTerm : results.keySet()) {
            if (i % 2 == 0) {
                document.newPage();
                i = 0;
            }
            ChartGenerator chartGenerator = new ChartGenerator();
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