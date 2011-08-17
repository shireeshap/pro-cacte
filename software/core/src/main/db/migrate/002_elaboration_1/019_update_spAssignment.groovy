class UpdateSpAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
            addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'off_treatment_date', 'date', nullable: true);


    }

    void down() {

    		dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'off_treatment_date')
    }

}