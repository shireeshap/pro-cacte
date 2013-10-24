class AlterClinicalStaff extends edu.northwestern.bioinformatics.bering.Migration {

     void up() {
           addColumn("CLINICAL_STAFFS", 'cs_status', 'string', nullable: true);
           addColumn("CLINICAL_STAFFS", 'effective_date', 'date', nullable: true);
           }

     void down() {
           dropColumn("CLINICAL_STAFFS", 'effective_date', 'date', nullable:true)
           dropColumn("CLINICAL_STAFFS", 'cs_status')
              }

          }