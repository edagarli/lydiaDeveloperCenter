package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import md5.Md5Util;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;
import com.lydia.mail.EmailSender;
import com.lydia.util.Constant;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class PasswordUpdate extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public PasswordUpdate()
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
		ObjectId devid = new ObjectId(request.getParameter("devid"));
		String password = request.getParameter("password");
		if (devid != null && !devid.equals("") && password != null
				&& !password.equals(""))
		{
			DBCollection userCollection = com.lydia.util.MongoDBUtil
					.getTestCollection(Constant.DEVELOPER);
			DBObject queryById = new BasicDBObject();
			queryById.put("_id", devid);
			DBObject findById = userCollection.findOne(queryById);
			if (findById != null)
			{
				DBObject condition = new BasicDBObject();
				condition.put("_id", devid);

				DBObject update = new BasicDBObject();
				update.put("password", Md5Util.md5(password));

				DBObject setValue = new BasicDBObject();
				setValue.put("$set", update);

				userCollection.update(condition, setValue);
				JsonObject object = new JsonObject();
				object.addProperty("status", Constant.RESULT_SUCCESS);
				out.println(object);
			}
		}
		else
		{
			out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID + "}");
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
