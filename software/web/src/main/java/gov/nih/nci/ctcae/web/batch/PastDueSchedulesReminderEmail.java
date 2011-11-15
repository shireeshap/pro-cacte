package gov.nih.nci.ctcae.web.batch;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.cabig.ctms.tools.DataSourceSelfDiscoveringPropertiesFactoryBean;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;

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

        Map<StudyOrganizationClinicalStaff, Set<StudyParticipantCrfSchedule>> siteClincalStaffAndParticipantAssignmentMap = new HashMap<StudyOrganizationClinicalStaff, Set<StudyParticipantCrfSchedule>>();
        Map<StudyParticipantAssignment, Set<StudyParticipantCrfSchedule>> studyParticipantAndSchedulesMap = new HashMap<StudyParticipantAssignment, Set<StudyParticipantCrfSchedule>>();
        DataAuditInfo auditInfo = new DataAuditInfo("admin", "localhost", new Date(), "127.0.0.0");
        DataAuditInfo.setLocal(auditInfo);
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.begin();
        logger.error("Nightly trigger bean job starts....");
        Query query = session.createQuery(new String("Select study from Study study"));
        List<Study> studies = query.list();
        Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();
        boolean web = false;
        for (Study study : studies) {
            for (StudySite studySite : study.getStudySites()) {
                List<StudyOrganizationClinicalStaff> clinicalStaffList = studySite.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA);
                clinicalStaffList.addAll(studySite.getStudyOrganizationClinicalStaffByRole(Role.SITE_PI));
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    if (studyParticipantAssignment.getStatus()!=null&&studyParticipantAssignment.getStatus().equals(RoleStatus.ACTIVE)) {
                        for (StudyParticipantMode mode : studyParticipantAssignment.getStudyParticipantModes()) {
                            if (mode.getMode().equals(AppMode.HOMEWEB)) {
                                if (mode.getEmail()) {
                                    web = true;
                                }
                            }
                        }
                        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                                if (today.after(studyParticipantCrfSchedule.getDueDate())) {
                                    if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.PASTDUE)) {
                                        studyParticipantCrfSchedule.setStatus(CrfStatus.PASTDUE);
                                        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : clinicalStaffList) {
                                            addScheduleToEmailList(siteClincalStaffAndParticipantAssignmentMap, studyParticipantCrfSchedule, studyOrganizationClinicalStaff);
                                        }
                                    } else if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS)) {
                                        studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
                                    }
                                }

                                if (web && today.after(studyParticipantCrfSchedule.getStartDate()) && today.before(studyParticipantCrfSchedule.getDueDate())) {
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
            String subject = "List of past due surveys";
            JavaMailSender javaMailSender = new JavaMailSender();
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : siteClincalStaffAndParticipantAssignmentMap.keySet()) {
                String emailAddress = studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getEmailAddress();
                String content = getHtmlContent(siteClincalStaffAndParticipantAssignmentMap.get(studyOrganizationClinicalStaff), studyOrganizationClinicalStaff);
                if (StringUtils.isNotBlank(emailAddress)) {
                    System.out.println("Sending ClinicalStaff email to " + emailAddress);
                    logger.error("Sending ClinicalStaff email to " + emailAddress);
                    MimeMessage message = javaMailSender.createMimeMessage();
                    message.setSubject(subject);
                    message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(emailAddress);
                    helper.setText(content, javaMailSender.isHtml());
                    javaMailSender.send(message);
                }
            }


            logger.error("Nightly trigger bean size of mails map...." + studyParticipantAndSchedulesMap.keySet().size());
            if (studyParticipantAndSchedulesMap.keySet().size() > 0) {
                for (StudyParticipantAssignment studyParticipantAssignment : studyParticipantAndSchedulesMap.keySet()) {
                    String participantEmailAddress = studyParticipantAssignment.getParticipant().getEmailAddress();
                    String participantEmailContent = getHtmlContentForParticipantEmail(studyParticipantAndSchedulesMap.get(studyParticipantAssignment), studyParticipantAssignment);
                    if (StringUtils.isNotBlank(participantEmailAddress)) {
                        System.out.println("Sending Survey Reminder email to " + participantEmailAddress);
                        logger.error("Sending Survey Reminder email to " + participantEmailAddress);
                        MimeMessage participantMessage = javaMailSender.createMimeMessage();
                        Locale locale = Locale.ENGLISH;
                        if(studyParticipantAssignment.getHomeWebLanguage()!= null && studyParticipantAssignment.getHomeWebLanguage().equals("SPANISH")){
                            locale = new Locale("es");
                        }
                        String participantSubject = messageSource.getMessage("participant.email.comp7", null, locale);
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
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        tx.commit();
        logger.error("Nightly trigger bean job ends....");
    }

    private void addScheduleToEmailList(Map<StudyOrganizationClinicalStaff, Set<StudyParticipantCrfSchedule>> siteClincalStaffAndParticipantAssignmentMap, StudyParticipantCrfSchedule studyParticipantCrfSchedule, StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        Set<StudyParticipantCrfSchedule> participantAssignmentSet = siteClincalStaffAndParticipantAssignmentMap.get(studyOrganizationClinicalStaff);
        if (participantAssignmentSet == null) {
            participantAssignmentSet = new HashSet<StudyParticipantCrfSchedule>();
            siteClincalStaffAndParticipantAssignmentMap.put(studyOrganizationClinicalStaff, participantAssignmentSet);
        }
        participantAssignmentSet.add(studyParticipantCrfSchedule);
    }

    private void addScheduleForParticipant(Map<StudyParticipantAssignment, Set<StudyParticipantCrfSchedule>> studyParticipantAndScheduleMap, StudyParticipantCrfSchedule studyParticipantCrfSchedule, StudyParticipantAssignment studyParticipantAssignment) {
        Set<StudyParticipantCrfSchedule> studyParticipantCrfScheduleSet = studyParticipantAndScheduleMap.get(studyParticipantAssignment);
        if (studyParticipantCrfScheduleSet == null) {
            studyParticipantCrfScheduleSet = new HashSet<StudyParticipantCrfSchedule>();
            studyParticipantAndScheduleMap.put(studyParticipantAssignment, studyParticipantCrfScheduleSet);
        }
        studyParticipantCrfScheduleSet.add(studyParticipantCrfSchedule);
    }

    private String getHtmlContent(Set<StudyParticipantCrfSchedule> studyParticipantCrfSchedules, StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        Map<Study, Map<StudySite, Map<CRF, Set<StudyParticipantCrfSchedule>>>> studySiteScehduleMap = new LinkedHashMap<Study, Map<StudySite, Map<CRF, Set<StudyParticipantCrfSchedule>>>>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
            StudySite studySite = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite();
            Study study = studySite.getStudy();
            CRF crf = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf();
            Map<StudySite, Map<CRF, Set<StudyParticipantCrfSchedule>>> map;
            if (studySiteScehduleMap.containsKey(study)) {
                map = studySiteScehduleMap.get(study);
            } else {
                map = new LinkedHashMap<StudySite, Map<CRF, Set<StudyParticipantCrfSchedule>>>();
                studySiteScehduleMap.put(study, map);
            }
            Map<CRF, Set<StudyParticipantCrfSchedule>> formschedules;
            if (map.containsKey(studySite)) {
                formschedules = map.get(studySite);
            } else {
                formschedules = new LinkedHashMap<CRF, Set<StudyParticipantCrfSchedule>>();
                map.put(studySite, formschedules);
            }
            Set<StudyParticipantCrfSchedule> participantCrfScheduleSet;
            if (formschedules.containsKey(crf)) {
                participantCrfScheduleSet = formschedules.get(crf);
            } else {
                participantCrfScheduleSet = new LinkedHashSet<StudyParticipantCrfSchedule>();
                formschedules.put(crf, participantCrfScheduleSet);
            }
            participantCrfScheduleSet.add(studyParticipantCrfSchedule);
        }
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html><head></head><body>");
        emailContent.append("Hello " + studyOrganizationClinicalStaff.getDisplayName());
        emailContent.append("<br><br>Below is the list of participants who have one or more surveys pending for completion.<br>");

        for (Study study : studySiteScehduleMap.keySet()) {
            emailContent.append("<br><br><b>Study: </b>" + study.getDisplayName());
            for (StudySite studySite : studySiteScehduleMap.get(study).keySet()) {
                emailContent.append("<br><b>Study site: </b>" + studySite.getDisplayName());
                for (CRF crf : studySiteScehduleMap.get(study).get(studySite).keySet()) {
                    emailContent.append("<br><b>Form: </b>" + crf.getTitle());
                    emailContent.append("<br><br><table border=\"1\">");
                    addRow(emailContent, new String[]{"Participant", "Schedule start date", "Schedule due date"});
                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studySiteScehduleMap.get(study).get(studySite).get(crf)) {
                        addRow(emailContent, new String[]{studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName(), DateUtils.format(studyParticipantCrfSchedule.getStartDate()), DateUtils.format(studyParticipantCrfSchedule.getDueDate())});
                    }
                    emailContent.append("</table>");
                    emailContent.append("</body></html>");
                }
            }
        }

        emailContent.append("<br>This is an auto-generated email from PRO-CTCAE system. Please do not reply to it.");
        return emailContent.toString();
    }

    private String getHtmlContentForParticipantEmail(Set<StudyParticipantCrfSchedule> studyParticipantCrfSchedules, StudyParticipantAssignment studyParticipantAssignment) {
//        Map<CRF, Set<StudyParticipantCrfSchedule>> participantCrfScheduleMap = new LinkedHashMap<CRF, Set<StudyParticipantCrfSchedule>>();
//        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
//            CRF crf = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf();
//            Set<StudyParticipantCrfSchedule> scheduleSet;
//            if (participantCrfScheduleMap.containsKey(crf)) {
//                scheduleSet = participantCrfScheduleMap.get(crf);
//            } else {
//                scheduleSet = new LinkedHashSet<StudyParticipantCrfSchedule>();
//                participantCrfScheduleMap.put(crf, scheduleSet);
//            }
//            scheduleSet.add(studyParticipantCrfSchedule);
//        }
        List<StudyParticipantCrfSchedule> sortedStudyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>(studyParticipantCrfSchedules);
        Collections.sort(sortedStudyParticipantCrfSchedules);

        Locale locale = Locale.ENGLISH;
        if(studyParticipantAssignment.getHomeWebLanguage()!= null && studyParticipantAssignment.getHomeWebLanguage().equals("SPANISH")){
            locale = new Locale("es");
        }

        StringBuilder participantEmailContent = new StringBuilder();
        participantEmailContent.append("<html><head></head><body>");
        participantEmailContent.append(messageSource.getMessage("participant.email.comp1", null, locale) +
                " <b><i>" + studyParticipantAssignment.getParticipant().getFirstName() + " " + studyParticipantAssignment.getParticipant().getLastName() + "</i></b><br/>");
        participantEmailContent.append("<br><b>" + messageSource.getMessage("participant.email.comp2", null, locale) + ": </b>" + studyParticipantAssignment.getStudySite().getStudy().getDisplayName());
        participantEmailContent.append("<br><b>" + messageSource.getMessage("participant.email.comp3", null, locale) + ": </b>" + studyParticipantAssignment.getStudySite().getDisplayName() + "</br><br/>");
//            for (CRF crf : participantCrfScheduleMap.keySet()) {
//                participantEmailContent.append("<br><b>Form: " + crf.getTitle());
//                participantEmailContent.append("<br><br><table border=\"1\">");
//                addRow(participantEmailContent, new String[]{"Schedule start date", "Schedule  due date"});
//                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
//                    addRow(participantEmailContent, new String[]{DateUtils.format(studyParticipantCrfSchedule.getStartDate()), DateUtils.format(studyParticipantCrfSchedule.getDueDate())});
//                }
//                participantEmailContent.append("</table>");
//                participantEmailContent.append("</body></html>");
//            }
//        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : sortedStudyParticipantCrfSchedules) {
//            participantEmailContent.append("FROM TITLE=>" + studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf().getTitle() + "<br/>");
//            participantEmailContent.append("  due date ->" + studyParticipantCrfSchedule.getDueDate() + "<br/><br/>");
//        }
        Date earliestDueDate = ((StudyParticipantCrfSchedule)sortedStudyParticipantCrfSchedules.get(0)).getDueDate();
        String baseUrl =  properties.getProperty(BASE_URL);

        participantEmailContent.append("<br>" + messageSource.getMessage("participant.email.comp4", null, locale) + "  : <b>" + DateUtils.format(earliestDueDate) + "</b><br/>");
        participantEmailContent.append("<br>" + messageSource.getMessage("participant.email.comp5", null, locale) + ": ");
        participantEmailContent.append("<p style='text-decoration:underline,color=blue'>"+ baseUrl + "</p><br/>");
        participantEmailContent.append("</body></html>");
        participantEmailContent.append("<br>" + messageSource.getMessage("participant.email.comp6", null, locale));
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
