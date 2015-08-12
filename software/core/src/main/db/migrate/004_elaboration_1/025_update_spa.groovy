class UpdateStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'web_language')
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'home_web_language', 'string', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'home_paper_language', 'string', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'clinic_web_language', 'string', nullable: true);
             addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'clinic_paper_language', 'string', nullable: true);
              }

   void down() {
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'clinic_paper_language')
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'clinic_web_language')
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'home_paper_language')
             dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'home_web_language')           
      }

}