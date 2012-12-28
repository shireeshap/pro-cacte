package gov.nih.nci.ctcae.web.batch;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantMode;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class PastDueSchedulesReminderEmail extends HibernateDaoSupport {
    protected static final Log logger = LogFactory.getLog(PastDueSchedulesReminderEmail.class);
    private DelegatingMessageSource messageSource;
    private Properties properties;

    public static final String BASE_URL = "base.url";

    public void setMessageSource(DelegatingMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


    @Transactional
    public void generateEmailReports() {

        Map<StudyOrganizationClinicalStaff, List<StudyParticipantCrfSchedule>> siteClincalStaffAndParticipantAssignmentMap = new HashMap<StudyOrganizationClinicalStaff, List<StudyParticipantCrfSchedule>>();
        Map<StudyParticipantAssignment, List<StudyParticipantCrfSchedule>> studyParticipantAndSchedulesMap = new HashMap<StudyParticipantAssignment, List<StudyParticipantCrfSchedule>>();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.begin();
        logger.debug("Nightly trigger bean job starts....");
        Query query = session.createQuery(new String("Select study from Study study"));
        List<Study> studies = query.list();
        Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, -2);
        Date yesterday = ProCtcAECalendar.getCalendarForDate(cal.getTime()).getTime();
        boolean web = false;
        for (Study study : studies) {

            for (StudySite studySite : study.getStudySites()) {
            	//add the study site level clinicians
                List<StudyOrganizationClinicalStaff> clinicalStaffList = studySite.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA);
                clinicalStaffList.addAll(studySite.getStudyOrganizationClinicalStaffByRole(Role.SITE_PI));
                //add the study level clinicians
            	clinicalStaffList.addAll(study.getStudyOrganizationClinicalStaffByRole(Role.LEAD_CRA));
            	clinicalStaffList.addAll(study.getStudyOrganizationClinicalStaffByRole(Role.PI));
            	
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                	web = false;
                    if (studyParticipantAssignment.getStatus() != null && studyParticipantAssignment.getStatus().equals(RoleStatus.ACTIVE)) {
                        for (StudyParticipantMode mode : studyParticipantAssignment.getStudyParticipantModes()) {
                            if (mode.getMode().equals(AppMode.HOMEWEB)) {
                                if (mode.getEmail()) {
                                    web = true;
                                }
                            }
                        }
                        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                            	
                            	//For all schedules that are past their due date, update status to Pastdue or completed accordingly
                            	if (today.after(studyParticipantCrfSchedule.getDueDate())) {
                            		if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
                                        studyParticipantCrfSchedule.setStatus(CrfStatus.PASTDUE);
	                            	} else if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS)) {
	                                    studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
	                                }                            		
                            	}

                            	//for all schedules that went past-due Yesterday (and yesterday only), add to past-due notifications email list.
                                if (today.after(studyParticipantCrfSchedule.getDueDate()) && yesterday.before(studyParticipantCrfSchedule.getDueDate())) {
                                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.PASTDUE)) {
                                        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : clinicalStaffList) {
                                            addScheduleToEmailList(siteClincalStaffAndParticipantAssignmentMap, studyParticipantCrfSchedule, studyOrganizationClinicalStaff);
                                        }
                                    }
                                }

                                //for all schedules that are scheduled or in-progress and available, send out patient reminders.
                                if (web && (today.equals(studyParticipantCrfSchedule.getStartDate())||today.after(studyParticipantCrfSchedule.getStartDate())) && (today.before(studyParticipantCrfSchedule.getDueDate())||today.equals(studyParticipantCrfSchedule.getDueDate()))) {
                                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS)) {
                                        addScheduleForParticipant(studyParticipantAndSchedulesMap, studyParticipantCrfSchedule, studyParticipantAssignment);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            String subject = "PRO-CTCAE System Notification: Recently Missed Surveys";
            JavaMailSender javaMailSender = new JavaMailSender();
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : siteClincalStaffAndParticipantAssignmentMap.keySet()) {
                if (siteClincalStaffAndParticipantAssignmentMap.get(studyOrganizationClinicalStaff).size() > 0) {
                    String emailAddress = studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress();
                    String content = getHtmlContent(siteClincalStaffAndParticipantAssignmentMap.get(studyOrganizationClinicalStaff), studyOrganizationClinicalStaff);
                    if (StringUtils.isNotBlank(emailAddress)) {
                        System.out.println("Sending ClinicalStaff email to " + emailAddress);
                        logger.debug("Sending ClinicalStaff email to " + emailAddress);
                        MimeMessage message = javaMailSender.createMimeMessage();
                        message.setSubject(subject);
                        message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
                        MimeMessageHelper helper = new MimeMessageHelper(message, true);
                        helper.setTo(emailAddress);
                        helper.setText(content, javaMailSender.isHtml());
                        javaMailSender.send(message);
                    }
                }
            }

            logger.debug("Nightly trigger bean size of mails map...." + studyParticipantAndSchedulesMap.keySet().size());
            if (studyParticipantAndSchedulesMap.keySet().size() > 0) {
                for (StudyParticipantAssignment studyParticipantAssignment : studyParticipantAndSchedulesMap.keySet()) {
                    String participantEmailAddress = studyParticipantAssignment.getParticipant().getEmailAddress();
                    String participantEmailContent = getHtmlContentForParticipantEmail(studyParticipantAndSchedulesMap.get(studyParticipantAssignment), studyParticipantAssignment);
                    if (StringUtils.isNotBlank(participantEmailAddress)) {
                        System.out.println("Sending Survey Reminder email to " + participantEmailAddress);
                        logger.debug("Sending Survey Reminder email to " + participantEmailAddress);
                        MimeMessage participantMessage = javaMailSender.createMimeMessage();
                        Locale locale = Locale.ENGLISH;
                        if (studyParticipantAssignment.getHomeWebLanguage() != null && studyParticipantAssignment.getHomeWebLanguage().equals("SPANISH")) {
                            locale = new Locale("es");
                        }
                        String participantSubject = messageSource.getMessage("participant.email.comp13", null, locale);
                        participantMessage.setSubject(participantSubject);
                        participantMessage.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
                        MimeMessageHelper helper = new MimeMessageHelper(participantMessage, true);
                        helper.setTo(participantEmailAddress);
                        helper.setText(participantEmailContent, javaMailSender.isHtml());
                        javaMailSender.send(participantMessage);
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (AddressException e) {
            logger.error(e.getMessage());
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        } finally {
            tx.commit();
        }
        logger.debug("Nightly trigger bean job ends....");
    }

    private void addScheduleToEmailList(Map<StudyOrganizationClinicalStaff, List<StudyParticipantCrfSchedule>> siteClincalStaffAndParticipantAssignmentMap, StudyParticipantCrfSchedule studyParticipantCrfSchedule, StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        List<StudyParticipantCrfSchedule> participantAssignmentSet = siteClincalStaffAndParticipantAssignmentMap.get(studyOrganizationClinicalStaff);
        if (participantAssignmentSet == null) {
            participantAssignmentSet = new ArrayList<StudyParticipantCrfSchedule>();
            siteClincalStaffAndParticipantAssignmentMap.put(studyOrganizationClinicalStaff, participantAssignmentSet);
        }
        participantAssignmentSet.add(studyParticipantCrfSchedule);
    }

    private void addScheduleForParticipant(Map<StudyParticipantAssignment, List<StudyParticipantCrfSchedule>> studyParticipantAndScheduleMap, StudyParticipantCrfSchedule studyParticipantCrfSchedule, StudyParticipantAssignment studyParticipantAssignment) {
        List<StudyParticipantCrfSchedule> studyParticipantCrfScheduleSet = studyParticipantAndScheduleMap.get(studyParticipantAssignment);
        if (studyParticipantCrfScheduleSet == null) {
            studyParticipantCrfScheduleSet = new ArrayList<StudyParticipantCrfSchedule>();
            studyParticipantAndScheduleMap.put(studyParticipantAssignment, studyParticipantCrfScheduleSet);
        }
        studyParticipantCrfScheduleSet.add(studyParticipantCrfSchedule);
    }

    private String getHtmlContent(List<StudyParticipantCrfSchedule> studyParticipantCrfSchedules, StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        Map<Study, Map<StudySite, Map<CRF, List<StudyParticipantCrfSchedule>>>> studySiteScehduleMap = new LinkedHashMap<Study, Map<StudySite, Map<CRF, List<StudyParticipantCrfSchedule>>>>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
            StudySite studySite = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite();
            Study study = studySite.getStudy();
            CRF crf = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf();
            Map<StudySite, Map<CRF, List<StudyParticipantCrfSchedule>>> map;
            if (studySiteScehduleMap.containsKey(study)) {
                map = studySiteScehduleMap.get(study);
            } else {
                map = new LinkedHashMap<StudySite, Map<CRF, List<StudyParticipantCrfSchedule>>>();
                studySiteScehduleMap.put(study, map);
            }
            Map<CRF, List<StudyParticipantCrfSchedule>> formschedules;
            if (map.containsKey(studySite)) {
                formschedules = map.get(studySite);
            } else {
                formschedules = new LinkedHashMap<CRF, List<StudyParticipantCrfSchedule>>();
                map.put(studySite, formschedules);
            }
            List<StudyParticipantCrfSchedule> participantCrfScheduleSet;
            if (formschedules.containsKey(crf)) {
                participantCrfScheduleSet = formschedules.get(crf);
            } else {
                participantCrfScheduleSet = new ArrayList<StudyParticipantCrfSchedule>();
                formschedules.put(crf, participantCrfScheduleSet);
            }
            participantCrfScheduleSet.add(studyParticipantCrfSchedule);
        }
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html><head></head><body>");
        emailContent.append("Hello " + studyOrganizationClinicalStaff.getDisplayName());
        emailContent.append("<br><br>Listed below are the PRO-CTCAE symptom surveys which expired last night and were not completed by the assigned study participant.<br>");

        for (Study study : studySiteScehduleMap.keySet()) {
            emailContent.append("<br><br><b>Study: </b>" + study.getDisplayName());
            for (StudySite studySite : studySiteScehduleMap.get(study).keySet()) {
                emailContent.append("<br><b>Study site: </b>" + studySite.getDisplayName());
                for (CRF crf : studySiteScehduleMap.get(study).get(studySite).keySet()) {
                    emailContent.append("<br><b>Survey: </b>" + crf.getTitle());
                    emailContent.append("<br><br><table border=\"1\">");
                    addRow(emailContent, new String[]{"Participant", "Survey start date", "Survey due date"});
                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studySiteScehduleMap.get(study).get(studySite).get(crf)) {
                        addRow(emailContent, new String[]{studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName(), DateUtils.format(studyParticipantCrfSchedule.getStartDate()), DateUtils.format(studyParticipantCrfSchedule.getDueDate())});
                    }
                    emailContent.append("</table>");
                    emailContent.append("</body></html>");
                }
            }
        }

        emailContent.append("<br>This email was generated by the PRO-CTCAE system. Please do not reply to it.");
        return emailContent.toString();
    }

    private String getHtmlContentForParticipantEmail(List<StudyParticipantCrfSchedule> studyParticipantCrfSchedules, StudyParticipantAssignment studyParticipantAssignment) {
        List<StudyParticipantCrfSchedule> sortedStudyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>(studyParticipantCrfSchedules);
        Collections.sort(sortedStudyParticipantCrfSchedules);

        Locale locale = Locale.ENGLISH;
        if (studyParticipantAssignment.getHomeWebLanguage() != null && studyParticipantAssignment.getHomeWebLanguage().equals("SPANISH")) {
            locale = new Locale("es");
        }

        StringBuilder participantEmailContent = new StringBuilder();
        participantEmailContent.append("<html><head></head><body>");
        participantEmailContent.append(messageSource.getMessage("participant.email.comp1", null, locale) +
                " <b><i>" + studyParticipantAssignment.getParticipant().getFirstName() + " " + studyParticipantAssignment.getParticipant().getLastName() + "</i></b><br/>");
        Date earliestDueDate = ((StudyParticipantCrfSchedule) sortedStudyParticipantCrfSchedules.get(0)).getDueDate();
        String baseUrl = properties.getProperty(BASE_URL);
        participantEmailContent.append("<br>" + messageSource.getMessage("participant.email.comp4", null, locale) + "<br/>");
        participantEmailContent.append("<p style='text-decoration:underline,color=blue'>" + baseUrl + "</p>");
        participantEmailContent.append("<br>" + messageSource.getMessage("participant.email.comp5", null, locale) + " <b>" + DateUtils.format(earliestDueDate) + ".</b>");
        participantEmailContent.append(" " + messageSource.getMessage("participant.email.comp6", null, locale) + "<br/>");
        participantEmailContent.append("<br/>" + messageSource.getMessage("participant.email.comp7", null, locale) + "<br/>");
        participantEmailContent.append("<br/><ul><li>" + messageSource.getMessage("participant.email.comp8", null, locale) + "</li><br/>");
        participantEmailContent.append("<li>" + messageSource.getMessage("participant.email.comp9", null, locale) + "</li><br/>");
        participantEmailContent.append("<li>" + messageSource.getMessage("participant.email.comp10", null, locale) + "</li></ul>");
        participantEmailContent.append("<br/>" + messageSource.getMessage("participant.email.comp11", null, locale) + "<br/>");
        participantEmailContent.append("<br/>" + messageSource.getMessage("participant.email.comp12", null, locale) + "<br/>");
        participantEmailContent.append("</body></html>");
        return participantEmailContent.toString();
    }

    private void addRow(StringBuilder content, String[] columnValues) {
        content.append("<tr>");
        addColumns(content, columnValues);
        content.append("</tr>");
    }

    private void addColumns(StringBuilder content, String[] columnValues) {
        for (String s : columnValues) {
            content.append("<td>").append(s).append("</td>");
        }
    }

}
