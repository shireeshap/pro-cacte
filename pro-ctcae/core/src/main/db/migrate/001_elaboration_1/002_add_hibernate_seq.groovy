class CreateRide extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {

   execute("create sequence hibernate_sequence")


    }

    void down() {
        dropTable("SEARCHES")
    }
}