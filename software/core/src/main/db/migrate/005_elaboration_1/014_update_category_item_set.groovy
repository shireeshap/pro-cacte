import edu.northwestern.bioinformatics.bering.Migration

class UpdateCategoryItemSet extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("DELETE FROM category_term_set WHERE ctc_term_id=5044")
    execute("DELETE FROM category_term_set WHERE ctc_term_id=5058")
  }

  void down() {
  }
}
