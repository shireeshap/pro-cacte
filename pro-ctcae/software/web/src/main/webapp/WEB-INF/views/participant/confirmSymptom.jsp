<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table>

    <tr>
        <td>
            <div id="confirmSymptom">
                <div style="border:1px solid #ccc; height:65px; padding:9px; margin-bottom:10px;">
                    <img src="<chrome:imageUrl name="../blue/stop_sign_small.png" />" alt="Stop!"
                         style="float:left; margin-right:15px; margin-left:50px;"/>

                    <div style="font-size:20px; margin-bottom:5px;">
                        You have already answered a question for ${values}, are you sure that you want to also answer a
                        question for ${selectedChoice}?
                    </div>
                </div>
                <br/>
                <br/>

                <div class="flow-buttons">
                    <tags:button color="orange" type="button" id="flow-update"
                                 onclick="parent.addSymptom('${selectedChoice}');closeWindow();"
                                 cssClass="next" value="Yes" icon="check"
                            />
                    <tags:button color="blue" type="button" id="flow-cancel"
                                 cssClass="previous ibutton" value="No" icon="x"
                                 onclick="closeWindow()"/>
                </div>
        </td>
    </tr>
</table>
