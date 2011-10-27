class UpdateParticipants extends edu.northwestern.bioinformatics.bering.Migration {

    void up() {
            addColumn("PARTICIPANTS", 'email_address', 'string', nullable: true);
            addColumn("PARTICIPANTS", 'phone_number', 'string', nullable: true);


    }

    void down() {

    		dropColumn("PARTICIPANTS", 'phone_number')
    		dropColumn("PARTICIPANTS", 'email_address')

    }

}