import java.lang.Exception;

class InsertEq5d extends edu.northwestern.bioinformatics.bering.Migration {
	void up(){
        execute("insert into ctc_categories (id, version_id, name, user_defined) values (416, 4, 'EQ5D-3L', FALSE)");
        execute("insert into ctc_categories (id, version_id, name, user_defined) values (417, 4, 'EQ5D-5L', FALSE)");
        

        execute("insert into ctc_terms (id, category_id, other_required) values (5080, 416, TRUE)");
        execute("insert into ctc_terms (id, category_id, other_required) values (5081, 416, TRUE)");        
        execute("insert into ctc_terms (id, category_id, other_required) values (5082, 416, TRUE)");
        execute("insert into ctc_terms (id, category_id, other_required) values (5083, 416, TRUE)");
        execute("insert into ctc_terms (id, category_id, other_required) values (5084, 416, TRUE)");
        execute("insert into ctc_terms (id, category_id, other_required) values (5085, 417, TRUE)");
        execute("insert into ctc_terms (id, category_id, other_required) values (5086, 417, TRUE)");        
        execute("insert into ctc_terms (id, category_id, other_required) values (5087, 417, TRUE)");
        execute("insert into ctc_terms (id, category_id, other_required) values (5088, 417, TRUE)");
        execute("insert into ctc_terms (id, category_id, other_required) values (5089, 417, TRUE)");
    
        execute("insert into category_term_set (ctc_term_id, category_id) values (5080, 416)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5081, 416)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5082, 416)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5083, 416)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5084, 416)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5085, 417)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5086, 417)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5087, 417)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5088, 417)");
        execute("insert into category_term_set (ctc_term_id, category_id) values (5089, 417)");
        
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5080, 5080, 'Mobility', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5081, 5081, 'Self-Care', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5082, 5082, 'Usual Activities', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5083, 5083, 'Pain/Discomfort', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5084, 5084, 'Anxiety/Depression', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5085, 5085, 'Mobility', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5086, 5086, 'Self-Care', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5087, 5087, 'Usual Activities', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5088, 5088, 'Pain/Discomfort', '')");
        execute("insert into ctc_terms_vocab (id, ctc_terms_id, term_english, term_spanish) values (5089, 5089, 'Anxiety/Depression', '')");
   
