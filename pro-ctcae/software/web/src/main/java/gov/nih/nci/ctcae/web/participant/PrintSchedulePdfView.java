package gov.nih.nci.ctcae.web.participant;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
        LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>> symptomMap = new LinkedHashMap();
        ArrayList<ProCtcQuestion> proCtcQuestions;
        Map<ProCtcTerm, ArrayList<ProCtcQuestion>> participantAddedProctcSymptomMap = new LinkedHashMap();
        ArrayList<ProCtcQuestion> participantAddedProctcQuestions;
        Map<String, ArrayList<MeddraQuestion>> participantAddedMeddraSymptomMap = new LinkedHashMap();
        ArrayList<MeddraQuestion> participantAddedMeddraQuestions;

        document.add(new Paragraph("Study: " + study.getDisplayName()));
        document.add(new Paragraph("Form: " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle()));
        document.add(new Paragraph("Study site: " + studySite.getDisplayName()));
        document.add(new Paragraph("Participant: " + participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));
        document.add(new Paragraph("Start date: " + DateUtils.format(studyParticipantCrfSchedule.getStartDate())));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        studyParticipantCrfSchedule.addParticipantAddedQuestions();
        if (studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions().size() > 0) {
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                if (studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion() != null) {
                    ProCtcTerm proctcSymptom = studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion().getProCtcTerm();
                    if (participantAddedProctcSymptomMap.containsKey(proctcSymptom)) {
                        participantAddedProctcQuestions = participantAddedProctcSymptomMap.get(proctcSymptom);
                    } else {
                        participantAddedProctcQuestions = new ArrayList();
                        participantAddedProctcSymptomMap.put(proctcSymptom, participantAddedProctcQuestions);
                    }
                    participantAddedProctcQuestions.add(studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion());
                }

                if (studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion() != null) {
                    String meddraSymptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getFullName();
                    if (participantAddedMeddraSymptomMap.containsKey(meddraSymptom)) {
                        participantAddedMeddraQuestions = participantAddedMeddraSymptomMap.get(meddraSymptom);
                    } else {
                        participantAddedMeddraQuestions = new ArrayList();
                        participantAddedMeddraSymptomMap.put(meddraSymptom, participantAddedMeddraQuestions);
                    }
                    participantAddedMeddraQuestions.add(studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion());
                }
            }
        }
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
            PdfPCell cell = new PdfPCell(new Paragraph(proCtcTerm.getProCtcTermVocab().getTermEnglish()));
            cell.setBackgroundColor(Color.lightGray);
            cell.setColspan(6);
            table.addCell(cell);
//            BaseFont bf;
//            bf = BaseFont.createFont("ZapfDingbats", "ZapfDingbats", true);
//            Font f1 = new Font(bf, 12);

            for (ProCtcQuestion proCtcQuestion : symptomMap.get(proCtcTerm)) {
                PdfPCell cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH), f));

                table.addCell(cell2);
                int i = 0;
                for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
                    i++;
                    Phrase ph = new Phrase(12, "O", f1);
                    ph.add(new Phrase(" " + proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));

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

        if (participantAddedProctcSymptomMap.keySet().size() > 0) {
            for (ProCtcTerm proCtcTerm : participantAddedProctcSymptomMap.keySet()) {
                PdfPTable table = new PdfPTable(new float[]{2.5f, 1f, 1f, 1f, 1f, 1f});
                table.setWidthPercentage(98);
                PdfPCell cell = new PdfPCell(new Paragraph(proCtcTerm.getProCtcTermVocab().getTermEnglish()));
                cell.setBackgroundColor(Color.lightGray);
                cell.setColspan(6);
                table.addCell(cell);

                for (ProCtcQuestion proCtcQuestion : participantAddedProctcSymptomMap.get(proCtcTerm)) {
                    PdfPCell cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH), f));

                    table.addCell(cell2);
                    int i = 0;
                    for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
                        i++;
                        Phrase ph = new Phrase(12, "O", f1);
                        ph.add(new Phrase(" " + proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));

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
        }
    }

        if (participantAddedMeddraSymptomMap.keySet().size() > 0) {
            for (String meddraTerm : participantAddedMeddraSymptomMap.keySet()) {
                PdfPTable table = new PdfPTable(new float[]{2.5f, 1f, 1f, 1f, 1f, 1f});
                table.setWidthPercentage(98);
                PdfPCell cell = new PdfPCell(new Paragraph(meddraTerm));
                cell.setBackgroundColor(Color.lightGray);
                cell.setColspan(6);
                table.addCell(cell);

                for (MeddraQuestion meddraQuestion : participantAddedMeddraSymptomMap.get(meddraTerm)) {
                    PdfPCell cell2 = new PdfPCell(new Paragraph(meddraQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH), f));

                    table.addCell(cell2);
                    int i = 0;
                    for (MeddraValidValue meddraValidValue : meddraQuestion.getValidValues()) {
                        i++;
                        Phrase ph = new Phrase(12, "O", f1);
                        ph.add(new Phrase(" " + meddraValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));

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
        }
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
