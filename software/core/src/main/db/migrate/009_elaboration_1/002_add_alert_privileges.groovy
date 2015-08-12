class AddCreateOrganizationPrivilege extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
    	 insert('PROCTCAE_PRIVILEGES', [id: -75, privilege_name: "PRIVILEGE_CREATE_ALERT", display_name: "PRIVILEGE_CREATE_ALERT"], primaryKey: false)
     	 insert('PROCTCAE_PRIVILEGES', [id: -76, privilege_name: "PRIVILEGE_EDIT_ALERT", display_name: "PRIVILEGE_EDIT_ALERT"], primaryKey: false)
         
         insert('ROLE_PRIVILEGES', [id: -2027, role_name:"ADMIN", privilege_id: -75], primaryKey: false)
		 insert('ROLE_PRIVILEGES', [id: -2028, role_name:"ADMIN", privilege_id: -76], primaryKey: false)
    }

    void down() {
	     execute("delete from PROCTCAE_PRIVILEGES where id = -75")
	     execute("delete from PROCTCAE_PRIVILEGES where id = -76")
	     
		 execute("delete from ROLE_PRIVILEGES where id = -2027")
		 execute("delete from ROLE_PRIVILEGES where id = -2028")
    }
}