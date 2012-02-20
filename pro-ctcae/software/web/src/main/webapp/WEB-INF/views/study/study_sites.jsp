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
    <tags:dwrJavascriptLink objects="organization"/>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>

    <script type="text/javascript">


        function addStudySiteDiv(transport) {
            $('studySiteTable').show()

            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }
        function addStudySite() {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudySite"/>", {
                onComplete:addStudySiteDiv,
                parameters:<tags:ajaxstandardparams/>,

                method:'get'
            })
        }
        function deleteStudySite(index) {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudySite"/>", {
                onComplete:function(transport) {
                    $('row-' + index).remove();

                },
                parameters:<tags:ajaxstandardparams/>+"&action=delete&siteIndexToRemove=" + index,

                method:'get'
            })
        }

        function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }

        Event.observe(window, "load", function() {
            <c:if test="${not empty command.study.studySites}">
            $('studySiteTable').show()
            </c:if>
        })
    </script>


</head>
<body>

<%--<chrome:box title="Study details">--%>

<%--</chrome:box>--%>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="true">

    <jsp:attribute name="singleFields">
         <chrome:division title="Study">
             <div class="row">
                 <div class="label"><spring:message code="form.label.study"/></div>
                 <div class="value">${command.study.shortTitle} </div>
             </div>
             <div class="row">
                 <div class="label"><spring:message code="study.label.assigned_identifier"/></div>
                 <div class="value">${command.study.assignedIdentifier} </div>
             </div>
         </chrome:division>
        <chrome:division title="Sites">
            <tags:instructions code="study.study_sites.top"/>
            <div align="left" style="margin-left: 50px">
                <table width="75%" class="tablecontent" style="display:none;" id="studySiteTable">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="95%" class="tableHeader"><spring:message
                                code='study.label.sites' text=''/></th>
                        <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>
                    </tr>
                    <c:forEach items="${command.study.studySites}" var="studySite" varStatus="status">
                        <c:if test="${not (studySite eq command.study.leadStudySite)}">
                            <tags:oneOrganization index="${status.index}"
                                                  inputName="study.studySites[${status.index}].organization"
                                                  title="Study Site" displayError="true"
                                                  required="true" readOnly="true"
                                                  studySite="${studySite}"/>
                        </c:if>
                    </c:forEach>
                    <tr id="hiddenDiv" align="center"></tr>
                </table>
                <br/>

                <div style="width:110px;">
                    <tags:button color="blue" markupWithTag="a" onclick="javascript:addStudySite()"
                                 value="study.button.add_study_site" icon="add" size="small"/>
                </div>
            </div>
        </chrome:division>
    </jsp:attribute>
</tags:tabForm>

</body>
</html>