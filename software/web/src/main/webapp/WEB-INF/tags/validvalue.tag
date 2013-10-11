<%@ attribute name="validValueId" type="java.lang.String" required="true" %>
<%@ attribute name="questionIndexOnPage" type="java.lang.String" required="true" %>
<%@ attribute name="validValueIndexForQuestion" type="java.lang.String" required="true" %>
<%@ attribute name="selectedId" type="java.lang.String" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="displayOrder" type="java.lang.String" required="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="class" value="norm"/>
<c:set var="onmouseout" value="onmouseout=\"javascript:this.className='norm';\""/>
<c:if test="${validValueId eq selectedId}">
    <c:set var="checked" value='checked="true"'/>
    <c:set var="class" value="selected"/>
    <c:set var="onmouseout" value=""/>
</c:if>
<td class="${class}"
    onmouseover="javascript:this.className = getClassName('${validValueIndexForQuestion}_column_${questionIndexOnPage}_rad', 'selected' , 'over');"  ${onmouseout}
    onclick="selectValidValue(this,'${displayOrder}','${questionIndexOnPage}','${validValueIndexForQuestion}','${title}')"
    id="${validValueIndexForQuestion}_column_${questionIndexOnPage}" >
    <div class="label">
        <input type="radio" id="${validValueIndexForQuestion}_column_${questionIndexOnPage}_rad"
               name="response${questionIndexOnPage}"
               value="${validValueId}" ${checked}/> ${title}
    </div>
</td>




