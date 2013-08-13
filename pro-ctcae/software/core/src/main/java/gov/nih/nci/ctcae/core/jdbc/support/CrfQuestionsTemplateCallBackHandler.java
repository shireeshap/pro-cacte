package gov.nih.nci.ctcae.core.jdbc.support;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @author AmeyS
 * CrfQuestionsTemplateCallBackHandler class. 
 * Used in overall study report generation.
 */
public class CrfQuestionsTemplateCallBackHandler implements RowCallbackHandler {
	
	public Map<String, List<CrfQuestionsTemplateWrapper>> map;
	public static String CRF_TITLE = "crfTitle";
	public static String QUESTION_TYPE = "questionType";
	public static String TERM_ENGLISH = "termEnglish";
	
	public CrfQuestionsTemplateCallBackHandler(Map<String, List<CrfQuestionsTemplateWrapper>> map){
		this.map = map;
	}
	
	@Override
	public void processRow(ResultSet rs) throws SQLException {
		List<CrfQuestionsTemplateWrapper> records;
		records = map.get(rs.getString(CRF_TITLE));
		if(records == null){
			records = new ArrayList<CrfQuestionsTemplateWrapper>();
			map.put(rs.getString(CRF_TITLE), records);
		}
		CrfQuestionsTemplateWrapper wrapper = new CrfQuestionsTemplateWrapper();
		wrapper.setCrfTitle(rs.getString(CRF_TITLE));
		wrapper.setTermEnglish(rs.getString(TERM_ENGLISH));
		wrapper.setQuestionType(ProCtcQuestionType.getProCtcQuestionType(rs.getString(QUESTION_TYPE)));
		records.add(wrapper);
	}

}
