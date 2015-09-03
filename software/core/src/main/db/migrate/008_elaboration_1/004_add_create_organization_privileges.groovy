class AddCreateOrganizationPrivilege extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
    	 insert('PROCTCAE_PRIVILEGES', [id: -73, privilege_name: "PRIVILEGE_CREATE_ORGANIZATION", display_name: "PRIVILEGE_CREATE_ORGANIZATION"], primaryKey: false)
     	 insert('PROCTCAE_PRIVILEGES', [id: -74, privilege_name: "PRIVILEGE_EDIT_ORGANIZATION", display_name: "PRIVILEGE_EDIT_ORGANIZATION"], primaryKey: false)
         
         insert('ROLE_PRIVILEGES', [id: -2023, role_name:"ADMIN", privilege_id: -73], primaryKey: false)
		 insert('ROLE_PRIVILEGES', [id: -2024, role_name:"CCA", privilege_id: -73], primaryKey: false)
		 insert('ROLE_PRIVILEGES', [id: -2025, role_name:"ADMIN", privilege_id: -74], primaryKey: false)
		 insert('ROLE_PRIVILEGES', [id: -2026, role_name:"CCA", privilege_id: -74], primaryKey: false)
    }

    void down() {
	     execute("delete from PROCTCAE_PRIVILEGES where id = -73")
	     execute("delete from PROCTCAE_PRIVILEGES where id = -74")
	     
		 execute("delete from ROLE_PRIVILEGES where id = -2023")
		 execute("delete from ROLE_PRIVILEGES where id = -2024")
		 execute("delete from ROLE_PRIVILEGES where id = -2025")
		 execute("delete from ROLE_PRIVILEGES where id = -2026")
    }
}