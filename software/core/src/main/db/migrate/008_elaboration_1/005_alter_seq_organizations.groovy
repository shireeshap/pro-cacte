class AlterSequenceOrganization extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
          execute('ALTER SEQUENCE organizations_id_seq RESTART WITH 109251')
  }

  void down() {
  }
}