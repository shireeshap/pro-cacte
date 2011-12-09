--Login function returns user id on successful login
--Input parameters are user number and pin
--Function returns 0 on unsuccnextQuestionIdCategoryessful login , returns valid user id which is >0 on successful login and returns -1 for any other DB issues
CREATE OR REPLACE FUNCTION make_plpgsql()
RETURNS VOID
LANGUAGE SQL
AS $x$
CREATE LANGUAGE plpgsql;
$x$;
 
SELECT
    CASE
    WHEN EXISTS(
        SELECT 1
        FROM pg_catalog.pg_language
        WHERE lanname='plpgsql'
    )
    THEN NULL
    ELSE make_plpgsql() END;

    
CREATE OR REPLACE FUNCTION ivrs_login(userNumber text, pin integer) RETURNS integer AS $x$
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
$x$ LANGUAGE plpgsql;



  --Ivrs_NumberForms function returns number of scheduled forms avilable for the user
  --Input parameters: userid is the returned value from login function for the same caller
  --Return values:  >=0 for number of scheduled forms avilable for the user, -1 for any other DB issues

  CREATE OR REPLACE FUNCTION Ivrs_NumberForms(userid integer) RETURNS integer AS $x$
  DECLARE
      v_ret_count integer :=0;
  BEGIN
     
     --SELECT count(spcs.id) INTO v_ret_count
     --from sp_crf_schedules spcs
     --JOIN study_participant_crfs spc ON  spcs.study_participant_crf_id=spc.id
     ---JOIN study_participant_assignments sp ON spc.study_participant_id = sp.id
     --JOIN participants p ON sp.participant_id = p.id
     --where spcs.start_date <=current_date and (spcs.status = 'SCHEDULED' OR spcs.status= 'INPROGRESS')
     --and p.user_id=userid;
     SELECT count(spcs.id) INTO v_ret_count
	 from sp_crf_schedules spcs
	 JOIN study_participant_crfs spc ON spcs.study_participant_crf_id=spc.id
	JOIN crfs c ON c.id=spc.crf_id
	JOIN study_participant_assignments sp ON spc.study_participant_id= sp.id
	JOIN participants p ON sp.participant_id = p.id
	where spcs.start_date <=current_date  AND spcs.due_date >= current_date and (spcs.status = 'SCHEDULED' OR spcs.status= 'INPROGRESS')
	and p.user_id=userid and c.is_hidden='FALSE';
   

       return v_ret_count;
  EXCEPTION
      WHEN OTHERS THEN
      return -1;
  END;
  $x$ LANGUAGE plpgsql;

--Ivrs_GetForm returns the form id for corresponding form number(form number(2) indicates the sequence of the returned (2nd)form list.)
--Input parameters: userid: user id and formNum
--Return value: formid: >0 is valid scheduled form id(sp_crf_schedules), -2 for invalid form id and -1 for any other DB issues

CREATE OR REPLACE FUNCTION Ivrs_GetForm(userid integer,formNum integer) RETURNS integer AS $x$
DECLARE
    v_form_id integer :=0;
BEGIN

	--SELECT spcs.id INTO v_form_id
	--from sp_crf_schedules spcs
	--JOIN study_participant_crfs spc ON spcs.study_participant_crf_id=spc.id
	--JOIN study_participant_assignments sp ON spc.study_participant_id= sp.id
	--JOIN participants p ON sp.participant_id = p.id
	--where spcs.start_date <=current_date and (spcs.status = 'SCHEDULED' OR spcs.status= 'INPROGRESS')
	--and p.user_id=userid
	--order by spcs.start_date,spcs.id LIMIT 1 OFFSET formNum-1;
	  -- RK bug 1080
    SELECT spcs.id INTO v_form_id
	from sp_crf_schedules spcs
	JOIN study_participant_crfs spc ON spcs.study_participant_crf_id=spc.id
	JOIN crfs c ON c.id=spc.crf_id
	JOIN study_participant_assignments sp ON spc.study_participant_id= sp.id
	JOIN participants p ON sp.participant_id = p.id
	where spcs.start_date <=current_date  AND spcs.due_date >= current_date and (spcs.status = 'SCHEDULED' OR spcs.status= 'INPROGRESS')
	and p.user_id=userid and c.is_hidden='FALSE'
	order by spcs.start_date,spcs.id LIMIT 1 OFFSET formNum-1;

	IF NOT FOUND THEN
		return 0;
	END IF;

	return v_form_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE plpgsql;

--IVRS_GetFormTitle function returns the form title for the given scheduled form id
--Input Parameters: userid , formid (scheduled form id)
--Return values: 'form title' when form title is found and 'Not Found' for DB issues as well as title information not found in DB for given form id
CREATE OR REPLACE FUNCTION IVRS_GetFormTitle(userid integer,formid integer) RETURNS text AS $x$
DECLARE
    v_form_title text :='Not Found';
BEGIN

        SELECT c.title INTO v_form_title
        FROM crfs c
        JOIN study_participant_crfs spc ON c.id=spc.crf_id
        JOIN sp_crf_schedules scs ON spc.id=scs.study_participant_crf_id
	    WHERE scs.id=formid;

     IF NOT FOUND THEN
	return 'Not Found';
     END IF;

     return v_form_title;
EXCEPTION
    WHEN OTHERS THEN
    return 'Not Found';
END;
$x$ LANGUAGE plpgsql;

--IVRS_GetFirstQuestion function returns the first unanswered question id along with the question category(PK of pro_ctc_question)
--for valid given scheduled form id(skipping rules applied)
--Input Parameters: userid , formid (scheduled form id)
--Return values: 'questionId_0' (added questions) 'questionId_1'(Mandatory questions) if it finds any valid question id, 0 if all the questions answered in the form and -1 for any other DB issues

CREATE OR REPLACE FUNCTION IVRS_GetFirstQuestion(userid integer,formid integer) RETURNS text AS $x$
DECLARE
    v_page_id integer := 0;
    v_gender text := '';
    v_question_id integer :=0;
    v_first_question_value integer := 0;
    v_current_answer_value integer :=0;
    v_question_skipped integer :=0;
    v_temp_value_id integer :=0;
    v_tmp_crf_page_id integer :=0;
    v_question_category text := 1;
    v_sp_crf_added_question_id integer :=0;
    v_page_no integer :=0;
    v_added_question_id integer :=0;
    v_sp_crf_sh_added_question_id integer :=0;
    v_spcsaq_id integer :=0;

    curs_questions CURSOR(formid_in integer,gender_in text) IS
    SELECT cpi.pro_ctc_question_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
	from study_participant_crf_items spci
	join crf_page_items cpi ON cpi.id= spci.crf_item_id
	join crf_pages cp ON cp.id=cpi.crf_page_id
	join pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
	where spci.sp_crf_schedule_id = formid_in and (pct.gender is null or pct.gender=gender_in or pct.gender ='both')
	order by spci.crf_item_id,cpi.display_order;

    curs_added_question CURSOR(formid_in integer) IS
    SELECT pcq.id,spcaq.pro_ctc_valid_value_id,spcaq.page_number
    FROM pro_ctc_questions pcq
    JOIN sp_crf_sch_added_questions spcaq ON spcaq.question_id=pcq.id
    WHERE spcaq.sp_crf_schedule_id=formid_in
    ORDER BY spcaq.id,pcq.display_order;

	curs_sp_added_question CURSOR(formid_in integer) IS
	SELECT pcq.id,spcaq.id,spcaq.page_number FROM pro_ctc_questions pcq
	JOIN sp_crf_added_questions spcaq ON spcaq.question_id = pcq.id
	JOIN sp_crf_schedules spcs ON (spcs.study_participant_crf_id=spcaq.sp_crf_id)
	WHERE spcs.id=formid_in order by spcaq.page_number,pcq.display_order;

BEGIN
	--getting gender information of the participant
	SELECT lower(gender) into v_gender FROM participants where user_id=userid;

	SELECT spcsaq.id into v_spcsaq_id FROM sp_crf_sch_added_questions spcsaq where spcsaq.sp_crf_schedule_id=formid;
	IF NOT FOUND THEN
	-- inserting added questions from last form in scheduled added questions table
		OPEN curs_sp_added_question(formid);
		LOOP
		FETCH curs_sp_added_question INTO v_added_question_id,v_sp_crf_added_question_id,v_page_no;
			EXIT WHEN NOT FOUND;

			SELECT NEXTVAL('sp_crf_sch_added_questions_id_seq') INTO v_sp_crf_sh_added_question_id;
			INSERT INTO sp_crf_sch_added_questions (id,version, sp_crf_schedule_id,question_id,page_number,spc_added_question_id)
			VALUES (v_sp_crf_sh_added_question_id,0,formid,v_added_question_id,v_page_no,v_sp_crf_added_question_id);

		END LOOP;
		CLOSE curs_sp_added_question;
	END IF;

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
		v_question_category := 0;
		OPEN curs_added_question(formid);
		    LOOP
		    FETCH curs_added_question INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
			   EXIT WHEN NOT FOUND;
			IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
			    v_page_id := v_tmp_crf_page_id;
			    v_first_question_value := v_temp_value_id;
			    IF v_first_question_value IS NULL THEN
				EXIT;
			    ELSE
				SELECT count(id) INTO v_question_skipped
				FROM crf_page_item_display_rules
				WHERE pro_ctc_valid_value_id = v_first_question_value;
			    END IF;
			END IF;
			v_current_answer_value := v_temp_value_id;
			IF v_current_answer_value IS NULL AND v_question_skipped > 0 THEN
			    EXIT;
			END IF;
		    END LOOP;

		    CLOSE curs_added_question;

		    IF  v_page_id=0 OR v_question_id IS NULL then
			return '0';
		    end if;
	end if;

	return v_question_id||'_'||v_question_category;

EXCEPTION
    WHEN OTHERS THEN
    return '-1';
END;
$x$ LANGUAGE plpgsql;

--Ivrs_GetQuestionType function returns the question type for the given scheduled form question id
--Input parameters: userid, formid (scheduled form id), questionid(study_participant_crf_items id)
--Return value: Question type for the given scheduled form question id
--possible values are "AMOUNT","FREQUENCY","INTERFERENCE","PRESENT" and "SEVERITY"

CREATE OR REPLACE FUNCTION IVRS_GetQuestionType(userid integer, formid integer, questionid integer)
  RETURNS text AS $x$
DECLARE
	v_question_type Text := '';
	v_type_number integer := 0;
	v_value text :='';
