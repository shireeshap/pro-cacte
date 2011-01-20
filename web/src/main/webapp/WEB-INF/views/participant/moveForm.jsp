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
    <tr>
        <td style="padding-left:20px">
            <c:choose>
                <c:when test="${fn:length(crfs) > 1}">
                    Multiple forms have events scheduled for <b>${participant.displayName}</b> on <b>${olddate}</b>.
                    Please select the forms for which you want to move the scheduled event(s)<br>
                    <c:forEach items="${crfs}" var="crf">
                        <input type="checkbox" name="selectedForms"
                               value="${crf.id}">${crf.title}
                        <br/>
                    </c:forEach>
                    <br/>
                </c:when>
                <c:otherwise>
                    You are about to move the scheduled event(s) on <b>${olddate}</b> for
                    <b>${participant.displayName}</b> on form: <b>${crfs[0].title}</b>
                    <input type="hidden" name="selectedForms" value="${crfs[0].id}"/>
                    <br/>
                    <br/>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td style="padding-left:20px">
            <div class="row validate-NOTEMPTY&&DATE" id="effectiveStartDate-row">
                <div class="label" style="width:21em;font-weight:normal;">
                    <span class="required-indicator">*</span>&nbsp;
                    Please provide a new date for this event&nbsp;
                </div>
                <div class="value">
                    <input id="effectiveStartDate" class="date validate-NOTEMPTY&&DATE"
                           name="effectiveStartDate"
                           title="Please provide a new date for this form"
                           value="${newdate}" size="20" enabled=""
                           type="text">
                    <a href="#" id="effectiveStartDate-calbutton">
                        <img src="/proctcae/images/chrome/b-calendar.gif" alt="Calendar" width="17"
                             align="absmiddle" border="0"
                             height="16">
                    </a>
                    <i>(mm/dd/yyyy)</i>
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <td style="padding-left:20px">Would you like to move only this event, all events, or this and all
            following
            events?<br></td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td style="padding-left:20px">
            <input type="button" value="Only this event"
                   onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${day}','add,del' )"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="This and all following events"
                   onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${day}','moveallfuture')"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="All events"
                   onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${day}','moveall' )"/>
            &nbsp;&nbsp;&nbsp;
            <input type="button" value="Cancel"
                   onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${day}','cancel')"/>
        </td>
    </tr>
</table>
<%--<div id="releaseForm">--%>
<%--<table width="100%">--%>
<%--<tr>--%>
<%--<td align="center">--%>

<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td align="center"><b>Would you like to move only this form, all forms, or this and all following forms?<br><br></b>--%>
<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
<%--<td align="center">--%>
<%--<input type="button" value="Only this instance"--%>
<%--onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','add,del')"/>--%>
<%--&nbsp;&nbsp;&nbsp;--%>
<%--<input type="button" value="All events"--%>
<%--onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','moveall')"/>--%>
<%--&nbsp;&nbsp;&nbsp;--%>
<%--<input type="button" value="All following"--%>
<%--onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','moveallfuture')"/>--%>
<%--&nbsp;&nbsp;&nbsp;--%>
<%--<input type="button" value="Cancel"--%>
<%--onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','cancel')"/>--%>
<%--</td>--%>
<%--</tr>--%>
<%--</table>--%>
<%--</div>--%>
