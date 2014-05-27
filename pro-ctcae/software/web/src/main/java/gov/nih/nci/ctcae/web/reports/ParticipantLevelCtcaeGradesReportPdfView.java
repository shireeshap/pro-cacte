package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**ParticipantLevelCtcaeGradesReportPdfView class
 * @author AmeyS
 * This class is responsible for painting pdf version of ACCRU AE Report.
 */
public class ParticipantLevelCtcaeGradesReportPdfView extends AbstractPdfView {
	private static String BLANK_DATE = "";
    private static String START_DATE = "startDate";
    private static String END_DATE = "endDate";
    private static String NO_PARTICIPANT_IDENTIFIER = "";
	private int totalPages = 0;
	private static String HEADER_VERBATIM = "Verbatim patient-reported PRO-CTCAE" ;
	private static String HEADER_AE_AND_MEDDRA_CODE = "CTCAE v4.0 Term and MedDRA Code (v12.0)";
	private static String HEADER_START_DATE = "Start Date";
	private static String HEADER_END_DATE = "End Date";
	private static String HEADER_INVESTIGATOR_REPORTED_GRADE = "Investigator Reported Event Grade";
	private static String HEADER_OUTCOME = "Outcome";
	private static String HEADER_AE_ATTRIBUTION = "AE Attribution";
	private static String COLON = ":";
	
    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document dupe = new Document(PageSize.A4.rotate());
        dupe.setMargins(35, 35, 15, 15);
        PdfWriter dupePdfWriter = PdfWriter.getInstance(dupe, bos);
        dupe.open();
        buildDocument(map, dupe, dupePdfWriter, request, httpServletResponse);
        totalPages = dupePdfWriter.getPageNumber();
        
