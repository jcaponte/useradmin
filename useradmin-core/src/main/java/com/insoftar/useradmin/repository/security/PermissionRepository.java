/**
 * 
 */
package com.insoftar.useradmin.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insoftar.useradmin.model.security.Permission;

/**
 * @author Juan Carlos Aponte
 *
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer>
{

}
