package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.Study;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * User: Harsh
 * Date: Apr 24, 2009
 * Time: 1:13:34 PM
 */
public class StudyLevelFullReportCsvView extends AbstractView {

    Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> results;
    Map<String, LinkedHashMap<String, Map<String, List<Date>>>> crfDateMap;

    Map<String, LinkedHashMap<String, List<String>>> crfModeMap;
    Map<String, LinkedHashMap<String, List<CrfStatus>>> crfStatusMap;
    Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping;
    Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping;
    List<String> proCtcTermHeaders;
    List<String> meddraTermHeaders;
    Map<String, String> participantInfoMap;
    Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> eq5dResults;


    Map<String, LinkedHashMap<String, Map<String, List<Date>>>> eq5dCrfDateMap;
    Map<String, LinkedHashMap<String, List<String>>> eq5dCrfModeMap;
    Map<String, LinkedHashMap<String, List<CrfStatus>>> eq5dCrfStatusMap;
    Map<String, LinkedHashMap<String, List<String>>> eq5dCrfHealthScoreMap;
    Map<String, Map<ProCtcQuestionType, String>> eq5dQuestionMapping;
    Map<String, Map<ProCtcQuestionType, String>> eq5dMeddraQuestionMapping;
    List<String> eq5dTermHeaders;
    List<String> eq5dMeddraHeaders;
    Map<String, String> eq5dParticipantInfoMap;

    CSVWriter proCsvWriter = null;
    PrintWriter proWriter;
    CSVWriter eq5dCsvWriter = null;
    PrintWriter eq5dWriter;
    String[] emptyRow;
    private static int maxRowSize;
    File overallStudyReport;
    File eq5DReport;
    ZipOutputStream zipOutputStream;
    boolean hasEq5dData = false;

    private static String NOT_AVAILABLE = "Not Available";
    private static String DEFAULT_POSITION = "";
    private static String FORCE_SKIP = "Forced skip";
    private static String MANUAL_SKIP = "Not answered";
    private static String FREQUENCY = "FRQ";
    private static String INTERFERENCE = "INT";
    private static String SEVERITY = "SEV";
    private static String PRESENT = "PRES";
    private static String AMOUNT = "AMT";
    private static String OVERALL_STUDY_REPORT = "OverallStudyReport";
    private static String EQ_5D = "EQ-5D";

    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        parseRequestAndRetrieveReportData(request);
        Study study = (Study) request.getSession().getAttribute("study");

        zipOutputStream = new ZipOutputStream(response.getOutputStream());
        maxRowSize = (7 + proCtcTermHeaders.size() + meddraTermHeaders.size());
        emptyRow = new String[maxRowSize];

        try {
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=\"ProCtcaeReports.zip\"");
            response.addHeader("Content-Transfer-Encoding", "binary");

            generateProCtcaeReport(request, study);
            proCsvWriter.flush();
            proWriter.flush();
            byte[] fileContent = fetchBytesFromFile(overallStudyReport);
            addToZip(zipOutputStream, fileContent, OVERALL_STUDY_REPORT + ".csv");

			if(hasEq5dData) {
				generateEq5dReport(request, study);
		        eq5dWriter.flush();
		        eq5dCsvWriter.flush();
		        fileContent = fetchBytesFromFile(eq5DReport);
		        addToZip(zipOutputStream, fileContent, EQ_5D + ".csv");
			}

