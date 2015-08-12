class AddOrganization extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
    insert('organizations', [id: 108032, nci_institute_code: "GA229", name: "Piedmont Fayette Hospital"], primaryKey: false)

  }

  void down() {
    execute("delete from organizations where id = 108032")

  }

}