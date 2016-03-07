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

import org.nanoj.web.tinymvc.config.Configuration;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ThymeleafUtil {

	/**
	 */
	private final static ServletContextTemplateResolver getTemplateResolver() {
		
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        // XHTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCacheTTLMs(3600000L); // TODO : parameter in configuration file
        templateResolver.setCharacterEncoding("utf-8");

        
        return templateResolver ;
	}

	protected final static ServletContextTemplateResolver getViewTemplateResolver(Configuration configuration) {
		
		ServletContextTemplateResolver templateResolver = getTemplateResolver() ;
		
        templateResolver.setPrefix( configuration.getViewsFolder() ) ; // e.g. "/WEB-INF/views/"
        templateResolver.setSuffix( configuration.getViewsSuffix() ) ; // e.g. ".html"
        
        return templateResolver ;
	}

	protected final static ServletContextTemplateResolver getLayoutTemplateResolver(Configuration configuration) {
		
		ServletContextTemplateResolver templateResolver = getTemplateResolver() ;
		
        templateResolver.setPrefix( configuration.getLayoutsFolder() ) ; // e.g. "/WEB-INF/layouts/"
        templateResolver.setSuffix( configuration.getLayoutsSuffix() ) ; // e.g. ".html"
        
        return templateResolver ;
	}
}
