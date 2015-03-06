package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
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
public class PrintFormPdfView extends AbstractPdfView {

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    CRFRepository crfRepository;

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
        Integer id = Integer.parseInt(request.getParameter("crfId"));
        String language = request.getParameter("lang");
        if (StringUtils.isEmpty(language)) {
            language = "en";
        }
        
        CRF crf = crfRepository.findById(id);
        
        LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>> symptomMap = new LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>>();
        ArrayList<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();

        
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
        
        Paragraph coverSurveyName = new Paragraph(new Chunk(crf.getTitle() , surveyNameFont));
        coverSurveyName.setAlignment(Element.ALIGN_CENTER);
        document.add(coverSurveyName);

        //setting the header
        Phrase headerPhrase = getHeaderTableInPhrase(document, crf, language);
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

        addcrfPageItemsToMap(crf, symptomMap, proCtcQuestions);

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

        symCount++;
        questionCounter = questionCounter + 2;
        if(isNewPage(symCount, questionCounter)){
        	document.newPage();
        }
        
        Table additionalSymptomsTable = getTableForAdditionalSymptoms(language, crf);
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
    
    private Table getTableForAdditionalSymptoms(String language, CRF crf) throws DocumentException {
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
            cell = new Cell(new Paragraph("  Please indicate any additional symptoms you have experienced " + crf.getRecallPeriod() + " that you were not asked about:"));
        } else {
            cell = new Cell(new Paragraph("  Por favor, indique cualquier síntoma adicional que han experimentado " + crf.getRecallPeriodInSpanish() + " que no se les preguntó sobre:"));
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

	private Phrase getHeaderTableInPhrase(Document document, CRF crf, String language) throws DocumentException {
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
        Cell c2 = new Cell(new Paragraph("" + crf.getStudy().getDisplayName()));
        c2.setBorderWidth(0);
        c2.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c2);
        
        Cell c3 = new Cell(new Paragraph("  Survey:", headerLabels));
        c3.setBorderWidth(0);
        c3.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c3);
        Cell c4 = new Cell(new Paragraph("" + crf.getTitle()));
        c4.setBorderWidth(0);
        c4.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c4);


        Cell c5 = new Cell(new Paragraph("  Survey start date:", headerLabels));
        c5.setBorderWidth(0);
        c5.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c5);
        Cell c6 = new Cell(new Paragraph(""));
        c6.setBorderWidth(0);
        c6.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c6);

        Cell c7 = new Cell(new Paragraph("  Survey due date:", headerLabels));
        c7.setBorderWidth(0);
        c7.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c7);
        Cell c8 = new Cell(new Paragraph(""));
        c8.setBorderWidth(0);
        c8.setVerticalAlignment(Element.ALIGN_TOP);
        table1.addCell(c8);

        Cell c9 = new Cell(new Paragraph("\n"));
        c9.setBorderWidth(0);
        c9.setColspan(2);
        table1.addCell(c9);

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
            thinkBackMsg = new Paragraph(new Chunk("  Please think back " + crf.getRecallPeriod() + "\n", font));
        	t2Cell = new Cell(thinkBackMsg);
        } else {
            thinkBackMsg = new Paragraph(new Chunk("  Por favor, piense de nuevo " + crf.getRecallPeriodInSpanish(), font));
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

    private void addcrfPageItemsToMap(CRF crf, LinkedHashMap<ProCtcTerm, ArrayList<ProCtcQuestion>> symptomMap, ArrayList<ProCtcQuestion> proCtcQuestions) {
    	for(CRFPage crfPage : crf.getCrfPages()){
    		for(CrfPageItem crfPageItem : crfPage.getCrfPageItems()){
    			ProCtcTerm proCtcTerm = crfPageItem.getProCtcQuestion().getProCtcTerm();
                if (symptomMap.containsKey(proCtcTerm)) {
                    proCtcQuestions = symptomMap.get(proCtcTerm);
                } else {
                    proCtcQuestions = new ArrayList<ProCtcQuestion>();
                    symptomMap.put(proCtcTerm, proCtcQuestions);
                }
                proCtcQuestions.add(crfPageItem.getProCtcQuestion());
    		}
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
    
    public void setCRFRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
