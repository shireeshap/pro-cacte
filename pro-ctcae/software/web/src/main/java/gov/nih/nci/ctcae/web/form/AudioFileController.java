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
        logger.debug("Entering handleRequestInternal");
        if (!StringUtils.isBlank(id)) {
            studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(id));
            logger.debug("loaded the sp_crf_schedule object with id = " + id);
        }
		if (getContentType() == null) {
			logger.error("contentType property must be set");
			throw new IllegalArgumentException("contentType property must be set");
		}

		String pathOfFileToFetch= getFilePath(studyParticipantCrfSchedule);
		if(!StringUtils.isBlank(pathOfFileToFetch)){
			byte[] bytes = getFileData(pathOfFileToFetch);
			String contentType = getContentType();
			
			if(bytes != null){
				logger.debug("buffering successful...attempting to stream");
				ServletOutputStream out = response.getOutputStream();
				// Write content type and also length (determined via byte array).
				response.setContentType(contentType);
				response.setContentLength(bytes.length);

				// Flush byte array to servlet output stream.
				out.write(bytes);
				logger.debug("flushing buffer stream");
				out.flush();
				logger.debug("flushed buffer stream");
			} else {
				logger.error("buffer is empty...printing error");
				PrintWriter printWriter = response.getWriter();
				printWriter.print("<b>System Error while loading file. Please try again later.</b>");
				printWriter.flush();
			}
		}
		logger.debug("Exiting handleRequestInternal");
		return null;
	}

	/**
	 * Gets the file path from the database.
	 *
	 * @return the file path
	 */
	private String getFilePath(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
		logger.debug("filepath: "+ studyParticipantCrfSchedule.getFilePath());
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
		logger.debug("Entering getFileData");
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
	          int reply = ftp.getReplyCode();
	          
	          if(FTPReply.isPositiveCompletion(reply)){
	        	  ftp.login(ftpUsername, ftpPassword);
	        	  ftp.enterLocalPassiveMode();
	        	  logger.debug("Connected Successfully to ftp server");
	        	  FileOutputStream dfile = new FileOutputStream(file);
	        	  isSuccess = ftp.retrieveFile(filePath, dfile);
	    		  
              } else {
	            logger.error("Connection Failed to ftp server");
	          }
	          
	          if(isSuccess){
	        	  logger.debug("successfully retrieved file from ftp server...populating buffer now.");
		  	      InputStream fis = new FileInputStream(file);
		  	      buffer = new byte[fis.available()];
		  	      fis.read(buffer);
		  	      fis.close();
		  	  } 
        } catch (SocketException ex) {
        	logger.error("contentType property must be set"+ ex.getMessage());
	    } catch (IOException ex) {
	    	logger.error("contentType property must be set"+ ex.getMessage());
	    } finally {
	    	ftp.disconnect();
	    	//delete the file from $CATALINA_HOME/bin
  	    	file.delete();
	    }
	    logger.debug("Exiting getFileData");
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
