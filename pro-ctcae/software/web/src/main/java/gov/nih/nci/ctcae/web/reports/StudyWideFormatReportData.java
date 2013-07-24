package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.SpcrfsWrapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author mehul
 * Date: Apr 15, 2011
 */
public class StudyWideFormatReportData{
	private String STUDY_ID = "2";
	
	JdbcTemplate jdbcTemplate;

    public List<SpcrfsWrapper> getSchedulesOnly(Integer studyId) {
    	
    	String fetchSchedulesOnly = "SELECT " +
    			"distinct spcrfs.id, spcrfs.study_participant_crf_id, spcrfs.start_date, spcrfs.due_date, " +
    			"spcrfs.status, spcrfs.is_holiday, spcrfs.cycle_number, spcrfs.cycle_day,spcrfs. week_in_study, " +
    			"spcrfs.month_in_study,  spcrfs.baseline, spcrfs.form_submission_mode, spcrfs.form_completion_date, " +
    			"spcrfs.file_path, spcrfs.verbatim, spcrfs.mark_delete, spcrfs.health_amount, spcrfs.version, crf.title " +
    			"from sp_crf_schedules spcrfs " +
    			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
    			" left join crfs crf on crf.id = spcrf.crf_id " +
    			" left join studies study on study.id = crf.study_id " +
    			"where study.id = 2 ";
    	
    	List<SpcrfsWrapper> list = jdbcTemplate.query(fetchSchedulesOnly, new StudyParticipantCrfScheduleRowMapper());
    	return list;
    }
    
 public List<SpcrfsWrapper> getResponsesOnly(Integer studyId) {
    	
    	String fetchResponsesOnly = "SELECT distinct spci.sp_crf_schedule_id as scheduleId, cpi.id, " +
    			"pcq.id, pcq.question_type, pctv.term_english, pcvvv.value_english " +
    			"from study_participant_crf_items spci" +
    			" left join crf_page_items cpi on cpi.id = spci.crf_item_id " +
    			" left join pro_ctc_questions pcq on pcq.id = cpi.pro_ctc_question_id " +
    			" left join pro_ctc_terms pct on pct.id = pcq.pro_ctc_term_id " +
    			" left join pro_ctc_terms_vocab pctv on pctv.pro_ctc_terms_id = pct.id " +
    			" left join pro_ctc_valid_values pcvv on pcvv.id = spci.pro_ctc_valid_value_id " +
    			" left join pro_ctc_valid_values_vocab pcvvv on pcvvv.pro_ctc_valid_values_id = pcvv.id" +
    			" where spci.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
    			"  left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
    			"  left join crfs crf on crf.id = spcrf.crf_id " +
    			"  where crf.study_id = 2)";
    	
    	List<SpcrfsWrapper> list = jdbcTemplate.query(fetchResponsesOnly, new StudyParticipantCrfScheduleRowMapper());
    	return list;
    }
 
 public List<SpcrfsWrapper> getAddedProQuestions(Integer studyId) {
 	
 	String fetchAddedProQuestions = "SELECT distinct spcaq.sp_crf_schedule_id as scheduleId, pcq.id, pctv.term_english, " +
 			" pcq.question_type, pcvvv.value_english " +
 			" from sp_crf_sch_added_questions spcaq " +
 			" left join pro_ctc_questions pcq on pcq.id = spcaq.question_id " +
 			" left join pro_ctc_terms pct on pct.id = pcq.pro_ctc_term_id " +
 			" left join pro_ctc_terms_vocab pctv on pctv.pro_ctc_terms_id = pct.id " +
 			" left join pro_ctc_valid_values pcvv on pcvv.id = spcaq.pro_ctc_valid_value_id " +
 			" left join pro_ctc_valid_values_vocab pcvvv on pcvvv.pro_ctc_valid_values_id = pcvv.id " +
 			" where spcaq.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
 			"  left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
 			"  left join crfs crf on crf.id = spcrf.crf_id " +
 			"  where crf.study_id = 2)";
 	
 	List<SpcrfsWrapper> list = jdbcTemplate.query(fetchAddedProQuestions, new StudyParticipantCrfScheduleRowMapper());
 	return list;
 }
 
 public List<SpcrfsWrapper> getAddedMeddraQuestions(Integer studyId) {
	 	
	 	String fetchAddedMeddraQuestions = "SELECT distinct spcaq.sp_crf_schedule_id as scheduleId, mq.id, " +
	 			" lltv.meddra_term_english, mq.question_type, mvvv.value_english " +
	 			" from sp_crf_sch_added_questions spcaq " +
	 			" left join meddra_questions mq on mq.id = spcaq.meddra_question_id " +
	 			" left join meddra_llt llt on llt.id = mq.meddra_llt_id " +
	 			" left join meddra_llt_vocab lltv on lltv.meddra_llt_id = llt.id  " +
	 			" left join meddra_valid_values mvv on mvv.id = spcaq.meddra_valid_value_id " +
	 			" left join meddra_valid_values_vocab mvvv on mvvv.meddra_valid_values_id = mvv.id " +
	 			" where spcaq.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
	 			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 			" left join crfs crf on crf.id = spcrf.crf_id " +
	 			" where crf.study_id = 2)";
	 	
	 	List<SpcrfsWrapper> list = jdbcTemplate.query(fetchAddedMeddraQuestions, new StudyParticipantCrfScheduleRowMapper());
	 	return list;
	 }
    
 public List<SpcrfsWrapper> getParticipantsAndOrg(Integer studyId) {
	 	
	 	String fetchParticipantsAndOrg = "SELECT distinct p.first_name, p.last_name, o.name as organizationId " +
	 			" from sp_crf_schedules spcrfs " +
	 			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 			" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " +
	 			" left join participants p on p.id = spa.participant_id " +
	 			" left join study_organizations so on so.study_id = 2 " +
	 			" left join organizations o on o.id = so.organization_id " +
	 			" where spcrfs.id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
	 			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 			" left join crfs crf on crf.id = spcrf.crf_id " +
	 			" where crf.study_id = 2)";
	 	
	 	List<SpcrfsWrapper> list = jdbcTemplate.query(fetchParticipantsAndOrg, new StudyParticipantCrfScheduleRowMapper());
	 	return list;
	 }
 
    
    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
    	this.jdbcTemplate = jdbcTemplate;
    }
}

