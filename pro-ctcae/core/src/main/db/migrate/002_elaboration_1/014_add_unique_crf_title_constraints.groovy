class AddUniqueCrfTitleConstraints extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		execute("ALTER TABLE CRFS ADD CONSTRAINT un_crfs_title UNIQUE (title)")
	}

	void down() {
	}
}

