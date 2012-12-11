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
<body>

    
    <chrome:box title="participant.label.on_hold_date">
        <chrome:division>
        
            <div id="offTreatment" style="margin-left:12px">
                <div style="text-align: left; margin-left: 0.5em;">
                    <div><spring:message code='participant.onHold'/></div>
                    <div><spring:message code='participant.onHold2'/></div>
                </div>
                 <div class="value" style="margin-left:12px" > <br/>
                    <input id="effectiveStartDate" class="date validate-NOTEMPTY&&DATE"
                           name="effectiveStartDate"
                           title="On hold date"
                           value="${newdate}" size="20" enabled=""
                           type="text">
                    <a href="#" id="effectiveStartDate-calbutton">
                        <img src="/proctcae/images/chrome/b-calendar.gif" alt="Calendar" width="17"
                             align="absmiddle" border="0"
                             height="16">
                    </a>
                    <i>(mm/dd/yyyy)</i>
                </div>

            </div>
                   
            <div class="flow-buttons">
                <table width="100%" border="0">
                      <tr>
                          <td align="left">
                            <spring:message code="participant.button.cancel" var="cancel"/>
                              <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="${cancel}" icon="x" color="red" size="large"
                             onclick="closeWindow()"/>
                              </td>
                          <td align="right">
                              <tags:button type="submit" id="flow-update" cssClass="next" value="Begin hold" color="blue" size="large"
                               onclick="parent.CP.beginHoldOnSchedules('${index}',$('effectiveStartDate').value,'onhold','${pid}')"
                               icon="check" overRideStyle=" position:relative; top:0px;"/>
                              <%--<input type="button" value="Begin hold"--%>
                   <%--onclick="parent.beginHoldOnSchedules('${index}',$('effectiveStartDate').value,'onhold','${pid}')"/>--%>
                          </td>
                        </tr>
                    </table>


            </div>


        </chrome:division>
    </chrome:box>

</body>