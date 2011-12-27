package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionDisplayRule;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcTermVocab;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.csvreader.CsvReader;


/**
 * @author Mehul Gulati
 *         Date: Jan 15, 2009
 */

public class ProCtcTermsImporterV4 {
    private CtcTermRepository ctcTermRepository;
    private static final String QUESTION_TEXT = "PRO-CTCAE Wording";
    private static final String PRO_CTC_TERM = "PRO-CTCAE Term";
    private static final String CORE_ITEM = "Core Item";
    private static final String QUESTION_TYPE = "Attribute";
    private static final String PRO_CTC_VALID_VALUES = "Response Options";
    private static final String CTC_TERM = "CTCAE v4 Term";
    private static final String CATEGORY = "CTCAE v4 System Organ Class (SOC)";


    public ProCtc loadProCtcTerms(boolean fromTestCase) throws IOException {
        CsvReader reader;
        HashMap<String, List<CsvLine>> hm = new LinkedHashMap<String, List<CsvLine>>();
        if (fromTestCase) {
            //Resource resource = new FileSystemResource("software/core/src/main/resources/");
            Resource resource = new FileSystemResource("core/src/main/resources/");
        	Resource resource1 = resource.createRelative("ProCtcTerms_V4.csv");
            File f = new File(resource1.getFile().getCanonicalPath());
            System.out.println(f.getCanonicalPath());
            reader = new CsvReader(new FileInputStream(f), Charset.forName("ISO-8859-1"));
        } else {
            ClassPathResource classPathResource = new ClassPathResource("ProCtcTerms_V4.csv");
            reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("ISO-8859-1"));
        }
        reader.readHeaders();
        String oldProCtcTerm = "";
        int displayOrderI = 0;

        while (reader.readRecord()) {
            CsvLine csvLine = new CsvLine();

            String question = reader.get(QUESTION_TEXT).trim();
            String proCtcTerm = reader.get(PRO_CTC_TERM).trim();
            if (proCtcTerm.equals(oldProCtcTerm)) {
                displayOrderI++;
            } else {
                displayOrderI = 1;
            }
            oldProCtcTerm = proCtcTerm;
            String core = reader.get(CORE_ITEM).trim();
            String attribute = reader.get(QUESTION_TYPE).trim();
            String validValues = reader.get(PRO_CTC_VALID_VALUES).trim();
            String ctcTerm = reader.get(CTC_TERM).trim();
            String ctcCategory = reader.get(CATEGORY).trim();

            String displayOrder = "" + displayOrderI;
            String questionType = attribute.substring(attribute.indexOf('-') + 1);

            csvLine.setProctcTerm(proCtcTerm);
            csvLine.setCtcTerm(ctcTerm);
            csvLine.setDisplayOrder(displayOrder);
            csvLine.setQuestionType(questionType);
            String firstLetter = question.substring(0, 1);
            question = firstLetter.toUpperCase() + question.substring(1);
            csvLine.setQuestionText(question);
            csvLine.setProctcValidValues(validValues);
            csvLine.setCoreItem(!StringUtils.isBlank(core));

            if (hm.containsKey(csvLine.getProctcTerm())) {
                List list = hm.get(csvLine.getProctcTerm());
                list.add(csvLine);
            } else {
                ArrayList list = new ArrayList();
                list.add(csvLine);
                hm.put(csvLine.getProctcTerm(), list);
            }
            System.out.println(csvLine);

        }
        ProCtc proCtc = new ProCtc();
        loadProCtcHelper(hm, proCtc);
        proCtc.setProCtcVersion("4.0");
        proCtc.setReleaseDate(new Date());
        return proCtc;
    }

    public ProCtc loadProCtcHelper(HashMap hm1, ProCtc proCtc) {
        HashMap<String, ProCtcQuestion> firstQuestions = new HashMap<String, ProCtcQuestion>();
        HashMap<String, List<CsvLine>> hm = hm1;


        for (String hmKey : hm.keySet()) {
            List<CsvLine> list = hm.get(hmKey);
            String ctcTerm = list.get(0).getCtcTerm();
            String proCtcTerm = hmKey;
            CtcQuery ctcQuery = new CtcQuery();
            ctcQuery.filterByName(ctcTerm);
            List<CtcTerm> ctcTerms = ctcTermRepository.find(ctcQuery);
            CtcTerm objCtcTerm = null;
            if (ctcTerms.size() == 0) {
                System.out.println("Could not find ctc term for " + proCtcTerm + ". Skipping " + proCtcTerm);
            } else {
                if (ctcTerms.size() > 1) {
                    System.out.println("Multiple ctc terms found for " + proCtcTerm + ". Skipping " + proCtcTerm);
                } else {
                    objCtcTerm = ctcTerms.get(0);
                }
            }

            if (objCtcTerm != null) {
                ProCtcTerm objProCtcTerm = new ProCtcTerm();
                ProCtcTermVocab proCtcTermVocab = new ProCtcTermVocab(objProCtcTerm);
                objProCtcTerm.setProCtcTermVocab(proCtcTermVocab);
                objProCtcTerm.getProCtcTermVocab().setTermEnglish(proCtcTerm);
                objProCtcTerm.setCtcTerm(objCtcTerm);
                objProCtcTerm.setProCtc(proCtc);
                proCtc.addProCtcTerm(objProCtcTerm);

                for (CsvLine hmValue : list) {
                    ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
                    proCtcQuestion.setQuestionText(hmValue.getQuestionText(), SupportedLanguageEnum.ENGLISH);
                    proCtcQuestion.setDisplayOrder(new Integer(hmValue.getDisplayOrder()));
                    proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.getByDisplayName(hmValue.getQuestionType()));
                    proCtcQuestion.setProCtcTerm(objProCtcTerm);
                    objProCtcTerm.addProCtcQuestion(proCtcQuestion);
                    objProCtcTerm.setCore(hmValue.isCoreItem());
                    if (hmValue.getGender() != null) {
                        objProCtcTerm.setGender(hmValue.getGender());
                    }
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
                        if (firstQuestion != null) {
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
            }
        }
        return proCtc;
    }


    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

}