   var hasError = false;
   var displaySymptom = false;
   var displayForm = true;
   var displayDate = true;
   var displayParticipants = false;
   var selectedCrf = '';
   var displayParticipant = false;
   var displayFilterBy = false;

   function displayForms(crfid) {
       $('divSymptomsRow').hide();
       $('divFormRow').hide();
       $('studydata').show();
       selectedCrf = crfid;
       var id = $('study').value
       crf.getReducedCrfs(id, updateFormDropDown)
   }

   function displaySites() {
   		var aResults = getOrganizations('%');
   		postOrganizationFetch(aResults);
   }

   function getOrganizations(sQuery) {
	   showIndicator("studySiteInput-indicator");
       var callbackProxy = function(results) {
           aResults = results;
       };
       var callMetaData = {callback:callbackProxy, async:false};
       organization.matchOrganizationByStudyId(unescape(sQuery), $('study').value, callMetaData);
       hideIndicator("studySiteInput-indicator");
       return aResults;
   }

   function postOrganizationFetch(values) {
   	$('divStudySiteRow').hide();
       $('studySiteAutoCompleter').hide();
       
       var siteNum = values.length;
       if (siteNum == 1) {
           $('studySite').value = values[0].id;
           $('studySiteDisplayName').innerHTML = values[0].displayName;
           $('divStudySiteRow').show();
       } else {
           $('studySiteAutoCompleter').show();
       }
   }

   function updateFormDropDown(crfs) {
       $('divFormRow').show();
       if (crfs.length == 1) {
           var value = crfs[0];
           $('formTitle').innerHTML = value['title'];
           $('form').value = value['id'];
           displaySymptoms(value['id']);
       } else {
           var select = populate('formTitle', crfs, 'title', 'id', selectedCrf, 'Form');
           select.onchange = function() {
               $('form').value = this.value;
               if (displaySymptom) {
                   displaySymptoms(this.value);
               }
           }
       }
   }

   function displaySymptoms(crfid) {
       $('form').value = crfid;
       if (displaySymptom) {
           crf.getSymptomsForCrf(crfid, updateSymptomDropDown);
       }
   }



   function updateSymptomDropDown(symptoms) {
       $('divSymptomsRow').show();
       var select = populate('proCtcTerms', symptoms, 'term', 'id', '', 'Symptom');
   }

   function customVisit(showVisit) {
       var myindex = showVisit.selectedIndex
       var selValue = showVisit.options[myindex].value
       if (selValue == "custom") {
           $('visitNum').show();
       } else {
           $('visitNum').hide();
       }
       if (selValue == "dateRange") {
           $('dateRange').show();
       } else {
           $('dateRange').hide();
       }

   }
   function performValidations() {
       hasError = false;
       var j = 0;
       var arr = new Array();
       if (displayForm) {
           arr[j++] = 'form';
       }
       if (displaySymptom) {
           arr[j++] = 'proCtcTermsSelect';
       }
       if (displayFilterBy) {
           arr[j++] = 'filterByValue';
       }
       for (var i = 0; i < arr.length; i++) {
           validateField(arr[i]);
       }
       if (hasError) {
           return false;
       }
       return true;
   }

   function isVisible(obj) {
       return obj.style.display != 'none';
   }
   function validateField(id) {
       var obj = $(id);
       if (obj && isVisible(obj)) {
           if (obj.value == '') {
               hasError = true;
               showError(obj);
           } else {
               removeError(obj);
           }
       }
   }

   function showResults(transport) {
       // var items = $('reportOuterDiv').childElements();
       // var len = items.length;
       // for (var i = 0; i < len; i++) {
       // if (items[i].id != 'reportInnerDiv') {
       // items[i].remove();
       // }
       // }
       $('reportOuterDiv').show();
       // new Insertion.After('reportInnerDiv', transport.responseText);
       var div = $('reportInnerDiv');
       div.update(transport.responseText);
   }


   function hideHelp() {
       $('attribute-help-content').style.display = 'none';
   }
   function showError(element) {
       hasError = true;
       removeError(element);
       new Insertion.Bottom(element.parentNode, " <ul id='" + element.name + "-msg'class='errors'><li>" + 'Missing ' + element.title + "</li></ul>");
   }
   function removeError(element) {
       msgId = element.name + "-msg"
       $(msgId) != null ? new Element.remove(msgId) : null
   }

   function fnDisplayParticipants() {
       if (displayParticipants) {
           var myParticipantAutoCompleter = new participantAutoCompleter
                   ('participant', function(autocompleter, text) {
                       participant.matchParticipantByStudySiteId(text,
                               $('studySite').value, $('study').value, function(values) {
                           autocompleter.setChoices(values)
                       })
                   });
           acCreate(myParticipantAutoCompleter);
           initSearchField();
           $('participantAutoCompleterDiv').show();
       }
   }
   function showResponses(id) {

       var request = new Ajax.Request("../participant/showCompletedCrf", {
           parameters:getStandardParamForAjax() + "&id=" + id ,
           onComplete:function(transport) {
               showConfirmationWindow(transport, 700, 500);
           },
           method:'get'
       })
   }

   function populate(divid, values, itext, ivalue, selectedValue, title) {
       var ele = $(divid);
       if (ele.firstChild != null) {
           ele.removeChild(ele.firstChild);
       }
       // var select = new Element('SELECT', {'id':divid + 'Select'});
       var select = new Element('SELECT', {'id':divid + 'Select','title':title});
       addPleaseSelect(select);

       for (var i = 0; i < values.length; i++) {
           var value = values[i];
           var option = new Element('OPTION', {});
           if (typeof(itext) != 'undefined') {
               option.label = value[itext];
               option.text = value[itext];
           } else {
               option.label = value;
               option.text = value;
           }
           if (typeof(ivalue) != 'undefined') {
               option.value = value[ivalue];
           } else {
               option.value = value;
           }
           if (option.value == selectedValue) {
               option.selected = true;
           }
           select.appendChild(option);
       }
       ele.empty();
       ele.appendChild(select);
       return select;
   }
   function addPleaseSelect(select) {
       var option = new Element('OPTION', {});
       option.label = 'Please select';
       option.text = 'Please select';
       option.value = '';
       select.appendChild(option);
   }
   function getSelectedAttributes() {
       var obj = document.getElementsByName('attribute');
       var selectedAttributes = '';
       for (var i = 0; i < obj.length; i++) {
           if (obj[i].checked) {
               selectedAttributes = selectedAttributes + '_' + obj[i].value;
           }
       }
       return selectedAttributes;
   }
   function updateChart(chkbox) {
       if (getSelectedAttributes() == '') {
           alert('Please select at least one question type.');
           chkbox.checked = true;
           return;
       }
       reportResults();
   }

   function getQueryString(igroup, iarms) {
       var queryString = getStandardParamForAjax();
       queryString += "&study=" + $('study').value;
       if (displayForm) {
           queryString += "&crf=" + $('form').value;
           queryString += "&studySite=" + $('studySite').value;
       }
       if (displaySymptom) {
           queryString += "&symptom=" + $('proCtcTermsSelect').value;
       } else {
           if ($('symptom') != null) {
               queryString += "&symptom=" + $('symptom').value;
           }
       }
       if (displayFilterBy) {
           queryString += "&filter=" + $('filterBy').value;
           queryString += "&filterVal=" + $('filterByValue').value;
       }

       queryString += "&attributes=" + getSelectedAttributes();
       var group = 'cycle';
       if (typeof(igroup) == 'undefined' || igroup == '') {
           if ($('groupby') != null) {
               group = $('groupby').value.toLowerCase();
           }
       } else {
           group = igroup;
       }
       queryString += "&group=" + group;
       if (typeof(iarms) == 'undefined' || iarms == '') {
           var arms = getSelectedArms();
           if ($('chartTypeDiv') != null) {
               var armsArr = arms.split('_');
               if (armsArr.size() > 2) {
                   $('chartTypeDiv').show()
                   queryString += "&chartType=" + getCheckedChartType();
               } else {
                   $('chartTypeDiv').hide()
               }
           }
           queryString += "&arms=" + arms;

       } else {
           queryString += "&arms=" + iarms;
       }

       return queryString;
   }

   function getCheckedChartType() {
       var chartTypes = document.getElementsByName('chartType');
       for (var i = 0; i < chartTypes.length; i++) {
           if (chartTypes[i].checked) {
               return chartTypes[i].value;
           }
       }
   }
   function getSelectedArms() {
       var c_value = "";
       if (showResultsInPopUpFlag) {
           var arms = document.getElementsByName('armPop');
           if (arms.length == 0) {
               var arms = document.getElementsByName('arm');
           }
       } else {
           var arms = document.getElementsByName('arm');
       }
       for (var i = 0; i < arms.length; i++) {
           if (arms[i].checked) {
               c_value = c_value + arms[i].value + "_";
           }
       }
       return c_value;
   }
   function showItems(Id, grade, att, period) {
       var request = new Ajax.Request("/proctcae/pages/reports/getParticipantItems", {
           parameters:getQueryString() + '&pid=' + Id + '&grade=' + grade + '&att=' + att + '&period=' + period,
           onComplete:function(transport) {
               var response = transport.responseText;
               new Insertion.After('items_row_' + Id, response);
               $('pShowImage_' + Id).hide();
               $('pHideImage_' + Id).show();
           },
           method:'get'
       }
               )
   }
   function hideItems(Id) {
       $('pShowImage_' + Id).show();
       $('pHideImage_' + Id).hide();
       var items = document.getElementsByTagName('tr');
       var delItems = new Array();
       var j = 0;
       for (var i = 0; i < items.length; i++) {
           var item = items[i];
           if (item.title == 'childTableRow_' + Id) {
               delItems[j++] = $(item);
           }
       }
       for (var k = 0; k < delItems.length; k++) {
           $(delItems[k]).remove();
       }
   }

   function showDetails(params) {
       showIndicator();
       var request = new Ajax.Request("reportDetails", {
           parameters:params,
           onComplete:function(transport) {
               if (showResultsInPopUpFlag) {
                   showResultsInPopUp(transport);
               } else {
                   showResults(transport);
               }
               hideIndicator();
           },
           method:'get'
       }
               )
   }

   function showResultsInPopUp(transport) {
       var win = showConfirmationWindow(transport, 850, 570);
   }
   function showChartInPopup(symptomId) {
       $('symptom').value = symptomId;
       showResultsInPopUpFlag = true;
       reportResults();
   }
   function resetPopUpFlagAndCallResults() {
       showResultsInPopUpFlag = false;
       if ($('symptom') != null) {
           $('symptom').value = '';
       }
       reportResults();
   }
