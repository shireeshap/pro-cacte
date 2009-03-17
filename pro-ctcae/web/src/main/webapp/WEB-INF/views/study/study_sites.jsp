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
                parameters:"subview=subview&",

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

<tags:tabForm tab="${tab}" flow="${flow}" willSave="true">

    <jsp:attribute name="repeatingFields">

        <chrome:division>
            <p><tags:instructions code="study.study_sites.top"/></p>

            <input type="hidden" value="" id="objectsIdsToRemove" name="objectsIdsToRemove"/>

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
                                              title="Study Site" displayError="true"
                                              required="true"></tags:oneOrganization>
                    </c:forEach>


                    <tr id="hiddenDiv" align="center"></tr>

                </table>


            </div>


        </chrome:division>
            <div class="local-buttons">

                <tags:button color="blue" markupWithTag="a" onclick="javascript:addStudySite()"
                             value="study.button.add_study_site"/>
            </div>
        <br>
                  <br>
        <br>
                  <br>
                            
    </jsp:attribute>

</tags:tabForm>

</body>
</html>