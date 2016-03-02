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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.util.StrUtil;
import org.nanoj.web.tinymvc.Configuration;
import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.env.ActionInfo;


/**
 * TINY MVC Action Renderer
 * 
 * @author Laurent Guerin
 */
public class ActionViewRenderer {
	
	//--- Attributes
//	private final ServletContext     servletContext ;
	private final ActionViewBuilder  actionViewBuilder ;

//    public ActionViewRenderer( ServletContext servletContext, Configuration actionServletConfig ) {
	public ActionViewRenderer( Configuration actionServletConfig ) {
		super();
		this.actionViewBuilder = new ActionViewBuilder(actionServletConfig);
//		this.servletContext = servletContext ;
	}

    public void render( final String actionResult, final ActionInfo actionInfo, 
    		final HttpServletRequest request, final HttpServletResponse response) {
		String targetPage = actionViewBuilder.getTargetPage(actionResult, actionInfo);
		
		if ( StrUtil.nullOrVoid(targetPage) ) {
			throw new TinyMvcException("No target page for action '" + actionInfo.getName() + "'");
		}
		
		//--- Set action model in request scope
		request.setAttribute("action", actionInfo);

		renderJSP( targetPage, request, response); 
    }

    private void renderJSP( final String targetPage,
    		final HttpServletRequest request, final HttpServletResponse response) {
    	
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

}
