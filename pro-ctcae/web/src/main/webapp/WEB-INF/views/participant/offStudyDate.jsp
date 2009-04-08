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
    <chrome:box title="participant.label.off_study_date">
        <chrome:division>

                    <div id="offTreatment">
                        <p>
                            Participant <strong>${command.participant.displayName}</strong> will be taken off treatment
                            from the specified date.
                            <br/>
                            Please provide the date on which this action will be made effective.
                            <tags:renderDate propertyName="offTreatmentDate"
                                             displayName="participant.label.off_study_date" required="true"/>
                        </p>

                        <p>
                            You cannot reverse this step. If you're not ready to proceed, please
                            <a href="javascript:closeWindow()">click here</a>.
                        </p>

                    </div>
                    <br>

                    <div class="flow-buttons">

                        <input type="submit" id="flow-update"
                               class="next" value="Assign date" alt="Save"
                                />


                        <input type="button" id="flow-cancel"
                               class="previous ibutton" value="Cancel" alt="Cancel"
                               onclick="closeWindow()"/>
                    </div>
               

        </chrome:division>
    </chrome:box>
</form:form>
</body>