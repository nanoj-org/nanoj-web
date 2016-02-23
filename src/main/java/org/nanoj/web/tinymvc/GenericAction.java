package org.nanoj.web.tinymvc ;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.util.beanmapper.BeanMapper;
import org.nanoj.web.tinymvc.env.FieldValues;
import org.nanoj.web.tinymvc.env.InputParameter;

/**
 * Generic Action (abstract) implementation with few useful methods
 *  
 * @author Laurent Guerin
 *
 */
public abstract class GenericAction implements Action {

	private final static String VALUE = "fieldvalue" ;
	
	@Override
	public void beforeAction(String method,  HttpServletRequest request, HttpServletResponse response) {
		// Default behavior : nothing to do 
	}
	
	@Override
	public void afterAction(String method,  HttpServletRequest request, HttpServletResponse response) {
		// Default behavior : nothing to do 
	}
	
	//=========================================================================================
	/**
	 * Returns a Map built from the Http request parameters
	 * @param request
	 * @return
	 */
	protected Map<String,String> getParametersMap(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		
		Enumeration<String> paramNames = request.getParameterNames() ;
		
		while ( paramNames.hasMoreElements() ) {
			String name  = paramNames.nextElement() ;
			String value = request.getParameter(name) ;
			map.put(name, value);
		}
		return map ;
	}
	
	/**
	 * Populate the given bean with the Http request parameters <br>
	 * Each request parameter having the same name as a bean property is set in the bean 
	 * @param bean 
	 * @param request
	 */
	protected void populateBeanFromParameters(Object bean, HttpServletRequest request) {
		Map<String,String> parameters = getParametersMap(request) ;
		BeanMapper mapper = new BeanMapper();
		mapper.mapToBean(parameters, bean);

	}
	
	/**
	 * Returns an InputParameter of the expected class for the given parameter name
	 * @param request the http request
	 * @param paramName the parameter's name
	 * @param clazz the type of the InputParameter
	 * @return
	 */
	protected <T> InputParameter<T> getInputParameter(HttpServletRequest request, String paramName, Class<T> clazz) {
		
		String paramValue = request.getParameter(paramName) ;
		if ( paramValue != null ) {
			//T value = convertValue( clazz, paramValue ) ;
			BeanMapper mapper = new BeanMapper();
			T value = mapper.getTypedValue(clazz, paramValue);
			return new InputParameter<T>(paramName, value);
		}
		else {
			throw new RuntimeException("No '" + paramName + "' parameter in the request") ;
		}
	}


	//=========================================================================================
	
	/**
	 * Returns a FieldValues objet located in the request scope (creates a new one if necessary)
	 * @param request
	 * @return
	 */
	@SuppressWarnings ( "unchecked" )
	private FieldValues<String,String> getFieldValues(HttpServletRequest request ) {
		FieldValues<String,String> fieldValues = null ;
		//--- Try to found an existing list of field values in the request
		Object o = null ;
		if ( ( o = request.getAttribute(VALUE) ) != null ) {
			if ( o instanceof FieldValues ) {				
				fieldValues = (FieldValues<String,String>) o ;
			}
		}
		//--- If not found : create a new one and put it in the request
		if ( null == fieldValues ) {
			fieldValues = new FieldValues<String,String>() ;
			request.setAttribute(VALUE, fieldValues);
		}
		return fieldValues ;
	}
	
	/**
	 * Set a specific field value in the "fieldvalue" object (in the request scope)
	 * 
	 * @param request
	 * @param fieldName
	 * @param fieldValue
	 */
	protected void setFieldValue(HttpServletRequest request, String fieldName, String fieldValue ) {
		FieldValues<String,String> fieldValues = getFieldValues(request);
		fieldValues.setFieldValue(fieldName, fieldValue);
	}

	/**
	 * Set a parameter value in the corresponding field value in the "fieldvalue" object (in the request scope)<br>
	 * Try to get the value of a request parameter with the same name as the given field name and set it.<br>
	 * If the parameter is not found a "blank" value is set.
	 * 
	 * @param request
	 * @param fieldName
	 */
	protected void setFieldValueFromParam(HttpServletRequest request, String fieldName) {
		FieldValues<String,String> fieldValues = getFieldValues(request);
		String paramValue = request.getParameter(fieldName);
		if ( null == paramValue ) {
			paramValue = "" ;
		}
		fieldValues.setFieldValue(fieldName, paramValue);
	}
	
