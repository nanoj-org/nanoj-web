package org.nanoj.web.tinymvc.servlet ;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nanoj.web.tinymvc.TinyMvcException;
import org.nanoj.web.tinymvc.env.ActionInfo;


/**
 * TINY MVC Front Controller Servlet
 * 
 * @author Laurent Guerin
 */
public class ActionServlet extends HttpServlet {

	//--- Constants
	private static final long serialVersionUID = 1L;
	
	private static final String METHOD_PARAM_NAME  = "action.method" ;

	//--- Attributes
	private ActionServletConfig  actionServletConfig = null ;
	private ActionProcessor      actionProcessor     = null ;
	private ActionViewBuilder    actionViewBuilder   = null ;
	
	private boolean traceFlag   = false ;
	
	private void trace(String msg)
	{
		if ( traceFlag ) {
			String className = this.getClass().getSimpleName() ;
			System.out.println("[TRACE] " + className +  " : " + msg );
		}
	}
	
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		//--- Configuration creation
		this.actionServletConfig = new ActionServletConfig(config);
		
		//--- Collaborators creation
		this.actionProcessor     = new ActionProcessor(actionServletConfig);
		this.actionViewBuilder   = new ActionViewBuilder(actionServletConfig);

		trace("Servlet " + this.getClass().getSimpleName() + " initialized :" );
		trace(" . templates folder = '" + this.actionServletConfig.getTemplatesFolder() + "'" ) ;
		trace(" . pages folder     = '" + this.actionServletConfig.getPagesFolder() + "'" ) ;
		trace(" . pages suffix     = '" + this.actionServletConfig.getPagesSuffix() + "'" ) ;
		trace(" . action provider class = '" + this.actionServletConfig.getActionProviderClassName() + "'" ) ;
		trace(" . actions package       = '" + this.actionServletConfig.getActionsPackage() + "'" ) ;
		trace(" . default action        = '" + this.actionServletConfig.getDefaultAction() + "'" ) ;
	}


	@Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	
    	trace("doGet " + request.getRequestURI() );
        process(request, response);
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	trace("doPost " + request.getRequestURI() );
        process(request, response);
    }
    
	/**
     * Process request for both HTTP GET and POST methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	
		//--- 1) Get action information from the request URL
		ActionInfo actionInfo = parseActionURI(request);
		
		//--- 2) Execute the action controller
		//String actionResult = executeAction(actionInfo, request, response);
		String actionResult = actionProcessor.executeAction(actionInfo, request, response);
		
		if ( null == actionResult ) {
			throw new TinyMvcException("Action result is null (action " + actionInfo.getClassName() + ")" );
		}

		if ( actionResult.trim().length() == 0 ) {
			throw new TinyMvcException("Action result is void (action " + actionInfo.getClassName() + ")" );
		}

		//--- 3) Dispatch (forward) to VIEW ( with or without template )  
		//String target = getForwardTarget( actionResult, actionInfo ) ;
		String targetPage = actionViewBuilder.getTargetPage(actionResult, actionInfo);
		
		//--- Set action model in request scope
		request.setAttribute("action", actionInfo);

		//--- Forward to target 
		RequestDispatcher requestDispatcher = this.getServletContext().getRequestDispatcher(targetPage) ;		
		requestDispatcher.forward(request, response);
		
    }
    
    /**
     * Parse the URI in order to obtain action information
     * @param request
     * @return
     */
    private ActionInfo parseActionURI( final HttpServletRequest request )  {
		String path = request.getPathInfo();

		trace("process : path = '" + path + "'" );
		
		if ( null == path || "".equals(path) || "/".equals(path) )
		{
			path="/" ; 
		}
		
		String actionString = path.substring(1); //  "/myaction" --> "myaction"

		String actionName = null;
		String methodName = null; // specific method (if any)
		
		//--- Get action name and method name from "PathInfo"
		if ( actionString.length() > 0 ) {
			
			int pointPosition = actionString.indexOf('.') ;
			if ( pointPosition == 0 ) {
				throw new TinyMvcException("Invalid action name (starts with '.')" );
			}
			else if ( pointPosition == actionString.length()-1 ) {
				throw new TinyMvcException("Invalid action name (ends with '.')" );
			}
			else if ( pointPosition > 0 ) {
				actionName = actionString.substring(0, pointPosition) ; // left part
				methodName = actionString.substring(pointPosition+1, actionString.length()); // right part
			}
			else {
				// If < 0 : No point (keep the name as is, no method )
				actionName = actionString ;
				methodName = null ; // no specific method
			}
		}
		else {
			actionName = "" ; // void action name
			methodName = null ; // no specific method
		}
		
		if ( null == methodName ) {
			//--- Try to find the method name in the request parameters ( e.g. for named submit buttons )
			String s = request.getParameter( METHOD_PARAM_NAME );
			if ( s != null ) {
				methodName = s.trim() ;
			}
		}

		trace("process : action name = '" + actionName + "', method name = '" + methodName + "'");
		
//		String actionRoot = request.getContextPath() + request.getServletPath() ;
//		return new ActionInfo(actionRoot, actionName, methodName );
		return new ActionInfo(request, actionName, methodName );
			
    }
    
