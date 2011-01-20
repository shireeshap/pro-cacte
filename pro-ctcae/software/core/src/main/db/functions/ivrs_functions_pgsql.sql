--Login function returns user id on successful login
--Input parameters are user number and pin
--Function returns 0 on unsuccessful login , returns valid user id which is >0 on successful login and returns -1 for any other DB issues
CREATE OR REPLACE FUNCTION ivrs_login(userNumber integer, pin integer) RETURNS integer AS $$
DECLARE
   v_ret_user_id integer := 0;
BEGIN

     SELECT user_id INTO v_ret_user_id from participants where user_number=userNumber and pin_number=pin;
     IF NOT FOUND THEN
	return 0;
     END IF;
     return v_ret_user_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

--Ivrs_NumberForms function returns number of scheduled forms avilable for the user
--Input parameters: userid is the returned value from login function for the same caller
--Return values:  >=0 for number of scheduled forms avilable for the user, -1 for any other DB issues



CREATE OR REPLACE FUNCTION Ivrs_NumberForms(userid integer) RETURNS integer AS $$
DECLARE
    v_ret_count integer :=0;
BEGIN

    SELECT count(spcs.id) INTO v_ret_count
    from sp_crf_schedules spcs
    JOIN study_participant_crfs spc ON  spcs.study_participant_crf_id=spc.id
    JOIN study_participant_assignments sp ON spc.study_participant_id = sp.id
    JOIN participants p ON sp.participant_id = p.id
    where spcs.start_date <=current_date and (spcs.status = 'SCHEDULED' OR spcs.status= 'INPROGRESS')
	  and p.user_id=userid;

     return v_ret_count;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

--Ivrs_GetForm returns the form id for corresponding form number(form number(2) indicates the sequence of the returned (2nd)form list.)
--Input parameters: userid: user id and formNum
--Return value: formid: >0 is valid scheduled form id(sp_crf_schedules), -2 for invalid form id and -1 for any other DB issues

CREATE OR REPLACE FUNCTION Ivrs_GetForm(userid integer,formNum integer) RETURNS integer AS $$
DECLARE
    v_form_id integer :=0;
BEGIN

	SELECT spcs.id INTO v_form_id
	from sp_crf_schedules spcs
	JOIN study_participant_crfs spc ON spcs.study_participant_crf_id=spc.id
	JOIN study_participant_assignments sp ON spc.study_participant_id= sp.id
	JOIN participants p ON sp.participant_id = p.id
	where spcs.start_date <=current_date and (spcs.status = 'SCHEDULED' OR spcs.status= 'INPROGRESS')
	and p.user_id=userid
	order by spcs.start_date,spcs.id LIMIT 1 OFFSET formNum-1;

	IF NOT FOUND THEN
		return 0;
	END IF;

	return v_form_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

--IVRS_GetFormTitle function returns the form title for the given scheduled form id
--Input Parameters: userid , formid (scheduled form id)
--Return values: 'form title' when form title is found and 'Not Found' for DB issues as well as title information not found in DB for given form id
CREATE OR REPLACE FUNCTION IVRS_GetFormTitle(userid integer,formid integer) RETURNS text AS $$
DECLARE
    v_form_title text :='Not Found';
BEGIN

        select c.title INTO v_form_title
        from crfs c
        JOIN study_participant_crfs spc ON c.id=spc.crf_id
        JOIN sp_crf_schedules scs ON spc.id=scs.study_participant_crf_id
	where scs.id=formid;

     IF NOT FOUND THEN
	return 'Not Found';
     END IF;

     return v_form_title;
EXCEPTION
    WHEN OTHERS THEN
    return 'Not Found';
END;
$$ LANGUAGE plpgsql;

--IVRS_GetFirstQuestion function returns the first unanswered question id(PK of study_participant_crf_items)
--for valid given scheduled form id(skipping rules applied)
--Input Parameters: userid , formid (scheduled form id)
--Return values: >0 is the valid question id, 0 if all the questions answered in the form and -1 for any other DB issues

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

    curs_questions CURSOR(formid_in integer,gender_in text) IS
    SELECT spci.crf_item_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
	from study_participant_crf_items spci
	join crf_page_items cpi ON cpi.id= spci.crf_item_id
	join crf_pages cp ON cp.id=cpi.crf_page_id
	join pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
	where spci.sp_crf_schedule_id = formid_in and (pct.gender is null or pct.gender=gender_in or pct.gender ='both')
	order by cpi.crf_page_id,cpi.display_order;

