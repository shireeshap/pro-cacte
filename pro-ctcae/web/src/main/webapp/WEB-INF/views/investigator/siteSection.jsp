<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:oneOrganization index="${index}" inputName="investigator.siteInvestigators[${index}].organization"
                      title="Study Site" displayError="false"></tags:oneOrganization>

