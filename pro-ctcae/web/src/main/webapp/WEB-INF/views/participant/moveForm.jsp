<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<style type="text/css">
    div.row div.label {
        float: left;
        font-weight: bold;
        margin-left: 0.1em;
        text-align: right;
        width: 25em;
    }

    div.row div.value {
        font-weight: normal;
        margin-left: 26em;
        text-align: left;
    }
</style>
<div id="releaseForm">
    <tags:renderDate propertyName="effectiveStartDate"
                     displayName="participant.schedule.newdate" required="true" noForm="true"
                     dateValue="${newdate}"/>
    <table width="100%">
        <tr>
            <td align="center"><b>Would you like to move only this form, all forms, or this and all following forms?<br></b>
            </td>
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
