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
            }   
        </script>
    </head>
    <body>

        <chrome:box title="participant.label.remove_hold_date">
            <chrome:division>
                <div id="offTreatment">
                    <tags:errors path="*"/>
                </div>
                <div style="margin-left:12px">
                    Surveys for the participant have been put on hold
                    beginning
                    <b><tags:formatDate value="${onHoldTreatmentDate}"/></b>.    <br>
                Click "Remove hold" to resume survey administration for this participant.
                      <div class="value">
                    <input id="offHoldTreatmentDate" class="date validate-NOTEMPTY&&DATE"
                           name="offHoldTreatmentDate"
                           title="Off hold date"
                           value="${newdate}" size="20" enabled=""
                           type="hidden">

                </div>

                </p>

                </div>
                  <br>
                <div class="flow-buttons">
                  <table width="100%" border="0">
                      <tr>
                          <td align="left">
                              <tags:button type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="Cancel" icon="x" color="red"  size="large"
                                 onclick="closeWindow()"/>
                          </td>
                          <td align="right">
                              <tags:button value="Remove hold" color="blue" id="flow-update" cssClass="next" size="large" icon="check"
                              onclick="parent.CP.participantOffHoldPost('${index}',$('offHoldTreatmentDate').value, 0, 0,'offhold')"
                              overRideStyle=" position:relative; top:0px;" />

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
