/**
 * 
 */
package com.insoftar.useradmin.admin.web.controllers.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.insoftar.useradmin.admin.security.SecurityUtil;
import com.insoftar.useradmin.admin.web.controllers.UserAdminProjectBaseController;
import com.insoftar.useradmin.model.security.Permission;
import com.insoftar.useradmin.security.SecurityService;

/**
 * @author Juan Carlos Aponte
 *
 */
@Controller
@Secured(SecurityUtil.MANAGE_PERMISSIONS)
public class PermissionController extends UserAdminProjectBaseController
{
	private static final String viewPrefix = "permissions/";
	
	@Autowired protected SecurityService securityService;
	
	@Override
	protected String getHeaderTitle()
	{
		return "Manage Permissions";
	}
	
	@RequestMapping(value="/permissions", method=RequestMethod.GET)
	public String listPermissions(Model model) {
		List<Permission> list = securityService.getAllPermissions();
		model.addAttribute("permissionsList",list);
		return viewPrefix+"permissions";
	}

}
