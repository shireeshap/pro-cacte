import edu.northwestern.bioinformatics.bering.Migration

class AlterPrivilegeUser extends edu.northwestern.bioinformatics.bering.Migration {
        void up() {
            insert('PROCTCAE_PRIVILEGES', [id: -10, privilege_name: "PRIVILEGE_VIEW_FORM", display_name: "PRIVILEGE_VIEW_FORM"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2001, role_name: "ADMIN", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2002, role_name: "CCA", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2003, role_name: "ODC", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2004, role_name: "PI", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2005, role_name: "LEAD_CRA", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2006, role_name: "TREATING_PHYSICIAN", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2007, role_name: "NURSE", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2008, role_name: "SITE_PI", privilege_id: "-10"], primaryKey: false)
            insert('ROLE_PRIVILEGES', [id: -2009, role_name: "SITE_CRA", privilege_id: "-10"], primaryKey: false)
            }
        void down() {
            }
}
