<%-- TODO: support for inner tabs (needs uniform controller support first) --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" %>
<%@attribute name="id" %>
<%@attribute name="cssClass" %>
<%@attribute name="style" %>
<%@attribute name="autopad" required="false" %>
<%@attribute name="collapsable" required="false" %>
<div class="formbuilderBox ${cssClass}"<tags:attribute name="id" value="${id}"/><tags:attribute name="style"
                                                                                                value="${style}"/>>

    <table class="formbuilderboxTable">
        <tr>
            <td class="TL"></td>
            <td class="T"></td>
            <td class="TR"></td>
        </tr>
        <tr>
            <td class="L"></td>
            <td class="formbuilderboxContent">
                <jsp:doBody/>
            </td>
            <td class="R"></td>
        </tr>
        <tr>
            <td class="BL"></td>
            <td class="B"></td>
            <td class="BR"></td>
        </tr>
    </table>

</div>
<!-- end box -->