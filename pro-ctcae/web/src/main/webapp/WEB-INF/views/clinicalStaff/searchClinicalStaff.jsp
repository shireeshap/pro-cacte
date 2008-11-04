<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">

<html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
      <tags:javascriptLink name="extremecomponents"/>
      <tags:dwrJavascriptLink objects="clinicalStaff"/>

      <script>

             function buildTable(form) {

                 var firstName = $F('firstName')
                 var lastName = $F('lastName')
                 var nciIdentifier = $F('nciIdentifier')

            //     alert(lastName);

                 if (firstName == '' && lastName == '' && nciIdentifier == '') {
                     $('error').innerHTML = "<font color='#FF0000'>Provide at least one value in the search field</font>"  ;

                 } else {
                     $('error').innerHTML = ""
                     $('bigSearch').show()
                     //		//showing indicator and hiding pervious results. (#10826)
                     $('indicator').className = '';
                     //	$('assembler_table').hide();  //do not hide the results..becz filter string get disappear
                     var parameterMap = getParameterMap(form);
                 //   clinicalStaff.searchClinicalStaff(parameterMap, showTable);
                    clinicalStaff.searchClinicalStaff(parameterMap, firstName, lastName, nciIdentifier, showTable);
                 }
             }


         </script>


  </head>
  <body>
  <div class="tabpane">
   <div class="workflow-tabs2">
       <ul id="" class="tabs autoclear">
           <li id="thirdlevelnav" class="tab selected">
               <div>
                   <a href="createClinicalStaff">Create/Edit ClinicalStaff</a>
               </div>
           </li>
           <li id="thirdlevelnav" class="tab">
               <div>
                   <a href="searchClinicalStaff">Search ClinicalStaff</a>
               </div>
           </li>
       </ul>
   </div>
</div>
  <chrome:box title="Search Criteria" autopad="true">
      <p><tags:instructions code="clinicalStaff.search.top"/> </p>

     <!-- <table>
          <tr><td>First Name</td><td><input type="text" id="firstName" name="firstName" maxlength="30"/></td></tr>
          <tr><td>Last Name</td><td><input type="text" id="lastName" name="lastName" maxlength="30"/></td></tr>
          <tr><td>ClinicalStaff Number</td><td><input type="text" id="nciIdentifier"name="nciIdentifier" maxlength="30"/></td></tr>
      </table>
        -->
      <div class="row">
           	<div class="label">First name</div>
           	<div class="value"><input type="text" id="firstName" name="firstName" maxlength="30"/></div>
       	</div>
		<div class="row">
           	<div class="label">Last name</div>
           	<div class="value"><input type="text" id="lastName" name="lastName" maxlength="30"/></div>
       	</div>
		<div class="row">
           	<div class="label">Identifier</div>
           	<div class="value"><input type="text" id="nciIdentifier"name="nciIdentifier" maxlength="30"/></div>
       	</div>

      <div id="error"></div>
  <div class="row">
              <div class="label"></div>
              <div class="value">

      <input class='ibutton' type='button'  onclick="buildTable('assembler');"
                    value='Search' title='Search ClinicalStaff'/>
             <tags:indicator id="indicator"/>
              </div>
     </div>


  </chrome:box>

  <div id="bigSearch" style="display:none;">
      <div class="endpanes"/>
      <chrome:box title="Results">
          <p><tags:instructions code="study.search.results"/></p>
          <form:form id="assembler">
              <chrome:division id="single-fields">
                  <div id="tableDiv">
                      <c:out value="${assembler}" escapeXml="false"/>
                  </div>
              </chrome:division>
          </form:form>
      </chrome:box>
 </div>


  </body>
</html>