//    /**
//     * Returns template full path
//     * @param templateName
//     * @return
//     */
//    private String getTemplateFullPath( final String templateName )  {
//    	
////    	if ( templateName.endsWith(this.pagesSuffix) ) {
////    		return this.templatesFolder + templateName ;
////    	}
////    	else {
////    		return this.templatesFolder + templateName + this.pagesSuffix ;
////    	}
//    	
//    	if ( templateName.endsWith( actionServletConfig.getPagesSuffix() ) ) {
//    		return actionServletConfig.getTemplatesFolder() + templateName ;
//    	}
//    	else {
//    		return actionServletConfig.getTemplatesFolder() + templateName + actionServletConfig.getPagesSuffix() ;
//    	}
//    }
    
//    /**
//     * Returns view page full path
//     * @param pageName
//     * @return
//     */
//    private String getPageFullPath( final String pageName )  {
//    	
////    	if ( pageName.endsWith(this.pagesSuffix) ) {
////    		return this.pagesFolder + pageName ;
////    	}
////    	else {
////    		return this.pagesFolder + pageName + this.pagesSuffix ;
////    	}
//    	if ( pageName.endsWith( actionServletConfig.getPagesSuffix() ) ) {
//    		return actionServletConfig.getPagesFolder() + pageName ;
//    	}
//    	else {
//    		return actionServletConfig.getPagesFolder() + pageName + actionServletConfig.getPagesSuffix()  ;
//    	}
//    }
    

//    /**
//     * Get the target page to be used in the request dispatcher FORWARD  
//     * @param actionResult
//     * @param actionInfo
//     * @return
//     */
//    private String getForwardTarget( final String actionResult, final ActionInfo actionInfo ) {
//    	String target = null ;
//    	String viewTemplate = null ;
//    	String viewPage     = null ;
//    	if ( actionResult.indexOf('<') >= 0 ) {
//    		String [] parts = actionResult.split("<");
//    		if ( parts.length == 2 ) {
//    			viewTemplate = parts[0].trim() ;
//    			viewPage     = parts[1].trim() ;
//    		}
//    		else {
//        		throw new TinyMvcException("Invalid action result '" + actionResult + "'" );
//    		}
//    	}
//    	else {
//        	viewTemplate = null ;
//        	viewPage     = actionResult.trim() ;
//    	}
//    	
//		trace(" view template = '" + viewTemplate + "' ");
//		trace(" view page     = '" + viewPage + "' ");
//
//    	actionInfo.setViewPage    ( getPageFullPath(viewPage) );
//    	
//    	if ( null != viewTemplate ) {
//    		actionInfo.setViewTemplate( getTemplateFullPath(viewTemplate) );
//    		target = actionInfo.getViewTemplate() ;
//    	}
//    	else {
//    		actionInfo.setViewTemplate( null );
//    		target = actionInfo.getViewPage();
//    	}
//    	
//		trace(" view target = '" + target + "' ");
//    	return target ;
//    	
//    }
//    
}
