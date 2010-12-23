<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<tags:dwrJavascriptLink objects="
uniqueIdentifier"/>
<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>

    <style>
        .specialWidth {
            width: 10px;
        }
    </style>

    <script type="text/javascript">

        var errorCount=0;
        function addStudyArmDiv(transport) {
            $('studyArmTable').show()

            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }

        function addStudyArm() {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudyArm"/>", {
                onComplete:addStudyArmDiv,
                parameters:<tags:ajaxstandardparams/>+"&componentType=site",
                method:'get'
            })
        }

        function deleteArm(armIndex) {
            $('armIndexToRemove').value = armIndex;
            var ele = document.getElementsByName('study.arms[' + armIndex + '].title')[0];
            if (ele.value == '') {
                ele.value = ".";
            }
            refreshPage();
        }

        function refreshPage() {
            var currentPage = $('_page').value;
            $('_target').name = '_target' + currentPage;
            alert("hello");
            $('command').submit();
        }
        function showHideCallFreq(value) {
            if (value) {
                jQuery('#call_freq').show();
            } else {
                jQuery('#call_freq').hide();
            }
        }
        function checkStudyIdentifier() {
            var studyId = "${param['studyId']}";
            var identifier = $('study.assignedIdentifier').value;
            if (identifier != "") {
                uniqueIdentifier.validateUniqueIdentifier(studyId, identifier, postCommentHandler);
                return;
            }
            else {
                jQuery('#error1').hide();
            }
        }
        function postCommentHandler(returnvalue) {
            if (returnvalue)
            {
                errorCount++;
                jQuery('#error1').show();
            }
            else {
                errorCount=0;
                jQuery('#error1').hide();
            }
        }
        Event.observe(window, "load", function() {

            //            $('study.callBackFrequency-row').addClassName('specialWidth');

        <c:if test="${command.admin eq true}">
            acCreate(new siteAutoComplter('study.studySponsor.organization'))
        </c:if>
            acCreate(new siteAutoComplter('study.dataCoordinatingCenter.organization'))
            acCreate(new siteAutoComplter('study.fundingSponsor.organization'))
            acCreate(new siteAutoComplter('study.leadStudySite.organization'))

        <c:if test="${command.study.studySponsor ne null}">
        <c:if test="${command.admin eq true}">
            initializeAutoCompleter('study.studySponsor.organization',
                    '${command.study.studySponsor.organization.displayName}', '${command.study.studySponsor.organization.id}')
        </c:if>
        </c:if>

        <c:if test="${command.study.dataCoordinatingCenter ne null}">
            initializeAutoCompleter('study.dataCoordinatingCenter.organization',
                    '${command.study.dataCoordinatingCenter.organization.displayName}', '${command.study.dataCoordinatingCenter.organization.id}')
        </c:if>
        <c:if test="${command.study.fundingSponsor ne null}">
            initializeAutoCompleter('study.fundingSponsor.organization',
                    '${command.study.fundingSponsor.organization.displayName}', '${command.study.fundingSponsor.organization.id}')
        </c:if>
        <c:if test="${command.study.leadStudySite ne null}">
            initializeAutoCompleter('study.leadStudySite.organization',
                    '${command.study.leadStudySite.organization.displayName}', '${command.study.leadStudySite.organization.id}')
        </c:if>
            initSearchField()
        })
    </script>
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="true">
   <jsp:attribute name="singleFields">
        <tags:instructions code="study.study_details.top"/><br/>
        <tags:renderText propertyName="study.assignedIdentifier" displayName="study.label.assigned_identifier"
                         required="true" size="50" onblur="checkStudyIdentifier()"/>
        <ul id="error1" style="display:none; padding-left:12em " class="errors">
            <li><spring:message code='study.unique_assignedIdentifier' text='study.unique_assignedIdentifier'/></li>
        </ul>
        <tags:renderText propertyName="study.shortTitle" displayName="study.label.short_title"
                         required="true" size="50"/>

        <tags:renderTextArea propertyName="study.longTitle" displayName="study.label.long_title"
                             required="true" cols="47"/>


        <tags:renderTextArea propertyName="study.description" displayName="study.label.description"
                             required="false" cols="47"/>
       
       <c:choose>
           <c:when test="${command.admin eq true}">
               <tags:renderAutocompleter propertyName="study.studySponsor.organization"
                                         displayName="study.label.study_sponsor"
                                         required="true" size="70"/>
           </c:when>
           <c:otherwise>
               <c:choose>
                   <c:when test="${command.study.studySponsor.organization ne null}">
                       <div class="row">
                           <div class="label"><tags:message code='study.label.study_sponsor'/></div>
                           <div class="value">${command.study.studySponsor.organization.displayName}</div>
                       </div>
                   </c:when>
                   <c:otherwise>
                       <div class="row">
                           <div class="label"><tags:message code='study.label.study_sponsor'/></div>
                           <div class="value">
                               <select id="study.studySponsor.organization" name="study.studySponsor.organization">
                                   <option value="">Please select</option>
                                   <c:forEach items="${command.organizationsWithCCARole}" var="organization">
                                       <option value="${organization.id}">${organization.displayName}</option>
                                   </c:forEach>
                               </select>
                           </div>
                       </div>
                   </c:otherwise>
               </c:choose>
           </c:otherwise>
       </c:choose>

        <tags:renderAutocompleter propertyName="study.dataCoordinatingCenter.organization"
                                  displayName="study.label.study_coordinating_center"
                                  required="true" size="70"/>

        <tags:renderAutocompleter propertyName="study.fundingSponsor.organization"
                                  displayName="study.label.study_funding_sponsor"
                                  required="true" size="70"/>

        <tags:renderAutocompleter propertyName="study.leadStudySite.organization"
                                  displayName="study.label.study_lead_site"
                                  required="true" size="70"/>
         <br>

      <c:if test="${not command.activeDefaultArm}">
          <chrome:division title="study.section.study_arms">
              <p><tags:instructions code="study.study_arms.top"/></p>

              <div align="left" style="margin-left: 50px">
                  <table width="95%" class="tablecontent" id="studyArmTable">
                      <tr id="sa-table-head" class="amendment-table-head">
                          <th class="tableHeader"><spring:message
                                  code='study.label.arms' text=''/></th>
                          <th class="tableHeader">&nbsp;</th>

                      </tr>
                      <c:forEach items="${command.study.nonDefaultArms}" var="arm" varStatus="status">
                          <tags:oneStudyArm index="${status.index}" arm="${arm}"/>
                      </c:forEach>
                      <tr id="hiddenDiv" align="center"></tr>
                  </table>
              </div>
          </chrome:division>
          <div align="left" style="padding-left:4em">
              <tags:button color="blue" icon="add" markupWithTag="a" onclick="javascript:addStudyArm()" size="small"
                           value="study.button.add_study_arm"/></div>
          <form:hidden path="armIndexToRemove" id="armIndexToRemove"/>
      </c:if>
       <br>
       <chrome:division title="study.sections.study_modes">
           <table border="0">
               <tr>
                   <td valign="top" align="right">
                       <b><tags:message code='study.label.home_reporting'/>&nbsp;</b>
                   </td>
                   <td>
                       <form:checkbox path="appModes" value="HOMEWEB"/>&nbsp;Web <br>
                       <form:checkbox path="appModes" value="IVRS"
                                      onclick="javascript:showHideCallFreq(this.checked);"/>&nbsp;IVRS/Automated
                       Telephone <br>

                       <div id="call_freq" style="display:none;">
                           <table >
                               <tr>
                                   <td align="right">
                                       <b>Reminder frequency</b>
                                   </td>
                                   <td>
                                       <select id="call_back_hour" name="call_back_hour">
                                           <option value=" ">Please select</option>
                                           <option value="30"
                                                   selected ${command.study.callBackHour eq 30 ? "selected='selected'" : " "}>
                                               30 minutes
                                           </option>
                                           <option value="60" ${command.study.callBackHour eq 60 ? "selected='selected'" : " "}>
                                               60 minutes
                                           </option>
                                       </select>
                                   </td>
                               </tr>
                               <tr>
                                   <td>
                                       <b>Number of reminders</b>
                                   </td>
                                   <td>
                                       <select id="call_back_frequency" name="call_back_frequency">
                                           <option value=" ">Please select</option>
                                           <option value="1" ${command.study.callBackFrequency eq 1 ? "selected='selected'" : " "}>
                                               1
                                           <option value="2" ${command.study.callBackFrequency eq 2 ? "selected='selected'" : " "}>
                                               2
                                           <option value="3"
                                                   selected ${command.study.callBackFrequency eq 3 ? "selected='selected'" : " "}>
                                               3
                                           <option value="4" ${command.study.callBackFrequency eq 4 ? "selected='selected'" : " "}>
                                               4
                                           <option value="5" ${command.study.callBackFrequency eq 2 ? "selected='selected'" : " "}>
                                               5
                                       </select>
                                   </td>
                               </tr>
                           </table>
                       </div>
                       <form:checkbox path="appModes" value="HOMEBOOKLET"/>&nbsp;Paper Form <br>
                   </td>
               </tr>
               <tr>
                   <td valign="top">
                       <b><tags:message code='study.label.clinic_reporting'/>&nbsp;</b>
                   </td>
                   <td>
                       <form:checkbox path="appModes" value="CLINICWEB"/>&nbsp;Web <br>
                       <form:checkbox path="appModes" value="CLINICBOOKLET"/>&nbsp;Paper Form <br>
                   </td>
               </tr>
           </table>
           <%--<div class="row">--%>
           <%--<div class="label"><tags:message code='study.label.home_reporting'/></div>--%>
           <%--<div class="value">--%>
           <%--<form:checkbox path="appModes" value="Web" label="Web"/> <br>--%>
           <%--<form:checkbox path="appModes" value="IVRS" label="IVRS"/> <br>--%>
           <%--<form:checkbox path="appModes" value="Booklet" label="Booklet"/> <br>--%>
           <%--<form:checkbox path="appModes" value="Clinic" label="Clinic"/>--%>
           <%--</div>--%>
           <%--</div>--%>

           <%--<tags:renderCheckBox displayName="Display Modes" propertyName="appModes" />--%>
       </chrome:division>

</jsp:attribute>

</tags:tabForm>

</body>
</html>