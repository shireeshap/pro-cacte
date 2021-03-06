<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="gov.nih.nci.ctcae.core.domain.Alert"%>
<%@page import="java.util.ArrayList"%>
<%@page import="gov.nih.nci.ctcae.web.ControllersUtils" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>



<%--
    boolean isMobile = ControllersUtils.isRequestComingFromMobile(request);
    if (isMobile) {
        response.sendRedirect("../mobile/login");
    }
--%>
<html>
<head>
    
    <style type="text/css">
		.solid-box {
			border: 4px solid #c03;
		    color: #c03;
		    font-size: 11pt;
		    margin: 0px 120px 12px 120px;
		    padding: 0 0 0.2em 1em;
		}
		
		.alert-heading {
			float: left;
		    font-weight: bold;
		    margin-left: 0;
		    text-align: left;
		    width: 10em;
		}
		
		.alert-message {
			font-weight: normal;
   			margin-left: 4em;
   			text-align: center;
		}
		
        .box {
            width: 35em;
            margin: 0 auto;
        }

        .submit {
            float: right; /*margin-top: 1em;*/
        }

        .forgot {
            float: left;
            margin-top: 1em;
        }

        * {
            zoom: 1;
        }
    </style>
    <!--[if IE]>
        <style>
            div.row div.value {
                margin-left:7px;
            }
        </style>
    <![endif]-->
    <script type="text/javascript">
    	function pwdFocus() {
            document.getElementById("fakepassword").style.display='none';
            document.getElementById("password").style.display='block';
           $('password').focus();
            //attachKeyBoard($('password'));
        }

        function pwdBlur() {
            if ($('password').value == '') {
                $('password').hide();
                $('fakepassword').show();
            }
        }

    	function checkSubmit(e)
    	{
    	   if(e && e.keyCode == 13) {
    	      document.loginForm.submit();
    	   }
    	}
    	
    	jQuery(document).ready(function() {
	    	jQuery("#password").focus(function() {
	    		jQuery("#password\\.error").hide();
	    	});
    	});
    	
    </script>
</head>

<body>
<spring:message code="login.title" var="loginTitle" />
    
        <div id="wrapper">
        <form method="POST" name="loginForm" id="loginForm" action="<c:url value="/pages/j_spring_security_check"/>">
        
        	<input type="hidden" id="lang" name="lang" value="${param.lang}" /> 
		    <c:set var="showLogin" value="true"/>
		    <c:if test="${not empty param.error}">
		        <c:choose>
		            <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'Bad credentials'}">
		                <p class="errors"><spring:message code="user.incorrect.username.password" /></p>
		            </c:when>
		            <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'User account is locked'}">
		                <p class="errors">
		                    <jsp:forward page="accountLocked.jsp"/>
		                </p>
		                <c:set var="showLogin" value="false"/>
		            </c:when>
		            <c:when test="${fn:contains(SPRING_SECURITY_LAST_EXCEPTION.message,'Password expired')}">
		                <p class="errors">
		                    <jsp:forward page="forwardToPasswordExpire.jsp"/>
		                </p>
		            </c:when>
		            <%--<c:when test="${fn:contains(SPRING_SECURITY_LAST_EXCEPTION.message, 'Use of web not activated')}">--%>
		                <%--<p class="errors">Use of web is not allowed.</p>--%>
		            <%--</c:when>--%>
		            <c:when test="${fn:contains(SPRING_SECURITY_LAST_EXCEPTION.message,'User account is currently locked')}">
		                <p class="errors">
		                    <p class="errors">"${fn:substring(SPRING_SECURITY_LAST_EXCEPTION.message,0,79)}"</p>
		                </p>
		            </c:when>
		            <c:otherwise>
		                <%--${SPRING_SECURITY_LAST_EXCEPTION.message}--%>
		                <p class="errors">User is inactive.</p>
		            </c:otherwise>
		        </c:choose>
		    </c:if>
		    <div id="upCommingAlertsDiv">
		    	<c:if test="${not empty alerts}">
			    	<div class="solid-box">
					    	<c:forEach items="${alerts}" var="alert">
					    		<div class="row" align="middle">${alert.alertMessage}</div>
					    	</c:forEach>
			    	</div>
		    	</c:if>
		    </div>
		    
		    <c:if test="${showLogin}">
	           <div class="content-box">     
                   <div class="box-container">
                       <div class="container"  id="login" onKeyPress="return checkSubmit(event)">
                            <p class="inputs">
                                <spring:message code="login.username" var="uname" />
                                <input class="username" id="username" name="j_username" size="30" type="text" value="${uname}" onblur="javascript:if(this.value=='') this.value='${uname}';" onfocus="javascript:if(this.value=='${uname}') this.value='';" onclick="attachKeyBoard($('username'));"/>
                            </p>
                            <p class="inputs">
                                <spring:message code="login.password" var="pwd" />
                                <input style="display: block" class="password" id="fakepassword" name="j_fakepassword" size="30" type="text" value="${pwd}" onfocus="pwdFocus();" autocomplete="off" />
                                <input style="display: none" class="password" id="password" name="j_password" size="30" type="password" value="" onclick="attachKeyBoard($('password'));" onblur="pwdBlur(); isSpclCharForPassword('password')" autocomplete="off" />
                                <div id="password.error" style="display:none;" class="errors">
                                	<spring:message code='password.special.character.message' text='password.special.character.message'/>
                                </div>
                            </p><br />
                            <div>
                                <p class="submit">
                                    <spring:message code="login.submit" var="loginSubmit" />
                                    <a href="javascript:document.loginForm.submit();" class="btn big-green" ><span>${loginSubmit}</span></a>
                                </p>
                           </div>
                           <div>
                               <a href='<c:url value="forgotusername"/>' border-bottom="none"><tags:message code="login.forgotUsername"/></a> &nbsp; &nbsp;
                               <a href='<c:url value="password"/>'><tags:message code="login.forgotPassword"/></a>
                           </div>
                            <div class="clear"></div><br/>
                           <div class="content-box1">
                               <div class="box-container1">
                                   <div class="container1">
                                    <p align="center"><b><tags:message code="participant.disclaimer"/>:</b></p>
                                    <p align="center">
                                        <tags:message code="login.disclaimer.1"/>
                                    </p>
                                   </div>
                               </div>
                          </div>
                         </div>
                     </div>
	             </div>
   		   </c:if>
   		   <div id="keyboardDiv" align="center"></div>
   		    
   		   <div class="content-box">     
	           <div class="box-container">
	           <div class="container">
			    <p align="center"><b><tags:message code="login.warning.label"/></b></p>
			    <p align="center">
		        	<tags:message code="login.warning.message.1"/>
		        	<tags:message code="login.warning.message.2"/>
		        	<tags:message code="login.warning.message.3"/>
			    </p>
		  	   </div>
		  	   </div>
		  </div>
    	</form>   
   		</div> 

</body>
</html>



