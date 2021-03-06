--Login function returns user id on successful login
--Input parameters are user number and pin
--Function returns 0 on unsuccnextQuestionIdCategoryessful login , returns valid user id which is >0 on successful login and returns -1 for any other DB issues
CREATE OR REPLACE FUNCTION ivrs_login(userNumber text, pin integer) RETURNS integer AS $$
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
$$ LANGUAGE plpgsql;

--IVRS_GetFirstQuestion function returns the first unanswered question id along with the question category(PK of pro_ctc_question)
--for valid given scheduled form id(skipping rules applied)
--Input Parameters: userid , formid (scheduled form id)
--Return values: 'questionId_0' (added questions) 'questionId_1'(Mandatory questions) if it finds any valid question id, 0 if all the questions answered in the form and -1 for any other DB issues

CREATE OR REPLACE FUNCTION IVRS_GetFirstQuestion(userid integer,formid integer) RETURNS text AS $$
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
    FROM pro_ctc_questions pcq
    WHERE pcq.id = questionid;

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
$$ LANGUAGE plpgsql;


--Ivrs_GetQuestionFile function returns the question audio file name for the given scheduled form question id
--Input parameters: userid, formid (scheduled form id), questionid(study_participant_crf_items.id)
--Return values: Question audio file name for the given scheduled form question id

CREATE OR REPLACE FUNCTION Ivrs_GetQuestionFile(userid integer,formid integer,questionid Integer) RETURNS text AS $$
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
    return 'Data Not Found';
END;
$$ LANGUAGE plpgsql;



--IVRS_GetNextQuestion function returns the next question id for the given form
--Input values: userid, formid (scheduled form id), questionid(pro_ctc_question.id)
--return values: 0 if all the questions answered in the form, >0 is the valid question id, -1 for any other DB issues
--Here next Question id is the PK id of pro_ctc_question
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
$$ LANGUAGE plpgsql;

--IVRS_GetNextAddedQuestion function returns the next question id for the given form
--Input values: userid, formid (scheduled form id), questionid(pro_ctc_question.id)
--return values: 0 if all the questions answered in the form, >0 is the valid question id, -1 for any other DB issues
--Here next Question id is the PK id of pro_ctc_question
--function considering questions from the current page (page of input question id)
CREATE OR REPLACE FUNCTION IVRS_GetNextAddedQuestion(userid integer, formid integer, questionid integer) RETURNS integer AS $$
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
$$ LANGUAGE plpgsql;

-- store the answer for the given question and returns next question id if exists

--store the answer for the given question and returns next valid un answered question id if exists
--input: userid , formid which is PK id of scheduled form, questionid, answerNum and questionCategory which specifies
--whether a questions in in mandatory or added core symptom page
--returns next valid un answered question id for the given form
CREATE OR REPLACE FUNCTION IVRS_AnswerQuestion(userid integer, formid integer, questionid integer, answernum integer, questioncategory integer)
 RETURNS text AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;


-- Inserts the symptom's questions to added questions based on answer and returns next core symptom id if exists

-- Inserts the symptom's questions to added questions based on answer (1= insert 2= will not insert questions) and returns next core symptom id if exists
--input: userid , formid which is PK id of scheduled form, symptomid and answer and corecategory
--returns next valid un asked core symptom id for the given form
CREATE OR REPLACE FUNCTION ivrs_AnswerCoreSymptom(userid integer, formid integer, symptomid integer, answer integer, corecategory integer)
  RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql';


-- ivrs_commitsession function complete the sceduled form upon completion of all questions and change the status to completed
--Input parameters:userid,pin and formid (scheduled form id,PK id of sp_crf_schedules)
--return: -2 for form not completed properly and still some questions needs to be answered, 1 for form completed, 0 for form not submitted properly, -1 for any DB issues
CREATE OR REPLACE FUNCTION IVRS_CommitSession(userid integer, formid integer, pin integer) RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql';

-- Function: ivrs_getQuestionAnswer(userid integer, formid integer, questionid integer)
-- DROP FUNCTION ivrs_getQuestionAnswer(userid integer, formid integer, questionid integer);
--return:  return ivrs answer (1--None,2--Mild,3--Moderate,4--Severe,5--Very Severe etc) for given question, for 0 for there is no answer for given question, -1 for any DB issues
-- Function: ivrs_getquestionanswer(integer, integer, integer, integer)

-- DROP FUNCTION ivrs_getquestionanswer(integer, integer, integer, integer);

CREATE OR REPLACE FUNCTION IVRS_GetQuestionAnswer(userid integer, formid integer, questionid integer, questioncategory integer)
  RETURNS integer AS $$
DECLARE
    v_return_answer_id integer :=0;
    v_proctc_crf_item_id integer :=0;
    v_proctc_value_id integer :=0;
BEGIN
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
			return 0;
		END IF;
	--get the IVRS answer type from valid values like 1,2 etc
		select count(*) INTO  v_return_answer_id from pro_ctc_valid_values where pro_ctc_question_id = questionid and id<v_proctc_value_id;

	return v_return_answer_id;