	/**
	 * Set all the request parameters in the "fieldvalue" object (in the request scope) <br>
	 * 
	 * @param request
	 */
	protected void setFieldValuesFromParameters(HttpServletRequest request) {
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
	
	//-------------------------------------------------------------------------------------------------------------
	// Param as String
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Returns the parameter's value as a String <br>
	 * Throws an exception if not found
	 * @param request
	 * @param paramName
	 * @return
	 */
	protected String getParamAsString(HttpServletRequest request, String paramName) {
		return getParamAsString(request, paramName, null);
	}
	/**
	 * Returns the parameter's value as a String <br>
	 * With a default value (used if not found)
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	protected String getParamAsString(HttpServletRequest request, String paramName, String defaultValue ) {
		String paramValue = request.getParameter(paramName);
		if ( null == paramValue ) {
			if ( defaultValue != null ) {
				return defaultValue ;
			}
			else {
				throw new TinyMvcException("Param '" + paramName + "' required");
			}
		}
		else {
			return paramValue ;
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	// Param as Boolean
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Returns the parameter's value as a boolean <br>
	 * Throws an exception if not found
	 * @param request
	 * @param paramName
	 * @return
	 */
	protected boolean getParamAsBoolean(HttpServletRequest request, String paramName) {
		return getParamAsBoolean(request, paramName, null);
	}
	/**
	 * Returns the parameter's value as a boolean <br>
	 * With a default value (used if not found)
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	protected boolean getParamAsBoolean(HttpServletRequest request, String paramName, Boolean defaultValue ) {
		String paramValue = request.getParameter(paramName);
		if ( null == paramValue ) {
			if ( defaultValue != null ) {
				return defaultValue ;
			}
			else {
				throw new TinyMvcException("Param '" + paramName + "' required");
			}
		}
		else {
			return paramValue.trim().equalsIgnoreCase("true") ;
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	// Param as Double
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Returns the parameter's value as a double <br>
	 * Throws an exception if not found
	 * @param request
	 * @param paramName
	 * @return
	 */
	protected double getParamAsDouble(HttpServletRequest request, String paramName) {
		return getParamAsDouble(request, paramName, null);
	}
	
	/**
	 * Returns the parameter's value as a double <br>
	 * With a default value (used if not found)
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	protected double getParamAsDouble(HttpServletRequest request, String paramName, Double defaultValue ) {
		return ((Double) getParamAsNumber(request, paramName, double.class, defaultValue ) ).doubleValue();
	}

	//-------------------------------------------------------------------------------------------------------------
	// Param as Float
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Returns the parameter's value as a float <br>
	 * Throws an exception if not found
	 * @param request
	 * @param paramName
	 * @return
	 */
	protected float getParamAsFloat(HttpServletRequest request, String paramName) {
		return getParamAsFloat(request, paramName, null);
	}
	/**
	 * Returns the parameter's value as a float <br>
	 * With a default value (used if not found)
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	protected float getParamAsFloat(HttpServletRequest request, String paramName, Float defaultValue) {
		return ((Float) getParamAsNumber(request, paramName, float.class, defaultValue ) ).floatValue();
	}
	
	//-------------------------------------------------------------------------------------------------------------
	// Param as Integer
	//-------------------------------------------------------------------------------------------------------------
	/**
	 * Returns the parameter's value as an integer <br>
	 * Throws an exception if not found
	 * @param request
	 * @param paramName
	 * @return
	 */
	protected int getParamAsInt(HttpServletRequest request, String paramName) {
		return getParamAsInt(request, paramName, null);
	}
	/**
	 * Returns the parameter's value as an integer <br>
	 * With a default value (used if not found)
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	protected int getParamAsInt(HttpServletRequest request, String paramName, Integer defaultValue) {
		return ((Integer) getParamAsNumber(request, paramName, int.class, defaultValue ) ).intValue();
	}
	
	//-------------------------------------------------------------------------------------------------------------
	private Number getParamAsNumber(HttpServletRequest request, String paramName, Class<? extends Number> type, Number defaultValue ) {
		
		String paramValue = request.getParameter(paramName);
		if ( null == paramValue ) {
			if ( defaultValue != null ) {
				return defaultValue ;
			}
			else {
				throw new TinyMvcException("Param '" + paramName + "' required");
			}
		}
		else {
			try {
				if ( type.equals( double.class ) ) {
					return new Double( Double.parseDouble(paramValue) );
				}
				else if ( type.equals( float.class ) ) {
					return new Float( Float.parseFloat(paramValue) );
				}
				else if ( type.equals( long.class ) ) {
					return new Long( Long.parseLong(paramValue) );
				}
				else if ( type.equals( int.class ) ) {
					return new Integer( Integer.parseInt(paramValue) );
				}
				else {
					throw new TinyMvcException("Cannot convert param '" + paramName + "' (invalid type " + type.getSimpleName() + ")");
				}
			} catch (NumberFormatException e) {
				throw new TinyMvcException("Param '" + paramName + "' : invalid numeric value '" + paramValue + "' (" + type.getSimpleName() + ")");
			}
		}
	}

}
