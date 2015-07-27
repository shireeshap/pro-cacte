package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * The Class AudioFileController. Responsible for fetching the audio file from the asterisk server location and streaming the audio as a 
 * byte array to the jsp.
 */
public class AudioFileController extends AbstractController {

	protected static final Log logger = LogFactory.getLog(AudioFileController.class);
	
	private String contentType;
	
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
				response.setContentType(contentType);
				response.setContentLength(bytes.length);
				logger.debug("buffering successful...attempting to stream");
				ServletOutputStream out = response.getOutputStream();
				out.write(bytes);
				logger.debug("flushing buffer stream");
				out.flush();
				out.close();
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
		logger.error("filepath: "+ studyParticipantCrfSchedule.getFilePath());
		return studyParticipantCrfSchedule.getFilePath();
	}

	/**
	 * Gets the audio file from the astersik location (on the same machine as tomcat) and converts it to a byte array for the UI.
	 * returns null in case of some exception.
	 *
	 * @return the file data
	 * @throws Exception the exception
	 */
	protected byte[] getFileData(String filePath) throws Exception{
		logger.debug("Entering getFileData");
    	byte[] bytes = null;
    	
    	try{
        	File file = new File(filePath);
    	    InputStream fis = new FileInputStream(file);
    	    bytes = new byte[(int) file.length()];
    	      
    	    fis.read(bytes);
    	    fis.close();
    	} catch(IOException ioe){
    		logger.error(ioe.getMessage());
    	}
    	
	    return bytes;
    }

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public StudyParticipantCrfScheduleRepository getStudyParticipantCrfScheduleRepository() {
		return studyParticipantCrfScheduleRepository;
	}

	public void setStudyParticipantCrfScheduleRepository(
			StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
		this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
	}

}
