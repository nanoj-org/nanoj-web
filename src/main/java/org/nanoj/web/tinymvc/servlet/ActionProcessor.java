package org.nanoj.web.tinymvc.servlet ;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.web.tinymvc.Action;
import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.env.ActionInfo;
import org.nanoj.web.tinymvc.provider.ActionProvider;
import org.nanoj.web.tinymvc.provider.DefaultActionProvider;
import org.nanoj.web.tinymvc.provider.InstanceProvider;


/**
 * Action processing called by the Servlet
 * 
 * @author Laurent GUERIN
 */
public class ActionProcessor {

	//private final ActionServletConfig actionServletConfig ;
	
	private final ActionProvider      actionProvider  ;

	public ActionProcessor(ActionServletConfig actionServletConfig) {
		super();
		//this.actionServletConfig = actionServletConfig;
		
		//--- Init ActionProvider
		String actionProviderClassName = actionServletConfig.getActionProviderClassName() ;
		String actionsPackage          = actionServletConfig.getActionsPackage();
		String defaultAction           = actionServletConfig.getDefaultAction();
		
		if ( actionProviderClassName != null ) {
			//--- Get instance of the action provider class (defined in configuration)
			this.actionProvider = InstanceProvider.createInstance(actionProviderClassName, ActionProvider.class) ;
	    	trace("action provider initialized ( class = " + actionProviderClassName + " )");
		}
		else {
			//--- No action provider class defined => try to use "Convention over Configuration"
			if ( actionsPackage != null ) {
		    	trace("actions package = '" + actionsPackage + "'");
				if ( defaultAction != null ) {
			    	trace("default action = '" + defaultAction + "'");
				}
				else {
			    	trace("no default action ");
				}
				this.actionProvider = new DefaultActionProvider( actionsPackage, defaultAction ) ;
		    	trace("action provider initialized ( class = " + DefaultActionProvider.class.getCanonicalName() + " )");
			}
			else {
				throw new TinyMvcException("Cannot get action : no action provider class and no actions package " );
			}
		}
		
	}

	private void trace(String msg)
	{
//		if ( traceFlag ) {
//			String className = this.getClass().getSimpleName() ;
//			System.out.println("[TRACE] " + className +  " : " + msg );
//		}
	}
	
    /**
     * Executes the given action <br>
     * Invoke the standard method or the specific method specified in the URI 
     * @param actionInfo
     * @param request
     * @param response
     * @return
     */
    protected String executeAction(final ActionInfo actionInfo, 
    		final HttpServletRequest request, final HttpServletResponse response )  {
    	
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
     * Returns the action implementation for the given action name
     * @param actionName
     * @return
     */
    private Action getAction( final String actionName )  {
    	trace("getAction('" + actionName + "')" );
		if ( null == this.actionProvider ) {
			//--- No yet initialized 
//			if ( actionServletConfig.getActionProviderClassName() != null ) {
//				//--- Get instance of the action provider class (defined in configuration)
//				this.actionProvider = InstanceProvider.getInstance(actionServletConfig.getActionProviderClassName(), ActionProvider.class) ;
//		    	trace("getAction : action provider initialized ( class = " + actionServletConfig.getActionProviderClassName() + " )");
//			}
//			else {
//				//--- No action provider class defined => try to use "Convention over Configuration"
//				if ( this.actionsPackage != null ) {
//			    	trace("getAction : actions package = '" + this.actionsPackage + "'");
//					if ( this.defaultAction != null ) {
//				    	trace("getAction : default action = '" + this.defaultAction + "'");
//					}
//					else {
//				    	trace("getAction : no default action ");
//					}
//					this.actionProvider = new DefaultActionProvider( this.actionsPackage, this.defaultAction ) ;
//			    	trace("getAction : action provider initialized ( class = " + DefaultActionProvider.class.getCanonicalName() + " )");
//				}
//				else {
//					throw new TinyMvcException("Cannot get action : no action provider class and no actions package " );
//				}
//			}
			throw new TinyMvcException("Action provider is not initialized");
		}

		Action action = this.actionProvider.getAction(actionName) ;
		if ( null == action ) {
			throw new TinyMvcException("Cannot get action '" + actionName + "' (not defined)" );
		}
		return action ;
    }
    
//    /**
//     * Returns template full path
//     * @param templateName
//     * @return
//     */
//    private String getTemplateFullPath( final String templateName )  {
//    	
//    	if ( templateName.endsWith(this.pagesSuffix) ) {
//    		return this.templatesFolder + templateName ;
//    	}
//    	else {
//    		return this.templatesFolder + templateName + this.pagesSuffix ;
//    	}
//    }
//    
//    /**
//     * Returns view page full path
//     * @param pageName
//     * @return
//     */
//    private String getPageFullPath( final String pageName )  {
//    	
//    	if ( pageName.endsWith(this.pagesSuffix) ) {
//    		return this.pagesFolder + pageName ;
//    	}
//    	else {
//    		return this.pagesFolder + pageName + this.pagesSuffix ;
//    	}
//    }
    
    /**
     * Execute a specific method using the reflection API
     * @param action
     * @param methodName
     * @param request
     * @param response
     * @return
     */
    private String executeSpecificMethod( final Action action, final String methodName, 
    		final HttpServletRequest request, final HttpServletResponse response ) { // throws ServletException {
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
				throw new TinyMvcException(cause);
			}
			else if ( e.getTargetException() != null ) {
				throw new TinyMvcException( e.getTargetException() );
			}
			else {
				throw new TinyMvcException(e);
			}
		}
    	
    	if ( result instanceof String ) {
    		return (String) result ;
    	}
    	else {
    		throw new TinyMvcException("The method '" + methodName + "' returned object is not a String" );
    	}
    }
    
}
