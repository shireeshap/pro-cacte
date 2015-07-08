<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>
</head>
<body>

	<table>
		<tr>
			<td>
				<ctcae:form method="post">
					<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
					
					<div id="toggleMessageDiv">
						<div class="message_style">
							<c:choose>
								<c:when test="${command.alert.alertStatus.displayName eq 'Active'}">
									By clicking "De-activate", this system alert would deactivated effective immediately. 
            			   			This system alert can be re-activated in the future if needed.								
								</c:when>
								<c:otherwise>
									By clicking "Activate", this system alert will be reactivated effective immediately.
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="flow-buttons">
	                    <c:choose>
	                        <c:when test="${command.alert.alertStatus.displayName eq 'Active'}">
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
</body>
</html>