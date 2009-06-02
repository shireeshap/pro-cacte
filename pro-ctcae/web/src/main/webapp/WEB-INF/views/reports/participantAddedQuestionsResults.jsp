<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <style type="text/css">
        .link {
            font-family: Verdana, Arial, Helvetica, sans-serif;
            font-size: 11px;
            font-weight: normal;
            color: #333333;
        }

        .link:hover {
            font-weight: normal;
            color: #6666ff;
        }

        table.widget {
            border: 1px solid #eaeaea;
            border-collapse: collapse;
            width: 100%;
        }

        .highlight td {
            background-color: #6666ff;
            color: white;
        }

        table.widget tr {
            border-bottom: 1px #999999 solid;
        }

        td.data {
            white-space: nowrap;
            text-align: center;
        }

        td.header-top {
            font-weight: bold;
            text-align: center;
            background-color: #cccccc;
        }

        .shadowB {
            background: #FAFAFA url( ../../images/allCommon-Icons.gif ) repeat-x scroll 100% 100%;
            border: 0 none;
            padding-bottom: 4px;
        }

        .ddnotediv {
            background-color: #F5F5F5; /*border: 2px solid #333333;*/
        /*border-top: 1px solid #F5F5F5;*/
            border-right: 3px solid #333333;
            border-bottom: 3px solid #333333; /*border-left: 1px solid #F5F5F5;*/
            padding: 7px;
            position: absolute;
            text-align: left;
        }

        .shadowr {
            background: #FAFAFA url( ../../images/allCommon-Icons.gif ) repeat-y scroll 100% -92px;
            padding-right: 4px;
        }

        .indIcon {
            padding-left: 10px;
            padding-right: 10px;
            position: relative;
            visibility: hidden;
            white-space: nowrap;
            cursor: pointer;
        }
    </style>
</head>
<body>
<chrome:box title="Report">
    <br/>
    <table class="widget" cellspacing="0" align="center">
        <tr>
            <td class="header-top"></td>
            <td class="header-top">
                Symptom
            </td>
            <td class="header-top">
                Number of times reported
            </td>
            <c:forEach items="${results}" var="lineitem">
        <tr id="details_row_${status.index}">
            <td align="right">
                <div id="img_${status.index}" class="indIcon"
                     onclick="showPopUpMenu('${status.index}','${participant.id}','${schedule.id}',-105,-130)">
                    <img src="../../images/menu.png" alt=""/>
                </div>
            </td>
            <td class="data">
                    ${lineitem[0]}
            </td>
            <td class="data">
                    ${lineitem[1]}
            </td>
        </tr>
        </c:forEach>
    </table>
    <br/>
</chrome:box>
<div id="dropnoteDiv" class="ddnotediv shadowB" style="display:none;left:0;top:0">
    <div id="dropnoteinnerDiv" class="shadowr">
    </div>
</div>
</body>
</html>



