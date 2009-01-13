class CreateAddedItems extends edu.northwestern.bioinformatics.bering.Migration {
	     void up() {
     			addColumn('sp_crf_added_questions','page_number', 'integer', nullable: true)
     	}

     	void down() {
     	}

}