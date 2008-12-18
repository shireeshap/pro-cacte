class AlterInvestigatorClinical extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {

        addColumn('CRFS', 'effective_start_date', 'date', nullable: true);
        addColumn('CRFS', 'effective_end_date', 'date', nullable: true);
        addColumn('CRFS', 'next_version_id', 'integer', nullable: true);
        execute('ALTER TABLE CRFS ADD CONSTRAINT fk_crfs_crf_id FOREIGN KEY (next_version_id) REFERENCES CRFS')
  }


  void down() {

  }


}
