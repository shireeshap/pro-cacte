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
            <div id="alertAdd">

                <div style="border:0px solid #ccc; height:95px; padding:9px; margin-bottom:10px;">
                    <%--<img src="<chrome:imageUrl name="../blue/stop_sign_small.png" />" alt="Stop!"--%>
                         <%--style="float:left; margin-right:15px; margin-left:50px;"/>--%>

                    <div style="font-size:15px; margin-bottom:10px;">
                        <spring:message code="participant.add_alert"/>
                    </div>


                </div>

                <div class="flow-buttons">
                    <spring:message code="participant.button_okay" var="yesButton"/>
                    <spring:message code="participant.button_no" var="noButton"/>
                    <tags:button color="orange" type="button" id="flow-update"
                                 onclick="closeWindow()"
                                 cssClass="next" value="${yesButton}" icon="check"/>

                </div>
          </div>
        </td>
    </tr>
</table>