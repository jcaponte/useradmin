/**
 * 
 */
package com.insoftar.useradmin.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insoftar.useradmin.model.security.User;

/**
 * @author Juan Carlos Aponte
 *
 */
public interface UserRepository extends JpaRepository<User, Integer>
{

	User findByEmail(String email);

}
