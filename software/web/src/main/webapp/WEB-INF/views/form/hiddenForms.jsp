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
            float: right;
        }

        * {
            zoom: 1;
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
         function showForm(crfId) {
        var request = new Ajax.Request("<c:url value="/pages/form/showForm"/>", {
            parameters:<tags:ajaxstandardparams/>+"&crfId=" + crfId ,
            onComplete:function(transport) {
                showConfirmationWindow(transport);
            },
            method:'get'
        })
    }

    </script>

</head>
<body>

<c:if test="${crfs ne null}">
    <table class="widget" cellspacing="0" align="center">
        <tr>
            <td class="header-top"></td>
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
        </tr>
        <c:forEach items="${crfs}" var="crf" varStatus="status">
            <tr id="details_row_${crf.id}">
                <td align="right">
                    <%--<a class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all"--%>
                       <%--id="crfActions${crf.id}"><span class="ui-icon ui-icon-triangle-1-s"></span>Actions</a>--%>
                    <%--<script>showPopUpMenu('${crf.id}', '${crf.status}');</script>--%>
                    <tags:button color="blue" id="hiddenForms" value="Show form" size="small" onclick="javascript:showForm('${crf.id}')"
                                    />
                </td>
                <td class="data">
                    <%--<c:if test="${crf.parentCrf ne null}">--%>
                        <%--<a href="javascript:showVersionForm('${crf.id}')"><img id="crfVersionShowImage_${crf.id}"--%>
                                                                               <%--src="../../images/arrow-right.png"--%>
                                                                               <%--style=""/></a>--%>
                        <%--<a href="javascript:hideVersionForm('${crf.id}')"><img id="crfVersionHideImage_${crf.id}"--%>
                                                                               <%--src="../../images/arrow-down.png"--%>
                                                                               <%--style="display:none"/></a>--%>
                    <%--</c:if>--%>

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
                            ${crf.status.displayName}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

</body>