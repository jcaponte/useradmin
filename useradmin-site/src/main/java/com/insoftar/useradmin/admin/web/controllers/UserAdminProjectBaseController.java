/**
 * 
 */
package com.insoftar.useradmin.admin.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.insoftar.useradmin.admin.security.AuthenticatedUser;
import com.insoftar.useradmin.common.services.JCLogger;

/**
 * @author Juan Carlos Aponte
 *
 */
public abstract class UserAdminProjectBaseController
{
	protected final JCLogger logger = JCLogger.getLogger(getClass());
	
	@Autowired protected MessageSource messageSource;
	
	protected abstract String getHeaderTitle();
	
	public String getMessage(String code)
	{
		return messageSource.getMessage(code, null, null);
	}
	
	public String getMessage(String code, String defaultMsg)
	{
		return messageSource.getMessage(code, null, defaultMsg, null);
	}
	
	@ModelAttribute("authenticatedUser")
    public AuthenticatedUser authenticatedUser(@AuthenticationPrincipal AuthenticatedUser authenticatedUser)
    {
        return authenticatedUser;
    }
	
	public static AuthenticatedUser getCurrentUser() {

	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if (principal instanceof AuthenticatedUser) {
	    	return ((AuthenticatedUser) principal);
	    }
	    // principal object is either null or represents anonymous user -
	    // neither of which our domain User object can represent - so return null
	    return null;
	}

	public static boolean isLoggedIn() {
	    return getCurrentUser() != null;
	}
}
