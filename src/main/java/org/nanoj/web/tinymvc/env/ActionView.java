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

import org.nanoj.util.StrUtil;
import org.nanoj.web.tinymvc.config.Configuration;

/**
 * Object containing information about the action view <br>
 * 
 * @author Laurent GUERIN
 */
public class ActionView {
	
	//--- Attributes
	private final String originalResult ;
	private final String pageName ;
	private final String pagePath ;
	private final String layoutName ;
	private final String layoutPath ;

	/**
	 * Constructor
	 * @param actionResult actionResult string : 'view' or 'view:layout'
	 * @param configuration
	 */
	public ActionView( String actionResult, Configuration configuration ) {
		super();
		if ( StrUtil.nullOrVoid(actionResult) ) {
			throw new IllegalArgumentException("Action result is null or void");
		}
		this.originalResult = actionResult ;
		// Simple names
		int pos = actionResult.indexOf(':') ; 
		if ( pos < 0 ) {
			// No ':'
			pageName = actionResult.trim() ;
			layoutName = null ;
		}
		else {
			pageName   = actionResult.substring(0, pos).trim() ;
			String s = actionResult.substring(pos+1, actionResult.length()).trim() ;
			if ( s.length() == 0 ) {
				layoutName = null ;
			}
			else {
				layoutName = s ;
			}
		}
		if ( pageName.length() == 0) {
			throw new IllegalArgumentException("Action view is void");
		}

		// Full paths
		pagePath = getPageFullPath(pageName, configuration);
		if ( layoutName != null ) {
			layoutPath = getLayoutFullPath(layoutName, configuration);
		}
		else {
			layoutPath = null ;
		}
	}

	//------------------------------------------------------------------------------
	/**
	 * Returns original result (never null) <br>
	 * e.g. : "mypage" or "mypage : mylayout"
	 * @return 
	 */
	public String getOriginalResult() {
    	return originalResult;
    }

	//------------------------------------------------------------------------------
	/**
	 * Returns the page name in short format (never null) <br>
	 * e.g. : "mypage"
	 * @return 
	 */
	public String getPageName() {
    	return pageName;
    }
	/**
	 * Returns the page path in the current web-app (never null) <br>
	 * e.g. : "/WEB-INF/views/mypage.jsp"
	 * @return
	 */
	public String getPagePath() {
    	return pagePath;
    }

	//------------------------------------------------------------------------------
	/**
	 * Returns the layout name in short format (can be null) <br>
	 * e.g. : "mylayout"
	 * @return
	 */
	public String getLayoutName() {
    	return layoutName;
    }
	/**
	 * Returns the layout path in the current web-app (can be null) <br>
	 * e.g. : "/WEB-INF/layouts/mylayout.jsp"
	 * @return
	 */
	public String getLayoutPath() {
    	return layoutPath;
    }
	/**
	 * Returns true if the action result has a layout 
	 * @return
	 */
	public boolean hasLayout() {
    	return layoutName != null ;
    }

	//------------------------------------------------------------------------------
	/**
	 * Returns the view name ( page name or layout name if any ) <br>
	 * e.g. : "mypage" or "mylayout"
	 * @return
	 */
	public String getViewName() {
		if ( layoutName != null ) {
	    	return layoutName;
		}
		else {
			return pageName;
		}
    }

	/**
	 * Returns the view path ( page path or layout path if any ) <br>
	 * e.g. : "/WEB-INF/views/mypage.jsp" or "/WEB-INF/layouts/mylayout.jsp"
	 * @return
	 */
	public String getViewPath() {
		if ( layoutPath != null ) {
	    	return layoutPath;
		}
		else {
			return pagePath;
		}
    }
	
	//------------------------------------------------------------------------------
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
