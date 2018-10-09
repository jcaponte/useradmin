/**
 * 
 */
package com.insoftar.useradmin;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Juan Carlos Aponte
 *
 */
@SpringBootApplication
public class UserAdminProjectApplication
{
	
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-4"));   // It will set GMT timezone
    }	
	

	public static void main(String[] args)
	{
		SpringApplication.run(UserAdminProjectApplication.class, args);
	}

}
