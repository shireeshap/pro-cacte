<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@attribute name="title" %>
<%@attribute name="id" %>
<%@attribute name="cssClass" %>
<%@attribute name="style" %>
<%@attribute name="enableDelete" type="java.lang.Boolean" %>
<%@attribute name="deleteParams" %>
<%@attribute name="collapsable" required="false" %>
<%@attribute name="message" type="java.lang.Boolean" required="false" %>
<%@attribute name="linkontitle" required="false" %>
<%@attribute name="linkurl" required="false" %>

<c:if test="${linkontitle ne null and linkontitle ne ''}">
    <proctcae:urlAuthorize url="${linkurl}"><c:set var="showlink" value="true"/></proctcae:urlAuthorize>
</c:if>
<div class="division ${cssClass}" <tags:attribute name="id" value="${id}"/> <tags:attribute name="style"
                                                                                            value="${style}"/>>
    <div class="header">
        <c:if test="${not empty title}">
            <c:if test="${enableDelete || collapsable}">
                <h3>
                    <table cellspacing="0" cellpadding="0" border="0" width="100%">
                        <tr>
                            <td width="100%">
                                <c:choose>
                                    <c:when test="${message == true}">
                                        <c:choose>
                                            <c:when test="${linkontitle ne null and showlink}">
                                                <a href="${linkontitle}">${title}</a>
                                                <a href="${linkontitle}">
                                                    <img src='<tags:imageUrl name="controlPanel_pencil.png"/>'/>
                                                </a>
                                            </c:when>
                                            <c:otherwise>${title}</c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${linkontitle ne null and showlink}">
                                                <a href="${linkontitle}">
                                                    <spring:message code='${title}' text='${title}'/>
                                                </a>
                                                <a href="${linkontitle}">
                                                    <img src='<tags:imageUrl name="controlPanel_pencil.png"/>'/>
                                                </a>
                                            </c:when>
                                            <c:otherwise><spring:message code='${title}' text='${title}'/></c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${enableDelete}">
                                <td align="right"><a
                                        href="javascript:${deleteParams}"><img
                                        src="<c:url value="/images/checkno.gif"/>" border="0" alt="delete"></a></td>
                            </c:if>
                            <c:if test="${collapsable}">
                                <td align="right"><a style="cursor:pointer;"
                                                     onClick="SwitchCollapsableState('contentOf-${id}', '${id}')"><img
                                        id="image-${id}" src="<chrome:imageUrl name="minimize.gif" />" border="0"
                                        height="16"/></a></td>
                            </c:if>
                        </tr>
                    </table>
                </h3>
            </c:if>

            <c:if test="${!enableDelete && !collapsable}">
                <h3>
                    <c:choose>
                        <c:when test="${message == true}">
                            <c:choose>
                                <c:when test="${linkontitle ne null and showlink}">
                                    <a href="${linkontitle}">${title}</a>
                                    <a href="${linkontitle}">
                                        <img src='<tags:imageUrl name="controlPanel_pencil.png"/>'/>
                                    </a>
                                </c:when>
                                <c:otherwise>${title}</c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${linkontitle ne null and showlink}">
                                    <a href="${linkontitle}">
                                        <spring:message code='${title}' text='${title}'/>
                                    </a>
                                    <a href="${linkontitle}">
                                        <img src='<tags:imageUrl name="controlPanel_pencil.png"/>'/>
                                    </a>
                                </c:when>
                                <c:otherwise><spring:message code='${title}' text='${title}'/></c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </h3>
            </c:if>
        </c:if>
    </div>

    <div class="content" id="contentOf-${id}" style="display:block; padding:0px; margin:10px 0 10px 5px;">
        <c:if test="${collapsable && (empty id)}"><h1 style="color:red; padding-bottom:20px;">Please give an unique ID
            to your Division Element.</h1></c:if>
        <jsp:doBody/>
    </div>
</div>
