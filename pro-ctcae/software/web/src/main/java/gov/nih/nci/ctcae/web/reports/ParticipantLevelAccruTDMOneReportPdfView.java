package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.ValidValue;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

/**
 * @author Amey
 * ParticipantLevelAccruTDMOneReportPdfView class
 * TODO: Correct the meddra code for "Alanine Aminotransferase increased"
 */
public class ParticipantLevelAccruTDMOneReportPdfView extends AbstractPdfView {
	
	public static final String[] aeNameArray = new String[]{"Aspartate Aminotransferase increased", "Alanine Aminotransferase increased", "Blood bilirubin increased", 
		"Platelet count decreased", "Hypoxia", "Pneumonitis", "Peripheral motor neuropathy", "Peripheral sensory neuropathy", "Anorexia", "Blurred vision", 
		"Bruising ", "Constipation", "Cough", "Diarrhea", "Dry Mouth", "Dyspnea", "Epistaxis", "Fatigue", "Flashing lights", "Floaters", "Headache", "Insomnia",
		"Myalgia", "Mucositis oral", "Nail discoloration", "Nail loss", "Nail ridging", "Nausea", "Pain", "Rash maculo-papular", "Urticaria", "Vomiting", 
		"Watering eyes"};
		
	public static final String[] aeMeddraCode = new String[]{"10003481", "10001551", "10005364", "10035528", "10021143", "10035742", "10034580", "10034620",
			"10002646", "10005886", "10006504", "10010774", "10011224", "10012727", "10013781", "10013963", "10015090", "10016256", "10016757", "10016778",
			"10019211", "10022437", "10028411", "10028130", "10028691", "10049281", "10062283", "10028813", "10033371", "10037868", "10046735", "10047700", 
			"10047848"};
	public static final int[] aeGrade = new int[]{4, 4, 4, 4, 5, 5, 5, 5, 5, 3, 2, 5, 3, 5, 3, 5, 5, 3, 3, 3, 3, 3, 3, 5, 1, 2, 1, 3, 3, 3, 3, 5, 3};
	
	private int totalPages = 0;
	
	private static final String CTC_ADVERSE_EVENT_TERM = "CTC Adverse Event Term\n (CTCAE v 4.0)";
	private static final String MEDDRA_ADVERSE_EVENT_CODE = "MedDRA Code \n (v. 12.0)";
	private static final String MEDDRA_ADVERSE_EVENT_CODE_PHRASE_ONE = "\n (must be completed)";
	private static final String PATIENT_REPORTED_AE_GRADES = "\n Patient Self-Reported AE Grades \n";
	private static final String CTC_ADVERSE_EVENT_GRADE = "CTC Adverse Event Grade";
	private static final String CTC_ADVERSE_EVENT_GRADE_PHRASE_ONE = "\n (highest grade in this reporting period) \n\n";
	private static final String CTC_ADVERSE_EVENT_GRADE_PHRASE_TWO = "\n\n  INCLUDE GRADE 0's";
	private static final String AE_EXPEDITE_REPORT_PHRASE_ONE = "Has an adverse event expedited report been submitted?*\n";
	private static final String AE_EXPEDITE_REPORT_PHRASE_TWO = "Enter a # below: \n 1=Yes\n 2=No";
	private static final String CTCAE_ATTRIBUTION_CODE = "CTCAE Attribution Code";
	private static final String CTCAE_ATTRIBUTION_CODE_PHRASE_ONE = "\n(If Grade >0)";
	private static final String CTCAE_ATTRIBUTION_CODE_PHRASE_TWO = "Enter a # below:\n 1=Unrelated\n 2=Unlikely\n 3=Possible\n 4=Probable\n 5=Definite";
	private static final String SEVERITY = "Severity";
	private static final String FREQUENCY = "Frequency";
	private static final String INTERFERENCE = "Interference with Activities of Daily Life";
	private static final String DATE_SPLITTER_SYMBOL = "/";
	private static final String NEW_LINE = "\n";
	private static final String HEADER_PROTOCOL_TITLE = "ACADEMIC and COMMINITY CANCER RESEARCH UNITED\n\t(ACCRU)";
	private static final String HEADER_FORM_TITLE = "Adverse Event Form";
	private static final String ADDITIONAL_CTC_ADVERSE_EVENT_TERM = "(Other) CTC Adverse Event Term\n (CTCAE v 4.0)";
	private static final String ADDITIONAL_MEDDRA_ADVERSE_EVENT_CODE = "MedDRA Code \n (v. 12.0)\n";
	private static final String ADDITIONAL_CTC_ADVERSE_EVENT_GRADE = "CTC Adverse Event Grade";
	private static final String ADDITIONAL_CTCAE_ATTRIBUTION_CODE = "CTCAE Attribution Code";
	private static final String ADDITIONAL_CTCAE_ATTRIBUTION_CODE_PHRASE_ONE = "\n(If Grade >0)";
	private static final String ADDITIONAL_CTCAE_ATTRIBUTION_CODE_PHRASE_TWO = "\nEnter a # below:\n 1=Unrelated\n 2=Unlikely\n 3=Possible\n 4=Probable\n 5=Definite";
	private static final String ADDITIONAL_AE_EXPEDITE_REPORT_PHRASE_ONE = "Has an adverse event expedited report been submitted?*\n\n";
	private static final String ADDITIONAL_AE_EXPEDITE_REPORT_PHRASE_TWO = "Enter a # below: \n 1=Yes\n 2=No";
	private static final String ADDITINOAL_TABLE_NOTE = "Adverse Events** beyond those required in Section 10.0 of the protocol.  Record grade 2 with attribution of possible, probable or definite and all grade 3, 4, and 5 regardless of attribution. ";

	

