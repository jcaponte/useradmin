/**
 * 
 */
package com.insoftar.useradmin.admin.web.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;


/**
 * @author Juan Carlos Aponte
 *
 */
public class WebUtils
{
	private WebUtils()
	{
	}

	
	public static String getURLWithContextPath(HttpServletRequest request)
	{
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}
	
	public static String generateHashForTp() {
		return String.format("%s", RandomStringUtils.randomAlphanumeric(8));		
	}
	

	 public static String getParamsString(Map<String, String> params) 
		      throws UnsupportedEncodingException{
		        StringBuilder result = new StringBuilder();
		 
		        for (Map.Entry<String, String> entry : params.entrySet()) {
		          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
		          result.append("=");
		          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		          result.append("&");
		        }
		 
		        String resultString = result.toString();
		        return resultString.length() > 0
		          ? resultString.substring(0, resultString.length() - 1)
		          : resultString;
		    }	
	
	
 
	 
	 
	 
	 
}
