package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: mehul
 * Date: 5/4/11
 */

public class ProTermsMultiLangImporterV4 {
    ProCtcTermRepository proCtcTermRepository;
    ProCtcQuestionRepository proCtcQuestionRepository;

    //    private static final String LANGUAGE = "Language";
    private static final String QUESTION_TEXT = "Language specific PRO-CTCAE Question Display";
    private static final String PRO_CTC_TERM = "PRO-CTCAE Term";
    private static final String PRO_CTC_TERM_LANG = "Language specific PRO-CTCAE Term Display";
    private static final String QUESTION_TYPE = "Attribute";
    private static final String VALID_VALUE1 = "Response Option - 1";
    private static final String VALID_VALUE2 = "Response Option - 2";
    private static final String VALID_VALUE3 = "Response Option - 3";
    private static final String VALID_VALUE4 = "Response Option - 4";
    private static final String VALID_VALUE5 = "Response Option - 5";
    private static final String VALID_VALUE6 = "Response Option - 6";
    private static final String VALID_VALUE7 = "Response Option - 7";

    public void updateMultiLangProTerms() throws IOException {
        CsvReader reader;
        ClassPathResource classPathResource = new ClassPathResource("Spanish_Pro-CTCAE_items_08.04.2011.csv");
//        reader = new CsvReader(new InputStreamReader(classPathResource.getInputStream()));
        reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("UTF-8"));
        reader.readHeaders();

        while (reader.readRecord()) {

            String proCtcTerm = reader.get(PRO_CTC_TERM).trim();
            String questionText = reader.get(QUESTION_TEXT).trim();
            String attribute = reader.get(QUESTION_TYPE).trim();
            String proCtcTermLang = reader.get(PRO_CTC_TERM_LANG).trim();
            String firstLetter = questionText.substring(0, 1);
            questionText = firstLetter.toUpperCase() + questionText.substring(1);
            String questionType = attribute.substring(attribute.indexOf('-') + 1);
            List<String> values = new ArrayList<String>();
            String value1 = reader.get(VALID_VALUE1).trim();
            String value2 = reader.get(VALID_VALUE2).trim();
            String value3 = reader.get(VALID_VALUE3).trim();
            String value4 = reader.get(VALID_VALUE4).trim();
            String value5 = reader.get(VALID_VALUE5).trim();
            String value6 = reader.get(VALID_VALUE6).trim();
            String value7 = reader.get(VALID_VALUE7).trim();
            values.add(value1);
            values.add(value2);
            values.add(value3);
            values.add(value4);
            values.add(value5);
            values.add(value6);
            values.add(value7);

            ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
            proCtcQuestionQuery.filterByQuestionType(ProCtcQuestionType.getByCode(questionType));
            proCtcQuestionQuery.filterByTerm(proCtcTerm);
            List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
            if (proCtcQuestions != null && proCtcQuestions.size() > 0) {
                ProCtcQuestion proCtcQuestion = proCtcQuestions.get(0);
//                if (language.equalsIgnoreCase("Spanish")) {
                System.out.println(questionText);
                proCtcQuestion.getProCtcQuestionVocab().setQuestionTextSpanish(questionText);


//                }
                int i = 0;
                for (ProCtcValidValue validValue : proCtcQuestion.getValidValues()) {
                    if (StringUtils.isNotBlank(values.get(i))) {
//                        if (language.equalsIgnoreCase("Spanish")) {
                        System.out.println(values.get(i));
                        validValue.getProCtcValidValueVocab().setValueSpanish(values.get(i));
//                            System.out.println("spanish value set");
//                        }
                    }
                    i++;
                }
                proCtcQuestionRepository.save(proCtcQuestion);
            }

            ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
            proCtcTermQuery.filterByTerm(proCtcTerm);
            List<ProCtcTerm> proTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
            if (proTerms != null && proTerms.size() > 0) {
                ProCtcTerm proTerm = proTerms.get(0);
//                if (language.equalsIgnoreCase("Spanish")) {
                proTerm.getProCtcTermVocab().setTermSpanish(proCtcTermLang);
                System.out.println("spanish proCtcTerm set");
//                }
                proCtcTermRepository.save(proTerm);
            }


        }
    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
