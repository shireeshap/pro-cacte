class CreateIndicies extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
 		execute('CREATE INDEX studies_short_title_idx ON studies (short_title)')
 		execute('CREATE INDEX studies_assigned_identifier_idx ON studies (assigned_identifier)')


 		execute('CREATE INDEX participants_flname_idx ON participants (first_name)')
 		execute('CREATE INDEX participants_flname_idx2 ON participants (last_name)')

 		execute('CREATE INDEX participants_eg_idx1 ON participants (ethnicity)')
 		execute('CREATE INDEX participants_eg_idx2 ON participants (gender)')


 		execute('CREATE INDEX organizations_namecode_idx1 ON organizations (name)')
 		execute('CREATE INDEX organizations_namecode_idx2 ON organizations (nci_institute_code)')

    }

    void down() {
        dropTable('DROP INDEX studies_short_title_idx')

        dropTable('DROP INDEX participants_flname_idx')
        dropTable('DROP INDEX participants_flname_idx2')

        dropTable('DROP INDEX participants_eg_idx1')
        dropTable('DROP INDEX participants_eg_idx2')

        dropTable('DROP INDEX organizations_namecode_idx1')
        dropTable('DROP INDEX organizations_namecode_idx2')

    }
}