BEGIN
	SELECT pcq.question_type INTO v_question_type
	from pro_ctc_questions pcq
        where pcq.id = questionid;

	IF NOT FOUND THEN
		return 'Data Not Found';
	END IF;
	-- get the number of response options for each given question
	SELECT count(*) INTO v_type_number FROM pro_ctc_valid_values where pro_ctc_question_id=questionid;
	-- based on question type and number of questions return the value. It is in ascending order.
	IF v_question_type='PRESENT' THEN
		IF v_type_number = 2 THEN
			return v_question_type||'_'||1;
		ELSIF  v_type_number = 3 THEN
			return v_question_type||'_'||2;
		ELSE
			return v_question_type||'_'||3;
		END IF;
	END IF;

	IF v_question_type='SEVERITY' THEN
		IF v_type_number = 5 THEN
			return v_question_type||'_'||1;
		ELSIF  v_type_number = 6 THEN
			return v_question_type||'_'||2;
		ELSE
			return v_question_type||'_'||3;
		END IF;
	END IF;

	IF v_question_type='FREQUENCY' THEN
			IF v_type_number = 5 THEN
				return v_question_type||'_'||1;
			ELSE
				SELECT value_english INTO v_value FROM pro_ctc_valid_values_vocab pcvv
				JOIN pro_ctc_valid_values pcv ON pcvv.id=pcv.id
				WHERE pcv.pro_ctc_question_id=questionid
				ORDER BY display_order OFFSET 5;
				IF v_value ='Prefer not to answer' THEN
					return v_question_type||'_'||3;
				ELSE
					return v_question_type||'_'||2;
				END IF;
			END IF;
    END IF;

	IF v_question_type='INTERFERENCE' THEN
		IF v_type_number = 5 THEN
			return v_question_type||'_'||1;
		ELSE
			return v_question_type||'_'||2;
		END IF;
	END IF;

	IF v_question_type='AMOUNT' AND v_type_number = 5 THEN
		return v_question_type;
	END IF;

	return '0';

EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$x$  LANGUAGE 'plpgsql' VOLATILE;


--Ivrs_GetQuestionText function returns the question text for the given scheduled form question id
--Input parameters: userid, formid (scheduled form id), questionid(study_participant_crf_items.id)
--Return values: Question text for the given scheduled form question id

CREATE OR REPLACE FUNCTION Ivrs_GetQuestionText(userid integer,formid integer,questionid Integer) RETURNS text AS $x$
DECLARE
    v_question_text Text := '';
BEGIN

    SELECT pcqv.question_text_english INTO v_question_text
    FROM pro_ctc_questions_vocab pcqv
    WHERE pcqv.pro_ctc_questions_id = questionid;

    IF NOT FOUND THEN
	return 'Data Not Found';
    END IF;

       return v_question_text;
EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$x$ LANGUAGE plpgsql;


--Ivrs_GetQuestionFile function returns the question audio file name for the given scheduled form question id
--Input parameters: userid, formid (scheduled form id), questionid(study_participant_crf_items.id)
--Return values: Question audio file name for the given scheduled form question id

CREATE OR REPLACE FUNCTION Ivrs_GetQuestionFile(userid integer,formid integer,questionid Integer) RETURNS text AS $x$
DECLARE
    v_question_file Text := '';
BEGIN

    SELECT pcq.question_file_name INTO v_question_file
	FROM pro_ctc_questions pcq
    WHERE pcq.id = questionid;

    IF NOT FOUND THEN
	v_question_file:= 'Data Not Found';
    END IF;

 return v_question_file;
EXCEPTION
    WHEN OTHERS THEN
    return '-1';
END;
$x$ LANGUAGE plpgsql;



--IVRS_GetNextQuestion function returns the next question id for the given form
--Input values: userid, formid (scheduled form id), questionid(pro_ctc_question.id)
--return values: 0 if all the questions answered in the form, >0 is the valid question id, -1 for any other DB issues
--Here next Question id is the PK id of pro_ctc_question
--function considering questions from the current page (page of input question id)
CREATE OR REPLACE FUNCTION IVRS_GetNextQuestion(userid integer, formid integer,questionid integer) RETURNS integer AS $x$
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
    SELECT cpi.pro_ctc_question_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
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
	where cpi.pro_ctc_question_id=questionid and spci.sp_crf_schedule_id = formid;

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
$x$ LANGUAGE plpgsql;

--IVRS_GetNextAddedQuestion function returns the next question id for the given form
--Input values: userid, formid (scheduled form id), questionid(pro_ctc_question.id)
--return values: 0 if all the questions answered in the form, >0 is the valid question id, -1 for any other DB issues
--Here next Question id is the PK id of pro_ctc_question
--function considering questions from the current page (page of input question id)
CREATE OR REPLACE FUNCTION IVRS_GetNextAddedQuestion(userid integer, formid integer, questionid integer) RETURNS integer AS $x$
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

    curs_added_question CURSOR(formid_in integer,pageid_in integer) IS
    SELECT spcaq.pro_ctc_valid_value_id,pcq.id,spcaq.page_number
    FROM pro_ctc_questions pcq
    JOIN sp_crf_sch_added_questions spcaq ON spcaq.question_id=pcq.id
    WHERE spcaq.sp_crf_schedule_id=formid_in and spcaq.page_number>=pageid_in
    ORDER BY spcaq.id,pcq.display_order;


    curs_first_added_question CURSOR(formid_in integer) IS
    SELECT spcaq.pro_ctc_valid_value_id,pcq.id,spcaq.page_number
    FROM pro_ctc_questions pcq
    JOIN sp_crf_sch_added_questions spcaq ON spcaq.question_id=pcq.id
    WHERE spcaq.sp_crf_schedule_id=formid_in
    ORDER BY spcaq.id,pcq.display_order;

BEGIN

	--getting current page for the given question
	SELECT spcaq.page_number INTO v_page_present_num
	from sp_crf_sch_added_questions spcaq
	where spcaq.question_id=questionid and spcaq.sp_crf_schedule_id = formid;
	IF NOT FOUND THEN
		OPEN curs_first_added_question(formid);
		LOOP
		FETCH curs_first_added_question INTO v_temp_value_id,v_question_id,v_tmp_crf_page_id;
		EXIT WHEN NOT FOUND;
				IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
					v_page_id := v_tmp_crf_page_id;
					v_first_question_value := v_temp_value_id;
					IF v_first_question_value IS NULL THEN
						EXIT;
					ELSE
						SELECT count(id) INTO v_question_skipped
						FROM crf_page_item_display_rules
						WHERE pro_ctc_valid_value_id = v_first_question_value;
					END IF;
				END IF;
				v_current_answer_value := v_temp_value_id;
				IF v_current_answer_value IS NULL AND v_question_skipped > 0 THEN
					EXIT;
				END IF;

		END LOOP;
		CLOSE curs_first_added_question;

	ELSE
	--traversing all the questions from the current page to get valid un answered question
	OPEN curs_added_question(formid,v_page_present_num);
	LOOP
	FETCH curs_added_question INTO v_temp_value_id,v_question_id,v_tmp_crf_page_id;
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

	CLOSE curs_added_question;
	END IF;
	--if there are no valid un answered questions then return 0
	IF v_page_id=0 OR v_question_id IS NULL then
		return 0;
	end if;
    return v_question_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE plpgsql;

-- store the answer for the given question and returns next question id if exists

--store the answer for the given question and returns next valid un answered question id if exists
--input: userid , formid which is PK id of scheduled form, questionid, answerNum and questionCategory which specifies
--whether a questions in in mandatory or added core symptom page
--returns next valid un answered question id for the given form
CREATE OR REPLACE FUNCTION IVRS_AnswerQuestion(userid integer, formid integer, questionid integer, answernum integer, questioncategory integer)
 RETURNS text AS $x$
DECLARE
    v_next_question_id integer :=0;
    v_crf_item_id integer :=0;
    v_proctc_value_id integer :=0;
    v_user_name text := '';
    v_return_question_category integer :=0;
    v_temp integer :=0;
    v_core_sym_count integer :=0;
    v_first_question integer :=0;
    v_temp_value integer :=0;
    v_question_type text :='';

BEGIN
	v_return_question_category :=questioncategory;
	-- get user name from users using user id
	SELECT user_name INTO v_user_name from users where id = userid;
	-- get the proctc valid value if using given proctc question id and answer from IVRS
	SELECT pcq.question_type INTO v_question_type from pro_ctc_questions pcq where pcq.id = questionid;
	-- process finding next quetion based on question category 1= mandatory and 0= optional/added
        -- Based on question type results are ordered by display_order and id respectively
	IF v_question_type ='PRESENT' THEN
		SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = questionid order by display_order LIMIT 1 OFFSET answerNum;
	ELSE
		SELECT id INTO v_proctc_value_id from pro_ctc_valid_values where pro_ctc_question_id = questionid order by id LIMIT 1 OFFSET answerNum;
	END IF;
	IF questionCategory = 1 THEN
		--get the proctc question id using schedule question (crf item id)
		SELECT spci.id INTO v_crf_item_id from crf_page_items cpi
		JOIN study_participant_crf_items spci ON spci.crf_item_id = cpi.id
		where spci.sp_crf_schedule_id=formid and cpi.pro_ctc_question_id=questionid;

		--update the answer for given question id
		UPDATE study_participant_crf_items SET pro_ctc_valid_value_id = v_proctc_value_id, response_date = now(),
		response_mode = 'IVRS',updated_by= v_user_name
		WHERE sp_crf_schedule_id=formid and id = v_crf_item_id;

		IF found THEN
			UPDATE sp_crf_schedules set status='INPROGRESS' where id=formid and status<>'INPROGRESS';
			--get the next valid un answered question for the form
			v_next_question_id := ivrs_getnextquestion(userid,formid,questionid);

		END IF;
        END IF;
	IF v_next_question_id =0 OR questionCategory = 0 THEN
	        v_return_question_category :=0;
		--update answer for given question id
		SELECT pro_ctc_valid_value_id INTO v_temp_value FROM sp_crf_sch_added_questions
		WHERE sp_crf_schedule_id=formid and question_id = questionid;
		IF NOT FOUND THEN
			v_temp_value := 0;
		END IF;

		UPDATE sp_crf_sch_added_questions SET pro_ctc_valid_value_id = v_proctc_value_id, response_date = now(),
		response_mode = 'IVRS',updated_by= v_user_name
		WHERE sp_crf_schedule_id=formid and question_id = questionid;

		SELECT iscsc.core_sym_count into v_core_sym_count from ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
		IF NOT FOUND THEN
			v_temp:=0;
		ELSE
			SELECT pcq1.id INTO v_first_question FROM pro_ctc_questions pcq1 WHERE pcq1.pro_ctc_term_id  IN
			(SELECT pcq2.pro_ctc_term_id FROM pro_ctc_questions pcq2 WHERE pcq2.id=questionid)
			 ORDER BY pcq1.display_order LIMIT 1;
			 IF v_first_question=questionid AND v_temp_value is null THEN
				UPDATE ivrs_sch_core_sym_count SET core_sym_count = v_core_sym_count-1 WHERE spc_schedule_id=formid;
		         END IF;
		END IF;
		IF found THEN
			UPDATE sp_crf_schedules set status='INPROGRESS' where id=formid and status<>'INPROGRESS';

		END IF;
		--get the next valid un answered question for the form
		v_next_question_id := ivrs_getnextAddedquestion(userid,formid,questionid);
	END IF;

	IF v_next_question_id = 0 THEN
		return '0';
	ELSIF v_next_question_id = -1 THEN
		return '-1';
        ELSE
		return v_next_question_id||'_'||v_return_question_category;
	END IF;
	--return v_next_question_id||'_'||questionCategory;
