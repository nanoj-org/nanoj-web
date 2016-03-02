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
package org.nanoj.web.tinymvc.util;

public class ConsoleLogger {

	public final static int ALL   = 900 ;
	
	public final static int DEBUG = 700 ;
	public final static int TRACE = 600 ;
	
	public final static int INFO  = 400 ; // Default level
	public final static int WARN  = 300 ;
	public final static int ERROR = 200 ;
	
	public final static int OFF   = 0 ;
					
	private static int globalLevel = INFO ;
	
	public final static void setGlobalLevel(int level) {
		globalLevel = level ;
	}
	
	public final static ConsoleLogger getLogger(final Class<?> clazz) {
		return new ConsoleLogger(clazz);
	}

	private final String className ;
	
	/**
	 * Constructor
	 * @param clazz
	 */
	public ConsoleLogger(Class<?> clazz) {
		this.className = clazz.getSimpleName();
	}

	public void info(String msg) {
		if ( globalLevel >= INFO ) {
			print("[INFO] " + className +  " : " + msg );
		}
	}

	public void error(String msg) {
		if ( globalLevel >= ERROR ) {
			print("[ERROR] " + className +  " : " + msg );
		}
	}

	public void trace(String msg) {
		if ( globalLevel >= TRACE ) {
			print("[TRACE] " + className +  " : " + msg );
		}
	}

	public void debug(String msg) {
		print("[DEBUG] " + className +  " : " + msg );
	}

	private void print(String msg) {
		System.out.println( msg );
	}
}
