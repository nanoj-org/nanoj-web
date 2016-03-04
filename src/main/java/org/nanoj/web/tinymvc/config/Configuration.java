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

/**
 * NanoJ Web Configuration ( SINGLE INSTANCE )
 * 
 * @author Laurent Guerin
 */
public class Configuration {

	//--- Parameters names
	protected static final String DEFAULT_ACTION    = "defaultAction" ;
	protected static final String ACTIONS_PATTERN   = "actionsPattern" ;
	protected static final String ACTIONS_PACKAGE   = "actionsPackage" ;
	protected static final String ACTIONS_PROVIDER  = "actionsProvider" ;

	protected static final String VIEWS_TYPE        = "viewsType" ;
	protected static final String VIEWS_FOLDER      = "viewsFolder" ;
	protected static final String VIEWS_SUFFIX      = "viewsSuffix" ;
	protected static final String LAYOUTS_FOLDER    = "layoutsFolder" ;
	protected static final String LAYOUTS_SUFFIX    = "layoutsSuffix" ;
	
	//--- Default values
	private static final String ACTIONS_PATTERN_DEFAULT_VALUE  = "/*" ;
	private static final String DEFAULT_ACTION_DEFAULT_VALUE   = "welcome" ;
	
	private static final String VIEWS_TYPE_DEFAULT_VALUE       = "jsp" ;

	private static final String VIEWS_FOLDER_DEFAULT_VALUE     = "/WEB-INF/views/" ;
	private static final String VIEWS_SUFFIX_DEFAULT_VALUE     = ".jsp" ;
	
	private static final String LAYOUTS_FOLDER_DEFAULT_VALUE   = "/WEB-INF/layouts/" ;
	private static final String LAYOUTS_SUFFIX_DEFAULT_VALUE   = ".jsp" ;

	
	//--- Actions attributes
	private String defaultAction  = DEFAULT_ACTION_DEFAULT_VALUE ; 
	private String actionsPattern = null ; 
	private String actionsPackage = null ; 
	private String actionsProviderClassName = null ; 

	//--- Views and Layouts attributes
	private String    viewsTypeString = VIEWS_TYPE_DEFAULT_VALUE ; 
	private ViewsType viewsType       = null ; 

	private String viewsFolder    = VIEWS_FOLDER_DEFAULT_VALUE ; 
	private String viewsSuffix    = VIEWS_SUFFIX_DEFAULT_VALUE ; 
	
	private String layoutsFolder  = LAYOUTS_FOLDER_DEFAULT_VALUE ; 
	private String layoutsSuffix  = LAYOUTS_SUFFIX_DEFAULT_VALUE ; 

		
	/**
	 * Constructor
	 * @param properties
	 */
	public Configuration(Properties properties) {
		//--- Actions
		setActionsPattern(properties.getProperty(ACTIONS_PATTERN)) ;
		setDefaultAction(properties.getProperty(DEFAULT_ACTION));
		setActionsPackage(properties.getProperty(ACTIONS_PACKAGE)) ;
		setActionsProviderClassName(properties.getProperty(ACTIONS_PROVIDER)) ;
		//--- Views and Layouts
		setViewsType(properties.getProperty(VIEWS_TYPE)) ;
		setViewsFolder(properties.getProperty(VIEWS_FOLDER)) ;
		setViewsSuffix(properties.getProperty(VIEWS_SUFFIX));
		setLayoutsFolder(properties.getProperty(LAYOUTS_FOLDER));
		setLayoutsSuffix(properties.getProperty(LAYOUTS_SUFFIX));
	}
	
	//--------------------------------------------------------------------------------
	/**
	 * Returns the trimmed parameter value or the given default value if null or void
	 * @param paramValue
	 * @param defaultValue
	 * @return
	 */
	private String paramValue(String paramValue, String defaultValue) {
		if ( paramValue != null ) {
			String paramValue2 = paramValue.trim() ;
			if ( paramValue2.length() > 0 ) {
				return paramValue2 ;
			}
		}
		return defaultValue ;
	}
	
	private String suffixValue(String paramValue, String defaultValue) {
		if ( paramValue != null ) {
			if ( paramValue.startsWith(".") ) {
				return paramValue.trim()  ;
			}
			else {
				return "." + paramValue.trim()  ;
			}
		}
		return defaultValue ;
	}

	private String folderValue(String paramValue, String defaultValue) {
		if ( paramValue != null ) {
			if ( paramValue.endsWith("/") ) {
				return paramValue.trim()  ;
			}
			else {
				return paramValue.trim()  + "/";
			}
		}
		return defaultValue ;
	}
	//--------------------------------------------------------------------------------
	private void setViewsType(String param) {
		this.viewsTypeString = paramValue(param, VIEWS_TYPE_DEFAULT_VALUE).toLowerCase();
		if ( "thymeleaf".equals(this.viewsTypeString ) ) {
			this.viewsType = ViewsType.THYMELEAF ;
		}
		else {
			this.viewsType = ViewsType.JSP ;
		}
	}	
	private void setViewsFolder(String param) {
		this.viewsFolder = folderValue(param, VIEWS_FOLDER_DEFAULT_VALUE);
	}	
	private void setViewsSuffix(String param) {
		this.viewsSuffix = suffixValue(param, VIEWS_SUFFIX_DEFAULT_VALUE);
	}	

	private void setLayoutsFolder(String param) {
		this.layoutsFolder = folderValue(param, LAYOUTS_FOLDER_DEFAULT_VALUE);
	}
	private void setLayoutsSuffix(String param) {
		this.layoutsSuffix = suffixValue(param, LAYOUTS_SUFFIX_DEFAULT_VALUE);
	}
	
	private void setActionsPattern(String param) {
		this.actionsPattern = paramValue(param, ACTIONS_PATTERN_DEFAULT_VALUE);
	}
	private void setDefaultAction(String param) {
		this.actionsPackage = paramValue(param, DEFAULT_ACTION_DEFAULT_VALUE);
	}
	private void setActionsPackage(String param) {
		this.actionsPackage = paramValue(param, null);
	}
	private void setActionsProviderClassName(String param) {
		this.actionsProviderClassName = paramValue(param, null);
	}
	
	//--------------------------------------------------------------------------------
	public ViewsType getViewsType() {
		return viewsType;
	}
	public String getViewsFolder() {
		return viewsFolder;
	}
	public String getViewsSuffix() {
		return viewsSuffix;
	}

	public String getLayoutsFolder() {
		return layoutsFolder;
	}
	public String getLayoutsSuffix() {
		return layoutsSuffix;
	}

	public String getDefaultAction() {
		return defaultAction;
	}
	public String getActionsPattern() {
		return actionsPattern;
	}
	public String getActionsPackage() {
		return actionsPackage;
	}
	public String getActionsProviderClassName() {
		return actionsProviderClassName;
	}
}
