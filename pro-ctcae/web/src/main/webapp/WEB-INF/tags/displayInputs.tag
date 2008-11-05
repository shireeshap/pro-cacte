<%@ attribute name="name" %>
<%@ attribute name="id" %>
<%@ attribute name="displayName" %>
<%@ attribute name="required" %>
<%@ attribute name="categoryName" %>

<%@ attribute name="values" %>


<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="size" %>
<%@attribute name="disabled" type="java.lang.Boolean" %>
<c:choose>
    <c:when test="${categoryName == 'text'}">
        <input type="text" name="${name}" size="${empty size ? attributes.size : size}"
               title="${displayName}"
               cssClass="${required ? 'validate-NOTEMPTY&&MAXLENGTH2000' : 'validate-MAXLENGTH2000'}"/>
    </c:when>

    <c:when test="${categoryName == 'date'}"><tags:displayDateInput id="${id}" displayName="${displayName}"
                                                                    cssClass="${required ? 'validate-NOTEMPTY' : ''}"/></c:when>


    <c:when test="${categoryName == 'checkbox'}"><input:checkbox id="${id}"/></c:when>

    <c:when test="${categoryName == 'select'}">
        <form:select name="${name}" items="${values}" title="${displayName}"
                     cssClass="${required ? 'validate-NOTEMPTY' : ''}">

        </form:select>
        <input type="hidden" name="test" id="test"/>

    </c:when>


    <c:when test="${categoryName == 'autocompleter'}">

        <input size="${empty size ? empty attributes.size ? '50' : attributes.size : size}" type="text"
               id="${id}-input" title="${displayName}" ${disabled ? 'disabled' : ''}
               class="autocomplete ${required ? 'validate-NOTEMPTY' : ''}"/>
        <tags:indicator id="${id}-indicator"/>
        <c:if test="${attributes.enableClear and not disabled}"><input type="button" id="${id}-clear" name="C"
                                                                       value="Clear"
                                                                       onClick="javascript:$('${id}-input').clear();$('${id}').clear();"/>
        </c:if>
        <div id="${id}-choices" class="autocomplete" style="display: none"></div>
        <input type="hidden" name="${name}" id="${id}"/>


    </c:when>

    <c:otherwise>
        UNIMPLEMENTED TYPE ${categoryName} for ${id}
    </c:otherwise>
</c:choose>


