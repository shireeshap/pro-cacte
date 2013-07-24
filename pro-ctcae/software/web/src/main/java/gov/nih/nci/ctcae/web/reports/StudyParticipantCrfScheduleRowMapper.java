package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyWideFormatWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

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


	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		/*StudyWideFormatWrapper survery = new StudyWideFormatWrapper();
		survery.setId(rs.getInt(ID));
		survery.setStudyParticipantCrf(rs.getInt(STUDY_PARTICIPANT_CRF_ID));
		survery.setStartDate(rs.getDate(START_DATE));
		survery.setDueDate(rs.getDate(DUE_DATE));*/
		return null;
	}

}
