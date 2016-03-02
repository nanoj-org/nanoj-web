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
package org.nanoj.web.tinymvc ;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * NanoJ Web Configuration Loader
 * 
 * @author Laurent Guerin
 */
public class ConfigurationLoader {

	private final static String  PROPERTIES_FILE_NAME  = "/META-INF/nanoj.properties";

		
	/**
	 * Constructor
	 */
	public ConfigurationLoader() {
		super();
	}
	
	/**
	 * Loads the configuration from the file '/META-INF/nanoj.properties'
	 * @return
	 */
	public Configuration loadConfiguration() {
		Properties properties = loadProperties() ;
		return new Configuration(properties);
	}
	
	/**
	 * Loads the properties from the file '/META-INF/nanoj.properties'
	 * @return
	 */
	public Properties loadProperties() {
		final Properties properties = new Properties();
		try {
			try ( InputStream stream = this.getClass().getResourceAsStream(PROPERTIES_FILE_NAME) ) {
				properties.load(stream);
			}
		} catch (IOException e) {
			throw new TinyMvcException("Cannot load properties from file '" + PROPERTIES_FILE_NAME + "'" );
		}
		return properties;
	}

}
