class AlterStudyParticipantCrfs extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    addColumn('study_participant_crfs', 'start_date', 'timestamp', nullable: true);
    addColumn('study_participant_crfs', 'due_date', 'timestamp', nullable: true);

  }

  void down() {
    dropColumn('study_participant_crfs', 'start_date');
    dropColumn('study_participant_crfs', 'due_date');

  }
}