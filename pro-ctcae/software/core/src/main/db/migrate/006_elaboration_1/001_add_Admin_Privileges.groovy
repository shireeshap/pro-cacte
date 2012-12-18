class AddAdminPrivileges extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
         insert('role_privileges', [id: -2021, role_name:"ADMIN", privilege_id: -403], primaryKey: false)
		 insert('role_privileges', [id: -2022, role_name:"ADMIN", privilege_id: -404], primaryKey: false)
    }

    void down() {
	     execute("delete from role_privileges where id = -2021")
		 execute("delete from role_privileges where id = -2022")
    }

}	