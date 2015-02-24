class UpdateSpc extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
    addColumn("STUDY_PARTICIPANT_CRFS", 'arm_id', 'integer');

  }

  void down() {
    dropColumn("STUDY_PARTICIPANT_CRFS", 'arm_id')
  }

}