	class AEDetail {
		String AEName;
		String AEMeddraCode;
		int AEGrade;

		public String getAEName() {
			return AEName;
		}

		public void setAEName(String aEName) {
			AEName = aEName;
		}

		public String getAEMeddraCode() {
			return AEMeddraCode;
		}

		public void setAEMeddraCode(String aEMeddraCode) {
			AEMeddraCode = aEMeddraCode;
		}

		public int getAEGrade() {
			return AEGrade;
		}

		public void setAEGrade(int aEGrade) {
			AEGrade = aEGrade;
		}
	}
	
	private List<AEDetail> buildAEListForDisplay(int startIndex, int endIndex){
		List<AEDetail> aeDetailList = new ArrayList<AEDetail>();
		AEDetail aeDetail;
		for(int i=startIndex; i <= endIndex; i++){
			aeDetail = new AEDetail();
			aeDetail.setAEName(aeNameArray[i]);
			aeDetail.setAEMeddraCode(aeMeddraCode[i]);
			aeDetail.setAEGrade(aeGrade[i]);
			
			aeDetailList.add(aeDetail);
		}
		return aeDetailList;
	}

    protected void buildPdfDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document dupe = new Document(PageSize.A4.rotate());
        dupe.setMargins(35, 35, 15, 15);
        PdfWriter dupePdfWriter = PdfWriter.getInstance(dupe, bos);
        dupe.open();
        buildDocument(map, dupe, dupePdfWriter, request, httpServletResponse);
        totalPages = dupePdfWriter.getPageNumber();
        