        execute("INSERT INTO pro_ctc (pro_ctc_version, release_date) SELECT pro_ctc_version, DATE '2010-07-07' FROM pro_ctc UNION VALUES ('4.0', DATE '2010-07-07') EXCEPT SELECT pro_ctc_version, DATE '2010-07-07' FROM pro_ctc;");
        
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  82, 5080, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  83, 5081, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  84, 5082, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  85, 5083, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  86, 5084, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  87, 5085, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  88, 5086, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  89, 5087, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  90, 5088, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
        execute("INSERT INTO pro_ctc_terms (id, ctc_term_id, pro_ctc_id, core, gender, currency) SELECT  91, 5089, id, FALSE, 'both', 'Y' FROM pro_ctc WHERE pro_ctc.pro_ctc_version='4.0'");
             
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (82, 82, 'Mobility', 'Movilidad')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (83, 83, 'Self-Care', 'Autocuidado')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (84, 84, 'Usual Activities', 'Actividades habituales')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (85, 85, 'Pain/Discomfort', 'Dolor/Malestar')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (86, 86, 'Anxiety/Depression', 'Ansiedad/Depresión')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (87, 87, 'Mobility', 'Movilidad')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (88, 88, 'Self-Care', 'Autocuidado')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (89, 89, 'Usual Activities', 'Actividades habituales')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (90, 90, 'Pain/Discomfort', 'Dolor/Malestar')");
        execute("insert into pro_ctc_terms_vocab (id, pro_ctc_terms_id, term_english, term_spanish) values (91, 91, 'Anxiety/Depression', 'Ansiedad/Depresión')");
             
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (127, 82, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (128, 83, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (129, 84, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (130, 85, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (131, 86, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (132, 87, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (133, 88, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (134, 89, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (135, 90, 'SEVERITY', 1)");
        execute("insert into pro_ctc_questions (id, pro_ctc_term_id, question_type, display_order) values (136, 91, 'SEVERITY', 1)");
        
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (127, 127, 'Mobility', 'Movilidad')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (128, 128, 'Self-Care', 'Autocuidado')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (129, 129, 'Usual Activities', 'Actividades habituales')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (130, 130, 'Pain/Discomfort', 'Dolor/Malestar')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (131, 131, 'Anxiety/Depression', 'Ansiedad/Depresión')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (132, 132, 'Mobility', 'Movilidad')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (133, 133, 'Self-Care', 'Autocuidado')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (134, 134, 'Usual Activities', 'Actividades habituales')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (135, 135, 'Pain/Discomfort', 'Dolor/Malestar')");
        execute("insert into pro_ctc_questions_vocab (id, pro_ctc_questions_id, question_text_english, question_text_spanish) values (136, 136, 'Anxiety/Depression', 'Ansiedad/Depresión')");
                     
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (591, 127, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (592, 127, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (593, 127, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (594, 128, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (595, 128, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (596, 128, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (597, 129, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (598, 129, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (599, 129, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (600, 130, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (601, 130, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (602, 130, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (603, 131, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (604, 131, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (605, 131, 3, 3)");
        
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (606, 132, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (607, 132, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (608, 132, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (609, 132, 4, 4)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (610, 132, 5, 5)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (611, 133, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (612, 133, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (613, 133, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (614, 133, 4, 4)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (615, 133, 5, 5)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (616, 134, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (617, 134, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (618, 134, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (619, 134, 4, 4)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (620, 134, 5, 5)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (621, 135, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (622, 135, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (623, 135, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (624, 135, 4, 4)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (625, 135, 5, 5)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (626, 136, 1, 1)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (627, 136, 2, 2)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (628, 136, 3, 3)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (629, 136, 4, 4)");
        execute("insert into pro_ctc_valid_values (id, pro_ctc_question_id, display_order, response_code) values (630, 136, 5, 5)");     
        
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (591, 591, 'I have no problems in walking about', 'No tengo problemas para caminar sobre')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (592, 592, 'I have some problems in walking about', 'Tengo algunos problemas para caminar sobre')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (593, 593, 'I am confined to bed', 'Estoy confinado a la cama')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (594, 594, 'I have no problems with self-care', 'No tengo problemas con el cuidado personal')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (595, 595, 'I have some problems washing or dressing myself', 'He lavado de algunos problemas o vestirme solo')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (596, 596, 'I am unable to wash or dress myself', 'Soy incapaz de lavar o vestirme')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (597, 597, 'I have no problems with performing my usual activities', 'No tengo problemas para realizar mis actividades habituales')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (598, 598, 'I have some problems with performing my usual activities', 'Tengo algunos problemas para realizar mis actividades habituales')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (599, 599, 'I am unable to perform my usual activities', 'No puedo realizar mis actividades habituales')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (600, 600, 'I have no pain or discomfort', 'No tengo ningún dolor o malestar')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (601, 601, 'I have moderate pain or discomfort', 'Tengo un dolor moderado o malestar')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (602, 602, 'I have extreme pain or discomfort', 'Tengo dolor o incomodidad extrema')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (603, 603, 'I am not anxious or depressed', 'No estoy ansioso o deprimido')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (604, 604, 'I am moderately anxious or depressed', 'Estoy moderadamente ansioso o deprimido')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (605, 605, 'I am extremely anxious or depressed', 'Estoy extremadamente ansioso o deprimido')");     
        
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (606, 606, 'I have no problems in walking about', 'No tengo problemas para caminar sobre')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (607, 607, 'I have slight problems in walking about', 'Tengo problemas leves para caminar sobre')");  
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (608, 608, 'I have some problems in walking about', 'Tengo algunos problemas para caminar sobre')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (609, 609, 'I have severe problems in walking about', 'Tengo serios problemas para caminar sobre')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (610, 610, 'I am confined to bed', 'Estoy confinado a la cama')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (611, 611, 'I have no problems with self-care', 'No tengo problemas con el cuidado personal')");  
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (612, 612, 'I have slight problems washing or dressing myself', 'He lavado de problemas leves o vestirme solo')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (613, 613, 'I have some problems washing or dressing myself', 'He lavado de algunos problemas o vestirme solo')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (614, 614, 'I have severe problems washing or dressing myself', 'He lavado de problemas graves o vestirme solo')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (615, 615, 'I am unable to wash or dress myself', 'Soy incapaz de lavar o vestirme')"); 
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (616, 616, 'I have no problems with performing my usual activities', 'No tengo problemas para realizar mis actividades habituales')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (617, 617, 'I have slight problems doing my usual activities', 'Tengo problemas leves haciendo mis actividades habituales')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (618, 618, 'I have some problems with performing my usual activities', 'Tengo algunos problemas para realizar mis actividades habituales')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (619, 619, 'I have severe problems doing my usual activities', 'Tengo problemas graves haciendo mis actividades habituales')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (620, 620, 'I am unable to perform my usual activities', 'No puedo realizar mis actividades habituales')");   
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (621, 621, 'I have no pain or discomfort', 'No tengo ningún dolor o malestar')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (622, 622, 'I have slight pain or discomfort', 'Tengo un poco de dolor o malestar')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (623, 623, 'I have moderate pain or discomfort', 'Tengo un dolor moderado o malestar')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (624, 624, 'I have severe pain or discomfort', 'Tengo dolor o molestia severa')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (625, 625, 'I have extreme pain or discomfort', 'Tengo dolor o incomodidad extrema')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (626, 626, 'I am not anxious or depressed', 'No estoy ansioso o deprimido')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (627, 627, 'I am slightly anxious or depressed', 'Estoy un poco ansioso o deprimido')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (628, 628, 'I am moderately anxious or depressed', 'Estoy moderadamente ansioso o deprimido')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (629, 629, 'I am severely anxious or depressed', 'Estoy gravemente ansioso o deprimido')");     
        execute("insert into pro_ctc_valid_values_vocab (id, pro_ctc_valid_values_id, value_english, value_spanish) values (630, 630, 'I am extremely anxious or depressed', 'Estoy extremadamente ansioso o deprimido')");     
        
        
	}
	
	void down(){
	}
}