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

<style>
	.message_style{
		height:65px; 
		width: 520px; 
		padding:9px; 
		margin-bottom:10px;
	}
</style>

<table>
    <tr>
        <td>
            <ctcae:form method="post">
           	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
           	<input type="hidden" id="tabNumber" name="tabNumber" value="${param['tabNumber']}" />
            
            <div id="message" class="message_style">
            	<div style="text-align: left; margin-left: 0.5em;">
                   By clicking "Submit", the status of the clinical researcher in the system will be updated immediately.
                </div>
            	
            </div>
            <br>
            <input type="hidden" name="status" value="${param['status']}"/>

            <div class="flow-buttons">
                <tags:button color="orange" type="submit" id="flow-update"
                             cssClass="next" value="Submit" icon="check"
                             overRideStyle=" position:relative; top:0px;"
                        />
                <tags:button color="blue" type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="Cancel" icon="x"
                             onclick="closeWindow()"/>
            </div>
            </ctcae:form>
        </td>
    </tr>
</table>