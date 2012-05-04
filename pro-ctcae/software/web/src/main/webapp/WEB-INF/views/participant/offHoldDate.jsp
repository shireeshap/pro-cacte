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

<%--<page:applyDecorator name="standardNoHeader">--%>
    <html>
    <head>
        <script type="text/javascript">
            function closeWindow() {
                     window.parent.Windows.close(window.parent._winOffHold.getId());
            }   </script>
    </head>
    <body>
    	
        <chrome:box title="participant.label.remove_hold_date">
            <chrome:division>
                <div id="offTreatment">
                    <tags:errors path="*"/>
                </div>
                <p align="left">
                    Surveys for the participant have been put on hold
                    beginning
                    <b><tags:formatDate value="${onHoldTreatmentDate}"/></b>, <c:if test="${cycle ne null && cycle ne 0}">which was <b>Cycle ${cycle}</b>, <b>Day ${day}</b>.</c:if><br> <br>
                    Specify the date on which surveys will resume.

                      <div class="value">
                    <input id="offHoldTreatmentDate" class="date validate-NOTEMPTY&&DATE"
                           name="offHoldTreatmentDate"
                           title="Off hold date"
                           value="${newdate}" size="20" enabled=""
                           type="text">
                    <a href="#" id="offHoldTreatmentDate-calbutton">
                        <img src="/proctcae/images/chrome/b-calendar.gif" alt="Calendar" width="17"
                             align="absmiddle" border="0"
                             height="16">
                    </a>
                    <i>(mm/dd/yyyy)</i>
                </div>
                    <p align="left">
                    Specify the cycle and day corresponding to the above selected off hold date. <br>
                     </p>
                <div id="cycle_day" style="display:block;width:258px" align="right">
                    <b> Cycle</b> &nbsp;<input name="cycle" id="cycle" type="text" size="2"> and <b> Day </b> &nbsp;<input
                        name="day"
                        type="text" id="day"
                        size="2">
                </div>


                </p>

                </div>

                <div class="flow-buttons">
                  <table width="100%" border="0">
                      <tr>
                          <td align="left">
                              <tags:button type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="Cancel" icon="x" color="red"  size="small"
                                 onclick="closeWindow()"/>
                          </td>
                          <td align="right">
                               <input type="button" value="Remove hold"
                   onclick="parent.participantOffHoldPost('${index}',$('offHoldTreatmentDate').value, $('cycle').value, $('day').value,'offhold')"/>

                          </td>
                      </tr>
                  </table>

                </div>


            </chrome:division>
        </chrome:box>
    <%--</ctcae:form>--%>
    </body>
    </html>
<%--</page:applyDecorator>--%>
