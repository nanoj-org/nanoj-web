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
package org.nanoj.web.tinymvc.servlet ;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.web.tinymvc.Action;
import org.nanoj.web.tinymvc.Configuration;
import org.nanoj.web.tinymvc.Const;
import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.env.ActionInfo;
import org.nanoj.web.tinymvc.env.FieldValuesManager;
import org.nanoj.web.tinymvc.provider.ActionProvider;
import org.nanoj.web.tinymvc.provider.DefaultActionProvider;
import org.nanoj.web.tinymvc.provider.InstanceProvider;


/**
 * Action processing called by the Servlet ( SINGLE INSTANCE )
 * 
 * @author Laurent GUERIN
 */
public class ActionProcessor {

	private final ActionProvider      actionProvider  ;
	private final FieldValuesManager  fieldValuesManager ;


	private void trace(String msg) {
		
	}
	
	public ActionProcessor(Configuration actionServletConfig) {
		super();
		
		//--- Initialize FieldValuesManager
		fieldValuesManager = FieldValuesManager.getInstance() ;
		
		//--- Initialize ActionProvider
		String actionProviderClassName = actionServletConfig.getActionsProviderClassName() ;
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

    /**
     * Executes the given action <br>
     * Invoke the standard method or the specific method specified in the URI 
     * @param actionInfo
     * @param request
     * @param response
     * @return
     */
    public String executeAction(final ActionInfo actionInfo, 
    		final HttpServletRequest request, final HttpServletResponse response )  {
    	
		//--- Get the action controller ( associated with the action name )
		Action action = getAction( actionInfo ) ;

		//--- Initialize the "action.method" value 
		request.setAttribute(Const.ACTION_METHOD_ATTRIBUTE_NAME, Const.ACTION_METHOD_PARAMETER_NAME);
		
		//--- Initialize the "fieldvalue" object stored as a request attribute
		fieldValuesManager.setFieldValuesFromParameters(request);
		
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
    private Action getAction( final ActionInfo actionInfo )  {
    	
    	trace("getAction('" + actionInfo.getName() + "')" );
    	
		if ( null == this.actionProvider ) {
			throw new TinyMvcException("Action provider is not initialized");
		}
		
		Action action = this.actionProvider.getAction( actionInfo.getName() ) ;
		if ( null == action ) {
			throw new TinyMvcException("Cannot get action '" + actionInfo.getName() + "' (not defined)" );
		}
		
		actionInfo.setClassName( action.getClass().getSimpleName() );
		
		return action ;
    }
    
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
				throw new TinyMvcException(invokeErrorMsg, cause);
			}
			else if ( e.getTargetException() != null ) {
				throw new TinyMvcException(invokeErrorMsg,  e.getTargetException() );
			}
			else {
				throw new TinyMvcException(invokeErrorMsg, e);
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
