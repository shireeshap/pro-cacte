
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

        Event.observe(window, "load", function() {
        <c:if test="${not empty command.study.studySites}">
            $('studySiteTable').show()
        </c:if>
        })
    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" willSave="true">
    <jsp:attribute name="singleFields">
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
    </jsp:attribute>
</tags:tabForm>

</body>
</html>