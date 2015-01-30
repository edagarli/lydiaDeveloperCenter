package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import md5.Md5Util;

import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DeveloperController extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	public DeveloperController()
	{
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy()
	{
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		
		if(email==null||email.equals("")||password==null||password.equals(""))
		{
			out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
		}
		else
		{
			DBCollection userCollection = MongoDBUtil.getTestCollection(Constant.DEVELOPER);
			DBObject queryByName = new BasicDBObject();
			queryByName.put("email",request.getParameter("email"));
			queryByName.put("password",Md5Util.md5(request.getParameter("password")));
			queryByName.put("codeStatus", 1);
			DBObject findByUsername=userCollection.findOne(queryByName);
			if(findByUsername!=null)
			{
	           out.print("{\"status\":"+Constant.LOGIN_SUCCESS+",\"devid\":\""+findByUsername.get("_id")+"\",\"token\":\""+Token.getTokenString(request.getSession())+"\"}");
			}
			else
			{
			   out.println("{\"status\":"+Constant.LOGIN_FAILURE+",\"devid\":\"\"}");
			}
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException
	{
		// Put your code here
	}
}
