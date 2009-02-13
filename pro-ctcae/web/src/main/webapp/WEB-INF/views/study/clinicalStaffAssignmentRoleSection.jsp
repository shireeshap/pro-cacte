<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="study" tagdir="/WEB-INF/tags/study" %>

<study:clinicalStaffAssignmentRole clinicalStaffAssignmentRole="${clinicalStaffAssignmentRole}"
                                            clinicalStaffAssignmentIndex="${clinicalStaffAssignmentIndex}"
                                            index="${clinicalStaffAssignmentRoleIndex}"/>
