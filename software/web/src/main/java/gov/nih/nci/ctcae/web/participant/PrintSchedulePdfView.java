package gov.nih.nci.ctcae.web.participant;

import com.lowagie.text.pdf.*;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.MeddraQuestion;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;

/**
 * author Mehul Gulati
 * Date: Jun 17, 2009
 */
public class PrintSchedulePdfView extends AbstractPdfView {

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        int total = duplicate(request) + 1;

        Integer id = Integer.parseInt(request.getParameter("id"));
        String language = request.getParameter("lang");
        if (StringUtils.isEmpty(language)) {
            language = "en";
        }
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(id);
        Study study = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getStudy();
        Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
        LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>> symptomMap = new LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>>();
        ArrayList<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();
        Map<ProCtcTerm, ArrayList<ProCtcQuestion>> participantAddedProctcSymptomMap = new LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>>();

        ArrayList<ProCtcQuestion> participantAddedProctcQuestions = new ArrayList<ProCtcQuestion>();
        Map<String, ArrayList<MeddraQuestion>> participantAddedMeddraSymptomMap = new LinkedHashMap<String, ArrayList<MeddraQuestion>>();
        ArrayList<MeddraQuestion> participantAddedMeddraQuestions = new ArrayList<MeddraQuestion>();
        Font coverFont = new Font();
        coverFont.setSize(22);
        coverFont.setStyle(Font.BOLD);
        for (int i = 0; i < 20; i++) {
            document.add(new Paragraph("\n"));
        }
        document.add(new Paragraph(new Chunk("                          Survey " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle(), coverFont)));

        Table table1 = new Table(2, 9);//2 Columns and 9 line
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

        Cell c7 = new Cell(new Paragraph("Participant:"));
        c7.setBorderWidth(0);
        table1.addCell(c7);

        Cell c8 = new Cell(new Paragraph("" + participant.getDisplayNameForReports()));
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

        Cell c15 = new Cell(new Paragraph("\n"));
        c15.setBorderWidth(0);
        table1.addCell(c15);

        Cell c16 = new Cell(new Paragraph("\n"));
        c16.setBorderWidth(0);
        table1.addCell(c16);

        HeaderFooter footer = new HeaderFooter(new Phrase(" CONFIDENTIAL                                                                                                                          Page ", new Font(Font.TIMES_ROMAN, 12, Font.NORMAL)), new Phrase(" of " + total, new Font(Font.TIMES_ROMAN, 12, Font.NORMAL)));
        document.setFooter(footer);

        Phrase p = new Phrase();
        p.clear();
        p.add(table1);
        Font font = new Font();
        //font.setStyle(Font.UNDERLINE);
        if (language.equalsIgnoreCase("en")) {
            p.add(new Paragraph(new Chunk("Please think back " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriod(), font)));
        } else {
            p.add(new Paragraph(new Chunk("Por favor, piense de nuevo " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriodInSpanish(), font)));
        }

        HeaderFooter header = new HeaderFooter(p, false);
        document.setHeader(header);

        for (int i = 0; i < 20; i++) {
            document.add(new Paragraph("\n"));
        }
        document.add(new Paragraph(" "));

        studyParticipantCrfSchedule.addParticipantAddedQuestions();
        addParticipantAddedQuestionsToMap(language, studyParticipantCrfSchedule, participantAddedProctcSymptomMap, participantAddedProctcQuestions, participantAddedMeddraSymptomMap, participantAddedMeddraQuestions);
        addSPCrfItemsToMap(studyParticipantCrfSchedule, symptomMap, proCtcQuestions);

        Font f = FontFactory.getFont("Arial", 14f);
        Font f1 = FontFactory.getFont("Arial", 16f);

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

