package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.Organization;
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
import java.sql.Date;
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
 * Created by IntelliJ IDEA.
 * User: Gaurav Gupta
 * Date: 11/22/11
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantLevelWorstSymptomReportPdfView extends AbstractPdfView {
	
	public static final String[] aeNameArray = new String[]{"Dysphagia", "Diarrhea", "Nausea", "Vomiting", "Dyspnea", "Peripheral sensory neuropathy", 
			"Constipation", "Mucositis oral", "Fatigue", "Pain", "Anorexia", "Anxiety", "Depression"};
	public static final String[] aeMeddraCode = new String[]{"10013950", "10012727", "10028813", "10047700", "10013963", "10034620", "10010774", "10028130", "10016256", 
			"10033371", "10002646", "10002855", "10012378"};
	public static final int[] aeGrade = new int[]{5, 5, 3, 5, 5, 5, 5, 5, 3, 3, 5, 5, 5};
	
	private int totalPages = 0;
	private static String REPORT_TITLE = "Alliance for Clinical Trials in Oncology";
	private static String PROTOCOL_NUMBER = "Protocol Number:";
	private static String SOLICITED_AE = "Solicited Adverse Event";
	private static String PATIENT_ID = "Patient ID:";
	private static String INSTITUTION_NO = "Institution (Inst. Number):";
	
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
        
        MyPdfPageEvent pageEvent = new MyPdfPageEvent();
        pageEvent.setTotalPages(totalPages);
        pdfWriter.setPageEvent(pageEvent);
        
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
        HeaderFooter footer = new HeaderFooter(new Phrase(" CONFIDENTIAL", new com.lowagie.text.Font(com.lowagie.text.Font.TIMES_ROMAN, 10, com.lowagie.text.Font.NORMAL)), false);
        document.setFooter(footer);

        // Header table
        Table insideTable = getHeaderForDocument(pdfWriter, study, participant, reportStartDate, reportEndDate);
        Phrase phrase = new Phrase (0);
        phrase.add(insideTable);
        phrase.add(new Phrase("\n"));
        HeaderFooter header = new HeaderFooter(phrase, false);
        header.setBorderWidthBottom(1);
        header.setBorder(0);
        header.setAlignment(Paragraph.ALIGN_CENTER);
        document.setHeader(header); 
        document.open();
        
        Table instructionsTable = getInstructionsForDocument(reportStartDate, reportEndDate);
        document.add(instructionsTable);
        
        // survey solicited symptoms section header
        addSurveySolicitedSymptomsToDocument(pdfWriter, document, results);
        addFeedbackQuestionsToDocument(document, pdfWriter);
        pdfWriter.newPage();
        addClinicianReportedSymptomsTableToDocument(document, pdfWriter);
    }

    private void addSurveySolicitedSymptomsToDocument(PdfWriter pdfWriter, Document document, 
			TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results) throws DocumentException, IOException {

		Table table = getHeaderSectionForSurveySolicitedSymptomsTable(false);
        List<AEDetail> aeDetailList = buildAEListForDisplay(0, 7);
        addSymptomsToTable(table, aeDetailList, results);
        document.add(table);

		table = getHeaderSectionForSurveySolicitedSymptomsTable(true);
        aeDetailList = buildAEListForDisplay(8, 12);
        addSymptomsToTable(table, aeDetailList, results);
        document.add(table);
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
	        	
	            cell = new Cell(new Paragraph(" O "));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            
	            cell = new Cell(new Paragraph("0", FontFactory.getFont("Times-Roman", 12)));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            cell = new Cell(new Paragraph("1", FontFactory.getFont("Times-Roman", 12)));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            cell = new Cell(new Paragraph("2", FontFactory.getFont("Times-Roman", 12)));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            cell = new Cell(new Paragraph("3", FontFactory.getFont("Times-Roman", 12)));
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	            if(aed.getAEGrade() == 3){
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	            }
	            if(aed.getAEGrade() == 4){
	                cell = new Cell(new Paragraph("4", FontFactory.getFont("Times-Roman", 12)));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph(" "));
	                cell.setBackgroundColor(Color.LIGHT_GRAY);
	                table.addCell(cell);
	            }
	            if(aed.getAEGrade() == 5){
	                cell = new Cell(new Paragraph("4", FontFactory.getFont("Times-Roman", 12)));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	                cell = new Cell(new Paragraph("5 (death)", FontFactory.getFont("Times-Roman", 12)));
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                cell.setVerticalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);
	            }
	            
	            cell = new Cell(new Paragraph(" "));
	            table.addCell(cell);
	        }
	}

	private Table getHeaderSectionForSurveySolicitedSymptomsTable(boolean contdIndicator) throws DocumentException {
        // Data Table
        Table table = new Table(13);
        table.setWidth(100);
        table.setWidths(new int[]{19, 8, 11, 11, 11, 10, 3, 3, 3, 3, 3, 7, 8});
        table.setAlignment(Element.ALIGN_CENTER);
        table.setAlignment(Element.ALIGN_MIDDLE);
        table.setPadding(1);
        table.setCellsFitPage(true);
        table.setBorder(0);
        
        Paragraph p = new Paragraph("", FontFactory.getFont("Times-Roman", 13, Font.BOLD));
        Cell headerCell = new Cell(p);
        headerCell.setBorder(0);
        headerCell.setColspan(13);
        headerCell.setRowspan(2);
        table.addCell(headerCell);
        
        Cell cell = new Cell(new Paragraph("Adverse Event Text Name\n (CTCAE v 4.0)", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph("MedDRA Adverse Event Code\n (v. 12.0)", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        p = new Paragraph("\n Patient Self-Reporting Adverse Event Ratings \n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        p.add(new Phrase("\n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell = new Cell(p);
        cell.setRowspan(1);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        Paragraph attribution = new Paragraph("Check the circle next to event(s) not evaluated at this visit", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        cell = new Cell(attribution);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        Paragraph aeg = new Paragraph("Adverse Event Grade \n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        aeg.add(new Phrase("(highest grade in this reporting period) \n\n", FontFactory.getFont("Times-Roman", 8, Font.ITALIC)));
        aeg.add(new Paragraph("INCLUDE GRADE 0's", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell = new Cell(aeg);
        cell.setColspan(6);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        attribution = new Paragraph("Has an adverse event expedited report been submitted?*\n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        attribution.add(new Phrase("Enter a # below: \n 1=Yes\n 2=No", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell = new Cell(attribution);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph("Severity", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(1);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Frequency", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(1);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new Cell(new Paragraph("Interference with Activities of Daily Life", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setRowspan(1);
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        return table;
	}

	private void addClinicianReportedSymptomsTableToDocument(Document document, PdfWriter pdfWriter) throws DocumentException, IOException {
		
        Table table = new Table(9);
        table.setWidth(84);
        table.setWidths(new int[]{22, 20, 3, 3, 3, 3, 3, 7, 20});
        table.setAlignment(Element.ALIGN_LEFT);
        table.setPadding(1);
        table.setCellsFitPage(true);
        table.setBorder(0);
        
        Cell cell = new Cell(new Paragraph("(Other) Adverse Event Text Name\n Not Listed\n (CTCAE v 4.0)", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        cell = new Cell(new Paragraph("MedDRA Adverse Event Code\n (v. 12.0) \n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell.add(new Phrase("(must be completed)", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        Paragraph attribution = new Paragraph("Adverse Event Grade\n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        attribution.add(new Phrase("(highest grade in this reporting period) \n\n", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell = new Cell(attribution);
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        attribution = new Paragraph("Has an adverse event expedited report been submitted?*", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        attribution.add(new Phrase("\nEnter a # below: 1=Yes  2=No", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        cell = new Cell(attribution);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        
        attribution = new Paragraph("Record all adverse events beyond those solicited. Record all grade 3, 4 and 5 regardless of attribution.", FontFactory.getFont("Times-Roman", 10, Font.PLAIN | Font.BOLD));
        cell = new Cell(attribution);
        cell.setColspan(9);
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
            cell = new Cell(new Paragraph(" "));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
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
        }
        Paragraph para = new Paragraph("*See section 10.0 of the protocol.\n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        para.add(new Paragraph("**Both hematologic and non-hematologic Adverse Events must be graded on this form as applicable.", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        cell = new Cell(para);
        cell.setColspan(9);
        cell.setBorder(0);
        table.addCell(cell);
        
        document.add(table);
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
        
        document.newPage();
        document.add(new Paragraph(" ", FontFactory.getFont("Times-Roman", 6)));
		Paragraph tQuestion = new Paragraph("Were any of the assigned CTCAE grades in the above form different than they would " +
        		"have been in the absence of access to the patient's self-reported AE responses?", FontFactory.getFont("Times-Roman", 10));
		tQuestion.add(new Phrase(" (check one)", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        document.add(tQuestion);
        document.add(new Paragraph("    O Yes", FontFactory.getFont("Times-Roman", 10)));
        document.add(new Paragraph("    O No", FontFactory.getFont("Times-Roman", 10)));
        
        document.add(new Paragraph("  ", FontFactory.getFont("Times-Roman", 8)));
        Paragraph blurb = new Paragraph("Were", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        blurb.add(new Phrase(" (other) ", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        blurb.add(new Phrase("adverse events assessed during this report period?", FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        document.add(blurb);
        
        blurb = new Paragraph("O Yes, and reportable adverse events occurred \n", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        blurb.setIndentationLeft(20f);
        document.add(blurb);
        
        blurb = new Paragraph("O Yes, but no reportable adverse events occurred", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        blurb.add(new Phrase(" (end form) ", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        blurb.setIndentationLeft(20f);
        document.add(blurb);
        
        blurb = new Paragraph("O No", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        blurb.add(new Phrase(" (end form) \n", FontFactory.getFont("Times-Roman", 10, Font.ITALIC)));
        blurb.setIndentationLeft(20f);
        document.add(blurb);
        
	}


	private Table getInstructionsForDocument(String reportStartDate, String reportEndDate) throws DocumentException {
        Table instructionsTable = new Table(1);
        instructionsTable.setWidth(100);
        instructionsTable.setBorderWidth(0);

        Paragraph instructionalParagraph = new Paragraph("\nInstructions: ", FontFactory.getFont("Times-Roman", 10, Font.BOLD));
        instructionalParagraph.add(new Phrase("This form should be completed by the CRA per the Test Schedule (Section 4.0) using patient's medical records, starting from the first day since the prior reporting period if post-baseline. " +
        		"When completing this form, the patient's self-reported adverse event ratings (shown in the table below for the reporting period) should be used as a reference.\n \n" , FontFactory.getFont("Times-Roman", 10, Font.PLAIN)));
        Paragraph datePara = new Paragraph("Reporting Period End Date (date on which the clinician evaluated patient's adverse events): (dd MMM yyyy) ", FontFactory.getFont("Times-Roman", 10, Font.PLAIN));
        datePara.add(new Phrase(convertDateToReqdFormat(reportEndDate), FontFactory.getFont("Times-Roman", 10, Font.PLAIN | com.lowagie.text.Font.UNDERLINE)));
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
		Table insideTable = new Table(9);
		insideTable.setAlignment(Table.ALIGN_LEFT);
		insideTable.setBorder(Rectangle.NO_BORDER);
		insideTable.setWidth(100);
		insideTable.setWidths(new int[]{33, 3, 5, 11, 3, 24, 1, 14, 6});
		
		// Line 1:
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
        
        para1 = new Paragraph(REPORT_TITLE, FontFactory.getFont("Arial", 15, Font.BOLD));
		headerCell = new Cell(para1);
        headerCell.setColspan(5);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        //Line 2:
        para1 = new Paragraph(SOLICITED_AE, FontFactory.getFont("Arial", 14, Font.BOLD));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(2);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(2);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        para1 = new Paragraph(PROTOCOL_NUMBER, FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        headerCell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN)));
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        para1 = new Paragraph(study.getAssignedIdentifier(), FontFactory.getFont("Arial", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE));
		headerCell = new Cell(para1);
        headerCell.setColspan(2);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        //Line 3:
        headerCell = new Cell(new Paragraph(""));
        headerCell.setColspan(1);
        headerCell.setRowspan(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(3);
        headerCell.setRowspan(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        para1 = new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(5);
        headerCell.setRowspan(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        //Line 4:
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
        para1 = new Paragraph(PATIENT_ID, FontFactory.getFont("Arial", 11, Font.PLAIN));
		headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        headerCell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN)));
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        String studyParticipantIdentifier = null;
        if(participant.getStudyParticipantAssignments().size() > 0){
        	studyParticipantIdentifier = participant.getStudyParticipantAssignments().get(0).getStudyParticipantIdentifier();
        }
        para1 = new Paragraph(studyParticipantIdentifier, FontFactory.getFont("Arial", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE));
		headerCell = new Cell(para1);
        headerCell.setColspan(2);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        //Line 5:
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
        para1 = new Paragraph(INSTITUTION_NO, FontFactory.getFont("Arial", 11, Font.PLAIN));
        headerCell = new Cell(para1);
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        headerCell = new Cell(new Paragraph("", FontFactory.getFont("Arial", 11, Font.PLAIN)));
        headerCell.setColspan(1);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);
        
        Organization org = participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization();
        String institution = (org.getNciInstituteCode() == null ? "" : org.getName() +" ( " + org.getNciInstituteCode() + " )");
        
        para1 = new Paragraph(String.format("%s", institution), FontFactory.getFont("Arial", 11, Font.PLAIN | com.lowagie.text.Font.UNDERLINE));
        headerCell = new Cell(para1);
        headerCell.setColspan(2);
        headerCell.setRowspan(2);
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
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
        headerCell.setColspan(5);
        headerCell.setRowspan(4);
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerCell.setNoWrap(false);
        insideTable.addCell(headerCell);
        
        headerCell = new Cell(new Paragraph("\n"));
        headerCell.setColspan(9);
        headerCell.setRowspan(2);
        headerCell.setBorder(Rectangle.NO_BORDER);
        insideTable.addCell(headerCell);

        return insideTable;
	}
	
}
