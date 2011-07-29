package gov.nih.nci.ctcae.core.csv.loader;

import com.csvreader.CsvReader;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.CtcTermRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author mehul gulati
 *         Date: Jun 28, 2010
 */
public class UpdateProCtcTermsImporterV4 {
    ProCtcQuestionRepository proCtcQuestionRepository;
    CtcTermRepository ctcTermRepository;
    ProCtcTermRepository proCtcTermRepository;

    private static final String QUESTION_TEXT = "PROCTCAE Wording";
    private static final String PRO_CTC_TERM = "PRO-CTCAE Term";
    private static final String CORE_ITEM = "Core Item";
    private static final String QUESTION_TYPE = "Attribute";
    private static final String PRO_CTC_VALID_VALUES = "Response Options";
    private static final String CTC_TERM = "CTCAE v4 Term";
    private static final String GENDER = "Gender";

    public void updateProCtcTerms(ProCtc proCtc) throws IOException {
        CsvReader reader;
        HashMap<String, List<CsvLine>> hm = new LinkedHashMap<String, List<CsvLine>>();

        ClassPathResource classPathResource = new ClassPathResource("PRO-CTCAE_items_updated_05.17.2011_formatted.csv");
        reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("ISO-8859-1"));
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
            String ctcTerm = reader.get(CTC_TERM).trim();
            String firstLetter = question.substring(0, 1);
            question = firstLetter.toUpperCase() + question.substring(1);
            String questionType = attribute.substring(attribute.indexOf('-') + 1);
            String validValues = reader.get(PRO_CTC_VALID_VALUES).trim();
            String gender = reader.get(GENDER).trim();
            String displayOrder = "" + displayOrderI;

            csvLine.setProctcTerm(proCtcTerm);
            csvLine.setCtcTerm(ctcTerm);
            csvLine.setDisplayOrder(displayOrder);
            csvLine.setQuestionType(questionType);
            csvLine.setQuestionText(question);
            csvLine.setProctcValidValues(validValues);
            csvLine.setCoreItem(!StringUtils.isBlank(core));
            csvLine.setGender(gender);

            ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
            proCtcQuestionQuery.filterByQuestionType(ProCtcQuestionType.getByCode(questionType));
            proCtcQuestionQuery.filterByTerm(proCtcTerm);
            List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
            if (proCtcQuestions != null && proCtcQuestions.size() > 0) {
                ProCtcQuestion ctcQuestion = proCtcQuestions.get(0);
                ctcQuestion.getProCtcTerm().setGender(gender);
                ctcQuestion.setQuestionText(question, SupportedLanguageEnum.ENGLISH);
                if (ctcQuestion.getProCtcQuestionType().equals(ProCtcQuestionType.PRESENT)) {
                    for (ProCtcValidValue proCtcValidValue : ctcQuestion.getValidValues()) {
                        if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).trim().equals("Yes")) {
                            proCtcValidValue.setDisplayOrder(1);
                        } else {
                            proCtcValidValue.setDisplayOrder(0);
                        }
                    }
                }
                setResponseCode(ctcQuestion);
                StringTokenizer st1 = new StringTokenizer(validValues, "/");
                Collection<ProCtcValidValue> values = ctcQuestion.getValidValues();
                Collection<String> validValues1 = new ArrayList();
                for (ProCtcValidValue proValue : values) {
                    String value = proValue.getValue(SupportedLanguageEnum.ENGLISH).trim();
                    validValues1.add(value);
                }
                int j = validValues1.size();
                while (st1.hasMoreTokens()) {
                    String nextToken = st1.nextToken().trim();
                    if (!validValues1.contains(nextToken)) {
                        ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
                        ProCtcValidValueVocab proCtcValidValueVocab = new ProCtcValidValueVocab();
                        proCtcValidValueVocab.setValueEnglish(nextToken);
                        proCtcValidValueVocab.setProCtcValidValue(proCtcValidValue);
                        proCtcValidValue.setProCtcValidValueVocab(proCtcValidValueVocab);
                        proCtcValidValue.setDisplayOrder(j);
                        j++;
                        ctcQuestion.addValidValue(proCtcValidValue);
                    }
                }
                ctcQuestion.getProCtcTerm().setCurrency("Y");
                proCtcQuestionRepository.save(ctcQuestion);
            } else {
                CtcQuery ctcQuery = new CtcQuery();
                ctcQuery.filterByName(ctcTerm);
                List<CtcTerm> ctcTerms = ctcTermRepository.find(ctcQuery);
                if (ctcTerms != null && ctcTerms.size() > 0) {
                    CtcTerm ctcTer = ctcTerms.get(0);
                    if (ctcTer.getProCtcTerms().size() > 0) {
                        ctcTer.getProCtcTerms().get(0).getProCtcTermVocab().setTermEnglish(proCtcTerm);
                        ctcTer.getProCtcTerms().get(0).setGender(gender);
                        ctcTermRepository.save(ctcTer);
                    } else {
                        if (hm.containsKey(csvLine.getProctcTerm())) {
                            List list = hm.get(csvLine.getProctcTerm());
                            list.add(csvLine);
                        } else {
                            ArrayList list = new ArrayList();
                            list.add(csvLine);
                            hm.put(csvLine.getProctcTerm(), list);
                        }
                    }
                }
            }
        }

        HashMap<String, ProCtcQuestion> firstQuestions = new HashMap<String, ProCtcQuestion>();

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
                objProCtcTerm.getProCtcTermVocab().setTermEnglish(proCtcTerm);
                objProCtcTerm.setCtcTerm(objCtcTerm);
                objProCtcTerm.setProCtc(proCtc);
                objProCtcTerm.setCurrency("Y");
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
                proCtcTermRepository.save(objProCtcTerm);
            }

        }

    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }

    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    public void setResponseCode(ProCtcQuestion proCtcQuestion) {
        int j = 0;
        for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
            proCtcValidValue.setResponseCode(j);
            j++;
        }
        for (ProCtcValidValue proCtcValidValue : proCtcQuestion.getValidValues()) {
            if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).toLowerCase().trim().equals("not sexually active")) {
                proCtcValidValue.setResponseCode(-66);
            }
            if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).toLowerCase().trim().equals("not applicable")) {
                proCtcValidValue.setResponseCode(-88);
            }
            if (proCtcValidValue.getValue(SupportedLanguageEnum.ENGLISH).toLowerCase().trim().equals("prefer not to answer")) {
                proCtcValidValue.setResponseCode(-77);
            }
        }
    }
}
