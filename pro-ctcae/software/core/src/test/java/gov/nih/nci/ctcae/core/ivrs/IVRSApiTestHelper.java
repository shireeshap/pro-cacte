package gov.nih.nci.ctcae.core.ivrs;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;


/**
 * The Class IVRSApiTestHelper.
 *
 * @author Suneel Allareddy
 * @since Jan 11, 2011
 */
public class IVRSApiTestHelper {
    private SimpleJdbcCall ivrsLoginFunction;
    private JdbcTemplate jdbcTemplate;
    DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer ivrsLogin(String userNumber, int pin) {
        String procedureName = "ivrs_login";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("usernumber", Types.VARCHAR),
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

    public String ivrsGetFirstQuestion(int userId, int formId) {
        String procedureName = "ivrs_getfirstquestion";
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

    public String ivrsGetAnswerQuestion(int userId, int formId, int questionId, int answerNum,int questioncategory) {
        String procedureName = "ivrs_answerquestion";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER),
                new SqlParameter("answernum", Types.INTEGER),
                new SqlParameter("questioncategory",Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId)
                .addValue("answernum", answerNum)
                .addValue("questioncategory",questioncategory);
        String result = ivrsLoginFunction.executeFunction(String.class, in);
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

    public String ivrsGetPreviousQuestion(int userId, int formId, int questionId,int questionCategory) {
        String procedureName = "ivrs_getpreviousquestion";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER),
                new SqlParameter("questioncategory", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId)
                .addValue("questioncategory",questionCategory);
        String result = ivrsLoginFunction.executeFunction(String.class, in);
        System.out.println(result);
        return result;
    }

     public Integer ivrsGetQuestionAnswer(int userId, int formId, int questionId,int questionCategory) {
        String procedureName = "ivrs_getquestionanswer";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("questionid", Types.INTEGER),
                new SqlParameter("questioncategory",Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("questionid", questionId)
                .addValue("questioncategory",questionCategory);
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
    public Integer ivrsDeleteAddedQuestions(int userId, int formId) {
        String procedureName = "ivrs_deleteaddedquestions";
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
    public Integer ivrsAnswerCoreSymptom(int userId, int formId,int symptomID, int answer,int coreCategory){
        String procedureName = "ivrs_answercoresymptom";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("symptomid", Types.INTEGER),
                new SqlParameter("answer", Types.INTEGER),
                new SqlParameter("corecategory", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("symptomid",symptomID)
                .addValue("answer",answer)
                .addValue("corecategory",coreCategory);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetSymptomQuestionAnswer(int userId, int formId,int symptomID, int answer,int coreCategory){
        String procedureName = "ivrs_get_sym_ques_ans";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("symptomID", Types.INTEGER),
                new SqlParameter("answer", Types.INTEGER),
                new SqlParameter("coreCategory", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("symptomID",symptomID)
                .addValue("answer",answer)
                .addValue("coreCategory",coreCategory);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetCoreSymptomFileName(int userId, int formId,int symptomID){
        String procedureName = "ivrs_getcoresymptomfilename";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("symptomID", Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("symptomID",symptomID);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }

    public Integer ivrsGetCoreSymptomID(int userId, int formId){
        String procedureName = "ivrs_getcoresymptomid";
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

    public String ivrsGetPreviousCoreSymptomID(int userId, int formId,int symptomID){
        String procedureName = "ivrs_getprevious_coresymid";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.VARCHAR),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("symptomid",Types.INTEGER));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("symptomid",symptomID);
        String result = ivrsLoginFunction.executeFunction(String.class, in);
        System.out.println(result);
        return result;
    }
       
    public Integer ivrsGetFormStatus(int userId, int formId){
        String procedureName = "ivrs_getform_status";
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

    public Integer ivrsGetNumberOfUnAnsSymps(int userId, int formId){
        String procedureName = "ivrs_get_no_of_unans_symps";
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

    public Integer ivrsSaveFilePath(int userId, int formId,String path){
        String procedureName = "ivrs_save_filepath";
        ivrsLoginFunction = new SimpleJdbcCall(dataSource)
                .withFunctionName(procedureName);
        ivrsLoginFunction.setAccessCallParameterMetaData(false);
        ivrsLoginFunction.declareParameters(
                new SqlOutParameter("RETURN", Types.INTEGER),
                new SqlParameter("userid", Types.INTEGER),
                new SqlParameter("formid", Types.INTEGER),
                new SqlParameter("filepath",Types.VARCHAR));
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("userid", userId)
                .addValue("formid", formId)
                .addValue("filepath",path);
        Integer result = ivrsLoginFunction.executeFunction(Integer.class, in);
        System.out.println(result);
        return result;
    }
    
    //invoked by the ivrsApiTest to insert dummy strings for filenames against every qs in pro-ctc_questions table
    //the file names are mandated by the stored functions to work properly
    public void ivrsUpdateFileNames(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("update pro_ctc_questions set question_file_name='x' where question_file_name is null");
        return;
    }
    
}
