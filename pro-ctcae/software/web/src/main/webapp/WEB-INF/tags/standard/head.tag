<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<meta http-equiv="X-UA-Compatible" content="text/html;charset=utf-8;IE=EmulateIE7">
<tags:javascriptLink name="prototype"/>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/prototype/1.7.0.0/prototype.js"></script>
 <jwr:style src="/csslib/proctcae.zcss" /> 
 <jwr:script src="/jslib/proctcae.zjs"/> 


<link rel="stylesheet" type="text/css" href="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/paginator/assets/skins/sam/paginator.css">

<!--[if lt IE 8]>
<tags:stylesheetLink name="ie7"/>
<![endif]-->

<!--[if gt IE 7]>
<tags:stylesheetLink name="ie"/>
<![endif]-->

<!-- Including css that is not compatible with jawr -->



<tags:dwrJavascriptLink objects="organization"/>
<tags:dwrJavascriptLink objects="study"/>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/event/event-min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/connection/connection-min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/dom/dom-min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/element/element-min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/paginator/paginator-min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/yui/2.9.0/build/datatable/datatable-min.js"></script>

<script>
	jQuery.noConflict();
</script>


