/**
 * 
 */
package com.insoftar.useradmin.exception;

/**
 * @author Juan Carlos Aponte
 *
 */
public class UserAdminException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UserAdminException()
	{
		super();
	}

	public UserAdminException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserAdminException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public UserAdminException(String message)
	{
		super(message);
	}

	public UserAdminException(Throwable cause)
	{
		super(cause);
	}
	
}
