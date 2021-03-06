<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@taglib prefix="organization" tagdir="/WEB-INF/tags/organization" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:dwrJavascriptLink objects="uniqueOrgIdValidator"/>

<html>
<head>
    <style type="text/css">
        div {
            font-weight: bold;
        }

        .nested_section {
            width: 100%;
            margin-left: 20px;
        }

        .required_label {
            text-align: left;
            float: left;
            line-height: 23px;
            margin: 0px 5px;
        }

        .required_value {
            float: left;
            line-height: 20px;
        }

        .required_item_heading {
            clear: both;
        }

        .updated {
            border: #494 solid;
            border-width: 1px 0;
            background-color: #8C8;
            padding: 1em 2em;
            text-align: center;
            margin: 1em 30%;
            color: #fff;
            font-weight: bold;
            font-size: 1.1em;
        }
    </style>
    <script type="text/javascript">
 		function checkOrganizationIdentifier() {
	  		var organizationId = "${param['organizationId']}";
	  		if(organizationId == null) {
	  			organizationId = "${createOrganizationCommand.organization.nciInstituteCode}";
	  		}
	  		
	  		var organizationIdentifier = jQuery("#organization\\.nciInstituteCode").val();
	  		jQuery("#organization\\.nciInstituteCode\\.error").hide();
	  		if(organizationIdentifier != '') {
				uniqueOrgIdValidator.validate(organizationId, organizationIdentifier, validationCallBackHandler);
				return
			} else {				    
		  			jQuery("#organization\\.nciInstituteCode\\.error").hide();
		  		}
  		}
 		
 		function validationCallBackHandler(result){ 						
			if(result) {
	  			jQuery("#organization\\.nciInstituteCode\\.error").show();
	  		} else {
	  			jQuery("#organization\\.nciInstituteCode\\.error").hide();
	  		}			
		}
 		
 		function checkSpecialCharacters() {
 			var hasSpecialCharacter = false;
 			jQuery("#org\\.name\\.specChar\\.error").hide();
 			jQuery("#org\\.nciInsti\\.specChar\\.error").hide();
 			if(isSpclCharEntered("#organization\\.name")) { 				
	  			jQuery("ul#org\\.name\\.specChar\\.error li").text("Special characters are not allowed here.");
	  			jQuery("#org\\.name\\.specChar\\.error").show();
	  			hasSpecialCharacter = true;
	  		} 
 			if(jQuery("#organization\\.nciInstituteCode").length > 0 && isSpclCharEntered("#organization\\.nciInstituteCode")) { 
 				jQuery("ul#org\\.nciInsti\\.specChar\\.error li").text("Special characters are not allowed here.");
	  			jQuery("#org\\.nciInsti\\.specChar\\.error").show();
	  			hasSpecialCharacter = true;
	  		} 			
 			if(hasSpecialCharacter) {
 				return false;
 			}
 			else { 				
	  			jQuery("#org\\.name\\.specChar\\.error").hide();
	  			jQuery("#org\\.nciInsti\\.specChar\\.error").hide();
	  			return true;
	  		}			
 		}
 		
 		function isSpclCharEntered(fieldName) {
 	        var iChars = "`~!@#$^&*+=[]\\\';,./{}|\":<>?";
 	        var fieldValue = jQuery(fieldName).val();
 	        jQuery('#' + fieldName + '.error').hide();
 	        jQuery(fieldName + '.error').hide();
 	        for (var i = 0; i < fieldValue.length; i++) {
 	            if (iChars.indexOf(fieldValue.charAt(i)) != -1) { 	               
 	                return true;
 	            }
 	        }
 	        return false;
 	    }
 	</script>
</head>
<body>
<organization:thirdlevelmenu selected="createOrganization"/>
<c:if test="${updated}"><p class="updated">Organization saved</p></c:if>
<ctcae:form method="post" commandName="createOrganizationCommand">
	<tags:hasErrorsMessage/>
    <chrome:box title="Create /Edit" autopad="true">
		<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
	    <c:set var="isEdit" value="${param['organizationId'] ne null}"/>
		
        <p><tags:instructions code="organization.instructions"/></p>
		<c:choose>
			<c:when test="${!isEdit}">
				<table width="100%">
			        <tr>
			            <td>
			                <tags:renderText propertyName="organization.name"
			                                 displayName="organization.name"
			                                 required="true"
			                                 size="60"/>
			                <ul id="organizationName.error" style="display:none; padding-left:12em " class="errors">
			                    <li><spring:message code='organizationName_validation' text='organizationName_validation'/>
			                    </li>
			                </ul>
			               <ul id="org.name.specChar.error" style="display:none;" class="errors">
			                    <li></li>
			                </ul>
			            </td>
			        </tr>
			        <tr>
			        	<td>
			        		<tags:renderText propertyName="organization.nciInstituteCode"
			                                 displayName="organization.assignedIdentifier"
			                                 required="true"
			                                 size="45"
			                                 onblur="checkOrganizationIdentifier()"/>
			                <ul id="organization.nciInstituteCode.error" style="display:none;" class="errors">
		                            <li><spring:message code='nciInstituteCode_validation' text='nciInstituteCode_validation'/>
				                    </li>
			                </ul>
			                <ul id="org.nciInsti.specChar.error" style="display:none;" class="errors">
			                    <li></li>
			                </ul>
			        	</td>
			        </tr>
		    	</table>
			</c:when>
			<c:otherwise>
				<table width="100%">
			        <tr>
			            <td>
			            	<tags:renderText propertyName="organization.name"
			                                 displayName="organization.name"
			                                 required="true"
			                                 size="60"/>
			                <ul id="organizationName.error" style="display:none; padding-left:12em " class="errors">
			                    <li><spring:message code='organizationName_validation' text='organizationName_validation'/>
			                    </li>
			                </ul>
			                <ul id="org.name.specChar.error" style="display:none;" class="errors">
			                    <li></li>
			                </ul>
			            </td>
			        </tr>
			        <tr>
			        	<td>
			        		<div class="row">
				            	<div class="label"><spring:message code="organization.assignedIdentifier"/></div>
				                <div class="value">${createOrganizationCommand.organization.nciInstituteCode}</div>
			            	</div>
			        	</td>
			        </tr>
		    	</table>
			</c:otherwise>
		</c:choose>
    </chrome:box>
    <div style="float: right;">
        <tags:button type="submit" value="Save" color="green" icon="save" onclick="JavaScript:return checkSpecialCharacters();"/>
    </div>
</ctcae:form>
</body>
</html>