BEGIN
	--getting gender information of the participant
	SELECT lower(gender) into v_gender FROM participants where user_id=userid;

	--Going through all the questions for the form and checking for the valid unanswered question
	OPEN curs_questions(formid,v_gender);
	LOOP
	FETCH curs_questions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
	       EXIT WHEN NOT FOUND;
		--when new page question or symptom comes
		IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
			v_page_id := v_tmp_crf_page_id;
			v_first_question_value := v_temp_value_id;
			--if first question which is condtional question and the same is not answered then return the same question id
			IF v_first_question_value IS NULL THEN
				EXIT;
			ELSE
				--checking for the questions in the page to be skipped or not
				SELECT count(id) INTO v_question_skipped
				from crf_page_item_display_rules
				where pro_ctc_valid_value_id = v_first_question_value;

			END IF;
		END IF;
		v_current_answer_value := v_temp_value_id;
		--for the non condtional questions, check for the presence of answer while skipp option is not applicable
		IF v_current_answer_value IS NULL AND v_question_skipped > 0 THEN
			EXIT;
		END IF;

	END LOOP;

	CLOSE curs_questions;

	--if there is no valid unanswered question then return 0
	IF v_page_id=0 OR v_question_id IS NULL then
		return 0;
	end if;

	return v_question_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;


--Ivrs_GetQuestionType function returns the question type for the given scheduled form question id
--Input parameters: userid, formid (scheduled form id), questionid(study_participant_crf_items id)
--Return value: Question type for the given scheduled form question id
--possible values are "AMOUNT","FREQUENCY","INTERFERENCE","PRESENT" and "SEVERITY"

CREATE OR REPLACE FUNCTION Ivrs_GetQuestionType(userid integer,formid integer,questionid Integer) RETURNS text AS $$
DECLARE
    v_question_type Text := '';

BEGIN

    SELECT pcq.question_type INTO v_question_type
    from pro_ctc_questions pcq
    join crf_page_items cpi on pcq.id=cpi.pro_ctc_question_id
    where cpi.id=questionid;

    IF NOT FOUND THEN
	return 'Data Not Found';
    END IF;
       return v_question_type;

EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$$ LANGUAGE plpgsql;


--Ivrs_GetQuestionText function returns the question text for the given scheduled form question id
--Input parameters: userid, formid (scheduled form id), questionid(study_participant_crf_items.id)
--Return values: Question text for the given scheduled form question id

CREATE OR REPLACE FUNCTION Ivrs_GetQuestionText(userid integer,formid integer,questionid Integer) RETURNS text AS $$
DECLARE
    v_question_text Text := '';
BEGIN

    SELECT pcq.question_text INTO v_question_text
    from pro_ctc_questions pcq
    join crf_page_items cpi on pcq.id=cpi.pro_ctc_question_id
    where cpi.id=questionid;

    IF NOT FOUND THEN
	return 'Data Not Found';
    END IF;

       return v_question_text;
EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$$ LANGUAGE plpgsql;


--Ivrs_GetQuestionFile function returns the question audio file name for the given scheduled form question id
--Input parameters: userid, formid (scheduled form id), questionid(study_participant_crf_items.id)
--Return values: Question audio file name for the given scheduled form question id

CREATE OR REPLACE FUNCTION Ivrs_GetQuestionFile(userid integer,formid integer,questionid Integer) RETURNS text AS $$
DECLARE
    v_question_file Text := '';
BEGIN

    SELECT pcq.question_file_name INTO v_question_file
	from pro_ctc_questions pcq
        join crf_page_items cpi on pcq.id=cpi.pro_ctc_question_id
        where cpi.id=questionid;

    IF NOT FOUND THEN
	v_question_file:= 'Data Not Found';
    END IF;

 return v_question_file;
EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$$ LANGUAGE plpgsql;



--IVRS_GetNextQuestion function returns the next question id for the given form
--Input vales: userid, formid (scheduled form id), questionid(study_participant_crf_items.id)
--return values: 0 if all the questions answered in the form, >0 is the valid question id, -1 for any other DB issues
--Here next Question id is the PK id of study_participant_crf_items
--function considering questions from the current page (page of input question id)
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

    curs_questions CURSOR(formid_in integer,gender_in text, pageid_in integer) IS
    SELECT spci.crf_item_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
	from study_participant_crf_items spci
	join crf_page_items cpi ON cpi.id= spci.crf_item_id
	join crf_pages cp ON cp.id=cpi.crf_page_id
	join pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
	where cpi.crf_page_id >= pageid_in and
	spci.sp_crf_schedule_id = formid_in and
	(pct.gender is null or pct.gender=gender_in or pct.gender ='both')
	order by cpi.crf_page_id,cpi.display_order;

