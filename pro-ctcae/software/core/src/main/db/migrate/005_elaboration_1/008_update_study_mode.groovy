class UpdateStudyMode extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("DELETE FROM study_modes WHERE mode = 'CLINICWEB'")
  }

  void down() {

  }
}