class AddUniqueCrfTitleConstraints extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("update CRFS set title = cast(id as varchar)")
    execute("ALTER TABLE CRFS ADD CONSTRAINT un_crfs_title UNIQUE (title)")
  }

  void down() {
  }
}

