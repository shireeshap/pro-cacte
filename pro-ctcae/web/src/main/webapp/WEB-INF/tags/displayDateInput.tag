<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@attribute name="id" type="java.lang.String" %>
<%@attribute name="displayName" type="java.lang.String" %>
<%@attribute name="categoryName" type="java.lang.String" %>

<%@attribute name="cssClass" %>

<input type="text" id="${id}" cssClass="date ${cssClass}" title="${displayName}"/>
<a href="#" id="${displayName}-calbutton">
    <img src="<chrome:imageUrl name="b-calendar.gif"/>" alt="Calendar" width="17" height="16" border="0"
         align="absmiddle"/>
</a>
<i>(mm/dd/yyyy)</i>
