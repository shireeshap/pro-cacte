<%@ page import="gov.nih.nci.ctcae.web.form.SubmitFormCommand" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%
    Object attribute = request.getAttribute("command");
    int numOfSymptoms = ((SubmitFormCommand) attribute).getDisplaySymptoms().size();
    int numOfRows = 0;
    if (numOfSymptoms > 0) {
        if (numOfSymptoms % 3 == 0) {
            numOfRows = numOfSymptoms / 3;
        } else {
            numOfRows = (numOfSymptoms / 3) + 1;
        }
        request.setAttribute("numrows", numOfRows - 1);
    } else {
        request.setAttribute("numrows", numOfRows);
    }
%>
<html>
<head>
<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>
<tags:includeVirtualKeyboard/>
<tags:includePrototypeWindow/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
    body {

    }

    input[type="text"], input[type="password"], textarea {
        background: url(../../images/blue/custom-field.png) repeat-x top;
        border: 1px solid #ccc;
        -moz-border-radius: 6px;
        -webkit-border-radius: 6px;
        border-radius: 6px;
        padding: 9px 10px;
        font-size: 20px;
        -moz-box-shadow: 0 1px 6px #999 inset;
        -webkit-box-shadow: 0 1px 6px #999 inset;
        box-shadow: 0 1px 6px #999 inset;
        text-shadow: 0 1px white;
    }

    input[type="text"]:hover, input[type="password"]:hover, textarea:hover {
        background-position: 0 -50px;
    }

    input[type="text"]:focus, input[type="password"]:focus, textarea:focus {
        background-position: 0 -100px;
        -moz-box-shadow: 0 0px 6px #ccc;
        -webkit-box-shadow: 0 0px 6px #ccc;
        box-shadow: 0 0px 6px #ccc;
    }

    .yui-skin-sam .yui-ac-content li {
        font-size: 20px;
        text-shadow: none;
        font-family: lucida grande, sans-serif;
    }

    * {
        zoom: 1;
    }

    .label {
        font-weight: bold;
        font-size: 15px;
        vertical-align: top;
    }

    #participantSymptomAutoComplete {
        width: 450px;
        padding-bottom: 43px;
    }

    #participantSymptomAutoComplete .yui-ac-content {
        max-height: 20em;
        overflow: auto;
        overflow-x: hidden; /* set scrolling */
        _height: 30em; /* ie6 */
    }

    /*#mytable td {*/
    /*height: 65px;*/
    /*background: #d6d6d6 url(../../images/lightgray-tall.png) repeat-x top;*/
    /*border: 1px solid lightgray;*/
    /*padding: 5px 15px 5px 40px;*/
    /*-moz-border-radius: 8px;*/
    /*-webkit-border-radius: 8px;*/
    /*border-radius: 8px;*/
    /*text-shadow: 0 1px white;*/
    /*vertical-align: middle;*/
    /*}*/

    .buttonLook {
        height: auto; /*display: block;*/
        background: #d6d6d6 url(../../images/lightgray-tall.png) repeat-x top; /*background: url(/proctcae/images/check-icon.png) no-repeat 0 50%;*/
        border: 1px solid lightgray;
        padding: 10px 15px 10px 15px;
        -moz-border-radius: 8px;
        -webkit-border-radius: 8px;
        border-radius: 8px;
        text-shadow: 0 1px white;
        vertical-align: middle;
        text-align: center;
        font-weight: bold;
        font-size: 20px;
    }

    /*#mytable td:hover {*/
    /*background: #d7ffb0 url(../../images/lightgreen-tall.png) repeat-x bottom;*/
    /*color: #245808;*/
    /*text-shadow: 0 1px white;*/
    /*cursor: pointer;*/
    /*border-color: #86bc56;*/
    /*}*/

    .tdHoverLook {
        background: #d3f8ff url(../../images/lightblue-tall.png) repeat-x bottom;
        color: #245468;
        text-shadow: 0 1px white;
        cursor: pointer;
        border-color: #90cbd9;

    }

    #mytable td input {
        display: none;
    }

    .hideTd {
        display: none;
    }

    .showTd {
        display: block;
        width: 30%; /*height: auto;*/
    /*display: block;*/
    /*background: #d6d6d6 url(../../images/lightgray-tall.png) repeat-x top;*/
    /*background: url(/proctcae/images/check-icon.png) no-repeat 0 50%;*/
    /*border: 1px solid lightgray;*/
    /*padding: 10px 15px 10px 15px;*/
    /*-moz-border-radius: 8px;*/
    /*-webkit-border-radius: 8px;*/
    /*border-radius: 8px;*/
    /*text-shadow: 0 1px white;*/
    /*vertical-align: middle;*/
    /*text-align: center;*/
    /*font-weight:bold;*/
    <%----%>
    }

    #mytable td.selected {
        background: #538f32 url(/proctcae/images/green-selected_tall.png) repeat-x top; /*background-image: url("/proctcae/images/check-icon.png");*/
    /*background: url(/proctcae/images/green-selected.png) 2px 0;*/
        color: white;
        text-shadow: 0 -1px #2a6f04;
        cursor: pointer;
        border-color: #538f32;
    }

    .check {
        display: block;
        background: url(/proctcae/images/check-icon.png) no-repeat 0 50%;
        height: 100%;
        margin-right: 7px;
        float: left;
    }