EXCEPTION
    WHEN OTHERS THEN
    return '-1';
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;


-- Inserts the symptom's questions to added questions based on answer and returns next core symptom id if exists

-- Inserts the symptom's questions to added questions based on answer (1= insert 2= will not insert questions) and returns next core symptom id if exists
--input: userid , formid which is PK id of scheduled form, symptomid and answer and corecategory
--returns next valid un asked core symptom id for the given form
CREATE OR REPLACE FUNCTION ivrs_AnswerCoreSymptom(userid integer, formid integer, symptomid integer, answer integer, corecategory integer)
  RETURNS integer AS $x$
DECLARE
	v_proc_ctc_term_id integer :=0;
    v_question_id integer :=0;
	v_sp_crf_id integer :=0;
	v_page_no integer :=0;
	v_schedule_id integer :=0;
	v_sp_crf_added_question_id integer := 0;
	v_sp_crf_sh_added_question_id integer := 0;
	v_core_sym_count integer :=0;
	v_spcaq_id integer :=0;

	cursor_questions CURSOR(symptom_id integer) IS
	SELECT pcq.id from pro_ctc_questions pcq where pcq.pro_ctc_term_id=symptom_id order by pcq.id;

BEGIN
    -- get study participant crf id using form id
	SELECT spcs.study_participant_crf_id INTO v_sp_crf_id from sp_crf_schedules spcs where id=formid;
	IF answer=1 THEN
        -- get the count of page numbers present in sp_crf_sch_added_questions using form id
		SELECT count(page_number) INTO v_page_no FROM sp_crf_sch_added_questions scsaq where scsaq.sp_crf_schedule_id = formid;
		-- if count is 0 then get count of page numbers from mandatory questions using form id else get max page number from added questions
		IF NOT FOUND or v_page_no=0 THEN

			SELECT count(distinct crf_page_id) INTO v_page_no FROM crf_page_items cpi
			JOIN study_participant_crf_items spci ON cpi.id= spci.crf_item_id
			where spci.sp_crf_schedule_id = formid;
		ELSE
			SELECT max(page_number) INTO v_page_no FROM sp_crf_sch_added_questions scsaq where scsaq.sp_crf_schedule_id = formid;
		END IF;
		v_page_no := v_page_no+1;
		-- insert added symptom questions in sp_crf_added_questions and sp_crf_sch_added_questions for all the scheduled forms
		OPEN cursor_questions(symptomid);
		LOOP
		FETCH cursor_questions INTO v_question_id;
		EXIT WHEN NOT FOUND;
			SELECT spcaq.id INTO v_spcaq_id from sp_crf_added_questions spcaq where spcaq.sp_crf_id=v_sp_crf_id and spcaq.question_id=v_question_id;
			IF NOT FOUND THEN
				SELECT NEXTVAL('sp_crf_added_questions_id_seq') INTO v_sp_crf_added_question_id;

				INSERT INTO sp_crf_added_questions (id,version,sp_crf_id,question_id,page_number)
				VALUES (v_sp_crf_added_question_id,0,v_sp_crf_id,v_question_id,v_page_no);

				SELECT NEXTVAL('sp_crf_sch_added_questions_id_seq') INTO v_sp_crf_sh_added_question_id;

				INSERT INTO sp_crf_sch_added_questions (id,version, sp_crf_schedule_id,question_id,page_number,spc_added_question_id)
				VALUES (v_sp_crf_sh_added_question_id,0,formid,v_question_id,v_page_no,v_sp_crf_added_question_id);
			ELSE
				EXIT;
			END IF;
		END LOOP;
		CLOSE cursor_questions;
	END IF;
	IF answer=0 THEN
		OPEN cursor_questions(symptomid);
			LOOP
			FETCH cursor_questions INTO v_question_id;
				EXIT WHEN NOT FOUND;
				SELECT spcaq.id INTO v_spcaq_id FROM sp_crf_added_questions spcaq WHERE spcaq.sp_crf_id=v_sp_crf_id and spcaq.question_id=v_question_id;
				IF NOT FOUND THEN
					EXIT;
				ELSE
					DELETE FROM sp_crf_added_questions  WHERE sp_crf_id=v_sp_crf_id and question_id=v_question_id;
					DELETE FROM sp_crf_sch_added_questions WHERE sp_crf_schedule_id=formid and question_id=v_question_id;
				END IF;
			END LOOP;
		CLOSE cursor_questions;
	END IF;
		SELECT iscsc.core_sym_count into v_core_sym_count from ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
		IF NOT FOUND THEN
			return 0;
		END IF;
		IF corecategory=0 THEN
			UPDATE ivrs_sch_core_sym_count SET core_sym_count = v_core_sym_count+1 WHERE spc_schedule_id=formid;
		END IF;
		-- get the next un asked core symptom id.
		v_proc_ctc_term_id := ivrs_getcoresymptomid(userid,formid);
		IF v_proc_ctc_term_id=0 THEN
			return 0;
		END IF;
	return v_proc_ctc_term_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql';


-- ivrs_commitsession function complete the sceduled form upon completion of all questions and change the status to completed
--Input parameters:userid,pin and formid (scheduled form id,PK id of sp_crf_schedules)
--return: -2 for form not completed properly and still some questions needs to be answered, 1 for form completed, 0 for form not submitted properly, -1 for any DB issues
CREATE OR REPLACE FUNCTION IVRS_CommitSession(userid integer, formid integer, pin integer) RETURNS integer AS $x$
DECLARE
	v_next_question_id text :=0;
	v_ret_value integer :=1;
BEGIN
	v_next_question_id := ivrs_getfirstquestion(userid,formid);
	IF v_next_question_id = '0'
	then
		--delete the invalid questions
		v_ret_value := ivrs_deleteAddedQuestions(userid,formid);

		INSERT INTO sp_crf_schedule_notif(id, spc_schedule_id, status, creation_date)
		VALUES (nextval('sp_crf_schedule_notif_id_seq'),formid,'SCHEDULED',now());

		UPDATE sp_crf_schedules set status='COMPLETED',form_submission_mode='IVRS',form_completion_date=now() where id=formid and Status ='INPROGRESS';

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
$x$ LANGUAGE 'plpgsql';

-- Function: ivrs_getQuestionAnswer(userid integer, formid integer, questionid integer)
-- DROP FUNCTION ivrs_getQuestionAnswer(userid integer, formid integer, questionid integer);
--return:  return ivrs answer (1--None,2--Mild,3--Moderate,4--Severe,5--Very Severe etc) for given question, for 0 for there is no answer for given question, -1 for any DB issues
-- Function: ivrs_getquestionanswer(integer, integer, integer, integer)

CREATE OR REPLACE FUNCTION ivrs_getquestionanswer(userid integer, formid integer, questionid integer, questioncategory integer)
  RETURNS integer AS
$x$ DECLARE
    v_return_answer_id integer :=0;
    v_proctc_crf_item_id integer :=0;
    v_proctc_value_id integer :=0;
    v_question_type Text := '';
    v_display integer :=0;
    
BEGIN
	SELECT pcq.question_type INTO v_question_type
	from pro_ctc_questions pcq
        where pcq.id = questionid;

	IF questioncategory = 1 THEN
		--get the proctc question id using schedule question (crf item id)
		SELECT cpi.id INTO v_proctc_crf_item_id from crf_page_items cpi
		JOIN  study_participant_crf_items spci ON spci.crf_item_id = cpi.id
		where cpi.pro_ctc_question_id=questionid and spci.sp_crf_schedule_id=formid;
		--get the answer id for the given form and given question
		select pro_ctc_valid_value_id INTO v_proctc_value_id from study_participant_crf_items where sp_crf_schedule_id=formid and crf_item_id = v_proctc_crf_item_id;
	END IF;
	IF questioncategory = 0 THEN
		select pro_ctc_valid_value_id INTO v_proctc_value_id from sp_crf_sch_added_questions spcsaq where spcsaq.sp_crf_schedule_id = formid and question_id = questionid;
	END IF;
	--if answer not found for the question return 0
		IF v_proctc_value_id IS NULL THEN
			return -5;
		END IF;
	--get the IVRS answer type from valid values like 1,2 etc
		IF v_question_type='PRESENT' THEN
		    SELECT display_order INTO v_display FROM pro_ctc_valid_values where  pro_ctc_question_id = questionid and id =v_proctc_value_id;
			SELECT count(*) INTO  v_return_answer_id FROM pro_ctc_valid_values where pro_ctc_question_id = questionid and display_order<v_display;
		--	select count(*) INTO  v_return_answer_id from pro_ctc_valid_values where pro_ctc_question_id = questionid and id>v_proctc_value_id;
		ELSE
			select count(*) INTO  v_return_answer_id from pro_ctc_valid_values where pro_ctc_question_id = questionid and id<v_proctc_value_id;
		END IF;
	return v_return_answer_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_getpreviousquestion(userid integer, formid integer, questionid integer,questioncategory integer)
-- DROP FUNCTION ivrs_getpreviousquestion(userid integer, formid integer, questionid integer,questioncategory integer);
--return:  return previous question id and question category for given question,
--for 0 if there is no previous question, -1 for any DB issues,-2 for question is the first question id in the form
CREATE OR REPLACE FUNCTION ivrs_getpreviousquestion(userid integer, formid integer, questionid integer, questioncategory integer)
  RETURNS text AS $x$
