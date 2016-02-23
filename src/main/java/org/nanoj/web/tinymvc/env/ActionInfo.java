package org.nanoj.web.tinymvc.env ;

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
	
	private final String name ;
	
	private final String method ;

	private String className = "?" ;

	private String viewTemplate = null ;

	private String viewPage = null ;

	/**
	 * Constructor
	 * @param root
	 * @param name
	 * @param method
	 */
	public ActionInfo(String root, String name, String method) {
		super();
		this.root = root;
		this.name = name;
		this.method = method;
	}

	/**
	 * Returns the current action root path in the current application context
	 * @return
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * Returns the current action name that has been executed to produce the current result
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
