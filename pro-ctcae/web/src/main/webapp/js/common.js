// Namespace for caAERS-specific shared functions and classes
var AE = { }

AE.INDICATOR_REF_COUNTS = { };

// this stuff should technically be synchronized.  Let see if it causes a problem.
AE.showIndicator = function(id) {
    if (!AE.INDICATOR_REF_COUNTS[id]) AE.INDICATOR_REF_COUNTS[id] = 0;
    AE.INDICATOR_REF_COUNTS[id] += 1
    AE.updateIndicatorVisibility(id)
}

AE.hideIndicator = function(id) {
    if (!AE.INDICATOR_REF_COUNTS[id]) AE.INDICATOR_REF_COUNTS[id] = 0;
    AE.INDICATOR_REF_COUNTS[id] -= 1;
    if (AE.INDICATOR_REF_COUNTS[id] < 0) AE.INDICATOR_REF_COUNTS[id] = 0;
    AE.updateIndicatorVisibility(id)
}

AE.updateIndicatorVisibility = function(id) {
    if ($(id) != null) {
        if (AE.INDICATOR_REF_COUNTS[id] > 0) {
            $(id).reveal();
        } else {
            $(id).conceal();
        }
    }
}

////// PROTOTYPE EXTENSIONS
// TODO: This code is shared with PSC.

Element.addMethods({
    // Like prototype's hide(), but uses the visibility CSS prop instead of display
    conceal: function() {
        for (var i = 0; i < arguments.length; i++) {
            var element = $(arguments[i]);
            element.style.visibility = 'hidden';
        }
    },

    // Like prototype's show(), but uses the visibility CSS prop instead of display
    reveal: function() {
        for (var i = 0; i < arguments.length; i++) {
            var element = $(arguments[i]);
            element.style.visibility = 'visible';
        }
    },

    // Disable all form elements contained in this element and add the class "disabled"
    disableDescendants: function() {
        for (var i = 0; i < arguments.length; i++) {
            var element = $(arguments[i]);
            element.addClassName("disabled")
            element.descendants().each(function(elt) {
                if (elt.disable) elt.disable()
            })
        }
    },

    // Enable all form elements contained in this element and remove the class "disabled"
    enableDescendants: function() {
        for (var i = 0; i < arguments.length; i++) {
            var element = $(arguments[i]);
            element.removeClassName("disabled")
            element.descendants().each(function(elt) {
                if (elt.enable) elt.enable()
            })
        }
    }
});

Form.selectedRadioValue = function(form, radioName) {
    var pair = $A($(form)[radioName])
            .collect(Form.Element.Serializers.inputSelector)
            .detect(function(e) {
        return e != null
    })
    return pair ? pair[1] : null;
}

////// CALENDAR POPUP HANDLERS

AE.registerCalendarPopups = function(containerId) {
    var sel = "input.date"
    if (containerId) sel = "#" + containerId + " " + sel
    $$(sel).each(function(input) {
        var anchorId = input.id + "-calbutton"
        Calendar.setup(
        {
            inputField  : input.id,
            button      : anchorId,
            ifFormat    : "%m/%d/%Y", // TODO: get this from the configuration
            weekNumbers : false
        }
                );
    })

    //for split-date
    sel = "input.split-date"
    if (containerId) sel = "#" + containerId + " " + sel
    $$(sel).each(function(input) {
        var yearInputId = input.id
        var baseInputId = input.id.substring(0, input.id.lastIndexOf('.yearString'))
        var anchorId = baseInputId + "-calbutton"
        var monthInputId = baseInputId + ".monthString"
        var dayInputId = baseInputId + ".dayString"

        Calendar.setup(
        {
            'yearInputId' : yearInputId,
            'monthInputId': monthInputId,
            'dayInputId'  :dayInputId,
            button        : anchorId,
            ifFormat      : "%m/%d/%Y", // TODO: get this from the configuration
            weekNumbers   : false,
            onSelect      : function(cal) {
                $(cal.params['dayInputId']).value = cal.date.print('%d')
                $(cal.params['monthInputId']).value = cal.date.print('%m')
                $(cal.params['yearInputId']).value = cal.date.print('%Y')
            }
        }
                );
    })
}

Element.observe(window, "load", function() {
    AE.registerCalendarPopups()
});

////// SSO

Event.observe(window, "load", function() {
    $$("a.sso").each(function(a) {
        Event.observe(a, "click", function(e) {
            Event.stop(e)
            var ssoForm = $('sso-form')
            ssoForm.action = a.href
            ssoForm.submit()
        })
    })
})


//////// SEARCH helpers

function showTable(table) {
    $('indicator').className = 'indicator'
    document.getElementById('tableDiv').innerHTML = table;
}

function copyValues(select, prop) {

    var selectArray = $(select).options;
    for (i = 0; i < selectArray.length; i++) {
        if (selectArray[i].selected) {
            $(prop).value = selectArray[i].value == "---" ? "" : selectArray[i].value
        }
    }
}

