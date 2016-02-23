package org.nanoj.web.tinymvc ;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * TINY MVC Front Controller Servlet
 * 
 * @author Laurent Guerin
 */
public class ActionServlet extends HttpServlet {

	//--- Constants
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_PAGES_FOLDER      = "/WEB-INF/pages/" ;

	private static final String DEFAULT_TEMPLATES_FOLDER  = "/WEB-INF/templates/" ;

	private static final String DEFAULT_PAGES_SUFFIX  = ".jsp" ;

	private static final String METHOD_PARAM_NAME  = "action.method" ;

	
	//--- Attributes
	private String pagesFolder     = DEFAULT_PAGES_FOLDER ;

	private String pagesSuffix     = DEFAULT_PAGES_SUFFIX ;
	
	private String templatesFolder = DEFAULT_TEMPLATES_FOLDER ;

	private String defaultAction   = null ;
	
	private String actionsPackage  = null ;
	
	private String actionProviderClassName  = null ;
	
	private ActionProvider actionProvider   = null ;
			
	private boolean traceFlag   = false ;
	
	private void trace(String msg)
	{
		if ( traceFlag ) {
			String className = this.getClass().getSimpleName() ;
			System.out.println("[TRACE] " + className +  " : " + msg );
		}
	}
	
	
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		//--- Templates Folder
		String paramTemplatesFolder = config.getInitParameter("templatesFolder");
		if ( paramTemplatesFolder != null ) {
			if ( paramTemplatesFolder.endsWith("/") ) {
				this.templatesFolder = paramTemplatesFolder ;
			}
			else {
				this.templatesFolder = paramTemplatesFolder + "/";
			}
		}
		
		//--- Pages Folder
		String paramPagesFolder = config.getInitParameter("pagesFolder");
		if ( paramPagesFolder != null ) {
			if ( paramPagesFolder.endsWith("/") ) {
				this.pagesFolder = paramPagesFolder ;
			}
			else {
				this.pagesFolder = paramPagesFolder + "/";
			}
		}
		
		//--- Page Suffix
		String paramPagesSuffix = config.getInitParameter("pagesSuffix");
		if ( paramPagesSuffix != null ) {
			if ( paramPagesSuffix.startsWith(".") ) {
				this.pagesSuffix = paramPagesSuffix ;
			}
			else {
				this.pagesSuffix = "." + paramPagesSuffix ;
			}
		}

		//--- Default action name
		String defaultActionParameter = config.getInitParameter("defaultAction");
		if ( defaultActionParameter != null ) {
			// LOG Default action defined 
			this.defaultAction = defaultActionParameter ;
		}
		else {
			// LOG Default action not defined 
			this.defaultAction = null ;
		}
		
		//--- Actions package name
		String actionsPackageParameter = config.getInitParameter("actionsPackage");
		if ( actionsPackageParameter != null ) {
			// LOG Default action defined 
			this.actionsPackage = actionsPackageParameter ;
		}
		else {
			// LOG Default action not defined 
			this.actionsPackage = null ;
		}
		
