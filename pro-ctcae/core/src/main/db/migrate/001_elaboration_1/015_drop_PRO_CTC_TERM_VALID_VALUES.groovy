class DropProCtcTermValidValue extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        dropTable("PRO_CTC_TERM_VALID_VALUES")
    }

    void down() {
        dropTable("PRO_CTC_TERM_VALID_VALUES")
    }
}