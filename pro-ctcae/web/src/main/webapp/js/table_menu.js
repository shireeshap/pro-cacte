function highlightrow(index) {
    $('details_row_' + index).className = 'highlight';
    $('img_' + index).style.visibility = 'visible';
}

function removehighlight(index)
{
    var rEle = $('details_row_' + index);
    rEle.className = '';
    $('img_' + index).style.visibility = 'hidden';
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

function showPopUpMenu(index, pid, sid, x, y) {

    var html = '<a href="../participant/create?id=' + pid + '" class="link">View participant</a><br/><a href="javascript:showResponses(' + sid + ');" class="link">View all responses</a>';
    Element.show($("dropnoteDiv"));
    $("dropnoteDiv").style.left = (findPosX($("img_" + index)) + x) + 'px';
    $("dropnoteDiv").style.top = (findPosY($("img_" + index)) + y) + 'px';
    $("dropnoteinnerDiv").innerHTML = html;
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
