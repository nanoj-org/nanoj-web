package org.nanoj.web.tinymvc.env ;

import javax.servlet.http.HttpServletRequest;

/**
 * Object containing information about the current action <br>
 * Stored in the request scope and usable in the JSP with 'E.L.' <br>
 * Keep this class public in order to be usable in JSP <br>
 *  
 * @author Laurent GUERIN
 *
 */
public class ActionInfo {

	private final String root ;

	private final String httpServer ;
	
	private final String name ;
	
	private final String method ;

	private String className = "?" ;

	private String viewTemplate = null ;

	private String viewPage = null ;

//	/**
//	 * Constructor
//	 * @param root
//	 * @param name
//	 * @param method
//	 */
//	public ActionInfo(String root, String name, String method) {
//		super();
//		this.root = root;
//		this.name = name;
//		this.method = method;
//	}
	
	public ActionInfo(HttpServletRequest request, String name, String method) {
		super();
		this.root = request.getContextPath() + request.getServletPath() ;
		this.httpServer = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() ;		
		this.name = name;
		this.method = method;
	}

	/**
	 * Returns the current action root path in the current application context <br>
	 * e.g. : '/mywebapp/actionservlet'
	 * @return
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * Returns the current action name that has been executed to produce the current result <br>
	 * e.g. : 'myaction'
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
		return root + "/" + name ;
	}

	/**
	 * Returns the current action absolute URL <br>
	 * e.g. : 'http://myhost:8080/mywebapp/actionservlet/myaction'
	 * @return
	 */
	public String getAbsoluteURL() {
		return httpServer + root + "/" + name ;
	}
	
	/**
	 * Returns the current action method that has been executed to produce the current result
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassName() {
		return className;
	}

	
	public String getViewTemplate() {
		return viewTemplate;
	}

	public void setViewTemplate(String viewTemplate) {
		this.viewTemplate = viewTemplate;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	@Override
	public String toString() {
		return "[root=" + root 
				+ ", name=" + name 
				+ ", method=" + method 
				+ ", className=" + className 
				+ ", viewTemplate=" + viewTemplate 
				+ ", viewPage=" + viewPage 
				+ "]";
	}
	
	
}
