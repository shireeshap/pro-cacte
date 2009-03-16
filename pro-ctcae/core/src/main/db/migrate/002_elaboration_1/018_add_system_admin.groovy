import edu.northwestern.bioinformatics.bering.Migration

class AddSystemAdmin extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    insert('USERS', [id: -1001, user_name: "SYSTEM_ADMIN", password: "negxLSkw13n5vOjPFIPjSg==",
            account_non_expired: true, account_non_locked: true, credentials_non_expired: true, enabled: true], primaryKey: false)

    insert('USER_ROLES', [id: -1001, user_id: -1001, role_name: "ADMIN"], primaryKey: false)


  }

  void down() {
  }
}
