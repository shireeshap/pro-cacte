class UpdateCrfTable extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {

        addColumn('CRFS', 'parent_version_id', 'integer', nullable: true);
        execute('ALTER TABLE CRFS ADD CONSTRAINT fk_prev_crfs_crf_id FOREIGN KEY (parent_version_id) REFERENCES CRFS')
  }


  void down() {

  }


}
