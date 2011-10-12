<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>

<%@taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<html>
<head>

    <link href="calendar-blue.css" rel="stylesheet" type="text/css"/>
    <tags:dwrJavascriptLink objects="searchUniversity"/>
    <tags:includePrototypeWindow></tags:includePrototypeWindow>


    <script language="JavaScript" type="text/JavaScript">


        var universityAutocompleterProps = {
            basename: "user.contactInformation.university",
            populator:    function(autocompleter, text) {
                searchUniversity.matchUniversity(text, function(values) {
                    autocompleter.setChoices(values)
                })
            },
            valueSelector:
                    function (obj) {
                        return obj.name;
                    }



        }
        function acPostSelect(mode, selectedChoice) {
            $(mode.basename).value = selectedChoice.id;
        }
        function acCreate(mode) {
            new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
                    mode.populator, {
                valueSelector: mode.valueSelector,
                afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                    acPostSelect(mode, selectedChoice)
                },
                indicator: mode.basename + "-indicator"
            })
        }


        Event.observe(window, "load", function() {
            acCreate(universityAutocompleterProps)
        <c:if test="${command.user.contactInformation.university ne null}">
            $('user.contactInformation.university-input').value = '${command.user.contactInformation.university.name}';
            $('user.contactInformation.university').value = '${command.user.contactInformation.university.id}';
            $('user.contactInformation.university-input').class = 'autocomplete';

        </c:if>

            initSearchField()

        })

    </script>

</head>
<body>


<ctcae:form method="post" cssClass="standard">

    <chrome:box title="Contact Information">

        <tags:renderRow propertyName="user.contactInformation.firstName" displayName="First Name" categoryName="text"
                        required="true"/>

        <tags:renderRow propertyName="user.contactInformation.lastName" displayName="Last Name" categoryName="text"
                        required="true"/>


        <tags:renderSelect propertyName="user.contactInformation.gender" displayName="Gender"
                           options="${genders}"
                           required="true"/>


        <tags:renderAutocompleter propertyName="user.contactInformation.university" displayName="University"
                                  required="true" help="true"
                                  showAllJavascript="javascript:showAllUniversities('university')"/>


        <tags:renderRow propertyName="user.contactInformation.emailId" displayName="Email Id" categoryName="text"
                        required="true"/>

        <tags:renderPassword propertyName="user.password" displayName="Password" categoryName="text"
                             required="true"/>

        <tags:renderPassword propertyName="confirmPassword" displayName="Confirm Password" categoryName="text"
                             required="true"/>

        <div class="row">
            <div class="submit">
                <input type="submit" value="Sign up"/>
            </div>
        </div>

    </chrome:box>


</ctcae:form>

</body>
</html>