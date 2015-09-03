package gov.nih.nci.ctcae.web.participant;

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

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * author Mehul Gulati, Vinay Gangoli
 * Date: Jun 17, 2009
 */
public class PrintSchedulePdfView extends AbstractPdfView {

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    @Override
    /**
    * Override to produce a landscape format.
    */
    protected Document newDocument() {
    	return new Document(PageSize.A4.rotate());
    }
    
    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document dupe = new Document(PageSize.A4.rotate());
        PdfWriter dupePdfWriter = PdfWriter.getInstance(dupe, bos);
        int totalPages = 0;
        dupe.open();
        buildDocument(map, dupe, dupePdfWriter, request, httpServletResponse, totalPages, true);
        totalPages = dupePdfWriter.getPageNumber();
        buildDocument(map, document, pdfWriter, request, httpServletResponse, totalPages, false);
    }

    private void buildDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse, int totalPages, boolean duplicate) throws Exception {
        int total = totalPages;
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
        
        Font surveyLabelFont = new Font();
        surveyLabelFont.setSize(22);
        surveyLabelFont.setStyle(Font.BOLD);
        surveyLabelFont.setStyle(Font.UNDERLINE);
        
        Font surveyNameFont = new Font();
        surveyNameFont.setSize(22);
        surveyNameFont.setStyle(Font.BOLD);
        
        for (int i = 0; i < 8; i++) {
            document.add(new Paragraph("\n"));
        }
        Paragraph coverSurveyLabel = new Paragraph(new Chunk("SURVEY" , surveyLabelFont));
        coverSurveyLabel.setAlignment(Element.ALIGN_CENTER);
        document.add(coverSurveyLabel);
        document.add(new Paragraph("\n"));
        
        Paragraph coverSurveyName = new Paragraph(new Chunk(studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle() , surveyNameFont));
        coverSurveyName.setAlignment(Element.ALIGN_CENTER);
        document.add(coverSurveyName);

        //setting the header
        Phrase headerPhrase = getHeaderTableInPhrase(document, study, studyParticipantCrfSchedule, participant, language);
        HeaderFooter header = new HeaderFooter(headerPhrase, false);
        header.setBorder(Rectangle.NO_BORDER);
        document.setHeader(header);
        
        //setting the footer
        Phrase footerPhrase = new Phrase("                                                                                                                                                                     Page ", new Font(Font.TIMES_ROMAN, 12, Font.NORMAL));
        Phrase footerPhrase2 = new Phrase(" of " + total, new Font(Font.TIMES_ROMAN, 12, Font.NORMAL));
        HeaderFooter footer = new HeaderFooter(footerPhrase, footerPhrase2);
        document.setFooter(footer);

        //doing this in order to start content from second page.
        for (int i = 0; i < 16; i++) {
            document.add(new Paragraph("\n"));
        }
        document.add(new Paragraph(" "));

        studyParticipantCrfSchedule.addParticipantAddedQuestions();
        addParticipantAddedQuestionsToMap(language, studyParticipantCrfSchedule, participantAddedProctcSymptomMap, participantAddedProctcQuestions, participantAddedMeddraSymptomMap, participantAddedMeddraQuestions);
        addSPCrfItemsToMap(studyParticipantCrfSchedule, symptomMap, proCtcQuestions);

        Font f = FontFactory.getFont("Arial", 12f);
        int autoPageCounter = pdfWriter.getPageNumber() + 1;
        int symCount = 0;
        int questionCounter = 0;
        
        Table table;
        for (ProCtcTerm proCtcTerm : symptomMap.keySet()) {
            table = getSymptomTable();
            
            symCount++;
            questionCounter = questionCounter + symptomMap.get(proCtcTerm).size();
            if(isNewPage(symCount, questionCounter)){
            	if(autoPageCounter == pdfWriter.getPageNumber()){
                	document.newPage();
            	}
            	questionCounter = symptomMap.get(proCtcTerm).size();
            	symCount = 1;
            	autoPageCounter++;
            }
            
            Cell cell;
            if (language.equals("en")) {
                cell = getCellForTerm(proCtcTerm.getProCtcTermVocab().getTermEnglish());
            } else {
                cell = getCellForTerm(proCtcTerm.getProCtcTermVocab().getTermSpanish());
            }
            table.addCell(cell);

            Cell cell2;
            for (ProCtcQuestion proCtcQuestion : symptomMap.get(proCtcTerm)) {
                if (language.equals("en")) {
                    cell2 = getCellForQuestion(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH));
                } else {
                    cell2 = getCellForQuestion(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH));
                }
                table.addCell(cell2);
                
                table.addCell(getCellForIndenting());
                for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
                    addValidValueToCell(proCtcValidValue, f, language, table, f);
                }
                for (int j = 0; j < 7 - proCtcQuestion.getValidValues().size(); j++) {
                    table.addCell(getBlankOptionCell());
                }
            }
            document.add(table);
            document.add(new Paragraph(" "));
        }

        if (participantAddedProctcSymptomMap.keySet().size() > 0) {
            for (ProCtcTerm proCtcTerm : participantAddedProctcSymptomMap.keySet()) {
                table = getSymptomTable();
                
                symCount++;
                questionCounter = questionCounter + participantAddedProctcSymptomMap.get(proCtcTerm).size();
                if(isNewPage(symCount, questionCounter)){
                	if(autoPageCounter == pdfWriter.getPageNumber()){
                    	document.newPage();
                	}
                	questionCounter = participantAddedProctcSymptomMap.get(proCtcTerm).size();
                	symCount = 1;
                	autoPageCounter++;
                }
                
                Cell cell;
                if (language.equals("en")) {
                    cell = getCellForTerm(proCtcTerm.getProCtcTermVocab().getTermEnglish());
                } else {
                    cell = getCellForTerm(proCtcTerm.getProCtcTermVocab().getTermSpanish());
                }
                table.addCell(cell);

                Cell cell2;
                for (ProCtcQuestion proCtcQuestion : participantAddedProctcSymptomMap.get(proCtcTerm)) {
                    if (language.equals("en")) {
                        cell2 = getCellForQuestion(proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH));
                    } else {
                        cell2 = getCellForQuestion(proCtcQuestion.getQuestionText(SupportedLanguageEnum.SPANISH));
                    }
                    table.addCell(cell2);
                    
                    table.addCell(getCellForIndenting());
                    for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
                        addValidValueToCell(proCtcValidValue, f, language, table, f);
                    }
                    for (int j = 0; j < 7 - proCtcQuestion.getValidValues().size(); j++) {
                        table.addCell(getBlankOptionCell());
                    }
                }
                document.add(table);
                document.add(new Paragraph(" "));
            }
        }

        if (participantAddedMeddraSymptomMap.keySet().size() > 0) {
            for (String meddraTerm : participantAddedMeddraSymptomMap.keySet()) {
            	table = getSymptomTable();
                
                symCount++;
                questionCounter = questionCounter + participantAddedMeddraSymptomMap.get(meddraTerm).size();
                if(isNewPage(symCount, questionCounter)){
                	if(autoPageCounter == pdfWriter.getPageNumber()){
                    	document.newPage();
                	}
                	questionCounter = participantAddedMeddraSymptomMap.get(meddraTerm).size();
                	symCount = 1;
                	autoPageCounter++;
                }
            	
                Cell cell = getCellForTerm(meddraTerm);
                table.addCell(cell);

                Cell cell2, cell3;
                Phrase ph;
                for (MeddraQuestion meddraQuestion : participantAddedMeddraSymptomMap.get(meddraTerm)) {
                    if (language.equals("en")) {
                        cell2 = getCellForQuestion(meddraQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH));
                    } else {
                        cell2 = getCellForQuestion(meddraQuestion.getQuestionText(SupportedLanguageEnum.SPANISH));
                    }
                    table.addCell(cell2);
                    
                    table.addCell(getCellForIndenting());
                    for (MeddraValidValue meddraValidValue : meddraQuestion.getValidValues()) {
                        ph = new Phrase(12, "O", f);
                        if (language.equals("en")) {
                            ph.add(new Phrase(" " + meddraValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));
                        } else {
                            ph.add(new Phrase(" " + meddraValidValue.getValue(SupportedLanguageEnum.SPANISH), f));
                        }
                        cell3 = new Cell(new Paragraph(ph));
                        cell3.setBorder(Rectangle.NO_BORDER);
                        cell3.setVerticalAlignment(Element.ALIGN_TOP);
                        cell3.setUseBorderPadding(true);
                        table.addCell(cell3);
                    }
                    for (int j = 0; j < 7 - meddraQuestion.getValidValues().size(); j++) {
                        table.addCell(getBlankOptionCell());
                    }
                }
                document.add(table);
                document.add(new Paragraph(" "));
            }
        }

        symCount++;
        questionCounter = questionCounter + 2;
        if(isNewPage(symCount, questionCounter)){
        	document.newPage();
        }
        
        Table additionalSymptomsTable = getTableForAdditionalSymptoms(language, studyParticipantCrfSchedule);
        document.add(additionalSymptomsTable);
        document.add(new Paragraph("  "));
    }

    private Cell getCellForIndenting() throws BadElementException{
    	Cell cell = new Cell(new Paragraph(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseBorderPadding(true);
        
        return cell;
    }
    
    
    private Cell getBlankOptionCell() throws BadElementException {
    	Cell cell = new Cell(new Paragraph(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseBorderPadding(true);
        
        return cell;
	}

	private Table getTableForAdditionalSymptoms(String language, StudyParticipantCrfSchedule studyParticipantCrfSchedule) throws DocumentException {
    	Table table = new Table(1);
        table.setWidth(100);
        table.setWidths(new int[]{100});
        table.setBorder(1);
        table.setBorderWidthBottom(1);
        table.setBorderWidthLeft(1);
        table.setBorderWidthRight(1);
        table.setBorderColor(Color.BLACK);
        table.setPadding(1);
        table.setCellsFitPage(true);
        
        Cell cell = null;
        if (language.equals("en")) {
            cell = new Cell(new Paragraph("  Please indicate any additional symptoms you have experienced " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriod() + " that you were not asked about:"));
        } else {
            cell = new Cell(new Paragraph("  Por favor, indique cualquier síntoma adicional que han experimentado " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriodInSpanish() + " que no se les preguntó sobre:"));
        }
        cell.setUseBorderPadding(true);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBackgroundColor(Color.lightGray);
        cell.setColspan(1);
        table.addCell(cell);

        Cell cell1 = new Cell(new Paragraph("\n"));
        cell1.setBackgroundColor(Color.WHITE);
        cell1.setColspan(1);
        table.addCell(cell1);
        table.addCell(cell1);
        table.addCell(cell1);
        
        return table;
	}

	private Phrase getHeaderTableInPhrase(Document document, Study study, StudyParticipantCrfSchedule studyParticipantCrfSchedule, Participant participant, String language) throws DocumentException {
        Font headerLabels = FontFactory.getFont("Arial", 12f, Font.BOLD);
        Table table1 = new Table(2, 9);//2 Columns and 9 line
        table1.setBorderWidthBottom(1);
        table1.setBorderWidthTop(1);
        table1.setWidth(100);
        table1.setWidths(new int[]{20, 80});

        Cell c1 = new Cell(new Paragraph("  Study:", headerLabels));
        c1.setBorderWidth(0);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c1);

        Cell c2 = new Cell(study.getDisplayName());
        c2.setBorderWidth(0);
        c2.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c2);

        Cell c3 = new Cell(new Paragraph("  Survey:", headerLabels));
        c3.setBorderWidth(0);
        c3.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c3);

        Cell c4 = new Cell(new Paragraph("" + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle()));
        c4.setBorderWidth(0);
        c4.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c4);

        Cell c7 = new Cell(new Paragraph("  Participant:", headerLabels));
        c7.setBorderWidth(0);
        c7.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c7);

        Cell c8 = new Cell(new Paragraph("" + participant.getDisplayNameForReports()));
        c8.setBorderWidth(0);
        c8.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c8);

        Cell c9 = new Cell(new Paragraph("  Survey start date:", headerLabels));
        c9.setBorderWidth(0);
        c9.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c9);

        Cell c10 = new Cell(new Paragraph("" + DateUtils.format(studyParticipantCrfSchedule.getStartDate())));
        c10.setBorderWidth(0);
        c10.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c10);

        Cell c11 = new Cell(new Paragraph("  Survey due date:", headerLabels));
        c11.setBorderWidth(0);
        c11.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c11);

        Cell c12 = new Cell(new Paragraph("" + DateUtils.format(studyParticipantCrfSchedule.getDueDate())));
        c12.setBorderWidth(0);
        c12.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c12);

        Cell c15 = new Cell(new Paragraph("\n"));
        c15.setBorderWidth(0);
        c15.setColspan(2);
        table1.addCell(c15);

        Phrase p = new Phrase(0);
        p.add(table1);
        p.add(new Phrase("\n"));
        Table table12 = new Table(1, 1);
        table12.setWidth(100);
        table12.setBorder(Rectangle.NO_BORDER);
        Cell t2Cell;
        Font font = new Font();
        Paragraph thinkBackMsg;
        if (language.equalsIgnoreCase("en")) {
            thinkBackMsg = new Paragraph(new Chunk("  Please think back " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriod() + "\n", font));
        	t2Cell = new Cell(thinkBackMsg);
        } else {
            thinkBackMsg = new Paragraph(new Chunk("  Por favor, piense de nuevo " + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getRecallPeriodInSpanish(), font));
        	t2Cell = new Cell(thinkBackMsg);
        }
        t2Cell.setBorderWidth(0);
        t2Cell.setVerticalAlignment(Rectangle.TOP);
        table12.addCell(t2Cell);
        p.add(table12);
        
        return p;
	}

	private Cell getCellForQuestion(String question) throws BadElementException {
		Paragraph paragraph = new Paragraph("    " + question + "?", FontFactory.getFont("Arial", 12f));
    	Cell cell = new Cell(paragraph);
        cell.setColspan(8);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setUseBorderPadding(true);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        
        return cell;
	}

	private Cell getCellForTerm(String term) throws BadElementException {
		Paragraph pSym = new Paragraph("  " + term, FontFactory.getFont("Arial", 12f, Font.BOLD));
    	Cell cell = new Cell(pSym);
        cell.setBackgroundColor(Color.lightGray);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setColspan(8);
        
		return cell;
	}

	private Table getSymptomTable() throws DocumentException {
		Table table = new Table(8);
        table.setWidth(100);
        table.setWidths(new int[]{4, 9, 10, 14, 12, 15, 18, 18});
        table.setBorder(1);
        table.setBorderWidthBottom(1);
        table.setBorderWidthLeft(1);
        table.setBorderWidthRight(1);
        table.setBorderColor(Color.BLACK);
        table.setPadding(1);
        table.setCellsFitPage(true);
        
        return table;
	}

	private boolean isNewPage(int symCount, int questionCounter) {
		return questionCounter > 6 || (symCount > 2 && questionCounter > 4) || symCount > 3;
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

    private void addValidValueToCell(ProCtcValidValue proCtcValidValue, Font f1, String language, Table table, Font f) throws BadElementException {
         Phrase ph = new Phrase(12, "O", f1);
         if (language.equals("en")) {
            ph.add(new Phrase(" " + proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH), f));
         } else {
            ph.add(new Phrase(" " + proCtcValidValue.getProCtcValidValueVocab().getValueSpanish(), f));
         }
         Cell cell3 = new Cell(new Paragraph(ph));
         cell3.setUseBorderPadding(true);
         cell3.setVerticalAlignment(Element.ALIGN_TOP);
         cell3.setBorder(Rectangle.NO_BORDER);
         table.addCell(cell3);
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
