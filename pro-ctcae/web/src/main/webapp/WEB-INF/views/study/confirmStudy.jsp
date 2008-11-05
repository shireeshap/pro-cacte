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
            width: 25em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
            width: 25em;

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
<chrome:flashMessage flashMessage="The Study was saved successfully"></chrome:flashMessage>

<chrome:box title="Study details">

    <chrome:division>
        <div class="leftpanel">
            <div class="row">
                <div class="label">Assigned identifier</div>
                <div class="value">${command.study.assignedIdentifier} </div>
            </div>
            <div class="row">
                <div class="label">Short title</div>
                <div class="value">${command.study.shortTitle} </div>
            </div>
            <div class="row">
                <div class="label">Long title</div>
                <div class="value">${command.study.longTitle} </div>
            </div>

            <div class="row">
                <div class="label">Description</div>
                <div class="value">${command.study.description} </div>
            </div>
            <div class="row">
                <div class="label">Funding sponsor</div>
                <div class="value">${command.study.studyFundingSponsor.organization.displayName} </div>
            </div>
            <div class="row">
                <div class="label">Coordinating center</div>
                <div class="value">${command.study.studyCoordinatingCenter.organization.displayName} </div>
            </div>
        </div>
    </chrome:division>
    <chrome:division title="Study sites">

        <div align="left" style="margin-left: 50px">
            <table width="55%" class="tablecontent">
                <tr id="ss-table-head" class="amendment-table-head">
                    <th width="95%" class="tableHeader">Sites</th>
                    <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                </tr>

                <tr>

                    <td style="border-right:none;">
                        <c:forEach items="${command.study.studySites}" var="studySite">
                            <div class="row">
                                    ${studySite.organization.displayName}
                            </div>
                        </c:forEach>

                    </td>


                    <td style="border-left:none;">


                    </td>
                </tr>


            </table>

        </div>


    </chrome:division>


</chrome:box>

</body>
</html>
