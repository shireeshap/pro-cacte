<%@ attribute name="studyParticipantAssignment" type="gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment" %>
<%@ attribute name="isEdit" %>
<%@ attribute name="selected" %>
<%@ tag import="java.util.Date" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="studysite" type="gov.nih.nci.ctcae.core.domain.StudySite" required="true" %>

<c:forEach items="${studysite.study.crfs}" var="crf">
    <c:if test="${crf.status eq 'Final' and crf.childCrf eq null}">
        <c:set var="hasforms" value="true"/>
    </c:if>
</c:forEach>
<tr>
    <td>

        <input type="radio" name="studySites" value="${studysite.id}"
               onclick="javascript:showForms(this, '${studysite.id}')"
               <c:if test="${selected}">checked</c:if>/>
    </td>
    <td>
        ${studysite.study.displayName}
    </td>
    <c:if test="${isEdit}">
        <c:choose>
            <c:when test="${studyParticipantAssignment.offTreatmentDate ne null}">
                <tags:formatDate value="${studyParticipantAssignment.offTreatmentDate}"/><br>
            </c:when>
            <c:otherwise>
                <a href="javascript:participantOffStudy(${studyParticipantAssignment.id})">Off study date...</a> <br>
                <c:if test="${studyParticipantAssignment.onHoldTreatmentDate eq null}">
                    <a href="javascript:participantOnHold(${studyParticipantAssignment.id})">Treatment on hold</a> <br>
                </c:if>
                <c:if test="${studyParticipantAssignment.onHoldTreatmentDate ne null}">
                    Treatment on-hold from <tags:formatDate
                        value="${studyParticipantAssignment.onHoldTreatmentDate}"/><br>
                    <a href="javascript:participantOffHold(${studyParticipantAssignment.id})"> Put participant on
                        treatment </a>
                </c:if>
                <c:if test="${studyParticipantAssignment.offHoldTreatmentDate ne null}">
                    Treatment hold removed from <tags:formatDate
                        value="${studyParticipantAssignment.offHoldTreatmentDate}"/> <br>
                </c:if>
            </c:otherwise>
        </c:choose>

    </c:if>
