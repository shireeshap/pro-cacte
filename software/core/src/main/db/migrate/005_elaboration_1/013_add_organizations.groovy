class AddOrganizations extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
    insert('organizations', [id: 108018, nci_institute_code: "IA090", name: "Mercy Medical Center-West Lakes"], primaryKey: false)
    insert('organizations', [id: 108019, nci_institute_code: "IA091", name: "Mercy Cancer Center-West Lakes"], primaryKey: false)
    insert('organizations', [id: 108020, nci_institute_code: "IA092", name: "Methodist West Hospital"], primaryKey: false)
    insert('organizations', [id: 108021, nci_institute_code: "IL378", name: "Spector, David MD (UIA Investigator)"], primaryKey: false)
    insert('organizations', [id: 108022, nci_institute_code: "IN162", name: "Kendrick Regional Center-Mooresville"], primaryKey: false)
    insert('organizations', [id: 108023, nci_institute_code: "MA185", name: "Dana-Farber/Brigham and Women's Cancer Center at South Shore"], primaryKey: false)
    insert('organizations', [id: 108024, nci_institute_code: "MA188", name: "Dana-Farber/Brigham and Women's Cancer Center at Milford Regional"], primaryKey: false)
    insert('organizations', [id: 108025, nci_institute_code: "MO185", name: "Mercy Clinic-Rolla-Cancer and Hematology"], primaryKey: false)
    insert('organizations', [id: 108026, nci_institute_code: "OH415", name: "Wayne Hospital"], primaryKey: false)
    insert('organizations', [id: 108027, nci_institute_code: "OH428", name: "Knox Community Hospital"], primaryKey: false)
    insert('organizations', [id: 108028, nci_institute_code: "OK087", name: "Integris Cancer Institute of Oklahoma"], primaryKey: false)
    insert('organizations', [id: 108029, nci_institute_code: "PA441", name: "Academic Urology Prostate Center"], primaryKey: false)
    insert('organizations', [id: 108030, nci_institute_code: "PA446", name: "Fox Chase Cancer Center Buckingham"], primaryKey: false)
    insert('organizations', [id: 108031, nci_institute_code: "WY016", name: "Rocky Mountain Oncology"], primaryKey: false)
  }

  void down() {
    execute("delete from organizations where id = 108018")
    execute("delete from organizations where id = 108019")
    execute("delete from organizations where id = 108020")
    execute("delete from organizations where id = 108021")
    execute("delete from organizations where id = 108022")
    execute("delete from organizations where id = 108023")
    execute("delete from organizations where id = 108024")
    execute("delete from organizations where id = 108025")
    execute("delete from organizations where id = 108026")
    execute("delete from organizations where id = 108027")
    execute("delete from organizations where id = 108028")
    execute("delete from organizations where id = 108029")
    execute("delete from organizations where id = 108030")
    execute("delete from organizations where id = 108031")
  }

}