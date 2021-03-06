package eExhibition.controller.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eExhibition.data.classes.User;
import eExhibition.model.classes.adminManager;
import eExhibition.model.classes.loginManager;

/**
 * Servlet implementation class adminResponseManager
 */
@WebServlet("/organiserResponseManager")
public class organiserResponseManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public organiserResponseManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		session.invalidate();		
		RequestDispatcher rd = 
		request.getRequestDispatcher("/index.jsp");
		rd.forward(request, response);	    		
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("userName")==""||session.getAttribute("userName")==null)
		{
			session.invalidate();
			
			RequestDispatcher rd = 
			request.getRequestDispatcher("/index.jsp");
			rd.forward(request, response);	    		
			return;
		}
		
		RequestDispatcher rd = null ;
		String action = request.getParameter("action");
		adminManager am=adminManager.getInstance();
		loginManager lm=loginManager.getInstance();
		if(action.equals("My Details"))
		{
			String uname=(String) session.getAttribute("userName");
			User organiser=am.getOrganiser(uname);
			request.setAttribute("password",am.getPasswordOfUserName(uname)); 
			 request.setAttribute("uname", uname); 
			 request.setAttribute("organiser", organiser);
			 request.setAttribute("CALLER", "Display Organiser");
			 request.setAttribute("Role", "Organiser");
			 rd = request.getRequestDispatcher("/displayOrganiser.jsp");
		}
		else if(action.equals("Update Organizer"))
		 {
			String uname=request.getParameter("uname").trim();
			String password=request.getParameter("password").trim();
			String oldUserId=request.getParameter("oldUserId").trim();//old password from hidden field 
			
			String name=request.getParameter("name").trim();
			String email=request.getParameter("email").trim();
			String phone=request.getParameter("phone").trim();
			String address=request.getParameter("address").trim();
			int flag=1;
			if(uname==""||password==""||name==""||email==""||phone==""||address=="")
			{
				request.setAttribute("errorMessage", "Organizer can not be updated because you are missing required fields...");
				rd = request.getRequestDispatcher("/errorPage.jsp");
				flag=0;//Go away
			}
			else if(lm.userNameExist(uname) ){//Will go if uname already exist
				if((uname.equals(oldUserId)))
				{
					//This means user going to update his info but he does not changed uname
				}
				else{//if he change user name then find whether uname already exist
				request.setAttribute("errorMessage", "Changes can not be made because Username already exists...");
				rd = request.getRequestDispatcher("/errorPage.jsp");
				flag=0;//Go away
				}
			}
			
			if(flag==1){
				User organiser = null;
			
				organiser=new User(uname,name,email,phone,address);
			
					User updatedOrganiser=am.updateOrganiser(organiser, oldUserId, password);
				 if(updatedOrganiser==null){
				    	
				    	request.setAttribute("errorMessage", "Something went wrong...");
						rd = request.getRequestDispatcher("/errorPage.jsp");
				    }
				    else{
				  request.setAttribute("password", password); 
				  request.setAttribute("uname", uname); 
				  request.setAttribute("organiser", updatedOrganiser);
				  request.setAttribute("CALLER", "Update Organiser");
				  request.setAttribute("Role", "Organiser");
				  rd = request.getRequestDispatcher("/displayOrganiser.jsp");
				    }
			
			}
			
			
		}
		
		
		
		
	
		
		
		rd.forward(request, response);
		
	}
	

}
