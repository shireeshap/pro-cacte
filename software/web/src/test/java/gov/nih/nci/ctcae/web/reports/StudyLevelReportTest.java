package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class StudyLevelReportTest extends WebTestCase {

    ProCtcQuestion proCtcQuestion1, proCtcQuestion2, proCtcQuestion3;
    ProCtcTerm proCtcTerm, proCtcTerm1;
    ArrayList<ValidValue> proCtcValidValueList1, proCtcValidValueList2, proCtcValidValueList3;
    TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap1, symptomMap2;
    TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> results;
    ArrayList<Date> dates1, dates2;
    LinkedHashMap<Participant, ArrayList<Date>> datesMap;


    Participant participant1, participant2;

    public void setUp() throws Exception {
        super.setUp();
        participant1 = Fixture.createParticipant("partcipant1f", "partcipant1l", "partcipant1id");
        participant2 = Fixture.createParticipant("partcipant2f", "partcipant2l", "partcipant2id");
        datesMap = new LinkedHashMap<Participant, ArrayList<Date>>();

        dates1 = new ArrayList<Date>();
        dates2 = new ArrayList<Date>();
        Calendar calendar = ProCtcAECalendar.getCalendarForDate(new Date());
        calendar.add(Calendar.DATE, 10);
        dates1.add(calendar.getTime());
        calendar.add(Calendar.DATE, 10);
        dates1.add(calendar.getTime());

        calendar.add(Calendar.DATE, 10);
        dates2.add(calendar.getTime());

        calendar.add(Calendar.DATE, 10);
        dates1.add(calendar.getTime());

        calendar.add(Calendar.DATE, 10);
        dates2.add(calendar.getTime());

        calendar.add(Calendar.DATE, 10);
        dates2.add(calendar.getTime());

        datesMap.put(participant1, dates1);
        datesMap.put(participant2, dates2);

        proCtcTerm = new ProCtcTerm();
        proCtcTerm.setProCtcTermVocab(new ProCtcTermVocab());
        proCtcTerm.getProCtcTermVocab().setTermEnglish("My Term");

        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setProCtcTermVocab(new ProCtcTermVocab());
        proCtcTerm1.getProCtcTermVocab().setTermEnglish("Your Term");


        proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        proCtcQuestion1.setProCtcTerm(proCtcTerm);

        proCtcValidValueList1 = new ArrayList<ValidValue>();

        ProCtcValidValue proCtcValidValue11 = new ProCtcValidValue();
        proCtcValidValue11.setValue(ProCtcQuestionType.SEVERITY.getValidValues()[0], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue11.setDisplayOrder(0);
        proCtcValidValue11.setResponseCode(0);

        ProCtcValidValue proCtcValidValue12 = new ProCtcValidValue();
        proCtcValidValue12.setValue(ProCtcQuestionType.SEVERITY.getValidValues()[1], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue12.setDisplayOrder(1);
        proCtcValidValue12.setResponseCode(2);

        ProCtcValidValue proCtcValidValue13 = new ProCtcValidValue();
        proCtcValidValue13.setValue(ProCtcQuestionType.SEVERITY.getValidValues()[3], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue13.setDisplayOrder(3);
        proCtcValidValue13.setResponseCode(3);

        proCtcValidValueList1.add(proCtcValidValue11);
        proCtcValidValueList1.add(proCtcValidValue12);
        proCtcValidValueList1.add(proCtcValidValue13);

        proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setProCtcQuestionType(ProCtcQuestionType.FREQUENCY);
        proCtcQuestion2.setProCtcTerm(proCtcTerm);

        proCtcValidValueList2 = new ArrayList<ValidValue>();

        ProCtcValidValue proCtcValidValue21 = new ProCtcValidValue();
        proCtcValidValue21.setValue(ProCtcQuestionType.FREQUENCY.getValidValues()[2], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue21.setDisplayOrder(2);
        proCtcValidValue21.setResponseCode(2);

        ProCtcValidValue proCtcValidValue22 = new ProCtcValidValue();
        proCtcValidValue22.setValue(ProCtcQuestionType.FREQUENCY.getValidValues()[4], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue22.setDisplayOrder(4);
        proCtcValidValue22.setResponseCode(4);

        ProCtcValidValue proCtcValidValue23 = new ProCtcValidValue();
        proCtcValidValue23.setValue(ProCtcQuestionType.FREQUENCY.getValidValues()[3], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue23.setDisplayOrder(3);
        proCtcValidValue23.setResponseCode(3);

        proCtcValidValueList2.add(proCtcValidValue21);
        proCtcValidValueList2.add(proCtcValidValue22);
        proCtcValidValueList2.add(proCtcValidValue23);

        proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        proCtcQuestion3.setProCtcTerm(proCtcTerm);

        proCtcValidValueList3 = new ArrayList<ValidValue>();

        ProCtcValidValue proCtcValidValue31 = new ProCtcValidValue();
        proCtcValidValue31.setValue(ProCtcQuestionType.INTERFERENCE.getValidValues()[1], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue31.setDisplayOrder(1);
        proCtcValidValue31.setResponseCode(1);

        ProCtcValidValue proCtcValidValue32 = new ProCtcValidValue();
        proCtcValidValue32.setValue(ProCtcQuestionType.INTERFERENCE.getValidValues()[4], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue32.setDisplayOrder(4);
        proCtcValidValue32.setResponseCode(4);

        ProCtcValidValue proCtcValidValue33 = new ProCtcValidValue();
        proCtcValidValue33.setValue(ProCtcQuestionType.INTERFERENCE.getValidValues()[3], SupportedLanguageEnum.ENGLISH);
        proCtcValidValue33.setDisplayOrder(3);
        proCtcValidValue33.setResponseCode(3);

        proCtcValidValueList3.add(proCtcValidValue31);
        proCtcValidValueList3.add(proCtcValidValue32);
        proCtcValidValueList3.add(proCtcValidValue33);

        results = new TreeMap<Organization, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>(new OrganizationNameComparator());

        LinkedHashMap<Question, ArrayList<ValidValue>> symptoms1 = new LinkedHashMap<Question, ArrayList<ValidValue>>();
        LinkedHashMap<Question, ArrayList<ValidValue>> symptoms2 = new LinkedHashMap<Question, ArrayList<ValidValue>>();
        proCtcTerm.addProCtcQuestion(proCtcQuestion1);
        proCtcTerm.addProCtcQuestion(proCtcQuestion2);
        proCtcTerm.addProCtcQuestion(proCtcQuestion3);

        symptoms1.put(proCtcQuestion1, proCtcValidValueList1);
        symptoms1.put(proCtcQuestion2, proCtcValidValueList2);
        symptoms2.put(proCtcQuestion3, proCtcValidValueList3);
        Organization organization = Fixture.createOrganization("orgname", "orgcode");
        Study study = Fixture.createStudyWithStudySite("short", "long", "assigned id", organization);
        symptomMap1 = new TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>();
        symptomMap2 = new TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>();
        symptomMap1.put(proCtcTerm.getProCtcTermVocab().getTermEnglish(), symptoms1);
        symptomMap1.put(proCtcTerm2.getProCtcTermVocab().getTermEnglish(), symptoms1);
        symptomMap2.put(proCtcTerm1.getProCtcTermVocab().getTermEnglish(), symptoms2);
        symptomMap2.put(proCtcTerm3.getProCtcTermVocab().getTermEnglish(), symptoms2);

        TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> pMap = new TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>(new ParticipantNameComparator());
        pMap.put(participant1, symptomMap1);
        pMap.put(participant2, symptomMap2);
        results.put(organization, pMap);

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDatesMap", datesMap);
        request.getSession().setAttribute("participant", Fixture.createParticipant("pf", "pl", "pid"));
        request.getSession().setAttribute("study", study);
        request.getSession().setAttribute("crf", Fixture.createCrf());
        request.getSession().setAttribute("studySite", study.getStudySites().get(0));
    }

    public void testExcelGeneration() throws Exception {

        StudyLevelReportExcelController controller = new StudyLevelReportExcelController();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        StudyLevelReportExcelView view = (StudyLevelReportExcelView) modelAndView.getView();
        view.render(null, request, response);
        assertEquals("application/vnd.ms-excel", response.getContentType());
        File f = new File("generatedexcel.xls");
        if (f.exists()) {
            assertTrue(f.delete());
        }
        assertTrue(f.createNewFile());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
        bufferedOutputStream.write(response.getContentAsByteArray());
        bufferedOutputStream.close();
    }

    public void testHtmlTableGeneration() throws Exception {

        StudyLevelReportResultsController controller = new StudyLevelReportResultsController();
        StringBuilder out = new StringBuilder();
        out.append("<html>\n" +
                "<head>\n" +
                "    <style type=\"text/css\">\n" +
                "        table.widget {\n" +
                "            border: 1px solid #eaeaea;\n" +
                "            border-collapse: collapse;\n" +
                "        }\n" +
                "\n" +
                "        table.widget col {\n" +
                "            width: 200px;\n" +
                "        }\n" +
                "\n" +
                "        td.data {\n" +
                "            border: 1px solid #eaeaea;\n" +
                "            background-color: #D5D5D5;\n" +
                "            white-space: nowrap;\n" +
                "            text-align: center;\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        td.header-top {\n" +
                "            border: 1px solid #eaeaea;\n" +
                "            font-weight: bold;\n" +
                "            text-align: center;\n" +
                "            background-color: #cccccc;\n" +
                "        }\n" +
                "\n" +
                "        td.category-name, td.subcategory-name, td.actual-question {\n" +
                "            border: 1px solid #eaeaea;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "\n" +
                "        td.category-name {\n" +
                "            background-color: #fff;\n" +
                "            font-weight: bolder;\n" +
                "        }\n" +
                "\n" +
                "        td.subcategory-name {\n" +
                "            background-color: #fff;\n" +
                "            padding-left: 6px;\n" +
                "            vertical-align: top;\n" +
                "        }\n" +
                "\n" +
                "        td.help-values {\n" +
                "            border: 1px solid #eaeaea;\n" +
                "            background-color: #fff;\n" +
                "            padding-left: 6px;\n" +
                "            vertical-align: top;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        td.actual-question {\n" +
                "            background-color: #009999;\n" +
                "            text-align: center;\n" +
                "            font-weight: bold;\n" +
                "            color: white;\n" +
                "        }\n" +
                "\n" +
                "        /* The hint to Hide and Show */\n" +
                "        .hint {\n" +
                "            z-index: 3; /* To handle the overlapping issue*/\n" +
                "            display: none;\n" +
                "            position: absolute;\n" +
                "            width: 700px;\n" +
                "            white-space: normal;\n" +
                "            margin-top: -4px;\n" +
                "            border: 1px solid #c93;\n" +
                "            padding: 10px 12px;\n" +
                "            opacity: .95;\n" +
                "            background: #ffc url( ../images/pointer.gif ) no-repeat -10px 5px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>");
        TreeMap<Organization, TreeMap<Participant, String>> table = controller.getHtmlTable(results, datesMap);
        for (Organization organization : table.keySet()) {
            TreeMap<Participant, String> map = table.get(organization);
            for (Participant participant : map.keySet()) {
                out.append(participant.getDisplayName()).append("\n");
                out.append(map.get(participant));
            }
        }
        File f = new File("generatedhtml.html");
        if (f.exists()) {
            assertTrue(f.delete());
        }
        assertTrue(f.createNewFile());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
        bufferedOutputStream.write(out.toString().getBytes());
        bufferedOutputStream.close();
    }


}