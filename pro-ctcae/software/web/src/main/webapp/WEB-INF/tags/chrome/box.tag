<%@ attribute name="noBackground" %>
<%-- TODO: support for inner tabs (needs uniform controller support first) --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="title" %>
<%@attribute name="message" type="java.lang.Boolean" required="false" %>
<%@attribute name="id" %>
<%@attribute name="cssClass" %>
<%@attribute name="style" %>
<%-- If this attribute is true, the provided contents will be wrapped in a .content div.
     Use it if the box will only need one content div -- i.e., it doesn't contain any
     chrome:divisions with titles. --%>
<%@attribute name="autopad" required="false" %>
<%@attribute name="collapsable" required="false" %>
<%@attribute name="enableDelete" required="false" %>
<%@attribute name="deleteParams" required="false" %>
<%@attribute name="collapsed" required="false" %>
<%@attribute name="omitBorders" required="false" %>


<c:set var="includeBorders" value="true"/>
<c:if test="${omitBorders}">
	<c:set var="includeBorders" value="false"/>
</c:if>
<div class="<c:if test="${includeBorders}"> box </c:if> ${cssClass}"
        <tags:attribute name="id" value="${id}"/> <tags:attribute name="style" value="${style}"/>>

    <!-- header -->
    <div class="header">
    <c:if test="${includeBorders}">
        <div class="background-L">
            <div class="background-R">
    </c:if>
                <h2>
                    <c:choose>
                        <c:when test="${message == false}">
                            ${title}
                        </c:when>
                        <c:otherwise>
                            <table cellpadding="0" cellspacing="0" 
	                            <c:choose>
	                            	<c:when test="${not includeBorders}">
	                            		width="90%" style="position:relative; left:5px;"
	                            	</c:when>
	                            	<c:otherwise>
	                            		width="100%"
	                            	</c:otherwise>
	                            </c:choose>>
                                <tr>
                                    <c:if test="${collapsable}">
                                        <td align="left" width="5%">
                                            <a style="cursor:pointer;"
                                               onClick="SwitchCollapsableState('contentOf-${id}', '${id}')">
                                                <img id="image-${id}" src="<tags:imageUrl name="arrow-down.png" />"
                                                     border="0"
                                                     height="16"/></a>
                                        </td>
                                    </c:if>
                                    <td>
                                        <spring:message code='${title}' text='${title}'/>
                                    </td>
                                    <td width="5%">
                                        <c:if test="${enableDelete}">
                                            <a href="javascript:${deleteParams}"><img
                                                    src="<c:url value="/images/checkno.gif"/>" border="0" alt="delete"></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </h2>
                <c:if test="${!empty title}">
                    <div class="hr"></div>
                </c:if>
      <c:if test="${includeBorders}">
            </div>
        </div>
      </c:if>
    </div>
    <!-- end header -->

    <!-- inner border -->
    <c:if test="${includeBorders}">
	    <div class="border-T">
	        <div class="border-L">
	            <div class="border-R">
	                <div class="border-B">
	                    <div class="border-TL">
	                        <div class="border-TR">
	                            <div class="border-BL">
	                                <div class="border-BR">
	 </c:if>
	                                    <div class="interior">
	                                        <div id="contentOf-${id}">
	
	                                            <c:if test="${autopad}">
	                                            <div class="content"></c:if>
	                                                <jsp:doBody/>
	                                                <c:if test="${autopad}"></div>
	                                            </c:if>
	                                        </div>
	                                    </div>
	<c:if test="${includeBorders}">
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
    </div>
    </c:if>
    <!-- end inner border -->
</div>
<c:if test="${collapsed}">
    <script type="text/javascript">
        SwitchCollapsableState('contentOf-${id}', '${id}');
    </script>
</c:if>
<!-- end box -->