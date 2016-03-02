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
package org.nanoj.web.tinymvc.config ;

import java.util.Properties;

import javax.servlet.ServletConfig;


/**
 * NanoJ Web Configuration ( SINGLE INSTANCE )
 * 
 * @author Laurent Guerin
 */
public class Configuration {

	//--- Parameters names
	private static final String LAYOUTS_FOLDER    = "layoutsFolder" ;
	private static final String VIEWS_FOLDER      = "viewsFolder" ;
	private static final String VIEWS_SUFFIX      = "viewsSuffix" ;
	private static final String DEFAULT_ACTION    = "defaultAction" ;
	private static final String ACTIONS_PACKAGE   = "actionsPackage" ;
	private static final String ACTIONS_PROVIDER  = "actionsProvider" ;
	
	//--- Default values
	private static final String VIEWS_FOLDER_DEFAULT_VALUE    = "/WEB-INF/views/" ;
	private static final String VIEWS_SUFFIX_DEFAULT_VALUE    = ".jsp" ;
	private static final String LAYOUTS_FOLDER_DEFAULT_VALUE  = "/WEB-INF/layouts/" ;
	private static final String DEFAULT_ACTION_DEFAULT_VALUE  = "welcome" ;

	
	//--- Attributes
	private String viewsFolder    = VIEWS_FOLDER_DEFAULT_VALUE ; 

	private String viewsSuffix    = VIEWS_SUFFIX_DEFAULT_VALUE ; 
	
	private String layoutsFolder  = LAYOUTS_FOLDER_DEFAULT_VALUE ; 

	private String defaultAction  = DEFAULT_ACTION_DEFAULT_VALUE ; 
	
	private String actionsPackage = null ; 
	
	private String actionsProviderClassName = null ; 
		
	/**
	 * Constructor
	 * @param config
	 */
	public Configuration(ServletConfig config) {
		setLayoutsFolder(config.getInitParameter(LAYOUTS_FOLDER));
		setViewsFolder(config.getInitParameter(VIEWS_FOLDER)) ;
		setViewsSuffix(config.getInitParameter(VIEWS_SUFFIX));
		setDefaultAction(config.getInitParameter(DEFAULT_ACTION));
		setActionsPackage(config.getInitParameter(ACTIONS_PACKAGE)) ;
		setActionsProviderClassName(config.getInitParameter(ACTIONS_PROVIDER)) ;
	}
	
	/**
	 * Constructor
	 * @param properties
	 */
	public Configuration(Properties properties) {
		setLayoutsFolder(properties.getProperty(LAYOUTS_FOLDER));		
		setViewsFolder(properties.getProperty(VIEWS_FOLDER)) ;
		setViewsSuffix(properties.getProperty(VIEWS_SUFFIX));
		setDefaultAction(properties.getProperty(DEFAULT_ACTION));
		setActionsPackage(properties.getProperty(ACTIONS_PACKAGE)) ;
		setActionsProviderClassName(properties.getProperty(ACTIONS_PROVIDER)) ;
	}
	
	//--------------------------------------------------------------------------------
	private void setLayoutsFolder(String param) {
		if ( param != null ) {
			if ( param.endsWith("/") ) {
				this.layoutsFolder = param.trim()  ;
			}
			else {
				this.layoutsFolder = param.trim()  + "/";
			}
		}
		else {
			this.layoutsFolder = LAYOUTS_FOLDER_DEFAULT_VALUE ;
		}
	}

	private void setViewsFolder(String param) {
		if ( param != null ) {
			if ( param.endsWith("/") ) {
				this.viewsFolder = param.trim()  ;
			}
			else {
				this.viewsFolder = param.trim() + "/";
			}
		}
		else {
			this.viewsFolder = VIEWS_FOLDER_DEFAULT_VALUE ;
		}
	}
	
	private void setViewsSuffix(String param) {
		if ( param != null ) {
			if ( param.startsWith(".") ) {
				this.viewsSuffix = param.trim()  ;
			}
			else {
				this.viewsSuffix = "." + param.trim()  ;
			}
		}
		else {
			this.viewsSuffix = VIEWS_SUFFIX_DEFAULT_VALUE ;
		}
	}	

	private void setDefaultAction(String param) {
		if ( param != null ) {
			this.defaultAction = param.trim() ;
		}
		else {
			this.defaultAction = DEFAULT_ACTION_DEFAULT_VALUE ;
		}
	}

	private void setActionsPackage(String param) {
		if ( param != null ) {
			this.actionsPackage = param.trim()  ;
		}
		else {
			this.actionsPackage = null ;
		}
	}

	private void setActionsProviderClassName(String param) {
		if ( param != null ) {
			this.actionsProviderClassName = param.trim() ;
		}
		else {
			this.actionsProviderClassName = null ;
		}		
	}
	
	//--------------------------------------------------------------------------------
	public String getViewsFolder() {
		return viewsFolder;
	}

	public String getViewsSuffix() {
		return viewsSuffix;
	}

	public String getLayoutsFolder() {
		return layoutsFolder;
	}

	public String getDefaultAction() {
		return defaultAction;
	}

	public String getActionsPackage() {
		return actionsPackage;
	}

	public String getActionsProviderClassName() {
		return actionsProviderClassName;
	}

}
