package gov.nih.nci.ctcae.web.login;

public class InvalidResetPasswordLinkException extends RuntimeException{
	String errorCode;
	String message;
	
	public InvalidResetPasswordLinkException(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
