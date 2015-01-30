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

import com.lydia.bean.Badge;
import com.lydia.bean.Code;
import com.lydia.mail.EmailSender;
import com.lydia.util.Constant;
import com.lydia.util.JsonUtil;
import com.lydia.util.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DeveloperRegister extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public DeveloperRegister()
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
		if( request.getParameter("email")==null||request.getParameter("email").equals(""))
		{
			System.out.println("email====="+ request.getParameter("email"));
			out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID+"}");
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
					try
					{
//						DBCollection user = MongoDBUtil
//								.getTestCollection(Constant.DEVELOPER);
//						DBObject query = new BasicDBObject();
//						query.put("invitationCode", request.getParameter("invitationCode"));
//						DBObject db = user.findOne(query);
//						if(db!=null){
//							    Code code=new Code();
//							    code.setStatus(Constant.RESULT_MESSAGE);	
//							    code.setMessage("邀请码已使用,请获取新的邀请码");
//							    String jsonStr = JsonUtil.toJson(code);  
//							    out.println(jsonStr);
//							    return;
//						}
//						
//						DBCollection dbColl = MongoDBUtil
//								.getTestCollection(Constant.INVITATIONCODE);
//						DBObject queryDb= new BasicDBObject();
//						queryDb.put("code", request.getParameter("invitationCode"));
//						DBObject dbObject= dbColl.findOne(queryDb);
//						if(dbObject==null){
//							 Code code=new Code();
//							    code.setStatus(Constant.RESULT_MESSAGE);	
//							    code.setMessage("无效的邀请码");
//							    String jsonStr = JsonUtil.toJson(code);  
//							    out.println(jsonStr);
//							    return;
//						}
						
						DBObject developer = new BasicDBObject();
						developer.put("email", request.getParameter("email"));
						developer.put("password", Md5Util.md5(request.getParameter("password")));
						developer.put("name", request.getParameter("name"));
						developer.put("website", request.getParameter("website"));
//						developer.put("invitationCode", request.getParameter("invitationCode"));
						developer.put("date", new Date());
						String code = new EmailSender().sendMail(request
								.getParameter("email"));
						developer.put("code", code);
						developer.put("codeStatus", 0);
						System.out.println("hasdajsh------" + code);
						userCollection.save(developer);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
								+ ",\"devid\":\"\"}");
						return;
					}
					DBObject deve = userCollection.findOne(queryByName);
					out.println("{\"status\":" + Constant.RESULT_SUCCESS
							+ ",\"devid\":\"" + deve.get("_id") + "\"}");
				}
				else
				{
					String code = request.getParameter("code");
					if (code.equals(findByUsername.get("code")))
					{
						DBObject condition = new BasicDBObject();
						condition.put("_id", new ObjectId(findByUsername.get("_id").toString()));
						
						DBObject update = new BasicDBObject();
						update.put("codeStatus",1);

						DBObject setValue = new BasicDBObject();
						setValue.put("$set", update);

						userCollection.update(condition, setValue);
						
						out.println("{\"status\":" + Constant.RESULT_SUCCESS
								+ ",\"devid\":\"" + findByUsername.get("_id") + "\"}");
					}
					else
					{
						out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
								+ ",\"devid\":\"\"}");
					}
				}
			}
			catch (Exception e)
			{
				out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR
						+ ",\"devid\":\"\"}");
				e.printStackTrace();
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