		//--- Action provider 
		String paramActionProvider = config.getInitParameter("actionProvider");
		if ( paramActionProvider != null ) {
			this.actionProviderClassName = paramActionProvider.trim() ;
		}
		else {
			this.actionProviderClassName = null ;
		}
		
		
		trace("Servlet " + this.getClass().getSimpleName() + " initialized :" );
		trace(" . templates folder = '" + this.templatesFolder + "'" ) ;
		trace(" . pages folder     = '" + this.pagesFolder + "'" ) ;
		trace(" . pages suffix     = '" + this.pagesSuffix + "'" ) ;
		trace(" . action provider  = '" + this.actionProviderClassName + "'" ) ;
	}


	@Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	
    	trace("doGet " + request.getRequestURI() );
        process(request, response);
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	trace("doPost " + request.getRequestURI() );
        process(request, response);
    }
    
	/**
     * Process request for both HTTP GET and POST methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	
		//--- Get action information from the request URL
		ActionInfo actionInfo = parseActionURI(request);
		
		//--- Execute the action controller
		String actionResult = executeAction(actionInfo, request, response);
		
		if ( null == actionResult ) {
			throw new TinyMvcException("Action result is null (action " + actionInfo.getClassName() + ")" );
		}

		if ( actionResult.trim().length() == 0 ) {
			throw new TinyMvcException("Action result is void (action " + actionInfo.getClassName() + ")" );
		}

		//--- Dispatch (forward) to VIEW ( with or without template )  
		String target = getForwardTarget( actionResult, actionInfo ) ;
		
		//--- Set action model in request scope
		request.setAttribute("action", actionInfo);

		//--- Forward to target 
		RequestDispatcher requestDispatcher = this.getServletContext().getRequestDispatcher(target) ;		
		requestDispatcher.forward(request, response);
		
    }
    
    /**
     * Parse the URI in order to obtain action information
     * @param request
     * @return
     */
    private ActionInfo parseActionURI( final HttpServletRequest request )  {
		String path = request.getPathInfo();

		trace("process : path = '" + path + "'" );
		
		if ( null == path || "".equals(path) || "/".equals(path) )
		{
			path="/" ; 
		}
		
		String actionString = path.substring(1); //  "/myaction" --> "myaction"

		String actionName = null;
		String methodName = null; // specific method (if any)
		
		//--- Get action name and method name from "PathInfo"
		if ( actionString.length() > 0 ) {
			
			int pointPosition = actionString.indexOf('.') ;
			if ( pointPosition == 0 ) {
				throw new TinyMvcException("Invalid action name (starts with '.')" );
			}
			else if ( pointPosition == actionString.length()-1 ) {
				throw new TinyMvcException("Invalid action name (ends with '.')" );
			}
			else if ( pointPosition > 0 ) {
				actionName = actionString.substring(0, pointPosition) ; // left part
				methodName = actionString.substring(pointPosition+1, actionString.length()); // right part
			}
			else {
				// If < 0 : No point (keep the name as is, no method )
				actionName = actionString ;
				methodName = null ; // no specific method
			}
		}
		else {
			actionName = "" ; // void action name
			methodName = null ; // no specific method
		}
		
		if ( null == methodName ) {
			//--- Try to find the method name in the request parameters ( e.g. for named submit buttons )
			String s = request.getParameter( METHOD_PARAM_NAME );
			if ( s != null ) {
				methodName = s.trim() ;
			}
		}

		trace("process : action name = '" + actionName + "', method name = '" + methodName + "'");
		
		String actionRoot = request.getContextPath() + request.getServletPath() ;
		return new ActionInfo(actionRoot, actionName, methodName );
			
    }
    
    /**
     * Returns the action implementation for the given action name
     * @param actionName
     * @return
     */
    private Action getAction( final String actionName )  {
    	trace("getAction('" + actionName + "')" );
		if ( null == this.actionProvider ) {
			//--- No yet initialized 
			if ( this.actionProviderClassName != null ) {
				//--- Get instance of the action provider class (defined in configuration)
				this.actionProvider = Util.getInstance(this.actionProviderClassName, ActionProvider.class) ;
		    	trace("getAction : action provider initialized ( class = " + this.actionProviderClassName + " )");
			}
			else {
				//--- No action provider class defined => try to use "Convention over Configuration"
				if ( this.actionsPackage != null ) {
			    	trace("getAction : actions package = '" + this.actionsPackage + "'");
					if ( this.defaultAction != null ) {
				    	trace("getAction : default action = '" + this.defaultAction + "'");
					}
					else {
				    	trace("getAction : no default action ");
					}
					this.actionProvider = new DefaultActionProvider( this.actionsPackage, this.defaultAction ) ;
			    	trace("getAction : action provider initialized ( class = " + DefaultActionProvider.class.getCanonicalName() + " )");
				}
				else {
					throw new TinyMvcException("Cannot get action : no action provider class and no actions package " );
				}
			}
		}

		Action action = this.actionProvider.getAction(actionName) ;
		if ( null == action ) {
			throw new TinyMvcException("Cannot get action '" + actionName + "' (not defined)" );
		}
		return action ;
    }
    
    /**
     * Returns template full path
     * @param templateName
     * @return
     */
    private String getTemplateFullPath( final String templateName )  {
    	
    	if ( templateName.endsWith(this.pagesSuffix) ) {
    		return this.templatesFolder + templateName ;
    	}
    	else {
    		return this.templatesFolder + templateName + this.pagesSuffix ;
    	}
    }
    
    /**
     * Returns view page full path
     * @param pageName
     * @return
     */
    private String getPageFullPath( final String pageName )  {
    	
    	if ( pageName.endsWith(this.pagesSuffix) ) {
    		return this.pagesFolder + pageName ;
    	}
    	else {
    		return this.pagesFolder + pageName + this.pagesSuffix ;
    	}
    }
    
    /**
     * Executes the given action <br>
     * Invoke the standard method or the specific method specified in the URI 
     * @param actionInfo
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private String executeAction(final ActionInfo actionInfo, 
    		final HttpServletRequest request, final HttpServletResponse response ) throws ServletException {
    	
		//--- Get the action controller ( associated with the action name )
		Action action = getAction( actionInfo.getName() ) ;
		
		actionInfo.setClassName( action.getClass().getSimpleName() );
		
		//--- Execute the action controller and get the view page 
		String actionResult = null ;
		String actionMethodName = actionInfo.getMethod() ;
		String method = ( actionMethodName != null ? actionMethodName : "process" ) ;
		
		
		try {
			//--- BEFORE ACTION CALL
			action.beforeAction(method, request, response);
			
			//--- ACTION CALL
			if ( actionMethodName != null ) {
				//--- Execute the specific action method
				actionResult = executeSpecificMethod( action, actionMethodName, request, response ) ;
			}
			else {
				//--- Execute the default method "process"
				actionResult = action.process( request, response) ;
			}
		}
		finally {
			//--- AFTER ACTION CALL
			action.afterAction(method, request, response);
		}
		

    	trace ("action result = '" + actionResult + "'");
    	return actionResult ;
    }
    
    /**
     * Execute a specific method using the reflection API
     * @param action
     * @param methodName
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private String executeSpecificMethod( final Action action, final String methodName, 
    		final HttpServletRequest request, final HttpServletResponse response ) throws ServletException {
    	Class<?> clazz = action.getClass();
    	String actionClassName = clazz.getSimpleName() ;
    	
    	//--- 1) Get method
    	Class<?> argTypes[] = { HttpServletRequest.class, HttpServletResponse.class } ;
    	String errorMsg = "Cannot get method '" + methodName + "' in action class '" + actionClassName + "'" ;
    	Method method = null ;
    	try {
			method = clazz.getMethod(methodName, argTypes);
		} catch (SecurityException e) {
			throw new TinyMvcException( errorMsg + " (SecurityException)", e );
		} catch (NoSuchMethodException e) {
			throw new TinyMvcException( errorMsg + " (NoSuchMethodException)", e );
		}

    	//--- 2) invoke method
    	Object arguments[] = { request, response } ;
    	String invokeErrorMsg = "Cannot execute method '" + actionClassName + "." + methodName + "'" ;
    	Object result = null ;
    	try {
    		result = method.invoke(action, arguments );
		} catch (IllegalArgumentException e) {
			throw new TinyMvcException( invokeErrorMsg + " (IllegalArgumentException)", e );
		} catch (IllegalAccessException e) {
			throw new TinyMvcException( invokeErrorMsg + " (IllegalAccessException)", e );
		} catch (InvocationTargetException e) {
			//--- Try to re-throw the original exception if RunTime level
			Throwable cause = e.getCause() ;
			if ( cause != null ) {
//				if ( cause instanceof RuntimeException ) {			
//					throw (RuntimeException)cause ;
//				}
//				else {
//					throw new ServletException(cause);
//				}
				throw new ServletException(cause);
			}
			else if ( e.getTargetException() != null ) {
				throw new ServletException( e.getTargetException() );
			}
			else {
				throw new ServletException(e);
			}
		}
    	
    	if ( result instanceof String ) {
    		return (String) result ;
    	}
    	else {
    		throw new TinyMvcException("The method '" + methodName + "' returned object is not a String" );
    	}
    }

    /**
     * Get the target page to be used in the request dispatcher FORWARD  
     * @param actionResult
     * @param actionInfo
     * @return
     */
    private String getForwardTarget( final String actionResult, final ActionInfo actionInfo ) {
    	String target = null ;
    	String viewTemplate = null ;
    	String viewPage     = null ;
    	if ( actionResult.indexOf('<') >= 0 ) {
    		String [] parts = actionResult.split("<");
    		if ( parts.length == 2 ) {
    			viewTemplate = parts[0].trim() ;
    			viewPage     = parts[1].trim() ;
    		}
    		else {
        		throw new TinyMvcException("Invalid action result '" + actionResult + "'" );
    		}
    	}
    	else {
        	viewTemplate = null ;
        	viewPage     = actionResult.trim() ;
    	}
    	
		trace(" view template = '" + viewTemplate + "' ");
		trace(" view page     = '" + viewPage + "' ");

    	actionInfo.setViewPage    ( getPageFullPath(viewPage) );
    	
    	if ( null != viewTemplate ) {
    		actionInfo.setViewTemplate( getTemplateFullPath(viewTemplate) );
    		target = actionInfo.getViewTemplate() ;
    	}
    	else {
    		actionInfo.setViewTemplate( null );
    		target = actionInfo.getViewPage();
    	}
    	
		trace(" view target = '" + target + "' ");
    	return target ;
    	
    }
    
}
