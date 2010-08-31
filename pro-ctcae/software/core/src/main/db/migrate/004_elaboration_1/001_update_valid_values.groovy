class UpdateValidValues extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

        execute("update pro_ctc_valid_values set value = 'Not sexually active' where value = ' Not applicable' and pro_ctc_question_id in (select id from pro_ctc_questions where pro_ctc_term_id in ( select id from pro_ctc_terms where term in ( 'Orgasm or climax','Pain during vaginal sex' ,'Took too long to have an orgasm or climax','Difficulty getting or keeping an erection','Ejaculation problems')))")
		execute("insert into pro_ctc_valid_values (value, pro_ctc_question_id, display_order) values ('Prefer not to answer' , (select p1.id from pro_ctc_questions p1, pro_ctc_terms p2 where p1. pro_ctc_term_id = p2.id and p2.term= 'Orgasm or climax' order by p1.display_order limit 1), 1 )")
        execute("insert into pro_ctc_valid_values (value, pro_ctc_question_id, display_order) values ('Prefer not to answer' , (select p1.id from pro_ctc_questions p1, pro_ctc_terms p2 where p1. pro_ctc_term_id = p2.id and p2.term= 'Pain during vaginal sex' order by p1.display_order limit 1), 6)")
        execute("insert into pro_ctc_valid_values (value, pro_ctc_question_id, display_order) values ('Prefer not to answer' , (select p1.id from pro_ctc_questions p1, pro_ctc_terms p2 where p1. pro_ctc_term_id = p2.id and p2.term= 'Took too long to have an orgasm or climax' order by p1.display_order limit 1), 1 )")
        execute("insert into pro_ctc_valid_values (value, pro_ctc_question_id, display_order) values ('Prefer not to answer' , (select p1.id from pro_ctc_questions p1, pro_ctc_terms p2 where p1. pro_ctc_term_id = p2.id and p2.term= 'Difficulty getting or keeping an erection' order by p1.display_order limit 1), 6 )")
        execute("insert into pro_ctc_valid_values (value, pro_ctc_question_id, display_order) values ('Prefer not to answer' , (select p1.id from pro_ctc_questions p1, pro_ctc_terms p2 where p1. pro_ctc_term_id = p2.id and p2.term= 'Ejaculation problems' order by p1.display_order limit 1), 6 )")
        }

        void down() {

	}
}