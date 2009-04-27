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
public class ParticipantCareReportTest extends WebTestCase {

    ProCtcQuestion proCtcQuestion1, proCtcQuestion2, proCtcQuestion3;
    ProCtcTerm proCtcTerm;
    ArrayList<ProCtcValidValue> proCtcValidValueList1, proCtcValidValueList2, proCtcValidValueList3;
    TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results;
    ArrayList<Date> dates;

    public void setUp() throws Exception {
        super.setUp();

        dates = new ArrayList<Date>();
        Calendar calendar = ProCtcAECalendar.getCalendarForDate(new Date());
        calendar.add(Calendar.DATE, 10);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DATE, 10);
        dates.add(calendar.getTime());
        calendar.add(Calendar.DATE, 10);
        dates.add(calendar.getTime());


        proCtcTerm = new ProCtcTerm();
        proCtcTerm.setTerm("My Term");
        proCtcTerm.setId(1);


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

        results = new TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());

        HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> symptoms = new HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>();
        proCtcTerm.addProCtcQuestion(proCtcQuestion1);
        proCtcTerm.addProCtcQuestion(proCtcQuestion2);
        proCtcTerm.addProCtcQuestion(proCtcQuestion3);

        symptoms.put(proCtcQuestion1, proCtcValidValueList1);
        symptoms.put(proCtcQuestion2, proCtcValidValueList2);
        symptoms.put(proCtcQuestion3, proCtcValidValueList3);

        Study study = Fixture.createStudyWithStudySite("short", "long", "assigned id", Fixture.createOrganization("orgname", "orgcode"));
        results.put(proCtcTerm, symptoms);
        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDates", dates);
        request.getSession().setAttribute("participant", Fixture.createParticipant("pf", "pl", "pid"));
        request.getSession().setAttribute("study", study);
        request.getSession().setAttribute("crf", Fixture.createCrf());
        request.getSession().setAttribute("studySite", study.getStudySites().get(0));
    }

    public void testPdfGeneration() throws Exception {

        ParticipantCarePdfController controller = new ParticipantCarePdfController();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        ParticipantCarePdfView view = (ParticipantCarePdfView) modelAndView.getView();
        view.render(null, request, response);
        assertEquals("application/pdf", response.getContentType());
        File f = new File("/etc/ctcae/generatedpdf.pdf");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
        bufferedOutputStream.write(response.getContentAsByteArray());
        bufferedOutputStream.close();


    }

    public void testExcelGeneration() throws Exception {

        ParticipantCareExcelController controller = new ParticipantCareExcelController();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        ParticipantCareExcelView view = (ParticipantCareExcelView) modelAndView.getView();
        view.render(null, request, response);
        assertEquals("application/vnd.ms-excel", response.getContentType());
        File f = new File("/etc/ctcae/generatedexcel.xls");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
        bufferedOutputStream.write(response.getContentAsByteArray());
        bufferedOutputStream.close();
    }
}