DECLARE
    v_gender text := '';
    v_question_id integer :=0;
    v_temp_value_id integer :=0;
    v_tmp_crf_page_id integer :=0;
    v_page_present_num integer := 0;
    v_page_present_num1 integer := 0;
    v_curr_question_index integer := 1;
    v_previous_page_required integer := 0;
    v_ret_question_id integer := 0;
    v_question_id_found integer := 0;
    v_last_answered_question_id integer :=0;
    v_ret_questioncategory integer := 0;
    v_sym_count integer :=0;
    v_proc_ctc_term_id integer :=0;
    v_count integer :=0;
    v_page_previous_num integer :=0;
    
    --get the questions for given symptom/page
    curs_questions CURSOR(formid_in integer,gender_in text, pageid_in integer) IS
    SELECT cpi.pro_ctc_question_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
	from study_participant_crf_items spci
	join crf_page_items cpi ON cpi.id= spci.crf_item_id
	join crf_pages cp ON cp.id=cpi.crf_page_id
	join pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
	where cpi.crf_page_id = pageid_in and
	spci.sp_crf_schedule_id = formid_in and
	(pct.gender is null or pct.gender=gender_in or pct.gender ='both')
	order by cpi.crf_page_id,cpi.display_order;
    --get the added questions for given symptom/page
    curs_addedquestions CURSOR(formid_in integer,pageid_in integer) IS
    SELECT spcsaq.question_id,spcsaq.pro_ctc_valid_value_id,spcsaq.page_number
    FROM sp_crf_sch_added_questions spcsaq
    WHERE spcsaq.sp_crf_schedule_id=formid_in and spcsaq.page_number=pageid_in
    order by spcsaq.id;


BEGIN
	v_ret_questioncategory := questioncategory;
	--getting gender information of the participant
	SELECT lower(gender) into v_gender FROM participants where user_id=userid;
	--if question id sending is 0 then try to get last answered question
	IF questionid=0 THEN
	    --  questioncategory =0 then try to get last answered added question
	    --if not found then try to get the last core screening list(questioncategory=2)
	    -- if not found then try to get the last answered required questions(questioncategory=1)
		IF questioncategory=0 THEN
			SELECT spcsaq.question_id INTO v_last_answered_question_id from
			sp_crf_sch_added_questions spcsaq
			WHERE spcsaq.sp_crf_schedule_id=formid and pro_ctc_valid_value_id IS NOT NULL
			order by spcsaq.id desc LIMIT 1 OFFSET 0;

			IF NOT FOUND THEN
				v_ret_questioncategory :=2;
				--return '-3';
			END IF;
		END IF;
		IF questioncategory=2 or v_ret_questioncategory=2 THEN
		    -- to know which is the last core screening questions
			SELECT iscsc.core_sym_count INTO v_sym_count FROM ivrs_sch_core_sym_count iscsc
			WHERE iscsc.spc_schedule_id=formid;
			IF v_sym_count >0 THEN
				SELECT pct1.id INTO v_proc_ctc_term_id FROM pro_ctc_terms pct1 WHERE pct1.core='true' and pct1.id NOT IN
				(SELECT DISTINCT(pct.id) FROM pro_ctc_terms pct
				JOIN pro_ctc_questions pcq ON pcq.pro_ctc_term_id= pct.id
				JOIN crf_page_items cpi ON cpi.pro_ctc_question_id=pcq.id
				JOIN study_participant_crf_items spci ON spci.crf_item_id=cpi.id
				AND spci.sp_crf_schedule_id=formid and pct.core='true')
				AND pct1.id NOT IN
				(SELECT DISTINCT(pcq.pro_ctc_term_id) FROM pro_ctc_questions pcq
				JOIN sp_crf_added_questions spcaq ON spcaq.question_id = pcq.id
				JOIN sp_crf_schedules spcs ON (spcs.study_participant_crf_id=spcaq.sp_crf_id)
				WHERE spcs.id=formid)
				ORDER BY pct1.id LIMIT 1 OFFSET v_sym_count-1;
			END IF;
			IF NOT FOUND THEN
				v_ret_questioncategory :=1;
			ELSE
				return v_proc_ctc_term_id||'_'||v_ret_questioncategory;
			END IF;
		END IF;
		IF questioncategory=1 or v_ret_questioncategory=1 THEN
			SELECT cpi.pro_ctc_question_id INTO v_last_answered_question_id
			from study_participant_crf_items spci
			JOIN crf_page_items cpi ON cpi.id= spci.crf_item_id
			WHERE spci.sp_crf_schedule_id = formid and pro_ctc_valid_value_id
			IS NOT NULL order by spci.id desc LIMIT 1 OFFSET 0;

			IF NOT FOUND THEN
				return '-3';
			END IF;
		END IF;
		return  v_last_answered_question_id||'_'||v_ret_questioncategory;
	END IF;
	IF questioncategory=0 THEN
		--getting current page for the given question
		SELECT spcsaq.page_number INTO v_page_present_num1
		FROM sp_crf_sch_added_questions spcsaq
		WHERE spcsaq.sp_crf_schedule_id=formid and  spcsaq.question_id=questionid ;
		--traversing all the questions in current page to get valid answered question
		OPEN curs_addedquestions(formid,v_page_present_num1);
		LOOP
		FETCH curs_addedquestions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
		       EXIT WHEN NOT FOUND;
		       --check whether first question it self given question
			IF v_curr_question_index=1 and v_question_id = questionid THEN
				v_previous_page_required := 1;
				EXIT;
			END IF;
			--If current questions matches with the given question
			IF v_question_id = questionid THEN
				v_question_id_found := 1;
				EXIT;
			END IF;
			--traversing through next questions
			v_curr_question_index := v_curr_question_index + 1;
			--capturing previous question id
			v_ret_question_id := v_question_id;
		END LOOP;
		CLOSE curs_addedquestions;
		--traversing all the questions in previous page if given question is first question in current page to get valid answered question
		IF v_previous_page_required = 1 THEN
			OPEN curs_addedquestions(formid,v_page_present_num1-1);
			LOOP
			v_question_id_found:=v_question_id_found+1;
			FETCH curs_addedquestions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
			       EXIT WHEN NOT FOUND;

				IF v_temp_value_id IS NULL THEN
					EXIT;
				END IF;
				v_ret_question_id := v_question_id;

			END LOOP;
			CLOSE curs_addedquestions;
		END IF;
	END IF;
	-- if there are no added answered questions check in core screening list
	IF questioncategory=0 AND v_previous_page_required = 1 AND v_ret_question_id=0 THEN
		SELECT iscsc.core_sym_count INTO v_sym_count FROM ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
		IF NOT FOUND THEN
			v_ret_questioncategory :=  1;
			v_previous_page_required := 0;
			SELECT cpi.pro_ctc_question_id INTO v_ret_question_id
				from study_participant_crf_items spci
				JOIN crf_page_items cpi ON cpi.id= spci.crf_item_id
				WHERE spci.sp_crf_schedule_id = formid and pro_ctc_valid_value_id
				IS NOT NULL order by spci.id desc LIMIT 1 OFFSET 0;
				IF NOT FOUND THEN
					return '-3';
				END IF;
		ELSE
			v_ret_questioncategory :=2;
			v_previous_page_required := 0;
			SELECT pct1.id INTO v_proc_ctc_term_id FROM pro_ctc_terms pct1 WHERE pct1.core='true' and pct1.id NOT IN
			(SELECT DISTINCT(pct.id) FROM pro_ctc_terms pct
			JOIN pro_ctc_questions pcq ON pcq.pro_ctc_term_id= pct.id
			JOIN crf_page_items cpi ON cpi.pro_ctc_question_id=pcq.id
			JOIN study_participant_crf_items spci ON spci.crf_item_id=cpi.id
			AND spci.sp_crf_schedule_id=formid and pct.core='true')
			AND pct1.id NOT IN
			(SELECT  DISTINCT(pcq.pro_ctc_term_id) from pro_ctc_questions pcq
			JOIN sp_crf_sch_added_questions spcsaq ON spcsaq.question_id=pcq.id
			WHERE spcsaq.sp_crf_schedule_id=formid and spcsaq.pro_ctc_valid_value_id IS NOT NULL )
			ORDER BY pct1.id LIMIT 1 OFFSET v_sym_count-1;
			return v_proc_ctc_term_id||'_'||v_ret_questioncategory;
		END IF;
	END IF;
	IF  questioncategory=1 THEN
		--getting current page for the given question
		SELECT cpi.crf_page_id INTO v_page_present_num
		from crf_page_items cpi
		join study_participant_crf_items spci ON cpi.id= spci.crf_item_id
		where cpi.pro_ctc_question_id=questionid and spci.sp_crf_schedule_id = formid;
		--traversing all the questions in current page to get valid answered question
		OPEN curs_questions(formid,v_gender,v_page_present_num);
		LOOP
		FETCH curs_questions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
		       EXIT WHEN NOT FOUND;
			--check whether first question it self given question
			IF v_curr_question_index=1 and v_question_id = questionid THEN
				v_previous_page_required := 1;
				EXIT;
			END IF;
			--If current questions matches with the given question
			IF v_question_id = questionid THEN
				v_question_id_found := 1;
				EXIT;
			END IF;
			--traversing through next questions
			v_curr_question_index := v_curr_question_index + 1;
			--capturing previous question id
			v_ret_question_id := v_question_id;
		END LOOP;
		CLOSE curs_questions;
		--traversing all the questions in previous page if given question is first question in current page to get valid answered question
		IF v_previous_page_required = 1 THEN
		    -- update for the issue PRKC-1124 
		    SELECT count(*) INTO v_count
			from study_participant_crf_items spci
			join crf_page_items cpi ON cpi.id= spci.crf_item_id
			join crf_pages cp ON cp.id=cpi.crf_page_id
			join pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
			where cpi.crf_page_id < v_page_present_num and
			spci.sp_crf_schedule_id = formid and
			(pct.gender is null or pct.gender=v_gender or pct.gender ='both');
            IF v_count>0 THEN
              SELECT cpi.crf_page_id INTO v_page_previous_num
              from study_participant_crf_items spci
              join crf_page_items cpi ON cpi.id= spci.crf_item_id
              join crf_pages cp ON cp.id=cpi.crf_page_id
              join pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
              where cpi.crf_page_id < v_page_present_num and
              spci.sp_crf_schedule_id = formid and
              (pct.gender is null or pct.gender=v_gender or pct.gender ='both')
              order by cpi.crf_page_id,cpi.display_order LIMIT 1 offset v_count-1;
			END IF;
			OPEN curs_questions(formid,v_gender,v_page_previous_num);
			LOOP
			v_question_id_found:=v_question_id_found+1;
			FETCH curs_questions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
			       EXIT WHEN NOT FOUND;
				IF v_temp_value_id IS NULL THEN
					EXIT;
				END IF;
				v_ret_question_id := v_question_id;
			END LOOP;
			CLOSE curs_questions;
		END IF;
	END IF;
	--if given question id is the first question then return -2
		IF v_previous_page_required = 1 AND v_ret_question_id = 0 then
			return '-2';
		end if;
		--if there are no valid un answered questions then return 0
		IF v_question_id_found = 0 then
			return '0';
		end if;
	return v_ret_question_id||'_'||v_ret_questioncategory;
EXCEPTION
    WHEN OTHERS THEN
return '-1';
END;
$x$ LANGUAGE 'plpgsql';

