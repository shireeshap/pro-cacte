package gov.nih.nci.ctcae.core;

import junit.framework.TestCase;


public class ProCtcSystemExceptionTest extends TestCase{
	private static String EXCEPTION_ONE = "This is a test exception";
	private static String EXCEPTION_CODE = "This the Exception Code";
	private static String EXCEPTION_CAUSE = "java.lang.Throwable ";

	public void testSystemExceptionMessage(){
		try{
			throw new ProCtcSystemException(EXCEPTION_ONE);
		}catch (Exception e) {
			assertEquals(e.getMessage(), EXCEPTION_ONE);
		}
	}
	
	public void testSystemExceptionCause(){
		try{
			throw new ProCtcSystemException(EXCEPTION_ONE,new Throwable(EXCEPTION_CAUSE));
		}catch (Exception e) {
			assertEquals(EXCEPTION_ONE, e.getMessage());
			assertEquals(new Throwable(EXCEPTION_CAUSE).getMessage(), e.getCause().getMessage());
			
		}
	}
	
	public void testSystemExceptionCode(){
		try{
			throw new ProCtcSystemException(EXCEPTION_CODE, EXCEPTION_ONE);
		}catch (ProCtcSystemException e) {
			assertEquals(EXCEPTION_ONE, e.getMessage());
			assertEquals(EXCEPTION_CODE, e.getErrorCode());
			
		}
	}
	
	public void testSystemException(){
		try{
			throw new ProCtcSystemException(EXCEPTION_CODE, EXCEPTION_ONE, new Throwable(EXCEPTION_CAUSE));
		}catch (ProCtcSystemException e) {
			assertEquals(EXCEPTION_CODE, e.getErrorCode());
			assertEquals(EXCEPTION_ONE, e.getMessage());
			assertEquals(new Throwable(EXCEPTION_CAUSE).getMessage(), e.getCause().getMessage());
		}
	}
	
}
