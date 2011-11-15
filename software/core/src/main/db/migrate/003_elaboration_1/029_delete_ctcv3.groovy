class UpdateCtcAEv4Terms extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {

          execute("delete from ctc_terms where id < 5000")
          execute("delete from ctc_categories where id < 400")
          execute("delete from ctc where id < 4")
    }

	void down() {
		execute("DELETE FROM ctc_terms WHERE category_id > 300 AND category_id < 5000")
		execute("DELETE FROM ctc_categories WHERE version_id=3")
	}
}
