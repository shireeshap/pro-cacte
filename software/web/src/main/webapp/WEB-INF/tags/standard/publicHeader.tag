<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri='http://www.springframework.org/security/tags' %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<tags:javascriptLink name="ga"/>
<tags:includeVirtualKeyboard/>
<script type="text/javascript">
    try {
        var pageTracker = _gat._getTracker("UA-15798971-1");
        pageTracker._trackPageview();
    } catch(err) {
    }

    try {
        var pageTracker = _gat._getTracker("UA-26475546-1");
        pageTracker._trackPageview();
    } catch(err) {
    }
    try {
        var pageTracker = _gat._getTracker("UA-26475150-1");
        pageTracker._trackPageview();
    } catch(err) {
    }

    function openNewWindow(url) {
        popupWin = window.open(url,
                'open_window',
                'menubar, toolbar, location, directories, status, scrollbars, resizable, dependent, width=700, height=700, left=0, top=0')
    }

    function openQuickGuideWindow(url) {
        popupWin = window.open(url,
                'open_window',
                'menubar, toolbar, location, directories, status, scrollbars, resizable, dependent, width=700, height=700, left=0, top=0')
    }
</script>

<div id="header">

    <div class="background-R">
        <div id="login-action">
            <br/>
            <table width="100%" height="78px">
                <tr>
                    <td><a href="/proctcae" title="Home"><img
                            src='<tags:imageUrl name="blue/PRO-CTCAE-logo.png"/>'/></a></td>
                    <td width="50%" style="color:white;vertical-align:top;">&nbsp;&nbsp;&nbsp;
                    </td>
                    <td align="right" width="252px">
                        <c:if test="${pageContext.request.requestURI eq '/proctcae/public/login'}">
                            <c:if test="${empty param.lang}">
                                <c:set var="currentEn" value="current"/>
                                <c:if test="${pageContext.response.locale eq 'es'}">
                                    <c:set var="currentEs" value="current"/>
                                    <c:set var="currentEn" value=""/>
                                </c:if>
                            </c:if>

                            <c:if test="${param.lang eq 'en'}">
                                <c:set var="currentEn" value="current"/>
                                <c:set var="currentEs" value=""/>
                            </c:if>
                            <c:if test="${param.lang eq 'es'}">
                                <c:set var="currentEn" value=""/>
                                <c:set var="currentEs" value="current"/>
                            </c:if>
                            <div class="language-toggle1" style="float:right;margin-right:26px">
                                <a class="left ${currentEn}" href="?lang=en">English</a>
                                <a class="right ${currentEs}" href="?lang=es">Español</a>
                            </div>
                            <div style="margin-top:0px;margin-right:-5px">
                                <div style="margin-top:30px;color:white;margin-left:0px;font-size:12px;font-family:'Lucida Grande',sans-serif;text-shadow:none">
                                    <table>
                                        <tr>
                                            <td>
                                                <c:set var="url" value="/proctcae/images/quickstart_guide_v5.pdf"/>
                                                <c:if test="${param.lang eq 'es'}">
                                                    <c:set var="url" value="/proctcae/images/quickstart_guide_spanish_v2.pdf"/>
                                                </c:if>
                                                <a onclick="openQuickGuideWindow('${url}');"
                                                   style="cursor:pointer;color:white;border:0px;background:none;">
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                    <img src="/proctcae/images/table/pdf.jpg"/>
                                                            </td>
                                                            <td>
                                                                <b><tags:message
                                                                        code="login.quickStart"/></b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </a>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${param.lang eq 'es'}">
                                                            <a onclick="openNewWindow('showVideo?lang=es');"
                                                                style="cursor:pointer;color:white;border:0px;background:none;">
                                                    </c:when>
                                                    <c:otherwise>
                                                            <a onclick="openNewWindow('showVideo?lang=en');"
                                                                style="cursor:pointer;color:white;border:0px;background:none;">
                                                    </c:otherwise>
                                                </c:choose>
                                                    <table width="100%">
                                                        <tr>
                                                            <td width="35px;" align="left">
                                                                <img style="margin:0px;"
                                                                     src="<chrome:imageUrl name="../video_camera_icon.png"/>"
                                                                     alt=""/>
                                                            </td>
                                                            <td valign="middle" align="left">
                                                                <b><spring:message code="help.video"/></b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </a>
                                            </td>

                                        </tr>
                                    </table>

                                </div>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
        </div>

        <div id="taskbar">
            <c:if test="${pageContext.request.requestURI eq '/proctcae/public/login' }">
                <p align="center">
                    <input id='usevirtualkeyboard' type="checkbox" onclick="showVirtualKeyBoard(this,'username');">&nbsp;
                    <img src="/proctcae/images/keyboard-icon.png"/>
                    <tags:message code="login.userVirtualKeyboard"/></p>
            </c:if>

        </div>
        <div id="floatingTaskbar" style="display:none;">
            <tags:floatingTaskbar/>
        </div>

    </div>

    <spring:message code="user.keepworking" var="keepWorking"/>
    <div id="logout_warning" style="display:none;text-align:left;padding-left:10px; width:410px;">
        <p>
            <font size="3"> <tags:message code="instruction_logout_warning"/></font>
        </p>

        <div class="content buttons autoclear" style="margin-top:20px; width:400px;">
            <div class="flow-buttons">
	            <span class="next">
	                <tags:button color="blue" value="${keepWorking}" type="submit"
                                 onclick="logOutOKClicked();"></tags:button>
	            </span>
            </div>
        </div>
    </div>

</div>


<!-- end header -->
