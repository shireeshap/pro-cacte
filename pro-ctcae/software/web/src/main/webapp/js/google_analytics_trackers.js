// Determine the tier using hostname from the URL.

 function isTier_SB_DEV() {
	 if(document.location.hostname == 'dev_tier_turn_off_ga.semanticbits.com') {
		 return true;
	 }
	 return false;
 }
 
 function isTier_SB_QA() {
	 if(document.location.hostname == 'symptoms.semanticbits.com') {
		 return true;
	 }
	 return false;
 }
 
 function isTier_SB_Demo() {
	 if(document.location.hostname == 'demo.semanticbits.com') {
		 return true;
	 }
	 return false;
 }
 
 function isTier_NCI_DEV() {
	 if(document.location.hostname == 'symptoms-dev.cancer.gov') {
		 return true;
	 }
	 return false;
 }
 
 function isTier_NCI_STAGE() {
	 if(document.location.hostname == 'symptoms-stage.cancer.gov') {
		 return true;
	 }
	 return false;
 }
 
 function isPROD() {
	 if(document.location.hostname == 'symptoms.cancer.gov') {
		 return true;
	 }
	 return false;
 }