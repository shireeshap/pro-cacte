import edu.northwestern.bioinformatics.bering.Migration

class CreateExtensionFuzzymatchstr extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        execute('CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;')
            }

    void down() {
    }

}