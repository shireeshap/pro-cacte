<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<chrome:box title="participant.reportmodehist.label.header">
    <div align="left" style="margin-left: 10px">
        <table class="tablecontent" width="100%">
            <tr>
                <th width="30%">
                    <tags:message code="participant.reportmodehist.label.mode"/>
                </th>
                <th width="30%">
                    <tags:message code="participant.reportmodehist.label.startdate"/>
                </th>
                <th width="30%">
                    <tags:message code="participant.reportmodehist.label.enddate"/>
                </th>
            </tr>
            <c:forEach items="${homeHistItems}" var="histItem">
                <tr>
                    <td>                            
                        ${histItem.mode.displayName}
                    </td>
                     <td>
                            <tags:formatDate value="${histItem.effectiveStartDate}"/>

                    </td>
                    <td>
                            <tags:formatDate value="${histItem.effectiveEndDate}"/>

                    </td>
                </tr>
            </c:forEach>
             <c:forEach items="${clinicHistItems}" var="histItem">
                <tr>
                    <td>
                        ${histItem.mode.displayName}
                    </td>
                     <td>
                             <tags:formatDate value="${histItem.effectiveStartDate}"/>

                    </td>
                    <td>
                            <tags:formatDate value="${histItem.effectiveEndDate}"/>

                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <br/><br/>
    <div style="float:right; padding-right:10px">
        <tags:button color="blue" type="button" id="flow-cancel"
                     cssClass="previous ibutton" value="Close" icon="x"
                     onclick="closeWindow()" size="small"/>
    </div>
    <br/>
    <br/>
</chrome:box>    