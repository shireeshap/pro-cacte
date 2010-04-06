function highlightrow(index) {
    $('details_row_' + index).className = 'highlight';
    //    $('img_' + index).style.visibility = 'visible';
}

function removehighlight(index)
{
    var rEle = $('details_row_' + index);
    rEle.className = '';
    //    $('img_' + index).style.visibility = 'hidden';
    if ($("dropnoteDiv")) {
        Element.hide($("dropnoteDiv"));
        $("dropnoteDiv").onmouseover = function() {
            rEle.className = "highlight";
            $('img_' + index).style.visibility = "visible";
            Element.show($("dropnoteDiv"));
        };
        $("dropnoteDiv").onmouseout = function() {
            rEle.className = "";
            $('img_' + index).style.visibility = "hidden";
            Element.hide($("dropnoteDiv"));
        };
    }
}
function showResponseDetails(symptom) {
    var request = new Ajax.Request("participantAddedQuestionsDetails", {
        parameters:getStandardParamForAjax() + "&symptom=" + symptom + "&crf=" + $('form').value + "&studySite=" + $('studySite').value  ,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 700, 500);
        },
        method:'get'
    })
}

function showPopUpMenu(index, pid, sid, ihtml) {
    var html = '<div id="search-engines"><ul>';
    if (typeof(ihtml) == 'undefined' || ihtml == '') {
        html += '<li><a href="#" onclick="location.href=\'../participant/edit?id=' + pid + '\'">View participant</a></li>';
        html += '<li><a href="#" onclick="javascript:showResponses(' + sid + ')">View all responses</a></li>';
    } else {
        html += ihtml
    }
    html += '</ul></div>';
    jQuery('#menuActions' + sid).menu({
        content: html,
        maxHeight: 180,
        positionOpts: {
            directionV: 'down',
            posX: 'left',
            posY: 'bottom',
            offsetX: 0,
            offsetY: 0
        },
        showSpeed: 300
    });
}

function findPosX(obj) {
    var X = 0;
    if (document.getElementById || document.all) {
        while (obj.offsetParent) {
            X += obj.offsetLeft;
            obj = obj.offsetParent;
        }
    } else {
        if (document.layers) {
            X += obj.x;
        }
    }
    return X;
}
function findPosY(obj) {
    var Y = 0;
    if (document.getElementById || document.all) {
        while (obj.offsetParent) {
            Y += obj.offsetTop;
            obj = obj.offsetParent;
        }
    } else {
        if (document.layers) {
            Y += obj.y;
        }
    }
    return Y;
}
function getLinksHtml(symptom) {
    return '<li><a href="#" onclick="javascript:showResponseDetails(\'' + symptom + '\')">View participant responses</a></li>';
}
