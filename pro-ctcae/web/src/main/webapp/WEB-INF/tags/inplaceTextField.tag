<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/ctcae/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@attribute name="path" %>
<%@attribute name="size" %>
<%@attribute name="validations" %>
<tags:includeScriptaculous/>

<c:set var="nPath" value="${fn:replace(path, '[', '_')}"/>
<c:set var="nPath" value="${fn:replace(nPath, ']', '_')}"/>
<c:set var="nPath" value="${fn:replace(nPath, '.', '_')}"/>

<span id="${nPath}-id"><ctcae:value path="${path}"/></span>
<script type="text/javascript">

    var symbol = '&';
    if ($("command").action.indexOf('?') == -1) symbol = '?';
    var editor_${nPath} = new Ajax.InPlaceEditor('${nPath}-id',
            '/ctcae/pages/form/setName' + symbol + 'subview',
    {   validations:'${validations}',
        cancelLink:false,
        cancelButton:true,
        okText:'ok',
        cancelText:'cancel',
        highlightcolor: 'lavender',
        callback: function(form, value) {
            return '_asynchronous=true&_asyncMethodName=doInPlaceEdit&_ajaxInPlaceEditParam=${path}&_pathToGet=${path}&${path}=' + escape(value);
        }
    });
</script>