-- -- Function: ivrs_getprevious_coresymid(userid integer, formid integer, symptomid integer)
-- DROP FUNCTION ivrs_getprevious_coresymid(userid integer, formid integer, symptomid integer);
--return:  return previous symptom id and question category for given symptomid,
--for questionsID_questionsCategory if there is no previous symptoms(0=additional questions and 1=regular questions),
--'-1' for any DB issues,-2 for question is the first question id in the form

CREATE OR REPLACE FUNCTION ivrs_getprevious_coresymid(userid integer, formid integer, symptomid integer)
  RETURNS text AS $x$
DECLARE
	v_question_id integer :=0;
	v_ret_questioncategory integer :=0;
	v_proc_ctc_term_id integer :=0;
	v_core_sym_count integer :=0;
	v_sym_row_id integer :=0;
BEGIN
	v_ret_questioncategory := 2;
	-- get the count of core screening questions asked
	SELECT iscsc.core_sym_count into v_core_sym_count from ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
    -- if not found then return the last answered added question or regular question
	IF NOT FOUND or v_core_sym_count=0 THEN
		SELECT spcsaq.question_id INTO v_question_id FROM
		sp_crf_sch_added_questions spcsaq
		WHERE spcsaq.sp_crf_schedule_id=formid and pro_ctc_valid_value_id IS NOT NULL
		order by spcsaq.id desc LIMIT 1 OFFSET 0;

		v_ret_questioncategory :=0;
		IF NOT FOUND THEN
			v_ret_questioncategory :=1;

			SELECT cpi.pro_ctc_question_id INTO v_question_id
			from study_participant_crf_items spci
			JOIN crf_page_items cpi ON cpi.id= spci.crf_item_id
			WHERE spci.sp_crf_schedule_id = formid and pro_ctc_valid_value_id
			IS NOT NULL order by spci.id desc LIMIT 1 OFFSET 0;

			IF NOT FOUND THEN
				return '-2';
			END IF;
			return v_question_id||'_'||v_ret_questioncategory;
		END IF;
		return v_question_id||'_'||v_ret_questioncategory;
	END IF;
	-- based on core symptom count get the asked previous core symptom id.
	IF v_core_sym_count>0 THEN
		v_ret_questioncategory := 2;


		SELECT count(p.id) INTO v_sym_row_id FROM pro_ctc_terms p
		JOIN pro_ctc_terms pct1 ON pct1.id=p.id
		WHERE pct1.core='true' and p.core='true' and p.id < symptomid and pct1.id NOT IN
		(SELECT DISTINCT(pct.id) FROM pro_ctc_terms pct
		JOIN pro_ctc_questions pcq ON pcq.pro_ctc_term_id= pct.id
		JOIN crf_page_items cpi ON cpi.pro_ctc_question_id=pcq.id
		JOIN study_participant_crf_items spci ON spci.crf_item_id=cpi.id
		AND spci.sp_crf_schedule_id=formid and pct.core='true')
		AND pct1.id NOT IN
		(SELECT  DISTINCT(pcq.pro_ctc_term_id) from pro_ctc_questions pcq
		JOIN sp_crf_sch_added_questions spcsaq ON spcsaq.question_id=pcq.id
		WHERE spcsaq.sp_crf_schedule_id=formid and spcsaq.pro_ctc_valid_value_id IS NOT NULL);

		IF v_sym_row_id >0 THEN
			SELECT pct1.id INTO v_proc_ctc_term_id FROM pro_ctc_terms pct1
			WHERE pct1.core='true' and pct1.id NOT IN
			(SELECT DISTINCT(pct.id) FROM pro_ctc_terms pct
			JOIN pro_ctc_questions pcq ON pcq.pro_ctc_term_id= pct.id
			JOIN crf_page_items cpi ON cpi.pro_ctc_question_id=pcq.id
			JOIN study_participant_crf_items spci ON spci.crf_item_id=cpi.id
			AND spci.sp_crf_schedule_id=formid and pct.core='true')
			AND pct1.id NOT IN
			(SELECT  DISTINCT(pcq.pro_ctc_term_id) from pro_ctc_questions pcq
			JOIN sp_crf_sch_added_questions spcsaq ON spcsaq.question_id=pcq.id
			WHERE spcsaq.sp_crf_schedule_id=formid and spcsaq.pro_ctc_valid_value_id IS NOT NULL )
			ORDER BY pct1.id LIMIT 1 OFFSET v_sym_row_id-1;
		END IF;
		-- if not found then check for last answered added or regular question.
		IF NOT FOUND or v_sym_row_id =0 THEN
			SELECT spcsaq.question_id INTO v_question_id FROM
			sp_crf_sch_added_questions spcsaq
			WHERE spcsaq.sp_crf_schedule_id=formid and pro_ctc_valid_value_id IS NOT NULL
			order by spcsaq.id desc LIMIT 1 OFFSET 0;
			v_ret_questioncategory :=0;
			IF NOT FOUND THEN
				v_ret_questioncategory :=1;
				SELECT cpi.pro_ctc_question_id INTO v_question_id
				from study_participant_crf_items spci
				JOIN crf_page_items cpi ON cpi.id= spci.crf_item_id
				WHERE spci.sp_crf_schedule_id = formid and pro_ctc_valid_value_id
				IS NOT NULL order by spci.id desc LIMIT 1 OFFSET 0;
				IF NOT FOUND THEN
					return '-2';
				END IF;
				return v_question_id||'_'||v_ret_questioncategory;
			END IF;
			return v_question_id||'_'||v_ret_questioncategory;
		END IF;
	END IF;
	return v_proc_ctc_term_id||'_'||v_ret_questioncategory;
EXCEPTION
    WHEN OTHERS THEN
return '-1';
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_isUserNew(userid integer)
-- DROP FUNCTION ivrs_isUserNew(userid integer);
--return:  return 1 for new users to IVRS ,0 for existing users to IVRS
--return : -1 for any DB issues,-2 for question is the first question id in the form
CREATE OR REPLACE FUNCTION IVRS_isUserNew(userid integer) RETURNS integer AS $x$
DECLARE
	v_ret_first_count integer :=0;
	v_sch_id integer :=0;
	v_gender text := '';
	v_ret_second_count integer :=0;
	v_set_count integer :=0;
    -- select all the inprogress and completed forms for the user
	curs_inprogress_forms CURSOR(user_id integer) IS
	SELECT spcs.id
	FROM sp_crf_schedules spcs
	JOIN study_participant_crfs spc ON  spcs.study_participant_crf_id=spc.id
	JOIN study_participant_assignments sp ON spc.study_participant_id = sp.id
	JOIN participants p ON sp.participant_id = p.id
	WHERE (spcs.status= 'INPROGRESS' OR spcs.status = 'COMPLETED') AND p.user_id=user_id ORDER BY spcs.id;

BEGIN
    -- get the gender for the user
	SELECT lower(gender) INTO v_gender FROM participants WHERE user_id=userid;
    -- get the number of forms which are being submitted through IVRS.
    -- return 0, if number of forms are greater than 0
    --else, find the number of questions submitted through IVRS, in both complated and inprogress forms
	SELECT count(spcs.id) INTO v_ret_first_count
	FROM sp_crf_schedules spcs
	JOIN study_participant_crfs spc ON  spcs.study_participant_crf_id=spc.id
	JOIN study_participant_assignments sp ON spc.study_participant_id = sp.id
	JOIN participants p ON sp.participant_id = p.id
	where (spcs.form_submission_mode='IVRS' and (spcs.status = 'COMPLETED' OR spcs.status= 'INPROGRESS'))
	and p.user_id=userid;
	IF v_ret_first_count > 0 THEN
		return 0;
	ELSE
		OPEN curs_inprogress_forms(userid);
		LOOP
			FETCH curs_inprogress_forms INTO v_sch_id;
				EXIT WHEN NOT FOUND;
                -- if count is greater than 0 then return 0 or else set count as 0
				SELECT count(spci.response_mode) INTO v_ret_second_count
				FROM study_participant_crf_items spci
				JOIN crf_page_items cpi ON cpi.id= spci.crf_item_id
				JOIN crf_pages cp ON cp.id=cpi.crf_page_id
				JOIN pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
				WHERE spci.sp_crf_schedule_id = v_sch_id AND spci.response_mode='IVRS'
				AND (pct.gender is null or pct.gender=v_gender or pct.gender ='both');
				IF v_ret_second_count >0 THEN
					return 0;
				ELSE
					v_set_count :=0;
				END IF;

			END LOOP;
			CLOSE curs_inprogress_forms;
			-- if count is 1 then return 0 (existing users)
			-- if count is 0 then return 1 (new user)
		IF v_set_count =1 THEN
			return 0;
		ELSE
			return 1;
		END IF;
	END IF;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;


-- Function: ivrs_getFormRecallPeriod(userid integer,formid integer)
-- DROP FUNCTION ivrs_getFormRecallPeriod(userid integer,formid integer);
--return:  return 1 for weekly period,return 2 for monthly, return 3 for last treatment,
-->return >3 for custom recall periods, -1 for any DB issues
CREATE OR REPLACE FUNCTION ivrs_getFormRecallPeriod(userid integer, formid integer) RETURNS integer AS $x$
DECLARE
    v_recall_period text := '';
    v_return_recall_period integer := 0;
BEGIN
        --get recall period for given form
        select lower(c.recall_period) INTO v_recall_period
        from crfs c
        JOIN study_participant_crfs spc ON c.id=spc.crf_id
        JOIN sp_crf_schedules scs ON spc.id=scs.study_participant_crf_id
	where scs.id=formid;
     --if there is no recall period, then return 0
     IF NOT FOUND THEN
	    return 0;
     END IF;
     --if recall period weekly then 1, monthly-2, last treatment-3 and for custom -4, later on this numbers will get keep on increase
     IF v_recall_period='over the past 7 days' THEN
	v_return_recall_period := 1;
     ELSIF v_recall_period='over the past 30 days' THEN
	v_return_recall_period := 2;
     ELSIF v_recall_period='since your last cancer treatment' THEN
	v_return_recall_period := 3;
     ELSE
	v_return_recall_period := 4;
     END IF;
     return v_return_recall_period;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql';

-- Function: ivrs_deleteaddedquestions(userid integer, formid integer)
-- DROP FUNCTION ivrs_deleteaddedquestions(userid integer, formid integer);
-- return: 0 if deletion of questions which are answered yes  is done
-- return :1 if there are any exceptions.
CREATE OR REPLACE FUNCTION ivrs_DeleteAddedQuestions(userid integer, formid integer)
  RETURNS integer AS $x$
DECLARE
	v_page_number integer :=0;

curs_questions CURSOR(formid_in integer) IS
    select scsaq.page_number from sp_crf_sch_added_questions scsaq
    JOIN pro_ctc_questions pcq ON scsaq.question_id=pcq.id JOIN pro_ctc_valid_values pcvv ON scsaq.pro_ctc_valid_value_id = pcvv.id
    where scsaq.sp_crf_schedule_id = formid_in
    and pcq.display_order=1 and pcvv.display_order=0;
