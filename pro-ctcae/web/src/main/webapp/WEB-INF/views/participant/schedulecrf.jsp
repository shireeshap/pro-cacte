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
        function addCrfSchedule(crfIndex) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addCrfSchedule"/>", {
                onComplete:function(transport) {
                    var response = transport.responseText;
                    new Insertion.Before("hiddenDiv-" + crfIndex, response);
                    AE.registerCalendarPopups()
                },
                parameters:"subview=subview&crfindex=" + crfIndex,
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

            <c:forEach items="${command.studyParticipantAssignment.studyParticipantCrfs}" var="participantCrf"
                       varStatus="status">
                <chrome:division title="${participantCrf.studyCrf.crf.title} (${participantCrf.studyCrf.crf.crfVersion})" message="false">
                    <jsp:useBean id="today" class="java.util.Date"/>
                    <c:if test="${participantCrf.studyCrf.crf.nextVersionId eq null}">
                        <input type="button" value="Add" onClick="addCrfSchedule('${status.index}')"
                               class="button"/>
                    </c:if>
                    <div align="left" style="margin-left: 50px">
                        <table width="70%" class="tablecontent">
                            <tr id="ss-table-head" class="amendment-table-head">
                                <th class="tableHeader"><tags:requiredIndicator/><spring:message
                                        code="schedulecrf.label.start_date"/></th>
                                <th class="tableHeader"><tags:requiredIndicator/><spring:message
                                        code="schedulecrf.label.due_date"/></th>
                                <th class="tableHeader"><spring:message code="schedulecrf.label.status"/></th>
                                <th class="tableHeader"></th>
                            </tr>
                            <c:forEach items="${participantCrf.studyParticipantCrfSchedules}" var="crfSchedule"
                                       varStatus="mystatus">
                                <tags:crfSchedule index="${status.index}-${mystatus.index}"
                                                  inputName="studyParticipantAssignment.studyParticipantCrfs[${status.index}].studyParticipantCrfSchedules[${mystatus.index}]"
                                                  title="Crf Schedule" displayError="false"
                                                  startDate="${crfSchedule.startDate}"
                                                  dueDate="${crfSchedule.dueDate}"
                                                  status="${crfSchedule.status}"></tags:crfSchedule>
                            </c:forEach>
                            <tr id="hiddenDiv-${status.index}"></tr>
                        </table>
                    </div>
                </chrome:division>
            </c:forEach>
</jsp:attribute>
</tags:tabForm>
</body>
</html>