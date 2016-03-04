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

import java.util.logging.Level;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.nanoj.util.ConsoleLoggerProvider;
import org.nanoj.web.tinymvc.config.Configuration;
import org.nanoj.web.tinymvc.config.ConfigurationLoader;
import org.nanoj.web.tinymvc.util.ConsoleLogger;

@WebListener
public class WebAppContextListener implements ServletContextListener {
	
	private ConsoleLogger logger = ConsoleLogger.getLogger(WebAppContextListener.class);
	
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	logger.info("NanoJ initialization");
        
        ConsoleLoggerProvider.setGlobalLevelThreshold(Level.OFF);
        
        ServletContext servletContext = servletContextEvent.getServletContext();
        Configuration configuration = loadConfiguration(servletContext);
        
        // TODO 
        //ConsoleLogger.setGlobalLevel(configuration.getLoggerLevel());
        ConsoleLogger.setGlobalLevel(ConsoleLogger.ALL);
        
        registerActionFilter(servletContext, configuration);
    }
    
    private Configuration loadConfiguration(ServletContext servletContext) {
		logger.trace("loadConfiguration()");
		ConfigurationLoader configurationLoader = new ConfigurationLoader() ;
		Configuration configuration = configurationLoader.loadConfiguration();
		return configuration ;
    }
    
    private void registerActionFilter(ServletContext servletContext, Configuration configuration) {
		logger.trace("registerActionFilter()");
        //final String ActionFilterURLPatterns = "/*";

    	// Filter instance creation
        ActionFilter actionFilter = new ActionFilter(configuration);
        logger.trace("ActionFilter created.");
        
        // Filter registration in the ServletContext
        FilterRegistration.Dynamic dynamicFilterRegistration = servletContext.addFilter("zzzNanoJ", actionFilter);
        logger.trace("ActionFilter registered.");
        
        // Filter mapping based on URL patterns
        /*
         * addMappingForUrlPatterns()
         * 
         * Adds a filter mapping with the given url patterns and dispatcher types for the Filter represented by this FilterRegistration.
         * Filter mappings are matched in the order in which they were added.
         * Depending on the value of the isMatchAfter parameter, the given filter mapping will be considered after or before any 
         * declared filter mappings of the ServletContext from which this FilterRegistration was obtained.
         * If this method is called multiple times, each successive call adds to the effects of the former. 
         * Parameters:
         *  - dispatcherTypes - the dispatcher types of the filter mapping, or null if the default DispatcherType.REQUEST is to be used
         *  - isMatchAfter    - true if the given filter mapping should be matched after any declared filter mappings, 
         *                      and false if it is supposed to be matched before any declared filter mappings 
         *                      of the ServletContext from which this FilterRegistration was obtained
         *  - urlPatterns     - the url patterns of the filter mapping 
         */
        //dynamicFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, ActionFilterURLPatterns );
        dynamicFilterRegistration.addMappingForUrlPatterns(null, true, configuration.getActionsPattern() );
        logger.trace("ActionFilter mapping added (pattern = " + configuration.getActionsPattern() + ")");
        
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	logger.trace("contextDestroyed() : Web application stopped");
    }
}
