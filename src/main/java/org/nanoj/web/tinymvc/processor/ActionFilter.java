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
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.config.Configuration;
import org.nanoj.web.tinymvc.config.ConfigurationLoader;
import org.nanoj.web.tinymvc.env.ActionInfo;

@WebFilter(urlPatterns = {"/*"} )
public class ActionFilter implements Filter  {
	
	private final Configuration      configuration ;
	private final ActionParser       actionParser  ;
	private final ActionProcessor    actionProcessor ;
	private final ActionViewRenderer actionViewRenderer ;
	
	
	private boolean traceFlag   = true ;
	private void trace(String msg) {
		if ( traceFlag ) {
			String className = this.getClass().getSimpleName() ;
			System.out.println("[TRACE] " + className +  " : " + msg );
		}
	}

	/**
	 * Constructor
	 */
	public ActionFilter() {
		super();
        trace("constructor()");
		ConfigurationLoader configurationLoader = new ConfigurationLoader() ;
		this.configuration = configurationLoader.loadConfiguration();
		this.actionParser       = new ActionParser() ;
		this.actionProcessor    = new ActionProcessor(configuration);
		this.actionViewRenderer = new ActionViewRenderer(configuration);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
        trace("init()");
		//trace("Filter " + this.getClass().getSimpleName() + " initialized :" );
		trace(" . actions package        = '" + this.configuration.getActionsPackage() + "'" ) ;
		trace(" . default action         = '" + this.configuration.getDefaultAction() + "'" ) ;
		trace(" . actions provider class = '" + this.configuration.getActionsProviderClassName() + "'" ) ;
		trace(" . views folder           = '" + this.configuration.getViewsFolder() + "'" ) ;
		trace(" . views suffix           = '" + this.configuration.getViewsSuffix() + "'" ) ;
		trace(" . layouts folder         = '" + this.configuration.getLayoutsFolder() + "'" ) ;
	}
	
	@Override
	public void destroy() {
        trace("destroy()");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
						throws IOException, ServletException {
        trace("doFilter()");
//		if ( request in action path ) {
//			processAction(servletRequest, servletResponse);
//		}
//		else {
//	        chain.doFilter(request, response);			
//		}
		processAction(servletRequest, servletResponse);
	}
	
	public void processAction(ServletRequest servletRequest, ServletResponse servletResponse) {
        final HttpServletRequest  request  = (HttpServletRequest)  servletRequest ;
        final HttpServletResponse response = (HttpServletResponse) servletResponse ;
        
		//--- 1) Get action information from the request URL
		ActionInfo actionInfo = actionParser.parseActionURI(request);
		trace("--- ActionInfo : " + actionInfo );
		
		//--- 2) Execute the action controller
		String actionResult = actionProcessor.executeAction(actionInfo, request, response);
		
		if ( actionResult == null ) {
			throw new TinyMvcException("Action result is null (action " + actionInfo.getClassName() + ")" );
		}
		if ( actionResult.trim().length() == 0 ) {
			throw new TinyMvcException("Action result is void (action " + actionInfo.getClassName() + ")" );
		}

		//--- 3) Dispatch (forward) to VIEW ( with or without template )  				
		actionViewRenderer.render(actionResult, actionInfo, request, response);
	}
}
