class CreateResearchStaff extends edu.northwestern.bioinformatics.bering.Migration {
   void up() {
           createTable("RESEARCH_STAFF") {t ->

           t.addVersionColumn()

           t.addColumn('email_address', 'string', nullable: true)
           t.addColumn('fax_number', 'string', nullable: true)
           t.addColumn('researcher_id', 'string', nullable: false)
           t.addColumn('phone_number', 'string', nullable: true)
           t.addColumn('first_name', 'string', nullable: false)
           t.addColumn('last_name', 'string', nullable: false)
           t.addColumn('middle_name', 'string', nullable: true)
           t.addColumn('title', 'string', nullable: true)
           t.addColumn('address', 'string', nullable: true)
           t.addColumn('organization_id', 'integer', nullable: false)
          
           }

           execute('ALTER TABLE RESEARCH_STAFF ADD CONSTRAINT FK_rs_org FOREIGN KEY (organization_id) REFERENCES ORGANIZATIONS')

       }

    void down() {
           execute("alter table RESEARCH_STAFF drop constraint FK_rs_org")

           dropTable("RESEARCH_STAFF")
           }
        }