package gov.nih.nci.ctcae.core.jdbc;

import gov.nih.nci.ctcae.core.jdbc.support.AddedMeddraQuestionCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.AddedMeddraQuestionWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.AddedProCtcQuestionCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.AddedProCtcQuestionWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.FirstResponseDateCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.ParticipantAndOganizationWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ParticipantAndOrganizationCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.ResponseModesCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.ResponseWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ResponsesCallBackHandler;
import gov.nih.nci.ctcae.core.jdbc.support.SpcrfsWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.StudyParticipantCrfScheduleRowMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Amey Sane
 * Date: July 30, 2013
 * StudyWideFormatReportData class.
 * Used to fetch data for Wide format Overall Study report
 */
public class StudyWideFormatReportDao{
	
	JdbcTemplate jdbcTemplate;

    public List<SpcrfsWrapper> getSchedulesOnly(Integer studyId, List<Integer> crfIds, Integer studySiteId) {
    	String fetchSchedulesOnly = "SELECT " +
    			"distinct spcrfs.id, spcrfs.study_participant_crf_id, spcrfs.start_date, spcrfs.due_date, " +
    			"spcrfs.status, spcrfs.is_holiday, spcrfs.cycle_number, spcrfs.cycle_day,spcrfs. week_in_study, " +
    			"spcrfs.month_in_study,  spcrfs.baseline, spcrfs.form_submission_mode, spcrfs.form_completion_date, " +
    			"spcrfs.file_path, spcrfs.verbatim, spcrfs.mark_delete, spcrfs.health_amount, spcrfs.version, crf.title " +
    			"from sp_crf_schedules spcrfs " +
    			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
    			(studySiteId != null?" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " : "") +
    			" left join crfs crf on crf.id = spcrf.crf_id " +
    			" where " +
    			(studyId != null? " crf.study_id = " + studyId + " " : "") + 
    			(crfIds != null? " and crf.id in (" + StringUtils.join(crfIds, ", ") + ") " : "") +
    			(studySiteId != null? " and spa.study_site_id = " + studySiteId + " " : "");
    	
    	List<SpcrfsWrapper> list = jdbcTemplate.query(fetchSchedulesOnly, new StudyParticipantCrfScheduleRowMapper());
    	return list;
    }
    
 public Map<Integer, List<ResponseWrapper>> getResponsesOnly(Integer studyId, List<Integer> crfIds, Integer studySiteId) {
    	
    	String fetchResponsesOnly = "SELECT distinct spci.sp_crf_schedule_id as scheduleId, cpi.id as crfPageItemId, " +
    			"pcq.id as proQuestionId, pcq.display_order as questionPosition, pct.gender, spci.response_date as responseDate, pcq.question_type, pctv.term_english, pcvvv.value_english," +
    			"pcvv.response_code as responseCode " +
    			"from study_participant_crf_items spci" +
    			" left join crf_page_items cpi on cpi.id = spci.crf_item_id " +
    			" left join pro_ctc_questions pcq on pcq.id = cpi.pro_ctc_question_id " +
    			" left join pro_ctc_terms pct on pct.id = pcq.pro_ctc_term_id " +
    			" left join pro_ctc_terms_vocab pctv on pctv.pro_ctc_terms_id = pct.id " +
    			" left join pro_ctc_valid_values pcvv on pcvv.id = spci.pro_ctc_valid_value_id " +
    			" left join pro_ctc_valid_values_vocab pcvvv on pcvvv.pro_ctc_valid_values_id = pcvv.id" +
    			" where spci.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
    			"  left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
    			(studySiteId != null?" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " : "") +
    			" left join crfs crf on crf.id = spcrf.crf_id " +
    			" where " +
    			(studyId != null? " crf.study_id = " + studyId + " " : "") + 
    			(crfIds != null? " and crf.id in (" + StringUtils.join(crfIds, ", ") + ") " : "") +
    			(studySiteId != null? " and spa.study_site_id = " + studySiteId + " " : "") +
    			")";
    	
    	Map<Integer, List<ResponseWrapper>> map = new HashMap<Integer, List<ResponseWrapper>>();
    	jdbcTemplate.query(fetchResponsesOnly, new ResponsesCallBackHandler(map));
    	return map;
    }
 