        document.setMargins(35, 35, 15, 15);
        MyPdfPageEvent event = new MyPdfPageEvent();
        event.setTotalPages(totalPages - 1);
        pdfWriter.setPageEvent(event);
        buildDocument(map, document, pdfWriter, request, httpServletResponse);
    }


    private void buildDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Map<AeWrapper, List<ParticipantGradeWrapper>> filteredParticipantGardeMap = (Map<AeWrapper, List<ParticipantGradeWrapper>>) request.getSession().getAttribute("participantGradeMap");
        List<AeReportEntryWrapper> sortedAeWithGradesList = (List<AeReportEntryWrapper>) request.getSession().getAttribute("adverseEventsWithGradesList");
        Participant participant = (Participant) request.getSession().getAttribute("participant");
        Study study = (Study) request.getSession().getAttribute("study");
        String reportStartDate = (String) request.getSession().getAttribute("reportStartDate");
        String reportEndDate = (String) request.getSession().getAttribute("reportEndDate");
        Map<String, String> dateRangeMap = (Map<String, String>) request.getSession().getAttribute("dateRangeMap");
        document.setPageSize(PageSize.A4.rotate());
        
        // Footer for ACCRU AE Report
        HeaderFooter footer = new HeaderFooter(new Phrase("CRA Name: _____________________________    CRA Signature: ___________________________  Date of Completion: ___ / ___ / _______                  CONFIDENTIAL", FontFactory.getFont("Arial", 10, Font.PLAIN)), false);
        footer.setBorder(Rectangle.NO_BORDER);
        document.setFooter(footer);

        // Header for ACCRU AE Report
        Table insideTable = getHeaderForDocument(pdfWriter, study, participant, reportStartDate, reportEndDate);
        Phrase phrase = new Phrase (0);
        phrase.add(insideTable);
        phrase.add(new Phrase("\n"));
        HeaderFooter header = new HeaderFooter(phrase, false);
        header.setBorderWidthBottom(Rectangle.NO_BORDER);
        header.setBorder(Rectangle.NO_BORDER);
        header.setAlignment(Paragraph.ALIGN_CENTER);
        document.setHeader(header); 
        
        document.open();
        
        // Add instruction section to the report
        Table instructionsTable = getInstructionsForDocument(dateRangeMap.get(START_DATE), dateRangeMap.get(END_DATE));
        document.add(instructionsTable);
        
        // Main table displaying adverse events along with ctcae grades and start & end dates and other info
        addSurveySolicitedSymptomsToDocument(pdfWriter, document, sortedAeWithGradesList);
        
        // Add feedback questions section on the report
        addFeedbackQuestionsToDocument(document, pdfWriter);
    }


	private void addSurveySolicitedSymptomsToDocument(PdfWriter pdfWriter, Document document, 
			List<AeReportEntryWrapper> sortedAeWithGradesList) throws DocumentException, IOException {
		document.add(getBlankRow());
        Table table = getHeaderSectionForSurveySolicitedSymptomsTable();
        table.setLastHeaderRow(3);
        addSymptomsToTable(table, sortedAeWithGradesList);
        document.add(table);
        return;
	}
	
	private Table getBlankRow() throws DocumentException{
		Table table = new Table(1);
		table.setWidth(100);
		table.setBorderWidth(0);
		
		Paragraph p = new Paragraph("\n");
		Cell cell = new Cell();
		cell.add(p);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		return table;
	}

	private void addSymptomsToTable(Table table, List<AeReportEntryWrapper> sortedAeWithGradesList) throws BadElementException {
        Cell cell;
        for(AeReportEntryWrapper entry : sortedAeWithGradesList){
	        
	        // Adverse Event + meddra code
	        Paragraph paragraph = null;
	        if(entry.getCtcaeTerm() != null){
	        	paragraph = new Paragraph(entry.getCtcaeTerm(), FontFactory.getFont("Arial", 10, Font.PLAIN));
	        } else {
	        	paragraph = new Paragraph("[Patient added free text]", FontFactory.getFont("Arial", 10, Font.ITALIC));
	        }
	        String meddraCode = (entry.getMeddraCode() != null ? entry.getMeddraCode() : "");
	        Paragraph meddraCodeParagraph = new Paragraph(" (" + meddraCode + ")", FontFactory.getFont("Arial", 10, Font.PLAIN));
	        cell = new Cell(paragraph);
	        cell.add(meddraCodeParagraph);
	        cell.setRowspan(1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        // Start date
	        cell = new Cell(new Paragraph(convertDateToReqdFormat(entry.getStartDate()), FontFactory.getFont("Arial", 10, Font.PLAIN)));
	        cell.setRowspan(1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        // End date
	        cell = new Cell(new Paragraph(convertDateToReqdFormat(entry.getEndDate()), FontFactory.getFont("Arial", 10, Font.PLAIN)));
	        cell.setRowspan(1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        // Patient-reported PRO-CTCAE / Verbatim
	        String verbatim = entry.getProctcaeVerbatim();
	        String symptomName = verbatim.substring(0, verbatim.indexOf(COLON) + 1);
	        String symptomDescription = verbatim.substring(verbatim.indexOf(COLON) + 1);
	        Paragraph verbatimParagraph = new Paragraph(symptomName, FontFactory.getFont("Arial", 10, Font.PLAIN));
	        verbatimParagraph.add(new Chunk(symptomDescription, FontFactory.getFont("Arial", 10, Font.ITALIC)));
			cell = new Cell(verbatimParagraph);
	        cell.setRowspan(1);
	        cell.setUseBorderPadding(true);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        // System generated ctcae grade: removed from display
	        /*String grade;
	        if(ProctcaeGradeMapping.PRESENT_CLINICIAN_ASSESS.equals(entry.getGrade())){
	        	grade = entry.getGrade();
	        } else {
	        	grade = " Grade " + entry.getGrade();
	        }
	        cell = new Cell(new Paragraph(grade, FontFactory.getFont("Arial", 10, Font.PLAIN)));
	        cell.setRowspan(1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);*/
			
	        // Investigator reported grade
	        cell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 10, Font.PLAIN)));
	        cell.setRowspan(1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);

	        // Outcome
	        cell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 10, Font.PLAIN)));
	        cell.setRowspan(1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        // AE attribution
	        cell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 10, Font.PLAIN)));
	        cell.setRowspan(1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
        }
	}
	
	/** GetHeaderSectionForSurveySolicitedSymptomsTable() function.
	 * @param contdIndicator
	 * @return
	 * @throws DocumentException
	 * returns the header row of the table
	 */
	private Table getHeaderSectionForSurveySolicitedSymptomsTable() throws DocumentException {
        // Data Table
		Table table = new Table(7);
		table.setWidth(100);
		table.setWidths(new int[]{18, 10, 10, 30, 9, 15, 10});
		table.setAlignment(Element.ALIGN_CENTER);
        table.setAlignment(Element.ALIGN_MIDDLE);
        table.setPadding(1);
        table.setCellsFitPage(true);
        table.setBorder(0);
        
        // Adverse Event with meddra code header
        Cell cell = new Cell(new Paragraph(HEADER_AE_AND_MEDDRA_CODE, FontFactory.getFont("Arial", 10, Font.BOLD)));
        //Paragraph info = new Paragraph("(CTCAE v4.0)", FontFactory.getFont("Arial", 10, Font.PLAIN));
        cell.setRowspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell.add(info);
        /*cell.add(new Paragraph(" & ", FontFactory.getFont("Arial", 10, Font.PLAIN)));
        cell.add(new Paragraph(HEADER_MEDDRA_CODE, FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.add(new Paragraph("(v12.0)", FontFactory.getFont("Arial", 10, Font.PLAIN)));*/
        table.addCell(cell);
        
        // Start date
        cell = new Cell(new Paragraph(HEADER_START_DATE, FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.setRowspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph info = new Paragraph("(dd-MMM-yyyy)", FontFactory.getFont("Arial", 10, Font.PLAIN));
        cell.add(info);
        table.addCell(cell);
        
        // Old end date
        cell = new Cell(new Paragraph(HEADER_END_DATE, FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.setRowspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        info = new Paragraph("(dd-MMM-yyyy)", FontFactory.getFont("Arial", 10, Font.PLAIN));
        cell.add(info);
        table.addCell(cell);
        
        // Patient-reported PRO-CTCAE / Verbatim
        cell = new Cell(new Paragraph(HEADER_VERBATIM, FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.setRowspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        // System generated ctcae grade: remved from the display
        /*cell = new Cell(new Paragraph("Patient Reported Event Grade", FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);*/
		
        // Investigator Reported Event Grade
        cell = new Cell(new Paragraph(HEADER_INVESTIGATOR_REPORTED_GRADE, FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        // Outcome
        cell = new Cell(new Paragraph(HEADER_OUTCOME, FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        // AE Attribution
        cell = new Cell(new Paragraph(HEADER_AE_ATTRIBUTION, FontFactory.getFont("Arial", 10, Font.BOLD)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        
        // System generated ctcae grade: remved from the display
        /*com.lowagie.text.List patientReportedGradesList = new com.lowagie.text.List(false, 6);
        patientReportedGradesList.setListSymbol(listSymbol);
        patientReportedGradesList.setIndentationLeft(15);
        ListItem listItem = new ListItem("Grade 1", FontFactory.getFont("Arial", 9, Font.PLAIN));
        patientReportedGradesList.add(listItem);
        listItem = new ListItem("Grade 2", FontFactory.getFont("Arial", 9, Font.PLAIN));
        patientReportedGradesList.add(listItem);
        listItem = new ListItem("Grade 3", FontFactory.getFont("Arial", 9, Font.PLAIN));
        patientReportedGradesList.add(listItem);
        cell = new Cell(patientReportedGradesList);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);*/
        
        
        com.lowagie.text.List investigatorReportedGradesList = new com.lowagie.text.List(false, 6);
        Chunk listSymbol = new Chunk("\u2022", FontFactory.getFont("Arial", 12, Font.BOLD));
        investigatorReportedGradesList.setListSymbol(listSymbol);
        investigatorReportedGradesList.setIndentationLeft(10);
        ListItem listItem = new ListItem("Grade 1", FontFactory.getFont("Arial", 9, Font.PLAIN));
        investigatorReportedGradesList.add(listItem);
        listItem = new ListItem("Grade 2", FontFactory.getFont("Arial", 9, Font.PLAIN));
        investigatorReportedGradesList.add(listItem);
        listItem = new ListItem("Grade 3", FontFactory.getFont("Arial", 9, Font.PLAIN));
        investigatorReportedGradesList.add(listItem);
        listItem = new ListItem("Grade 4", FontFactory.getFont("Arial", 9, Font.PLAIN));
        investigatorReportedGradesList.add(listItem);
        listItem = new ListItem("Grade 5", FontFactory.getFont("Arial", 9, Font.PLAIN));
        investigatorReportedGradesList.add(listItem);
        cell = new Cell(investigatorReportedGradesList);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);
        
        
        com.lowagie.text.List outcomeList = new com.lowagie.text.List(false, 6);
        outcomeList.setListSymbol(listSymbol);
        outcomeList.setIndentationLeft(10);
        listItem = new ListItem("Recovered", FontFactory.getFont("Arial", 9, Font.PLAIN));
        outcomeList.add(listItem);
        listItem = new ListItem("Recovering", FontFactory.getFont("Arial", 9, Font.PLAIN));
        outcomeList.add(listItem);
        listItem = new ListItem("Not recovered", FontFactory.getFont("Arial", 9, Font.PLAIN));
        outcomeList.add(listItem);
        listItem = new ListItem("Recovered with sequelae", FontFactory.getFont("Arial", 9, Font.PLAIN));
        outcomeList.add(listItem);
        listItem = new ListItem("Fatal", FontFactory.getFont("Arial", 9, Font.PLAIN));
        outcomeList.add(listItem);
        listItem = new ListItem("Unknown", FontFactory.getFont("Arial", 9, Font.PLAIN));
        outcomeList.add(listItem);
        cell = new Cell(outcomeList);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);
        
        
        com.lowagie.text.List attributionList = new com.lowagie.text.List(false, 6);
        attributionList.setListSymbol(listSymbol);
        attributionList.setIndentationLeft(10);
        listItem = new ListItem("Unrelated", FontFactory.getFont("Arial", 9, Font.PLAIN));
        attributionList.add(listItem);
        listItem = new ListItem("Possible", FontFactory.getFont("Arial", 9, Font.PLAIN));
        attributionList.add(listItem);
        listItem = new ListItem("Probable", FontFactory.getFont("Arial", 9, Font.PLAIN));
        attributionList.add(listItem);
        cell = new Cell(attributionList);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);
        
        return table;
	}

	private void addFeedbackQuestionsToDocument(Document document, PdfWriter pdfWriter) throws DocumentException, IOException {
		document.newPage();
		document.add(new Paragraph("  ", FontFactory.getFont("Arial", 8)));
		document.add(new Paragraph("Feedback Questions:", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		
		Paragraph question = new Paragraph(" \n1. Who assigned the CTCAE grades?", FontFactory.getFont("Arial", 9, Font.PLAIN));
		Paragraph questionInstruction = new Paragraph(" (check one)", FontFactory.getFont("Arial", 9, Font.ITALIC));
		question.add(questionInstruction);
		document.add(question);
		Paragraph responseOptions = new Paragraph("", FontFactory.getFont("Arial", 9, Font.PLAIN));
		responseOptions.add(new Phrase(" O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" Investigator", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.add(new Phrase("    O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" CRA (Nurse)", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.add(new Phrase("    O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" CRA (Non-nurse)", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.add(new Phrase("    O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" Other", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		
		//responseOptions.add(lineContent);
		responseOptions.setIndentationLeft(20f);
		document.add(responseOptions);
		
		question = new Paragraph(" \n2. Did the person who assigned the CTCAE grades have access to the patient's self-reported AE grades at the time they " +
				"assigned the CTCAE grades?", FontFactory.getFont("Arial", 9, Font.PLAIN));
		questionInstruction = new Paragraph(" (check one)", FontFactory.getFont("Arial", 9, Font.ITALIC));
		question.add(questionInstruction);
		document.add(question);
		responseOptions = new Paragraph("", FontFactory.getFont("Arial", 9, Font.PLAIN));
		//responseOptions = new Paragraph(" O Yes    O No (end form)    O Unsure (end form)", FontFactory.getFont("Arial", 9, Font.PLAIN));
		responseOptions.add(new Phrase(" O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" Yes", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.add(new Phrase("    O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" No (end form)", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.add(new Phrase("    O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" Unsure (end form)", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		
		responseOptions.setIndentationLeft(20f);
		document.add(responseOptions);
		
		question = new Paragraph(" \n3. If Yes, did the patient's self-reported AE grades influence the CTCAE grading?", FontFactory.getFont("Arial", 9, Font.PLAIN));
		questionInstruction = new Paragraph(" (check one)", FontFactory.getFont("Arial", 9, Font.ITALIC));
		question.add(questionInstruction);
		document.add(question);
		responseOptions = new Paragraph("", FontFactory.getFont("Arial", 9, Font.PLAIN));
		//responseOptions = new Paragraph(" O Yes    O No (end form)    O Unsure", FontFactory.getFont("Arial", 9, Font.PLAIN));
		responseOptions.add(new Phrase(" O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" Yes", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.add(new Phrase("    O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" No (end form)", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.add(new Phrase("    O ", FontFactory.getFont("Arial", 10, Font.BOLD)));
		responseOptions.add(new Phrase(" Unsure", FontFactory.getFont("Arial", 9, Font.PLAIN)));
		responseOptions.setIndentationLeft(20f);
		document.add(responseOptions);
	}


	private Table getInstructionsForDocument(String reportStartDate, String reportEndDate) throws DocumentException {
        Table instructionsTable = new Table(1);
        instructionsTable.setWidth(100);
        instructionsTable.setBorderWidth(0);

        Paragraph instructionalParagraph = new Paragraph("\nINSTRUCTIONS: ", FontFactory.getFont("Arial", 10, Font.PLAIN));
        instructionalParagraph.add(new Phrase("\n\nReview and update each cycle. Use this form to assist reporting into RAVE. Note that RAVE will also " +
        		"include fields for: Study drug action taken (including dose modifications) and Serious Adverse Events. A new log line should " +
        		"be added each time a new instance of an Adverse Event occurs or if the grade changes. For further details please refer to the Forms Instructions."
        		 , FontFactory.getFont("Arial", 9, Font.PLAIN)));
        
        instructionalParagraph.setIndentationLeft(1);
        Paragraph datePara = new Paragraph("\n\n Reporting Period for this form: Start Date: " + (StringUtils.isEmpty(reportStartDate) ? "__ / __ / ____" : convertDateToReqdFormat(reportStartDate)) +
        		", End Date: " + (StringUtils.isEmpty(reportEndDate) ? "__ / __ / ____" : convertDateToReqdFormat(reportEndDate)) , FontFactory.getFont("Arial", 10, Font.PLAIN));
        instructionalParagraph.add(datePara);

        Cell headerCell = new Cell(instructionalParagraph);
        headerCell.setBorderWidth(0);
        instructionsTable.addCell(headerCell);
        
        return instructionsTable;
	}

	private String convertDateToReqdFormat(String reportEndDate) {
		StringBuffer formattedDate = new StringBuffer("");
		String[] splitArr = reportEndDate.split("/");

		if(splitArr.length == 3){
			formattedDate.append(splitArr[1]);
			formattedDate.append("-");
			formattedDate.append(getMonth(splitArr[0]));
			formattedDate.append("-");
			formattedDate.append(splitArr[2]);
		} else {
			Calendar cal = Calendar.getInstance();
			
			formattedDate.append(cal.get(Calendar.DAY_OF_MONTH));
			formattedDate.append("-");
			formattedDate.append(getMonth(String.valueOf(cal.get(Calendar.MONTH) + 1)));
			formattedDate.append("-");
			formattedDate.append(cal.get(Calendar.YEAR));
		}
		
		return formattedDate.toString();
	}
	
	private String convertDateToReqdFormat(Date date) {
		if(date != null){
			String formatedDate = DateUtils.format(date);
			return  convertDateToReqdFormat(formatedDate);
		}
		
		return BLANK_DATE;
	}
	
	private Object getMonth(String string) {
		if(string.equalsIgnoreCase("01") || string.equalsIgnoreCase("1")){
			return "JAN";
		}
		if(string.equalsIgnoreCase("02") || string.equalsIgnoreCase("2")){
			return "FEB";
		}
		if(string.equalsIgnoreCase("03") || string.equalsIgnoreCase("3")){
			return "MAR";
		}
		if(string.equalsIgnoreCase("04") || string.equalsIgnoreCase("4")){
			return "APR";
		}
		if(string.equalsIgnoreCase("05") || string.equalsIgnoreCase("5")){
			return "MAY";
		}
		if(string.equalsIgnoreCase("06") || string.equalsIgnoreCase("6")){
			return "JUN";
		}
		if(string.equalsIgnoreCase("07") || string.equalsIgnoreCase("7")){
			return "JUL";
		}
		if(string.equalsIgnoreCase("08") || string.equalsIgnoreCase("8")){
			return "AUG";
		}
		if(string.equalsIgnoreCase("09") || string.equalsIgnoreCase("9")){
			return "SEP";
		}
		if(string.equalsIgnoreCase("10")){
			return "OCT";
		}
		if(string.equalsIgnoreCase("11")){
			return "NOV";
		}
		if(string.equalsIgnoreCase("12")){
			return "DEC";
		}
		return "";
	}
	
	private Table getHeaderForDocument(PdfWriter pdfWriter, Study study, Participant participant, String reportStartDate, String reportEndDate) throws DocumentException, IOException {
		Table insideTable = new Table(7);
		insideTable.setAlignment(Table.ALIGN_LEFT);
		insideTable.setBorder(Rectangle.NO_BORDER);
		insideTable.setWidth(100);
		insideTable.setWidths(new int[]{33, 3, 10, 21, 3, 15, 15});
		
		Paragraph para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.BOLD));
		Cell headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
		
		headerCell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN)));
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("ACADEMIC and COMMUNITY CANCER", FontFactory.getFont("Arial", 11, Font.BOLD));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        headerCell = new Cell(new Paragraph("RESEARCH UNITED - ACCRU", FontFactory.getFont("Arial", 11, Font.BOLD)));
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setNoWrap(false);
        insideTable.addCell(headerCell);
        
        headerCell = new Cell(new Paragraph("Adverse Event Log", FontFactory.getFont("Arial", 14, Font.BOLD)));
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        para1 = new Paragraph("Protocol Number:", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        para1 = new Paragraph(study.getAssignedIdentifier(), FontFactory.getFont("Arial", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        headerCell = new Cell(new Paragraph(""));
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 14, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
        headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        para1 = new Paragraph("Patient ID:", FontFactory.getFont("Arial", 11, Font.PLAIN));
        headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        String studyParticipantIdentifier = null;
        if(participant.getStudyParticipantAssignments().size() > 0){
        	studyParticipantIdentifier = participant.getStudyParticipantAssignments().get(0).getStudyParticipantIdentifier();
        }
        para1 = new Paragraph((studyParticipantIdentifier != null ? studyParticipantIdentifier : NO_PARTICIPANT_IDENTIFIER) + "\n", FontFactory.getFont("Arial", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE));
        headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        //Blank line after header
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        headerCell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN)));
        headerCell.setColspan(3);
        headerCell.setRowspan(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setNoWrap(false);
        insideTable.addCell(headerCell);
        
        headerCell = new Cell(new Paragraph("\n"));
        headerCell.setColspan(7);
        headerCell.setRowspan(2);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);

        return insideTable;
	}
}