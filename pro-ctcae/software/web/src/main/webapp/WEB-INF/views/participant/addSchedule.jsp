<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<table width="100%">
    <tr>
        <td style="padding-left:20px">
            <c:choose>
                <c:when test="${fn:length(crfs) > 1}">
                    Multiple forms have been assigned to <b>${participant.displayName}.</b>
                    Please select the forms for which you want to schedule an event. <br /><br />
                    <p style="padding-left:30px">
	                    <c:forEach items="${crfs}" var="crf">
	                        <input type="checkbox" name="selectedForms" <c:if test="${crf.value}">disabled="true"</c:if>
	                               value="${crf.key.id}">&nbsp;${crf.key.title}
	                        <br/>
	                    </c:forEach>
                    </p>
                    <br/>
                </c:when>
                <c:when test="${firstCrf ne null}">
                    You are about to add a new event for ${participant.displayName} on form: ${firstCrf.title}
                    <input type="hidden" name="selectedForms" value="${firstCrf.id}"/>
                    <br/>
                    <br/>
                </c:when>
                <c:otherwise>
                    There are no forms available on this study at this time.
                    <div style="float:right; padding-right:10px">
                        <br/><br/>
                        <tags:button color="blue" type="button" id="flow-cancel"
                                     cssClass="previous ibutton" value="Close" icon="x"
                                     onclick="closeWindow()" size="small"/>  <br/>
                    </div>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <c:if test="${firstCrf ne null}">
        <tr>
            <td style="padding-left:20px">Would you like to add a new event on ${date}?<br></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td style="padding-left:20px">
                <input type="button" value="Yes"
                       onclick="parent.addRemoveSchedule('${index}','${day}','add', '${pid}' )"/>
                &nbsp;&nbsp;&nbsp;
                <input type="button" value="No"
                       onclick="parent.addRemoveSchedule('${index}','${day}','cancel', '${pid}')"/>
            </td>
        </tr>
    </c:if>

</table>