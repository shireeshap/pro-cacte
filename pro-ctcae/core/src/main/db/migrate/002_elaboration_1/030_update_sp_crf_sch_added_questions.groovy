class UpdateSpCrfSchAddedQuestions extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
                  execute('ALTER TABLE sp_crf_sch_added_questions ALTER COLUMN question_id DROP NOT NULL')
                  
    }

    void down() {

    }

}