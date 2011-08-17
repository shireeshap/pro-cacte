<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table>
    <tr>
        <td>
            <form:form method="post">
            <div id="releaseForm">
                <div style="border:0px solid #ccc; height:65px; padding:9px; margin-bottom:10px;">
                    <c:choose>
                        <c:when test="${command.status eq 'ACTIVE'}">
                            Please provide the date on which this Clinical staff will be effectively deactivated.
                        </c:when>
                        <c:otherwise>
                            Please provide the date on which this Clinical staff will be effectively activated.
                        </c:otherwise>
                    </c:choose>
                    <tags:renderDate propertyName="effectiveDate"
                                     displayName="clinicalStaff.label.effective_date" required="true"/>

                </div>
                <br>

                <div class="flow-buttons">
                    <c:choose>
                        <c:when test="${command.status eq 'ACTIVE'}">
                            <tags:button color="orange" type="submit" id="flow-update"
                                         cssClass="next" value="De-activate" icon="check"
                                    />
                        </c:when>
                        <c:otherwise>
                            <tags:button color="orange" type="submit" id="flow-update"
                                         cssClass="next" value="Activate" icon="check"
                                    />
                        </c:otherwise>
                    </c:choose>
                    <tags:button color="blue" type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="Cancel" icon="x"
                                 onclick="closeWindow()"/>
                </div>
                </form:form>
        </td>
    </tr>
</table>