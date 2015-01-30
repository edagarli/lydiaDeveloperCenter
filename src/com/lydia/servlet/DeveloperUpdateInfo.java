package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import md5.Md5Util;

import org.bson.types.ObjectId;
import com.lydia.util.Constant;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DeveloperUpdateInfo extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public DeveloperUpdateInfo()
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
		PrintWriter out = response.getWriter();
		if(Token.isTokenStringValid(request.getParameter(Token.TOKEN_STRING_NAME), request.getSession()))
		{
			ObjectId devid = new ObjectId(request.getParameter("devid"));
			if (devid != null && !devid.equals(""))
			{

				DBCollection userCollection = com.lydia.util.MongoDBUtil
						.getTestCollection(Constant.DEVELOPER);
				DBObject queryById = new BasicDBObject();
				queryById.put("_id", devid);
				DBObject findById = userCollection.findOne(queryById);
				if (findById != null)
				{
					String name = request.getParameter("name");
					if(name==null||name.equals(""))
					{
						name=findById.get("name").toString();
					}
					String website = request.getParameter("website").toString();
					if(website==null||website.equals(""))
					{
						website=findById.get("website").toString();
					}
					
					
					String password=null;
					
				
						System.out.println("1====");
						password=request.getParameter("password");
						System.out.println("2==="+password);
						if(password==null||password.equals(""))
						{
							password = findById.get("password").toString();
							System.out.println("3===="+password);
						}
						else
						{
							password=Md5Util.md5(password);
							System.out.println("4===="+password);
						}
					
					String email = findById.get("email").toString();
					DBObject condition = new BasicDBObject();
					condition.put("_id", devid);
					DBObject update = new BasicDBObject();
					update.put("name", name);
					update.put("password", password);
					update.put("email", email);
					update.put("website", website);

					DBObject setValue = new BasicDBObject();
					setValue.put("$set", update);

					userCollection.update(condition, setValue);

					out.println("{\"status\":" + Constant.RESULT_SUCCESS + "}");
				}
			}
			else
			{
				out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID + "}");
			}
		}
		else
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
