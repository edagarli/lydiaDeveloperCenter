package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;
import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.RedisUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class GetApps extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public GetApps()
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		if (Token.isTokenStringValid(
				request.getParameter(Token.TOKEN_STRING_NAME),
				request.getSession()))
		{
			DBCollection appsCollection = MongoDBUtil
					.getTestCollection(Constant.APPS);
			
			DBObject queryById = new BasicDBObject();
			ObjectId devid = new ObjectId(request.getParameter("devid"));
			queryById.put("devid", devid);
			DBCursor findById = appsCollection.find(queryById);
			
			if (findById == null)
			{
				out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
						+ "}");
				return;
			} 
			else if (findById != null)
			{
				try
				{
					Iterator<DBObject> iter = findById.iterator();
					while (iter.hasNext())
					{
						DBObject db = iter.next();
						long rejectionTimes=0;
						if(db.get("rejectionTimes")!=null){
							 rejectionTimes=Long.valueOf(db.get("rejectionTimes").toString());
							 System.out.println("rejectionTimes--"+rejectionTimes);
						}
						
					    Object s=db.get("rejectDate");
					    
					    if(s!=null){
					    	SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
					    	 SimpleDateFormat sdf= new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
							    Date dt2;
								try {
									dt2 = formatter.parse(s.toString());
//									Date d1= sdf(dt2);
								    long lTime =dt2.getTime();
								    long today=new Date().getTime();
								    if(rejectionTimes>=3){
								    	System.out.println("today--"+today);
								    	System.out.println("lTime--"+lTime);
								    	if((today-lTime)>(7*24*60*60*1000)){
								    		System.out.println("lTime----2");
								    		DBObject condition = new BasicDBObject();
								    		String appId=db.get("_id").toString();
											condition.put("_id", new ObjectId(appId));
								    		DBObject update = new BasicDBObject();
								    		update.put("rejectionTimes",0);
											update.put("status", Constant.STATUS_REJECTED);
											
											DBObject setValue = new BasicDBObject();
											setValue.put("$set", update);
											appsCollection.update(condition,setValue);
								    	}
								    }
								} catch (ParseException e) {		
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					    }
					}
				} catch (Exception e)
				{
					e.printStackTrace();
					out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR
							+ "}");
					return;
				}
			}
			
			DBCollection appsAnotehrCollection = MongoDBUtil
					.getTestTwoCollection(Constant.APPS);
			DBCursor findByIdTwo = appsAnotehrCollection.find(queryById);
			
			if ( findByIdTwo == null)
			{
				out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
						+ "}");
				return;
			} 
			else if ( findByIdTwo != null)
			{
				try
				{
					Iterator<DBObject> iter = findByIdTwo.iterator();
					String str = "[";
					System.out.println("-----s---" + str);
					System.out.println("---u---" + iter);
					while (iter.hasNext())
					{
						DBObject db = iter.next();
						String id = db.get("_id").toString();
						String name = db.get("name").toString();
						String manifest="";
						if(db.get("manifest")!=null){
							manifest = db.get("manifest").toString();
						}else{
							manifest = "";
						}
						String status = db.get("status").toString();
						Iterator t1 = RedisUtil.getJedisSet().iterator();
						String openTimes=null;
						while (t1.hasNext())
						{
							Object obj1 = t1.next();
							
							if(obj1.toString().equals(id))
							{
								 openTimes=Integer.valueOf(RedisUtil.jedis.hkeys(obj1.toString()).size()).toString();
							}
						}
						// = db.get("downloads").toString();
						String description = db.get("description").toString();
						str = str + "{\"id\":\"" + id + "\",\"name\":\"" + name
								+ "\",\"manifest\":\"" + manifest + "\",\"status\":\""
								+ status + "\",\"downloads\":\"" + openTimes
								+ "\",\"description\":\"" + description
								+ "\"},";
						System.out.println("---jsas------" + str);
					}

					if (str.indexOf(",") == -1)
					{
						str += "]";
					} else
					{
						str = str.substring(0, str.length() - 1);
						str += "]";
					}
					out.println("{\"status\":" + Constant.RESULT_SUCCESS
							+ ",\"apps\":" + str + "}");
				} catch (Exception e)
				{
					e.printStackTrace();
					out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR
							+ "}");
					return;
				}
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
