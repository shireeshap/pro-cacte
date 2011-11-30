<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<page:applyDecorator name="standardNoHeader">
    <html>
    <head>
        <script type="text/javascript">
            function closeWindow() {
                     window.parent.Windows.close(window.parent._winOffHold.getId());
//                var win = Windows.close();
            }   </script>
    </head>
    <body>
    <c:set var="url"><c:url value="/pages/participant/participantOffHold"/>?id=${param.id}&subview=x</c:set>
        <%--${pageContext.request.requestURL}--%>

    <ctcae:form method="post" action="${url}">
   		<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
    	
        <chrome:box title="participant.label.remove_hold_date">
            <chrome:division>
                <div id="offTreatment">
                    <tags:errors path="*"/>
                </div>
                <p align="left">
                    Surveys for the participant <i><b>"${command.participant.displayName}"</b></i> have been put on hold
                    beginning
                    <b><tags:formatDate value="${onHoldTreatmentDate}"/></b>, <c:if test="${cycle ne null && cycle ne 0}">which was <b>Cycle ${cycle}</b>, <b>Day ${day}</b>.</c:if><br> <br>
                    Specify the date on which surveys will resume.
                        <tags:renderDate propertyName="offHoldTreatmentDate"
                                         displayName="participant.label.remove_hold_date1" required="true"/>

                        <%--<c:if test="${cycleNumber ne 0}">--%>
                        <%--<i>For the selected date, the cycle number is ${cycleNumber} and day is ${dayNumber}</i> <br>--%>
                        <%--</c:if>--%>


                        <%--<input type="checkbox" name="recreate" value="cycle"--%>
                        <%--onclick="javascript:showHideCycleDay(this.checked);"> --%>
                    <p align="left">
                    Specify the cycle and day corresponding to the above selected off hold date. <br>
                     </p>
                <div id="cycle_day" style="display:block;width:258px" align="right">
                    <b> Cycle</b> &nbsp;<input name="cycle" type="text" size="2"> and <b> Day </b> &nbsp;<input
                        name="day"
                        type="text"
                        size="2">
                </div>


                </p>

                </div>

                <div class="flow-buttons">
                  <table width="100%" border="0">
                      <tr>
                          <td align="left">
                              <tags:button type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="Cancel" icon="x" color="red"
                                 onclick="closeWindow()"/>
                          </td>
                          <td align="right">
                                <tags:button type="submit" id="flow-update"
                                 cssClass="next" value="Remove Hold" icon="check" color="orange"/>
                          </td>
                      </tr>
                  </table>

                </div>


            </chrome:division>
        </chrome:box>
    </ctcae:form>
    </body>
    </html>
</page:applyDecorator>
