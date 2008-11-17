class CreateDiseaseCategories extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {

       createTable("DISEASE_CATEGORIES") {t ->

          t.addVersionColumn()

          t.addColumn('name', 'string', nullable: true)

 }
 }

  void down() {

       dropTable("DISEASE_CATEGORIES")
        }

        }