</style>
<tags:includeScriptaculous/>
<tags:dwrJavascriptLink objects="scheduleCrf"/>
<script type="text/javascript">
    var oAC;
    var greeting = "Begin typing here";
    <c:if test="${pageContext.response.locale == 'es'}">
         greeting = "Comenzar a escribir aquí";
    </c:if>

    function initializeAutoCompleter() {
        var oDS = new YAHOO.util.XHRDataSource("matchSymptoms");
        oDS.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;
        oDS.responseSchema = {
            recordDelim: ";",
            fieldDelim: "\t"
        };
        oAC = new YAHOO.widget.AutoComplete("participantSymptomInput", "participantSymptomContainer", oDS);

        oAC.maxResultsDisplayed = 100;
        $('participantSymptomInput').className+=" pending-search";
        $('participantSymptomInput').value = greeting;

        Event.observe($('participantSymptomInput'), 'click', function() {
            if ($('participantSymptomInput').value == greeting) {
                $('participantSymptomInput').value = '';
            }
            //$('participantSymptomInput').removeClassName('pending-search');
            removeCssClass($('participantSymptomInput'), 'pending-search');
        })
        Event.observe($('participantSymptomInput'), 'blur', function() {
            if ($('participantSymptomInput').value == '')
            {
                $('participantSymptomInput').value = greeting;
                $('participantSymptomInput').className+=" pending-search";
            }
        })
    }

    var nextColumnIndex = 0;
    var tdCount = 0;
    function addNewSymptom(selectedChoice) {
        if($F('participantSymptomInput') != greeting){
            scheduleCrf.checkIfSymptomAlreadyExistsInForm(selectedChoice, function(values) {
                if (values != '') {
                    var request = new Ajax.Request("<c:url value="/pages/participant/confirmSymptom?subview=subview"/>", {
                        parameters:<tags:ajaxstandardparams/>+"&values=" + values + "&selectedChoice=" + selectedChoice + "&isMapped=" + false +"&CSRF_TOKEN=${sessionScope.CSRF_TOKEN}",
                        onComplete:function(transport) {
                        	if(isIe7()){
                        		myWindow = window.open('','','width=600,height=250');
                        		myWindow.document.write(transport.responseText);
                        		myWindow.focus();
                        	} else {
                        		showConfirmationWindow(transport, 600, 250);
                        	}
                            clearInput();
                        },
                        method:'post'
                    })
                } else {
                    checkMapping(selectedChoice);
                    addSymptom(selectedChoice);
                }

            })
        }
    }

    function checkMapping(selectedChoice) {
        scheduleCrf.checkIfSymptomMapsToProctc(selectedChoice, function(values) {
            if (values != '') {

                var request = new Ajax.Request("<c:url value="/pages/participant/confirmSymptom?subview=subview"/>", {
                    parameters:<tags:ajaxstandardparams/>+"&mappedValues=" + values + "&selectedChoice=" + selectedChoice + "&isMapped=" + true +"&CSRF_TOKEN=${sessionScope.CSRF_TOKEN}",
                    onComplete:function(transport) {
                    	if(isIe7()){
                    		myWindow = window.open('','','width=500,height=150');
                    		myWindow.document.write(transport.responseText);
                    		myWindow.focus();
                    	} else {
                            showConfirmationWindow(transport, 500, 150);
                    	}
                    },
                    method:'post'
                })

            }
        })
    }

    function alertForAdd() {
        var request = new Ajax.Request("<c:url value="/pages/participant/alertForAdd"/>", {
            onComplete:function(transport) {
        	if(isIe7()){
        		myWindow = window.open('','','width=650,height=180');
        		myWindow.document.write(transport.responseText);
        		myWindow.focus();
        	} else {
        		showConfirmationWindow(transport, 650, 180);
        	}
            },
            parameters:<tags:ajaxstandardparams/> +"&index=ind",
            method:'get'
        })
    }


    function addSymptom(escapedSelectedChoice) {
        var selectedChoice = unescape(escapedSelectedChoice);
        var checkboxitems = document.getElementsByName('symptomsByParticipants');
        var itemfound = false;
        for (var i = 0; i < checkboxitems.length; i++) {
            if (checkboxitems[i].value == selectedChoice) {
                checkboxitems[i].checked = true;
                itemfound = true
                changeClass(checkboxitems[i], checkboxitems[i].id);
                break;
            }
        }
        if (!itemfound) {
            addCheckbox(selectedChoice);
        }
        clearInput();
        initSearchField();

    }
    function clearInput() {
        $('participantSymptomInput').clear();
        $('participantSymptomInput').value = greeting;
        $('participantSymptomInput').className = "yui-ac-input pending-search";
    }
    function addCheckbox(selectedChoice) {
        clearInput();

        if (selectedChoice == '') {
            return;
        }
    //    if (nextColumnIndex % 3 == 0) {
    //        var idVar = Math.floor(nextColumnIndex / 3) * 3;
          var idVar = nextColumnIndex ;
            var tbody = document.getElementById('mytable').getElementsByTagName("TBODY")[0];
            var row = document.createElement("TR");

            var td2 = document.createElement("TD");
            td2.id = 'td_' + nextColumnIndex + '_b';
            $(td2).className+=" buttonLook";
            td2.onmouseover = function() {
                addHoverClass(idVar);
            }
            td2.onmouseout = function() {
                removeHoverClass(idVar);
            }

            tdCount = nextColumnIndex + 2;

            row.appendChild(td2);
            tbody.appendChild(row);

            td2.onclick = function() {
                var v = idVar;
                changeTdClass(v);
            }

        var tdb = document.getElementById('td_' + nextColumnIndex + '_b');
        var chkbox = document.createElement('input');
        chkbox.type = "checkbox";
        chkbox.name = 'symptomsByParticipants';
        chkbox.value = selectedChoice;
        chkbox.id = nextColumnIndex;
        chkbox.onchange = function() {
            changeClass(chkbox, chkbox.id);
        }

        var divTag = document.createElement("div");
        divTag.id = "div1_" + nextColumnIndex;
        divTag.setAttribute("align", "left");
        divTag.style.margin = "0px auto";
        divTag.className += " check";
        divTag.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
        if (nextColumnIndex > 0) {
            tdb.className+=" buttonLook";
        }
        tdb.appendChild(chkbox);
        tdb.appendChild(divTag);
        selectCheckBox(nextColumnIndex);

        var div = document.createElement('div');
        div.setAttribute('id', 'div_' + nextColumnIndex);
        div.appendChild(document.createTextNode(selectedChoice));
        tdb.appendChild(div);
        changeClass(chkbox, nextColumnIndex);
        nextColumnIndex++;
    }

    function selectCheckBox(index) {
        var x = document.getElementById(index);
        var td = $('td_' + index + '_b');
        x.checked = true;
        td.className+=" selected";
    }

    function changeTdClass(index) {
    //    alert(index-1);
        var ind = index;
    //           alert(ind);
        var x = $('' + ind);
    //    alert(x);
        var td = $('td_' + ind + '_b');
        var div = $('div1_' + ind);
    //    alert(td);
        if (x.checked) {
    //            alert("checked");
            x.checked = false;
    //            document.getElementById("img_"+index).style.display = "none";
            //$(td).removeClassName("selected");
            removeCssClass($(td), "selected");
            $(td).className+="";
            //div.removeClassName('check');
            removeCssClass($(div), "check");
            div.className+=" hideTd";
        } else {
    //            alert("uncheck");
    //                document.getElementById("img_" + index).style.display = "block";
            x.checked = true;
            //td.removeClassName("");
            //removeCssClass($(div), "check");
            td.className+=" selected";
            div.className+=" check";
        }
    }

    function removeTdClass(index, count) {
        var columnIndex = index + 1;
    //            alert("a");
        while (columnIndex <= count) {
    //                alert(columnIndex);
    //                alert(count);
            //$('td_' + columnIndex + '_b').removeClassName('buttonLook');
            removeCssClass($('td_' + columnIndex + '_b'), "buttonLook");
    //        $('td_' + columnIndex + '_b').addClassName('hideTd');
            columnIndex++;
        }
    }

    function addHoverClass(index) {
    //    alert(1);
        var columnIndex = index;
        $('td_' + columnIndex + '_b').className+=" tdHoverLook";
    }
    function removeHoverClass(index) {
        var columnIndex = index;
        //$('td_' + columnIndex + '_b').removeClassName('tdHoverLook');
        removeCssClass($('td_' + columnIndex + '_b'), "tdHoverLook");
    }

    Event.observe(window, "load", function() {
        initializeAutoCompleter()
    })

    function changeClass(obj, index) {
        var div = $('div_' + index);
        if (obj.checked) {
            //$(div).removeClassName("norm");
            removeCssClass($(div), "norm");
            $(div).className+=" over";
        } else {
            //div.removeClassName("over");
            removeCssClass($(div), "over");
            div.className+=" norm";
        }
    }
    function submitForm(direction, eq5dFlow) {
        if(eq5dFlow === 'false' && ($F('participantSymptomInput'))!= greeting){
            alertForAdd();
            return;
        }    
        document.myForm.direction.value = direction;
        document.myForm.submit();
    }

    function sendConfirmedSymptom() {
        var selectedValueNew = escape($('participantSymptomInput').value);
        addSymptom(selectedValueNew);
        closeWindow();
    }

	function removeCssClass(element, classToRemove){
		var origCss = element.className;
		var origCssParts = origCss.split(" ");
		var finalCss = "";
		for ( var i = 0; i < origCssParts.length; i++ ){
			if(origCssParts[i].trim() != classToRemove){
				finalCss+=origCssParts[i].trim() + " ";
			}
		}
		element.className="";
		element.className=finalCss;
	}

	String.prototype.trim=function(){return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');};

	function isIe7(){
		if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){ //test for MSIE x.x;
			 var ieversion=new Number(RegExp.$1) // capture x.x portion and store as a number
			 if (ieversion>=9)
				 return false;
			 else if (ieversion>=8)
				 return false;
			 else if (ieversion>=7)
			  	return true;
			 else if (ieversion>=6)
				 return false;
			 else if (ieversion>=5)
				 return false;
		}
	}
    </script>
