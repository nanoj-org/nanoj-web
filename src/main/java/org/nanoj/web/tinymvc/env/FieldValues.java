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