            for (ProCtcQuestion proCtcQuestion : symptomMap.get(proCtcTerm)) {
                PdfPCell cell2;
                if (language.equals("en")) {
                    cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH) + "?", f));
                } else {
                    cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH) + "?", f));
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
                        cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH) + "?", f));
                    } else {
                        cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH) + "?", f));
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
                        cell2 = new PdfPCell(new Paragraph(meddraQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH) + "?", f));
                    } else {
                        cell2 = new PdfPCell(new Paragraph(meddraQuestion.getQuestionText(SupportedLanguageEnum.SPANISH) + "?", f));
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

        PdfPCell cell = null;
        if (language.equals("en")) {
            cell = new PdfPCell(new Paragraph("Please indicate any additional symptoms you have experienced " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriod() + " that you were not asked about:"));
        } else {
            cell = new PdfPCell(new Paragraph("Por favor, indique cualquier síntoma adicional que han experimentado " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriodInSpanish() + " que no se les preguntó sobre:"));
        }

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

    /**
     * Duplicate of the createPDf method. This is invoked in order to get the total page count.
     * As of this version of iText, the only way to get the total page count is to generate the entire document.
     * Hence the new Document and printWriter Objects which are eventually discarded.
     *
     * @param request the request
     * @return the int
     * @throws Exception the exception
     */
    public int duplicate(HttpServletRequest request) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document dupe = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(dupe, bos);
        dupe.open();
        Integer id = Integer.parseInt(request.getParameter("id"));
        String language = request.getParameter("lang");
        if (language == null || language == "") {
            language = "en";
        }
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(id);
        Study study = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getStudy();
        Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
        LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>> symptomMap = new LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>>();
        ArrayList<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();
        Map<ProCtcTerm, ArrayList<ProCtcQuestion>> participantAddedProctcSymptomMap = new LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>>();

        ArrayList<ProCtcQuestion> participantAddedProctcQuestions = new ArrayList<ProCtcQuestion>();
        Map<String, ArrayList<MeddraQuestion>> participantAddedMeddraSymptomMap = new LinkedHashMap<String, ArrayList<MeddraQuestion>>();
        ArrayList<MeddraQuestion> participantAddedMeddraQuestions = new ArrayList<MeddraQuestion>();
        Font coverFont = new Font();
        coverFont.setSize(22);
        coverFont.setStyle(Font.BOLD);
        for (int i = 0; i < 20; i++) {
            dupe.add(new Paragraph("\n"));
        }
        dupe.add(new Paragraph(new Chunk("                          Survey " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle(), coverFont)));

//        addHeaderFooter(dupe, study, studyParticipantCrfSchedule, participant);
        Table table1 = new Table(2, 9);//2 Columns and 9 line
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

        Cell c7 = new Cell(new Paragraph("Participant:"));
        c7.setBorderWidth(0);
        table1.addCell(c7);

        Cell c8 = new Cell(new Paragraph("" + participant.getDisplayNameForReports()));
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

        Cell c15 = new Cell(new Paragraph("\n"));
        c15.setBorderWidth(0);
        table1.addCell(c15);

        Cell c16 = new Cell(new Paragraph("\n"));
        c16.setBorderWidth(0);
        table1.addCell(c16);
        dupe.add(table1);

        HeaderFooter footer = new HeaderFooter(new Phrase(" CONFIDENIAL                                                                                                                              Page ", new Font(Font.TIMES_ROMAN, 12, Font.NORMAL)), true);
        dupe.setFooter(footer);
        Phrase p = new Phrase();
        p.clear();
        p.add(table1);
        Font font = new Font();
        font.setStyle(Font.UNDERLINE);
        if (language.equalsIgnoreCase("en")) {
            p.add(new Paragraph(new Chunk("Please think back " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriod(), font)));
        } else {
            p.add(new Paragraph(new Chunk("Por favor, piense de nuevo " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriodInSpanish(), font)));
        }
//        dupe.add(new Paragraph(new Chunk("Please think back " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriod(), font)));
        HeaderFooter header = new HeaderFooter(p, false);
        dupe.setHeader(header);
        for (int i = 0; i < 20; i++) {
            dupe.add(new Paragraph("\n"));
        }
        dupe.add(new Paragraph(" "));
        dupe.add(new Paragraph(" "));
        studyParticipantCrfSchedule.addParticipantAddedQuestions();
        addParticipantAddedQuestionsToMap(language, studyParticipantCrfSchedule, participantAddedProctcSymptomMap, participantAddedProctcQuestions, participantAddedMeddraSymptomMap, participantAddedMeddraQuestions);
        addSPCrfItemsToMap(studyParticipantCrfSchedule, symptomMap, proCtcQuestions);
        Font f = FontFactory.getFont("Arial", 14f);
        Font f1 = FontFactory.getFont("Arial", 16f);

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

            for (ProCtcQuestion proCtcQuestion : symptomMap.get(proCtcTerm)) {
                PdfPCell cell2;
                if (language.equals("en")) {
                    cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH) + "?", f));
                } else {
                    cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH) + "?", f));
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
            dupe.add(table);
            dupe.add(new Paragraph(" "));
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
                        cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH) + "?", f));
                    } else {
                        cell2 = new PdfPCell(new Paragraph(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH) + "?", f));
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

                dupe.add(table);
                dupe.add(new Paragraph(" "));
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
                        cell2 = new PdfPCell(new Paragraph(meddraQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH) + "?", f));
                    } else {
                        cell2 = new PdfPCell(new Paragraph(meddraQuestion.getQuestionText(SupportedLanguageEnum.SPANISH) + "?", f));
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
                dupe.add(table);
                dupe.add(new Paragraph(" "));
            }
        }
        PdfPTable table2 = new PdfPTable(new float[]{1f});
        table2.setWidthPercentage(98);
        PdfPCell cell = new PdfPCell(new Paragraph("Please indicate any additional symptoms you have experienced " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriod() + " that you were not asked about:"));
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
        dupe.add(table2);
        dupe.add(new Paragraph(" "));

        return pdfWriter.getPageNumber();
    }

    private void addParticipantAddedQuestionsToMap(String language, StudyParticipantCrfSchedule studyParticipantCrfSchedule, Map<ProCtcTerm, ArrayList<ProCtcQuestion>> participantAddedProctcSymptomMap, ArrayList<ProCtcQuestion> participantAddedProctcQuestions, Map<String, ArrayList<MeddraQuestion>> participantAddedMeddraSymptomMap, ArrayList<MeddraQuestion> participantAddedMeddraQuestions) {

        if (studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions().size() > 0) {
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                if (studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion() != null) {
                    ProCtcTerm proctcSymptom = studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion().getProCtcTerm();
                    if (participantAddedProctcSymptomMap.containsKey(proctcSymptom)) {
                        participantAddedProctcQuestions = participantAddedProctcSymptomMap.get(proctcSymptom);
                    } else {
                        participantAddedProctcQuestions = new ArrayList<ProCtcQuestion>();
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
                        participantAddedMeddraQuestions = new ArrayList<MeddraQuestion>();
                        participantAddedMeddraSymptomMap.put(meddraSymptom, participantAddedMeddraQuestions);
                    }
                    participantAddedMeddraQuestions.add(studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion());
                }
            }
        }
    }

    private void addSPCrfItemsToMap(StudyParticipantCrfSchedule studyParticipantCrfSchedule, LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>> symptomMap, ArrayList<ProCtcQuestion> proCtcQuestions) {
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
