package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import md5.Md5Util;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;
import com.lydia.mail.EmailSender;
import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class ForgetPwd extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	public ForgetPwd()
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
		PrintWriter out = response.getWriter();
		if (request.getParameter("email") == null
				|| request.getParameter("email").equals(""))
		{
			out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID + "}");
		}
		else
		{
			try
			{
				DBCollection userCollection = MongoDBUtil
						.getTestCollection(Constant.DEVELOPER);
				DBObject queryByName = new BasicDBObject();

				queryByName.put("email", request.getParameter("email"));
				DBObject findByUsername = userCollection.findOne(queryByName);
				if (findByUsername == null)
				{
					JsonObject object = new JsonObject();
					object.addProperty("status", Constant.RESULT_PARAM_INVALID);
					out.println(object);
				}
				else
				{
					if ("1".equals(request.getParameter("sign")))
					{
						String code = new EmailSender().sendMail(request
								.getParameter("email"));
						System.out.println("-++++--" + code);
						DBObject condition = new BasicDBObject();
						condition.put("_id",
								new ObjectId(findByUsername.get("_id")
										.toString()));

						DBObject update = new BasicDBObject();
						update.put("code", code);
						DBObject setValue = new BasicDBObject();
						setValue.put("$set", update);
						userCollection.update(condition, setValue);
						JsonObject object = new JsonObject();
						object.addProperty("status", Constant.RESULT_SUCCESS);
						out.println(object);
					}
					else if("2".equals(request.getParameter("sign")))
					{
						String code=request.getParameter("code");
						if (code.equals(findByUsername.get("code")))
						{
							out.println("{\"status\":" + Constant.RESULT_SUCCESS
									+ ",\"devid\":\"" + findByUsername.get("_id")
									+ "\"}");
						}
						else
						{
							out.println("{\"status\":"
									+ Constant.RESULT_PARAM_INVALID
									+ ",\"devid\":\"\"}");
						}
					}
				}
			}
			catch (Exception e)
			{
				out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR
						+ ",\"devid\":\"\"}");
			}
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
