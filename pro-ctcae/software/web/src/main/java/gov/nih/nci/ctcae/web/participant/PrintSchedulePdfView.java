package gov.nih.nci.ctcae.web.participant;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import org.jfree.ui.Align;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.ServletException;
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
        String language = request.getParameter("lang");
        if (language == null || language == "") {
            language = "en";
        }
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
//        document.add(new Paragraph("Study: " + study.getDisplayName()));
//        document.add(new Paragraph("Survey: " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle()));
//        document.add(new Paragraph("Study site: " + studySite.getDisplayName()));
//        document.add(new Paragraph("Participant: " + participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));
//        document.add(new Paragraph("Survey start date: " + DateUtils.format(studyParticipantCrfSchedule.getStartDate())));
//        document.add(new Paragraph("Survey due date: " + DateUtils.format(studyParticipantCrfSchedule.getDueDate())));
//        document.add(new Paragraph(" "));
//        document.add(new Paragraph(" "));
        Table table1 = new Table(2, 8);//2 Columns and 8 line
        table1.setWidth(100);
        table1.setWidths(new int[]{20, 80});
        table1.setBorderWidth(0);
        Cell c1 = new Cell(new Paragraph("Study:"));
        c1.setBorderWidth(0);
        table1.addCell(c1);

        Cell c2 = new Cell(new Paragraph("" + study.getDisplayName()));
        c2.setBorderWidth(0);
        table1.addCell(c2);

        Cell c3 = new Cell(new Paragraph("Survey:"));
        c3.setBorderWidth(0);
        table1.addCell(c3);

        Cell c4 = new Cell(new Paragraph("" + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle()));
        c4.setBorderWidth(0);
        table1.addCell(c4);

        Cell c5 = new Cell(new Paragraph("Study site:"));
        c5.setBorderWidth(0);
        table1.addCell(c5);

        Cell c6 = new Cell(new Paragraph("" + studySite.getDisplayName()));
        c6.setBorderWidth(0);
        table1.addCell(c6);

        Cell c7 = new Cell(new Paragraph("Participant:"));
        c7.setBorderWidth(0);
        table1.addCell(c7);

        Cell c8 = new Cell(new Paragraph("" + participant.getDisplayName() + " [" + participant.getAssignedIdentifier() + "]"));
        c8.setBorderWidth(0);
        table1.addCell(c8);

        Cell c9 = new Cell(new Paragraph("Survey start date:"));
        c9.setBorderWidth(0);
        table1.addCell(c9);

        Cell c10 = new Cell(new Paragraph("" + DateUtils.format(studyParticipantCrfSchedule.getStartDate())));
        c10.setBorderWidth(0);
        table1.addCell(c10);

        Cell c11 = new Cell(new Paragraph("Survey due date:"));
        c11.setBorderWidth(0);
        table1.addCell(c11);

        Cell c12 = new Cell(new Paragraph("" + DateUtils.format(studyParticipantCrfSchedule.getDueDate())));
        c12.setBorderWidth(0);
        table1.addCell(c12);

        Cell c13 = new Cell(new Paragraph("\n"));
        c13.setBorderWidth(0);
        table1.addCell(c13);

        Cell c14 = new Cell(new Paragraph("\n"));
        c14.setBorderWidth(0);
        table1.addCell(c14);
        document.add(table1);

        HeaderFooter footer = new HeaderFooter(new Phrase("[ CONFIDENIAL ]                         ", new Font(Font.COURIER, 12, Font.NORMAL)), true);
        document.setFooter(footer);
        Phrase p = new Phrase();
        p.clear();
        p.add(table1);
        HeaderFooter header = new HeaderFooter(p, false);
        document.setHeader(header);


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
                    String meddraSymptom;
                    if (language.equals("en")) {
                        meddraSymptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getFullName(SupportedLanguageEnum.ENGLISH);
                    } else {
                        meddraSymptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getLowLevelTermVocab().getMeddraTermSpanish();
                    }
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
            PdfPTable table = new PdfPTable(new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f});
            table.setWidthPercentage(98);
            PdfPCell cell;
            if (language.equals("en")) {
                cell = new PdfPCell(new Paragraph(proCtcTerm.getProCtcTermVocab().getTermEnglish()));
            } else {
                cell = new PdfPCell(new Paragraph(proCtcTerm.getProCtcTermVocab().getTermSpanish()));
            }
            cell.setBackgroundColor(Color.lightGray);
            cell.setColspan(8);
            table.addCell(cell);
