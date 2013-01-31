class UpdateStudyOrgClinicalStaffs extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
                 addColumn("study_org_clinical_staffs", 'notify', 'boolean', nullable: true);
                 execute("update study_org_clinical_staffs set notify = 'true'")
              }

   void down() {
		     dropColumn("study_org_clinical_staffs", 'notify')
    }

}