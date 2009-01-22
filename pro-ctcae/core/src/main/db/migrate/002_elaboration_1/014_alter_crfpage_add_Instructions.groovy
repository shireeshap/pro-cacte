class AlterCrfPageAddInstructions extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    addColumn("crf_pages", "instructions", "string", nullable: true)


  }

  void down() {
    dropColumn("crf_pages", "instructions");
  }


}