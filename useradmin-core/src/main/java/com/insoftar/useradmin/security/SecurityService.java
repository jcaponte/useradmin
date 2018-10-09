/**
 * 
 */
package com.insoftar.useradmin.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.insoftar.useradmin.exception.UserAdminException;
import com.insoftar.useradmin.model.security.Permission;
import com.insoftar.useradmin.model.security.Role;
import com.insoftar.useradmin.model.security.User;
import com.insoftar.useradmin.repository.security.PermissionRepository;
import com.insoftar.useradmin.repository.security.RoleRepository;
import com.insoftar.useradmin.repository.security.UserRepository;

/**
 * @author Juan Carlos Aponte
 *
 */
@Service
@Transactional
public class SecurityService
{
	@Autowired UserRepository userRepository;
	@Autowired PermissionRepository permissionRepository;
	@Autowired RoleRepository roleRepository;
	
	public User findUserByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}
	
	public String resetPassword(String email)
	{
		User user = findUserByEmail(email);
		if(user == null)
		{
			throw new UserAdminException("Invalid email address");
		}
		String uuid = UUID.randomUUID().toString();
		user.setPasswordResetToken(uuid);
		return uuid;
	}

	public void updatePassword(String email, String token, String password)
	{
		User user = findUserByEmail(email);
		if(user == null)
		{
			throw new UserAdminException("Invalid email address");
		}
		if(!StringUtils.hasText(token) || !token.equals(user.getPasswordResetToken())){
			throw new UserAdminException("Invalid password reset token");
		}
		user.setPassword(password);
		user.setPasswordResetToken(null);
	}

	public boolean verifyPasswordResetToken(String email, String token)
	{
		User user = findUserByEmail(email);
		if(user == null)
		{
			throw new UserAdminException("Invalid email address");
		}
		if(!StringUtils.hasText(token) || !token.equals(user.getPasswordResetToken())){
			return false;
		}
		return true;
	}
	
	public List<Permission> getAllPermissions() {
		return permissionRepository.findAll();
	}

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public Role getRoleByName(String roleName)
	{
		return roleRepository.findByName(roleName);
	}
	
	public Role createRole(Role role)
	{
		Role roleByName = getRoleByName(role.getName());
		if(roleByName != null){
			throw new UserAdminException("Role "+role.getName()+" already exist");
		}
		List<Permission> persistedPermissions = new ArrayList<>();
		List<Permission> permissions = role.getPermissions();
		
		if(permissions != null){
			for (Permission permission : permissions) {
				if(permission.getId() != 0)
				{
					persistedPermissions.add(permissionRepository.findOne(permission.getId()));
				}
			}
		}
		
		role.setPermissions(persistedPermissions);
		return roleRepository.save(role);
	}
	
	public Role updateRole(Role role)
	{
		Role persistedRole = getRoleById(role.getId());
		if(persistedRole == null){
			throw new UserAdminException("Role "+role.getId()+" doesn't exist");
		}
		persistedRole.setDescription(role.getDescription());
		persistedRole.setLabel(role.getLabel());
		List<Permission> updatedPermissions = new ArrayList<>();
		List<Permission> permissions = role.getPermissions();
		if(permissions != null){
			for (Permission permission : permissions) {
				if(permission.getId() != 0)
				{
					updatedPermissions.add(permissionRepository.findOne(permission.getId()));
				}
			}
		}
		persistedRole.setPermissions(updatedPermissions);
		return roleRepository.save(persistedRole);
	}
	
	public Role getRoleById(Integer id) {
		return roleRepository.findOne(id);
	}
	
	public User getUserById(Integer id)
	{
		return userRepository.findOne(id);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public User createUser(User user)
	{
		User userByEmail = findUserByEmail(user.getEmail());
		if(userByEmail != null){
			throw new UserAdminException("Email "+user.getEmail()+" already in use");
		}
		List<Role> persistedRoles = new ArrayList<>();
		List<Role> roles = user.getRoles();
		if(roles != null){
			for (Role role : roles) {
				if(role.getId() != 0)
				{
					persistedRoles.add(roleRepository.findOne(role.getId()));
				}
			}
		}
		user.setRoles(persistedRoles);
		
		return userRepository.save(user);
	}
	
	public User updateUser(User user)
	{
		User persistedUser = getUserById(user.getId());
		if(persistedUser == null){
			throw new UserAdminException("User "+user.getId()+" doesn't exist");
		}
		
		List<Role> updatedRoles = new ArrayList<>();
		List<Role> roles = user.getRoles();
		if(roles != null){
			for (Role role : roles) {
				if(role.getId() != 0)
				{
					updatedRoles.add(roleRepository.findOne(role.getId()));
				}
			}
		}
		persistedUser.setName(user.getName());
		persistedUser.setEmail(user.getEmail());
		persistedUser.setRoles(updatedRoles);
		return userRepository.save(persistedUser);
	}
	
	public void deleteUser(User user) {
		
		userRepository.delete(user);
	}
	
	public void deleteRole(Role role) {
		
		roleRepository.delete(role);
	}

}
