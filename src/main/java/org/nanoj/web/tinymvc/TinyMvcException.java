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
package org.nanoj.web.tinymvc ;

/**
 * Runtime exception for Tiny MVC errors
 * 
 * @author Laurent Guerin
 *
 */
public class TinyMvcException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    //-----------------------------------------------------------------------------
    /**
     * Constructor
     * 
     * @param message
     */
    public TinyMvcException(String message)
    {
        super(message);
    }

    //-----------------------------------------------------------------------------
    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public TinyMvcException(String message, Throwable cause)
    {
        super(message, cause);
    }

    //-----------------------------------------------------------------------------
    /**
     * Constructor
     * 
     * @param cause
     */
    public TinyMvcException(Throwable cause)
    {
        super(cause);
    }


}
