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

            Event.observe(window, "load", function() {
                  <c:forEach  items="${investigatorCommand.investigator.siteInvestigators}" var="siteInvestigator" varStatus="status">
                      var siteBaseName = 'investigator.siteInvestigators[${status.index}].organization'
                      acCreate(new siteAutoComplter(siteBaseName));
                      initializeAutoCompleter(siteBaseName, '${siteInvestigator.organization.displayName}', '${siteInvestigator.organization.id}');
                  </c:forEach>
                      initSearchField()
                  })

        
            function addSiteDiv(transport) {
                var response = transport.responseText;
                new Insertion.Before("hiddenDiv", response);
            }

        function addSite() {
                  var request = new Ajax.Request("<c:url value="/pages/investigator/addSite"/>", {
                      onComplete:addSiteDiv,
                      parameters:"subview=subview&",

                      method:'get'
                  })
              }
       </script>

  </head>
  <body>
  <div class="tabpane">
   <div class="workflow-tabs2">
       <ul id="" class="tabs autoclear">
           <li id="thirdlevelnav" class="tab selected">
               <div>
                   <a href="createInvestigator">Create/Edit Investigator</a>
               </div>
           </li>
           <li id="thirdlevelnav" class="tab">
               <div>
                   <a href="searchInvestigator">Search Investigator</a>
               </div>
           </li>
       </ul>
   </div>
</div>

  <form:form method="post" commandName="investigatorCommand">

     <chrome:box title="Investigator details">

         <p><tags:instructions code="investigator.investigator_details.top" /></p>
          <chrome:division title="Investigator Details"></chrome:division>
         <table >
             <tr>
                 <td>
                    <tags:renderText propertyName="investigator.firstName" displayName="First name"
                                  required="true" />
                     <tags:renderText propertyName="investigator.middleName" displayName="Middle name" />
                     <tags:renderText propertyName="investigator.lastName" displayName="Last name"
                                         required="true"  />
                     <tags:renderText propertyName="investigator.nciIdentifier" displayName="Investigator number"
                                         required="true" />

                  </td>
                  <td style="vertical-align:top">

                      <tags:renderEmail propertyName="investigator.emailAddress" displayName="Email address"
                                                              required="true"  />
                      <tags:renderPhoneOrFax propertyName="investigator.phoneNumber" displayName="Phone"
                                                              required="true"  />
                      <tags:renderPhoneOrFax propertyName="investigator.faxNumber" displayName="Fax"  />
                      

                 </td>
             </tr>
         </table>

         <chrome:division title="Investigator Sites">
               <p><tags:instructions code="study.study_sites.top"/></p>

               <input type="hidden" value="" id="objectsIdsToRemove" name="objectsIdsToRemove"/>

               <div align="left" style="margin-left: 50px">
                   <table width="55%" class="tablecontent">
                       <tr id="ss-table-head" class="amendment-table-head">
                           <th width="95%" class="tableHeader"><tags:requiredIndicator/>Site</th>
                           <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                       </tr>
               <c:forEach items="${investigatorCommand.investigator.siteInvestigators}" var="siteInvestigator" varStatus="status">

                           <tags:oneOrganization index="${status.index}"
                                                 inputName="investigator.siteInvestigators[${status.index}].organization"
                                                 title="Investigator Site" displayError="true"></tags:oneOrganization>
                       </c:forEach>

                       <tr id="hiddenDiv"></tr>

                   </table>

               </div>
               <tags:tabControls willSave="${willSave}" saveButtonLabel="${saveButtonLabel}">
                   <jsp:attribute name="localButtons">
                       <input type="button" value="Add Site" onClick="addSite()" class="button"/>

                   </jsp:attribute>
               </tags:tabControls>


           </chrome:division>


     </chrome:box>
<tags:tabControls willSave="true"/>
  </form:form>

  </body>
</html>