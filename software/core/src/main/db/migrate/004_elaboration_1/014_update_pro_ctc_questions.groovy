class UpdateProCtcQuestions extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {

             addColumn("PRO_CTC_QUESTIONS", 'question_file_name', 'string', nullable: true);

              }

   void down() {
		     dropColumn("PRO_CTC_QUESTIONS", 'question_file_name')
		   }
}