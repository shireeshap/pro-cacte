class CreateParticipants extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
         createTable("PARTICIPANTS") {t ->
                 t.addVersionColumn()

                 t.addColumn('birth_date', 'date', nullable: true)
                 t.addColumn('ethnicity', 'string', nullable: true)
                 t.addColumn('gender', 'string', nullable: true)
                 t.addColumn('maiden_name', 'string', nullable: true)
                 t.addColumn('race', 'string', nullable: true)
                 t.addColumn('first_name', 'string', nullable: false)
                 t.addColumn('last_name', 'string', nullable: false)
                 t.addColumn('middle_name', 'string', nullable: true)
                 t.addColumn('title', 'string', nullable: true)
                 t.addColumn('address', 'string', nullable: true)

              }
           }

     void down()  {
              dropTable("PARTICIPANTS")
              }
           }