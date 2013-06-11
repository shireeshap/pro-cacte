class AddingOrganizations extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
    insert('organizations', [id: 108035, nci_institute_code: "SC084", name: "Cancer Centers of the Carolinas-Greer Medical Oncology"], primaryKey: false)
    insert('organizations', [id: 108036, nci_institute_code: "SC085", name: "Cancer Centers of the Carolinas-Greer Radiation Oncology"], primaryKey: false)
  }

  void down() {
    execute("delete from organizations where id = 108035")
    execute("delete from organizations where id = 108036")
  }

}