class AlterStudyParticipantCrfItems extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute('ALTER TABLE study_participant_crf_items ALTER COLUMN selected_response DROP NOT NULL')

  }



  void down() {
    execute("DELETE FROM PRO_CTC_TERMS")
  }
}
