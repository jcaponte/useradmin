/**
 * 
 */
package com.insoftar.useradmin.admin.web.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.insoftar.useradmin.admin.security.SecurityUtil;

/**
 * @author Juan Carlos Aponte
 *
 */
@Controller
@Secured({SecurityUtil.MANAGE_PERMISSIONS,
	SecurityUtil.MANAGE_ROLES,
	SecurityUtil.MANAGE_SETTINGS,
	SecurityUtil.MANAGE_USERS})
public class HomeController extends UserAdminProjectBaseController
{	
	@Override
	protected String getHeaderTitle() {
		return "Home";
	}
	
	@RequestMapping("/home")
	public String home(Model model)
	{
		return "home";
	}

	

}
