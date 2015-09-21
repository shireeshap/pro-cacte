package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * author Ramakrishna Gundala
 * Date: Sep 15, 2015
 */
public class PrintParticipantCompleteSurveySchedulePdfView extends AbstractPdfView {

	ParticipantRepository participantRepository;

    public PrintParticipantCompleteSurveySchedulePdfView() {
    	setContentType("application/pdf");
	}

	public ParticipantRepository getParticipantRepository() {
		return participantRepository;
	}

	public void setParticipantRepository(ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

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
	        Integer participantId = Integer.parseInt(request.getParameter("id"));
	        Map<String,Object> dataMap = getStudyParticipantAndParticipantSchedulesMap(participantId);
	        String studyIdentifier = (String) dataMap.get("studyID");
	        String participantIdentifier = (String)dataMap.get("participantID");
	        List<StudyParticipantCrfSchedule> participantCrfSchedules = ( List<StudyParticipantCrfSchedule>) dataMap.get("spCrfSchedules");
	        
	        
	        buildDocument(map, dupe, dupePdfWriter, request, httpServletResponse, totalPages, true, studyIdentifier, participantIdentifier, participantCrfSchedules);
	        totalPages = dupePdfWriter.getPageNumber();
	        
	        
	        buildDocument(map, document, pdfWriter, request, httpServletResponse, totalPages, false, studyIdentifier, participantIdentifier, participantCrfSchedules);
	 }
	 
	 private Map<String,Object> getStudyParticipantAndParticipantSchedulesMap(Integer participantId){
		 Map<String,Object> map = new HashMap<String,Object>();
		 Participant participant = participantRepository.findById(participantId);
	        List<StudyParticipantCrf> studyParticipantCrfs = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs();
	       Study study = participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy();
	       List<StudyParticipantCrfSchedule> participantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>();
	        for(StudyParticipantCrf studyParticiapantCrf : studyParticipantCrfs ){
	        	participantCrfSchedules.addAll(studyParticiapantCrf.getStudyParticipantCrfSchedules());
	        }
	        
	        Collections.sort(participantCrfSchedules, new Comparator<StudyParticipantCrfSchedule>() {
				@Override
				public int compare(StudyParticipantCrfSchedule spCrfSchedule1,
						StudyParticipantCrfSchedule spCrfSchedule2) {
					return -DateUtils.compareDate(spCrfSchedule1.getStartDate(), spCrfSchedule2.getStartDate());
				}
			});
	        
	        map.put("studyID", study.getAssignedIdentifier());
	        map.put("participantID", participant.getStudyParticipantIdentifier());
	        map.put("spCrfSchedules", participantCrfSchedules);
	        
	        return map;
		 
	 }

