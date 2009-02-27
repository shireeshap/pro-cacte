package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.intercept.web.DefaultFilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.FilterSecurityInterceptor;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author Vinay Kumar
 * @crated Feb 27, 2009
 */
public class UrlSecurityIntegrationTestCase extends AbstractWebIntegrationTestCase {

    public void testFormPrivileges() throws IOException, ServletException {

        FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor) getApplicationContext().getBeansOfType(FilterSecurityInterceptor.class).values().iterator().next();

        DefaultFilterInvocationDefinitionSource objectDefinitionSource = (DefaultFilterInvocationDefinitionSource) filterSecurityInterceptor.getObjectDefinitionSource();

        String url = "/pages/form/editForm";
        request.setPathInfo(url);


        ConfigAttributeDefinition configAttributeDefinition = objectDefinitionSource.lookupAttributes(url, "");
        assertEquals("PRIVILEGE_EDIT_FORM", String.valueOf(configAttributeDefinition.getConfigAttributes().iterator().next()));
    }

    public void testHomePagePrivileges() throws IOException, ServletException {

        FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor) getApplicationContext().getBeansOfType(FilterSecurityInterceptor.class).values().iterator().next();

        DefaultFilterInvocationDefinitionSource objectDefinitionSource = (DefaultFilterInvocationDefinitionSource) filterSecurityInterceptor.getObjectDefinitionSource();

        String url = "/pages/home";
        request.setPathInfo(url);


        ConfigAttributeDefinition configAttributeDefinition = objectDefinitionSource.lookupAttributes(url, "");
        assertEquals("IS_AUTHENTICATED_FULLY", String.valueOf(configAttributeDefinition.getConfigAttributes().iterator().next()));
    }

}
