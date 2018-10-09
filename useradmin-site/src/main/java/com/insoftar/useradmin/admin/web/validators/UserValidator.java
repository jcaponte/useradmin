/**
 * 
 */
package com.insoftar.useradmin.admin.web.validators;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.insoftar.useradmin.model.security.User;
import com.insoftar.useradmin.security.SecurityService;

/**
 * @author Ebclosion
 *
 */
@Component
public class UserValidator implements Validator
{
	@Autowired protected MessageSource messageSource;
	@Autowired protected SecurityService securityService;
	
	@Override
	public boolean supports(Class<?> clazz)
	{
		return User.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors)
	{

	    ValidationUtils.rejectIfEmpty(errors, "name", "user.name.empty");
	    ValidationUtils.rejectIfEmpty(errors, "email", "user.email.empty");
	    ValidationUtils.rejectIfEmpty(errors, "password", "user.password.empty");
		
		
		User user = (User) target;
		
		
		
		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);
		if (!(pattern.matcher(user.getEmail()).matches())) {
			errors.rejectValue("email", "user.email.invalid");
		}		
		
		
		String email = user.getEmail();
		User userByEmail = securityService.findUserByEmail(email);
		if(userByEmail != null){
			errors.rejectValue("email", "error.exists", new Object[]{email}, "Email "+email+" already in use");
		}
	}
	
	
}
