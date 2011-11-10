class DropCcaPrivilege extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
     	execute('DELETE FROM role_privileges WHERE id=-2')
     	execute('DELETE FROM role_privileges WHERE id=-5')
      }


    void down() {
    }

}