<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="releaseForm">
    <div style="border:1px solid #ccc; height:65px; padding:9px; margin-bottom:10px;">
        Please provide a new date for this form
        <tags:renderDate propertyName="effectiveStartDate"
                         displayName="form.label.effective_start_date" required="true" noForm="true"/>
    </div>

    <table width="100%">
        <tr>
            <td align="center"><b>Would you like to move only this form, all forms, or this and all following forms by
                day(s)?<br></b></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td align="center">
                <input type="button" value="Only this instance"
                       onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','add,del')"/>
                &nbsp;&nbsp;&nbsp;
                <input type="button" value="All events"
                       onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','moveall')"/>
                &nbsp;&nbsp;&nbsp;
                <input type="button" value="All following"
                       onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','moveallfuture')"/>
                &nbsp;&nbsp;&nbsp;
                <input type="button" value="Cancel"
                       onclick="parent.addRemoveSchedule('${index}',$('effectiveStartDate').value+',${olddate}','cancel')"/>
            </td>
        </tr>
    </table>
</div>
