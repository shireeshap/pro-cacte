<%@ page import="gov.nih.nci.ctcae.web.filter.SecurityRequestWrapper" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />

<%@page language="java" isErrorPage="true" %>
<%@ page isErrorPage="true" %>
<%@ page language="java" %>

<page:applyDecorator name="standard">

    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <!--    <script type="text/javascript" language="JavaScript">
            function PanelCombo(element) {
                panelDiv = $(element + "-interior");
                imageId = element + '-image';
                imageSource = document.getElementById(imageId).src;
                if (panelDiv.style.display == 'none') {
                    new Effect.OpenUp(panelDiv, arguments[1] || {});
                    document.getElementById(imageId).src = imageSource.replace('minimize', 'maximize');
                } else {
                    new Effect.CloseDown(panelDiv, arguments[1] || {});
                    document.getElementById(imageId).src = imageSource.replace('maximize', 'minimize');
                }
            }
            Effect.OpenUp = function(element) {
                element = $(element);
                new Effect.BlindDown(element, arguments[1] || {});
            }

            Effect.CloseDown = function(element) {
                element = $(element);
                new Effect.BlindUp(element, arguments[1] || {});
            }

            Effect.Combo = function(element) {
                element = $(element);
                if (element.style.display == 'none') {
                    new Effect.OpenUp(element, arguments[1] || {});
                } else {
                    new Effect.CloseDown(element, arguments[1] || {});
                }
            }
        </script>    --> 
    </head>

    <body>
	<br />
    <chrome:box title="Error" autopad="true">

        <div class="row">
            <div class="error">
                <div class="label">
                    OOPS:
                </div>
            </div>
            <div class="value">
                The system encountered an error. Please contact your system administrator.
            </div>
        </div>

        <div class="row">
            <div class="value">
                <a href="<c:url value="/"/>">Return Home</a>
            </div>
        </div>
    </chrome:box>


    <div class="box">
        <!-- header -->
        <div class="header">
            <div class="background-L">
                <div class="background-R">
                    <table width="100%">
                        <tr>
                            <td>
                                <h2>Detailed Error</h2>
                            </td>
                            <td align="right">
                           <!--      <div id="error-image-div">
                                    <a href="javascript:PanelCombo('error');">
                                        <img id="error-image" src="<tags:imageUrl name="chrome/minimize.gif"/>" border="2" alt="toggle button"></a>
                                </div> -->
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <!-- end header --><!-- end header -->

        <!-- inner border -->
        <div class="border-T">
            <div class="border-L">
                <div class="border-R">
                    <div class="border-B">
                        <div class="border-TL">
                            <div class="border-TR">
                                <div class="border-BL">
                                    <div class="border-BR">
                                        <div id="error-interior" class="interior" >
                                            <div class="content">

                                                <TABLE CELLPADDING="5" CELLSPACING="0" BORDER="1" WIDTH="100%">
                                                    <TR>
                                                        <TD WIDTH="20%"><B>Status Code</B></TD>
                                                        <TD WIDTH="80%"><c:out value="${requestScope['javax.servlet.error.status_code']}" />
                                                        </TD>
                                                    </TR>
                                                    <TR>
                                                        <TD WIDTH="20%"><B>Exception Type</B></TD>
                                                        <TD WIDTH="80%"><c:out value="${requestScope['javax.servlet.error.exception']}" />
                                                        </TD>
                                                    </TR>
                                                    <TR>
                                                        <TD WIDTH="20%"><B>Message</B></TD>
                                                        <TD WIDTH="80%"><c:out value="${requestScope['javax.servlet.error.message']}" />
                                                        </TD>
                                                    </TR>
                                                    <TR>
                                                        <TD WIDTH="20%"><B>Timestamp</B></TD>
                                                        <TD WIDTH="80%"><fmt:formatDate value="${date}" type="both" dateStyle="long" timeStyle="long" />
                                                        </TD>
                                                    </TR>
                                                    <TR>
                                                        <TD WIDTH="20%"><B>Action</B></TD>
                                                        <TD WIDTH="80%"><c:out value="${requestScope['javax.servlet.forward.request_uri']}" />
                                                        </TD>
                                                    </TR>
                                                    <TR>
                                                        <TD WIDTH="20%"><B>User agent</B></TD>
                                                        <TD WIDTH="80%"><c:out value="${header['user-agent']}" />
                                                        </TD>
                                                    </TR>
                                                </TABLE>
                                                
                                                <br />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end inner border -->
        </div>


    </div>
    </body>
    </html>
</page:applyDecorator>