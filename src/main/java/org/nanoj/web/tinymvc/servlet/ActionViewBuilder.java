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

import org.nanoj.web.tinymvc.Configuration;
import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.env.ActionInfo;


/**
 * TINY MVC View Builder
 * 
 * @author Laurent Guerin
 */
public class ActionViewBuilder {
	
	//--- Attributes
	private final String viewsFolder ; 

	private final String pagesSuffix ; 
	
	private final String layoutsFolder ; 


    public ActionViewBuilder( Configuration actionServletConfig ) {
		super();
		this.viewsFolder = actionServletConfig.getViewsFolder();
		this.pagesSuffix = actionServletConfig.getViewsSuffix();
		this.layoutsFolder = actionServletConfig.getLayoutsFolder();
	}

	/**
     * Returns the target page to be used in the request dispatcher FORWARD  
     * @param actionResult
     * @param actionInfo
     * @return
     */
    protected String getTargetPage( final String actionResult, final ActionInfo actionInfo ) {
    	//String target = null ;
    	String viewLayout = null ;
    	String viewPage     = null ;
    	if ( actionResult.indexOf('<') >= 0 ) {
    		String [] parts = actionResult.split("<");
    		if ( parts.length == 2 ) {
    			viewLayout = parts[0].trim() ;
    			viewPage     = parts[1].trim() ;
    		}
    		else {
        		throw new TinyMvcException("Invalid action result '" + actionResult + "'" );
    		}
    	}
    	else {
        	viewLayout = null ;
        	viewPage     = actionResult.trim() ;
    	}
    	
//		trace(" view template = '" + viewTemplate + "' ");
//		trace(" view page     = '" + viewPage + "' ");

    	actionInfo.setViewPage( getPageFullPath(viewPage) );
    	
    	if ( null != viewLayout ) {
    		actionInfo.setViewLayout( getLayoutFullPath(viewLayout) );
    		return actionInfo.getViewLayout() ;
    	}
    	else {
    		actionInfo.setViewLayout( null );
    		return actionInfo.getViewPage();
    	}
    	
//		trace(" view target = '" + target + "' ");
//    	return target ;
    }

    /**
     * Returns view page full path
     * @param pageName
     * @return
     */
    private String getPageFullPath( final String pageName )  {
    	
    	if ( pageName.endsWith(this.pagesSuffix) ) {
    		return this.viewsFolder + pageName ;
    	}
    	else {
    		return this.viewsFolder + pageName + this.pagesSuffix ;
    	}
    	
//    	if ( pageName.endsWith( actionServletConfig.getPagesSuffix() ) ) {
//    		return actionServletConfig.getPagesFolder() + pageName ;
//    	}
//    	else {
//    		return actionServletConfig.getPagesFolder() + pageName + actionServletConfig.getPagesSuffix()  ;
//    	}
    }

    /**
     * Returns template full path
     * @param templateName
     * @return
     */
    private String getLayoutFullPath( final String templateName )  {
    	
    	if ( templateName.endsWith(this.pagesSuffix) ) {
    		return this.layoutsFolder + templateName ;
    	}
    	else {
    		return this.layoutsFolder + templateName + this.pagesSuffix ;
    	}
    	
//    	if ( templateName.endsWith( actionServletConfig.getPagesSuffix() ) ) {
//    		return actionServletConfig.getTemplatesFolder() + templateName ;
//    	}
//    	else {
//    		return actionServletConfig.getTemplatesFolder() + templateName + actionServletConfig.getPagesSuffix() ;
//    	}
    }
    
    
}
