class AddColumnIsScheduleInitialized extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
    	addColumn("study_participant_crfs", 'is_schedule_initialized', 'boolean', nullable: true);
    	execute("update study_participant_crfs set is_schedule_initialized=false");
    }

    void down() {
    	dropColumn("study_participant_crfs", 'is_schedule_initialized');
    }

}