class AlterStudyParticipantAssignment extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
           addColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'sp_status', 'string', nullable: true);
           }

     void down() {
           dropColumn("STUDY_PARTICIPANT_ASSIGNMENTS", 'sp_status')
              }

          }