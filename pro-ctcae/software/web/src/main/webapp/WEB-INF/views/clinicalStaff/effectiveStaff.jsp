<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<style>
	.message_style{
		border:0px solid #ccc; 
		height:65px; 
		padding:9px; 
		margin-bottom:10px; 
		width: 475px;
	}
</style>
<table>
    <tr>
        <td>
            <ctcae:form method="post">
           	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />

            <div id="releaseForm">
                <div class="message_style">
                    <c:choose>
                        <c:when test="${command.status.displayName eq 'Active'}">
                           By clicking "De-activate", this user will no longer be able to login to the system, effective immediately. 
            			   The system will automatically inactivate the user on any studies to which they are assigned. This user can be re-activated in the future if needed.
                        </c:when>
                        <c:otherwise>
                        	By clicking "Activate", this user will be authorized to login to the system, effective immediately.
                        </c:otherwise>
                    </c:choose>
                </div>
                <br>

                <div class="flow-buttons">
                    <c:choose>
                        <c:when test="${command.status.displayName eq 'Active'}">
                            <tags:button color="orange" type="submit" id="flow-update"
                                         cssClass="next" value="De-activate" icon="check"
                                    overRideStyle="position:relative; top: 0px;"/>
                        </c:when>
                        <c:otherwise>
                            <tags:button color="orange" type="submit" id="flow-update"
                                         cssClass="next" value="Activate" icon="check"
                                    overRideStyle="position:relative; top: 0px;"/>
                        </c:otherwise>
                    </c:choose>
                    <tags:button color="blue" type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="Cancel" icon="x"
                                 onclick="closeWindow()"/>
                </div>
                </ctcae:form>
        </td>
    </tr>
</table>
