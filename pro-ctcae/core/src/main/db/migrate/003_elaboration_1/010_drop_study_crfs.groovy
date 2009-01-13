class DropStudyCrfs extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute("delete from study_participant_crf_items");
    execute("delete from sp_crf_schedules");

    execute("delete from crf_page_item_display_rules");
    execute("delete from crf_page_items");
    execute("delete from crf_pages");
    execute("delete from study_participant_crfs");

    if (databaseMatches('oracle')) {
      execute('drop table STUDY_CRFS CASCADE CONSTRAINTS')

      execute('drop SEQUENCE seq_STUDY_CRFS_id')


    } else {
      execute('drop table STUDY_CRFS CASCADE')


    }


    execute("delete from crfs");

    addColumn("crfs", "study_id", 'integer', nullable: false)

    renameColumn("STUDY_PARTICIPANT_CRFS", "study_crf_id", "crf_id")

    execute('ALTER TABLE CRFS ADD CONSTRAINT fk_crf_study FOREIGN KEY (study_id) REFERENCES STUDIES')
    execute('ALTER TABLE STUDY_PARTICIPANT_CRFS ADD CONSTRAINT fk_spc_study_crf FOREIGN KEY (crf_id) REFERENCES CRFS')


  }

  void down() {

  }
}  