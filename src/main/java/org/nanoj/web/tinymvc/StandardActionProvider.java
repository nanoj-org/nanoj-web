package org.nanoj.web.tinymvc ;

import java.util.Map;

/**
 * Standard provider for actions <br>
 * Can be used directly by the application as the ActionProvider implementation
 * 
 * @author Laurent Guerin
 *
 */
public class StandardActionProvider implements ActionProvider {

	private final Map<String, Class<? extends Action > > map ;
	
	
	public StandardActionProvider( Map<String, Class<? extends Action>> map) {
		super();
		if ( map != null ) {
			this.map = map;
		}
		else {
			throw new TinyMvcException("Map argument is null" );
		}
	}


	public final Action getAction(String actionName ) {
		
		if ( null == actionName ) {
			throw new TinyMvcException("Action name is null" );
		}
		
		String actionName2 = actionName.trim().toLowerCase() ;
		Class<? extends Action > c = map.get( actionName2 ) ;
		if ( null == c ) {
			throw new TinyMvcException("No action for '" + actionName2 + "'");
		}
			
		Action actionInstance;
		try {
			actionInstance = c.newInstance();
		} catch (InstantiationException e) {
			throw new TinyMvcException("Cannot instantiate " + c.getCanonicalName(), e );
		} catch (IllegalAccessException e) {
			throw new TinyMvcException("Cannot instantiate " + c.getCanonicalName(), e );
		}
		
		return actionInstance ;
	}
	
}