BEGIN
	--check for the added questions whose valid values is equal to none/never/not at all.
	OPEN curs_questions(formid);
	LOOP
	FETCH curs_questions INTO v_page_number;
		EXIT WHEN NOT FOUND;
	        DELETE from sp_crf_added_questions  WHERE page_number=v_page_number and sp_crf_id IN
	        (SELECT scs.study_participant_crf_id from sp_crf_schedules scs WHERE scs.id=formid ) ;
	END LOOP;
	CLOSE curs_questions;
	return 0;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_getcoresymptomid(userid integer, formid integer)
-- DROP FUNCTION ivrs_getcoresymptomid(userid integer, formid integer);
CREATE OR REPLACE FUNCTION ivrs_GetCoreSymptomID(userid integer, formid integer)
  RETURNS integer AS $x$
DECLARE
    v_proc_ctc_term_id integer :=0;
    v_core_count integer :=0;
    v_ivrs_sch_core_sym_count_id integer :=0;
    v_core_sym_count integer :=0;
BEGIN
    -- check if there is any entry for the given scheduled form id if not then insert a new record in the table
	SELECT count(*) into v_core_count from ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
	IF NOT FOUND or v_core_count=0 THEN
		SELECT NEXTVAL('ivrs_sch_core_sym_count_id_seq') INTO v_ivrs_sch_core_sym_count_id;
		INSERT INTO ivrs_sch_core_sym_count (id,version,spc_schedule_id,core_sym_count)
		VALUES (v_ivrs_sch_core_sym_count_id,0,formid,0);
	ELSE
		SELECT iscsc.core_sym_count into v_core_sym_count from ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
	END IF;
	-- get the next symptom id using count.
	SELECT pct1.id INTO v_proc_ctc_term_id FROM pro_ctc_terms pct1 WHERE pct1.core='true' and pct1.id NOT IN
	(SELECT DISTINCT(pct.id) FROM pro_ctc_terms pct
	JOIN pro_ctc_questions pcq ON pcq.pro_ctc_term_id= pct.id
	JOIN crf_page_items cpi ON cpi.pro_ctc_question_id=pcq.id
	JOIN study_participant_crf_items spci ON spci.crf_item_id=cpi.id
	AND spci.sp_crf_schedule_id=formid and pct.core='true')
	AND pct1.id NOT IN
	(SELECT  DISTINCT(pcq.pro_ctc_term_id) from pro_ctc_questions pcq
	JOIN sp_crf_sch_added_questions spcsaq ON spcsaq.question_id=pcq.id
	WHERE spcsaq.sp_crf_schedule_id=formid and spcsaq.pro_ctc_valid_value_id IS NOT NULL )
	ORDER BY pct1.id LIMIT 1 OFFSET v_core_sym_count;

  IF NOT FOUND THEN
	return 0;
  END IF;
	--UPDATE ivrs_sch_core_sym_count SET core_sym_count = v_core_sym_count+1 WHERE spc_schedule_id=formid;
	return v_proc_ctc_term_id;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_getcoresymptomfilename(serid integer, formid integer, symptomid integer)
-- DROP FUNCTION ivrs_getcoresymptomfilename(serid integer, formid integer, symptomid integer);  \
-- return : file name for given symptom id.
CREATE OR REPLACE FUNCTION ivrs_GetCoreSymptomFileName(userid integer, formid integer, symptomid integer)
  RETURNS text AS $x$
DECLARE
    v_symptom_file text := '';
BEGIN
	SELECT pct.file_name INTO v_symptom_file FROM pro_ctc_terms pct WHERE id=symptomid;
	IF NOT FOUND THEN
		v_symptom_file:= 'Data Not Found';
        END IF;
	return v_symptom_file;
EXCEPTION
    WHEN OTHERS THEN
    return 'Data Not Found';
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_get_sym_ques_ans(userid integer, formid integer, symptomid integer, corecategory integer)
-- DROP FUNCTION ivrs_get_sym_ques_ans(userid integer, formid integer, symptomid integer, corecategory integer);
-- return: 2(NO) or 1(YES) based on core category which determines if 7 (previous quesitons is asked or not)
-- return: 0 if previous question is not asked.
CREATE OR REPLACE FUNCTION ivrs_Get_Sym_Ques_Ans(userid integer, formid integer, symptomid integer, corecategory integer)
  RETURNS integer AS $x$
DECLARE
	v_spcaq_id integer :=0;
	v_sp_crf_id integer :=0;
	v_question_id integer :=0 ;
BEGIN
	IF corecategory=1 THEN
		SELECT spcs.study_participant_crf_id INTO v_sp_crf_id from sp_crf_schedules spcs where id=formid;

		SELECT pcq.id INTO v_question_id FROM pro_ctc_terms pct
		JOIN pro_ctc_questions pcq ON pcq.pro_ctc_term_id=pct.id
		WHERE pct.id=symptomid
		ORDER BY pcq.id LIMIT 1;

		SELECT spcaq.id INTO v_spcaq_id FROM sp_crf_added_questions spcaq
		WHERE spcaq.question_id=v_question_id AND spcaq.sp_crf_id=v_sp_crf_id ;
		IF NOT FOUND THEN
			return 2;
		END IF;
		return 1;
	ELSE
		return 0;
	END IF;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_save_filepath(userid integer, formid integer, filepath text)
-- DROP FUNCTION ivrs_save_filepath(userid integer, formid integer, filepath text);
-- return: 1(successfully updated) or 0(not successfully updated)
-- return: -1 if there are any exceptions

CREATE OR REPLACE FUNCTION IVRS_Save_FilePath(userid integer, formid integer,filepath text)
  RETURNS integer AS $x$
DECLARE
BEGIN
    -- update sp_crf_schedules with the given filepath.
	UPDATE sp_crf_schedules SET file_path=filepath WHERE id=formid AND Status ='INPROGRESS';
	IF FOUND THEN
		return 1;
	ELSE
		return 0;
	END IF;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_get_no_of_unans_symps(userid integer, formid integer)
-- DROP FUNCTION ivrs_get_no_of_unans_symps(userid integer, formid integer);
-- return: number of unanswered symptoms in the given form.
-- return: -1 if there are any exceptions

CREATE OR REPLACE FUNCTION ivrs_get_no_of_unans_symps(userid integer, formid integer) RETURNS integer AS $x$
DECLARE
	v_page_id integer := 0;
	v_gender text := '';
	v_question_id integer :=0;
	v_first_question_value integer := 0;
	v_current_answer_value integer :=0;
	v_question_skipped integer :=0;
	v_temp_value_id integer :=0;
	v_tmp_crf_page_id integer :=0;
	v_count integer :=0;
	v_pcqid integer :=0;
	v_temp_count integer :=0;
	v_count_core integer :=0;
	v_core_count integer :=0;
	v_core_sym_count integer :=0;

	-- to retrieve all the regular questions for the form based on participant's gender
	curs_questions CURSOR(formid_in integer,gender_in text) IS
	SELECT cpi.pro_ctc_question_id,spci.pro_ctc_valid_value_id,cpi.crf_page_id
	FROM study_participant_crf_items spci
	JOIN crf_page_items cpi ON cpi.id= spci.crf_item_id
	JOIN crf_pages cp ON cp.id=cpi.crf_page_id
	JOIN pro_ctc_terms pct ON pct.id=cp.pro_ctc_term_id
	WHERE spci.sp_crf_schedule_id = formid_in AND (pct.gender IS NULL OR pct.gender=gender_in OR pct.gender ='both')
	ORDER BY spci.crf_item_id,cpi.display_order;
	--to retrieve all the participant added questions for the form, from sp_crf_sch_added_questions
	curs_added_question CURSOR(formid_in integer) IS
	SELECT pcq.id,spcaq.pro_ctc_valid_value_id,spcaq.page_number
	FROM pro_ctc_questions pcq
	JOIN sp_crf_sch_added_questions spcaq ON spcaq.question_id=pcq.id
	WHERE spcaq.sp_crf_schedule_id=formid_in
	ORDER BY spcaq.id,pcq.display_order;
	--to retrieve all the participant added questions for the form, from sp_crf_added_questions
	curs_sp_added_question CURSOR(formid_in integer) IS
	SELECT pcq.id,spcaq.id,spcaq.page_number FROM pro_ctc_questions pcq
	JOIN sp_crf_added_questions spcaq ON spcaq.question_id = pcq.id
	JOIN sp_crf_schedules spcs ON (spcs.study_participant_crf_id=spcaq.sp_crf_id)
	WHERE spcs.id=formid_in ORDER BY spcaq.page_number,pcq.display_order;

