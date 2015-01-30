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

public class ModifyApp extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ModifyApp() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
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
			throws ServletException, IOException {

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
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
	    String name=request.getParameter("name");
	    String url=request.getParameter("url");
	    String description=request.getParameter("description");
	    String appId=request.getParameter("appId");
	    String category=request.getParameter("category");
	    String keywords=request.getParameter("keywords");
	    int keyInt=keywords.split(",").length;
	    if(Token.isTokenStringValid(request.getParameter(Token.TOKEN_STRING_NAME), request.getSession()))
		{
	    	if(name==null||"".equals(name)||url==null||"".equals(url)||description==null||"".equals(description)||appId==null||"".equals(appId)||category==null||"".equals(category))
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
				}else if(Integer.valueOf(findById.get("status").toString()).intValue()==Constant.STATUS_APPROVED){
					JsonObject object=new JsonObject();
					object.addProperty("status",Constant.RESULT_PARAM_INVALID);
					out.println(object);
				}
				else{
					DBObject update = new BasicDBObject();
					update.put("url", url);
					update.put("name", name);
					update.put("description", description);
					update.put("category",category);
					update.put("keywords", keywords);
					
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
	public void init() throws ServletException {
		// Put your code here
	}

}
