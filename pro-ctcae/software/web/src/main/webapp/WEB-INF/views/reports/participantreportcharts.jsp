<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<chrome:box title="Report">

    <tags:instructions code="participant.report.graph.instructions"/>
    <c:if test="${fn:length(allAttributes)>1}">
        &nbsp;&nbsp;<b>Display:</b>&nbsp;<c:forEach items="${allAttributes}" var="attribute">
        <input type="checkbox"
               <c:if test="${fn:contains(selectedAttributes,attribute)}">checked="true"</c:if> name="attribute"
               value="${attribute}"
               onclick="updateChart(this,'${symptom}');">${attribute}&nbsp;&nbsp;
    </c:forEach>
        <br/>
        <br/>
    </c:if>

    <%--<chrome:division title="Participant reported responses for ${symptom} symptom (Worst responses)"/>--%>
    <br/>

    <div align="center">
        <img src="../../servlet/DisplayChart?filename=${participantReportChartFileName}" border=0/>
    </div>
    <div style="float:right; padding-right:10px">
        <tags:button color="blue" type="button" id="flow-cancel"
                     cssClass="previous ibutton" value="Close" icon="x"
                     onclick="closeWindow()" size="small"/>
    </div>
    <br/>
    <br/>
</chrome:box>
