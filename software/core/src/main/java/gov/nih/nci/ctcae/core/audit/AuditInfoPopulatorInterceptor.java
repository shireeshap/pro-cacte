package gov.nih.nci.ctcae.core.audit;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.User;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Will populate auditing information in current thread context.
 *
 * @author Suneel Allareddy
 * @see gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo
 */
public class AuditInfoPopulatorInterceptor implements MethodInterceptor {

    private static final Log logger = LogFactory.getLog(AuditInfoPopulatorInterceptor.class);

    private String fromUrl;   

    private boolean fromJob = false;

    @SuppressWarnings("deprecation")
    public Object invoke(MethodInvocation method) throws Throwable {
        DataAuditInfo oldAuditInfo = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String userName = getUserLoginName(authentication);
                if (userName != null) {
                    oldAuditInfo = (DataAuditInfo) DataAuditInfo.getLocal();
                    String url = fromUrl == null ? Thread.currentThread().getName() : fromUrl;
                    DataAuditInfo.setLocal(new DataAuditInfo(userName, "127.0.0.1", new Date(), url));
                }
            }
            //This block is for Spring Jobs which doesn't have authentication
            if(isFromJob()){
                DataAuditInfo.setLocal(new DataAuditInfo("admin", "127.0.0.1", new Date(), "127.0.0.1"));
            }

            return method.proceed();

        } catch (Throwable e) {
            logger.error("AuditInfoPopulatorInterceptor", e);
            throw e;
        } finally {
            DataAuditInfo.setLocal(oldAuditInfo);
        }
        //return null;
    }

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    /**
     * This method will find the login name, of the user available in
     * SecurityContext.
     *
     * @return
     */
    public static String getUserLoginName(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String userName = "";
        if (principal instanceof User) {
            userName = ((User) principal).getUsername();
        } else {
            userName = principal.toString();
        }

        return userName;
	}

    public boolean isFromJob() {
        return fromJob;
    }

    public void setFromJob(boolean fromJob) {
        this.fromJob = fromJob;
    }
}

