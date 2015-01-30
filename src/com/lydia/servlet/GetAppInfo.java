package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;
import com.lydia.bean.App;
import com.lydia.bean.AppInfoResult;
import com.lydia.util.Constant;
import com.lydia.util.JsonUtil;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class GetAppInfo extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetAppInfo() {
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
		 doPost(request,response);
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
		String appId=request.getParameter("appId");
		if (Token.isTokenStringValid(
				request.getParameter(Token.TOKEN_STRING_NAME),
				request.getSession())){
			DBCollection appsCollection = MongoDBUtil.getTestCollection(Constant.APPS);
			DBObject queryById = new BasicDBObject();
			queryById.put("_id", new ObjectId(appId));
			DBObject findById = appsCollection.findOne(queryById);
			if(findById==null)
			{
				JsonObject object=new JsonObject();
				object.addProperty("status",Constant.RESULT_PARAM_INVALID);
				out.println(object);
			}else{
				AppInfoResult result=new AppInfoResult();
				App app=new App();
				app.setAppName(findById.get("name").toString());
				app.setAppCategory(findById.get("category").toString());
				app.setAppDescription(findById.get("description").toString());
				app.setAppURL(findById.get("url").toString());
				app.setStatus(Integer.valueOf(findById.get("status").toString()));
				if(findById.get("manifest")!=null){
					app.setManifest(findById.get("manifest").toString());
				}else{
					app.setManifest("");
				}
				if(findById.get("keywords")!=null)
				app.setAppKeywords(findById.get("keywords").toString());
				else{
					app.setAppKeywords("");
				}
				result.setStatus(Constant.RESULT_SUCCESS);
				result.setApp(app);
				String jsonStr = JsonUtil.toJson(result);  
				out.println(jsonStr);
			}
		}else{
			out.println("{\"status\":" + Constant.RESULT_TOKEN_INVALID + "}");
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