        document.setMargins(35, 35, 15, 15);
        buildDocument(map, document, pdfWriter, request, httpServletResponse);
    }


    private void buildDocument(Map map, Document document, PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results = (TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>) request.getSession().getAttribute("sessionResultsMap");
        List<ProCtcQuestionType> questionTypes = (List<ProCtcQuestionType>) request.getSession().getAttribute("questionTypes");
        Participant participant = (Participant) request.getSession().getAttribute("participant");
        Study study = (Study) request.getSession().getAttribute("study");
        String reportStartDate = (String) request.getSession().getAttribute("reportStartDate");
        String reportEndDate = (String) request.getSession().getAttribute("reportEndDate");

        document.setPageSize(PageSize.A4.rotate());
        
        // Header table
        Table insideTable = getHeaderForDocument(pdfWriter, study, participant, reportStartDate, reportEndDate);
        Phrase phrase = new Phrase (0);
        phrase.add(insideTable);
        HeaderFooter header = new HeaderFooter(phrase, false);
        header.setBorderWidthBottom(1);
        header.setBorder(0);
        header.setAlignment(Paragraph.ALIGN_CENTER);
        document.setHeader(header); 
        
        HeaderFooter footer = new HeaderFooter(new Phrase(""), false);
        footer.setBorder(Rectangle.NO_BORDER);
        document.setFooter(footer);
        document.open();
        
        addCheckboxesToHeader(pdfWriter, false);

        // add instruction section
        Table instructionsTable = getInstructionsForDocument(reportStartDate, reportEndDate);
        document.add(instructionsTable);
        // survey solicited symptoms section header
        addSurveySolicitedSymptomsToDocument(pdfWriter, document, results);
        document.newPage();
        addFeedbackQuestionsToDocument(document, pdfWriter);
        addClinicianReportedSymptomsTableToDocument(document, pdfWriter);
    	addCheckboxesToHeader(pdfWriter, true);
    }


	/**
	 * Adds the checkboxes to header.
	 */
	private void addCheckboxesToHeader(PdfWriter pdfWriter, boolean portrait) throws IOException, DocumentException {
		String yesStr = "Yes";
		String noStr = "No";
		RadioCheckField check = null;
		if(!portrait) {
			 //check = new RadioCheckField(pdfWriter, new Rectangle(490, 52, 500, 62), yesStr + Math.random(), yesStr);
			 check = new RadioCheckField(pdfWriter, new Rectangle(542, 492, 552, 502), yesStr + Math.random(), yesStr);
		} else {
			check = new RadioCheckField(pdfWriter, new Rectangle(380, 715, 390, 725), yesStr + Math.random(), yesStr);
		}
        check.setCheckType(RadioCheckField.TYPE_CROSS);
        check.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
        check.setBorderColor(Color.black);
        check.setBackgroundColor(Color.white);
        PdfFormField ck = check.getCheckField();
        pdfWriter.addAnnotation(ck);
        
        if(!portrait) {
        	//check = new RadioCheckField(pdfWriter, new Rectangle(490, 22, 500, 32), noStr + Math.random(), yesStr);
        	check = new RadioCheckField(pdfWriter, new Rectangle(574, 492, 584, 502), noStr + Math.random(), yesStr);
        } else {
        	check = new RadioCheckField(pdfWriter, new Rectangle(410, 715, 420, 725), noStr + Math.random(), yesStr);
        }
        check.setCheckType(RadioCheckField.TYPE_CROSS);
        check.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
        check.setBorderColor(Color.black);
        check.setBackgroundColor(Color.white);	
        ck = check.getCheckField();
        pdfWriter.addAnnotation(ck);
        
        PdfContentByte cb = pdfWriter.getDirectContent();
        cb.beginText();
		cb.setFontAndSize(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), 8f);
		if(!portrait) {
			//cb.showTextAligned(Element.ALIGN_LEFT, yesStr, 544, 490, 0);
			cb.showTextAligned(Element.ALIGN_LEFT, yesStr, 554, 492, 0);
		} else {
			cb.showTextAligned(Element.ALIGN_LEFT, yesStr, 395, 715, 0);
		}
		cb.endText();
		
        cb.beginText();
		cb.setFontAndSize(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), 8f);
		if(!portrait) {
			//cb.showTextAligned(Element.ALIGN_LEFT, noStr, 576, 490, 0);
			cb.showTextAligned(Element.ALIGN_LEFT, noStr, 586, 492, 0);
		} else {
			cb.showTextAligned(Element.ALIGN_LEFT, noStr, 425, 715, 0);
		}
		cb.endText();
		
        cb = pdfWriter.getDirectContent();
        cb.beginText();
        //new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.NORMAL)
		cb.setFontAndSize(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), 10f);
		int currentPageNumber = pdfWriter.getPageNumber() - 1;
		int total = totalPages - 1;
		cb.showTextAligned(Element.ALIGN_LEFT, "Page "+ currentPageNumber +" of "+ total, 650, 12, 0);
		cb.endText();
	}
	
	private void addSurveySolicitedSymptomsToDocument(PdfWriter pdfWriter, Document document, 
			TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results) throws DocumentException, IOException {
		
		Table table = getHeaderSectionForSurveySolicitedSymptomsTable(false);
        List<AEDetail> aeDetailList = buildAEListForDisplay(0, 7);
        addSymptomsToTable(table, aeDetailList, results);
        document.add(table);
		addCheckboxesToHeader(pdfWriter, false);
		pdfWriter.newPage();

		table = getHeaderSectionForSurveySolicitedSymptomsTable(true);
        aeDetailList = buildAEListForDisplay(8, 20);
        addSymptomsToTable(table, aeDetailList, results);
        document.add(table);
		addCheckboxesToHeader(pdfWriter, false);
		pdfWriter.newPage();
        
		table = getHeaderSectionForSurveySolicitedSymptomsTable(true);
        aeDetailList = buildAEListForDisplay(21, 32);
        addSymptomsToTable(table, aeDetailList, results);
        document.add(table);
        addCheckboxesToHeader(pdfWriter, false);
        pdfWriter.newPage();
        
       /* table = getHeaderSectionForSurveySolicitedSymptomsTable(true);
        aeDetailList = buildAEListForDisplay(31, 32);
        addSymptomsToTable(table, aeDetailList, results);
        document.add(table);
		addCheckboxesToHeader(pdfWriter, false);
		pdfWriter.newPage();*/
		
        return;
	}


	private void addSymptomsToTable(Table table, List<AEDetail> aeDetailList, TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results) throws BadElementException {
        boolean addBlankCells;
        Cell cell;
	       for(AEDetail aed : aeDetailList){
	        	cell = new Cell(new Paragraph(aed.getAEName(), FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            table.addCell(cell);
	            
	            cell = new Cell(new Paragraph(aed.getAEMeddraCode(), FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            
	            //see if participant has answered any of the aeSymptoms we are going to report.
	            addBlankCells = true;
	            for (String[] term : results.keySet()) {
	            	if(term[2].equalsIgnoreCase(aed.getAEMeddraCode())){
	            		addBlankCells = false;
	                    HashMap<Question, ArrayList<ValidValue>> questionMap = results.get(term);
	                    // For Severity type
	                    String severityValue = "-";
	                    for (Question question : questionMap.keySet()) {
	                        if (question.getQuestionType().getDisplayName().equals("Severity")) {
	                            List<ValidValue> proCtcValidValues = questionMap.get(question);
	                            severityValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
	                        }
	                        if (question.getQuestionType().getDisplayName().equals("Amount")) {
	                            List<ValidValue> proCtcValidValues = questionMap.get(question);
	                            severityValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
	                        }
	                    }
	                    cell = new Cell(new Paragraph(severityValue, FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
	                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                    table.addCell(cell);
	                    
	                    // For frequency type
	                    String frequencyValue = "-";
	                    for (Question question : questionMap.keySet()) {
	                        if (question.getQuestionType().getDisplayName().equals("Frequency")) {
	                            List<ValidValue> proCtcValidValues = questionMap.get(question);
	                            frequencyValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
	                        }
	                        if (question.getQuestionType().getDisplayName().equals("Present/Absent")) {
	                            List<ValidValue> proCtcValidValues = questionMap.get(question);
	                            String presentValue = "";
	                            if(proCtcValidValues.get(0) instanceof ProCtcValidValue){
	                            	presentValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
	                            } else if(proCtcValidValues.get(0) instanceof MeddraValidValue){
	                                presentValue = ((MeddraValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
	                            }
	                            if (presentValue.trim().equals("Yes")) {
	                                frequencyValue = "Present";
	                            } else {
	                                frequencyValue = "Absent";
	                            }
	                        }
	                    }
	                    cell = new Cell(new Paragraph(frequencyValue, FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
	                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                    table.addCell(cell);

	                    // For Interference type
	                    String interferenceValue = "-";
	                    for (Question question : questionMap.keySet()) {
	                        if (question.getQuestionType().getDisplayName().equals("Interference")) {
	                            List<ValidValue> proCtcValidValues = questionMap.get(question);
	                            interferenceValue = ((ProCtcValidValue)proCtcValidValues.get(0)).getValue(SupportedLanguageEnum.ENGLISH);
	                        }
	                    }
	                    cell = new Cell(new Paragraph(interferenceValue, FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
	                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                    table.addCell(cell);
	            	}
	            }
	            if(addBlankCells){
	                cell = new Cell(new Paragraph(" "));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	            }
	        	
	            cell = new Cell(new Paragraph("0", FontFactory.getFont("Times-Roman", 12)));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            if("10021143".equalsIgnoreCase(aed.getAEMeddraCode())){
	            	cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	            } else {
	            	cell = new Cell(new Paragraph("1", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            	table.addCell(cell);
	            }
	            
	            if(aed.getAEGrade() == 1){
	            	cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	            }
	            if(aed.getAEGrade() == 2){
	            	cell = new Cell(new Paragraph("2", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	            }
	            if(aed.getAEGrade() == 3){
	            	cell = new Cell(new Paragraph("2", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph("3", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	            }
	            if(aed.getAEGrade() == 4){
	            	cell = new Cell(new Paragraph("2", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph("3", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph("4", FontFactory.getFont("Times-Roman", 12)));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	            }
	            if(aed.getAEGrade() == 5){
	            	cell = new Cell(new Paragraph("2", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph("3", FontFactory.getFont("Times-Roman", 12)));
	            	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph("4", FontFactory.getFont("Times-Roman", 12)));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph("5 (death)", FontFactory.getFont("Times-Roman", 12)));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                cell.setVerticalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	            }
	            
	            cell = new Cell(new Paragraph(" "));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            
	            cell = new Cell(new Paragraph(" "));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	        }
	}

	private Table getHeaderSectionForSurveySolicitedSymptomsTable(boolean contdIndicator) throws DocumentException {
        // Data Table
        Table table = new Table(13);
        table.setWidth(100);
        table.setWidths(new int[]{19, 9, 10, 10, 10, 3, 3, 3, 3, 3, 7, 10, 10});
        table.setAlignment(Element.ALIGN_CENTER);
        table.setAlignment(Element.ALIGN_MIDDLE);
        table.setPadding(1);
        table.setCellsFitPage(true);
        table.setBorder(0);
        
        Cell cell = new Cell(new Paragraph(CTC_ADVERSE_EVENT_TERM, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        Paragraph meddraAdverseEventCode = new Paragraph(MEDDRA_ADVERSE_EVENT_CODE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        meddraAdverseEventCode.add(new Phrase(MEDDRA_ADVERSE_EVENT_CODE_PHRASE_ONE, FontFactory.getFont("Times-Roman", 9, Font.ITALIC)));
        cell = new Cell(meddraAdverseEventCode);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        Paragraph p = new Paragraph(PATIENT_REPORTED_AE_GRADES, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        p.add(new Phrase(NEW_LINE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell = new Cell(p);
        cell.setRowspan(1);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        Paragraph aeg = new Paragraph(CTC_ADVERSE_EVENT_GRADE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        aeg.add(new Phrase(CTC_ADVERSE_EVENT_GRADE_PHRASE_ONE, FontFactory.getFont("Times-Roman", 9, Font.ITALIC)));
        aeg.add(new Paragraph(CTC_ADVERSE_EVENT_GRADE_PHRASE_TWO, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell = new Cell(aeg);
        cell.setColspan(6);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        Paragraph attribution = new Paragraph(CTCAE_ATTRIBUTION_CODE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        attribution.add(new Paragraph(CTCAE_ATTRIBUTION_CODE_PHRASE_ONE, FontFactory.getFont("Times-Roman", 9, Font.ITALIC)));
        attribution.add(new Paragraph(CTCAE_ATTRIBUTION_CODE_PHRASE_TWO, FontFactory.getFont("Times-Roman", 9, Font.ITALIC)));
        cell = new Cell(attribution);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);

        attribution = new Paragraph(AE_EXPEDITE_REPORT_PHRASE_ONE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        attribution.add(new Phrase(AE_EXPEDITE_REPORT_PHRASE_TWO, FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell = new Cell(attribution);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph(SEVERITY, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(1);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph(FREQUENCY, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(1);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph(INTERFERENCE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(1);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        return table;
	}



	private void addFeedbackQuestionsToDocument(Document document, PdfWriter pdfWriter) throws DocumentException, IOException {
        
      document.add(new Paragraph("  ", FontFactory.getFont("Times-Roman", 8)));

		Paragraph fQuestion = new Paragraph("Is the person who filled out the above form the same person who assigned the actual CTCAE grades for this patient?", FontFactory.getFont("Times-Roman", 10));
		fQuestion.add(new Phrase(" (check one)", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        document.add(fQuestion);
        document.add(new Paragraph("    O Yes", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O No", FontFactory.getFont("Times-Roman", 10)));
        
        document.add(new Paragraph(" ", FontFactory.getFont("Times-Roman", 6)));
		Paragraph sQuestion = new Paragraph("Did the person who assigned CTCAE grades for the above form have access to the patient's self-reported AE " +
        		"grades at the time they assigned the CTCAE grades?", FontFactory.getFont("Times-Roman", 10));
		sQuestion.add(new Phrase(" (check one)", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        document.add(sQuestion);
        Paragraph para = new Paragraph("    O Yes ", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        document.add(para);
        
        para = new Paragraph("    O No ", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        para.add(new Phrase("(If No, proceed to Other Adverse Events question)", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        document.add(para);
        
        document.add(new Paragraph(" ", FontFactory.getFont("Times-Roman", 6)));
		Paragraph tQuestion = new Paragraph("Were any of the assigned CTCAE grades in the above form different than they would " +
        		"have been in the absence of access to the patient's self-reported AE responses?", FontFactory.getFont("Times-Roman", 10));
		tQuestion.add(new Phrase(" (check one)", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        document.add(tQuestion);
        document.add(new Paragraph("    O Yes", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O No"	, FontFactory.getFont("Times-Roman", 10)));
        
        addCheckboxesToHeader(pdfWriter, false);
        
        //document.setPageSize(PageSize.A4);
        document.newPage();
	    document.add(new Paragraph("  ", FontFactory.getFont("Times-Roman", 8)));
	    Paragraph blurb = new Paragraph("Were", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
	    blurb.add(new Phrase(" (other) ", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
	    blurb.add(new Phrase("adverse events assessed during this report period?", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
	    document.add(blurb);
	        
	    blurb = new Paragraph("O Yes, and reportable adverse events occurred " + NEW_LINE , FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
	    blurb.setIndentationLeft(20f);
	    document.add(blurb);
	        
	    blurb = new Paragraph("O Yes, but no reportable adverse events occurred", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
	    blurb.add(new Phrase(" (end form) ", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
	    blurb.setIndentationLeft(20f);
	    document.add(blurb);
	        
	    blurb = new Paragraph("O No", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
	    blurb.add(new Phrase(" (end form) " + NEW_LINE, FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
	    blurb.setIndentationLeft(20f);
	    document.add(blurb);
	}


	private Table getInstructionsForDocument(String reportStartDate, String reportEndDate) throws DocumentException {
        Table instructionsTable = new Table(1);
        instructionsTable.setWidth(100);
        instructionsTable.setBorderWidth(0);

        Paragraph instructionalParagraph = new Paragraph(NEW_LINE + "Current Cycle Number", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        instructionalParagraph.add(new Phrase("(adverse events associated with this cycle): _______" + NEW_LINE + NEW_LINE , FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        
        Paragraph datePara = new Paragraph("Date of evaluation: ", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        datePara.add(new Phrase("(mm/dd/yyyy)  ", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        datePara.add(new Phrase(convertDateToReqdFormat(reportEndDate) + NEW_LINE , FontFactory.getFont("Times-Roman", 10, Font.PLAIN | com.lowagie.text.Font.UNDERLINE)));
        instructionalParagraph.add(datePara);

        Cell headerCell = new Cell(instructionalParagraph);
        headerCell.setBorderWidth(0);
        instructionsTable.addCell(headerCell);
        
        return instructionsTable;
	}


	private String convertDateToReqdFormat(String reportEndDate) {
		StringBuffer formattedDate = new StringBuffer("");
		String[] splitArr = reportEndDate.split(DATE_SPLITTER_SYMBOL);

		if(splitArr.length == 3){
			formattedDate.append(splitArr[0]);
			formattedDate.append(DATE_SPLITTER_SYMBOL);
			formattedDate.append(splitArr[1]);
			formattedDate.append(DATE_SPLITTER_SYMBOL);
			formattedDate.append(splitArr[2]);
		} else {
			Calendar cal = Calendar.getInstance();
			formattedDate.append(cal.get(Calendar.MONTH) + 1);
			formattedDate.append(DATE_SPLITTER_SYMBOL);
			formattedDate.append(cal.get(Calendar.DATE));
			formattedDate.append(DATE_SPLITTER_SYMBOL);
			formattedDate.append(cal.get(Calendar.YEAR));
		}
		
		return formattedDate.toString();
	}

	private Table getHeaderForDocument(PdfWriter pdfWriter, Study study, Participant participant, String reportStartDate, String reportEndDate) throws DocumentException, IOException {
		Table insideTable = new Table(9);
		insideTable.setAlignment(Table.ALIGN_LEFT);
		insideTable.setBorder(0);
		insideTable.setBorderWidthBottom(0);
		insideTable.setBorderWidthLeft(0);
		insideTable.setBorderWidthRight(0);
		insideTable.setBorderColor(Color.BLACK);
        insideTable.setWidth(81);
        insideTable.setWidths(new int[]{13, 6, 16, 10, 1, 10, 10, 10, 25});
        
        // first row
        Cell headerCell = new Cell(new Paragraph("PLACE LABEL HERE", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        headerCell.setColspan(5);
        headerCell.setRowspan(1);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthLeft(0);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        insideTable.addCell(headerCell);
       
        headerCell = new Cell(new Paragraph(HEADER_PROTOCOL_TITLE, FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        headerCell.setColspan(4);
        headerCell.setRowspan(3);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthLeft(1);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        insideTable.addCell(headerCell);
        
        //second row
        Paragraph para1 = new Paragraph(" Protocol Number:", FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        headerCell = new Cell(para1);
        headerCell.setColspan(2);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph(String.format(" %s", study.getAssignedIdentifier().toUpperCase()), FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        insideTable.addCell(headerCell);
        
       /* para1 = new Paragraph("", FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        insideTable.addCell(headerCell);*/



        Paragraph para2 = new Paragraph(" Patient ID: ", FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        para2.add(new Phrase(String.format("  %s  ", participant.getStudyParticipantIdentifier().toUpperCase()), FontFactory.getFont("Times-Roman", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE)));
        headerCell = new Cell(para2);
        headerCell.setColspan(2);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        insideTable.addCell(headerCell);
        
        para2 = new Paragraph(" Patient Initials: " , FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        Cell piCell = new Cell(para2);
        piCell.setColspan(1);
        piCell.setRowspan(2);
        piCell.setBorderWidthBottom(0);
        piCell.setBorderWidthTop(0);
        piCell.setBorderWidthRight(0);
        piCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        piCell.setVerticalAlignment(Element.ALIGN_TOP);
        insideTable.addCell(piCell);
        
        para2 = new Paragraph(String.format(" %s", participant.getNameInitialsForReports()), FontFactory.getFont("Times-Roman", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE));
        piCell = new Cell(para2);
        piCell.setColspan(1);
        piCell.setRowspan(1);
        piCell.setBorderWidthBottom(0);
        piCell.setBorderWidthTop(0);
        piCell.setBorderWidthRight(0);
        piCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        piCell.setVerticalAlignment(Element.ALIGN_TOP);
        insideTable.addCell(piCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setBorderWidthLeft(0);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        insideTable.addCell(headerCell);

        headerCell = new Cell(new Paragraph(HEADER_FORM_TITLE, FontFactory.getFont("Times-Roman", 11, Font.BOLD)));
        headerCell.setColspan(4);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthLeft(1);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        insideTable.addCell(headerCell);
        
        para2 = new Paragraph(" L  F  M", FontFactory.getFont("Times-Roman", 6, Font.PLAIN));
        piCell = new Cell(para2);
        piCell.setColspan(1);
        piCell.setRowspan(1);
        piCell.setBorderWidthBottom(0);
        piCell.setBorderWidthTop(0);
        piCell.setBorderWidthRight(0);
        piCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        piCell.setVerticalAlignment(Element.ALIGN_TOP);
        insideTable.addCell(piCell);

        para2 = new Paragraph(" Institution Number: ", FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        para2.add(new Phrase(String.format("%s", participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization().getNciInstituteCode()), FontFactory.getFont("Times-Roman", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE)));
        headerCell = new Cell(para2);
        headerCell.setColspan(5);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        insideTable.addCell(headerCell);

        headerCell = new Cell(new Paragraph("ALL ITEMS MUST BE COMPLETED", FontFactory.getFont("Times-Roman", 8, Font.BOLD)));
        headerCell.setColspan(4);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthBottom(0);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthLeft(1);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        insideTable.addCell(headerCell);

        para2 = new Paragraph(" Institution:", FontFactory.getFont("Times-Roman", 11, Font.PLAIN));
        para2.add(new Phrase(String.format("%s", participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization().getDisplayName()), FontFactory.getFont("Times-Roman", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE)));
        headerCell = new Cell(para2);
        headerCell.setColspan(5);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthRight(0);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        insideTable.addCell(headerCell);
        
        Paragraph subtext = new Paragraph("Are data amended? ", FontFactory.getFont("Times-Roman", 8));
        subtext.add(new Phrase("(check one)", FontFactory.getFont("Times-Roman", 8, Font.ITALIC)));
        subtext.setIndentationLeft(-55f);
        Paragraph pleaseCircle = new Paragraph(NEW_LINE + "                            (If data amended, please circle in red when using paper forms)" + NEW_LINE, FontFactory.getFont("Times-Roman", 8, Font.ITALIC));
        pleaseCircle.setAlignment(Element.ALIGN_CENTER);
        subtext.add(pleaseCircle);
        
        headerCell = new Cell(subtext);
        headerCell.setColspan(4);
        headerCell.setRowspan(2);
        headerCell.setBorderWidthTop(0);
        headerCell.setBorderWidthLeft(1);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        insideTable.addCell(headerCell);
        
        subtext = new Paragraph(" ", FontFactory.getFont("Times-Roman", 8));
        headerCell = new Cell(subtext);
        headerCell.setColspan(9);
        headerCell.setRowspan(1);
        headerCell.setBorder(0);
        headerCell.setBorderWidthBottom(1);
        insideTable.addCell(headerCell);
        
        return insideTable;
	}
	
	private void addClinicianReportedSymptomsTableToDocument(Document document, PdfWriter pdfWriter) throws DocumentException, IOException {
	    Table table = new Table(10);
        table.setWidth(100);
        table.setWidths(new int[]{19, 12, 3, 3, 3, 3, 3, 7, 7, 7});
        table.setAlignment(Element.ALIGN_LEFT);
        table.setPadding(1);
        table.setCellsFitPage(true);
        table.setBorder(0);
        
        Cell cell = new Cell(new Paragraph(ADDITIONAL_CTC_ADVERSE_EVENT_TERM, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph(ADDITIONAL_MEDDRA_ADVERSE_EVENT_CODE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.add(new Phrase("(must be completed)", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        Paragraph ctcaeAdverseEvent = new Paragraph(ADDITIONAL_CTC_ADVERSE_EVENT_GRADE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        ctcaeAdverseEvent.add(new Phrase("(highest grade in this reporting period) \n\n", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell = new Cell(ctcaeAdverseEvent);
        cell.setRowspan(2);
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        Paragraph attribution = new Paragraph(ADDITIONAL_CTCAE_ATTRIBUTION_CODE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        attribution.add(new Paragraph(ADDITIONAL_CTCAE_ATTRIBUTION_CODE_PHRASE_ONE, FontFactory.getFont("Times-Roman", 9, Font.ITALIC)));
        //attribution.add(new Paragraph(ADDITIONAL_CTCAE_ATTRIBUTION_CODE_PHRASE_TWO, FontFactory.getFont("Times-Roman", 9, Font.ITALIC)));
        cell = new Cell(attribution);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        attribution = new Paragraph(ADDITIONAL_AE_EXPEDITE_REPORT_PHRASE_ONE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        //attribution.add(new Phrase(ADDITIONAL_AE_EXPEDITE_REPORT_PHRASE_TWO, FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell = new Cell(attribution);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        
        
        attribution = new Paragraph(ADDITINOAL_TABLE_NOTE, FontFactory.getFont("Times-Roman", 10, Font.PLAIN | Font.BOLD));
        cell = new Cell(attribution);
        cell.setColspan(10);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        for(int i = 0; i < 8; i++){
            cell = new Cell(new Paragraph(" "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new Cell(new Paragraph("  "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new Cell(new Paragraph(" "));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
            cell = new Cell(new Paragraph(" "));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
            cell = new Cell(new Paragraph("2", FontFactory.getFont("Times-Roman", 12)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new Cell(new Paragraph("3", FontFactory.getFont("Times-Roman", 12)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new Cell(new Paragraph("4", FontFactory.getFont("Times-Roman", 12)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new Cell(new Paragraph("5 (death)", FontFactory.getFont("Times-Roman", 12)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new Cell(new Paragraph("  "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new Cell(new Paragraph("  "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        
        Paragraph para = new Paragraph("*See section 10.0 of the protocol.\n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        para.add(new Paragraph("**Both hematologic and non-hematologic Adverse Events must be graded on this form as applicable.", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell = new Cell(para);
        cell.setColspan(10);
        cell.setBorder(0);
        table.addCell(cell);
        document.add(table);
	}
	
}
