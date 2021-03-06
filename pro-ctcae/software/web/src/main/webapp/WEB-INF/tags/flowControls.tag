<%@ attribute name="coninueAction" %>
<%@ attribute name="backAction" %>
<%@ attribute name="saveAction" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- must specify either tab & flow or tabNumber and isLast --%>
<%@attribute name="willSave" %>
<%@attribute name="showBack" %>
<%@attribute name="showContinue" %>
<%@attribute name="saveButtonLabel" required="false" %>
<%@attribute name="localButtons" fragment="true" %>
<div class="content buttons autoclear">
    <div class="local-buttons">
        <jsp:invoke fragment="localButtons"/>
    </div>
    <div class="flow-buttons">
        <c:if test="${showBack}">
        <span class="prev">

                <%--<input type="submit" id="flow-prev" class="tab${tabNumber - 1}"--%>
                       <%--value="Back"/>--%>
            <%----%>
            <div id="prev">

                <a href="${flowExecutionUrl}&_eventId=${backAction}">
                    <input type="image" src="<c:url value="/images/blue/back_btn.png"/>"
                           id="flow-next"/>
                </a>

            </div>

        </span>
        </c:if>
        <span class="next">

            <c:if test="${willSave}">
                <div id="submit">
                    <input type="hidden" name="execution" value="${flowExecutionKey}">
                    <input type="hidden" name="_eventId" value="${saveAction}">
                    <input type="submit" id="saveSubmit" value="${saveButtonLabel}"
                           src="<c:url value="/images/blue/save_btn.png"/>"/>
                </div>


            </c:if>

            <c:if test="${showContinue}">
                <%--<c:set var="saveText" value="${not empty saveButtonLabel ? saveButtonLabel : 'Save'}"/>--%>
                <%--<c:set var="continueLabel" value="${willSave ? saveText : ''}"/>--%>
                <%--<c:if test="${not empty continueLabel }">--%>
                <%--<c:set var="continueLabel" value="${continueLabel} &amp; "/>--%>
                <%--</c:if>--%>
                <%--<c:if test="${not isLast}">--%>
                <%--<c:set var="continueLabel" value="${continueLabel}Continue"/>--%>
                <%--</c:if>--%>

                <!--<input type="submit" id="flow-next" value="${continueLabel} &raquo;"/>-->


                <div id="submit">
                    <input type="hidden" name="execution" value="${flowExecutionKey}">
                    <input type="hidden" name="_eventId" value="${coninueAction}">
                    <input type="submit" id="continueSubmit" value="Save & Continue"
                           src="<c:url value="/images/blue/saveandcontinue_btn.png"/>"/>
                </div>
                <%--<input type="image" src="/images/blue/saveandcontinue_btn.png" id="flow-next" value="${continueLabel} &raquo;" alt="${continueLabel} &raquo;"/>--%>


            </c:if>
        </span>
    </div>
</div>

