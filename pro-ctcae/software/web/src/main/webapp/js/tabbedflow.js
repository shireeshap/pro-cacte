AE.clickSrc;
AE.tabbedFlowUpdateTarget = function(evt) {
    var a = Event.element(evt);
    var eleClassNames = "";
	var tabclass = "";
	//Save & Back button has table and image tags
	//In Chrome if user clicks on button, the click event may trigger on table, tr, td, img elements(in firefox the click event trigger on button)
	//So if element is not button then we have to get button element and get the class names
	if(Prototype.Browser.WebKit && jQuery.inArray(a.nodeName.toLowerCase(), ["table", "tr", "td", "img"]) > 0) {
		eleClassNames = jQuery(a).parent().closest('button').attr('class');		
	}	
	else {
		eleClassNames = jQuery(a).attr('class');		
	}    
	if(eleClassNames != "" && eleClassNames.length > 1) {
		jQuery.each(eleClassNames.split(" ") , function(index, value) {      
		        if(value.slice(0, 3) == "tab") {
		        	tabclass = value;
		        }
		});
	}
    var targetPage = tabclass.slice(3)
    if(targetPage != "") {
    	$('_target').name = "_target" + targetPage
    }
    if (Prototype.Browser.IE) {
        if ($('command')._finish) $('_finish').disable()
    } else {
        if ($('command')._finish) $('command')._finish.disable()
    }
    AE.tabbedFlowDisableTarget(evt);
}

// Reload the page with updated message on putting a participant onHold or taking the participant offHold
// Need to set _target property here, to stay on the same page after reload.
AE.RefreshPage = function(){
	var currentPage = $('_target').name.slice(7,8);
	currentPage = currentPage - 1;
	$('_target').name = "_target" + currentPage
    if (Prototype.Browser.IE) {
        if ($('command')._finish) $('_finish').disable()
    } else {
        if ($('command')._finish) $('command')._finish.disable()
    }
    AE.tabbedFlowDisableTarget(evt);
	
}

AE.tabbedFlowSelectAndSubmit = function(click) {
    Event.stop(click)
    AE.tabbedFlowUpdateTarget(click)
    $('command').submit() // command is the default ID for a form created with form:form
}

AE.tabbedFlowDisableTarget = function(click) {
    //click.target.disble() - the event submission process stops in case of IE7,so using hide().
    /*
     var btn = $(click.target);
     if(btn.type == 'submit' || btn.type == 'button'){
     btn.disable();
     AE.clickSrc=btn;
     }
     */
}

Event.observe(window, "load", function() {
    $$("li.tab a").each(function(a) {
        Event.observe(a, "click", AE.tabbedFlowSelectAndSubmit)
    })
    if ($("flow-prev")) Event.observe("flow-prev", "click", AE.tabbedFlowUpdateTarget)
    if ($("flow-update")) Event.observe("flow-update", "click", AE.tabbedFlowUpdateTarget)
    if ($("flow-next")) Event.observe("flow-next", "click", AE.tabbedFlowDisableTarget)
    if ($("putOnHold")) Event.observe("putOnHold", "click", AE.RefreshPage)
    if ($("putOffHold")) Event.observe("putOffHold", "click", AE.RefreshPage)
})