package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * The Class AudioFileController. Responsible for fetching the audio file from the asterisk server location by using ftp and streaming the audio as a 
 * byte array to the jsp.
 */
public class AudioFileController extends AbstractController {

	public static final String FTP_IP = "ftp.server.name";
	public static final String FTP_USERNAME = "ftp.username";
	public static final String FTP_PASSWORD = "ftp.password";
	
//	public static final String ftpFilePath = "/var/lib/asterisk/sounds/sampleWavFile30Sec.wav";
	protected static final Log logger = LogFactory.getLog(AudioFileController.class);
	
	private String contentType;
	private Properties properties;
	
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        if (!StringUtils.isBlank(id)) {
            studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(id));
        }
		if (getContentType() == null) {
			throw new IllegalArgumentException("contentType property must be set");
		}

		String pathOfFileToFetch= getFilePath(studyParticipantCrfSchedule);
		if(!StringUtils.isBlank(pathOfFileToFetch)){
			byte[] bytes = getFileData(pathOfFileToFetch);
			String contentType = getContentType();
			
			if(bytes != null){
				ServletOutputStream out = response.getOutputStream();
				// Write content type and also length (determined via byte array).
				response.setContentType(contentType);
				response.setContentLength(bytes.length);

				// Flush byte array to servlet output stream.
				out.write(bytes);
				out.flush();
			} else {
				PrintWriter printWriter = response.getWriter();
				printWriter.print("<b>System Error while loading file. Please try again later.</b>");
				printWriter.flush();
			}
		}
		return null;
	}

	/**
	 * Gets the file path from the database.
	 *
	 * @return the file path
	 */
	private String getFilePath(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		return studyParticipantCrfSchedule.getFilePath();
	}

	/**
	 * Gets the audio file from the remote server and converts it to a byte array for the UI.
	 * returns null in case of some exception.
	 *
	 * @return the file data
	 * @throws Exception the exception
	 */
	protected byte[] getFileData(String filePath) throws Exception{
    	String ftpIp = properties.getProperty(FTP_IP);
    	String ftpUsername = properties.getProperty(FTP_USERNAME);
    	String ftpPassword = properties.getProperty(FTP_PASSWORD);
    	
    	FTPClient ftp = new FTPClient();
    	boolean isSuccess = false;
    	byte[] buffer = null;
    	
    	//this will create a file in $CATALINA_HOME/bin
    	File file = new File("prt_rec.wav");
    	try {
	          ftp.connect(ftpIp);
	          ftp.login(ftpUsername, ftpPassword);
	          int reply = ftp.getReplyCode();
	               
	          if(FTPReply.isPositiveCompletion(reply)){
	        	  logger.debug("Connected Successfully");
	        	  
	        	  FileOutputStream dfile = new FileOutputStream(file);
	        	  isSuccess = ftp.retrieveFile(filePath, dfile);
	    		  
              } else {
	            logger.error("Connection Failed");
	          }
	          
	          if(isSuccess){
		  	      InputStream fis = new FileInputStream(file);
		  	      buffer = new byte[fis.available()];
		  	      fis.read(buffer);
		  	      fis.close();
		  	  } 
        } catch (SocketException ex) {
           ex.printStackTrace();
	    } catch (IOException ex) {
           ex.printStackTrace();
	    } finally {
	    	ftp.disconnect();
	    	//delete the file from $CATALINA_HOME/bin
  	    	file.delete();
	    }
	    
	    return buffer;
    }

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public StudyParticipantCrfScheduleRepository getStudyParticipantCrfScheduleRepository() {
		return studyParticipantCrfScheduleRepository;
	}

	public void setStudyParticipantCrfScheduleRepository(
			StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
		this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
	}

}
