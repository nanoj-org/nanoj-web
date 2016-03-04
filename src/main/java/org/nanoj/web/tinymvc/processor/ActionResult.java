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

import org.nanoj.util.StrUtil;
import org.nanoj.web.tinymvc.config.Configuration;

/**
 * TINY MVC Action Result
 * 
 * @author Laurent Guerin
 */
public class ActionResult {
	
	//--- Attributes
	private final String view ;
	private final String viewFullPath ;
	private final String layout ;
	private final String layoutFullPath ;

	/**
	 * Constructor
	 * @param actionResult actionResult string : 'view' or 'view:layout'
	 * @param configuration
	 */
	public ActionResult( String actionResult, Configuration configuration ) {
		super();
		if ( StrUtil.nullOrVoid(actionResult) ) {
			throw new IllegalArgumentException("Action result is null or void");
		}
		
		// Simple names
		int pos = actionResult.indexOf(':') ; 
		if ( pos < 0 ) {
			// No ':'
			view = actionResult.trim() ;
			layout = null ;
		}
		else {
			view   = actionResult.substring(0, pos).trim() ;
			String s = actionResult.substring(pos+1, actionResult.length()).trim() ;
			if ( s.length() == 0 ) {
				layout = null ;
			}
			else {
				layout = s ;
			}
		}
		if ( view.length() == 0) {
			throw new IllegalArgumentException("Action view is void");
		}

		// Full paths
		viewFullPath = getPageFullPath(view, configuration);
		if ( layout != null ) {
			layoutFullPath = getLayoutFullPath(layout, configuration);
		}
		else {
			layoutFullPath = null ;
		}
	}

	//------------------------------------------------------------------------------
	/**
	 * Returns the view name in short format (never null) <br>
	 * e.g. : "mypage"
	 * @return 
	 */
	public String getView() {
    	return view;
    }
	/**
	 * Returns the view in 'full path' format (never null) <br>
	 * e.g. : "/WEB-INF/views/mypage.jsp"
	 * @return
	 */
	public String getViewFullPath() {
    	return viewFullPath;
    }

	//------------------------------------------------------------------------------
	/**
	 * Returns the layout name in short format (can be null) <br>
	 * e.g. : "mylayout"
	 * @return
	 */
	public String getLayout() {
    	return layout;
    }
	/**
	 * Returns the layout name in 'full path'  format (can be null) <br>
	 * e.g. : "/WEB-INF/layouts/mylayout.jsp"
	 * @return
	 */
	public String getLayoutFullPath() {
    	return layoutFullPath;
    }
	/**
	 * Returns true if the action result has a layout 
	 * @return
	 */
	public boolean hasLayout() {
    	return layout != null ;
    }

	//------------------------------------------------------------------------------
	/**
	 * Returns the target full path ( view page full path or layout full path )
	 * @return
	 */
	public String getTargetFullPath() {
		if ( layoutFullPath != null ) {
	    	return layoutFullPath;
		}
		else {
			return viewFullPath;
		}
    }
	
    /**
     * Returns view page full path
     * @param pageName
     * @return
     */
    private String getPageFullPath( String pageName, Configuration configuration ) {
    	
    	if ( pageName.endsWith( configuration.getViewsSuffix() ) ) {
    		return configuration.getViewsFolder() + pageName ;
    	}
    	else {
    		return configuration.getViewsFolder() + pageName + configuration.getViewsSuffix() ;
    	}    	
    }

    /**
     * Returns layout full path
     * @param layoutName
     * @return
     */
    private String getLayoutFullPath( String layoutName, Configuration configuration ) {
    	
    	if ( layoutName.endsWith( configuration.getViewsSuffix() ) ) {
    		return configuration.getLayoutsFolder() + layoutName ;
    	}
    	else {
    		return configuration.getLayoutsFolder() + layoutName + configuration.getViewsSuffix() ;
    	}    	
    }


}
