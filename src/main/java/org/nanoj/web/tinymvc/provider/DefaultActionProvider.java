package org.nanoj.web.tinymvc.provider ;

import org.nanoj.web.tinymvc.Action;
import org.nanoj.web.tinymvc.TinyMvcException;


/**
 * Default provider for actions <br>
 * Based on naming conventions
 * 
 * @author Laurent Guerin
 *
 */
public class DefaultActionProvider implements ActionProvider {

	private final String actionsPackage ;

	private final String defaultAction ;
		
	/**
	 * Constructor <br>
	 * 
	 * @param actionsPackage
	 * @param defaultAction
	 */
	public DefaultActionProvider(String actionsPackage, String defaultAction) {
		super();
		if ( null == actionsPackage ) {
			throw new TinyMvcException("Cannot create DefaultActionProvider : actions package is null");
		}
		if ( actionsPackage.endsWith(".") ) {
			this.actionsPackage = actionsPackage ;
		}
		else {
			this.actionsPackage = actionsPackage + "." ;
		}
		this.defaultAction  = defaultAction ; // Can be null 
	} 


	/* (non-Javadoc)
	 * @see org.telosys.web.tinymvc.ActionProvider#getAction(java.lang.String)
	 */
	public final Action getAction(String actionName ) {
		
		if ( null == actionName ) {
			throw new TinyMvcException("Action name is null" );
		}
		
		String actionName2 = actionName.trim().toLowerCase() ;
		if ( actionName2.length() == 0 ) {
			if ( this.defaultAction != null ) {
				actionName2 = this.defaultAction ;
			}
			else {
				throw new TinyMvcException("No action name and no default action" );
			}
		}
		
		//--- First char to UpperCase
		byte[] bytes = actionName2.getBytes();
		byte firstChar = bytes[0] ;
		if ( firstChar >= 'a' && firstChar <= 'z' ) {
			byte delta = 'a' - 'A' ;
			bytes[0] = (byte) (firstChar - delta) ;
		}

		//--- Full class name 
		String actionClassName = actionsPackage + new String(bytes) + "Action" ; // "my.package.DosomethingAction" 
		
		return InstanceProvider.getInstance(actionClassName, Action.class) ;
	}
	
}