BEGIN
	--getting gender information of the participant
	SELECT lower(gender) into v_gender FROM participants where user_id=userid;

	--getting current page for the given question
	SELECT cpi.crf_page_id INTO v_page_present_num
	from crf_page_items cpi
	join study_participant_crf_items spci ON cpi.id= spci.crf_item_id
	where spci.crf_item_id=questionid;

	--traversing all the questions from the current page to get valid un answered question
	OPEN curs_questions(formid,v_gender,v_page_present_num);
	LOOP
	FETCH curs_questions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
	       EXIT WHEN NOT FOUND;
		--when new page question or symptom comes
		IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
			v_page_id := v_tmp_crf_page_id;
			v_first_question_value := v_temp_value_id;
			--if first question which is condtional question is it self not answered then return the same question id
			IF v_first_question_value IS NULL THEN
				EXIT;
			ELSE
				--checking for the questions in the page to be skipped or not
				SELECT count(id) INTO v_question_skipped from crf_page_item_display_rules
				where pro_ctc_valid_value_id = v_first_question_value;

			END IF;
		END IF;
		v_current_answer_value := v_temp_value_id;
		--for the non condtional questions, check for the answer presents and skipped option not applicable
		IF v_current_answer_value IS NULL AND v_question_skipped > 0 THEN
			EXIT;
		END IF;

	END LOOP;

	CLOSE curs_questions;
	--if there are no valid un answered questions then return 0
	IF v_page_id=0 OR v_question_id IS NULL then
		return 0;
	end if;

	return v_question_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;

-- store the answer for the given question and returns next question id if exists

--store the answer for the given question and returns next valid un answered question id if exists
--input: userid , formid which is PK id of scheduled form, questionid, answerNum
--returns next valid un answered question id for the given form
CREATE OR REPLACE FUNCTION IVRS_AnswerQuestion(userid integer, formid integer,questionid integer,answerNum integer) RETURNS integer AS $$
DECLARE
    v_next_question_id integer :=0;
    v_proctc_question_id integer :=0;
    v_proctc_value_id integer :=0;
BEGIN
	--get the proctc question id using schedule question (crf item id)
	SELECT pro_ctc_question_id INTO v_proctc_question_id from crf_page_items where id=questionid;

	--get the valid value id from the proctc valid values table for given answer num.
	--SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = v_proctc_question_id and display_order = (answerNum-1);
	--SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = v_proctc_question_id and value = anserNum;
	SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = v_proctc_question_id order by id LIMIT 1 OFFSET answerNum-1;
	--update the answer for given question id
	UPDATE study_participant_crf_items SET pro_ctc_valid_value_id = v_proctc_value_id WHERE sp_crf_schedule_id=formid and crf_item_id = questionid;
	IF found THEN
		UPDATE sp_crf_schedules set status='INPROGRESS' where id=formid and status<>'INPROGRESS';
		--get the next valid un answered question for the form
		v_next_question_id := ivrs_getnextquestion(userid,formid,questionid);
	END IF;

	return v_next_question_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE plpgsql;


-- ivrs_commitsession function complete the sceduled form upon completion of all questions and change the status to completed
--Input parameters:userid,pin and formid (scheduled form id,PK id of sp_crf_schedules)
--return: -2 for form not completed properly and still some questions needs to be answered, 1 for form completed, 0 for form not submitted properly, -1 for any DB issues
CREATE OR REPLACE FUNCTION ivrs_commitsession(userid integer, formid integer, pin integer)
  RETURNS integer AS $$
DECLARE
	v_next_question_id integer :=0;
BEGIN
	v_next_question_id := ivrs_getfirstquestion(userid,formid);
	IF v_next_question_id = 0
	then
		INSERT INTO sp_crf_schedule_notif(id, spc_schedule_id, status, creation_date)
		VALUES (nextval('sp_crf_schedule_notif_id_seq'),formid,'SCHEDULED',now());

		UPDATE sp_crf_schedules set status='COMPLETED',form_submission_mode='IVRS' where id=formid and Status ='INPROGRESS';

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
$$ LANGUAGE 'plpgsql';




