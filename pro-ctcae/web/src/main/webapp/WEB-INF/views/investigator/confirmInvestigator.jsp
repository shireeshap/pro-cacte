<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
     
      <style type="text/css">
            .label {
                width: 12em;
                padding: 1px;
                margin-right: 0.5em;
            }

            div.row div.value {
                white-space: normal;
            }

            #studyDetails td.label {
                font-weight: bold;
                float: left;
                margin-left: 0.5em;
                margin-right: 0.5em;
                width: 12em;
                padding: 1px;
            }
        </style>
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
  <chrome:box title="Confirmation">
      <div id="row">Investigator is saved succssfully</div>
     <table border="0" style="width:100%">
         <tr>
            <td>

              <div class="row">
                  <div class="label">First Name</div>
                  <div class="value">${investigatorCommand.firstName} </div>
              </div>
              <div class="row">
                  <div class="label">Middle Name</div>
                  <div class="value">${investigatorCommand.middleName} </div>
              </div>
              <div class="row">
                  <div class="label">Last Name</div>
                  <div class="value">${investigatorCommand.lastName} </div>
              </div>
              <div class="row">
                  <div class="label">Investigator Number</div>
                  <div class="value">${investigatorCommand.nciIdentifier} </div>
              </div>
           </td>
         <td style="vertical-align:top">
              <div class="row">
                  <div class="label">Email Address</div>
                  <div class="value">${investigatorCommand.emailAddress} </div>
              </div>
              <div class="row">
                  <div class="label">Phone</div>
                  <div class="value">${investigatorCommand.phoneNumber} </div>
              </div>
              <div class="row">
                  <div class="label">Fax</div>
                  <div class="value">${investigatorCommand.faxNumber} </div>
              </div>
        
        </td>
    </tr>
   </table>

  </chrome:box>

  </body>
</html>