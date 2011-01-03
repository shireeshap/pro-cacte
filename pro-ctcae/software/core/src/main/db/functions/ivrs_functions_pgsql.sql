--Login function returns user id on successful loign
CREATE OR REPLACE FUNCTION ivrs_login(userNumber integer, pin integer) RETURNS integer AS $$
DECLARE
   v_ret_user_id integer := 0;
BEGIN

     SELECT user_id INTO v_ret_user_id from participants where user_number=userNumber and pin_number=pin;
     IF NOT FOUND THEN
	return -2;
     END IF;
     return v_ret_user_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION ivrs_login(integer, integer) OWNER TO ctcae;

-- Ivrs_NumberForms function returns number of scheduled forms avilable for the user
CREATE OR REPLACE FUNCTION Ivrs_NumberForms(userid integer) RETURNS integer AS $$
DECLARE
    v_ret_count integer :=0;
BEGIN

    SELECT count(*) INTO v_ret_count
    from sp_crf_schedules spcs JOIN (study_participant_crfs spc JOIN
    (study_participant_assignments sp JOIN participants p
    ON (sp.participant_id = p.id) AND p.user_id=userid)
    ON (spc.study_participant_id= sp.id))
    ON (spcs.study_participant_crf_id=spc.id AND spcs.start_date <=current_date and (upper(spcs.status) = 'SCHEDULED' OR upper(spcs.status)= 'INPROGRESS'));

     return v_ret_count;
EXCEPTION
    WHEN OTHERS THEN
    return 0;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION Ivrs_NumberForms(integer) OWNER TO ctcae;

--Ivrs_GetForm returns the form id for corresponding form number
CREATE OR REPLACE FUNCTION Ivrs_GetForm(userid integer,formNum integer) RETURNS integer AS $$
DECLARE
    v_form_id integer :=0;
BEGIN

    SELECT spcs.id INTO v_form_id
    from sp_crf_schedules spcs JOIN (study_participant_crfs spc JOIN
    (study_participant_assignments sp JOIN participants p
    ON (sp.participant_id = p.id) AND p.user_id=userid)
    ON (spc.study_participant_id= sp.id))
    ON (spcs.study_participant_crf_id=spc.id AND spcs.start_date <=current_date and (upper(spcs.status) = 'SCHEDULED' OR upper(spcs.status)= 'INPROGRESS'))
    order by spcs.id LIMIT 1 OFFSET formNum-1;
     IF NOT FOUND THEN
	return -1;
     END IF;

     return v_form_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION Ivrs_GetForm(integer,integer) OWNER TO ctcae;

--IVRS_GetFormTitle function returns the form title for the given form id
CREATE OR REPLACE FUNCTION IVRS_GetFormTitle(userid integer,formid integer) RETURNS text AS $$
DECLARE
    v_form_title text :=0;
BEGIN

    select c.title INTO v_form_title from crfs c JOIN (study_participant_crfs spc JOIN
	sp_crf_schedules scs ON (spc.id=scs.study_participant_crf_id) and scs.id=formid)
	ON (c.id=spc.crf_id);
     IF NOT FOUND THEN
	return 'Not Found';
     END IF;

     return v_form_title;
EXCEPTION
    WHEN OTHERS THEN
    return 'Not Found';
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION IVRS_GetFormTitle(integer,integer) OWNER TO ctcae;

-- function returns the first question id for for given scheduled form id

CREATE OR REPLACE FUNCTION IVRS_GetFirstQuestion(userid integer,formid integer) RETURNS integer AS $$
DECLARE
    v_page_id integer := 0;
    v_gender text := '';
    v_question_id integer :=0;
    v_first_question_value integer := 0;
    v_current_answer_value integer :=0;
    v_question_skipped integer :=0;
    v_temp_value_id integer :=0;
    v_tmp_crf_page_id integer :=0;
    curs_questions CURSOR(formid integer,gender text) IS SELECT spci.crf_item_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
	from study_participant_crf_items spci join
	crf_page_items cpi ON (cpi.id= spci.crf_item_id) join crf_pages cp ON (cp.id=cpi.crf_page_id)
	join pro_ctc_terms pct ON ((pct.id=cp.pro_ctc_term_id) and (pct.gender is null or upper(pct.gender)=upper(gender) or pct.gender ='both'))
	where spci.sp_crf_schedule_id = formid order by cpi.crf_page_id,cpi.display_order;

