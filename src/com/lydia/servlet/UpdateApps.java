package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;
import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UpdateApps extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public UpdateApps()
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
	    String manifestURL=request.getParameter("manifestURL");
	    String icon_src=request.getParameter("icon_src");
	    String appId=request.getParameter("appId");
	    String keywords=request.getParameter("keywords");
	    int keyInt=keywords.split(",").length;
	    if(Token.isTokenStringValid(request.getParameter(Token.TOKEN_STRING_NAME), request.getSession()))
		{
	    	if(manifestURL==null||"".equals(manifestURL)||icon_src==null||"".equals(icon_src)||appId==null||"".equals(appId))
	    	{
	    		JsonObject object=new JsonObject();
				object.addProperty("status",Constant.RESULT_PARAM_INVALID);
				System.out.println("---object--"+object);
				out.println(object);
	    	}
	    	else if(keywords==null||keyInt==0)
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
					JSONObject ob=JSONObject.fromObject(manifestURL);
					Object launch_path=ob.get("launch_path");
					JSONObject data=ob.getJSONObject("developer");  
					Object url=data.get("url");
						
					DBObject update = new BasicDBObject();
					update.put("manifest", manifestURL);
					update.put("icon_src", icon_src);
					update.put("url", new StringBuffer(url.toString()).append(launch_path.toString()));
					update.put("keywords", keywords);
					update.put("status",Constant.STATUS_REVIEW);
					
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
	    	JsonObject object=new JsonObject();
			object.addProperty("status",Constant.RESULT_TOKEN_INVALID);
			out.println(object);
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
