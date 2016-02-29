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
package org.nanoj.web.tinymvc.env ;

import javax.servlet.http.HttpServletRequest;

import org.nanoj.util.StrUtil;

/**
 * Object containing information about the current action <br>
 * Stored in the request scope and then usable in the JSP with 'E.L.' <br>
 * Keep this class public in order to be usable in JSP <br>
 *  
 * @author Laurent GUERIN
 *
 */
public class ActionInfo {

	private final String root ;

	private final String httpServer ;
	
	private final String originalName ;

	private final String name ;
	
	private final String method ;

	private String className = "?" ;

	private String viewTemplate = null ;

	private String viewPage = null ;

	/**
	 * Constructor
	 * @param request
	 * @param originalName
	 * @param method
	 */
	public ActionInfo(HttpServletRequest request, String originalName, String method) {
		super();
		this.root = request.getContextPath() + request.getServletPath() ;
		this.httpServer = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() ;		
		this.originalName = originalName;
		this.method = method;
		this.name = StrUtil.firstCharLowerCase( originalName ) ;
	}

	/**
	 * Returns the current action root path in the current application context <br>
	 * e.g. : '/mywebapp/action'
	 * @return
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * Returns the current action original name (without modification) <br>
	 * e.g. : 'myaction' or 'MyAction'
	 * @return
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * Returns the current action conventional name <br>
	 * e.g. : 'compute' for 'Compute' or 'myAction' for 'MyAction'
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the current action relative URL <br>
	 * e.g. : '/mywebapp/actionservlet/myaction'
	 * @return
	 */
	public String getRelativeURL() {
		return root + "/" + originalName ;
	}

	/**
	 * Returns the current action absolute URL <br>
	 * e.g. : 'http://myhost:8080/mywebapp/actionservlet/myaction'
	 * @return
	 */
	public String getAbsoluteURL() {
		return httpServer + root + "/" + originalName ;
	}
	
	/**
	 * Returns the current action method that has been executed to produce the current result
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	//---------------------------------------------------------------------------------
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassName() {
		return className;
	}

	
	//---------------------------------------------------------------------------------
	public String getViewTemplate() {
		return viewTemplate;
	}
	public void setViewTemplate(String viewTemplate) {
		this.viewTemplate = viewTemplate;
	}

	//---------------------------------------------------------------------------------
	public String getViewPage() {
		return viewPage;
	}
	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	//---------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "[root=" + root 
				+ ", name=" + name 
				+ ", originalName=" + originalName 
				+ ", method=" + method 
				+ ", className=" + className 
				+ ", viewTemplate=" + viewTemplate 
				+ ", viewPage=" + viewPage 
				+ "]";
	}
	
}