</tr>
<tr id="subform_${studysite.id}" <c:if test="${not selected}">style="display:none"</c:if>>
    <td></td>
    <td>
        <table class="widget" cellspacing="0">
            <tr>
                <td class="data" width="30%" align="right">
                    <span class="required-indicator">*&nbsp;&nbsp;</span><b> Participant study identifier</b>
                </td>
                <td class="data">
                    <input type="text"
                           name="participantStudyIdentifier_${studysite.id}"
                           value="${studyParticipantAssignment.studyParticipantIdentifier}"
                           title="identifier"
                           id="participantStudyIdentifier_${studysite.id}"/>

                </td>
            </tr>
            <tr>
                <c:if test="${fn:length(studysite.study.nonDefaultArms) > 0}">
                    <c:choose>
                        <c:when test="${fn:length(studysite.study.nonDefaultArms) > 1}">
                            <td align="right" class="data">
                                <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
                                        code="study.label.arm"/></b>
                            </td>
                            <td class="data">
                                <select name="arm_${studysite.id}" title="arm"
                                        id="arm_${studysite.id}">
                                    <option value="">Please select</option>
                                    <c:forEach items="${studysite.study.nonDefaultArms}" var="arm">
                                        <option value="${arm.id}" <c:if
                                                test="${studyParticipantAssignment.arm.id eq arm.id}">selected</c:if>>
                                                ${arm.title}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="right" class="data">
                                <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
                                        code="study.label.arm"/></b>
                            </td>
                            <td class="data">
                                <input type="hidden" name="arm_${studysite.id}"
                                       value="${studysite.study.nonDefaultArms[0].id}">${studysite.study.nonDefaultArms[0].title}
                            </td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </tr>
            <%--<tr>--%>
                <%--<td align="right" class="data">--%>
                    <%--<b>Patient self reporting option</b>--%>
                <%--</td>--%>
                <%--working checkbox implementation--%>
                <%--<td>--%>
                <%--<table>--%>
                <%--<c:forEach items="${studysite.study.studyModes}" var="studyMode">--%>
                <%--<c:if test="${studyMode.mode.displayName eq 'Clinic'}">--%>
                <%----%>
                <%--<tr>--%>
                <%--<td>--%>
                <%--<tags:renderCheckBox propertyName="participantModes"--%>
                <%--values="${studyParticipantAssignment.selectedAppModes}"--%>
                <%--displayName="${studyMode.mode.displayName}"--%>
                <%--propertyValue="${studyMode.mode.code}" noForm="true"--%>
                <%--useRenderInput="true"/>--%>
                <%----%>
                <%--</td>--%>
                <%--<td>--%>
                <%--&nbsp;&nbsp;&nbsp;(no reminder will be sent)--%>
                <%--</td>--%>
                <%--</tr>--%>
                <%--</c:if>--%>
                <%--<c:if test="${studyMode.mode.displayName eq 'Booklet'}">--%>
                <%--<tr>--%>
                <%--<td>--%>
                <%--<tags:renderCheckBox propertyName="participantModes"--%>
                <%--values="${studyParticipantAssignment.selectedAppModes}"--%>
                <%--displayName="${studyMode.mode.displayName}"--%>
                <%--propertyValue="${studyMode.mode.code}" noForm="true"--%>
                <%--useRenderInput="true"/>--%>
                <%--</td>--%>
                <%--<td>--%>
                <%--&nbsp;&nbsp;&nbsp;(no reminder will be sent)--%>
                <%--</td>--%>
                <%--</tr>--%>
                <%--</c:if>--%>
                <%----%>
                <%--<c:if test="${studyMode.mode.displayName eq 'Web' || studyMode.mode.displayName eq 'IVRS'}"  >--%>
                <%--<tr>--%>
                <%--<td>--%>
                <%--<tags:renderCheckBox propertyName="participantModes"--%>
                <%--values="${studyParticipantAssignment.selectedAppModes}"--%>
                <%--displayName="${studyMode.mode.displayName}"--%>
                <%--propertyValue="${studyMode.mode.code}" noForm="true"--%>
                <%--useRenderInput="true" onclick="javascript:showOrHideEmail(this.checked);"/>--%>
                <%--</td>--%>
                <%--<td>--%>
                <%--<c:if test="${studyMode.mode.displayName eq 'Web'}">--%>
                <%--<input type="checkbox" name="email" value="true"--%>
                <%--id="email" ${studyParticipantAssignment.studyParticipantModes[0].email ? "checked" : " "}/> notify via email--%>
                <%--<input type="hidden" name="_${propertyName}" value="on">--%>
                <%--</c:if>--%>
                <%--<c:if test="${studyMode.mode.displayName eq 'IVRS'}">--%>
                <%--<input type="checkbox" name="call" value="true"--%>
                <%--id="call" ${studyParticipantAssignment.studyParticipantModes[0].call ? "checked" : " "}/> notify via phone call &nbsp;&nbsp;&nbsp;--%>
                <%--<input type="hidden" name="_${propertyName}" value="on">--%>
                <%--<input type="checkbox" name="text" value="true"--%>
                <%--id="text" ${studyParticipantAssignment.studyParticipantModes[0].text ? "checked" : " "}/> notify via text--%>
                <%--<input type="hidden" name="_${propertyName}" value="on">--%>
                <%--</c:if>--%>
                <%----%>
                <%--</td>--%>
                <%--</tr>--%>
                <%--</c:if>--%>
                <%--</c:forEach>--%>
                <%--</table>--%>


                <%--<c:forEach items="${studysite.study.studyModes}" var="studyMode">--%>
                <%--<tags:renderCheckBox propertyName="participantModes"--%>
                <%--values="${studyParticipantAssignment.selectedAppModes}"--%>
                <%--displayName="${studyMode.mode.displayName}"--%>
                <%--propertyValue="${studyMode.mode.code}" noForm="true"--%>
                <%--useRenderInput="true"/>--%>
                <%----%>
                <%--</c:forEach>--%>

                <%--<tags:renderCheckBox propertyName="email" propertyValue="true" noForm="true" useRenderInput="true"/> notify via email --%>
                <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td align="right" class="data">
                    <b><spring:message code="participant.label.startdate"/></b>
                </td>
                <td class="data">
                    <c:choose>
                        <c:when test="${selected}">
                            <tags:renderDate
                                    propertyName="study_date_${studysite.id}"
                                    doNotshowLabel="true" required="true"
                                    noForm="true" dateValue="${studyParticipantAssignment.studyStartDate}"/>
                        </c:when>
                        <c:otherwise>
                            <tags:renderDate
                                    propertyName="study_date_${studysite.id}"
                                    doNotshowLabel="true" required="true"
                                    noForm="true" dateValue="<%= new Date()%>"/>
                        </c:otherwise>
                    </c:choose>
                </td>

            </tr>
            <!-- for demo -->
            <tr>
                <td align="right" class="data">
                    <b> Patient home reporting options </b>
                </td>
                <td width="10%">
                    <input type="radio" name="Web" value="Web" onclick="showOrHideEmail(this.checked);">Web
                    <br>
                    <input type="radio" name="Web" value="IVRS" onclick="showOrHideEmail(this.checked);">IVRS
                </td>
                <td>
                    <input type="checkbox" name="email" value="email">reminder via email <br>
                    <input type="checkbox" name="text" value="text" onclick="phoneRequired(this.checked);">reminder via
                    text message &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" name="text" value="text" onclick="phoneRequired(this.checked);">reminder via
                    phone call
                </td>
            </tr>
            <tr>
                <td align="right" class="data">
                    <b> Patient in-clinic reporting options </b>
                </td>
                <td >
                    <input type="radio" name="clinic" value="Web">In-clinic
                </td>
            </tr>

            <c:if test="${hasforms eq 'true'}">
                <c:set var="hasforms" value="false"/>
                <c:forEach items="${studysite.study.crfs}" var="crf">
                    <c:if test="${crf.status eq 'Final' and crf.childCrf eq null}">
                        <tr>
                            <td align="right" class="data">
                                <b><spring:message code="form.tab.form"/></b>
                            </td>
                            <td class="data">
                                    ${crf.title}
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </c:if>
        </table>
    </td>
</tr>