/////  autocompleter search fields
function initSearchField() {

    $$("input[type=text].autocomplete").each(function(theInput)
    {
        addEventHandlersForAutoCompleter(theInput)
    });

    $$("input[type=text][class='autocomplete validate-NOTEMPTY']").each(function(theInput)
    {
        addEventHandlersForAutoCompleter(theInput)
    });

}
function addEventHandlersForAutoCompleter(theInput) {

    var message = '(Begin typing here)';

    Event.observe(theInput, 'focus', clearDefaultText);
    Event.observe(theInput, 'blur', replaceDefaultText);

    /* Save the current value */
    if (theInput.value == '') {
        /* Add event handlers */

        theInput.defaultText = message;
        theInput.className = 'pending-search';
        theInput.value = message;
    }
}
function clearDefaultText(e) {
    var target = window.event ? window.event.srcElement : e ? e.target : null;
    if (!target) return;

    if (target.value == '(Begin typing here)') {
        target.value = '';
        target.className = 'autocomplete';

    }

}

function replaceDefaultText(e) {
    var target = window.event ? window.event.srcElement : e ? e.target : null;
    if (!target) return;

    if (target.value == '') {
        $(target.id.substring(0, target.id.length - 6)).value = ''
        target.value = '(Begin typing here)';
        target.className = 'pending-search';
    }

}

function updateTypeAndText(divId, type, text) {
    if ($(divId).value.length > 0) {
        text = text + $(divId).value + ",";
        type = type + divId + ',';
    }
}

function showAjaxLoadingImage() {
    Dialog.info("", {width:250, height:100, className: "alphacube", showProgress: true});

}
function closeAjaxLoadingImage() {
    Dialog.closeInfo()
}
function showConfirmationWindow(transport, width, height, top, left) {
    if (typeof(width) == 'undefined') {
        width = 600;
    }
    if (typeof(height) == 'undefined') {
        height = 300;
    }
    if (typeof(top) == 'undefined') {
        top = 250;
    }
    if (typeof(width) == 'undefined') {
        left = 200;
    }
    var win = Windows.getFocusedWindow();
    if (win == null) {
        win = new Window({ id: '100' , className: "alphacube", closable : true, minimizable : false, maximizable :
                true, title: "", height:height, width: width,top:top,left:left});
        win.setDestroyOnClose();
        win.setHTMLContent(transport.responseText);
        win.showCenter(true);
    } else {
        win.setHTMLContent(transport.responseText);
        win.refresh();
    }
    AE.registerCalendarPopups();
    return win;
}
function closeWindow() {
    var win = Windows.getFocusedWindow();
    if (win != null) {
        win.close();
    }
}
function acPostSelect(mode, selectedChoice) {
    $(mode.basename).value = selectedChoice.id;
    if ( selectedChoice.id == '') {
        $(mode.basename + "-input").value = '';
    }
}
function acCreate(mode) {
    return new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
            mode.populator, {
        valueSelector: mode.valueSelector,
        afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
            acPostSelect(mode, selectedChoice)
        },
        indicator: mode.basename + "-indicator"
    })

}
var siteAutoComplter = Class.create();
Object.extend(siteAutoComplter.prototype, {
    initialize: function(basename) {

        this.basename = basename;
        this.populator = function(autocompleter, text) {
            organization.matchOrganization(text, function(values) {
                autocompleter.setChoices(values)
            })
        },
                this.valueSelector = function (obj) {
                    return obj.displayName;
                }

    }


});

var organizationClinicalStaffAutoComplter = Class.create();
Object.extend(organizationClinicalStaffAutoComplter.prototype, {
    initialize: function(basename, studyOrganizationId) {

        this.basename = basename;
        this.studyOrganizationId = studyOrganizationId;
        this.populator = function(autocompleter, text) {
            clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(text, studyOrganizationId, function(values) {
                autocompleter.setChoices(values)
            })
        },
                this.valueSelector = function (obj) {
                    return obj.displayName;
                }

    }
});

var studySiteAutoComplter = Class.create();
Object.extend(studySiteAutoComplter.prototype, {
    initialize: function(basename, studyId) {

        this.basename = basename;
        this.studyId = studyId;
        this.populator = function(autocompleter, text) {
            organization.matchOrganizationByStudyId(text, studyId, function(values) {
                autocompleter.setChoices(values);
            })
        },
                this.valueSelector = function (obj) {
                    return obj.displayName;
                }

    }
});
var participantAutoCompleter = Class.create();
Object.extend(participantAutoCompleter.prototype, {
    initialize: function(basename, participantPopulator) {
        this.basename = basename;

        if (participantPopulator) {
            this.populator = participantPopulator
        }
        else {
            this.populator = function(autocompleter, text) {
                participant.matchParticipantByStudySiteId(text, studySiteId, studyId, function(values) {
                    autocompleter.setChoices(values)
                })
            }

        }
        this.valueSelector = function (obj) {
            return obj.displayName;
        }

    }
});


