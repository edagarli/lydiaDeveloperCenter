package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DeveloperLogout extends HttpServlet
{

	/**
	 * Constructor of the object.
	 */
	public DeveloperLogout()
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		if (Token.isTokenStringValid(
				request.getParameter(Token.TOKEN_STRING_NAME),
				request.getSession()))
		{
			String devidString = request.getParameter("devid");
			System.out.println("-------" + devidString);
			ObjectId devid = new ObjectId(devidString);
			if (devid != null && !devid.equals(""))
			{
				DBCollection userCollection = MongoDBUtil
						.getTestCollection(Constant.DEVELOPER);
				DBObject queryById = new BasicDBObject();
				queryById.put("_id", devid);
				DBObject findById = userCollection.findOne(queryById);
				System.out.println("-----" + findById);
				if (findById != null)
				{
					out.println("{\"status\":" + Constant.RESULT_SUCCESS
							+ ",\"devid\":\"\"}");
				} else
				{
					out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
							+ "}");
				}
			} else
			{
				out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
						+ "}");
			}

		} else
		{
			out.println("{\"status\":" + Constant.RESULT_TOKEN_INVALID + "}");
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException
	{
		// Put your code here
	}

}
