package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author Vinay Kumar
 * @crated Mar 18, 2009
 */
public class StudyLevelReportTest extends WebTestCase {

    ProCtcQuestion proCtcQuestion1, proCtcQuestion2, proCtcQuestion3;
    ProCtcTerm proCtcTerm, proCtcTerm1;
    ArrayList<ProCtcValidValue> proCtcValidValueList1, proCtcValidValueList2, proCtcValidValueList3;
    TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap1, symptomMap2;
    TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> results;
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
        proCtcTerm.setTerm("My Term");

        proCtcTerm1 = new ProCtcTerm();
        proCtcTerm1.setTerm("Your Term");


        proCtcQuestion1 = new ProCtcQuestion();
        proCtcQuestion1.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        proCtcQuestion1.setProCtcTerm(proCtcTerm);

        proCtcValidValueList1 = new ArrayList<ProCtcValidValue>();

        ProCtcValidValue proCtcValidValue11 = new ProCtcValidValue();
        proCtcValidValue11.setValue(ProCtcQuestionType.SEVERITY.getValidValues()[0]);
        proCtcValidValue11.setDisplayOrder(0);

        ProCtcValidValue proCtcValidValue12 = new ProCtcValidValue();
        proCtcValidValue12.setValue(ProCtcQuestionType.SEVERITY.getValidValues()[1]);
        proCtcValidValue12.setDisplayOrder(1);

        ProCtcValidValue proCtcValidValue13 = new ProCtcValidValue();
        proCtcValidValue13.setValue(ProCtcQuestionType.SEVERITY.getValidValues()[3]);
        proCtcValidValue13.setDisplayOrder(3);

        proCtcValidValueList1.add(proCtcValidValue11);
        proCtcValidValueList1.add(proCtcValidValue12);
        proCtcValidValueList1.add(proCtcValidValue13);

        proCtcQuestion2 = new ProCtcQuestion();
        proCtcQuestion2.setProCtcQuestionType(ProCtcQuestionType.FREQUENCY);
        proCtcQuestion2.setProCtcTerm(proCtcTerm);

        proCtcValidValueList2 = new ArrayList<ProCtcValidValue>();

        ProCtcValidValue proCtcValidValue21 = new ProCtcValidValue();
        proCtcValidValue21.setValue(ProCtcQuestionType.FREQUENCY.getValidValues()[2]);
        proCtcValidValue21.setDisplayOrder(2);

        ProCtcValidValue proCtcValidValue22 = new ProCtcValidValue();
        proCtcValidValue22.setValue(ProCtcQuestionType.FREQUENCY.getValidValues()[4]);
        proCtcValidValue22.setDisplayOrder(4);

        ProCtcValidValue proCtcValidValue23 = new ProCtcValidValue();
        proCtcValidValue23.setValue(ProCtcQuestionType.FREQUENCY.getValidValues()[3]);
        proCtcValidValue23.setDisplayOrder(3);

        proCtcValidValueList2.add(proCtcValidValue21);
        proCtcValidValueList2.add(proCtcValidValue22);
        proCtcValidValueList2.add(proCtcValidValue23);

        proCtcQuestion3 = new ProCtcQuestion();
        proCtcQuestion3.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
        proCtcQuestion3.setProCtcTerm(proCtcTerm);

        proCtcValidValueList3 = new ArrayList<ProCtcValidValue>();

        ProCtcValidValue proCtcValidValue31 = new ProCtcValidValue();
        proCtcValidValue31.setValue(ProCtcQuestionType.INTERFERENCE.getValidValues()[1]);
        proCtcValidValue31.setDisplayOrder(1);

        ProCtcValidValue proCtcValidValue32 = new ProCtcValidValue();
        proCtcValidValue32.setValue(ProCtcQuestionType.INTERFERENCE.getValidValues()[4]);
        proCtcValidValue32.setDisplayOrder(4);

        ProCtcValidValue proCtcValidValue33 = new ProCtcValidValue();
        proCtcValidValue33.setValue(ProCtcQuestionType.INTERFERENCE.getValidValues()[3]);
        proCtcValidValue33.setDisplayOrder(3);

        proCtcValidValueList3.add(proCtcValidValue31);
        proCtcValidValueList3.add(proCtcValidValue32);
        proCtcValidValueList3.add(proCtcValidValue33);

        results = new TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>(new ParticipantNameComparator());

        LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> symptoms1 = new LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>();
        LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> symptoms2 = new LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>();
        proCtcTerm.addProCtcQuestion(proCtcQuestion1);
        proCtcTerm.addProCtcQuestion(proCtcQuestion2);
        proCtcTerm.addProCtcQuestion(proCtcQuestion3);

        symptoms1.put(proCtcQuestion1, proCtcValidValueList1);
        symptoms1.put(proCtcQuestion2, proCtcValidValueList2);
        symptoms2.put(proCtcQuestion3, proCtcValidValueList3);

        Study study = Fixture.createStudyWithStudySite("short", "long", "assigned id", Fixture.createOrganization("orgname", "orgcode"));
        symptomMap1 = new TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());
        symptomMap2 = new TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());
        symptomMap1.put(proCtcTerm, symptoms1);
        symptomMap1.put(proCtcTerm2, symptoms1);
        symptomMap2.put(proCtcTerm1, symptoms2);
        symptomMap2.put(proCtcTerm3, symptoms2);

        results.put(participant1, symptomMap1);
        results.put(participant2, symptomMap2);

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDatesMap", datesMap);
        request.getSession().setAttribute("participant", Fixture.createParticipant("pf", "pl", "pid"));
        request.getSession().setAttribute("study", study);
        request.getSession().setAttribute("crf", Fixture.createCrf());
        request.getSession().setAttribute("studySite", study.getStudySites().get(0));
    }

    public void testPdfGeneration() throws Exception {

        StudyLevelReportPdfController controller = new StudyLevelReportPdfController();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        StudyLevelReportPdfView view = (StudyLevelReportPdfView) modelAndView.getView();
        view.render(null, request, response);
        assertEquals("application/pdf", response.getContentType());
        File f = new File("/etc/proctcae/generatedpdf.pdf");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
        bufferedOutputStream.write(response.getContentAsByteArray());
        bufferedOutputStream.close();


    }

    public void testExcelGeneration() throws Exception {

        StudyLevelReportExcelController controller = new StudyLevelReportExcelController();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        StudyLevelReportExcelView view = (StudyLevelReportExcelView) modelAndView.getView();
        view.render(null, request, response);
        assertEquals("application/vnd.ms-excel", response.getContentType());
        File f = new File("/etc/proctcae/generatedexcel.xls");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
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
        TreeMap<Participant, String> table = controller.getTable(results, datesMap);
        for (Participant participant : table.keySet()) {
            out.append(participant.getDisplayName() + "\n");
            out.append(table.get(participant));
        }
        File f = new File("/etc/proctcae/generatedhtml.html");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
        bufferedOutputStream.write(out.toString().getBytes());
        bufferedOutputStream.close();
    }


}