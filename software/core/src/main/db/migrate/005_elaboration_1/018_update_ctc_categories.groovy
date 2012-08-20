class UpdateCtcCategories extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("update ctc_categories set name = 'Core symptoms' where name = 'Core terms'");
  }

  void down() {
  }
}