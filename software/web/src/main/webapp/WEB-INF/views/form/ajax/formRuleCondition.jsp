<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:formRuleCondition ruleIndex="${ruleIndex}" ruleConditionIndex="${ruleConditionIndex}"
                        condition="${condition}" operators="${operators}"
                        questionTypes="${questionTypes}" 
                        crfId="${crfId}"/>


