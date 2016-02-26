/**
 *  Copyright (C) 2013-2016 Laurent GUERIN - NanoJ project org. ( http://www.nanoj.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
