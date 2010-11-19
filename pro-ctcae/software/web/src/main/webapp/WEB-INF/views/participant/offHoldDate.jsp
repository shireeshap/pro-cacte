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
    <chrome:box title="participant.label.remove_hold_date">
        <chrome:division>

            <div id="offTreatment">
                <%--<div style="border:1px solid #f00; height:100px; padding:9px; margin-bottom:10px;">--%>
                    <%--<img src="<chrome:imageUrl name="../blue/stop_sign.png" />" alt="Stop!"--%>
                         <%--style="float:left; margin-right:30px; margin-left:55px;"/>--%>

                    <%--<div style="font-size:20px; margin-bottom:5px;">Are you sure you want to do this?</div>--%>

                </div>
                <p>
                    Participant <strong>${command.participant.displayName}</strong>
                    will be re-assigned to treatment and all the schedules will be reinstated. <br><br>
                    <input type="radio" name="recreate" value="recreate"> Recreate cycles  <br>
                    <input type="radio" name="recreate" value="move"> Continue cycle from where the treatment was put on hold 
                    <tags:renderDate propertyName="offHoldTreatmentDate"
                                     displayName="participant.label.remove_hold_date" required="true"/>

                </p>

            </div>

            <div class="flow-buttons">

                <tags:button type="submit" id="flow-update"
                             cssClass="next" value="Assign" icon="check" color="orange"/>


                <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="Cancel" icon="x" color="red"
                             onclick="closeWindow()"/>
            </div>


        </chrome:division>
    </chrome:box>
</form:form>
</body>