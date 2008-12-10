class AddProCtcQuestionValidValues extends edu.northwestern.bioinformatics.bering.Migration {

	void up() {


		execute("delete from PRO_CTC_VALID_VALUES");
		addColumn('PRO_CTC_VALID_VALUES', 'display_name', 'string', nullable: false);
		dropColumn('PRO_CTC_VALID_VALUES', 'value');
		addColumn('PRO_CTC_VALID_VALUES', 'value', 'integer', nullable: false);

		insert('PRO_CTC_VALID_VALUES', [id: -1, display_name: "None", value: 0, pro_ctc_question_id: -1], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -2, display_name: "Mild", value: 1, pro_ctc_question_id: -1], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -3, display_name: "Moderate", value: 2, pro_ctc_question_id: -1], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -4, display_name: "Severe", value: 3, pro_ctc_question_id: -1], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -5, display_name: "Very Severe", value: 5, pro_ctc_question_id: -1], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -6, display_name: "None", value: 0, pro_ctc_question_id: -2], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -7, display_name: "Mild", value: 1, pro_ctc_question_id: -2], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -8, display_name: "Moderate", value: 2, pro_ctc_question_id: -2], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -9, display_name: "Severe", value: 3, pro_ctc_question_id: -2], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -10, display_name: "Very Severe", value: 5, pro_ctc_question_id: -2], primaryKey: false);


		insert('PRO_CTC_VALID_VALUES', [id: -11, display_name: "None", value: 0, pro_ctc_question_id: -3], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -12, display_name: "Mild", value: 1, pro_ctc_question_id: -3], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -13, display_name: "Moderate", value: 2, pro_ctc_question_id: -3], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -14, display_name: "Severe", value: 3, pro_ctc_question_id: -3], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -15, display_name: "Very Severe", value: 5, pro_ctc_question_id: -3], primaryKey: false);


		insert('PRO_CTC_VALID_VALUES', [id: -16, display_name: "Not at all", value: 0, pro_ctc_question_id: -4], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -17, display_name: "Mild", value: 1, pro_ctc_question_id: -4], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -18, display_name: "Moderate", value: 2, pro_ctc_question_id: -4], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -19, display_name: "Severe", value: 3, pro_ctc_question_id: -4], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -20, display_name: "Very Severe", value: 5, pro_ctc_question_id: -4], primaryKey: false)

		insert('PRO_CTC_VALID_VALUES', [id: -21, display_name: "None", value: 0, pro_ctc_question_id: -5], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -22, display_name: "A Little Bit", value: 1, pro_ctc_question_id: -5], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -23, display_name: "Somewhat", value: 2, pro_ctc_question_id: -5], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -24, display_name: "Quite a Bit", value: 3, pro_ctc_question_id: -5], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -25, display_name: "Very Much", value: 4, pro_ctc_question_id: -5], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -26, display_name: "None", value: 0, pro_ctc_question_id: -6], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -27, display_name: "A Little Bit", value: 1, pro_ctc_question_id: -6], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -28, display_name: "Somewhat", value: 2, pro_ctc_question_id: -6], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -29, display_name: "Quite a Bit", value: 3, pro_ctc_question_id: -6], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -30, display_name: "Very Much", value: 4, pro_ctc_question_id: -6], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -31, display_name: "None", value: 0, pro_ctc_question_id: -7], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -32, display_name: "A Little Bit", value: 1, pro_ctc_question_id: -7], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -33, display_name: "Somewhat", value: 2, pro_ctc_question_id: -7], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -34, display_name: "Quite a Bit", value: 3, pro_ctc_question_id: -7], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -35, display_name: "Very Much", value: 4, pro_ctc_question_id: -7], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -36, display_name: "None", value: 0, pro_ctc_question_id: -8], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -37, display_name: "A Little Bit", value: 1, pro_ctc_question_id: -8], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -38, display_name: "Somewhat", value: 2, pro_ctc_question_id: -8], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -39, display_name: "Quite a Bit", value: 3, pro_ctc_question_id: -8], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -40, display_name: "Very Much", value: 4, pro_ctc_question_id: -8], primaryKey: false);


		insert('PRO_CTC_VALID_VALUES', [id: -41, display_name: "None", value: 0, pro_ctc_question_id: -9], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -42, display_name: "Mild", value: 1, pro_ctc_question_id: -9], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -43, display_name: "Moderate", value: 2, pro_ctc_question_id: -9], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -44, display_name: "Severe", value: 3, pro_ctc_question_id: -9], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -45, display_name: "Very Severe", value: 5, pro_ctc_question_id: -9], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -46, display_name: "None", value: 0, pro_ctc_question_id: -10], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -47, display_name: "A Little Bit", value: 1, pro_ctc_question_id: -10], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -48, display_name: "Somewhat", value: 2, pro_ctc_question_id: -10], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -49, display_name: "Quite a Bit", value: 3, pro_ctc_question_id: -10], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -50, display_name: "Very Much", value: 4, pro_ctc_question_id: -10], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -51, display_name: "None", value: 0, pro_ctc_question_id: -11], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -52, display_name: "Mild", value: 1, pro_ctc_question_id: -11], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -53, display_name: "Moderate", value: 2, pro_ctc_question_id: -11], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -54, display_name: "Severe", value: 3, pro_ctc_question_id: -11], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -55, display_name: "Very Severe", value: 5, pro_ctc_question_id: -11], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -56, display_name: "None", value: 0, pro_ctc_question_id: -12], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -57, display_name: "A Little Bit", value: 1, pro_ctc_question_id: -12], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -58, display_name: "Somewhat", value: 2, pro_ctc_question_id: -12], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -59, display_name: "Quite a Bit", value: 3, pro_ctc_question_id: -12], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -60, display_name: "Very Much", value: 4, pro_ctc_question_id: -12], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -61, display_name: "None", value: 0, pro_ctc_question_id: -13], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -62, display_name: "Mild", value: 1, pro_ctc_question_id: -13], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -63, display_name: "Moderate", value: 2, pro_ctc_question_id: -13], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -64, display_name: "Sevre", value: 3, pro_ctc_question_id: -13], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -65, display_name: "Very Severe", value: 5, pro_ctc_question_id: -13], primaryKey: false);

		insert('PRO_CTC_VALID_VALUES', [id: -66, display_name: "None", value: 0, pro_ctc_question_id: -14], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -67, display_name: "A Little Bit", value: 1, pro_ctc_question_id: -14], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -68, display_name: "Somewhat", value: 2, pro_ctc_question_id: -14], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -69, display_name: "Quite a Bit", value: 3, pro_ctc_question_id: -14], primaryKey: false);
		insert('PRO_CTC_VALID_VALUES', [id: -70, display_name: "Very Much", value: 4, pro_ctc_question_id: -14], primaryKey: false);


	}


	void down() {
		execute("DELETE FROM PRO_CTC_TERMS")
	}
}
