class PopulateSites extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        // Have to break up the inserts so as not to exceed the java max method length
        m0()
    }

    void m0() {
        insert('Organizations', [ id: 2, name: "National Cancer Institute",nci_institute_code:"NCI"], primaryKey: false)
      	insert('Organizations', [ id: 3, name: "Wake Forest Comprehensive Cancer Center",nci_institute_code:"WAKE"], primaryKey: false)
        insert('Organizations', [ id: 4, name: "Duke University Comprehensive Cancer Center",nci_institute_code:"DUKE"], primaryKey: false)
       insert('Organizations', [ id:5,version:0,name: "Gynecologic Oncology Group",nci_institute_code:"GOG" ], primaryKey: false)
    }

    void down() {
        execute("DELETE FROM Organizations")
    }
}
