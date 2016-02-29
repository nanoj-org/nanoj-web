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
package org.nanoj.web.tinymvc.env;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Request scope "fieldvalue" object manager <br>
 * SINGLETON  <br>
 * 
 * @author Laurent Guerin
 *
 */
public class FieldValuesManager {
	
	private final static String FIELD_VALUE = "fieldvalue" ;
	
	private final static FieldValuesManager singleInstance = new FieldValuesManager();
	
	public final static FieldValuesManager getInstance() {
		return singleInstance ;
	}
	
	/**
	 * No public constructor
	 */
	private FieldValuesManager() {
	}
	
	/**
	 * Set all the request parameters in the "fieldvalue" object (in the request scope) <br>
	 * 
	 * @param request
	 */
	public void setFieldValuesFromParameters(HttpServletRequest request) {
		FieldValues<String,String> fieldValues = getFieldValues(request);

		Map<String,String[]> parameters = request.getParameterMap() ;
		Set<String> paramNames = parameters.keySet() ;
		for ( String paramName : paramNames ) {
			String[] v = parameters.get(paramName);
			if ( v != null ) {
				String paramValue = v[0] ;
				if ( null == paramValue ) {
					paramValue = "" ;
				}
				fieldValues.setFieldValue(paramName, paramValue);
			}
		}
	}
	
	/**
	 * Set a specific field value in the "fieldvalue" object (in the request scope)
	 * 
	 * @param request
	 * @param fieldName
	 * @param fieldValue
	 */
	public void setFieldValue(HttpServletRequest request, String fieldName, String fieldValue ) {
		FieldValues<String,String> fieldValues = getFieldValues(request);
		fieldValues.setFieldValue(fieldName, fieldValue);
	}
	
	/**
	 * Returns a FieldValues object located in the request scope (creates a new one if necessary)
	 * @param request
	 * @return
	 */
	@SuppressWarnings ( "unchecked" )
	private FieldValues<String,String> getFieldValues(HttpServletRequest request ) {
		FieldValues<String,String> fieldValues = null ;
		//--- Try to found an existing list of field values in the request
		Object o = null ;
		if ( ( o = request.getAttribute(FIELD_VALUE) ) != null ) {
			if ( o instanceof FieldValues ) {				
				fieldValues = (FieldValues<String,String>) o ;
			}
		}
		//--- If not found : create a new one and put it in the request
		if ( null == fieldValues ) {
			fieldValues = new FieldValues<String,String>() ;
			request.setAttribute(FIELD_VALUE, fieldValues);
		}
		return fieldValues ;
	}
	

}
