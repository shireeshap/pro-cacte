package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;


/**
 * @author Mehul Gulati
 *         Date: Jan 15, 2009
 */

public class ProCtcTermsImporterV3 {

    private static final String PRO_CTC_TERM = "ProCtcae Term";
    private static final String CTC_TERM = "Ctcae Term";
    private static final String DISPLAY_ORDER = "Display Order";
    private static final String QUESTION_TYPE = "Question Type";
    private static final String QUESTION_TEXT = "Question Text";
    private static final String PRO_CTC_VALID_VALUES = "ProCtc Valid Values";

    private CtcTermRepository ctcTermRepository;

    public ProCtc readCsv() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("ctcae_display_rules.csv");
        HashMap<String, List<CsvLine>> hm = new HashMap<String, List<CsvLine>>();
        HashMap<String, ProCtcQuestion> firstQuestions = new HashMap<String, ProCtcQuestion>();
        CsvReader reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("ISO-8859-1"));
        reader.readHeaders();
        while (reader.readRecord()) {
            CsvLine csvLine = new CsvLine();

            String proCtcTerm = reader.get(PRO_CTC_TERM);
            String ctcTerm = reader.get(CTC_TERM);
            String displayOrder = reader.get(DISPLAY_ORDER);
            String questionType = reader.get(QUESTION_TYPE);
            String questionText = reader.get(QUESTION_TEXT);
            String proCtcValidValues = reader.get(PRO_CTC_VALID_VALUES);

            if (questionType.toLowerCase().equals("present")) {
                questionType = "Present/Not present";
            }

            csvLine.setProctcTerm(proCtcTerm);
            csvLine.setCtcTerm(ctcTerm);
            csvLine.setDisplayOrder(displayOrder);
            csvLine.setQuestionType(questionType);
            csvLine.setQuestionText(questionText);
            csvLine.setProctcValidValues(proCtcValidValues);
            //System.out.println(csvLine.toString());

            if (hm.containsKey(csvLine.getProctcTerm())) {
                List list = hm.get(csvLine.getProctcTerm());
                list.add(csvLine);
            } else {
                ArrayList list = new ArrayList();
                list.add(csvLine);
                hm.put(csvLine.getProctcTerm(), list);

            }
        }


        ProCtc proCtc = new ProCtc();
        proCtc.setProCtcVersion("3.0");
        proCtc.setReleaseDate(new Date());

        for (String hmKey : hm.keySet()) {
            List<CsvLine> list = hm.get(hmKey);
            CtcQuery ctcQuery = new CtcQuery();
            ctcQuery.filterByName(list.get(0).getCtcTerm());
            List<CtcTerm> ctcTerm = ctcTermRepository.find(ctcQuery);
            if (ctcTerm.size() == 0) {
                System.out.println(list.get(0).getCtcTerm());
                continue;
            }

            ProCtcTerm proCtcTerm = new ProCtcTerm();
            proCtcTerm.getProCtcTermVocab().setTermEnglish(hmKey);
            proCtc.addProCtcTerm(proCtcTerm);
            proCtcTerm.setCtcTerm(ctcTerm.get(0));

            for (CsvLine hmValue : list) {
                ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
                String temp = hmValue.getQuestionText();
                temp = StringUtils.replace(temp, "$", hmValue.getProctcTerm());
                proCtcQuestion.setQuestionText(temp, SupportedLanguageEnum.ENGLISH);
                proCtcQuestion.setDisplayOrder(new Integer(hmValue.getDisplayOrder()));
                proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.getByDisplayName(hmValue.getQuestionType()));
                proCtcTerm.addProCtcQuestion(proCtcQuestion);


                StringTokenizer st1 = new StringTokenizer(hmValue.getProctcValidValues(), "/");
                int j = 0;
                while (st1.hasMoreTokens()) {
                    ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
                    proCtcValidValue.setValue(st1.nextToken(), SupportedLanguageEnum.ENGLISH);
                    if (proCtcQuestion.getProCtcQuestionType().equals(ProCtcQuestionType.PRESENT)) {
                        if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).equals("Yes")) {
                            proCtcValidValue.setDisplayOrder(1);
                        } else {
                            proCtcValidValue.setDisplayOrder(0);
                        }
                    } else {
                        proCtcValidValue.setDisplayOrder(j);
                    }
                    j++;
                    proCtcQuestion.addValidValue(proCtcValidValue);
                }

                if (new Integer(hmValue.getDisplayOrder()) == 1) {
                    firstQuestions.put(hmValue.getProctcTerm(), proCtcQuestion);
                }

                if (new Integer(hmValue.getDisplayOrder()) > 1) {
                    int i = 0;
                    ProCtcQuestion firstQuestion = firstQuestions.get(hmValue.getProctcTerm());
                    for (ProCtcValidValue v : firstQuestion.getValidValues()) {
                        if (i == 0) {
                            i++;
                        } else {
                            ProCtcQuestionDisplayRule rule = new ProCtcQuestionDisplayRule();
                            rule.setProCtcValidValue(v);
                            proCtcQuestion.addDisplayRules(rule);

                        }
                    }

                }
            }
        }
        return proCtc;

    }

    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }
}