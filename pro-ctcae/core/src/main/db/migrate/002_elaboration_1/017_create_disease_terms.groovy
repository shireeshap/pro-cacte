class CreateDiseaseTerms extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {

       createTable("DISEASE_TERMS") {t ->

          t.addVersionColumn()

          t.addColumn('term', 'string', nullable: true)
          t.addColumn('ctep_term', 'string', nullable: true)
          t.addColumn('disease_category_id', 'integer', nullable:true)

 }
     execute('ALTER TABLE DISEASE_TERMS ADD CONSTRAINT fk_term_category FOREIGN KEY (disease_category_id) REFERENCES DISEASE_CATEGORIES')
 }

  void down() {

       execute("alter table DISEASE_TERMS drop constraint FK_term_category")
       dropTable("DISEASE_TERMS")
        }

        }