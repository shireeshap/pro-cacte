<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<table width="100%">
     <c:if test="${fn:length(resultMap['successForms']) > 0}">
    <tr>
    <td style="padding-left:20px">
    <spring:message code="participant.reschedule.sucess"></spring:message> <br/>
    <c:forEach items="${resultMap['successForms']}" var="crf">
       <br/> <b> ${crf} </b>

    </c:forEach>
    <br/>
    </td>
    </tr>
    </c:if>
    <tr>
    <td>&nbsp;</td>
    </tr>
    <c:if test="${fn:length(resultMap['failedForms']) > 0}">
    <tr>
    <td style="padding-left:20px">
    <spring:message code="participant.reschedule.failure.forms"></spring:message> <br/>
    <c:forEach items="${resultMap['failedForms']}" var="crf">
        <br/> <b> ${crf} </b>

    </c:forEach>
    <br/>
    </td>
    </tr>
    </c:if>
    <tr>
    <td>&nbsp;</td>
    </tr>
    <tr align="center">
    <td style="padding-left:20px">
    <input type="button" value="Ok" onclick="parent.addRemoveSchedule('${index}','${day}','cancel')"/>

    </td>
    </tr>
    </table>

