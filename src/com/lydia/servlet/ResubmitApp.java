package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;
import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ResubmitApp extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4473262588167431195L;

	/**
	 * Constructor of the object.
	 */
	public ResubmitApp()
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
		String appId=request.getParameter("appId");
		if(Token.isTokenStringValid(request.getParameter(Token.TOKEN_STRING_NAME), request.getSession()))
		{
			if(appId==null||"".equals(appId))
			{
				JsonObject object=new JsonObject();
				object.addProperty("status",Constant.RESULT_PARAM_INVALID);
				System.out.println("---object--"+object);
				out.println(object);
			}
			else
			{
				DBCollection appsCollection = MongoDBUtil.getTestCollection(Constant.APPS);
				DBObject queryById = new BasicDBObject();
				queryById.put("_id", new ObjectId(appId));
				DBObject findById = appsCollection.findOne(queryById);
				
				if(findById==null)
				{
					JsonObject object=new JsonObject();
					object.addProperty("status",Constant.RESULT_PARAM_INVALID);
					out.println(object);
				}
				else
				{
//					long rejectionTimes=Long.valueOf(findById.get("rejectionTimes").toString());
//				    String s=findById.get("rejectDate").toString();
//				    
//				    if(s!=null){
//				    	 SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//						    Date dt2;
//							try {
//								dt2 = sdf.parse(s);
//							    long lTime = dt2.getTime();
//							    long today=new Date().getTime();
//							    if(rejectionTimes>=3){
//							    	if((today-lTime)<(7*24*60*60*1000)){
//							    		DBObject update = new BasicDBObject();
//										update.put("status", Constant.STATUS_ABUSE);
//										
//										DBObject setValue = new BasicDBObject();
//										setValue.put("$set", update);
//										
//										appsCollection.update(queryById,setValue);
//										
//										JsonObject object=new JsonObject();
//										object.addProperty("status",Constant.RESULT_SUCCESS);
//										out.println(object);
//										return;
//							    	}
//							    }
//							} catch (ParseException e) {		
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//				    }
				    
					DBObject update = new BasicDBObject();
					update.put("status", Constant.STATUS_REVIEW);
					
					DBObject setValue = new BasicDBObject();
					setValue.put("$set", update);
					
					appsCollection.update(queryById,setValue);
					
					JsonObject object=new JsonObject();
					object.addProperty("status",Constant.RESULT_SUCCESS);
					out.println(object);
				}
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
