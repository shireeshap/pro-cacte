<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<head>
    <tags:formBuilder/>
    <tags:formActionMenu/>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:stylesheetLink name="table_menu"/>

    <tags:includePrototypeWindow/>
    <tags:dwrJavascriptLink objects="crf"/>

    <style type="text/css">
        .even {
            background-color: #ffffff;
        }

        a.fg-button {
            float: left;
        }

        * {
            zoom: 1;
        }
        td.header-top {
            background-color: #CCCCCC;
            font-weight: bold;
            text-align: left;
}
        td.data {
              text-align: left;
        }
    </style>
    <!--[if IE]>
        <style>
            div.row div.value {
                margin-left:7px;
            }
        </style>
    <![endif]-->
    <script type="text/javascript">
        Event.observe(window, "load", function () {
            var sac = new studyAutoCompleter('study');
            acCreateStudy(sac);

        <c:if test="${study ne null}">
            initializeAutoCompleter('study',
                    '${study.displayName}', '${study.id}')
        </c:if>
            initSearchField();
        });


        function buildTable() {
            var id = $('study').value
            var url = window.location.href.substring(0, window.location.href.indexOf('?'));
            window.location.href = url + "?studyId=" + id;
        }

        function acCreateStudy(mode) {
            new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
                    mode.populator, {
                valueSelector: mode.valueSelector,
                afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                    acPostSelect(mode, selectedChoice);
                    buildTable();
                },
                indicator: mode.basename + "-indicator"
            })

        }


        function showVersionForm(crfId) {
            var request = new Ajax.Request("<c:url value="/pages/form/showVersionForm"/>", {
                parameters:<tags:ajaxstandardparams/>+"&crfId=" + crfId,
                onComplete:function(transport) {
                    var response = transport.responseText;
                    new Insertion.After('details_row_' + crfId, response);
                    $('crfVersionShowImage_' + crfId).hide();
                    $('crfVersionHideImage_' + crfId).show();
                },
                method:'get'
            })
        }
        function hideVersionForm(crfId) {
            $('crfVersionShowImage_' + crfId).show();
            $('crfVersionHideImage_' + crfId).hide();
            $$('tr.childTableRow_' + crfId).each(function(item) {
                item.remove();
            });
        }


    </script>

</head>
<body>
<chrome:box title="form.box.select_study" id="study-entry">
    <div align="left" style="margin-left: 50px">
        <tags:instructions code="instruction_select_study"/>

        <tags:renderAutocompleter propertyName="study"
                                  displayName="Study"
                                  required="true"
                                  size="100"
                                  noForm="true"/>

        <c:if test="${crfs ne null}">
            You have selected the study ${study.shortTitle}.
        </c:if>
        <br>
    </div>
</chrome:box>
<c:if test="${crfs ne null}">
    <div id="noForm">
        <proctcae:urlAuthorize url="/pages/form/basicForm">
            <table width="100%">
                <tr>
                    <td>
                        <tags:button color="blue" markupWithTag="a" id="newFormUrl" icon="add" value="New Form"
                                     href="basicForm?studyId=${study.id}"/>
                    </td>
                    <td align="right">
                        <tags:button color="blue" markupWithTag="a" id="hiddenForms" value="View hidden forms" size="small"
                                     href="hiddenForms?studyId=${study.id}"/>
                    </td>
                </tr>
            </table>
            <%--<tags:instructions code="form.manage.instructions"/>--%>
        </proctcae:urlAuthorize>
    </div>
    <br/>
    <table class="widget" cellspacing="0" align="left">
        <tr>
            <td class="header-top"></td>
            <td class="header-top">
                Title
            </td>
            <td class="header-top">
                Version
            </td>
            <td class="header-top">
                Effective Date
            </td>
            <td class="header-top">
                Status
            </td>
            <td class="header-top"></td>
        </tr>
        <c:forEach items="${crfs}" var="crf" varStatus="status">
            <tr id="details_row_${crf.id}">
                <td class="data">
                    <c:if test="${crf.parentCrf ne null}">
                        <a href="javascript:showVersionForm('${crf.id}')"><img id="crfVersionShowImage_${crf.id}"
                                                                               src="../../images/arrow-right.png"
                                                                               style=""/></a>
                        <a href="javascript:hideVersionForm('${crf.id}')"><img id="crfVersionHideImage_${crf.id}"
                                                                               src="../../images/arrow-down.png"
                                                                               style="display:none"/></a>
                    </c:if>
                </td>
                <td class="data">
                        ${crf.title}
                </td>
                <td class="data">
                        ${crf.crfVersion}
                </td>
                <td class="data">
                    <tags:formatDate value="${crf.effectiveStartDate}"/>
                </td>
                <td class="data">
                    <c:choose>
                        <c:when test="${crf.status eq 'RELEASED'}">Final</c:when>
                        <c:otherwise>
                            ${crf.status}
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"
                       id="crfActions${crf.id}"><span class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>
                    <script>showPopUpMenu('${crf.id}', '${crf.status}');</script>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>