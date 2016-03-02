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
package org.nanoj.web.tinymvc.provider ;

import org.nanoj.util.StrUtil;
import org.nanoj.web.tinymvc.Action;
import org.nanoj.web.tinymvc.TinyMvcException;


/**
 * Standard provider for actions <br>
 * Search actions classes in the standard way using the 'actions package'
 * 
 * @author Laurent Guerin
 *
 */
public class StandardActionProvider implements ActionProvider {

	private final String actionsPackageWithEndingPoint ;

	//private final String defaultActionName ;
		
	/**
	 * Constructor <br>
	 * 
	 * @param actionsPackage  the actions package name 
	 */
//	public StandardActionProvider(String actionsPackage, String defaultAction) {
	public StandardActionProvider(String actionsPackage) {
		super();
		
		//--- Build the actions package name 
		if ( null == actionsPackage ) {
			throw new TinyMvcException("Cannot create DefaultActionProvider : actions package is null");
		}
		if ( actionsPackage.endsWith(".") ) {
			this.actionsPackageWithEndingPoint = actionsPackage ;
		}
		else {
			this.actionsPackageWithEndingPoint = actionsPackage + "." ;
		}

		//--- Set the default action 
		//this.defaultActionName  = defaultAction ; // Can be null 
	} 


	@Override
	public final Action getAction(String actionName ) {
		
		if ( actionName == null ) {
			throw new TinyMvcException("Action name is null" );
		}
		
		if ( actionName.trim().length() == 0 ) {
			throw new TinyMvcException("Action name is null" );
//			if ( this.defaultActionName != null ) {
//				actionName = this.defaultActionName ;
//			}
//			else {
//				throw new TinyMvcException("No action name and no default action" );
//			}
		}
		
		//--- Full class name ( e.g. "org.demo.actions.DoSomethingAction" ) 
		String actionClassName = actionsPackageWithEndingPoint + StrUtil.firstCharUpperCase(actionName) + "Action" ; 
		
		return InstanceProvider.createInstance(actionClassName, Action.class) ;
	}
	
}
