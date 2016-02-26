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

import javax.servlet.ServletConfig;


/**
 * Action Servlet Configuration
 * 
 * @author Laurent Guerin
 */
public class ActionServletConfig {

	//--- Constants
	private static final String DEFAULT_PAGES_FOLDER      = "/WEB-INF/pages/" ;

	private static final String DEFAULT_TEMPLATES_FOLDER  = "/WEB-INF/templates/" ;

	private static final String DEFAULT_PAGES_SUFFIX      = ".jsp" ;

//	private static final String METHOD_PARAM_NAME         = "action.method" ;

	
	//--- Attributes
	private final String pagesFolder ; //   = DEFAULT_PAGES_FOLDER

	private final String pagesSuffix ; //    = DEFAULT_PAGES_SUFFIX ;
	
	private final String templatesFolder ; //  = DEFAULT_TEMPLATES_FOLDER ;

	private final String defaultAction ; //  = null ;
	
	private final String actionsPackage ; // = null ;
	
	private final String actionProviderClassName ; // = null ;
	
//	private ActionProvider actionProvider   = null ;
			
	
	/**
	 * Constructor
	 * @param config
	 */
	protected ActionServletConfig(ServletConfig config) {
		
		//--- Templates Folder
		String paramTemplatesFolder = config.getInitParameter("templatesFolder");
		if ( paramTemplatesFolder != null ) {
			if ( paramTemplatesFolder.endsWith("/") ) {
				this.templatesFolder = paramTemplatesFolder.trim()  ;
			}
			else {
				this.templatesFolder = paramTemplatesFolder.trim()  + "/";
			}
		}
		else {
			this.templatesFolder = DEFAULT_TEMPLATES_FOLDER ;
		}
		
		//--- Pages Folder
		String paramPagesFolder = config.getInitParameter("pagesFolder");
		if ( paramPagesFolder != null ) {
			if ( paramPagesFolder.endsWith("/") ) {
				this.pagesFolder = paramPagesFolder.trim()  ;
			}
			else {
				this.pagesFolder = paramPagesFolder.trim() + "/";
			}
		}
		else {
			this.pagesFolder = DEFAULT_PAGES_FOLDER ;
		}
		
		//--- Pages Suffix
		String paramPagesSuffix = config.getInitParameter("pagesSuffix");
		if ( paramPagesSuffix != null ) {
			if ( paramPagesSuffix.startsWith(".") ) {
				this.pagesSuffix = paramPagesSuffix.trim()  ;
			}
			else {
				this.pagesSuffix = "." + paramPagesSuffix.trim()  ;
			}
		}
		else {
			this.pagesSuffix = DEFAULT_PAGES_SUFFIX ;
		}

		//--- Default action name
		String defaultActionParameter = config.getInitParameter("defaultAction");
		if ( defaultActionParameter != null ) {
			// LOG Default action defined 
			this.defaultAction = defaultActionParameter.trim() ;
		}
		else {
			// LOG Default action not defined 
			this.defaultAction = null ;
		}
		
		//--- Actions package name
		String actionsPackageParameter = config.getInitParameter("actionsPackage");
		if ( actionsPackageParameter != null ) {
			// LOG Default action defined 
			this.actionsPackage = actionsPackageParameter.trim()  ;
		}
		else {
			// LOG Default action not defined 
			this.actionsPackage = null ;
		}
		
		//--- Action provider class name
		String paramActionProvider = config.getInitParameter("actionProvider");
		if ( paramActionProvider != null ) {
			this.actionProviderClassName = paramActionProvider.trim() ;
		}
		else {
			this.actionProviderClassName = null ;
		}
		
	}

	public String getPagesFolder() {
		return pagesFolder;
	}

	public String getPagesSuffix() {
		return pagesSuffix;
	}

	public String getTemplatesFolder() {
		return templatesFolder;
	}

	public String getDefaultAction() {
		return defaultAction;
	}

	public String getActionsPackage() {
		return actionsPackage;
	}

	public String getActionProviderClassName() {
		return actionProviderClassName;
	}

}
