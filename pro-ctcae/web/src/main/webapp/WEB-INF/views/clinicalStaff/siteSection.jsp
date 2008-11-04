<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:oneOrganization index="${index}" inputName="clinicalStaff.siteClinicalStaffs[${index}].organization"
                      title="Site" displayError="false"></tags:oneOrganization>

