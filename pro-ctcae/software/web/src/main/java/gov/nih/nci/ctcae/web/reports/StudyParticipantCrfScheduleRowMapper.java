package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.SpcrfsWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author AmeyS
 * StudyParticipantCrfScheduleRowMapper class. 
 * Used in overall study report generation.
 */
public class StudyParticipantCrfScheduleRowMapper implements RowMapper {
	
	private static String ID = "id";
	private static String STUDY_PARTICIPANT_CRF_ID = "study_participant_crf_id";
	private static String START_DATE = "start_date";
	private static String DUE_DATE = "due_date";
	private static String STATUS = "status";
	private static String IS_HOLIDAY = "is_holiday";
	private static String CYCLE_NUMBER = "cycle_number";
	private static String CYCLE_DAY = "cycle_day";
	private static String WEEK_IN_STUDY = "week_in_study";
	private static String MONTH_IN_STUDY = "month_in_study";
	private static String BASELINE = "baseline";
	private static String FORM_SUBMISSION_MODE = "form_submission_mode";
	private static String FORM_COMPLETION_DATE = "form_completion_date";
	private static String FILE_PATH = "file_path";
	private static String VERBATIM = "verbatim";
	private static String MARK_DELETE = "mark_delete";
	private static String HEALTH_AMOUNT = "health_amount";
	private static String CRF_TITLE = "title";

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		SpcrfsWrapper survey = new SpcrfsWrapper();
		survey.setId(rs.getInt(ID));
		survey.setStudyParticipantCrf(rs.getInt(STUDY_PARTICIPANT_CRF_ID));
		survey.setStartDate(rs.getDate(START_DATE));
		survey.setDueDate(rs.getDate(DUE_DATE));
		survey.setStatus(CrfStatus.getCrfStatus(rs.getString(STATUS)));
		survey.setHoliday(rs.getBoolean(IS_HOLIDAY));
		survey.setCycleNumber(rs.getInt(CYCLE_DAY));
		survey.setCycleDay(rs.getInt(CYCLE_DAY));
		survey.setWeekInStudy(rs.getInt(WEEK_IN_STUDY));
		survey.setMonthInStudy(rs.getInt(MONTH_IN_STUDY));
		survey.setBaseline(rs.getBoolean(BASELINE));
		survey.setFormSubmissionMode(AppMode.getAppMode(rs.getString(FORM_SUBMISSION_MODE)));
		survey.setCompletionDate(rs.getDate(FORM_COMPLETION_DATE));
		survey.setFilePath(rs.getString(FILE_PATH));
		survey.setVerbatim(rs.getString(VERBATIM));
		survey.setMarkDelete(rs.getBoolean(MARK_DELETE));
		survey.setHealthAmount(rs.getInt(HEALTH_AMOUNT));
		survey.setCrfTitle(rs.getString(CRF_TITLE));
		return survey;
	}
	
}
