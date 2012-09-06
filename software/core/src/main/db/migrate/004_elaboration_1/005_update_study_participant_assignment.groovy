class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'on_hold_treatment_date', 'date', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'off_hold_treatment_date', 'date', nullable: true);
              }

   void down() {
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'off_hold_treatment_date')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'on_hold_treatment_date')

    }

}