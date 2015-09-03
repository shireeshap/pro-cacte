class IvrsScheduleCoreSymCount extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
      createTable("IVRS_SCH_CORE_SYM_COUNT") {t ->
      t.addVersionColumn()
      t.addColumn('spc_schedule_id ', 'integer', nullable: true)
      t.addColumn('core_sym_count', 'integer', nullable: true)
    }
    execute('ALTER TABLE IVRS_SCH_CORE_SYM_COUNT ADD CONSTRAINT fk_ivrs_core_spc_sch FOREIGN KEY (spc_schedule_id) REFERENCES SP_CRF_SCHEDULES')
  }

  void down() {
    dropTable("IVRS_SCH_CORE_SYM_COUNT")
  }
}
