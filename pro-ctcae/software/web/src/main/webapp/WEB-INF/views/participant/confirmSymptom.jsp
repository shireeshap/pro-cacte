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
            <div id="confirmSymptom">
                <c:if test="${isMapped eq false}">
                <div style="border:1px solid #ccc; height:95px; padding:9px; margin-bottom:10px;">
                    <img src="<chrome:imageUrl name="../blue/stop_sign_small.png" />" alt="Stop!"
                         style="float:left; margin-right:15px; margin-left:50px;"/>

                    <div style="font-size:15px; margin-bottom:10px;">
                        <spring:message code="participant.confirm_symptom"/> <b>${values}</b>. <spring:message code="participant.confirm_symptom1"/> <b>${selectedChoice}</b>?
                    </div>


                </div>
                <br/>
                <br/>

                <div class="flow-buttons">
                    <spring:message code="participant.button_yes" var="yesButton"/>
                    <spring:message code="participant.button_no" var="noButton"/>
                    <tags:button color="orange" type="button" id="flow-update"
                                 onclick="closeWindow()"
                                 cssClass="next" value="${yesButton}" icon="check"/>
                    <%--<tags:button color="orange" type="button" id="flow-update"--%>
                                 <%--onclick="sendConfirmedSymptom();"--%>
                                 <%--cssClass="next" value="${yesButton}" icon="check"/>--%>
                    <%--<tags:button color="blue" type="button" id="flow-cancel"--%>
                                 <%--cssClass="previous ibutton" value="${noButton}" icon="x"--%>
                                 <%--onclick="closeWindow()"/>--%>
                </div>
                </c:if>
                <c:if test="${isMapped eq true}">
                   <div style="font-size:15px; margin-bottom:10px;">
                      <spring:message code="participant.confirm_symptom2"/>   <b>${mappedValues}</b>.
                    </div>

                    <div class="flow-buttons" align="right" >
                        <spring:message code="participant.button_yes" var="okayButton"/>
                    <tags:button color="orange" type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="${okayButton}" icon="check"
                                 onclick="closeWindow()"/>
                </div>
                </c:if>
                </div>
        </td>
    </tr>
</table>
