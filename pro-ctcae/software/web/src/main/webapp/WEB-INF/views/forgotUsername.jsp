<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <script type="text/javascript">
        function changeAccordingToRole(){
//            if(index ==10)
//                var selectedFilterOption = $('filterOptions').value;
//            else if(index ==20)
                var selectedFilterOption = $('filterOptions').value;
            switch(selectedFilterOption)
            {
                case 'Clinical/Research staff member':
                                    jQuery('#participantForm').hide();
//                                    var filterValue =$('filterOptionsParticipant').value;
//                                    if(filterValue != 'Please select')
//                                    $('filterOptions').value= $('filterOptionsParticipant').value;
//                                    jQuery('#filterOptions').show();
                                    jQuery('#staff').show();
                                    jQuery('#participantInstructions').hide();
                                    jQuery('#studyInstructions').show();
                                    break;
                case 'Patient':
                                    jQuery('#staff').hide();
//                                    $('filterOptionsParticipant').value=$('filterOptions').value;
//                                    jQuery('#filterOptionsParticipant').show();
                                    jQuery('#participantForm').show();
                                    jQuery('#studyInstructions').hide();
                                    jQuery('#participantInstructions').show();
                                    break;
                case 'Please select':
                                     jQuery('#staff').hide();
                                     jQuery('#participantForm').hide();
            }
        }

    </script>
</head>
<body>
<chrome:box title="Forgot Username" autopad="true">
    <c:choose>
        <c:when test="${showConfirmation}">
            Your username has been emailed to you. You can <a href='<c:url value="login"/>'>login</a> to the system or
            <a href='<c:url value="password"/>'>reset</a> the password using that username.
        </c:when>
        <c:otherwise>
            <div id="studyInstructions">
                <div style="margin-left:0.1em" >
                    Please enter your last name and email address below.
                    We will send the username to your registered email address.   
                <br/>
                </div>
            </div>
             <div id="participantInstructions" style="display:none">
                <div style="margin-left:0.1em" >
                    Please enter your email address below.
                    We will send the username to your registered email address.
                    If you have trouble remembering your email, contact your study coordinator.
                <br/>
                </div>
            </div>
            <table>
                <tr>
                    <td style="text-align:right" width="62px">
                        <c:if test="${mode eq 'Y'}"><b>Role</b>
                    </td>
                    <td>
                       <select id="filterOptions" onchange="changeAccordingToRole();">
                                    <option selected="true">Please select</option>
                                    <option>Clinical/Research staff member</option>
                                    <option>Patient</option>
                            </select>
                    </td>
                       </c:if>
                </tr>
            </table>
            <form method="POST" action="forgotusername">
                <c:if test="${mode eq 'N'}">
                    <table id="non-deIdentifiedMode">
                        <tr>
                            <td style="text-align:right"><b>Last name</b></td>
                            <td><input type="text" name="lastName" value=""/></td>
                        </tr>
                        <tr>
                            <td style="text-align:right"><b>Email</b></td>
                            <td><input type="text" name="email" value=""/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>&nbsp;&nbsp;&nbsp;<tags:button type="submit" value="Submit" color="green"/></td>
                        </tr>
                    </table>
                </c:if>
                <table id="staff" style="display:none">
                    <%--<tr>--%>
                        <%--<td style="text-align:right">--%>
                             <%--<c:if test="${mode eq 'Y'}"><b>Role</b>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<select id="filterOptions" onchange="changeAccordingToRole(10);">--%>
                                    <%--<option selected="true">Please select</option>--%>
                                    <%--<option>Clinical/Research staff member</option>--%>
                                    <%--<option>Patient</option>--%>
                            <%--</select>--%>
                        <%--</td>--%>
                             <%--</c:if>--%>
                    <%--</tr>--%>
                    <tr>
                        <td style="text-align:right"><b>Last name</b></td>
                        <td><input type="text" name="lastName" value=""/></td>
                    </tr>
                    <tr>
                        <td  style="text-align:right"><b>Email</b></td>
                        <td><input type="text" name="email" value=""/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>&nbsp;&nbsp;&nbsp;<tags:button type="submit" value="Submit" color="green"/></td>
                    </tr>
                </table>
                <table id="participantForm" style="display:none">
                      <%--<tr>--%>
                          <%--<td style="text-align:right">--%>
                             <%--<c:if test="${mode eq 'Y'}"> <b>Role</b>--%>
                          <%--</td>--%>
                          <%--<td>--%>
                              <%--<select id="filterOptionsParticipant" onchange="changeAccordingToRole(20);">--%>
                                    <%--<option selected="true">Please select</option>--%>
                                    <%--<option>Clinical/Research staff member</option>--%>
                                    <%--<option>Patient</option>--%>
                              <%--</select>--%>
                          <%--</td>--%>
                              <%--</c:if>--%>
                      <%--</tr>--%>
                      <tr>
                          <td  style="text-align:right" width="62"><b>Email</b></td>
                          <td><input type="text" name="participantEmail" value="" size="37.9"/></td>
                      </tr>
                    <tr>
                        <td></td>
                        <td>&nbsp;&nbsp;&nbsp;<tags:button type="submit" value="Submit" color="green"/></td>
                    </tr>
                </table>
            </form>

            <c:if test="${not empty Message}">
                <b><font color="red"> <spring:message code="${Message}"/></font></b>
            </c:if>
        </c:otherwise>
    </c:choose>
</chrome:box>
</body>
</html>
