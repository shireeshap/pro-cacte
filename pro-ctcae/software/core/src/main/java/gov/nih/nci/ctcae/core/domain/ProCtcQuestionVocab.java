package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The Class ProCtcQuestionVocab.
 *
 * @author Vinay Gangoli
 */

@Entity
@Table(name = "pro_ctc_questions_vocab")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_pro_ctc_questions_vocab_id")})
public class ProCtcQuestionVocab extends BasePersistable {


    protected static transient final Log logger = LogFactory.getLog(ProCtcQuestionVocab.class);

	public ProCtcQuestionVocab(){
		super();
	}
	
	public ProCtcQuestionVocab(ProCtcQuestion proCtcQuestion){
		super();
		this.proCtcQuestion = proCtcQuestion;
	}
	
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "question_text_english", nullable = false)
    private String questionTextEnglish;

    @Column(name = "question_text_spanish", nullable = true)
    private String questionTextSpanish;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id", name = "pro_ctc_questions_id", nullable = false)
    @Cascade({CascadeType.SAVE_UPDATE})
    private ProCtcQuestion proCtcQuestion; 
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return questionTextEnglish;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcQuestionVocab)) return false;

        ProCtcQuestionVocab that = (ProCtcQuestionVocab) o;
        if (questionTextEnglish != null ? !questionTextEnglish.equals(that.questionTextEnglish) : that.questionTextEnglish != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (questionTextEnglish != null ? questionTextEnglish.hashCode() : 0);
        result = 31 * result;
        result = 31 * result;
        return result;
    }
	
    public String getQuestionTextEnglish() {
        if (questionTextEnglish.indexOf(":") != -1) {
        	questionTextEnglish = questionTextEnglish.substring(0, questionTextEnglish.indexOf(":"));
        }
        questionTextEnglish = questionTextEnglish.replaceAll("\\?", "");
        return questionTextEnglish;
    }

	public void setQuestionTextEnglish(String questionTextEnglish) {
		this.questionTextEnglish = questionTextEnglish;
	}

	public String getQuestionTextSpanish() {
        if(questionTextSpanish == null){
            logger.debug("##########Could not find spanish translation for Question: "+ questionTextEnglish);
            questionTextSpanish = this.questionTextEnglish;
        }
        if ( questionTextSpanish.indexOf(":") != -1) {
            questionTextSpanish = questionTextSpanish.substring(0, questionTextSpanish.indexOf(":"));
        }
        questionTextSpanish = questionTextSpanish.replaceAll("\\?", "");
        return questionTextSpanish;
	}

	public void setQuestionTextSpanish(String questionTextSpanish) {
		this.questionTextSpanish = questionTextSpanish;
	}

	public ProCtcQuestion getProCtcQuestion() {
		return proCtcQuestion;
	}

	public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
		this.proCtcQuestion = proCtcQuestion;
	}

}