BEGIN
	SELECT gender into v_gender FROM participants where user_id=userid;

	OPEN curs_questions(formid,v_gender);
	LOOP
	FETCH curs_questions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
	       EXIT WHEN NOT FOUND;

		IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
			v_page_id := v_tmp_crf_page_id;
			v_first_question_value := v_temp_value_id;
			IF v_first_question_value IS NULL THEN
				EXIT;
			ELSE
				SELECT count(*) INTO v_question_skipped from crf_page_item_display_rules
				where pro_ctc_valid_value_id = v_first_question_value;

			END IF;
		END IF;
		v_current_answer_value := v_temp_value_id;

		IF v_current_answer_value IS NULL AND v_question_skipped > 0 THEN
			EXIT;
		END IF;

	END LOOP;

	CLOSE curs_questions;

	IF v_page_id=0 OR v_question_id IS NULL then
		return 0;
	end if;

	return v_question_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION IVRS_GetFirstQuestion(integer,integer) OWNER TO ctcae;

--function returns the question type for the given question id
CREATE OR REPLACE FUNCTION Ivrs_GetQuestionType(userid integer,formid integer,questionid Integer) RETURNS text AS $$
DECLARE
    v_question_type Text := '';

BEGIN

    SELECT pcq.question_type INTO v_question_type from pro_ctc_questions pcq join crf_page_items cpi on
    ((pcq.id=cpi.pro_ctc_question_id) and cpi.id=questionid);

    IF NOT FOUND THEN
	return 'Data Not Found';
    END IF;
       return v_question_type;
EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION Ivrs_GetQuestionType(integer,integer,integer) OWNER TO ctcae;

-- function returns the question text for the given question id
CREATE OR REPLACE FUNCTION Ivrs_GetQuestionText(userid integer,formid integer,questionid Integer) RETURNS text AS $$
DECLARE
    v_question_text Text := '';
BEGIN

    SELECT pcq.question_text INTO v_question_text from pro_ctc_questions pcq join crf_page_items cpi on
    ((pcq.id=cpi.pro_ctc_question_id) and cpi.id=questionid);

    IF NOT FOUND THEN
	return 'Data Not Found';
    END IF;

       return v_question_text;
EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION Ivrs_GetQuestionText(integer,integer,integer) OWNER TO ctcae;

-- function returns the question file name(audio file) for the given question id

CREATE OR REPLACE FUNCTION Ivrs_GetQuestionFile(userid integer,formid integer,questionid Integer) RETURNS text AS $$
DECLARE
    v_question_file Text := '';
BEGIN

	SELECT pcq.question_file_name INTO v_question_file from pro_ctc_questions pcq join crf_page_items cpi on
	 ((pcq.id=cpi.pro_ctc_question_id) and cpi.id=questionid);
    IF NOT FOUND OR v_question_file IS NULL THEN
	v_question_file:= 'Data Not Found';
    END IF;

    --IF v_question_file IS NULL THEN
	--v_question_file := 'Data Not Found';
    --END IF;

 return v_question_file;
EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION Ivrs_GetQuestionFile(integer,integer,integer) OWNER TO ctcae;

-- function returns the next question id for the given form

