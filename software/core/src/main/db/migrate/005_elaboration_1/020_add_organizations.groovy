class AddOrganizations extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
    insert('organizations', [id: 108033, nci_institute_code: "KS094", name: "Cancer Center of Kansas - Fort Scott"], primaryKey: false)
    insert('organizations', [id: 108034, nci_institute_code: "KS097", name: "Cancer Center of Kansas-Liberal"], primaryKey: false)
  }

  void down() {
    execute("delete from organizations where id = 108033")
    execute("delete from organizations where id = 108034")
  }

}