EXCEPTION
    WHEN OTHERS THEN
    return -1;
END;
$$ LANGUAGE 'plpgsql';

-- Function: ivrs_getpreviousquestion(userid integer, formid integer, questionid integer,questioncategory integer)
-- DROP FUNCTION ivrs_getpreviousquestion(userid integer, formid integer, questionid integer,questioncategory integer);
--return:  return previous question id and question category for given question,
--for 0 if there is no previous question, -1 for any DB issues,-2 for question is the first question id in the form
CREATE OR REPLACE FUNCTION ivrs_getpreviousquestion(userid integer, formid integer, questionid integer, questioncategory integer)
  RETURNS text AS $$
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
			OPEN curs_questions(formid,v_gender,v_page_present_num-1);
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
$$ LANGUAGE 'plpgsql';

-- -- Function: ivrs_getprevious_coresymid(userid integer, formid integer, symptomid integer)
-- DROP FUNCTION ivrs_getprevious_coresymid(userid integer, formid integer, symptomid integer);
--return:  return previous symptom id and question category for given symptomid,
--for questionsID_questionsCategory if there is no previous symptoms(0=additional questions and 1=regular questions),
--'-1' for any DB issues,-2 for question is the first question id in the form

CREATE OR REPLACE FUNCTION ivrs_getprevious_coresymid(userid integer, formid integer, symptomid integer)
  RETURNS text AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_isUserNew(userid integer)
-- DROP FUNCTION ivrs_isUserNew(userid integer);
--return:  return 1 for new users to IVRS ,0 for existing users to IVRS
--return : -1 for any DB issues,-2 for question is the first question id in the form
CREATE OR REPLACE FUNCTION IVRS_isUserNew(userid integer) RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;


-- Function: ivrs_getFormRecallPeriod(userid integer,formid integer)
-- DROP FUNCTION ivrs_getFormRecallPeriod(userid integer,formid integer);
--return:  return 1 for weekly period,return 2 for monthly, return 3 for last treatment,
-->return >3 for custom recall periods, -1 for any DB issues
CREATE OR REPLACE FUNCTION ivrs_getFormRecallPeriod(userid integer, formid integer) RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql';

-- Function: ivrs_deleteaddedquestions(userid integer, formid integer)
-- DROP FUNCTION ivrs_deleteaddedquestions(userid integer, formid integer);
-- return: 0 if deletion of questions which are answered yes  is done
-- return :1 if there are any exceptions.
CREATE OR REPLACE FUNCTION ivrs_DeleteAddedQuestions(userid integer, formid integer)
  RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_getcoresymptomid(userid integer, formid integer)
-- DROP FUNCTION ivrs_getcoresymptomid(userid integer, formid integer);
CREATE OR REPLACE FUNCTION ivrs_GetCoreSymptomID(userid integer, formid integer)
  RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_getcoresymptomfilename(serid integer, formid integer, symptomid integer)
-- DROP FUNCTION ivrs_getcoresymptomfilename(serid integer, formid integer, symptomid integer);  \
-- return : file name for given symptom id.
CREATE OR REPLACE FUNCTION ivrs_GetCoreSymptomFileName(userid integer, formid integer, symptomid integer)
  RETURNS text AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_get_sym_ques_ans(userid integer, formid integer, symptomid integer, corecategory integer)
-- DROP FUNCTION ivrs_get_sym_ques_ans(userid integer, formid integer, symptomid integer, corecategory integer);
-- return: 2(NO) or 1(YES) based on core category which determines if 7 (previous quesitons is asked or not)
-- return: 0 if previous question is not asked.
CREATE OR REPLACE FUNCTION ivrs_Get_Sym_Ques_Ans(userid integer, formid integer, symptomid integer, corecategory integer)
  RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_save_filepath(userid integer, formid integer, filepath text)
-- DROP FUNCTION ivrs_save_filepath(userid integer, formid integer, filepath text);
-- return: 1(successfully updated) or 0(not successfully updated)
-- return: -1 if there are any exceptions

CREATE OR REPLACE FUNCTION IVRS_Save_FilePath(userid integer, formid integer,filepath text)
  RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_get_no_of_unans_symps(userid integer, formid integer)
-- DROP FUNCTION ivrs_get_no_of_unans_symps(userid integer, formid integer);
-- return: number of unanswered symptoms in the given form.
-- return: -1 if there are any exceptions

CREATE OR REPLACE FUNCTION ivrs_get_no_of_unans_symps(userid integer, formid integer) RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;

-- Function: ivrs_getform_status(userid integer, formid integer)
-- DROP FUNCTION ivrs_getform_status(userid integer, formid integer);
-- return 0 if the form is schedueled and 1 if the form is in INPROGRESS
-- return -2 if there is no record found for the given form
-- return -1 for exception

CREATE OR REPLACE FUNCTION ivrs_getform_status(userid integer, formid integer) RETURNS integer AS $$
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
$$ LANGUAGE 'plpgsql' VOLATILE;


