<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<tags:dwrJavascriptLink objects="nameValidator,userNameValidation,uniqueStaffEmailAddress"/>
<tags:javascriptLink name="ui_fields_validation"/>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:stylesheetLink name="yui-autocomplete"/>
	<tags:javascriptLink name="yui-autocomplete"/>
	<tags:dwrJavascriptLink objects="organization"/>

    <script type="text/javascript">
        var managerAutoComp;
        var orgName;
        Event.observe(window, "load", function() {
            <c:forEach  items="${clinicalStaffCommand.clinicalStaff.organizationClinicalStaffs}" var="organizationClinicalStaff" varStatus="status">
                <c:if test="${organizationClinicalStaff.id eq null}">
                    new YUIAutoCompleter('clinicalStaff.organizationClinicalStaffs[${status.index}].organizationInput', getOrgs, handleSelect);
                    orgName = "${organizationClinicalStaff.organization.displayName}";
                    if(orgName != ''){
                        $('clinicalStaff.organizationClinicalStaffs[${status.index}].organizationInput').value = "${organizationClinicalStaff.organization.displayName}";
                        $('clinicalStaff.organizationClinicalStaffs[${status.index}].organizationInput').removeClassName('pending-search');
                    }

                </c:if>
            </c:forEach>
        });


        // checking if there are any error based on class name and style.
        function hideError() {
            var errorlist;
            var count = 0;
            var hideError = false;
            for (i = 0; i < document.getElementsByClassName('errors').length; i++) {
                errorlist = document.getElementsByClassName('errors')[i];
                if (errorlist.style.display != 'none') {
                    count++;
                    if (errorlist.id == "") {
                        hideError = true;
                        errorlist.hide();

                    }
                }
            }
            
        }
        
        function validate(){
			var name=jQuery('[name=alert.alertMessage]').val();
			if(name != null || name != ''){
					var reg=/[^a-zA-Z0-9\!\@\%\*\_;,:.'<>\?\-\[\]\s\/()]+/; 
				
					if(reg.test(name)){  
						jQuery("#alertValidationErrorDiv").show();            
						return false;
					}               
				jQuery("#alertValidationErrorDiv").hide();
				return true;
				
				}
			jQuery("#alertValidationErrorDiv").hide();
			return true;
		}
        
    </script>
    <!--[if IE]>
    <style>
        div.row div.value {
            margin-left: 0;
        }
    </style>
    <![endif]-->
</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/admin/createClinicalStaff">
                <li id="thirdlevelnav-x" class="tab selected">
                    <div>
                        <a href="createSystemAlert"><tags:message code="alert.tab.createAlert"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/admin/searchClinicalStaff">
                <li id="thirdlevelnav-x" class="tab">
                    <div>
                        <a href="searchAlert"><tags:message code="alert.tab.searchAlert"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>

	<ctcae:form method="post" commandName="createSystemAlertCommand">
		<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
		
		<c:set var="isEdit"
		       value="${param['systemAlertId'] ne null}"/>
		<input name="isEdit" value="${isEdit}" type="hidden"/>
		<tags:hasErrorsMessage hideErrorDetails="false"/>
		
		<chrome:box title="Create /Edit" autopad="true" >
			<input type="hidden" id="showForm" name="showForm" value=""/>
			
			<p><tags:instructions code="alert.search.details.top"/></p>
			<chrome:division title="alert.division.details">
				<table width="100%">
					<tr>
						<td>
							<tags:renderDate propertyName="alert.startDate"
											 displayName="alert.label.startDate" 
											 required="true"/>
							<ul id="alert.startDate.error" style="display:none; padding-left:12em " class="errors">
			                    <li><spring:message code='startDate_validation' text='startDate_validation'/>
			                    </li>
			              		</ul>
						</td>
					</tr>
					<tr>
						<td>
							<tags:renderDate propertyName="alert.endDate"
											 displayName="alert.label.endDate"
											 required="true"/>
							<ul id="alert.endDate.error" style="display:none; padding-left:12em " class="errors">
			                    <li><spring:message code='endDate_validation' text='endDate_validation'/></li>
			              	</ul>
						</td>
					</tr>
					<tr>	
						<td>
							<tags:renderTextArea propertyName="alert.alertMessage"
												 displayName="alert.label.alertMessage"
												 required="true" />
							<ul id="alert.alertMessage.error" style="display:none; padding-left:12em " class="errors">
			                    <li><spring:message code='alertMessage_validation' text='alertMessage_validation'/></li>
			              	</ul>
			              	<div id="alertValidationErrorDiv" style="display:none;">
				              	<ul id="alertValidationError" style="padding-left:12em" class="errors">
				                    <li>Invalid character(s). The message can't have anything other than a-z A-Z 0-9 () - _ % = [] ; : ' < > , . ? / @ ! *</li>
				              	</ul>
				            </div>
						</td>
					</tr>
				</table>
			</chrome:division>
			<br/>
		</chrome:box>
			<div style="text-align:right"><tags:button type="submit" color="green" value="Save" icon="save" onclick="return validate();"/></div>
	</ctcae:form>

</body>
</html>