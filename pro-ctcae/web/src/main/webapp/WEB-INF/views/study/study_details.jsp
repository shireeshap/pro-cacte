<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>


    <script type="text/javascript">

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
            $('command').submit();
        }

        Event.observe(window, "load", function() {
            acCreate(new siteAutoComplter('study.dataCoordinatingCenter.organization'))
            acCreate(new siteAutoComplter('study.fundingSponsor.organization'))
            acCreate(new siteAutoComplter('study.leadStudySite.organization'))

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

        <p><tags:instructions code="study.study_details.top"/></p>

        <tags:renderText propertyName="study.assignedIdentifier" displayName="study.label.assigned_identifier"
                         required="true" size="50"/>

        <tags:renderText propertyName="study.shortTitle" displayName="study.label.short_title"
                         required="true" size="50"/>

        <tags:renderTextArea propertyName="study.longTitle" displayName="study.label.long_title"
                             required="true" cols="47"/>


        <tags:renderTextArea propertyName="study.description" displayName="study.label.description"
                             required="false" cols="47"/>
       
       <c:choose>
           <c:when test="${command.study.studySponsor.organization ne null}">
               <div class="row">
                   <div class="label"><tags:message code='study.label.study_sponsor'/></div>
                   <div class="value">${command.study.studySponsor.organization.displayName}</div>
               </div>
           </c:when>
           <c:otherwise>
               <tags:renderAutocompleter propertyName="study.studySponsor.organization"
                                         displayName="study.label.study_sponsor"
                                         required="true" size="70"/>

               <script type="text/javascript">
                   acCreate(new siteAutoComplter('study.studySponsor.organization'))
               </script>
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
</jsp:attribute>

</tags:tabForm>

</body>
</html>