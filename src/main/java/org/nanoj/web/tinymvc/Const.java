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

package org.nanoj.web.tinymvc;

public class Const {

	/**
	 * The "action.method" parameter name in a request <br>
	 * Usable as "action.method=value" in a request URL  <br>
	 * e.g. : "myaction?p1=1&p2=23&action.method=add"  <br>
	 */
	public final static String ACTION_METHOD_PARAMETER_NAME = "action.method" ;

	/**
	 * The attribute name for "action.method" value in the request scope <br>
	 * Usable as ${METHOD} in the JSP <br>
	 * This attribute contains the ACTION_METHOD_PARAMETER_NAME <br>
	 */
	public final static String ACTION_METHOD_ATTRIBUTE_NAME = "METHOD" ;
	
}