BEGIN
	--getting gender information of the participant
	SELECT lower(gender) INTO v_gender FROM participants WHERE user_id=userid;

	--Going through all the questions for the form and getting the number of unanswered symptoms.
	OPEN curs_questions(formid,v_gender);
	LOOP
	FETCH curs_questions INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
	       EXIT WHEN NOT FOUND;
		--when new page question or symptom comes
		IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
			v_page_id := v_tmp_crf_page_id;
			v_first_question_value := v_temp_value_id;
			--if first question which is condtional question and the same is not answered
			--then increase the symptom count by 1
			IF v_first_question_value IS NULL THEN
				v_count:=v_count+1;
			ELSE
				--checking for the questions in the page to be skipped or not
				SELECT count(id) INTO v_question_skipped
				FROM crf_page_item_display_rules
				WHERE pro_ctc_valid_value_id = v_first_question_value;
			END IF;
		END IF;
		v_current_answer_value := v_temp_value_id;
		--for the non condtional questions, check for the presence of answer while skip option is not applicable
		-- and when new page or symptom comes then increase symptom count by 1
		IF v_current_answer_value IS NULL AND v_question_skipped > 0 AND v_page_id=v_tmp_crf_page_id THEN
			 v_count=v_count+1;
			 v_question_skipped:=0;
		END IF;
	END LOOP;
	CLOSE curs_questions;

	--Going through all the added questions for the form and getting the number of unanswered symptoms.
	OPEN curs_added_question(formid);
		    LOOP
		    FETCH curs_added_question INTO v_question_id,v_temp_value_id,v_tmp_crf_page_id;
			   EXIT WHEN NOT FOUND;
			   --when new page question or symptom comes
			   IF v_page_id=0 OR v_page_id<>v_tmp_crf_page_id THEN
				v_page_id := v_tmp_crf_page_id;
				v_first_question_value := v_temp_value_id;
				--if first question which is condtional question and the same is not answered
				--then increase the symptom count by 1
				IF v_first_question_value IS NULL THEN
					v_count:=v_count+1;
				ELSE
					--checking for the questions in the page to be skipped or not
					SELECT count(id) INTO v_question_skipped
					FROM crf_page_item_display_rules
					WHERE pro_ctc_valid_value_id = v_first_question_value;
				END IF;
			    END IF;
			v_current_answer_value := v_temp_value_id;
			--for the non condtional questions, check for the presence of answer while skip option is not applicable
			-- and when new page or symptom comes then increase symptom count by 1
			IF v_current_answer_value IS NULL AND v_question_skipped > 0 AND v_page_id=v_tmp_crf_page_id THEN
				v_count=v_count+1;
				v_question_skipped:=0;
			END IF;
	END LOOP;
	CLOSE curs_added_question;
	-- check whether there are any participant added questions in the sp_crf_sch_added_questions,
	-- if not found then get the number of symptoms to be added for that form at run time.
	SELECT pcq.id INTO v_pcqid FROM pro_ctc_questions pcq
	JOIN sp_crf_sch_added_questions spcaq ON spcaq.question_id=pcq.id
	WHERE spcaq.sp_crf_schedule_id=formid ORDER BY spcaq.id,pcq.display_order;
	IF NOT found THEN
		SELECT count(distinct(pcq.pro_ctc_term_id)) INTO v_temp_count FROM pro_ctc_questions pcq
		JOIN sp_crf_added_questions spcaq ON spcaq.question_id = pcq.id
		JOIN sp_crf_schedules spcs ON (spcs.study_participant_crf_id=spcaq.sp_crf_id)
		WHERE spcs.id=formid;
	ELSE
		v_temp_count:=0;
	END IF;
	--Check if there is an entry in ivrs_sch_core_sym_count for the given form
	--if yes then get the core sym count for that form
	SELECT count(*) INTO v_core_count FROM ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
	IF found OR v_core_count>0 THEN
		SELECT iscsc.core_sym_count INTO v_core_sym_count FROM ivrs_sch_core_sym_count iscsc WHERE iscsc.spc_schedule_id=formid;
	END IF;
	-- get the count of the unanswered core symptom flow which are not consumed
	SELECT count(pct2.id) INTO v_count_core from pro_ctc_terms pct2 where pct2.id IN
	(SELECT pct1.id  FROM pro_ctc_terms pct1 WHERE pct1.core='true' and pct1.id NOT IN
	(SELECT DISTINCT(pct.id) FROM pro_ctc_terms pct
	JOIN pro_ctc_questions pcq ON pcq.pro_ctc_term_id= pct.id
	JOIN crf_page_items cpi ON cpi.pro_ctc_question_id=pcq.id
	JOIN study_participant_crf_items spci ON spci.crf_item_id=cpi.id
	AND spci.sp_crf_schedule_id=formid and pct.core='true')
	AND pct1.id NOT IN
	(SELECT  DISTINCT(pcq.pro_ctc_term_id) from pro_ctc_questions pcq
	JOIN sp_crf_sch_added_questions spcsaq ON spcsaq.question_id=pcq.id
	WHERE spcsaq.sp_crf_schedule_id=formid and spcsaq.pro_ctc_valid_value_id IS NOT NULL )
	OFFSET v_core_sym_count);
	-- return the sum of added core symptom, regular quetion symptom and core screening symptoms.
	return v_temp_count+v_count+v_count_core;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_getform_status(userid integer, formid integer)
-- DROP FUNCTION ivrs_getform_status(userid integer, formid integer);
-- return 0 if the form is schedueled and 1 if the form is in INPROGRESS
-- return -2 if there is no record found for the given form
-- return -1 for exception

CREATE OR REPLACE FUNCTION ivrs_getform_status(userid integer, formid integer) RETURNS integer AS $x$
DECLARE
    v_form_stauts text :='';
BEGIN
    -- get the given form status
	SELECT spcs.status INTO v_form_stauts
	FROM sp_crf_schedules spcs
	JOIN study_participant_crfs spc ON spcs.study_participant_crf_id=spc.id
	JOIN study_participant_assignments sp ON spc.study_participant_id= sp.id
	JOIN participants p ON sp.participant_id = p.id
	WHERE spcs.start_date <=current_date AND spcs.id=formid
	AND p.user_id=userid;
    -- if no record found return -2
	IF NOT FOUND THEN
		return -2;
	END IF;
	-- if form is inprogress return 1 and 0 otherwise.
	IF v_form_stauts='INPROGRESS' THEN
		return 1;
	ELSE
		return 0;
	END IF;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$x$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: delete_participant_timestamp(userid integer)

-- DROP FUNCTION delete_participant_timestamp(userid integer);
-- Deletes the timestamp of a participant when IVRS call is dropped
CREATE OR REPLACE FUNCTION delete_participant_timestamp(userid integer)
  RETURNS integer AS
$x$
  DECLARE
      v_ret integer :=0;
  BEGIN
	SELECT spa.id INTO v_ret FROM study_participant_assignments spa
	JOIN participants p ON spa.participant_id=p.id
	WHERE p.user_id = userid;

	UPDATE study_participant_assignments SET live_access_timestamp = null WHERE id =v_ret;

       return 0;
  EXCEPTION
      WHEN OTHERS THEN
      return -1;
  END;
  $x$
  LANGUAGE 'plpgsql' VOLATILE;

-- Function: set_participant_timestamp(userid integer)

-- DROP FUNCTION set_participant_timestamp(userid integer);
-- sets the timestamp of a participant when IVRS call got initiated
CREATE OR REPLACE FUNCTION set_participant_timestamp(userid integer)
  RETURNS integer AS
$x$
  DECLARE
      v_ret integer :=0;
  BEGIN
	SELECT spa.id INTO v_ret FROM study_participant_assignments spa
	JOIN participants p ON spa.participant_id=p.id
	WHERE p.user_id = userid;
	UPDATE study_participant_assignments SET live_access_timestamp = now() WHERE id =v_ret;

       return 0;
  EXCEPTION
      WHEN OTHERS THEN
      return -1;
  END;
  $x$
  LANGUAGE 'plpgsql' VOLATILE;



--Function: unaccent(text)
-- replaces accented chars ()ONLY a,e,i,o,u and n with tilde) with the corresponding non accented chars.
-- used to fetch úlcer when ulcer is typed.
CREATE OR REPLACE FUNCTION unaccent(text) RETURNS text AS $BODY$
BEGIN
    RETURN translate($1, 'áéíóúÁÉÍÓÚñÑ', 'aeiouAEIOUnN');
END;
$BODY$ LANGUAGE 'plpgsql' VOLATILE;

-- --Function: ivrs_updateques_filename()
--
-- --DROP FUNCTION ivrs_updateques_filename();
--
-- CREATE OR REPLACE FUNCTION ivrs_updateques_filename()
--   RETURNS integer AS
-- $x$
-- DECLARE
-- 	v_count integer :=1;
-- 	v_value text :='';
-- BEGIN
--
-- 	FOR i IN 1..126 LOOP
-- 		v_value :='question'||i;
-- 		UPDATE pro_ctc_questions SET question_file_name=v_value WHERE id=i;
--
--
-- 	END LOOP;
--
-- 	return 0;
-- 	--WHILE v_count < 126 LOOP
-- 	--	UPDATE pro_ctc_questions SET question_file_name='question' WHERE id=v_count;
-- 	--	v_count := v_count + 1;
-- 	--END LOOP;
--
-- EXCEPTION
--     WHEN OTHERS THEN
--     return -1;
-- END;
--
-- $x$
--   LANGUAGE 'plpgsql' VOLATILE;


CREATE OR REPLACE FUNCTION ivrs_updateques_filename()
  RETURNS integer AS
$x$
DECLARE
	v_count integer :=1;
	v_value text :='';
	v_question_term text :='';
