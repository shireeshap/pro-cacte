<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <script type="text/javascript">
        function participant(){
            jQuery('#staff').hide();
            jQuery('#staffInstructions').hide();
            jQuery('#participantInstructions').show();
            jQuery('#participantForm').show();

        }

        function staff(){
           jQuery('#participantInstructions').hide();
           jQuery('#participantForm').hide();
           jQuery('#staff').show(); 
           jQuery('#staffInstructions').show();
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
          <div id="staffInstructions">
            <div style="margin-left:0.1em" >
                Please enter your last name and email address below.
                We will send the username to your registered email address. If Participant
                <a href="javascript:participant();"> Click here</a>
                <br/>
            </div>
          </div>
          <div id="participantInstructions" style="display:none">
            <div style="margin-left:0.1em">
                Please enter your last name and email address below.
                We will send the username to your registered email address. If Staff
                <a href="javascript:staff();"> Click here</a>
                <br/>
            </div>
          </div>  
            <form method="POST" action="forgotusername">
                <table id="staff">
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
                    <tr>

                        <td style="text-align:right"><b>Participant study identifier</b></td>
                        <td><input type="text" name="participantIdentifier" value=""/></td>                     
                    </tr>
                    <tr>
                        <td  style="text-align:right"><b>Email</b></td>
                        <td><input type="text" name="participantEmail" value=""/></td>
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
