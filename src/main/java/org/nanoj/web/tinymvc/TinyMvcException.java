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
