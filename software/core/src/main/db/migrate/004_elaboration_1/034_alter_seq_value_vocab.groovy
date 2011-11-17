class StudyParticipantReportingModeHistory extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
          execute('ALTER SEQUENCE pro_ctc_valid_values_vocab_id_seq RESTART WITH 1000')
  }

  void down() {
  }
}