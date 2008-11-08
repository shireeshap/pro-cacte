<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:crfSchedule index="${index}" inputName="studyParticipantAssignment.studyParticipantCrfs[${index}]"
                  title="Crf Schedule" displayError="false"></tags:crfSchedule>


