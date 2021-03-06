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

<table>
    <tr>
        <td>
            <ctcae:form method="post">
            	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
            
                <c:choose>
                    <c:when test="${command.released}">
                        <div id="errorMessages">
                            <p/>

                            <div id="errors">
                                The Form has already been released :
                            </div>
                            <p>
                                Please
                                <a href="<c:url value="/pages/form/manageForm?studyId=${command.study.id}"/>">return to the form</a>.
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="releaseForm">
                            <div style="border:1px solid #ccc; height:65px; padding:9px; margin-bottom:10px;">
                                <img src="<chrome:imageUrl name="../blue/stop_sign_small.png" />" alt="Stop!"
                                     style="float:left; margin-right:15px; margin-left:50px;"/>
                                <div style="font-size:20px; margin-bottom:5px;">Are you sure you are ready to
                                    release this form?
                                </div>
                            </div>
                            <div>Releasing a form is irreversible, you will no longer be able to edit this form.<br>
                                However you can create a new version in the future. 
                            </div>
                            <br/>

                            <div>
                                You are about to release <strong>${command.title}</strong>. You will no longer be able to edit it. 
                                Please note that, depending on the number of participants associated with this form, the system may take
                                up to a minute to complete this action. 
                            </div>
                            <br/>
                            Please provide the date on which this form will be made effective.
                            <tags:renderDate propertyName="effectiveStartDate"
                                             displayName="form.label.effective_start_date" required="true"/>

                        </div>
                        <div class="flow-buttons">
                            <tags:button color="orange" type="submit" id="flow-update"
                                         cssClass="next" value="Release" icon="check" overRideStyle=" position:relative; top:0px;" />
                                    
                            <tags:button color="blue" type="button" id="flow-cancel"
                                         cssClass="previous ibutton" value="Cancel" icon="x"
                                         onclick="closeWindow();"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </ctcae:form>
        </td>
    </tr>
</table>
