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
    <table width="100%">
        <tr>
            <td align="center">
                <div class="row validate-NOTEMPTY&&DATE" id="effectiveStartDate-row" style="margin-left:5em">
                    <div class="label" style="width:21em;">
                        <span class="required-indicator">*</span>&nbsp;
                        Please provide a new date for this form&nbsp;
                    </div>
                    <div class="value">
                        <input id="effectiveStartDate" class="date validate-NOTEMPTY&&DATE"
                               name="effectiveStartDate"
                               title="Please provide a new date for this form"
                               value="<tags:formatDate value='${newdate}'/>" size="20" enabled=""
                               type="text">
                        <a href="#" id="effectiveStartDate-calbutton">
                            <img src="/proctcae/images/chrome/b-calendar.gif" alt="Calendar" width="17"
                                 align="absmiddle" border="0"
                                 height="16">
                        </a>
                        <i>(mm/dd/yyyy)</i>
                    </div>
                </div>

            </td>
        </tr>
        <tr>
            <td align="center"><b>Would you like to move only this form, all forms, or this and all following forms?<br><br></b>
            </td>
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
