<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="report" tagdir="/WEB-INF/tags/reports" %>
<html>
<head>
    <tags:dwrJavascriptLink objects="crf"/>
    <tags:javascriptLink name="reports_common1"/>
    <tags:javascriptLink name="table_menu"/>
    <tags:stylesheetLink name="table_menu"/>
    <tags:includePrototypeWindow/>
    <tags:includeScriptaculous/>
    <script type="text/javascript">
        var showResultsInPopUpFlag = false;
        function reportResults(attributes, group, arms) {
            if (!performValidations()) {
                return;
            }
            showIndicator();
            var request = new Ajax.Request("${url}", {
                parameters:getQueryString(attributes, group, arms),
                onComplete:function(transport) {
                    if (showResultsInPopUpFlag) {
                        showResultsInPopUp(transport);
                    } else {
                        showResults(transport);
                    }
                    hideIndicator();
                },
                method:'get'
            }
                    )
        }

        function showText(obj) {
            var val = obj.value;
            if (val == '') {
                $('filterByValue').hide();
                $('filterByValueHelp').hide();
            } else {
                $('filterByValue').show();
                $('filterByValueHelp').show();
            }
        }
    </script>
</head>
<body>
<report:thirdlevelmenu selected="${param['rt']}"/>
<report:reportSpecificJS selected="${param['rt']}"/>
<chrome:box title="participant.label.search_criteria">
    <div align="left" style="margin-left: 50px">
        <c:choose>
            <c:when test="${study ne null}">
                <div class="row">
                    <div class="label"><tags:message code="reports.label.study"/></div>
                    <div class="value">${study.displayName}</div>
                </div>
                <input type="hidden" id="study" name="study" value="${study.id}"/>
            </c:when>
            <c:otherwise>
                <tags:renderAutocompleter propertyName="study" displayName="Study" required="true" size="100"
                                          noForm="true"/>
                <script type="text/javascript">
                    createStudyAutoCompleter();
                </script>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${crfs ne null}">
                <c:choose>
                    <c:when test="${fn:length(crfs)==1}">
                        <div class="row">
                            <div class="label"><tags:message code="reports.label.form"/></div>
                            <div class="value">${crfs[0].title}</div>
                            <input type="hidden" name="form" id="form" value="${crf.id}" title="Form"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row">
                            <div class="label"><tags:message code="reports.label.form"/></div>
                            <div class="value">
                                <select onchange="javascript:displaySymptoms(this.value)" name="form" id="form">
                                    <option value="">Please select</option>
                                    <option value="-1">All</option>
                                    <c:forEach items="${crfs}" var="crf">
                                        <option value="${crf.id}">${crf.title}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <div class="row" id="divFormRow" style="display:none">
                    <div class="label"><tags:message code="reports.label.form"/></div>
                    <div class="value" id="formTitle"></div>
                    <input type="hidden" name="form" id="form" value="" title="Form"/>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${proctcterms ne null}">
                <div class="row">
                    <div class="label"><tags:message code="reports.label.symptoms"/></div>
                    <div class="value">
                        <select id="proCtcTermsSelect" title="symptom">
                                <%--<select multiple size="4">--%>
                            <option value="All">All</option>
                            <c:forEach items="${proctcterms}" var="proctcterm">
                                <option value="${proctcterm.id}">${proctcterm.term}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row" id="divSymptomsRow" style="display:none">
                    <div class="label"><tags:message code="reports.label.symptoms"/></div>
                    <div class="value" id="proCtcTerms"></div>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${studySite ne null}">
                <div class="row">
                    <div class="label"><tags:message code="reports.label.site"/></div>
                    <div class="value">${studySite.displayName}</div>
                </div>
                <input type="hidden" id="studySite" name="studySite" value="${studySite.id}"/>
            </c:when>
            <c:otherwise>
                <div>
                    <input type="hidden" id="studySite" name="studySite" value="" title="Study site"/>

                    <div id="studySiteAutoCompleter" style="display:none">
                        <tags:renderAutocompleter
                                propertyName="studySite" displayName="Study site" size="60"
                                noForm="true"/>
                    </div>
                    <div class="row" id="divStudySiteRow" style="display:none">
                        <div class="label"><tags:message code="reports.label.site"/></div>
                        <div class="value" id="studySiteDisplayName"></div>
                    </div>
                </div>
                <c:if test="${study ne null}">
                    <script type="text/javascript">
                        displaySites();
                    </script>
                </c:if>
            </c:otherwise>
        </c:choose>
        <div class="row" id="filterByDiv" style="display:none">
            <div class="label"><tags:message code="reports.label.FilterBy"/></div>
            <div class="value">
                <select id="filterBy" title="Filter By" onchange="showText(this);">
                    <option value="">None</option>
                    <option value="cycle">Cycle</option>
                    <option value="week">Week</option>
                    <option value="month">Month</option>
                </select>
                <input type="text" name="filterByValue" id="filterByValue" style="display:none" size="8"
                       title="Filter By"/>

                <div style="font-size:11px;display:none" id="filterByValueHelp"> (You can either specify a range of
                    values using dash, for ex. 2-5, or separate
                    values using comma, for ex. 1,5,8)
                </div>
            </div>
        </div>
        <div id="search" class="row" style="display:none">
            <div class="value">
                <tags:button color="blue" value="Search" onclick="resetPopUpFlagAndCallResults();" size="big"
                             icon="search"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>
    </div>
</chrome:box>
<div id="reportOuterDiv" style="display:none;">
    <div>
        <div id="reportInnerDiv"/>
    </div>
</div>

</body>
</html>