package gov.nih.nci.ctcae.core.helper;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: c3pr
 * Date: 10/25/11
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class AudioFileRenameHelper {

    public static void main(String str[]) {
        // Directory path here
        String path = "E:\\IVRS Spanish recordings\\Module_02_Voice-Prompts\\MP3\\";

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                // File (or directory) with old name
                File file = listOfFiles[i];
                String str1= file.getName() + "abc";
                String[] temp = str1.split(".mp3");
                String oldFileName =   temp[0];
                String newFileName = getNewFileName(oldFileName);
                if (newFileName != null){
                    String newFile = path +newFileName + ".mp3";
                    System.out.println("newfile name-> " + newFileName);
                    // File (or directory) with new name
                    File file2 = new File(newFile);
                    // Rename file (or directory)
                    boolean success = file.renameTo(file2);
                    if (!success) {
                        // File was not successfully renamed
                        System.out.println("can't rename file: " + file.getName());
                    }
                }
            }
        }

    }

    private static String getNewFileName(String oldFileName) {
         System.out.println("--> " + oldFileName);
        if(oldFileName.equals("001")) return	"calloutMessage";
        if(oldFileName.equals("002")) return	"welcome";
        if(oldFileName.equals("003")) return	"askCallerId";
        if(oldFileName.equals("004")) return	"askCallerPin";
        if(oldFileName.equals("005")) return	"incorrectIdOrPin";
        if(oldFileName.equals("006")) return	"contactAdmin";
        if(oldFileName.equals("007")) return	"noFormsAvailable";
        if(oldFileName.equals("008")) return	"instructions";
        if(oldFileName.equals("009")) return	"playInstructionOption";
        if(oldFileName.equals("010")) return	"newSurveyMessage";
        if(oldFileName.equals("011")) return	"inProgressSurveyMessage";
        if(oldFileName.equals("012")) return	"10minsClip";
        if(oldFileName.equals("013")) return	"20minsClip";
        if(oldFileName.equals("014")) return	"30minsClip";
        if(oldFileName.equals("015")) return	"mandatoryInstructions";
        if(oldFileName.equals("016")) return	"aboutToFillForm";
        if(oldFileName.equals("017")) return	"7DaysRecallPeriod";
        if(oldFileName.equals("018")) return	"monthlyRecallPeriod";
        if(oldFileName.equals("019")) return	"lastTreatmentRecallPeriod";
        if(oldFileName.equals("020")) return	"halfWayDoneMessage";
        if(oldFileName.equals("021")) return	"question1";
        if(oldFileName.equals("022")) return	"question2";
        if(oldFileName.equals("023")) return	"question3";
        if(oldFileName.equals("024")) return	"question4";
        if(oldFileName.equals("025")) return	"question5";
        if(oldFileName.equals("026")) return	"question6";
        if(oldFileName.equals("027")) return	"question7";
        if(oldFileName.equals("028")) return	"question8";
        if(oldFileName.equals("029")) return	"question9";
        if(oldFileName.equals("030")) return	"question10";
        if(oldFileName.equals("031")) return	"question11";
        if(oldFileName.equals("032")) return	"question12";
        if(oldFileName.equals("033")) return	"question13";
        if(oldFileName.equals("034")) return	"question14";
        if(oldFileName.equals("035")) return	"question15";
        if(oldFileName.equals("036")) return	"question16";
        if(oldFileName.equals("037")) return	"question17";
        if(oldFileName.equals("038")) return	"question18";
        if(oldFileName.equals("039")) return	"question19";
        if(oldFileName.equals("040")) return	"question20";
        if(oldFileName.equals("041")) return	"question21";
        if(oldFileName.equals("042")) return	"question22";
        if(oldFileName.equals("043")) return	"question23";
        if(oldFileName.equals("044")) return	"question24";
        if(oldFileName.equals("045")) return	"question25";
        if(oldFileName.equals("046")) return	"question26";
        if(oldFileName.equals("047")) return	"question27";
        if(oldFileName.equals("048")) return	"question28";
        if(oldFileName.equals("049")) return	"question30";
        if(oldFileName.equals("050")) return	"question31";
        if(oldFileName.equals("051")) return	"question32";
        if(oldFileName.equals("052")) return	"question33";
        if(oldFileName.equals("053")) return	"question34";
        if(oldFileName.equals("054")) return	"question35";
        if(oldFileName.equals("055")) return	"question36";
        if(oldFileName.equals("056")) return	"question37";
        if(oldFileName.equals("057")) return	"question38";
        if(oldFileName.equals("058")) return	"question39";
        if(oldFileName.equals("059")) return	"question40";
        if(oldFileName.equals("060")) return	"question41";
        if(oldFileName.equals("061")) return	"question42";
        if(oldFileName.equals("062")) return	"question43";
        if(oldFileName.equals("063")) return	"question44";
        if(oldFileName.equals("064")) return	"question45";
        if(oldFileName.equals("065")) return	"question46";
        if(oldFileName.equals("066")) return	"question124";
        if(oldFileName.equals("067")) return	"question125";
        if(oldFileName.equals("068")) return	"question126";
        if(oldFileName.equals("069")) return	"question47";
        if(oldFileName.equals("070")) return	"question48";
        if(oldFileName.equals("071")) return	"question49";
        if(oldFileName.equals("072")) return	"question50";
        if(oldFileName.equals("073")) return	"question51";
        if(oldFileName.equals("074")) return	"question52";
        if(oldFileName.equals("075")) return	"question53";
        if(oldFileName.equals("076")) return	"question54";
        if(oldFileName.equals("077")) return	"question55";
        if(oldFileName.equals("078")) return	"question56";
        if(oldFileName.equals("079")) return	"question57";
        if(oldFileName.equals("080")) return	"question58";
        if(oldFileName.equals("081")) return	"question59";
        if(oldFileName.equals("082")) return	"question60";
        if(oldFileName.equals("083")) return	"question61";
        if(oldFileName.equals("084")) return	"question62";
        if(oldFileName.equals("085")) return	"question63";
        if(oldFileName.equals("086")) return	"question64";
        if(oldFileName.equals("087")) return	"question65";
        if(oldFileName.equals("088")) return	"question66";
        if(oldFileName.equals("089")) return	"question67";
        if(oldFileName.equals("090")) return	"question68";
        if(oldFileName.equals("091")) return	"question69";
        if(oldFileName.equals("092")) return	"question70";
        if(oldFileName.equals("093")) return	"question71";
        if(oldFileName.equals("094")) return	"question72";
        if(oldFileName.equals("095")) return	"question73";
        if(oldFileName.equals("096")) return	"question74";
        if(oldFileName.equals("097")) return	"question75";
        if(oldFileName.equals("098")) return	"question77";
        if(oldFileName.equals("099")) return	"question78";
        if(oldFileName.equals("100")) return	"question79";
        if(oldFileName.equals("101")) return	"question80";
        if(oldFileName.equals("102")) return	"question81";
        if(oldFileName.equals("103")) return	"question82";
        if(oldFileName.equals("104")) return	"question83";
        if(oldFileName.equals("105")) return	"question84";
        if(oldFileName.equals("106")) return	"question85";
        if(oldFileName.equals("107")) return	"question86";
        if(oldFileName.equals("108")) return	"question87";
        if(oldFileName.equals("109")) return	"question88";
        if(oldFileName.equals("110")) return	"question89";
        if(oldFileName.equals("111")) return	"question90";
        if(oldFileName.equals("112")) return	"question91";
        if(oldFileName.equals("113")) return	"question92";
        if(oldFileName.equals("114")) return	"question93";
        if(oldFileName.equals("115")) return	"question94";
        if(oldFileName.equals("116")) return	"question95";
        if(oldFileName.equals("117")) return	"question96";
        if(oldFileName.equals("118")) return	"question97";
        if(oldFileName.equals("119")) return	"question98";
        if(oldFileName.equals("120")) return	"question99";
        if(oldFileName.equals("121")) return	"question100";
        if(oldFileName.equals("122")) return	"question101";
        if(oldFileName.equals("123")) return	"question102";
        if(oldFileName.equals("124")) return	"question103";
        if(oldFileName.equals("125")) return	"question104";
        if(oldFileName.equals("126")) return	"question105";
        if(oldFileName.equals("127")) return	"question106";
        if(oldFileName.equals("128")) return	"question107";
        if(oldFileName.equals("129")) return	"question108";
        if(oldFileName.equals("130")) return	"question109";
        if(oldFileName.equals("131")) return	"question76";
        if(oldFileName.equals("132")) return	"question29";
        if(oldFileName.equals("133")) return	"question112";
        if(oldFileName.equals("134")) return	"question113";
        if(oldFileName.equals("135")) return	"question114";
        if(oldFileName.equals("136")) return	"question115";
        if(oldFileName.equals("137")) return	"question116";
        if(oldFileName.equals("138")) return	"question117";
        if(oldFileName.equals("139")) return	"question118";
        if(oldFileName.equals("140")) return	"question119";
        if(oldFileName.equals("141")) return	"question120";
        if(oldFileName.equals("142")) return	"question121";
        if(oldFileName.equals("143")) return	"question122";
        if(oldFileName.equals("144")) return	"question123";
        if(oldFileName.equals("145")) return	"presentOptions1";
        if(oldFileName.equals("146")) return	"presentOptions2";
        if(oldFileName.equals("147")) return	"presentOptions3";
        if(oldFileName.equals("148")) return	"frequencyOptions1";
        if(oldFileName.equals("149")) return	"frequencyOptions2";
        if(oldFileName.equals("150")) return	"frequencyOptions3";
        if(oldFileName.equals("151")) return	"severityOptions1";
        if(oldFileName.equals("152")) return	"severityOptions2";
        if(oldFileName.equals("153")) return	"severityOptions3";
        if(oldFileName.equals("154")) return	"amountOptions1";
        if(oldFileName.equals("155")) return	"amountOptions2";
        if(oldFileName.equals("156")) return	"interferenceOptions1";
        if(oldFileName.equals("157")) return	"interferenceOptions2";
        if(oldFileName.equals("158")) return	"presentOption1";
        if(oldFileName.equals("159")) return	"presentOption2";
        if(oldFileName.equals("160")) return	"presentOptionNotAvailable";
        if(oldFileName.equals("161")) return	"presentOptionNotSexuallyActive";
        if(oldFileName.equals("162")) return	"presentOptionPreferNotToAnswer";
        if(oldFileName.equals("163")) return	"frequencyOption1";
        if(oldFileName.equals("164")) return	"frequencyOption2";
        if(oldFileName.equals("165")) return	"frequencyOption3";
        if(oldFileName.equals("166")) return	"frequencyOption4";
        if(oldFileName.equals("167")) return	"frequencyOption5";
   //     if(oldFileName.equals("168")) return	"optionNotAvailable";
        if(oldFileName.equals("169")) return	"frequencyOptionPreferNotToAnswer";
        if(oldFileName.equals("170")) return	"severityOption1";
        if(oldFileName.equals("171")) return	"severityOption2";
        if(oldFileName.equals("172")) return	"severityOption3";
        if(oldFileName.equals("173")) return	"severityOption4";
        if(oldFileName.equals("174")) return	"severityOption5";
   //     if(oldFileName.equals("175")) return	"optionNotAvailable";
        if(oldFileName.equals("176")) return	"optionNotSexualyActive";
        if(oldFileName.equals("177")) return	"optionPreferNotToAnswer";
        if(oldFileName.equals("178")) return	"amountOption1";
        if(oldFileName.equals("179")) return	"amountOption2";
        if(oldFileName.equals("180")) return	"amountOption3";
        if(oldFileName.equals("181")) return	"amountOption4";
        if(oldFileName.equals("182")) return	"amountOption5";
     //   if(oldFileName.equals("183")) return	"optionNotAvailable";
        if(oldFileName.equals("184")) return	"interferenceOption1";
        if(oldFileName.equals("185")) return	"interferenceOption2";
        if(oldFileName.equals("186")) return	"interferenceOption3";
        if(oldFileName.equals("187")) return	"interferenceOption4";
        if(oldFileName.equals("188")) return	"interferenceOption5";
        if(oldFileName.equals("189")) return	"optionNotAvailable";
        if(oldFileName.equals("190")) return	"confirmOrChangeThisAnswerYN";
        if(oldFileName.equals("191")) return	"cs_anxiety";
        if(oldFileName.equals("192")) return	"cs_constipation";
        if(oldFileName.equals("193")) return	"cs_decreased_appetite";
        if(oldFileName.equals("194")) return	"cs_fatigue";
        if(oldFileName.equals("195")) return	"cs_insomnia";
        if(oldFileName.equals("196")) return	"cs_loose_watery";
        if(oldFileName.equals("197")) return	"cs_mouth_throat_sores";
        if(oldFileName.equals("198")) return	"cs_nausea";
        if(oldFileName.equals("199")) return	"cs_numbness";
        if(oldFileName.equals("200")) return	"cs_pain";
        if(oldFileName.equals("201")) return	"cs_rash";
        if(oldFileName.equals("202")) return	"cs_sad_feelings";
        if(oldFileName.equals("203")) return	"cs_shortness_breath";
        if(oldFileName.equals("204")) return	"cs_vomiting";
        if(oldFileName.equals("205")) return	"recordAdditionalSymptomInstructions";
        if(oldFileName.equals("206")) return	"recordingInstructions";
        if(oldFileName.equals("207")) return	"successfullySubmittedForm";
        if(oldFileName.equals("208")) return	"thankyou";
        if(oldFileName.equals("209")) return	"invalidOption";
        if(oldFileName.equals("210")) return	"technicalIssue";
        if(oldFileName.equals("211")) return	"sorryCouldNotHear";
        if(oldFileName.equals("212")) return	"tryLater";
        if(oldFileName.equals("213")) return	"noMorePreviousQuestionsExist";
        if(oldFileName.equals("214")) return	"languageOption";
        return null;
    }
}






































































































































































































































