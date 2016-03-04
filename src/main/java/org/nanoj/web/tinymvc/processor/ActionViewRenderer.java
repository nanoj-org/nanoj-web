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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.util.StrUtil;
import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.config.Configuration;
import org.nanoj.web.tinymvc.env.ActionInfo;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


/**
 * TINY MVC Action Renderer
 * 
 * @author Laurent Guerin
 */
public class ActionViewRenderer {
	
	//--- Attributes
//	private final ActionViewBuilder  actionViewBuilder ;
	private final Configuration configuration ;

	public ActionViewRenderer( Configuration configuration ) {
		super();
		//this.actionViewBuilder = new ActionViewBuilder(configuration);
		this.configuration = configuration ;
	}

    public void render( final String actionResultString, final ActionInfo actionInfo, 
    		final HttpServletRequest request, final HttpServletResponse response) {
		
		ActionResult actionResult = new ActionResult(actionResultString, configuration);

//		String targetPage = actionViewBuilder.getTargetPage(actionResultObject, actionInfo);
		
    	actionInfo.setViewPage( actionResult.getViewFullPath() );
    	actionInfo.setViewLayout( actionResult.getLayoutFullPath() );

		//--- Set action model in request scope
		request.setAttribute("action", actionInfo);

		if ( true ) {
			renderJSP( actionInfo, actionResult, request, response); 
		}
		else {
			renderWithThymeleaf( actionInfo, actionResult, request, response);
		}
    }

    private void renderJSP( ActionInfo actionInfo, ActionResult actionResult,
    		HttpServletRequest request, HttpServletResponse response) {
    	
		String targetPage = actionResult.getTargetFullPath();
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

    private void renderWithThymeleaf( ActionInfo actionInfo, ActionResult actionResult,
    		HttpServletRequest request, HttpServletResponse response) {
    	
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        // XHTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCharacterEncoding("utf-8");

        //--- 
//        templateResolver.setPrefix("/WEB-INF/");
//        templateResolver.setSuffix(".html");
        String targetName = "?";
        if ( actionResult.hasLayout() ) {
        	targetName = actionResult.getLayout();
            templateResolver.setPrefix(configuration.getLayoutsFolder() ); // e.g. "/WEB-INF/layouts/"
            templateResolver.setSuffix(configuration.getLayoutsSuffix() ); // e.g. ".jsp" or ".html"
        }
        else {
        	targetName = actionResult.getView();
            templateResolver.setPrefix(configuration.getViewsFolder() ); // e.g. "/WEB-INF/views/"
            templateResolver.setPrefix(configuration.getViewsSuffix() ); // e.g. ".jsp" or ".html"
        }

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        
        
        PrintWriter responseWriter ;
        try {
			responseWriter = response.getWriter();
		} catch (IOException e) {
			throw new RuntimeException("Cannot get response writer (IOException)", e);
		}

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
        // The target name will be 
        // prefixed with templateResolver prefix ( e.g. "/WEB-INF/" ) 
        // and suffixed with the templateResolver suffix ( e.g. ".html" )
        templateEngine.process(targetName, webContext, responseWriter);
    }

}
