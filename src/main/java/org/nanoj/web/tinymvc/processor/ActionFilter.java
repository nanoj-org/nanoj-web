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
package org.nanoj.web.tinymvc.processor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.web.tinymvc.config.Configuration;
import org.nanoj.web.tinymvc.env.ActionInfo;
import org.nanoj.web.tinymvc.util.ConsoleLogger;

//@WebFilter( filterName="zzz-nanoj", urlPatterns = {"/*"} )
//@WebFilter( filterName="aaa-nanoj", urlPatterns = {"/*"} )
public class ActionFilter implements Filter  {
	
	private final Configuration      configuration ;
	private final ActionProcessor    actionProcessor ;
	
	private final ConsoleLogger      logger = ConsoleLogger.getLogger(ActionFilter.class);

	/**
	 * Constructor
	 */
	public ActionFilter(Configuration configuration) {
		super();
		logger.trace("constructor()");
		// ConfigurationLoader configurationLoader = new ConfigurationLoader() ;
		// this.configuration = configurationLoader.loadConfiguration();
		this.configuration = configuration ;
		this.actionProcessor = new ActionProcessor(configuration);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("NanoJ filer initialization");
		//trace("Filter " + this.getClass().getSimpleName() + " initialized :" );
		logger.info(" . actions package        = '" + this.configuration.getActionsPackage() + "'" ) ;
		logger.info(" . default action         = '" + this.configuration.getDefaultAction() + "'" ) ;
		logger.info(" . actions provider class = '" + this.configuration.getActionsProviderClassName() + "'" ) ;
		logger.info(" . views folder           = '" + this.configuration.getViewsFolder() + "'" ) ;
		logger.info(" . views suffix           = '" + this.configuration.getViewsSuffix() + "'" ) ;
		logger.info(" . layouts folder         = '" + this.configuration.getLayoutsFolder() + "'" ) ;
		logger.info(" . layouts suffix         = '" + this.configuration.getLayoutsSuffix() + "'" ) ;
		logger.info(" . views type             = '" + this.configuration.getViewsType() + "'" ) ;
	}
	
	@Override
	public void destroy() {
		logger.trace("destroy()");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
						throws IOException, ServletException {
		logger.trace("doFilter()");
        
        ActionInfo actionInfo = actionProcessor.processAction( (HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse );
        
        logger.trace("action '" + actionInfo.getName() +"' "
        		+ "--> " + actionInfo.getClassName() + "." + actionInfo.getMethodCalled() + "() "
        		+ "--> '" + actionInfo.getResult() + "' "
        		+ "--> '" + actionInfo.getView() + "' " 
        		);
	}
	
}
