class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'time')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'hour')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'time_zone')

             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_am_pm', 'string', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_hour', 'integer', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_minute', 'integer', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_time_zone', 'string', nullable: true);

             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_am_pm', 'string', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_hour', 'integer', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_minute', 'integer', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_time_zone', 'string', nullable: true);
              }

   void down() {
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_time_zone')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_minute')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_hour')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'reminder_am_pm')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_time_zone')
		     dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_minute')
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_hour')
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'call_am_pm')


    }

}