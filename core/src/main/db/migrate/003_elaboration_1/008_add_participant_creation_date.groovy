import edu.northwestern.bioinformatics.bering.Migration

class AddRolesAndPriveleges extends edu.northwestern.bioinformatics.bering.Migration {
 void up() {
        addColumn("PARTICIPANTS", 'creation_date', 'timestamp', nullable: true); }
 void down() {
 }
}