</head>
<body>
    <ctcae:form method="post" name="myForm" id="myForm"> 
        <c:if test="${command.isEq5dCrf}">
            <div style="padding-left:835px;font-size:12px;color: #666666;">
		        <spring:message code="current.page"/>: ${command.newPageIndex}
		        <spring:message code="pages.left"/> ${command.totalPages}
		    </div>
		    <table cellspacing="0">
		        <tr>
		            <td width="80%">
		                 
		            </td>
		            <td width="4%">
		                <div style="font-size:12px;color: #666666;">
		                 <spring:message code="progress"/>:
		                </div>
		            </td>
		            <td valign="middle" width="20%">
		                 <div class='progress-bar-outer'>
		                    <div class='progress-bar-inner' style="width: ${(command.newPageIndex/command.totalPages)*150}px;"></div>
		                </div>
		            </td>
		        </tr>
		    </table> 
		</c:if>
        <chrome:box autopad="true" message="false">
	    <c:choose>
	        <c:when test="${command.isEq5dCrf}">
	           
	            <table width="100%"> 
			        <tr>
			            <td width="85%" valign="top"></td>
			            <td align="center"><b><spring:message code="eq5d.vas.instructions.1"/></b></td></tr>
			        <tr>
			            <td valign="top">
			                    <ul style="list-style-type:disc;padding-left:70px;text-align:left">
									<li style="font-size:16px;height:50px"><spring:message code="eq5d.vas.instructions.2"/></li>
									<li style="font-size:16px;height:50px"><spring:message code="eq5d.vas.instructions.3"/></li>
									<li style="font-size:16px;height:50px"><spring:message code="eq5d.vas.instructions.4"/>&nbsp;<u><spring:message code="eq5d.vas.instructions.5"/></u>&nbsp;<spring:message code="eq5d.vas.instructions.6"/><br/> 
									        <spring:message code="eq5d.vas.instructions.7"/>&nbsp;<u><spring:message code="eq5d.vas.instructions.8"/></u>&nbsp;<spring:message code="eq5d.vas.instructions.9"/></li>
									<li style="font-size:16px;height:50px"><spring:message code="eq5d.vas.instructions.10"/></li>
									<li style="font-size:16px;height:50px"><spring:message code="eq5d.vas.instructions.11"/></li>
							    </ul>
							  <p style="padding-left:70px;"><br/><br/>
			                  <b><spring:message code="eq5d.vas.instructions.12"/>&nbsp;</b>
			                   <input type="text" name="healthAmount" id="healthAmount" title="Health Amount" value="${command.schedule.healthAmount}" size="3" style="text-align:center"/>   
			                   </p>         
			            </td>    
			            <td align="center">
			              <div id="slider-vertical" style="height: 400px;">
			              </div>
			             </td>
			        </tr>
			        <tr>
			             <td></td><td align="center"><b><spring:message code="eq5d.vas.instructions.13"/></b></td>
			        </tr>
			    </table>        
			    
		        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
		        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
		        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
		        <tags:stylesheetLink name="jQuery-theme/jQuery-slider/style"/>
		        <script>
		            jQuery(function () {
		                jQuery("#slider-vertical").slider({
		                    orientation : "vertical",
		                    range : "min",
		                    min : 0,
		                    max : 100,
		                    value : ${command.schedule.healthAmount != null ? command.schedule.healthAmount : 0 },
		                    slide : function (event, ui) {
		                        //jQuery("#healthAmountDisplay").val(ui.value);
		                        jQuery("#healthAmount").val(ui.value);
		                    }
		                });
		                //jQuery("#healthAmount").val(jQuery("#slider-vertical").slider("value"));
		            });
		        </script>
		        <script>
		        $.noConflict();
		        
		        </script>
	        </c:when>
	        <c:otherwise>
	                <p style="font-size:18px">
				        <b><tags:message code="participant.form.typesymptom"/></b>
				    </p>
				
				    <div id="keyboardDiv"></div>
				    <br/>
				    <div class="yui-skin-sam">
				        <c:set value=" ${sessionScope['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE']}"
				               var="lang"/>
				        <table cellspacing="10px;" border="0">
				            <tr>
				                <td width="80%">
				                    <div id="participantSymptomAutoComplete">
				                        <input id="participantSymptomInput" type="text">
				                        <div id="participantSymptomContainer"></div>
				                    </div>
				                </td>
				                <td>
				                    <spring:message code="add" var="add"/>
				                    <a onclick="javascript:addNewSymptom($('participantSymptomInput').value)"
				                       class="btn green-med"><span><spring:message code="add"/></span></a>
				                </td>
				                <td>
				                    <spring:message code="clear" var="clear"/>
				                    <a onclick="javascript:clearInput()" class="btn red-med"><span><spring:message code="clear"/></span></a>
				                </td>
				            </tr>
				        </table>
				
				    </div>
				
				    <div class="row" style="margin-left: 15px;font-size:20px">
				        <input id='usevirtualkeyboard' type="checkbox"
				               onclick="showVirtualKeyBoard(this,'participantSymptomInput');">
				         <img src="/proctcae/images/keyboard-icon.png"/>
				        <spring:message code="virtualKeyboard"/>
				    </div>
	        </c:otherwise>
	    </c:choose>


        <input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
        <table id="mytable" width="100%" border="0">
            <tbody>
            <tr>
                <td width="32%" colspan="1">
                <td width="32%" colspan="1">
                <td width="32%" colspan="1">
            </tr>
            </tbody>
        </table>
        <input type="hidden" name="direction"/>
        </chrome:box>
    </ctcae:form>


<table width="100%" cellspacing="10">
    <tr>
        <td align="right" width="50%">
            <spring:message code="back" var="back"/>
            <a href="#" class="btn big-blue-left" onclick="javascript:submitForm('back', '${command.isEq5dCrf}')"><span>${back}</span></a>
        </td>
        <td align="left" width="50%">
            <spring:message code="next" var="next"/>
            <a href="#" class="btn huge-green" onclick="javascript:submitForm('continue', '${command.isEq5dCrf}')"><span>${next}</span></a>
        </td>
    </tr>
</table>

</body>
</html>