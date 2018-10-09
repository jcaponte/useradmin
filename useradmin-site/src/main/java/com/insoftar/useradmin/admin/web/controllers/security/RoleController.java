/**
 * 
 */
package com.insoftar.useradmin.admin.web.controllers.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insoftar.useradmin.admin.security.SecurityUtil;
import com.insoftar.useradmin.admin.web.controllers.UserAdminProjectBaseController;
import com.insoftar.useradmin.admin.web.validators.RoleValidator;
import com.insoftar.useradmin.model.security.Permission;
import com.insoftar.useradmin.model.security.Role;
import com.insoftar.useradmin.security.SecurityService;

/**
 * @author Juan Carlos Aponte
 *
 */
@Controller
@Secured(SecurityUtil.MANAGE_ROLES)
public class RoleController extends UserAdminProjectBaseController
{
	private static final String viewPrefix = "roles/";
	
	@Autowired protected SecurityService securityService;
	@Autowired private RoleValidator roleValidator;

	@Override
	protected String getHeaderTitle()
	{
		return "Manage Roles";
	}
	
	@ModelAttribute("permissionsList")
	public List<Permission> permissionsList(){
		return securityService.getAllPermissions();
	}
	
	@ModelAttribute("rolesList")
	public List<Role> rolesList(){
		return securityService.getAllRoles();
	}	

	@RequestMapping(value="/roles", method=RequestMethod.GET)
	public String createRoleForm(Model model) {
		model.addAttribute("role",new Role());
		model.addAttribute("tittle","Crear");
		model.addAttribute("classCollapse","collapsed-box");
		
		return viewPrefix+"roles";
	}

	@RequestMapping(value="/roles", method=RequestMethod.POST)
	public String createRole(@Valid @ModelAttribute("role") Role role, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes) {
		try {
			roleValidator.validate(role, result);
			if (result.hasErrors()) {
				model.addAttribute("warning",
						"Ocurrio un error al validar los datos, por favor verifique e intente de nuevo");
				return viewPrefix + "roles";
			}
			Role persistedRole = securityService.createRole(role);
			logger.debug("Created new role with id : {} and name : {}", persistedRole.getId(), persistedRole.getName());
			redirectAttributes.addFlashAttribute("success", "El Rol fue creado exitosamente");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Ocurrio un error al guardar los datos");
			logger.error(e.getMessage());
		}
		return "redirect:/roles";
	}
	
	
	@RequestMapping(value="/roles/{id}", method=RequestMethod.GET)
	public String editRoleForm(@PathVariable Integer id, Model model) {
		Role role = securityService.getRoleById(id);
		Map<Integer, Permission> assignedPermissionMap = new HashMap<>();
		List<Permission> permissions = role.getPermissions();
		for (Permission permission : permissions)
		{
			assignedPermissionMap.put(permission.getId(), permission);
		}
		List<Permission> rolePermissions = new ArrayList<>();
		List<Permission> allPermissions = securityService.getAllPermissions();
		for (Permission permission : allPermissions)
		{
			if(assignedPermissionMap.containsKey(permission.getId())){
				rolePermissions.add(permission);
			} else {
				rolePermissions.add(null);
			}
		}
		role.setPermissions(rolePermissions);
		model.addAttribute("role",role);
		model.addAttribute("tittle","Editar");
		model.addAttribute("classCollapse","");
		return viewPrefix+"roles";
	}
	
	@RequestMapping(value="/roles/{id}", method=RequestMethod.POST)
	public String updateRole(@ModelAttribute("role") Role role, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes) {		
		try {
			Role persistedRole = securityService.updateRole(role);
			logger.debug("Updated role with id : {} and name : {}", persistedRole.getId(), persistedRole.getName());
			redirectAttributes.addFlashAttribute("success", "Rol actualizado exitosamente");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Ocurrio un error al guardar los datos");
			logger.error(e.getMessage());
		}
		return "redirect:/roles";
	}
	
	@RequestMapping(value = "/roles/delete/{id}", method = RequestMethod.GET)
	public String deleteRole(@ModelAttribute("role") Role role, Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
			securityService.deleteRole(role);
			redirectAttributes.addFlashAttribute("success", "Rol eliminado con éxito.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Ocurrio un error al guardar los datos");
			logger.error(e.getMessage());
		}
		return "redirect:/roles";
	}
}
