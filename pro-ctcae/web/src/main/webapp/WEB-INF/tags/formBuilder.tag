<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    function releaseForm(crfId) {

        var request = new Ajax.Request("<c:url value="/pages/form/releaseForm"/>", {
            parameters:"crfId=" + crfId + "&subview=subview",
            onComplete:function(transport) {
                showConfirmationWindow(transport, 650, 280);
            },
            method:'get'
        })

    }

</script>