CREATE OR REPLACE FUNCTION IVRS_GetNextQuestion(userid integer, formid integer,questionid integer) RETURNS integer AS $$
DECLARE
    v_page_id integer := 0;
    v_gender text := '';
    v_question_id integer :=0;
    v_first_question_value integer := 0;
    v_current_answer_value integer :=0;
    v_question_skipped integer :=0;
    v_temp_value_id integer :=0;
    v_tmp_crf_page_id integer :=0;
    v_page_present_num integer := 0;
    curs_questions CURSOR(formid integer,gender text, pageid integer) IS SELECT spci.crf_item_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
	from study_participant_crf_items spci join
	crf_page_items cpi ON ((cpi.id= spci.crf_item_id) and cpi.crf_page_id >= pageid) join crf_pages cp ON (cp.id=cpi.crf_page_id)
	join pro_ctc_terms pct ON ((pct.id=cp.pro_ctc_term_id) and (pct.gender is null or upper(pct.gender)=upper(gender) or pct.gender ='both'))
	where spci.sp_crf_schedule_id = formid order by cpi.crf_page_id,cpi.display_order;

BEGIN
	SELECT gender into v_gender FROM participants where user_id=userid;

	SELECT cpi.crf_page_id INTO v_page_present_num
	from crf_page_items cpi join study_participant_crf_items spci
	ON ((cpi.id= spci.crf_item_id) and spci.crf_item_id=questionid);

	OPEN curs_questions(formid,v_gender,v_page_present_num);
	LOOP
	FETCH curs_questions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
	       EXIT WHEN NOT FOUND;

		IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
			v_page_id := v_tmp_crf_page_id;
			v_first_question_value := v_temp_value_id;
			IF v_first_question_value IS NULL THEN
				EXIT;
			ELSE
				SELECT count(*) INTO v_question_skipped from crf_page_item_display_rules
				where pro_ctc_valid_value_id = v_first_question_value;

			END IF;
		END IF;
		v_current_answer_value := v_temp_value_id;

		IF v_current_answer_value IS NULL AND v_question_skipped > 0 THEN
			EXIT;
		END IF;

	END LOOP;

	CLOSE curs_questions;

	IF v_page_id=0 OR v_question_id IS NULL then
		return 0;
	end if;

	return v_question_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION IVRS_GetNextQuestion(integer,integer,integer) OWNER TO ctcae;

-- store the answer for the given question and returns next question id if exists

CREATE OR REPLACE FUNCTION IVRS_AnswerQuestion(userid integer, formid integer,questionid integer,answerNum integer) RETURNS integer AS $$
DECLARE
    v_next_question_id integer :=0;

    v_proctc_question_id integer :=0;
    v_proctc_value_id integer :=0;
BEGIN
	--get the answer from pro_ctc_valid_values
	SELECT pro_ctc_question_id INTO v_proctc_question_id from crf_page_items where id=questionid;

	--SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = v_proctc_question_id and display_order = (answerNum-1);
	--SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = v_proctc_question_id and value = anserNum;
	SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = v_proctc_question_id order by id LIMIT 1 OFFSET answerNum-1;


	UPDATE study_participant_crf_items SET pro_ctc_valid_value_id = v_proctc_value_id WHERE crf_item_id = questionid;
	IF found THEN
		UPDATE sp_crf_schedules set status='INPROGRESS' where id=formid and status<>'INPROGRESS';
		v_next_question_id := ivrs_getnextquestion(userid,formid,questionid);
	END IF;

	return v_next_question_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION IVRS_AnswerQuestion(integer,integer,integer,integer) OWNER TO ctcae;

-- function complete the sceduled form upon completion of all questions and change the status to completed

CREATE OR REPLACE FUNCTION ivrs_commitsession(userid integer, formid integer, pin integer)
  RETURNS integer AS
$BODY$
DECLARE
	v_next_question_id integer :=0;
BEGIN
	v_next_question_id := ivrs_getfirstquestion(userid,formid);
	IF v_next_question_id = 0
	then
		UPDATE sp_crf_schedules set status='COMPLETED' where id=formid and Status ='INPROGRESS';
		IF found then
			return 1;
		else
			return 0;
		END IF;
	ELSE
		return -2;
	END IF;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION ivrs_commitsession(integer, integer, integer) OWNER TO ctcae;



