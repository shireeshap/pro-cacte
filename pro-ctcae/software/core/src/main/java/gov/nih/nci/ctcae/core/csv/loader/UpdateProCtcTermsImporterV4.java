package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.csvreader.CsvReader;
import org.springframework.core.io.ClassPathResource;

/**
 * @author mehul gulati
 * Date: Jun 28, 2010
 */
public class UpdateProCtcTermsImporterV4 {
    ProCtcQuestionRepository proCtcQuestionRepository;

    private static final String QUESTION_TEXT = "PRO-CTCAE Wording";
    private static final String PRO_CTC_TERM = "PRO-CTCAE Term";
    private static final String CORE_ITEM = "Core Item";
    private static final String QUESTION_TYPE = "Attribute";
 
    public void updateProCtcTerms() throws IOException {
        CsvReader reader;

        ClassPathResource classPathResource = new ClassPathResource("Update_ProCtcTerms_V4.csv");
        reader = new CsvReader(classPathResource.getInputStream(), Charset.forName("ISO-8859-1"));
        reader.readHeaders();

        while (reader.readRecord()) {

            String question = reader.get(QUESTION_TEXT).trim();
            String proCtcTerm = reader.get(PRO_CTC_TERM).trim();
            String core = reader.get(CORE_ITEM).trim();
            String attribute = reader.get(QUESTION_TYPE).trim();
            String firstLetter = question.substring(0, 1);
            question = firstLetter.toUpperCase() + question.substring(1);
            String questionType = attribute.substring(attribute.indexOf('-') + 1);

            ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
            proCtcQuestionQuery.filterByQuestionType(ProCtcQuestionType.getByCode(questionType));
            proCtcQuestionQuery.filterByTerm(proCtcTerm);
            List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
            if (proCtcQuestions != null && proCtcQuestions.size() > 0) {
                ProCtcQuestion ctcQuestion = proCtcQuestions.get(0);
                ctcQuestion.setQuestionText(question);
                proCtcQuestionRepository.save(ctcQuestion);
            }
        }

    }

    public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository) {
        this.proCtcQuestionRepository = proCtcQuestionRepository;
    }
}
