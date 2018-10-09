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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.insoftar.useradmin.admin.web.validators.UserValidator;
import com.insoftar.useradmin.model.security.Role;
import com.insoftar.useradmin.model.security.User;
import com.insoftar.useradmin.security.SecurityService;


/**
 * @author Juan Carlos Aponte
 *
 */
@Controller
@Secured(SecurityUtil.MANAGE_USERS)
public class UserController extends UserAdminProjectBaseController {
	private static final String viewPrefix = "users/";
	@Autowired
	protected SecurityService securityService;

	@Autowired
	private UserValidator userValidator;
	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Override
	protected String getHeaderTitle() {
		return "Manage Users";
	}

	@ModelAttribute("rolesList")
	public List<Role> rolesList() {
		return securityService.getAllRoles();
	}
	
	@ModelAttribute("users")
	public List<User> usersList() {
		return securityService.getAllUsers();
		
	}
	

	

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String createUserForm(Model model) {
		/*List<User> list = securityService.getAllUsers();
		model.addAttribute("users", list);*/
		// para la forma
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("tittle","Crear");
		model.addAttribute("classCollapse","collapsed-box");
		
		return viewPrefix + "users";
	}

	

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String createUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		userValidator.validate(user, result);
		if (result.hasErrors()) {

			model.addAttribute("warning",
					"Ocurrio un error al validar los datos, por favor verifique e intente de nuevo");

			List<User> list = securityService.getAllUsers();
			model.addAttribute("users", list);

			return viewPrefix + "users";
		}
		String password = user.getPassword();
		String encodedPwd = passwordEncoder.encode(password);
		user.setPassword(encodedPwd);

		try {
			User persistedUser = securityService.createUser(user);
			logger.debug("Created new User with id : {} and name : {}", persistedUser.getId(), persistedUser.getName());
			redirectAttributes.addFlashAttribute("success", "Usuario creado con éxito");
		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("error", "Ocurrio un error al guardar los datos");
		}

		return "redirect:/users";
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public String editUserForm(@PathVariable Integer id, Model model) {
		User user = securityService.getUserById(id);
		Map<Integer, Role> assignedRoleMap = new HashMap<>();
		List<Role> roles = user.getRoles();
		for (Role role : roles) {
			assignedRoleMap.put(role.getId(), role);
		}
		List<Role> userRoles = new ArrayList<>();
		List<Role> allRoles = securityService.getAllRoles();
		for (Role role : allRoles) {
			if (assignedRoleMap.containsKey(role.getId())) {
				userRoles.add(role);
			} else {
				userRoles.add(null);
			}
		}
		user.setRoles(userRoles);
		model.addAttribute("user", user);
		model.addAttribute("tittle","Editar");
		model.addAttribute("classCollapse","");
		return viewPrefix + "users";
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
	public String updateUser(@ModelAttribute("user") User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return viewPrefix + "users";
		}
				
		
		try {
			String password = user.getPassword();
			String encodedPwd = passwordEncoder.encode(password);
			user.setPassword(encodedPwd);			
			User persistedUser = securityService.updateUser(user);
			redirectAttributes.addFlashAttribute("success", "Usuario editado con éxito");
			return "redirect:/users";
		} catch (Exception ex) {
			redirectAttributes.addFlashAttribute("error", "Ocurrio un error al guardar los datos");
			return "redirect:/users/" + user.getId() ;
		}
		
							
		
	}
	
	
	@RequestMapping(value = "/users/delete/{id}", method = RequestMethod.GET)
	public String deleteUser(@ModelAttribute("user") User user, Model model,
			RedirectAttributes redirectAttributes) {
		
		securityService.deleteUser(user);
		
		redirectAttributes.addFlashAttribute("success", "Usuario eliminado con éxito.");
		
		return "redirect:/users";
	}

}
