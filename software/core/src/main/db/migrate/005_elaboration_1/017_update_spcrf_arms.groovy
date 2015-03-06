class UpdateSpcrfArms extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("update study_participant_crfs spc set arm_id = spa.arm_id from study_participant_assignments spa where spa.id = spc.study_participant_id");
  }

  void down() {
  }
}