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
        function addStudySiteClinicalStaffDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }

        function addStudySiteClinicalStaff(studySiteIndex) {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudyComponent"/>", {
                onComplete:addStudySiteClinicalStaffDiv,
                parameters:"subview=subview&componentTyep=studySiteClinicalStaff&studySiteIndex="+studySiteIndex,
                method:'get'
            })
        }


        function fireDelete(index, divToRemove) {
            if ($('objectsIdsToRemove').value != '') {
                $('objectsIdsToRemove').value = $('objectsIdsToRemove').value + "," + index;
            }
            else {
                $('objectsIdsToRemove').value = index;
            }

            $(divToRemove).remove();

        }


        Event.observe(window, "load", function() {


        <c:forEach  items="${command.study.studySites}" var="studySite" varStatus="status">
            var siteBaseName = 'study.studySites[${status.index}].organization'
            acCreate(new siteAutoComplter(siteBaseName))
            initializeAutoCompleter(siteBaseName, '${studySite.organization.displayName}', '${studySite.organization.id}');
        </c:forEach>
        <c:if test="${not empty command.study.studySites}">
            $('studySiteTable').show()

        </c:if>
            initSearchField()


        })


    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}">

    <jsp:attribute name="singleFields">

            <p><tags:instructions code="study.study_investigators.top"/></p>

    <tags:renderSelect options="${sites}" noForm="${true}"/>
    </jsp:attribute>


    <jsp:attribute name="repeatingFields">

        <chrome:division>


            <div align="left" style="margin-left: 50px">
                <table width="55%" class="tablecontent" style="display:none;" id="studySiteTable">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="95%" class="tableHeader"><tags:requiredIndicator/><spring:message
                                code='study.label.sites' text=''/></th>
                        <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                    </tr>
                    <c:forEach items="${command.study.studySites}" var="studySite" varStatus="status">

                        <tags:oneOrganization index="${status.index}"
                                              inputName="study.studySites[${status.index}].organization"
                                              title="Study Site" displayError="true"></tags:oneOrganization>
                    </c:forEach>


                    <tr id="hiddenDiv" align="center"></tr>

                </table>

            </div>


        </chrome:division>
    </jsp:attribute>
    <jsp:attribute name="localButtons">

        <tags:button type="anchor" onClick="javascript:addStudySiteClinicalStaff()"
                     value="study.button.add_investigator"/>

    </jsp:attribute>
</tags:tabForm>

</body>
</html>