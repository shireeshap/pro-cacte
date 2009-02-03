<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<body>

<form:form method="post">
    <chrome:box title="form.label.release_form">
        <chrome:division>
            <c:choose>
                <c:when test="${command.released}">
                    <div id="errorMessages">
                        <p/>

                        <div id="errors">
                            The Form has already been released :
                        </div>
                        <p>
                            Please
                            <a href="<c:url value="/pages/form/manageForm?studyId=${command.study.id}"/>">return
                                to
                                the form</a>.
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div id="releaseForm">
                        <p>
                            You are about to release

                            <strong>${command.title}</strong>.

                            You will no longer be able to edit it.
                            <br/>
                            Please provide the date on which this form will be made effective.
                            <tags:renderDate propertyName="effectiveStartDate"
                                             displayName="form.label.effective_start_date" required="true"/>
                        </p>

                        <p>
                            You cannot reverse this step. If you're not ready to proceed, please
                            <a href="javascript:closeWindow()">click here</a>.
                        </p>

                    </div>
                    <br>

                    <div class="flow-buttons">

                        <input type="submit" id="flow-update"
                               class="next" value="Release" alt="Save"
                                />


                        <input type="button" id="flow-cancel"
                               class="previous ibutton" value="Cancel" alt="Cancel"
                               onclick="closeWindow()"/>
                    </div>
                </c:otherwise>
            </c:choose>


        </chrome:division>
    </chrome:box>
</form:form>
</body>