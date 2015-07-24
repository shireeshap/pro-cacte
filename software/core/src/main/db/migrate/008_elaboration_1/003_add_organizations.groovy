class AddOrganizationSMOC extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
    insert('organizations', [id: 109248, nci_institute_code: "NC254", name: "Southeastern Medical Oncology Center - Clinton Site"], primaryKey: false)
  }

  void down() {
    execute("delete from organizations where id = 109248")
  }
}