BEGIN
    UPDATE pro_ctc_questions SET question_file_name='question1'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have ACHING JOINTS (SUCH AS ELBOWS, KNEES, SHOULDERS)');
	UPDATE pro_ctc_questions SET question_file_name='question2'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your ACHING JOINTS (SUCH AS ELBOWS, KNEES, SHOULDERS) at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question3'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did ACHING JOINTS (SUCH AS ELBOWS, KNEES, SHOULDERS) INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question4'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have ACHING MUSCLES');
	UPDATE pro_ctc_questions SET question_file_name='question5'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your ACHING MUSCLES at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question6'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did ACHING MUSCLES INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question7'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your ACNE OR PIMPLES ON THE FACE OR CHEST at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question8'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you FEEL ANXIETY');
	UPDATE pro_ctc_questions SET question_file_name='question9'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your ANXIETY at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question10'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did ANXIETY INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question11'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have ARM OR LEG SWELLING');
	UPDATE pro_ctc_questions SET question_file_name='question12'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your ARM OR LEG SWELLING at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question13'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did ARM OR LEG SWELLING INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question14'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any BED SORES');
	UPDATE pro_ctc_questions SET question_file_name='question15'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have BLOATING OF THE ABDOMEN (BELLY)');
	UPDATE pro_ctc_questions SET question_file_name='question16'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your BLOATING OF THE ABDOMEN (BELLY) at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question17'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your BLURRY VISION at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question18'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did BLURRY VISION INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question19'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your BODY ODOR at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question20'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your BREAST AREA ENLARGEMENT OR TENDERNESS at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question21'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you BRUISE EASILY (BLACK AND BLUE MARKS)');
	UPDATE pro_ctc_questions SET question_file_name='question22'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any CHANGE IN THE COLOR OF YOUR FINGERNAILS OR TOENAILS');
	UPDATE pro_ctc_questions SET question_file_name='question23'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your CONSTIPATION at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question24'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your COUGH at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question25'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did COUGH INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question26'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your DECREASED APPETITE at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question27'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did DECREASED APPETITE INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question28'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your DECREASED SEXUAL INTEREST at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question29'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have an UNEXPECTED DECREASE IN SWEATING');
	UPDATE pro_ctc_questions SET question_file_name='question30'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your DIFFICULTY GETTING OR KEEPING AN ERECTION at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question31'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your DIFFICULTY SWALLOWING at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question32'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your DIZZINESS at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question33'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did DIZZINESS INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question34'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your DRY MOUTH at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question35'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your DRY SKIN at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question36'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have EJACULATION PROBLEMS');
	UPDATE pro_ctc_questions SET question_file_name='question37'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your FATIGUE, TIREDNESS, OR LACK OF ENERGY at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question38'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did FATIGUE, TIREDNESS, OR LACK OF ENERGY INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question39'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you FEEL THAT NOTHING COULD CHEER YOU UP');
	UPDATE pro_ctc_questions SET question_file_name='question40'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your FEELINGS THAT NOTHING COULD CHEER YOU UP at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question41'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did FEELING THAT NOTHING COULD CHEER YOU UP INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question42'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any FLASHING LIGHTS IN FRONT OF YOUR EYES');
	UPDATE pro_ctc_questions SET question_file_name='question43'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Were there times when you had to URINATE FREQUENTLY');
	UPDATE pro_ctc_questions SET question_file_name='question44'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did FREQUENT URINATION INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question45'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any HAIR LOSS');
	UPDATE pro_ctc_questions SET question_file_name='question46'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your HAND-FOOT SYNDROME (A RASH OF THE HANDS OR FEET THAT CAN CAUSE CRACKING, PEELING, REDNESS, OR PAIN) at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question47'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have HEARTBURN');
	UPDATE pro_ctc_questions SET question_file_name='question48'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your HEARTBURN at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question49'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have HICCUPS');
	UPDATE pro_ctc_questions SET question_file_name='question50'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your HICCUPS at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question51'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any HIVES (ITCHY RED BUMPS ON THE SKIN)');
	UPDATE pro_ctc_questions SET question_file_name='question52'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your HOARSE VOICE at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question53'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have HOT FLASHES');
	UPDATE pro_ctc_questions SET question_file_name='question54'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your HOT FLASHES at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question55'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any INCREASED PASSING OF GAS (FLATULENCE)');
	UPDATE pro_ctc_questions SET question_file_name='question56'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any INCREASED SKIN SENSITIVITY TO SUNLIGHT');
	UPDATE pro_ctc_questions SET question_file_name='question57'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your INSOMNIA (INCLUDING DIFFICULTLY FALLING ASLEEP, STAYING ASLEEP, OR WAKING UP EARLY) at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question58'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did INSOMNIA (INCLUDING DIFFICULTY FALLING ASLEEP, STAYING ASLEEP, OR WAKING UP EARLY) INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question59'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any IRREGULAR MENSTRUAL PERIODS');
	UPDATE pro_ctc_questions SET question_file_name='question60'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your ITCHY SKIN at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question61'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have LOOSE OR WATERY STOOLS (DIARRHEA)');
	UPDATE pro_ctc_questions SET question_file_name='question62'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you LOSE ANY FINGERNAILS OR TOENAILS');
	UPDATE pro_ctc_questions SET question_file_name='question63'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you LOSE CONTROL OF BOWEL MOVEMENTS');
	UPDATE pro_ctc_questions SET question_file_name='question64'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did LOSS OF CONTROL OF BOWEL MOVEMENTS INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question65'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have LOSS OF CONTROL OF URINE (LEAKAGE)');
	UPDATE pro_ctc_questions SET question_file_name='question66'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did LOSS OF CONTROL OF URINE (LEAKAGE) INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question67'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you MISS AN EXPECTED MENSTRUAL PERIOD');
	UPDATE pro_ctc_questions SET question_file_name='question68'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your MOUTH OR THROAT SORES at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question69'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did MOUTH OR THROAT SORES INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question70'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have NAUSEA');
	UPDATE pro_ctc_questions SET question_file_name='question71'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your NAUSEA at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question72'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have NOSEBLEEDS');
	UPDATE pro_ctc_questions SET question_file_name='question73'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your NOSEBLEEDS at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question74'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your NUMBNESS OR TINGLING IN YOUR HANDS OR FEET at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question75'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did NUMBNESS OR TINGLING IN YOUR HANDS OR FEET INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question76'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Were you UNABLE TO HAVE AN ORGASM OR CLIMAX');
	UPDATE pro_ctc_questions SET question_file_name='question77'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have PAIN');
	UPDATE pro_ctc_questions SET question_file_name='question78'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your PAIN at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question79'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did PAIN INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question80'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your PAIN DURING VAGINAL SEX at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question81'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have PAIN IN THE ABDOMEN (BELLY AREA)');
	UPDATE pro_ctc_questions SET question_file_name='question82'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your PAIN IN THE ABDOMEN (BELLY AREA) at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question83'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did PAIN IN THE ABDOMEN (BELLY AREA) INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question84'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your PAIN OR BURNING WITH URINATION at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question85'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any PAIN, SWELLING, OR REDNESS AT A SITE OF DRUG INJECTION OR IV');
	UPDATE pro_ctc_questions SET question_file_name='question86'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you feel a POUNDING OR RACING HEARTBEAT (PALPITATIONS)');
	UPDATE pro_ctc_questions SET question_file_name='question87'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your POUNDING OR RACING HEARTBEAT (PALPITATIONS) at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question88'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your PROBLEMS WITH CONCENTRATION at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question89'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did PROBLEMS WITH CONCENTRATION INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question90'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your PROBLEMS WITH MEMORY at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question91'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did PROBLEMS WITH MEMORY INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question92'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your PROBLEMS WITH TASTING FOOD OR DRINK at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question93'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any RASH');
	UPDATE pro_ctc_questions SET question_file_name='question94'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any RIDGES OR BUMPS ON YOUR FINGERNAILS OR TOENAILS');
	UPDATE pro_ctc_questions SET question_file_name='question95'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of RINGING IN YOUR EARS at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question96'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have SAD OR UNHAPPY FEELINGS');
	UPDATE pro_ctc_questions SET question_file_name='question97'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your SAD OR UNHAPPY FEELINGS at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question98'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did SAD OR UNHAPPY FEELINGS INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question99'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have SHIVERING OR SHAKING CHILLS');
	UPDATE pro_ctc_questions SET question_file_name='question100'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your SHIVERING OR SHAKING CHILLS at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question101'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your SHORTNESS OF BREATH at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question102'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did SHORTNESS OF BREATH INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question103'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your SKIN BURNS FROM RADIATION at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question104'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of SKIN CRACKING AT THE CORNERS OF YOUR MOUTH at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question105'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any SPOTS OR LINES (FLOATERS) THAT DRIFT IN FRONT OF YOUR EYES');
	UPDATE pro_ctc_questions SET question_file_name='question106'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any STRETCH MARKS');
	UPDATE pro_ctc_questions SET question_file_name='question107'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you feel an URGE TO URINATE ALL OF A SUDDEN');
	UPDATE pro_ctc_questions SET question_file_name='question108'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did SUDDEN URGES TO URINATE INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question109'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you feel that it TOOK TOO LONG TO HAVE AN ORGASM OR CLIMAX');
	UPDATE pro_ctc_questions SET question_file_name='question110'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have TREMORS');
	UPDATE pro_ctc_questions SET question_file_name='question111'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your TREMORS at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question112'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have UNEXPECTED OR EXCESSIVE SWEATING DURING THE DAY OR NIGHTTIME (NOT RELATED TO HOT FLASHES)');
	UPDATE pro_ctc_questions SET question_file_name='question113'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your UNEXPECTED OR EXCESSIVE SWEATING DURING THE DAY OR NIGHTTIME (NOT RELATED TO HOT FLASHES) at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question114'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any UNUSUAL DARKENING OF THE SKIN');
	UPDATE pro_ctc_questions SET question_file_name='question115'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any UNUSUAL VAGINAL DISCHARGE');
	UPDATE pro_ctc_questions SET question_file_name='question116'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any URINE COLOR CHANGE');
	UPDATE pro_ctc_questions SET question_file_name='question117'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your VAGINAL DRYNESS at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question118'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='Did you have any VOICE CHANGES');
	UPDATE pro_ctc_questions SET question_file_name='question119'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have VOMITING');
	UPDATE pro_ctc_questions SET question_file_name='question120'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your VOMITING at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question121'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your WATERY EYES (TEARING) at their WORST');
	UPDATE pro_ctc_questions SET question_file_name='question122'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did WATERY EYES (TEARING) INTERFERE with your usual or daily activities');
	UPDATE pro_ctc_questions SET question_file_name='question123'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your WHEEZING (WHISTLING NOISE IN THE CHEST WITH BREATHING) at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question124'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How OFTEN did you have a HEADACHE');
	UPDATE pro_ctc_questions SET question_file_name='question125'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='What was the SEVERITY of your HEADACHE at its WORST');
	UPDATE pro_ctc_questions SET question_file_name='question126'
	WHERE id=(SELECT pro_ctc_questions_id FROM pro_ctc_questions_vocab WHERE
	question_text_english='How much did your HEADACHE INTERFERE with your usual or daily activities');
	return 0;
EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;

$x$
  LANGUAGE 'plpgsql' VOLATILE;




SELECT ivrs_updateques_filename();

-- Function: ivrs_updateSymp_filename()

-- DROP FUNCTION ivrs_updateSymp_filename();

CREATE OR REPLACE FUNCTION ivrs_updateSymp_filename()
  RETURNS integer AS
$BODY$
DECLARE
	v_count integer :=1;
	v_value integer :=0;
	v_term text :='';
BEGIN

	UPDATE pro_ctc_terms SET file_name='cs_anxiety'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Anxiety');
	UPDATE pro_ctc_terms SET file_name='cs_arm_leg_swelling'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Arm or leg swelling');
	UPDATE pro_ctc_terms SET file_name='cs_concentration_problem'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Problems with concentration');
	UPDATE pro_ctc_terms SET file_name='cs_constipation'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Constipation');
	UPDATE pro_ctc_terms SET file_name='cs_decreased_appetite'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Decreased appetite');
	UPDATE pro_ctc_terms SET file_name='cs_dry_mouth'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Dry mouth');
	UPDATE pro_ctc_terms SET file_name='cs_fatigue'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Fatigue, tiredness, or lack of energy');
	UPDATE pro_ctc_terms SET file_name='cs_headache'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Headache');
	UPDATE pro_ctc_terms SET file_name='cs_insomnia'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Insomnia (including difficulty falling asleep, staying asleep, or waking up early)');
	UPDATE pro_ctc_terms SET file_name='cs_loose_watery'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Loose or watery stools (diarrhea)');
	UPDATE pro_ctc_terms SET file_name='cs_mouth_throat_sores'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Mouth or throat sores');
	UPDATE pro_ctc_terms SET file_name='cs_nausea'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Nausea');
	UPDATE pro_ctc_terms SET file_name='cs_pain'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Pain');
	UPDATE pro_ctc_terms SET file_name='cs_tasting_problem'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Problems with tasting food or drink');
	UPDATE pro_ctc_terms SET file_name='cs_vomiting'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Vomiting');
	UPDATE pro_ctc_terms SET file_name='cs_numbness'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Numbness or tingling in your hands or feet');
    UPDATE pro_ctc_terms SET file_name='cs_shortness_breath'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Shortness of breath');
    UPDATE pro_ctc_terms SET file_name='cs_sad_feelings'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Sad or unhappy feelings');
    UPDATE pro_ctc_terms SET file_name='cs_rash'
	WHERE id=(SELECT pro_ctc_terms_id FROM pro_ctc_terms_vocab WHERE term_english='Rash');

	return 0;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;

$BODY$
  LANGUAGE 'plpgsql' VOLATILE;


SELECT ivrs_updateSymp_filename();