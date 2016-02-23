package org.nanoj.web.tinymvc.servlet ;

import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.env.ActionInfo;


/**
 * TINY MVC Action Renderer
 * 
 * @author Laurent Guerin
 */
public class ActionViewBuilder {
	
	//--- Attributes
	private final String pagesFolder ; 

	private final String pagesSuffix ; 
	
	private final String templatesFolder ; 


    public ActionViewBuilder( ActionServletConfig actionServletConfig ) {
		super();
		this.pagesFolder = actionServletConfig.getPagesFolder();
		this.pagesSuffix = actionServletConfig.getPagesSuffix();
		this.templatesFolder = actionServletConfig.getTemplatesFolder();
	}

	/**
     * Returns the target page to be used in the request dispatcher FORWARD  
     * @param actionResult
     * @param actionInfo
     * @return
     */
    protected String getTargetPage( final String actionResult, final ActionInfo actionInfo ) {
    	String target = null ;
    	String viewTemplate = null ;
    	String viewPage     = null ;
    	if ( actionResult.indexOf('<') >= 0 ) {
    		String [] parts = actionResult.split("<");
    		if ( parts.length == 2 ) {
    			viewTemplate = parts[0].trim() ;
    			viewPage     = parts[1].trim() ;
    		}
    		else {
        		throw new TinyMvcException("Invalid action result '" + actionResult + "'" );
    		}
    	}
    	else {
        	viewTemplate = null ;
        	viewPage     = actionResult.trim() ;
    	}
    	
//		trace(" view template = '" + viewTemplate + "' ");
//		trace(" view page     = '" + viewPage + "' ");

    	actionInfo.setViewPage( getPageFullPath(viewPage) );
    	
    	if ( null != viewTemplate ) {
    		actionInfo.setViewTemplate( getTemplateFullPath(viewTemplate) );
    		target = actionInfo.getViewTemplate() ;
    	}
    	else {
    		actionInfo.setViewTemplate( null );
    		target = actionInfo.getViewPage();
    	}
    	
//		trace(" view target = '" + target + "' ");
    	return target ;
    	
    }

    /**
     * Returns view page full path
     * @param pageName
     * @return
     */
    private String getPageFullPath( final String pageName )  {
    	
    	if ( pageName.endsWith(this.pagesSuffix) ) {
    		return this.pagesFolder + pageName ;
    	}
    	else {
    		return this.pagesFolder + pageName + this.pagesSuffix ;
    	}
    	
//    	if ( pageName.endsWith( actionServletConfig.getPagesSuffix() ) ) {
//    		return actionServletConfig.getPagesFolder() + pageName ;
//    	}
//    	else {
//    		return actionServletConfig.getPagesFolder() + pageName + actionServletConfig.getPagesSuffix()  ;
//    	}
    }

    /**
     * Returns template full path
     * @param templateName
     * @return
     */
    private String getTemplateFullPath( final String templateName )  {
    	
    	if ( templateName.endsWith(this.pagesSuffix) ) {
    		return this.templatesFolder + templateName ;
    	}
    	else {
    		return this.templatesFolder + templateName + this.pagesSuffix ;
    	}
    	
//    	if ( templateName.endsWith( actionServletConfig.getPagesSuffix() ) ) {
//    		return actionServletConfig.getTemplatesFolder() + templateName ;
//    	}
//    	else {
//    		return actionServletConfig.getTemplatesFolder() + templateName + actionServletConfig.getPagesSuffix() ;
//    	}
    }
    
    
}
