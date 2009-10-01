package gov.nih.nci.ctcae.web.participant;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.*;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

/**
 * author Mehul Gulati
 * Date: Jun 17, 2009
 */
public class PrintSchedulePdfView extends AbstractPdfView {

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Integer id = Integer.parseInt(request.getParameter("id"));
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(id);
        Study study = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getStudy();
        StudyOrganization studySite = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite();
        Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
        HashMap<ProCtcTerm, ArrayList<ProCtcQuestion>> symptomMap = new HashMap();
        ArrayList<ProCtcQuestion> proCtcQuestions;

        document.add(new Paragraph("Study: " + study.getDisplayName()));
        document.add(new Paragraph("Form: " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle()));
        document.add(new Paragraph("Study site: " + studySite.getDisplayName()));
        document.add(new Paragraph("Participant: " + participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));
        document.add(new Paragraph("Start date: " + DateUtils.format(studyParticipantCrfSchedule.getStartDate())));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));


        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
            ProCtcTerm proCtcTerm = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm();
            if (symptomMap.containsKey(proCtcTerm)) {
                proCtcQuestions = symptomMap.get(proCtcTerm);
            } else {
                proCtcQuestions = new ArrayList<ProCtcQuestion>();
                symptomMap.put(proCtcTerm, proCtcQuestions);
            }
            proCtcQuestions.add(studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion());
        }
        Font f = FontFactory.getFont("Arial", 9f);
        Font f1 = FontFactory.getFont("Arial", 13f);

        for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
            PdfPTable table = new PdfPTable(new float[]{2.5f, 1f, 1f, 1f, 1f, 1f});
            table.setWidthPercentage(98);
            PdfPCell cell = new PdfPCell(new Paragraph(proCtcTerm.getTerm()));
            cell.setBackgroundColor(Color.lightGray);
            cell.setColspan(6);
            table.addCell(cell);
//            BaseFont bf;
//            bf = BaseFont.createFont("ZapfDingbats", "ZapfDingbats", true);
//            Font f1 = new Font(bf, 12);

            for (ProCtcQuestion proCtcQuestion : symptomMap.get(proCtcTerm)) {
                PdfPCell cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(), f));

                table.addCell(cell2);
                int i = 0;
                for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
                    i++;
                  Phrase ph = new Phrase(12, "O", f1);
                  ph.add(new Phrase(" " + proCtcValidValue.getValue(), f));

                    PdfPCell cell3 = new PdfPCell(new Paragraph(ph));
                    table.addCell(cell3);
                    cell3.setBorderWidth(0.0f);

                }
                for (int j = 0; j < 5 - i; j++) {
                    PdfPCell cell4 = new PdfPCell(new Paragraph(""));
                    table.addCell(cell4);
                    cell4.setBorderWidth(0.0f);
                }

            }

            document.add(table);
            document.add(new Paragraph(" "));
//            document.add(new Paragraph("width :" + width));
//            document.add(new Paragraph(proCtcTerm.getTerm()));
        }

    }

    static String cst(char c) {
        if (c == 0)
            return "\u00a0";
        String s = Integer.toHexString(c);
        s = s.toUpperCase();
        s = "0000" + s;
        return s.substring(s.length() - 4);
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
