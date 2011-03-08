package gov.nih.nci.ctcae.core.ivrs;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.sql.Types;


/**
 * The Class IVRSApiTestHelper.
 *
 * @author Suneel Allareddy
 * @since Jan 11, 2011
 */
public class IVRSApiTestHelper {
    private SimpleJdbcCall ivrsLoginFunction;

    DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer ivrsLogin(int userNumber, int pin) {
        String procedureName = "ivrs_login";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("usernumber", Types.INTEGER),
                new SqlParameter("pin", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("usernumber", userNumber)
                .addValue("pin", pin);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsNumberOfForms(int userId) {
        String procedureName = "ivrs_numberforms";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetForm(int userId, int formNum) {
        String procedureName = "ivrs_getform";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formnum", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formnum", formNum);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public String ivrsGetformtitle(int userId, int formId) {
        String procedureName = "ivrs_getformtitle";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId);
        String result = ivrsLoginFunction.executeFunction(String.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetFirstQuestion(int userId, int formId) {
        String procedureName = "ivrs_getfirstquestion";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetAnswerQuestion(int userId, int formId, int questionId, int answerNum) {
        String procedureName = "ivrs_answerquestion";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER),
                new SqlParameter("answernum", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId)
                .addValue("answernum", answerNum);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public String ivrsGetQuestionText(int userId, int formId, int questionId) {
        String procedureName = "ivrs_getquestiontext";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId);
        String result = ivrsLoginFunction.executeFunction(String.class, in);
        System.out.println(result);
        return result;
    }

    public String ivrsGetQuestionType(int userId, int formId, int questionId) {
        String procedureName = "ivrs_getquestiontype";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId);
        String result = ivrsLoginFunction.executeFunction(String.class, in);
        System.out.println(result);
        return result;
    }

    public String ivrsGetQuestionFile(int userId, int formId, int questionId) {
        String procedureName = "ivrs_getquestionfile";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId);
        String result = ivrsLoginFunction.executeFunction(String.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetPreviousQuestion(int userId, int formId, int questionId) {
        String procedureName = "ivrs_getpreviousquestion";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

     public Integer ivrsGetQuestionAnswer(int userId, int formId, int questionId) {
        String procedureName = "ivrs_getquestionanswer";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsIsUserNew(int userId) {
        String procedureName = "ivrs_isUserNew";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetFormRecallPeriod(int userId, int formId) {
        String procedureName = "ivrs_getFormRecallPeriod";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsCommitSession(int userId, int formId, int pin) {
        String procedureName = "ivrs_commitsession";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("pin", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("pin", pin);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

}