 public Map<Integer, List<AddedProCtcQuestionWrapper>> getAddedProQuestions(Integer studyId, List<Integer> crfIds, Integer studySiteId) {
 	
 	String fetchAddedProQuestions = "SELECT distinct spcaq.sp_crf_schedule_id as scheduleId, pcq.id as proQuestionId, pcq.display_order as questionPosition, " +
 			"pct.gender, pctv.term_english, pcq.question_type, pcvvv.value_english, pcvv.response_code as responseCode " +
 			" from sp_crf_sch_added_questions spcaq " +
 			" left join pro_ctc_questions pcq on pcq.id = spcaq.question_id " +
 			" left join pro_ctc_terms pct on pct.id = pcq.pro_ctc_term_id " +
 			" left join pro_ctc_terms_vocab pctv on pctv.pro_ctc_terms_id = pct.id " +
 			" left join pro_ctc_valid_values pcvv on pcvv.id = spcaq.pro_ctc_valid_value_id " +
 			" left join pro_ctc_valid_values_vocab pcvvv on pcvvv.pro_ctc_valid_values_id = pcvv.id " +
 			" where spcaq.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
 			"  left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
 			(studySiteId != null?" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " : "") +
 			"  left join crfs crf on crf.id = spcrf.crf_id " +
 			" where " +
			(studyId != null? " crf.study_id = " + studyId + " " : "") + 
			(crfIds != null? " and crf.id in (" + StringUtils.join(crfIds, ", ") + ") " : "") +
			(studySiteId != null? " and spa.study_site_id = " + studySiteId + " " : "") +
 			 ") and spcaq.question_id IS NOT NULL ";
 	
 	Map<Integer, List<AddedProCtcQuestionWrapper>> map = new HashMap<Integer, List<AddedProCtcQuestionWrapper>>();
 	jdbcTemplate.query(fetchAddedProQuestions, new AddedProCtcQuestionCallBackHandler(map));
 	return map;
 }
 
 public Map<Integer, List<AddedMeddraQuestionWrapper>> getAddedMeddraQuestions(Integer studyId, List<Integer> crfIds, Integer studySiteId) {
	 	
	 	String fetchAddedMeddraQuestions = "SELECT distinct spcaq.sp_crf_schedule_id as scheduleId, mq.id as meddraQuestionId, " +
	 			" lltv.meddra_term_english, mq.question_type, mvvv.value_english, mvv.display_order as displayOrder " +
	 			" from sp_crf_sch_added_questions spcaq " +
	 			" left join meddra_questions mq on mq.id = spcaq.meddra_question_id " +
	 			" left join meddra_llt llt on llt.id = mq.meddra_llt_id " +
	 			" left join meddra_llt_vocab lltv on lltv.meddra_llt_id = llt.id  " +
	 			" left join meddra_valid_values mvv on mvv.id = spcaq.meddra_valid_value_id " +
	 			" left join meddra_valid_values_vocab mvvv on mvvv.meddra_valid_values_id = mvv.id " +
	 			" where spcaq.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
	 			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 			(studySiteId != null?" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " : "") +
	 			" left join crfs crf on crf.id = spcrf.crf_id " +
	 			" where " +
				(studyId != null? " crf.study_id = " + studyId + " " : "") + 
				(crfIds != null? " and crf.id in (" + StringUtils.join(crfIds, ", ") + ") " : "") +
				(studySiteId != null? " and spa.study_site_id = " + studySiteId + " " : "") +
	 			") and spcaq.meddra_question_id IS NOT NULL ";
	 	
	 	
	 	Map<Integer, List<AddedMeddraQuestionWrapper>> map = new HashMap<Integer, List<AddedMeddraQuestionWrapper>>();
	 	jdbcTemplate.query(fetchAddedMeddraQuestions, new AddedMeddraQuestionCallBackHandler(map));
	 	return map;
	 }
    
