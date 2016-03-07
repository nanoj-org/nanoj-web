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

	private final String requestURL ;

	private final String requestURI ;
	
	private final String originalName ;

	private final String name ;
	
	private final String method ;

	private String controllerClass  = null ;
	private String controllerMethod = null ;
	private String controllerResult = null ;

//	private String viewLayout = null ;
//	private String viewPage = null ;
	private ActionView actionView = null ;
	
	/**
	 * Constructor
	 * @param request
	 * @param originalName
	 * @param method
	 */
	public ActionInfo(HttpServletRequest request, String originalName, String method) {
		super();
		this.requestURL = removeEndingMethodName(request.getRequestURL().toString(), method);
		this.requestURI = removeEndingMethodName(request.getRequestURI(), method);		
		this.originalName = originalName;
		this.name = StrUtil.firstCharLowerCase( originalName ) ;
		this.method = method;
	}

	private String removeEndingMethodName(String s, String method) {
		if ( ! StrUtil.nullOrVoid(method) ) {
			return StrUtil.removeEnd(s, "." + method );
		}
		return s ;
	}

	/**
	 * Returns the current action request URL <br>
	 * same as HttpServletRequest.getRequestURL() <br>
	 * e.g. : 'http://myhost:8080/mywebapp/aaa/bbb/myaction'
	 * @return
	 */
	public String getRequestURL() {
		return requestURL;
	}

	/**
	 * Returns the current action request URI <br>
	 * same as HttpServletRequest.getRequestURI() <br>
	 * e.g. : '/mywebapp/aaa/bbb/myaction'
	 * @return
	 */
	public String getRequestURI() {
		return requestURI;
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
	 * Returns the current action method that has been executed to produce the current result
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	//---------------------------------------------------------------------------------
	/**
	 * Set information regarding the processing result (after class.method execution)
	 * @param className
	 * @param methodCalled
	 * @param result
	 */
	public void setProcessingResult(String className, String methodCalled, String result ) {
		this.controllerClass = className;
		this.controllerMethod = methodCalled;
		this.controllerResult = result;
	}

	/**
	 * Returns the controller class used to process the action <br>
	 * e.g. : "ComputeAction" Java class   
	 * @return
	 */
	public String getControllerClass() {
		return controllerClass;
	}
	
	/**
	 * Returns the controller method used to process the action <br>
	 * ( a method of the controller class ) <br>
	 * e.g. : "add" Java method ( defined in "ComputeAction" Java class )
	 * @return
	 */
	public String getControllerMethod() {
		return controllerMethod;
	}
	
	public String getControllerResult() {
		return controllerResult;
	}
	
	//---------------------------------------------------------------------------------
	public void setActionView(ActionView actionView) {
		this.actionView = actionView ;
	}
	
	public String getViewName() {
		return actionView != null ? actionView.getViewName() : null ;
	}
	public String getViewPath() {
		return actionView != null ? actionView.getViewPath() : null ;
	}
	public String getViewPageName() {
		return actionView != null ? actionView.getPageName() : null ;
	}
	public String getViewPagePath() {
		return actionView != null ? actionView.getPagePath() : null ;
	}
	public String getViewLayoutName() {
		return actionView != null ? actionView.getLayoutName() : null ;
	}
	public String getViewLayoutPath() {
		return actionView != null ? actionView.getLayoutPath() : null ;
	}
//	public void setViewLayout(String viewLayout) {
//		this.viewLayout = viewLayout;
//	}

//	public void setViewPage(String viewPage) {
//		this.viewPage = viewPage;
//	}

	//---------------------------------------------------------------------------------
//	public String getView() {
//		if ( viewLayout != null ) {
//			return viewLayout + ":" + viewPage ;
//		}
//		else {
//			return viewPage;
//		}
//	}
	//---------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "[  name=" + name 
				+ ", originalName=" + originalName 
				+ ", method=" + method 
				+ ", controllerClass=" + controllerClass 
				+ ", controllerMethod=" + controllerMethod
				+ ", controllerResult=" + controllerResult
				+ ", viewLayoutName=" + getViewLayoutName() 
				+ ", viewPageName=" + getViewPageName()
				+ ", viewPageName=" + getViewName()
				+ "]";
	}
	
}
