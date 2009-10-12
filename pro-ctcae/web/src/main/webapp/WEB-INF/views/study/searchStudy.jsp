<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

    <!--<title>Search Studies</title>-->
    <tags:javascriptLink name="extremecomponents"/>
    <tags:dwrJavascriptLink objects="study"/>
    <style type="text/css">
        /* Override default lable length */


        div.row div.label {
            width: 9em;
        }

        div.row div.value {
            margin-left: 10em;
        }

        .endpanes {
            clear: both;
        }
    </style>
    <script>

        function buildTable(form) {

            var type = $F('searchType')
            var text = $F('searchText')

            if (text == '') {
                $('error').innerHTML = "<font color='#FF0000'>Provide at least one character in the search field</font>";

            } else {
                $('error').innerHTML = ""
                $('bigSearch').show()
                //		//showing indicator and hiding pervious results. (#10826)
                $('indicator').className = '';
                //	$('assembler_table').hide();  //do not hide the results..becz filter string get disappear
                var parameterMap = getParameterMap(form);

                study.searchStudies(parameterMap, type, text, showTable);
            }
        }

        function navigate(e) {
            if (event.keyCode == 13)  //enter pressed
                doSend();
        }
        document.onkeypress = navigate;
        function doSend() {
            buildTable('assembler');
        }

    </script>
</head>
<body>
<chrome:box title="study.label.search" autopad="true">
    <p><tags:instructions code="study.search.top"/></p>

    <div class="content">

        <div class="row" name="inputs">
            <div class="label"> Search By:</div>
            <div class="value">
                <select id="searchType">
                    <c:forEach items="${searchCriteria}" var="item">
                        <option value="${item.code}">${item.desc}</option>
                        <%--<input type="options" items="${searchCriteria}" itemLabel="desc" itemValue="code"/>--%>
                    </c:forEach>

                </select>
                <input type="text" id="searchText" size="25">
                    <%--<form:input path="searchCriteria[${status.index}].searchText" size="25"/>--%>

                <div id="error"></div>
            </div>
        </div>
        <div style="padding-left:132px">
        <tags:button color="blue" icon="search" size="big" type="button" onclick="buildTable('assembler');" value='Search'/>
            </div>
        <tags:indicator id="indicator"/>
    </div>
</chrome:box>


<div id="bigSearch" style="display:none;">
    <div class="endpanes"/>
    <chrome:box title="Results">
        <p><tags:instructions code="study.search.results"/></p>
        <form:form id="assembler">
            <chrome:division id="single-fields">
                <div id="tableDiv">
                    <c:out value="${assembler}" escapeXml="false"/>
                </div>
            </chrome:division>
        </form:form>
    </chrome:box>
</div>

</body>
</html>