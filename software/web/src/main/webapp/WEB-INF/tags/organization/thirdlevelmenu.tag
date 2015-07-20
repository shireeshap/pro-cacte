<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="selected" type="String" required="true" %>

<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/admin/createOrganization">
                <li id="thirdlevelnav-x" class="tab ${selected=='createOrganization'? 'selected' : ''}">
                    <div>
                        <a href="createOrganization"><tags:message code="organization.tab.createOrganization"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/admin/searchOrganization">
                <li id="thirdlevelnav-x" class="tab ${selected=='searchOrganization'? 'selected' : ''}">
                    <div>
                        <a href="searchOrganization"><tags:message code="organization.tab.searchOrganization"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>