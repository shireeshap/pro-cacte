<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<chrome:box title="participant.reportmodehist.label.header">
<chrome:division title="study.label.home_reporting"/>
    <div align="left" style="margin-left: 100px">
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
        </table>
    </div>
        </br>
<chrome:division title="study.label.clinic_reporting"/>
    <div align="left" style="margin-left: 100px">
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
     </br>
</chrome:box>    