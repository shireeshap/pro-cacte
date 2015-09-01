class AddCtcAEv4Terms extends edu.northwestern.bioinformatics.bering.Migration {
	void up() {
		// Have to break up the inserts so as not to exceed the java max method length
		m0()
		m1()
		m2()
		m3()
		m4()
	}

	void m0() {
        insert('ctc', [id: 4, name: "CTC v4.0"], primaryKey: false)
	
        insert('ctc_categories', [version_id: 4, id: 401, name: 'Musculoskeletal and connective tissue disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 402, name: 'Skin and subcutaneous tissue disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 403, name: 'Psychiatric disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 404, name: 'General disorders and administration site conditions'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 405, name: 'Gastrointestinal disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 406, name: 'Eye disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 407, name: 'Reproductive system and breast disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 408, name: 'Injury, poisoning and procedural complications'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 409, name: 'Respiratory, thoracic and mediastinal disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 410, name: 'Metabolism and nutrition disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 411, name: 'Nervous system disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 412, name: 'Renal and urinary disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 413, name: 'Vascular disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 414, name: 'Cardiac disorders'], primaryKey: false)
        insert('ctc_categories', [version_id: 4, id: 415, name: 'Ear and labyrinth disorders'], primaryKey: false)
	}

	void m1() {
        insert('ctc_terms', [category_id: 401, id: 5001, term: "Arthralgia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 401, id: 5002, term: "Myalgia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5003, term: "Rash acneiform", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 403, id: 5004, term: "Anxiety", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 404, id: 5005, term: "Edema limbs", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5006, term: "Skin ulceration", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5007, term: "Abdominal distension", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 406, id: 5008, term: "Blurred vision", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5009, term: "Body odor", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 407, id: 5010, term: "Gynecomastia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 408, id: 5011, term: "Bruising", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5012, term: "Nail discoloration", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5013, term: "Constipation", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 409, id: 5014, term: "Cough", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 410, id: 5015, term: "Anorexia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 403, id: 5016, term: "Libido decreased", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5017, term: "Hypohidrosis", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 407, id: 5018, term: "Erectile dysfunction", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5019, term: "Dysphagia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 411, id: 5020, term: "Dizziness", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        	}

	void m2() {
insert('ctc_terms', [category_id: 405, id: 5021, term: "Dry mouth", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5022, term: "Dry skin", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 407, id: 5023, term: "Ejaculation disorder", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 404, id: 5024, term: "Fatigue", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 403, id: 5025, term: "Depression", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 406, id: 5026, term: "Flashing lights", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 412, id: 5027, term: "Urinary frequency", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5028, term: "Alopecia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5029, term: "Palmar-plantar erythrodysesthesia syndrome", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5030, term: "Dyspepsia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 409, id: 5031, term: "Hiccups", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5032, term: "Urticaria", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 409, id: 5033, term: "Hoarseness", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 413, id: 5034, term: "Hot flashes", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5035, term: "Flatulence", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5036, term: "Photosensitivity", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 403, id: 5037, term: "Insomnia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 407, id: 5038, term: "Irregular menstruation", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5039, term: "Pruritus", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5040, term: "Diarrhea", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
	}

	void m3() {
        insert('ctc_terms', [category_id: 402, id: 5041, term: "Nail loss", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5042, term: "Fecal incontinence", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 412, id: 5043, term: "Urinary incontinence", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5044, term: "Mucositis oral", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5045, term: "Nausea", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 409, id: 5046, term: "Epistaxis", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 411, id: 5047, term: "Peripheral sensory neuropathy", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 403, id: 5048, term: "Anorgasmia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 404, id: 5049, term: "Pain", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 407, id: 5050, term: "Dyspareunia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5051, term: "Abdominal pain", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 412, id: 5052, term: "Urinary tract pain", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 404, id: 5053, term: "Injection site reaction", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 414, id: 5054, term: "Palpitations", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 411, id: 5055, term: "Concentration impairment", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 411, id: 5056, term: "Memory impairment", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 411, id: 5057, term: "Dysgeusia", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5058, term: "Rash maculo-papular", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5059, term: "Nail ridging", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 415, id: 5060, term: "Tinnitus", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
	}

	void m4() {
        insert('ctc_terms', [category_id: 404, id: 5061, term: "Chills", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 409, id: 5062, term: "Dyspnea", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 408, id: 5063, term: "Dermatitis radiation", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5064, term: "Skin and subcutaneous tissue disorders - Other, specify (Cheilitis)", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 406, id: 5065, term: "Floaters", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5066, term: "Skin and subcutaneous tissue disorders - Other, specify (Striae)", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 412, id: 5067, term: "Urinary urgency", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 403, id: 5068, term: "Delayed orgasm", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 411, id: 5069, term: "Tremor", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5070, term: "Hyperhidrosis", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 402, id: 5071, term: "Skin hyperpigmentation", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 407, id: 5072, term: "Vaginal discharge", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 412, id: 5073, term: "Urine discoloration", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 407, id: 5074, term: "Vaginal dryness", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 409, id: 5075, term: "Voice alteration", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 405, id: 5076, term: "Vomiting", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 406, id: 5077, term: "Watering eyes", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
        insert('ctc_terms', [category_id: 409, id: 5078, term: "Wheezing", ctep_term: "", ctep_code: "", other_required: "1"], primaryKey: false)
	}



	void down() {
		execute("DELETE FROM ctc_terms WHERE category_id > 300 AND category_id < 400")
		execute("DELETE FROM ctc_categories WHERE version_id=3")
	}
}
