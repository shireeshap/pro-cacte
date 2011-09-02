class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_time_zone')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_minute')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_hour')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_am_pm')
           }

   void down() {

		   }

}