//pass empty string for role if you don't want to search for role.
var studyOrganizationClinicalStaffForRoleAutoCompleter = Class.create();
Object.extend(studyOrganizationClinicalStaffForRoleAutoCompleter.prototype, {
    initialize: function(basename, studyOrganizationId, role) {

        this.basename = basename;
        this.organizationId = studyOrganizationId;
        this.role = role;
        this.populator = function(autocompleter, text) {
            clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(text, studyOrganizationId, role, function(values) {
                autocompleter.setChoices(values)
            })
        },
                this.valueSelector = function (obj) {
                    return obj.displayName;
                }

    }
});

var studyAutoCompleter = Class.create();
Object.extend(studyAutoCompleter.prototype, {
    initialize: function(basename) {
        this.basename = basename;
        this.populator = function(autocompleter, text) {
            study.matchStudy(text, function(values) {
                autocompleter.setChoices(values)
            })
        },
                this.valueSelector = function (obj) {
                    return obj.displayName;
                }
    }
});


function initializeAutoCompleter(basename, name, id) {
    if (name != '') {
        $(basename + '-input').value = name;
        $(basename).value = id;
        $(basename + '-input').addClassName('autocomplete');

    }
}
function clearAutoCompleter(basename) {
    $(basename + '-input').value = '';
    $(basename).value = '';
    $(basename + '-input').addClassName('autocomplete');


}

// COLLAPSABLE DIV ELEMENT
////////////////////////////////////////////////////////////////////////////////////////////////

function SwitchCollapsableState(contentElement, id) {

    panelDiv = $(contentElement);
    imageId = 'image-' + id;
    imageSource = $(imageId).src;

    if (panelDiv.style.display == 'none') {
        OpenUp(panelDiv, arguments[1] || {});
        document.getElementById(imageId).src = imageSource.replace('right', 'down');
    } else {
        CloseDown(panelDiv, arguments[1] || {});
        document.getElementById(imageId).src = imageSource.replace('down', 'right');
    }
}

function OpenUp(element) {
    element = $(element);
    new Effect.BlindDown(element, arguments[1] || {});
}

function CloseDown(element) {
    element = $(element);
    new Effect.BlindUp(element, arguments[1] || {});
}

////////////////////////////////////////////////////////////////////////////////////////////////

function dump(arr, level) {
    var dumped_text = "";
    if (!level) level = 0;

    // The padding given at the beginning of the line.
    var level_padding = "";
    for (var j = 0; j < level + 1; j++) level_padding += "    ";

    if (typeof(arr) == 'object') { //Array/Hashes/Objects
        for (var item in arr) {
            var value = arr[item];

            if (typeof(value) == 'object') { //If it is an array,
                dumped_text += level_padding + "'" + item + "' ...\n";
                dumped_text += dump(value, level + 1);
            } else {
                dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
            }
        }
    } else { //Stings/Chars/Numbers etc.
        dumped_text = "===>" + arr + "<===(" + typeof(arr) + ")";
    }
    return dumped_text;
}

function addRemoveConditionalTriggeringDisplayToQuestion() {

    $$("div.sortable").each(function (item) {
        var id = item.id;
        if (!id.include('dummySortable_')) {
            var questionId = id.substr(9, id.length)
            if ($$('tr.conditionalTriggering_' + questionId).length > 0) {
                $('conditionalTriggeringImage_' + questionId).show();
                $("sortable_" + questionId).addClassName('conditional-triggering');
            } else {
                $('conditionalTriggeringImage_' + questionId).hide();
                $("sortable_" + questionId).removeClassName('conditional-triggering');
            }
            var conditionsTable = $('conditionsTable_' + questionId)
            if ($$('tr.conditionalQuestion_' + questionId + '_condition').length == 0) {
                $("sortable_" + questionId).removeClassName('conditional-question');
                $('conditionsImage_' + questionId).hide();
                if (conditionsTable != null) {
                    conditionsTable.hide();
                }

            } else {
                $('conditionsImage_' + questionId).show();
                if (conditionsTable != null) {
                    conditionsTable.show();
                }
                $("sortable_" + questionId).addClassName('conditional-question')
            }
        }
    });

}
function reOrderQuestionNumber() {
    var i = 0;
    $$("div.sortableSpan").each(function (item) {
        item.innerHTML = i + 1;
        //			var orderNumberAtCrfItemPropertiesPage = $$("span.sortableSpan")[parseInt(item.id) - 1];
        //			orderNumberAtCrfItemPropertiesPage.innerHTML = i + 1;
        i = i + 1;


    })


}
function refreshPage() {
    try {
        var currentPage = $('_page').value;
        $('_target').name = '_target' + currentPage;
        $('command').submit();
    } catch(err) {
        var a = $('_target').name;
        var b = a.substr(7);
        var c = b - 1;
        $('_target').name = '_target' + c;
    }
}