	 private void buildDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse, 
			 int totalPages, boolean duplicate, String studyIdentifier, String participantIdentifier, List<StudyParticipantCrfSchedule> participantCrfSchedules) throws Exception {
	    int total = totalPages;
        
	    
	    Font studyIDFont = new Font(Font.BOLD);
        studyIDFont.setSize(20);
        
        Font f = new Font();
        f.setSize(18);
        
        
        Chunk c1 = new Chunk("Study ID: ", studyIDFont);
        c1.setFont(f);
        Chunk sigUnderline = new Chunk(studyIdentifier);
        sigUnderline.setUnderline(0.1f, -2f);
        sigUnderline.setFont(f);
        
        Paragraph studyIDLabel = new Paragraph();
        studyIDLabel.add(c1);
        studyIDLabel.setAlignment(Element.ALIGN_RIGHT);
        studyIDLabel.add(sigUnderline);
        document.add(studyIDLabel);
        
        document.add(new Paragraph("\n"));
        
        
        Chunk c3 = new Chunk("Participant ID: ", studyIDFont);
        sigUnderline = new Chunk(participantIdentifier);
        sigUnderline.setUnderline(0.1f, -2f);
        sigUnderline.setFont(f);
        
        Paragraph participantLabel = new Paragraph();
        participantLabel.add(c3);
        participantLabel.setAlignment(Element.ALIGN_RIGHT);
        participantLabel.add(sigUnderline);
        document.add(participantLabel);
        
        
        document.add(new Paragraph("\n"));
        
        Font surveyLabelFont = new Font();
        surveyLabelFont.setSize(22);
        surveyLabelFont.setStyle(Font.BOLD);
        
        Chunk c5 = new Chunk("		Survey Schedule", surveyLabelFont);
       
        Chunk c6 = new Chunk(" (as of " + DateUtils.format(new Date()) + ")" , f);
        
        Paragraph coverSurveyLabel = new Paragraph();
        coverSurveyLabel.setAlignment(Element.ALIGN_LEFT);
        coverSurveyLabel.add(c5);
        coverSurveyLabel.add(c6);
        document.add(coverSurveyLabel);
        document.add(new Paragraph("\n"));
        
        
        //setting the footer
        Phrase footerPhrase = new Phrase("CONFIDENTIAL                                                                                                                                                                     Page ", new Font(Font.TIMES_ROMAN, 12, Font.NORMAL));
        Phrase footerPhrase2 = new Phrase(" of " + total, new Font(Font.TIMES_ROMAN, 12, Font.NORMAL));
        HeaderFooter footer = new HeaderFooter(footerPhrase, footerPhrase2);
        document.setFooter(footer);

        Table table = getSurveyTable();
        Cell cell = new Cell("	Survey Start Date"); 
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setHeader(true);
        table.addCell(cell);
        
        cell = new Cell("	Survey Due Date"); 
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setHeader(true);
        table.addCell(cell);
        
        cell = new Cell("	Status"); 
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setHeader(true);
        table.addCell(cell);
        
        table.endHeaders();
        Cell tableCell;
        for(StudyParticipantCrfSchedule participantCrfSchedule : participantCrfSchedules){
        	tableCell = new Cell(DateUtils.format(participantCrfSchedule.getStartDate()));
        	tableCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        	table.addCell(tableCell);
        	
        	tableCell = new Cell(DateUtils.format(participantCrfSchedule.getDueDate()));
        	tableCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        	table.addCell(tableCell);
        	
        	
        	tableCell = new Cell(participantCrfSchedule.getStatus().getCode());
        	tableCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        	table.addCell(tableCell);
        	
        	
            /* table.addCell(DateUtils.format(participantCrfSchedule.getStartDate()));
             table.addCell(DateUtils.format(participantCrfSchedule.getDueDate()));
             table.addCell(participantCrfSchedule.getStatus().getCode());*/
        }

         document.add(table);
         
         document.add(new Paragraph(" "));
         document.add(new Paragraph(" "));
         
         Paragraph contactInfoRequestParagraph = new Paragraph(new Chunk("		If you have any questions about your schedule, please contact:", studyIDFont));
         contactInfoRequestParagraph.setAlignment(Element.ALIGN_LEFT);
         document.add(contactInfoRequestParagraph);
         document.add(new Paragraph("\n"));
         document.add(new Paragraph("\n"));
         
         Paragraph underlineParagraph = new Paragraph(new Chunk("		_____________________________________________________________________________________"));
         underlineParagraph.setSpacingAfter(1);
         underlineParagraph.setSpacingBefore(1);
       //  underlineParagraph
         underlineParagraph.setAlignment(Element.ALIGN_LEFT);
         document.add(underlineParagraph);
         
         
         Paragraph contactInfoParagraph = new Paragraph(new Chunk("		(name)                                        (phone)                                     (email)"));
         contactInfoParagraph.setAlignment(Element.ALIGN_LEFT);
         document.add(contactInfoParagraph);
         
         document.add(new Paragraph("\n"));
    }

	private Table getSurveyTable() throws DocumentException {
		Table table = new Table(3);
        table.setWidth(60);
        table.setWidths(new int[]{23, 20, 17});
        table.setBorder(1);
        table.setBorderWidthBottom(1);
        table.setBorderWidthLeft(1);
        table.setBorderWidthRight(1);
        table.setPadding(1);
        table.setSpacing(1);
        table.setCellsFitPage(false);
        
        return table;
	}


}
