<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<table>
    <tr>
        <td>
            <ctcae:form method="post">
           	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
            
            <div id="releaseForm">
                <div style="border:1px solid #ccc; height:65px; padding:9px; margin-bottom:10px;">
                    Please provide the date on which new status will be made effective.
                    <tags:renderDate propertyName="statusDate"
                                     displayName="form.label.effective_start_date" required="true"/>
                </div>
                <br>
                <input type="hidden" name="status" value="${param['status']}"/>

                <div class="flow-buttons">
                    <tags:button color="orange" type="submit" id="flow-update"
                                 cssClass="next" value="Submit" icon="check"
                            />
                    <tags:button color="blue" type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="Cancel" icon="x"
                                 onclick="closeWindow()"/>
                </div>
            </div>
            </ctcae:form>
        </td>
    </tr>
</table>