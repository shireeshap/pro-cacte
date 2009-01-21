class AlterCrfAddCreationMode extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    addColumn("CRFS", "crf_creation_mode", "string", nullable: true)
    execute("update CRFS set crf_creation_mode='BASIC'");

    if (databaseMatches("oracle")) {
      execute('ALTER TABLE CRFS modify crf_creation_mode char not NULL')
    } else {
      execute('ALTER TABLE CRFS ALTER COLUMN crf_creation_mode set NOT NULL')
    }

  }

  void down() {
    dropColumn("crfs", "crf_creation_mode");
  }
}