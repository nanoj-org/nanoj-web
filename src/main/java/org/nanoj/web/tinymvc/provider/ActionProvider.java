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
	 * @param actionName the conventional action name (starting with a lower case)
	 * @return
	 */
	Action getAction( String actionName) ;
}
