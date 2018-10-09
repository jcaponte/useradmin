/**
 * 
 */
package com.insoftar.useradmin.admin.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Juan Carlos Aponte
 *
 */
public class MenuConfiguration 
{
	private static Map<String, String> MENU_URL_PATTERN_MAP = new HashMap<>();
	
	static
	{
		MENU_URL_PATTERN_MAP.put("/useradmin/home", "Home");
		MENU_URL_PATTERN_MAP.put("/useradmin/users", "Users");
		MENU_URL_PATTERN_MAP.put("/useradmin/roles", "Roles");
		MENU_URL_PATTERN_MAP.put("/useradmin/permissions", "Permissions");
	}
	
	public static Map<String, String> getMenuUrlPatternMap() {
		return Collections.unmodifiableMap(MENU_URL_PATTERN_MAP);
	}

	public static String getMatchingMenu(String uri) {
		Set<String> keySet = MENU_URL_PATTERN_MAP.keySet();
		for (String key : keySet) {
			if(uri.equalsIgnoreCase(key)){
				return MENU_URL_PATTERN_MAP.get(key);
			}
		}
		return "";
	}
}
