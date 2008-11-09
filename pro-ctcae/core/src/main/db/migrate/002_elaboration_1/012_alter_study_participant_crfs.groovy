class DropDueDateFromStudyParticipantCrf extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute('ALTER TABLE study_participant_crfs DROP COLUMN start_date')
    execute('ALTER TABLE study_participant_crfs DROP COLUMN due_date')
  }


  void down() {
  }
}
