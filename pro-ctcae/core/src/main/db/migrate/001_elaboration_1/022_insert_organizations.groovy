class AddOrganizations extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    // Have to break up the inserts so as not to exceed the java max method length
    m0()
  }

  void m0() {
    insert('Organizations', [id: 1, name: "National Cancer Institute", nci_institute_code: "NCI"], primaryKey: false)
    insert('Organizations', [id: 2, name: "Cancer and Leukemia Group B", nci_institute_code: "CALGB"], primaryKey: false)
    insert('Organizations', [id: 3, name: "Duke University Medical Center", nci_institute_code: "NC010"], primaryKey: false)
    insert('Organizations', [id: 4, version: 0, name: "Wake Forest University Health Sciences", nci_institute_code: "NC002"], primaryKey: false)
    insert('Organizations', [id: 5, name: "Harvard Medical School", nci_institute_code: "MA039"], primaryKey: false)
    insert('Organizations', [id: 6, name: "Memorial Sloan Kettering Cancer Center", nci_institute_code: "NY016"], primaryKey: false)
    insert('Organizations', [id: 7, name: "M D Anderson Cancer Center", nci_institute_code: "MDA"], primaryKey: false)
    insert('Organizations', [id: 8, version: 0, name: "University of Pennsylvania Medical Center", nci_institute_code: "PA151"], primaryKey: false)
    insert('Organizations', [id: 9, name: "Division of Cancer Prevention", nci_institute_code: "DCP"], primaryKey: false)
    insert('Organizations', [id: 10, name: "Cancer Therapy Evaluation Program", nci_institute_code: "CTEP"], primaryKey: false)
    insert('Organizations', [id: 11, name: "US Food and Drug Administration", nci_institute_code: "USFDA"], primaryKey: false)
  }

  void down() {
    execute("DELETE FROM Organizations")
  }
}
