/**
 * 
 */
package com.insoftar.useradmin.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insoftar.useradmin.model.security.Role;

/**
 * @author Juan Carlos Aponte
 *
 */
public interface RoleRepository extends JpaRepository<Role, Integer>
{

	Role findByName(String name);

}