//            BaseFont bf;
//            bf = BaseFont.createFont("ZapfDingbats", "ZapfDingbats", true);
//            Font f1 = new Font(bf, 12);

            for (ProCtcQuestion proCtcQuestion : symptomMap.get(proCtcTerm)) {
                PdfPCell cell2;
                if (language.equals("en")) {
                    cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH), f));
                } else {
                    cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH), f));
                }
                cell2.setColspan(8);
                table.addCell(cell2);
                int i = 0;
                for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
                    i++;
                    Phrase ph = new Phrase(12, "O", f1);
                    if (language.equals("en")) {
                        ph.add(new Phrase(" " + proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));
                    } else {
                        ph.add(new Phrase(" " + proCtcValidValue.getProCtcValidValueVocab().getValueSpanish(), f));
                    }
                    PdfPCell cell3 = new PdfPCell(new Paragraph(ph));
                    table.addCell(cell3);
                    cell3.setBorderWidth(0.0f);

                }
                for (int j = 0; j < 7 - i; j++) {
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
                PdfPTable table = new PdfPTable(new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f});
                table.setWidthPercentage(98);
                PdfPCell cell;
                if (language.equals("en")) {
                    cell = new PdfPCell(new Paragraph(proCtcTerm.getProCtcTermVocab().getTermEnglish()));
                } else {
                    cell = new PdfPCell(new Paragraph(proCtcTerm.getProCtcTermVocab().getTermSpanish()));
                }
                cell.setBackgroundColor(Color.lightGray);
                cell.setColspan(8);
                table.addCell(cell);

                for (ProCtcQuestion proCtcQuestion : participantAddedProctcSymptomMap.get(proCtcTerm)) {
                    PdfPCell cell2;
                    if (language.equals("en")) {
                        cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH), f));
                    } else {
                        cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH), f));
                    }
                    cell2.setColspan(8);
                    table.addCell(cell2);
                    int i = 0;
                    for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
                        i++;
                        Phrase ph = new Phrase(12, "O", f1);
                        if (language.equals("en")) {
                            ph.add(new Phrase(" " + proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));
                        } else {
                            ph.add(new Phrase(" " + proCtcValidValue.getValue(SupportedLanguageEnum.SPANISH), f));
                        }

                        PdfPCell cell3 = new PdfPCell(new Paragraph(ph));
                        table.addCell(cell3);
                        cell3.setBorderWidth(0.0f);

                    }
                    for (int j = 0; j < 8 - i; j++) {
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
                PdfPTable table = new PdfPTable(new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f});
                table.setWidthPercentage(98);
                PdfPCell cell = new PdfPCell(new Paragraph(meddraTerm));
                cell.setBackgroundColor(Color.lightGray);
                cell.setColspan(8);
                table.addCell(cell);

                for (MeddraQuestion meddraQuestion : participantAddedMeddraSymptomMap.get(meddraTerm)) {
                    PdfPCell cell2;
                    if (language.equals("en")) {
                        cell2 = new PdfPCell(new Paragraph(meddraQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH), f));
                    } else {
                        cell2 = new PdfPCell(new Paragraph(meddraQuestion.getQuestionText(SupportedLanguageEnum.SPANISH), f));
                    }
                    cell2.setColspan(8);
                    table.addCell(cell2);
                    int i = 0;
                    for (MeddraValidValue meddraValidValue : meddraQuestion.getValidValues()) {
                        i++;
                        Phrase ph = new Phrase(12, "O", f1);
                        if (language.equals("en")) {
                            ph.add(new Phrase(" " + meddraValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));
                        } else {
                            ph.add(new Phrase(" " + meddraValidValue.getValue(SupportedLanguageEnum.SPANISH), f));
                        }
                        PdfPCell cell3 = new PdfPCell(new Paragraph(ph));
                        table.addCell(cell3);
                        cell3.setBorderWidth(0.0f);

                    }
                    for (int j = 0; j < 8 - i; j++) {
                        PdfPCell cell4 = new PdfPCell(new Paragraph(""));
                        table.addCell(cell4);
                        cell4.setBorderWidth(0.0f);
                    }

                }

                document.add(table);
                document.add(new Paragraph(" "));
            }
        }
        PdfPTable table2 = new PdfPTable(new float[]{1f});
        table2.setWidthPercentage(98);
        PdfPCell cell = new PdfPCell(new Paragraph("Notes"));
        cell.setBackgroundColor(Color.lightGray);
        cell.setColspan(1);
        table2.addCell(cell);

        PdfPCell cell1 = new PdfPCell(new Paragraph("\n"));
        cell1.setBackgroundColor(Color.WHITE);
        cell1.setColspan(1);
        table2.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Paragraph("\n"));
        cell2.setBackgroundColor(Color.WHITE);
        cell2.setColspan(1);
        table2.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Paragraph("\n"));
        cell3.setBackgroundColor(Color.WHITE);
        cell3.setColspan(1);
        table2.addCell(cell3);
        document.add(table2);
        document.add(new Paragraph(" "));
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
