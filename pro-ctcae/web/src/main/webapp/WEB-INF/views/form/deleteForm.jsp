<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<body>

<form:form method="post">
    <chrome:box title="form.label.delete_form">
        <chrome:division>
                    <div id="releaseForm">
                        <p>
                            You are about to delete

                            <strong>${command.crf.title}</strong>.

                        </p>

                        <p>
                            You cannot reverse this step. If you're not ready to proceed, please
                            <a href="javascript:closeReleaseFormWindow()">return
                                to
                                the search results</a>.
                        </p>

                     </div>
                    <br>
                    <div class="flow-buttons">

                        <input type="submit" id="flow-update"
                               class="next" value="Delete" alt="Save"
                               />


                        <input type="button" id="flow-cancel"
                               class="previous ibutton" value="Cancel" alt="Cancel"
                               onclick="closeReleaseFormWindow()"/>
                    </div>


        </chrome:division>
    </chrome:box>
</form:form>
</body>