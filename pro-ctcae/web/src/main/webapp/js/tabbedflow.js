AE.clickSrc;
AE.tabbedFlowUpdateTarget = function(evt) {
    var a = Event.element(evt)
    var tabclass = Element.classNames(a).detect(function(cls) {
        return cls.slice(0, 3) == "tab"
    })
    var targetPage = tabclass.slice(3)
    $('_target').name = "_target" + targetPage
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
})