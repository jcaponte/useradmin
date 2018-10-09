/**
 * 
 */
package com.insoftar.useradmin;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.insoftar.useradmin.common.services.EmailService;
/**
 * @author Juan Carlos Aponte
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserAdminCoreApplicationTest
{
	@Autowired DataSource dataSource;
	@Autowired EmailService emailService;
	
	@Test
	public void testDummy() throws SQLException
	{
		String schema = dataSource.getConnection().getCatalog();
		assertTrue("useradmin".equalsIgnoreCase(schema));
	}
	
	@Test
	public void testSendEmail()
	{
		emailService.sendEmail("admin@gmail.com", "User Admin - Test Mail", "This is a test email from UserAdmin");
	}
	
}
