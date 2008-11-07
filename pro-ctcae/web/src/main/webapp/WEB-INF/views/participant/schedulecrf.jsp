<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">
<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:javascriptLink name="extremecomponents"/>
    <script type="text/javascript">
        function addCrfSchedule(studyCrfId, crfIndex) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {
                onComplete:function(transport) {
                    var response = transport.responseText;
                    new Insertion.Before("hiddenDiv-" + crfIndex, response);
                },
                parameters:"subview=subview&studycrfid=" + studyCrfId,
                method:'get'
            })
        }
        function fireDelete(index, divToRemove) {
            if ($('objectsIndexesToRemove').value != '') {
                $('objectsIndexesToRemove').value = $('objectsIndexesToRemove').value + "," + index;
            }
            else {
                $('objectsIndexesToRemove').value = index;
            }
            $(divToRemove).remove();
        }
    </script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false">
    <jsp:attribute name="singleFields">
        <input type="hidden" value="" id="objectsIndexesToRemove" name="objectsIndexesToRemove"/>
        <input type="hidden" name="_finish" value="true"/>
        <c:set var="studycrfs" value="${command.study.studyCrfs}"/>
        <c:forEach items="${studycrfs}" var="studycrf" varStatus="status">
            <chrome:division title="${studycrf.crf.title}">
                <input type="button" value="Add" onClick="addCrfSchedule('${studycrf.id}', '${status.index}')"
                       class="button"/>

                <div align="left" style="margin-left: 50px">
                    <table width="70%" class="tablecontent">
                        <tr id="ss-table-head" class="amendment-table-head">
                            <th class="tableHeader"><tags:requiredIndicator/>Start Date</th>
                            <th class="tableHeader"><tags:requiredIndicator/>Due Date</th>
                        </tr>
                        <tr id="hiddenDiv-${status.index}"></tr>
                    </table>
                </div>
            </chrome:division>
        </c:forEach>
</jsp:attribute>
</tags:tabForm>
</body>
</html>