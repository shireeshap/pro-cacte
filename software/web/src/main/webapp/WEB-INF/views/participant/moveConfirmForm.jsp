<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${fn:length(issueForms) > 0}">
<table width="100%">
    <tr>
        <td style="padding-left:20px">
                    <spring:message code="participant.reschedule.pastdue"></spring:message>   <br/>   <br/>
                    <c:forEach items="${issueForms}" var="crf">
                       <b> ${crf} </b>
                        <br/>
                    </c:forEach>
                    <br/>

             <div id="div_hidden_checkboxes" style="display:none;">
            <c:forEach items="${selectedForms}" var="crf">
               <%--<input type="hidden" name="selectedForms" value="${crf}"/>  --%>
                <input type="checkbox" name="selectedForms" value="${crf}" checked>
            </c:forEach>
                 </div>
        </td>
    </tr>

    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr align="center">
        <td style="padding-left:20px">
            <input type="button" value="Cancel"
                   onclick="parent.addRemoveSchedule('${index}','${date}','cancel')"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="Confirm"
                   onclick="parent.addRemoveSchedule('${index}','${date}','${action}' )"/>

        </td>
    </tr>
</table>
</c:if>
