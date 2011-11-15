class AlterMeddraValidValueSequence extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
          execute('ALTER SEQUENCE meddra_valid_values_vocab_id_seq RESTART WITH 1000')
          execute('ALTER SEQUENCE meddra_questions_vocab_id_seq RESTART WITH 1000')
  }

  void down() {
  }
}