package org.nanoj.web.tinymvc.provider ;

import org.nanoj.web.tinymvc.Action;

/**
 * Action Provider interface <br>
 * 
 * @author Laurent Guerin
 *
 */
public interface ActionProvider {

	/**
	 * Return the action instance for the given action name <br>
	 * 
	 * @param actionName
	 * @return
	 */
	Action getAction( String actionName) ;
}
