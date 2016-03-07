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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.util.StrUtil;
import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.config.Configuration;
import org.nanoj.web.tinymvc.config.ViewsType;
import org.nanoj.web.tinymvc.env.ActionInfo;
import org.nanoj.web.tinymvc.env.ActionView;
import org.nanoj.web.tinymvc.util.ConsoleLogger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;


/**
 * TINY MVC Action Renderer
 * 
 * @author Laurent Guerin
 */
public class ActionViewRenderer {
	
	private final ConsoleLogger  logger = ConsoleLogger.getLogger(ActionViewRenderer.class);

	private final Configuration  configuration ;

	public ActionViewRenderer( Configuration configuration ) {
		super();
		this.configuration = configuration ;
	}

    public ActionView render( final String actionResultString, final ActionInfo actionInfo, 
    		final HttpServletRequest request, final HttpServletResponse response) {
		
		ActionView actionView = new ActionView(actionResultString, configuration);

//    	actionInfo.setViewPage( actionView.getViewPath() );
//    	actionInfo.setViewLayout( actionView.getLayoutPath() );
    	actionInfo.setActionView(actionView);
    	
		//--- Set action model in request scope
		request.setAttribute("action", actionInfo);

		if ( configuration.getViewsType() == ViewsType.THYMELEAF ) {
			logger.trace("render with Thymeleaf...");
			renderWithThymeleaf( actionInfo, actionView, request, response);
		}
		else {
			logger.trace("render with JSP...");
			renderWithJSP( actionInfo, actionView, request, response); 
		}
		return actionView ;
    }

    private void renderWithJSP( ActionInfo actionInfo, ActionView actionView,
    		HttpServletRequest request, HttpServletResponse response) {
    	
		String targetPage = actionView.getViewPath();
		if ( StrUtil.nullOrVoid(targetPage) ) {
			throw new TinyMvcException("No target page for action '" + actionInfo.getName() + "'");
		}

		ServletContext servletContext = request.getServletContext();
		//--- Forward to target 
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(targetPage) ;
		
		try {
			requestDispatcher.forward(request, response);
		} catch (ServletException e) {
			throw new TinyMvcException("Cannot render JSP (getRequestDispatcher : ServletException)", e);
		} catch (IOException e) {
			throw new TinyMvcException("Cannot render JSP (getRequestDispatcher : IOException)", e);
		}
    }

    private void renderWithThymeleaf( ActionInfo actionInfo, ActionView actionView,
    		HttpServletRequest request, HttpServletResponse response) {
    	
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        // XHTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCharacterEncoding("utf-8");

        TemplateEngine templateEngine = new TemplateEngine();

        //--- Define template with prefix and suffix 
        String thymeleafTemplate = "?";
        if ( actionView.hasLayout() ) {
        	
        	thymeleafTemplate = actionView.getLayoutName();
        	
        	//--- Multiple TemplateResolvers       	
            logger.trace("Thymeleaf : 2 TemplateResolvers ( view + layout ) ");
        	Set<TemplateResolver> templateResolvers = new HashSet<TemplateResolver>();
        	templateResolvers.add( ThymeleafUtil.getViewTemplateResolver(configuration) );
        	templateResolvers.add( ThymeleafUtil.getLayoutTemplateResolver(configuration) );
        	templateEngine.setTemplateResolvers(templateResolvers);
        }
        else {
        	thymeleafTemplate = actionView.getPageName();
        	
        	//--- Only one TemplateResolver
            logger.trace("Thymeleaf : only 'view' TemplateResolver ");
            templateEngine.setTemplateResolver(ThymeleafUtil.getViewTemplateResolver(configuration));
        }

        logger.trace("Thymeleaf template : '" + thymeleafTemplate + "'");

        
        PrintWriter responseWriter ;
        try {
			responseWriter = response.getWriter();
		} catch (IOException e) {
			throw new TinyMvcException("Cannot get response writer (IOException)", e);
		}

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
        // The template name will be 
        // prefixed with templateResolver prefix ( e.g. "/WEB-INF/" ) 
        // and suffixed with the templateResolver suffix ( e.g. ".html" )
        templateEngine.process(thymeleafTemplate, webContext, responseWriter);
    }
}
