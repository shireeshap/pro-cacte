<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
      <script type="text/javascript">
          var url = '<c:url value="/pages/participant/schedulecrf" />';
          window.parent.location = url + '?pId=${pId}';
      </script>
  </head>
  <body></body>
</html>