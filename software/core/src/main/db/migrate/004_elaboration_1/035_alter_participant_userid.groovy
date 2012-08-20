class AlterParticipantUserNumberType extends edu.northwestern.bioinformatics.bering.Migration {

  void up() {
          execute('ALTER TABLE participants ALTER user_number TYPE text')
  }

  void down() {
  }
}