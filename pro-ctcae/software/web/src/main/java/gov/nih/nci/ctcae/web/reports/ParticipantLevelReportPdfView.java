package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.ValidValue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class ParticipantLevelReportPdfView extends AbstractPdfView {
    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results = (TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        ArrayList<String> dates = (ArrayList<String>) request.getSession().getAttribute("sessionDates");
        String baselineDate = (String) request.getSession().getAttribute("baselineDate");
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

        //Participant
        document.add(new Paragraph("Participant: " + participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));

        //Report run date
        document.add(new Paragraph("Report run date: " + DateUtils.format(new Date())));

        document.add(new Paragraph(" "));

        Object[] datesArr = dates.toArray();

        int numOfMaxColsInTable = 4;
        int currentIteration = 0;
        while (true) {
            int numOfColsInCurrentTable;
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
                cell = new PdfPCell(new Paragraph((String) datesArr[absIndex]));
                cell.setBackgroundColor(Color.lightGray);
                table.addCell(cell);
            }

            for (String[] term : results.keySet()) {
                cell = new PdfPCell(new Paragraph(term[1]));
                cell.setBackgroundColor(Color.lightGray);
                table.addCell(cell);
                boolean first = true;
                HashMap<Question, ArrayList<ValidValue>> questionMap = results.get(term);
                for (Question question : questionMap.keySet()) {
                    if (!first) {
                        table.addCell("");
                    }
                    first = false;
                    cell = new PdfPCell(new Paragraph(question.getQuestionType().getDisplayName()));
                    cell.setBackgroundColor(new Color(161, 218, 215));
                    table.addCell(cell);
                    ArrayList<ValidValue> validValues = questionMap.get(question);
                    Object[] validValuesArr = validValues.toArray();
                    for (int k = 0; k < numOfColsInCurrentTable; k++) {
                        int absIndex = currentIteration * numOfMaxColsInTable + k;
                        if (validValuesArr.length > absIndex && validValuesArr[absIndex]!=null) {
                            table.addCell(((ValidValue)validValuesArr[absIndex]).getValue(SupportedLanguageEnum.ENGLISH));
                        } else {
                            table.addCell("");
                        }
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
        for (String[] term : results.keySet()) {
            if (i % 2 == 0) {
                document.newPage();
                i = 0;
            }
            ParticipantLevelChartGenerator chartGenerator = new ParticipantLevelChartGenerator();
            PdfContentByte cb = pdfWriter.getDirectContent();
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
            JFreeChart chart = chartGenerator.getChartForSymptom(results, dates, term[0], null, baselineDate);
            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height - 20);
            chart.draw(g2, r2D);
            g2.dispose();
            cb.addTemplate(tp, 0, i * height);
            i++;
        }
    }
}