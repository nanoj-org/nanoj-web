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
package org.nanoj.web.tinymvc.env ;

import java.util.HashMap;

/**
 * Map holding the value for each defined field ( field-name - fied-value ) <br>
 * 
 * Stored in the request scope <br>
 * Usable in the JSP with Expression Language, eg : ${fieldValue.theFieldName}
 * 
 * @author Laurent Guerin
 *
 */
public class FieldValues<K,E> extends HashMap<K,E> {

	private static final long serialVersionUID = 1L;

	/**
	 * Set a field value 
	 * @param fieldName 
	 * @param fieldValue
	 */
	public void setFieldValue( K fieldName, E fieldValue ) {
		super.put(fieldName, fieldValue);
	}
}
