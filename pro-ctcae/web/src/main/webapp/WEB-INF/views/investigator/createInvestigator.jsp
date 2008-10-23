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

     <chrome:box title="Investigator details" autopad="true">

         <p><tags:instructions code="investigator.investigator_details.top" /></p>
          <chrome:division title="Investigator Details"></chrome:division>
         <table>
             <tr>
                 <td>

                     <tags:renderText propertyName="firstName" displayName="First Name"
                                  required="true" size="30"/>
                     <tags:renderText propertyName="middleName" displayName="Middle Name" size="30"/>
                     <tags:renderText propertyName="lastName" displayName="Last Name"
                                         required="true" size="30"/>
                     <tags:renderText propertyName="nciIdentifier" displayName="Investigator Number"
                                         required="true" size="30"/>

                  </td>
                  <td>

                      <tags:renderEmail propertyName="emailAddress" displayName="Email Address"
                                                              required="true" size="30"/>
                      <tags:renderPhoneOrFax propertyName="phoneNumber" displayName="Phone" 
                                                              required="true" size="30"/>
                      <tags:renderPhoneOrFax propertyName="faxNumber" displayName="Fax" size="30"/>

                 </td>
             </tr>
         </table>

         <div class="row">
                  <div class="submit">
                      <input type="submit" id="submitButton" value="Submit"/>
                  </div>
          </div>




     </chrome:box>

  </form:form>

  </body>
</html>