            zipOutputStream.flush();

        } catch (Exception e) {
            logger.error("Error in generating Overall Study Report. ", e);
        } finally {

            proCsvWriter.close();
            proWriter.close();
            overallStudyReport.delete();
            if (hasEq5dData) {
                eq5dCsvWriter.close();
                eq5dWriter.close();
                eq5DReport.delete();
            }
            zipOutputStream.close();
        }
    }


    @SuppressWarnings("unchecked")
    public void parseRequestAndRetrieveReportData(HttpServletRequest request) {
        results = (Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>>) request.getSession().getAttribute("sessionResultsMap");
        crfDateMap     = (Map<String, LinkedHashMap<String, Map<String, List<Date>>>>) request.getSession().getAttribute("sessionCRFDatesMap");
        crfModeMap = (Map<String, LinkedHashMap<String, List<String>>>) request.getSession().getAttribute("sessionCRFModeMap");
        crfStatusMap = (Map<String, LinkedHashMap<String, List<CrfStatus>>>) request.getSession().getAttribute("sessionCRFStatusMap");
        proCtcQuestionMapping = (Map<String, Map<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionProCtcQuestionMapping");
        meddraQuestionMapping = (Map<String, Map<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionMeddraQuestionMapping");
        proCtcTermHeaders = (List<String>) request.getSession().getAttribute("sessionProCtcTermHeaders");
        meddraTermHeaders = (List<String>) request.getSession().getAttribute("sessionMeddraTermHeaders");
        participantInfoMap = (Map<String, String>) request.getSession().getAttribute("participantInfoMap");

        eq5dResults = (Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>>) request.getSession().getAttribute("sessionEq5dResultsMap");
        eq5dCrfDateMap = (Map<String, LinkedHashMap<String, Map<String, List<Date>>>>) request.getSession().getAttribute("sessionEq5dCRFDatesMap");
        eq5dCrfModeMap = (Map<String, LinkedHashMap<String, List<String>>>) request.getSession().getAttribute("sessionEq5dCRFModeMap");
        eq5dCrfStatusMap = (Map<String, LinkedHashMap<String, List<CrfStatus>>>) request.getSession().getAttribute("sessionEq5dCRFStatusMap");
        eq5dCrfHealthScoreMap = (Map<String, LinkedHashMap<String, List<String>>>) request.getSession().getAttribute("sessionEq5dHealthScoreMap");
        eq5dQuestionMapping = (Map<String, Map<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionEq5dProCtcQuestionMapping");
        eq5dTermHeaders = (List<String>) request.getSession().getAttribute("sessionEq5dProCtcTermHeaders");
        eq5dMeddraHeaders = new ArrayList<String>();
        eq5dMeddraQuestionMapping = new HashMap<String, Map<ProCtcQuestionType, String>>();
        eq5dParticipantInfoMap = (Map<String, String>) request.getSession().getAttribute("eq5dparticipantInfoMap");
        if (!eq5dResults.isEmpty()) {
            hasEq5dData = true;
        }
    }

    public void generateEq5dReport(HttpServletRequest request, Study study) throws IOException {
        eq5DReport = new File(createFileName(EQ_5D, study.getShortTitle()));
        eq5dWriter = new PrintWriter(eq5DReport);

        eq5dCsvWriter = new CSVWriter(eq5dWriter);
        createReportHeader(study, request, eq5dCsvWriter);

        //Blank row
        eq5dCsvWriter.writeNext(emptyRow);
        //Legend
        buildEq5dReportLegend();

        //Blank row
        eq5dCsvWriter.writeNext(emptyRow);

        //Create main table header
        createTableHeaders(eq5dTermHeaders, eq5dMeddraHeaders, true, eq5dCsvWriter);

        for (String organization : eq5dResults.keySet()) {
            Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>> crfMap = eq5dResults.get(organization);
            for (String crf : crfMap.keySet()) {

                Map<String, Map<String, LinkedHashMap<String, List<String>>>> participantMap = crfMap.get(crf);
                for (String participant : participantMap.keySet()) {
                    List<String> eq5dRow = new ArrayList<String>();
                    List<List<String>> eq5dRowSet = new ArrayList<List<String>>();
                    try {
                        Map<String, LinkedHashMap<String, List<String>>> symptomMap = (Map<String, LinkedHashMap<String, List<String>>>) participantMap.get(participant);
                        Map<String, List<Date>> dates = null;
                        List<String> appModes = null;
                        List<CrfStatus> statusList = null;
                        List<String> healthScoreList = null;

	                    dates = getAllAdministeredDatesForCurrentSymptom(eq5dCrfDateMap.get(crf), participant);
                        appModes = getAllAppModesForCurrentSymptom(eq5dCrfModeMap.get(crf), participant);
                        statusList = getAllCrfStatusForCurrentSymptom(eq5dCrfStatusMap.get(crf), participant);
                        healthScoreList = getAllCrfHealthScoresForCurrentSymptom(eq5dCrfHealthScoreMap.get(crf), participant);

                        createCurrentRowPrefix(eq5dRowSet, dates, appModes, statusList, eq5dParticipantInfoMap.get(participant), study.getShortTitle(), organization,
                                crf, eq5dTermHeaders, eq5dMeddraHeaders, true);

                        writeResponsesToCSV(symptomMap, eq5dRowSet, eq5dQuestionMapping, eq5dMeddraQuestionMapping, healthScoreList, true);

                        for (int i = 0; i < eq5dRowSet.size(); i++) {
                            eq5dRow = eq5dRowSet.get(i);
                            eq5dCsvWriter.writeNext(eq5dRow.toArray(new String[eq5dRow.size()]));
                        }

                    } catch (Exception e) {
                        logger.error("Error in writing EQ-5D responses: " , e);
                    }
                }
            }
        }
    }

    public String createFileName(String reportType, String studyShortTitle) {
        return reportType + "_" + studyShortTitle + ".csv";
    }


    public void generateProCtcaeReport(HttpServletRequest request, Study study) throws IOException {
        overallStudyReport = new File(createFileName(OVERALL_STUDY_REPORT, study.getShortTitle()));
        proWriter = new PrintWriter(overallStudyReport);

        proCsvWriter = new CSVWriter(proWriter);
        createReportHeader(study, request, proCsvWriter);


        //Blank row
        proCsvWriter.writeNext(emptyRow);
        //Legend
        buildProReportLegend();

        //Blank row
        proCsvWriter.writeNext(emptyRow);
        //Create main table header
        createTableHeaders(proCtcTermHeaders, meddraTermHeaders, false, proCsvWriter);


        for (String organization : results.keySet()) {
            Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>> crfMap = results.get(organization);
            for (String crf : crfMap.keySet()) {

                Map<String, Map<String, LinkedHashMap<String, List<String>>>> participantMap = crfMap.get(crf);
                for (String participant : participantMap.keySet()) {
                    List<String> proRow = new ArrayList<String>();
                    List<List<String>> proRowSet = new ArrayList<List<String>>();
                    try {
                        Map<String, LinkedHashMap<String, List<String>>> symptomMap = (Map<String, LinkedHashMap<String, List<String>>>) participantMap.get(participant);
                        Map<String, List<Date>> dates = null;
                        List<String> appModes = null;
                        List<CrfStatus> statusList = null;

                        dates = getAllAdministeredDatesForCurrentSymptom(crfDateMap.get(crf), participant);


                        appModes = getAllAppModesForCurrentSymptom(crfModeMap.get(crf), participant);
                        statusList = getAllCrfStatusForCurrentSymptom(crfStatusMap.get(crf), participant);

                        createCurrentRowPrefix(proRowSet, dates, appModes, statusList, participantInfoMap.get(participant), study.getShortTitle(), organization,
                                crf, proCtcTermHeaders, meddraTermHeaders, false);

                        writeResponsesToCSV(symptomMap, proRowSet, proCtcQuestionMapping, meddraQuestionMapping, null, false);

                        for (int i = 0; i < proRowSet.size(); i++) {
                            proRow = proRowSet.get(i);
                            proCsvWriter.writeNext(proRow.toArray(new String[proRow.size()]));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.debug("Error in writing ProCtcae responses: " + e.getStackTrace());
                    }
                }
            }
        }
    }


    private void writeResponsesToCSV(Map<String, LinkedHashMap<String, List<String>>> symptomMap, List<List<String>> rowSet, Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping,
                                     Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping, List<String> healthScoreList, boolean isEq5d) {

        int startIndex = 10;
        int cellNum = 10;
        List<String> proRow = new ArrayList<String>();
        for (String termEnglish : symptomMap.keySet()) {
            LinkedHashMap<String, List<String>> questionMap = symptomMap.get(termEnglish);
            for (String proCtcQuestionType : questionMap.keySet()) {
                List<String> valuesList = questionMap.get(proCtcQuestionType);
                int index = 0;
                for (String validValue : valuesList) {
                    try {
                        proRow = getRow(rowSet, index);
                    } catch (Exception e) {
                        e.getStackTrace();
                    }

                    String pos;
                    int tempPos;
                    pos = getCellNumberValidValue(proCtcQuestionMapping, meddraQuestionMapping, termEnglish, ProCtcQuestionType.getByCode(proCtcQuestionType));
                     if(!pos.equals(DEFAULT_POSITION)){
                         tempPos = Integer.valueOf(pos) + startIndex;
                    	 cellNum = tempPos;
                    	 proRow.set(cellNum, validValue);
                     } else {
                    	 logger.debug("No column found for Term: " + termEnglish + " and question type: " + proCtcQuestionType);
                    	 continue;
                     }
                    index++;
                }
                cellNum++;
            }
        }

        // Add the health score column for EQ5D report
        if (isEq5d) {
            List<String> row = new ArrayList<String>();
            for (int i = 0; i < rowSet.size(); i++) {
                row = getRow(rowSet, i);
                row.set(15, healthScoreList.get(i));
            }
        }

    }

    private Integer createCurrentRowPrefix(List<List<String>> rowSet, Map<String, List<Date>> dates, List<String> appModes, List<CrfStatus> statusList, String participantIdentifier,
                                           String studyShortTitle, String organization, String crf, List<String> proCtcTermHeaders, List<String> meddraTermHeaders,
                                           boolean isEq5d) {
        int rownum = 0;

        if (dates != null && appModes != null) {
            int index = 0;

            List<Date> firstResponseDates = dates.get("firstResponseDates");
            List<Date> scheduledStartDates = dates.get("scheduledStartDates");
            List<Date> scheduledDueDates = dates.get("scheduledDueDates");
            List<Date> scheduledCompletionDates = dates.get("scheduledCompletionDates");

            for (; index < firstResponseDates.size(); ) {
                int column = 4;
                Date startDate = scheduledStartDates.get(index);
                Date dueDate = scheduledDueDates.get(index);

                Date firstResponseDate = firstResponseDates.get(index);
                Date completionDate = scheduledCompletionDates.get(index);
                List<String> row = createRow(rowSet, rownum++);

                createCurrentRowSubPrefix(row, participantIdentifier, studyShortTitle, organization, crf);
                if (startDate!= null) {
                    row.add(column++, DateUtils.format(startDate));
                } else {
                    row.add(column++, NOT_AVAILABLE);
                }

                if (dueDate  != null) {
                    row.add(column++, DateUtils.format(dueDate ));
                } else {
                    row.add(column++, NOT_AVAILABLE);
                }

                if (firstResponseDate != null) {
                    row.add(column++, DateUtils.format(firstResponseDate));
                } else {
                    row.add(column++, NOT_AVAILABLE);
                }

                if (completionDate  != null) {
                    row.add(column++, DateUtils.format(completionDate ));
                } else {
                    row.add(column++, NOT_AVAILABLE);
                }

                if (appModes.get(index) != null) {
                    row.add(column++, appModes.get(index));
                } else {
                    row.add(column++, NOT_AVAILABLE);
                }
                row.add(column++, statusList.get(index).getDisplayName());

                if (!isEq5d) {
                    markDefaultNotAdministered(row, column++, (proCtcTermHeaders.size() + meddraTermHeaders.size()));
                } else {
                    markDefaultNotAdministered(row, column++, (proCtcTermHeaders.size() + meddraTermHeaders.size() + 1));
                }
                index++;
            }
        }
        return rownum;
    }

    Map<String, List<Date>> getAllAdministeredDatesForCurrentSymptom(LinkedHashMap<String, Map<String, List<Date>>> datesMap, String participant) {

        Map<String, List<Date>> datesForParticant = null;
        for (String participantD : datesMap.keySet()) {
            if (participant.equals(participantD)) {
                datesForParticant = datesMap.get(participant);
                break;
            }
        }
        return datesForParticant;
    }

    List<String> getAllAppModesForCurrentSymptom(LinkedHashMap<String, List<String>> modesMap, String participant) {
        List<String> appModes = null;
        for (String participantM : modesMap.keySet()) {
            if (participant.equals(participantM)) {
                appModes = modesMap.get(participant);
                break;
            }
        }
        return appModes;
    }

    List<CrfStatus> getAllCrfStatusForCurrentSymptom(LinkedHashMap<String, List<CrfStatus>> statusMap, String participant) {
        List<CrfStatus> statusList = null;

        for (String participantS : statusMap.keySet()) {
            if (participant.equals(participantS)) {
                statusList = statusMap.get(participant);
                break;
            }
        }
        return statusList;
    }

    List<String> getAllCrfHealthScoresForCurrentSymptom(LinkedHashMap<String, List<String>> VasMap, String participant) {
        List<String> healthScoreList = null;

        for (String participantS : VasMap.keySet()) {
            if (participant.equals(participantS)) {
                healthScoreList = VasMap.get(participant);
                break;
            }
        }
        return healthScoreList;
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

    private void buildEq5dReportLegend() {
        int col = 0;
        List<String> eq5dRow = new ArrayList<String>();
        eq5dRow = new ArrayList<String>();
        eq5dRow.add(col++, "Legend:");
        for (int i = 0; i <= 4; i++) {
            eq5dRow.add((col + i), String.valueOf(i + 1));
        }
        eq5dRow.add(6, "-55");
        eq5dCsvWriter.writeNext((String[]) eq5dRow.toArray(new String[eq5dRow.size()]));

        col = 0;
        eq5dRow.clear();
        eq5dRow.add(col++, "");
        eq5dRow.add(col++, "None");
        eq5dRow.add(col++, "Slight");
        eq5dRow.add(col++, "Moderate");
        eq5dRow.add(col++, "Severe");
        eq5dRow.add(col++, "Extreme");
        eq5dRow.add(col++, "Not answered");
        eq5dCsvWriter.writeNext((String[]) eq5dRow.toArray(new String[eq5dRow.size()]));
    }

    private void buildProReportLegend() {
        int col = 0;
        List<String> proRow = new ArrayList<String>();
        proRow.add(col++, "Legend:");
        proRow.add(col++, "");

        for (int i = 0; i <= 4; i++) {
            proRow.add((col + i), String.valueOf(i));
        }
        col = 7;
        proRow.add(col++, "-77");
        proRow.add(col++, "-88");
        proRow.add(col++, "-66");
        proRow.add(col++, "-2000");
        proRow.add(col++, "-99");
        proRow.add(col++, "-55");
        proCsvWriter.writeNext((String[]) proRow.toArray(new String[proRow.size()]));

        proRow.clear();
        col = 0;
        proRow.add(col++, "");
        proRow.add(col++, ProCtcQuestionType.FREQUENCY.getDisplayName() + " (" + FREQUENCY + ")");

        int j = 0;
        for (String value : ProCtcQuestionType.FREQUENCY.getValidValues()) {
            proRow.add((col + j), value);
            j++;
        }
        col = 7;
        proRow.add(col++, "Prefer not to answer");
        proRow.add(col++, "Not applicable");
        proRow.add(col++, "Not sexually active");
        proRow.add(col++, "Not asked");
        proRow.add(col++, FORCE_SKIP);
        proRow.add(col++, MANUAL_SKIP);
        proCsvWriter.writeNext((String[]) proRow.toArray(new String[proRow.size()]));

        proRow.clear();
        col = 0;
        proRow.add(col++, "");
        proRow.add(col++, ProCtcQuestionType.SEVERITY.getDisplayName() + " (" + SEVERITY + ")");
        int k = 0;
        for (String value : ProCtcQuestionType.SEVERITY.getValidValues()) {
            proRow.add((col + k), value);
            k++;
        }
        col = 7;
        proRow.add(col++, "Prefer not to answer");
        proRow.add(col++, "Not applicable");
        proRow.add(col++, "Not sexually active");
        proRow.add(col++, "Not asked");
        proRow.add(col++, FORCE_SKIP);
        proRow.add(col++, MANUAL_SKIP);
        proCsvWriter.writeNext((String[]) proRow.toArray(new String[proRow.size()]));

        proRow.clear();
        col = 0;
        proRow.add(col++, "");
        proRow.add(col++, ProCtcQuestionType.INTERFERENCE.getDisplayName() + " (" + INTERFERENCE + ")");
        int l = 0;
        for (String value : ProCtcQuestionType.INTERFERENCE.getValidValues()) {
            proRow.add((col + l), value);
            l++;
        }
        col = 7;
        proRow.add(col++, "Prefer not to answer");
        proRow.add(col++, "Not applicable");
        proRow.add(col++, "Not sexually active");
        proRow.add(col++, "Not asked");
        proRow.add(col++, FORCE_SKIP);
        proRow.add(col++, MANUAL_SKIP);
        proCsvWriter.writeNext((String[]) proRow.toArray(new String[proRow.size()]));

        proRow.clear();
        col = 0;
        proRow.add(col++, "");
        proRow.add(col++, ProCtcQuestionType.PRESENT.getDisplayName() + " (" + PRESENT + ")");
        proRow.add(col++, "No");
        proRow.add(col++, "Yes");
        for (int n = 0; n <= 2; n++) {
            proRow.add((col + n), "");
        }
        col = 7;
        proRow.add(col++, "Prefer not to answer");
        proRow.add(col++, "Not applicable");
        proRow.add(col++, "Not sexually active");
        proRow.add(col++, "Not asked");
        proRow.add(col++, FORCE_SKIP);
        proRow.add(col++, MANUAL_SKIP);
        proCsvWriter.writeNext((String[]) proRow.toArray(new String[proRow.size()]));


        proRow.clear();
        col = 0;
        proRow.add(col++, "");
        proRow.add(col++, ProCtcQuestionType.AMOUNT.getDisplayName() + " (" + AMOUNT + ")");
        int p = 0;
        for (String value : ProCtcQuestionType.AMOUNT.getValidValues()) {
            proRow.add((col + p), value);
            p++;
        }
        col = 7;
        proRow.add(col++, "Prefer not to answer");
        proRow.add(col++, "Not applicable");
        proRow.add(col++, "Not sexually active");
        proRow.add(col++, "Not asked");
        proRow.add(col++, FORCE_SKIP);
        proRow.add(col++, MANUAL_SKIP);

        proCsvWriter.writeNext((String[]) proRow.toArray(new String[proRow.size()]));
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

        String studySiteParam = (String) request.getSession().getAttribute("organizationName");
        if (!StringUtils.isEmpty(studySiteParam)) {
            reportInformation = new ArrayList<String>();
            reportInformation.add(0, "Study Site");
            reportInformation.add(1, studySiteParam);
            writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
        }

        String crfIdParam = (String) request.getSession().getAttribute("crfTitle");
        if (!StringUtils.isEmpty(crfIdParam)) {
            reportInformation = new ArrayList<String>();
            reportInformation.add(0, "Form");
            reportInformation.add(1, crfIdParam);
            writer.writeNext((String[]) reportInformation.toArray(new String[reportInformation.size()]));
        }
    }


    private void createTableHeaders(List<String> proCtcTermHeaders, List<String> meddraTermHeaders, boolean isEq5d, CSVWriter writer) {
        List<String> proRow = new ArrayList<String>();
        int col = 0;
        proRow.add(col++, "Participant ID");
        proRow.add(col++, "Study");
        proRow.add(col++, "Study Site");
        proRow.add(col++, "Survey name");
        proRow.add(col++, "Scheduled Start Date");
        proRow.add(col++, "Scheduled End Date");
        proRow.add(col++, "First response date");
        proRow.add(col++, "Completion date");
        proRow.add(col++, "Mode");
        proRow.add(col++, "Status");
        if (!proCtcTermHeaders.isEmpty()) {
            for (int i = 0; i < proCtcTermHeaders.size(); i++) {
                proRow.add(col++, proCtcTermHeaders.get(i));
            }
        }
        if (!meddraTermHeaders.isEmpty()) {
            for (int i = 0; i < meddraTermHeaders.size(); i++) {
                proRow.add(col++, meddraTermHeaders.get(i));
            }
        }
        if (isEq5d) {
            proRow.add(col, "Health Score");
        }
        writer.writeNext((String[]) proRow.toArray(new String[proRow.size()]));
    }

    private List<String> createRow(List<List<String>> rowSet, int rownum) {
        List<String> newRow = new ArrayList<String>(maxRowSize);
        rowSet.add(rownum, newRow);
        return newRow;
    }

    private void markDefaultNotAdministered(List<String> row, int col, int length) {
        for (int i = col; i < length + col; i++) {
            row.add(i, String.valueOf(-2000));
        }
    }

    private List<String> getRow(List<List<String>> rowSet, int rowNum) {
        return rowSet.get(rowNum);
    }

    private void createCurrentRowSubPrefix(List<String> row, String participant, String study, String studySite, String formName) {
        row.add(0, participant);
        row.add(1, study);
        row.add(2, studySite);
        row.add(3, formName);

    }

    private String getCellNumberValidValue(Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping,
                                           Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping, String termEnglish, ProCtcQuestionType questionType) {

        Map<ProCtcQuestionType, String> proTypeMap = proCtcQuestionMapping.get(termEnglish);
        if (proTypeMap != null) {
            if (proTypeMap.get(questionType) != null) {
                return proTypeMap.get(questionType);
            }
        }
        Map<ProCtcQuestionType, String> meddraTypeMap = meddraQuestionMapping.get(termEnglish);
        if (meddraTypeMap != null) {
            if (meddraTypeMap.get(questionType) != null) {
                return meddraTypeMap.get(questionType);
            }
        }
        return DEFAULT_POSITION;
    }
}

