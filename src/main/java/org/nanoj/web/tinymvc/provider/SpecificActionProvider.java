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

import java.util.Map;

import org.nanoj.web.tinymvc.Action;
import org.nanoj.web.tinymvc.TinyMvcException;

/**
 * Standard provider for actions <br>
 * Can be used directly by the application as the ActionProvider implementation
 * 
 * @author Laurent Guerin
 *
 */
public class SpecificActionProvider implements ActionProvider {

	private final Map<String, Class<? extends Action > > map ;
	
	
	public SpecificActionProvider( Map<String, Class<? extends Action>> map) {
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
		Class<? extends Action > actionClass = map.get( actionName2 ) ;
		if ( null == actionClass ) {
			throw new TinyMvcException("No action class for '" + actionName2 + "'");
		}
			
//		Action actionInstance;
//		try {
//			actionInstance = actionClass.newInstance();
//		} catch (InstantiationException e) {
//			throw new TinyMvcException("Cannot instantiate " + actionClass.getCanonicalName(), e );
//		} catch (IllegalAccessException e) {
//			throw new TinyMvcException("Cannot instantiate " + actionClass.getCanonicalName(), e );
//		}
//		
//		return actionInstance ;

//		return InstanceProvider.createInstance(actionClass, Action.class) ;
		return InstanceProvider.createInstance(actionClass) ;
	}
	
}
