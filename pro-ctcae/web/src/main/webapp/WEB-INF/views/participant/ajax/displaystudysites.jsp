<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
    .tableHeader {
        background-color: #2B4186;
        background-image: url( /ctcae/images/blue/eXtableheader_bg.png );
        background-position: center top;
        background-repeat: repeat-x;
        color: white;
        font-size: 13px;
        font-weight: bold;
        margin: 0;
        padding: 4px 3px;
        text-align: left;
    }

    table.widget {
        width: 100%;
        background-color: #e9e8e8;
        border-top: 1px solid #999999;
        border-right: 1px solid #999999;
    }

    td.data {
        vertical-align: top;
        border-bottom: 1px solid #999999;
        border-left: 1px solid #999999;
    }
</style>
<table cellpadding="0" width="100%">
    <tr>
        <td class="tableHeader">
            Study identifier
        </td>
        <td class="tableHeader">
            Short title
        </td>
        <td class="tableHeader">
            Funding sponsor
        </td>
        <td class="tableHeader">
            Coordinating center
        </td>
        <td class="tableHeader">
            Patient study identifier
        </td>
    </tr>

    <c:forEach items="${studysites}" var="studysite">
        <c:forEach items="${studysite.study.crfs}" var="crf">
            <c:if test="${crf.status eq 'Released' and crf.nextVersionId eq null}">
                <c:set var="hasforms" value="true"/>
            </c:if>
        </c:forEach>
        <tr>
            <td>

                <input type="checkbox" name="studySite" value="${studysite.id}"
                       onchange="javascript:showForms(this, '${studysite.id}')"> ${studysite.study.assignedIdentifier}
            </td>
            <td>
                    ${studysite.study.shortTitle}
            </td>
            <td>
                    ${studysite.study.studyFundingSponsor.organization.nciInstituteCode}
            </td>
            <td>
                    ${studysite.study.studyCoordinatingCenter.organization.nciInstituteCode}
            </td>
            <td>
                <input type="text" name="participantStudyIdentifier_${studysite.id}" value="">
            </td>
        </tr>
        <c:if test="${hasforms eq 'true'}">
            <c:set var="hasforms" value="false"/>
            <tr id="forms_${studysite.id}" style="display:none">
                <td>
                    &nbsp;
                </td>
                <td colspan="4">
                    <table class="widget" cellspacing="0">
                        <c:forEach items="${studysite.study.crfs}" var="crf">
                            <c:if test="${crf.status eq 'Released' and crf.nextVersionId eq null}">
                                <tr>
                                    <td class="data" width="50%" align="left">
                                        &nbsp;&nbsp;<b>Form: </b>${crf.title}
                                    </td>
                                        <%--<td class="data">--%>
                                        <%--<tags:formatDate value="${crf.effectiveStartDate}"/>--%>
                                        <%--</td>--%>
                                    <td class="data" align="center">
                                        Start Date <tags:renderDate propertyName="form_date_${crf.id}" doNotshowLabel="true"
                                                         noForm="true"/>
                                    </td>

                                </tr>
                            </c:if>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </c:if>
    </c:forEach>
</table>