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
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class LoaderHelper {
	private ProCtcTermRepository proCtcTermRepository;
	private CtcTermRepository ctcTermRepository;
	
	public static Log log = LogFactory.getLog(LoaderHelper.class);
	
   public ProCtc createProCtcTermsAndProCtcQuestionsFromHashMap(HashMap<String, List<CsvLine>> hm1, ProCtc proCtc) {
        HashMap<String, ProCtcQuestion> firstQuestions = new HashMap<String, ProCtcQuestion>();
        HashMap<String, List<CsvLine>> hm = hm1;

        for (String hmKey : hm.keySet()) {
            List<CsvLine> list = hm.get(hmKey);
            String ctcTermEnglishText = list.get(0).getCtcTerm();
            Integer proCtcTermSystemId = list.get(0).getProctcTermSystemId();
            String proCtcTermEnglishText = hmKey;
            CtcTerm ctcTerm = findCtcTermFromRepository(ctcTermRepository, ctcTermEnglishText, proCtcTermEnglishText);
            if (ctcTerm != null) {
            	ProCtcTerm proCtcTerm = createProCtcTerm(proCtc, ctcTerm, proCtcTermEnglishText, proCtcTermSystemId);
            	proCtc.addProCtcTerm(proCtcTerm);
                for (CsvLine hmValue : list) {
                	ProCtcQuestion proCtcQuestion = createProCtcQuestion(proCtcTerm, hmValue.getQuestionText(), hmValue.getDisplayOrder(), hmValue.getQuestionType());
                	proCtcTerm.setProCtcSystemId(hmValue.getProctcTermSystemId());
                    proCtcTerm.addProCtcQuestion(proCtcQuestion);
                    proCtcTerm.setCore(hmValue.isCoreItem());
                    if (hmValue.getGender() != null) {
                        proCtcTerm.setGender(hmValue.getGender());
                    }
                    addProCtcValidValuesForProCtcQuestion(hmValue.getProctcValidValues(), proCtcQuestion);
                    if (new Integer(hmValue.getDisplayOrder()).intValue() == 1) {
                        firstQuestions.put(hmValue.getProctcTerm(), proCtcQuestion);
                    }
                    createQuestionDisplayRule(firstQuestions, proCtcQuestion, hmValue.getDisplayOrder(), hmValue.getProctcTerm());
                    proCtcTermRepository.save(proCtcTerm);
                }
            }
            else{
            	log.error("Could not find CtcTerm for: "+ proCtcTermEnglishText);
            }
        }
        return proCtc;
    }
   
   public void addToProCtcTermHashMap(HashMap<String, List<CsvLine>> hm, CsvLine csvLine){
   	if (hm.containsKey(csvLine.getProctcTerm())) {
   		List<CsvLine> list = hm.get(csvLine.getProctcTerm());
           list.add(csvLine);
       } else {
           List<CsvLine> list = new ArrayList<CsvLine>();
           list.add(csvLine);
           hm.put(csvLine.getProctcTerm(), list);
       }
   }
   
   private CtcTerm findCtcTermFromRepository(CtcTermRepository ctcTermRepository, String ctcTermEnglishText, String proCtcTermEnglishText){
  	 CtcQuery ctcQuery = new CtcQuery();
       ctcQuery.filterByName(ctcTermEnglishText);
       List<CtcTerm> ctcTerms = ctcTermRepository.find(ctcQuery);
       CtcTerm CtcTerm = null;
       if (ctcTerms.size() == 0) {
           log.error("Could not find ctc term for " + proCtcTermEnglishText + ". Skipping " + proCtcTermEnglishText);
           return null;
       } else {
           if (ctcTerms.size() > 1) {
        	   log.error("Multiple ctc terms found for " + proCtcTermEnglishText + ". Skipping " + proCtcTermEnglishText);
               return null;
           } else {
               CtcTerm = ctcTerms.get(0);
           }
       }
       return CtcTerm;
  }
   
   private void addProCtcValidValuesForProCtcQuestion(String proCtcValidValueString, ProCtcQuestion proCtcQuestion){
       StringTokenizer st1 = new StringTokenizer(proCtcValidValueString, "/");
       int j = 0;
       while (st1.hasMoreTokens()) {
           ProCtcValidValue proCtcValidValue = new ProCtcValidValue();
           String responseText = st1.nextToken().trim();
           proCtcValidValue.setValue(responseText, SupportedLanguageEnum.ENGLISH);
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
   }
   
   private ProCtcQuestion createProCtcQuestion(ProCtcTerm proCtcTerm, String QuestionTextEnglish, String displayOrder, String questionType){
   	ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
       proCtcQuestion.setQuestionText(QuestionTextEnglish, SupportedLanguageEnum.ENGLISH);
       proCtcQuestion.setDisplayOrder(new Integer(displayOrder));
       proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.getByDisplayName(questionType));
       proCtcQuestion.setProCtcTerm(proCtcTerm);
       return proCtcQuestion;
   }
   
   private ProCtcTerm createProCtcTerm(ProCtc proCtc, CtcTerm CtcTerm, String proCtcTermEnglishText, Integer proCtcTermSystemId){
  	 ProCtcTerm proCtcTerm = new ProCtcTerm();
       ProCtcTermVocab proCtcTermVocab = new ProCtcTermVocab(proCtcTerm);
       proCtcTerm.setProCtcTermVocab(proCtcTermVocab);
       proCtcTerm.getProCtcTermVocab().setTermEnglish(proCtcTermEnglishText);
       proCtcTerm.setCtcTerm(CtcTerm);
       proCtcTerm.setProCtc(proCtc);
       proCtcTerm.setProCtcSystemId(proCtcTermSystemId);
       return proCtcTerm;
  }
   

   private void createQuestionDisplayRule(HashMap<String, ProCtcQuestion> firstQuestions, ProCtcQuestion proCtcQuestion, String displayOrder, String proCtcTerm){
   	 if (new Integer(displayOrder) > 1) {
            int i = 0;
            ProCtcQuestion firstQuestion = firstQuestions.get(proCtcTerm);
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
   
   @Required
   public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
       this.proCtcTermRepository = proCtcTermRepository;
   }
   
   @Required
   public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
       this.ctcTermRepository = ctcTermRepository;
   }

}