 public Map<Integer, List<ParticipantAndOganizationWrapper>> getParticipantsAndOrg(Integer studyId, List<Integer> crfIds, Integer studySiteId) {
	 	
	 	String fetchParticipantsAndOrg = "SELECT distinct spcrfs.id as scheduleId, p.id as participantId, spa.study_participant_identifier, " +
	 			" p.first_name, p.last_name, p.gender, o.name as organizationName " +
	 			" from sp_crf_schedules spcrfs " +
	 			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 			" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " +
	 			" left join participants p on p.id = spa.participant_id " +
	 			" left join study_organizations so on so.id = spa.study_site_id " +
	 			" left join organizations o on o.id = so.organization_id " +
	 			" where spcrfs.id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
	 			" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 			(studySiteId != null?" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " : "") +
	 			" left join crfs crf on crf.id = spcrf.crf_id " +
			 	" where " +
				(studyId != null? " crf.study_id = " + studyId + " " : "") + 
				(crfIds != null? " and crf.id in (" + StringUtils.join(crfIds, ", ") + ") " : "") +
				(studySiteId != null? " and spa.study_site_id = " + studySiteId + " " : "") +
				")";
	 			
	 	
	 	Map<Integer, List<ParticipantAndOganizationWrapper>> map = new HashMap<Integer, List<ParticipantAndOganizationWrapper>>();
	 	jdbcTemplate.query(fetchParticipantsAndOrg, new ParticipantAndOrganizationCallBackHandler(map));
	 	return map;
	 }
 
 public Map<Integer, Date> getFirstResponseDate(Integer studyId, List<Integer> crfIds, Integer studySiteId){
	 String fetchFirstResponseDate = " Select spci.sp_crf_schedule_id as scheduleId, min(spci.response_date) as firstResponseDate from study_participant_crf_items spci " +
	 		" where spci.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
	 		" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 		(studySiteId != null?" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " : "") +
	 		" left join crfs crf on crf.id = spcrf.crf_id " +
	 		" where " +
			(studyId != null? " crf.study_id = " + studyId + " " : "") + 
			(crfIds != null? " and crf.id in (" + StringUtils.join(crfIds, ", ") + ") " : "") +
			(studySiteId != null? " and spa.study_site_id = " + studySiteId + " " : "") +
	 		" ) " +
	 		" group by spci.sp_crf_schedule_id ";
	 Map<Integer, Date> map = new HashMap<Integer, Date>();
	 jdbcTemplate.query(fetchFirstResponseDate, new FirstResponseDateCallBackHandler(map));
	 return map; 
 }
 
 public Map<Integer, String> getResponseModes(Integer studyId, List<Integer> crfIds, Integer studySiteId){
	 String fetchFirstResponseDate = " Select spci.sp_crf_schedule_id as scheduleId, spci.response_mode as responseMode from study_participant_crf_items spci " +
	 		" where spci.sp_crf_schedule_id in (SELECT distinct spcrfs.id from sp_crf_schedules spcrfs " +
	 		" left join study_participant_crfs spcrf on spcrf.id = spcrfs.study_participant_crf_id " +
	 		(studySiteId != null?" left join study_participant_assignments spa on spa.id = spcrf.study_participant_id " : "") +
	 		" left join crfs crf on crf.id = spcrf.crf_id " +
	 		" where " +
			(studyId != null? " crf.study_id = " + studyId + " " : "") + 
			(crfIds != null? " and crf.id in (" + StringUtils.join(crfIds, ", ") + ") " : "") +
			(studySiteId != null? " and spa.study_site_id = " + studySiteId + " " : "") +
	 		" ) ";
	 Map<Integer, String> map = new HashMap<Integer, String>();
	 jdbcTemplate.query(fetchFirstResponseDate, new ResponseModesCallBackHandler(map));
	 return map; 
 }
 
    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
    	this.jdbcTemplate = jdbcTemplate;
    }
}

