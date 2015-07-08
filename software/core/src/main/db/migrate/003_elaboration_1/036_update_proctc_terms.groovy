class UpdateProCtcTerms extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
     addColumn("PRO_CTC_TERMS", 'gender', 'string', nullable: true);
  }

  void down() {
    dropColumn("PRO_CTC_TERMS", 'gender')
  }
}