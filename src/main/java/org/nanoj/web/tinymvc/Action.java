package org.nanoj.web.tinymvc ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action interface <br>
 * Must be implemented by every action provided by the application
 * 
 * @author Laurent Guerin
 *
 */
public interface Action {

	/**
	 * Action pre-processing
	 * @param method
	 * @param request
	 * @param response
	 */
	void beforeAction(String method,  HttpServletRequest request, HttpServletResponse response) ;
	
	/**
	 * Default processing method for an action <br>
	 * Called when the action is called without method name <br>
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	String process( HttpServletRequest request, HttpServletResponse response) ;

	/**
	 * Action post-processing
	 * @param method
	 * @param request
	 * @param response
	 */
	void afterAction(String method,  HttpServletRequest request, HttpServletResponse response) ;
}
