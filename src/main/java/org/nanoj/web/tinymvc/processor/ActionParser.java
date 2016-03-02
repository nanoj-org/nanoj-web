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
package org.nanoj.web.tinymvc.processor ;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.nanoj.util.ConsoleLoggerProvider;
import org.nanoj.util.StrUtil;
import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.env.ActionInfo;

/**
 * Action parser ( SINGLE INSTANCE ) <br>
 * 
 * @author Laurent GUERIN
 */
public class ActionParser {
	
	private final static Logger LOGGER = ConsoleLoggerProvider.getLogger(ActionParser.class); 
	
	private final String defaultActionName ;

    /**
     * Constructor
     * @param defaultActionName
     */
    public ActionParser(String defaultActionName) {
		super();
		this.defaultActionName = defaultActionName;
	}
    
	/**
     * Parses the given request URI in order to obtain action information
     * @param request
     * @return
     */
    public ActionInfo parseActionURI( final HttpServletRequest request )  {
		String requestURI = request.getRequestURI() ;
		LOGGER.info("request.getRequestURI() : '" + requestURI + "'");
		// Examples :
		//   . /mywebapp/aaaa/bbb/ccc

		
		String[] parts = StrUtil.split(requestURI, '/') ;
		String actionString = "" ;
		if ( parts.length > 0 ) {
			actionString = parts[parts.length-1];
		}
		LOGGER.info("actionString : '" + actionString + "'"); // 'myaction' or 'myaction.method' or ''
		
		
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
		
		//--- If action name is still undefined => try to set the default action name
		if ( StrUtil.nullOrVoid(actionName) ) {
			if ( this.defaultActionName != null ) {
				actionName = this.defaultActionName ;
			}
			else {
				throw new TinyMvcException("No action name and no default action" );
			}
		}

		if ( methodName == null ) {
			//--- Try to find the method name in the request parameters ( e.g. for named submit buttons )
			String s = request.getParameter( Const.ACTION_METHOD_PARAMETER_NAME );
			if ( s != null ) {
				methodName = s.trim() ;
			}
		}

		return new ActionInfo(request, actionName, methodName );
			
    }

}
