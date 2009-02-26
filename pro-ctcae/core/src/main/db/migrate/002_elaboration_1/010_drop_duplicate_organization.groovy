import edu.northwestern.bioinformatics.bering.Migration

class DropDuplicateOrganization extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    execute('delete from organizations where id=-6')
    execute('delete from organizations where id=103999')
    execute('delete from organizations where id=104276')
    execute('delete from organizations where id=106534')

  }

  void down() {

  }
}