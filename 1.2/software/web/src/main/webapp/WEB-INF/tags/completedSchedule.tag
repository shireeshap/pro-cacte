<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="completedSchedule" type="gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule"
             required="true" %>
<c:set var="myindex" value="1"/>

<div style="float:right; padding-right:5px">
    <tags:button color="blue" markupWithTag="a" value="Print"
                 onclick="window.print();return false;"/>
</div>

<table width="100%" cellpadding="3px" cellspacing="0px" border="0">
    <c:set var="myindex" value="1"/>
    <c:forEach items="${completedSchedule.symptomItems}" var="symptom">
        <tr>
            <td><br/></td>
        </tr>
        <tr style="background-color:#cccccc;">
            <td><b>${symptom.key} </b></td>
        </tr>
        <c:forEach items="${symptom.value}" var="items">
            <tr>
                <td><b>${myindex}. ${items[0]}</b></td>
            </tr>
            <tr>
                <td>Answer: <u><i>${items[1]}</i></u></td>
            </tr>
            <c:set var="myindex" value="${myindex + 1}"/>
        </c:forEach>
    </c:forEach>
</table>