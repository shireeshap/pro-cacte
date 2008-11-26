class AlterProCtcQuestion extends edu.northwestern.bioinformatics.bering.Migration {

	void up() {


		addColumn('PRO_CTC_QUESTIONS', 'question_type', 'string', nullable: true);


		execute("update PRO_CTC_QUESTIONS set question_type='SEVERITY' where  question_text='Severity'");

		execute("update PRO_CTC_QUESTIONS set question_type='INTERFERENCE' where  question_text='Interference'");


		execute("update PRO_CTC_QUESTIONS set question_text='Over the past week, what was the WORST SEVERITY of your' where  question_type='Severity'");
		execute("update PRO_CTC_QUESTIONS set question_text='Over the past week, how much did the below symptoms INTERFERE with your daily activities' where  question_type='Interference'");

	}


	void down() {
		execute("DELETE FROM PRO_CTC_TERMS")
	}
}
