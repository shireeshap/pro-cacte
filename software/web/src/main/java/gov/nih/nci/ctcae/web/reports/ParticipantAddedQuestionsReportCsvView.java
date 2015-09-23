package gov.nih.nci.ctcae.web.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

import au.com.bytecode.opencsv.CSVWriter;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.Study;


/**
 * @author Amey
 * ParticipantAddedQuestionsReportCsvView class
 * Generates participant added symptom report as a csv file
 */
public class ParticipantAddedQuestionsReportCsvView extends AbstractView {
	
	List<ParticipantAddedSymptomVerbatimWrapper> verbatimWrappers;
	private static final String EMPTY_COLUMN = "";
	private static String PARTICIPANT_ADDED_SYMPTOMS_REPORT = "ParticipantAddedSymptomsReport";

    CSVWriter csvWriter = null;
    PrintWriter printWriter;
    String[] emptyRow;
    private static int maxRowSize;
    File participantAddedSymptomsReport;
    ZipOutputStream zipOutputStream;


    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	verbatimWrappers = (List<ParticipantAddedSymptomVerbatimWrapper>) request.getSession().getAttribute(ParticipantAddedQuestionsCsvController.RESULTANT_VERBATIM_WRAPPER);
    	
        //parseRequestAndRetrieveReportData(request);
        Study study = (Study) request.getSession().getAttribute("selectedStudy");

        zipOutputStream = new ZipOutputStream(response.getOutputStream());
        maxRowSize = 5;
        
        
        emptyRow = new String[maxRowSize];

        try {
        	 response.setContentType("application/zip");
             response.addHeader("Content-Disposition", "attachment; filename=\"ProCtcaeAddedSymptomReports.zip\"");
             response.addHeader("Content-Transfer-Encoding", "binary");

            generateParticipantAddedSymptomsReport(request, study);
            csvWriter.flush();
            printWriter.flush();
            byte[] fileContent = fetchBytesFromFile(participantAddedSymptomsReport);
            addToZip(zipOutputStream, fileContent, PARTICIPANT_ADDED_SYMPTOMS_REPORT + ".csv");


            zipOutputStream.flush();

        } catch (Exception e) {
            logger.error("Error in generating Participant Added Symptoms Report. ", e);
        } finally {

            csvWriter.close();
            printWriter.close();
            participantAddedSymptomsReport.delete();
            zipOutputStream.close();
        }
    }


    public String createFileName(String reportType, String studyShortTitle) {
        return reportType + "_" + studyShortTitle + ".csv";
    }


    public void generateParticipantAddedSymptomsReport(HttpServletRequest request, Study study) throws IOException {
        participantAddedSymptomsReport = new File(createFileName(PARTICIPANT_ADDED_SYMPTOMS_REPORT, study.getShortTitle()));
        printWriter = new PrintWriter(participantAddedSymptomsReport);

        csvWriter = new CSVWriter(printWriter);
        createReportHeader(study, request, csvWriter);


        //Blank row
        csvWriter.writeNext(emptyRow);
        
        /*//Legend
        buildProReportLegend();*/

        //Blank row
        csvWriter.writeNext(emptyRow);
        //Create main table header
        createTableHeaders(csvWriter);
        
        for(ParticipantAddedSymptomVerbatimWrapper verbatimWrapper: verbatimWrappers) {
        	List<String> verbatimRow = new ArrayList<String>();
        	
        	if(StringUtils.isNotBlank(verbatimWrapper.getStudyParticipantIdentifier())) {
        		verbatimRow.add(verbatimWrapper.getStudyParticipantIdentifier());
        	} else {
        		verbatimRow.add(EMPTY_COLUMN);
        	}
        	
        	if(StringUtils.isNotBlank(verbatimWrapper.getVerbatim())) {
        		verbatimRow.add(verbatimWrapper.getVerbatim());
        	} else {
        		verbatimRow.add(EMPTY_COLUMN);
        	}
        	
        	if(verbatimWrapper.getLowLevelTerm() != null) {
        		verbatimRow.add(verbatimWrapper.getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH));
        	} else {
        		verbatimRow.add(EMPTY_COLUMN);
        	}
        	
        	if(verbatimWrapper.getProCtcTerm() != null) {
        		verbatimRow.add(verbatimWrapper.getProCtcTerm().getTermEnglish(SupportedLanguageEnum.ENGLISH));
        	} else {
        		verbatimRow.add(EMPTY_COLUMN);
        	}
        	
        	try {
        		csvWriter.writeNext(verbatimRow.toArray(new String[verbatimRow.size()]));
			} catch (Exception e) {
				 e.printStackTrace();
                 logger.error("Error in writing verbatim row: " + e.getStackTrace());
            }
        	
        }


    }


    public byte[] fetchBytesFromFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] fileContent = new byte[(int) file.length()];
        inputStream.read(fileContent);

        return fileContent;
    }


    public void addToZip(ZipOutputStream zipOutputStream, byte[] fileContent, String fileName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(fileContent);
        zipOutputStream.closeEntry();
    }


    private void createReportHeader(Study study, HttpServletRequest request, CSVWriter writer) {
        List<String> reportInformation = new ArrayList<String>();
        reportInformation.add(0, "Study");
        reportInformation.add(1, study.getDisplayName());
        writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
        reportInformation = new ArrayList<String>();
        reportInformation.add(0, "Report run date");
        reportInformation.add(1, DateUtils.format(new Date()));
        writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));

        String organizationName = (String) request.getSession().getAttribute("organizationName");
        if (!StringUtils.isEmpty(organizationName)) {
            reportInformation = new ArrayList<String>();
            reportInformation.add(0, "Study Site");
            reportInformation.add(1, organizationName);
            writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
        }

        String crfTitle = (String) request.getSession().getAttribute("crfTitle");
        if (!StringUtils.isEmpty(crfTitle)) {
            reportInformation = new ArrayList<String>();
            reportInformation.add(0, "Form");
            reportInformation.add(1, crfTitle);
            writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
        }
    }


    private void createTableHeaders(CSVWriter writer) {
        List<String> proRow = new ArrayList<String>();
        int col = 0;
        proRow.add(col++, "Participant ID");
        proRow.add(col++, "Verbatim");
        proRow.add(col++, "Meddra Term");
        proRow.add(col++, "Pro-ctc Term");
        
        writer.writeNext((String[]) proRow.toArray(new String[proRow.size()]));
    }

}

