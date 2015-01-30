package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import net.sf.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.lydia.util.Constant;
import com.lydia.util.JsonUtil;
import com.lydia.util.JsonValidator;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.PageSpider;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class AppClaim extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public AppClaim() {
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String id=request.getParameter("devid");
		String appName=request.getParameter("appName");
		String manifestURL=request.getParameter("manifestURL");
		if (Token.isTokenStringValid(
				request.getParameter(Token.TOKEN_STRING_NAME),
				request.getSession()))
		{
			if(id==null||"".equals(id)||appName==null||"".equals(appName)||manifestURL==null||"".equals(manifestURL))
			{
				JsonObject object=new JsonObject();
				object.addProperty("status",Constant.RESULT_PARAM_INVALID);
				System.out.println("---object--"+object);
				out.println(object);
			}
			else
			{
//				DBCollection userCollection = MongoDBUtil.getTestCollection(Constant.APPS);
//				DBObject query=new BasicDBObject();
//				query.put("_id",new ObjectId(id));
//			    DBObject find=userCollection.findOne(query);
				
				DBCollection appsCollection = MongoDBUtil.getTestCollection(Constant.APPS);
				DBObject queryById = new BasicDBObject();
				queryById.put("name",appName);
				DBObject findById = appsCollection.findOne(queryById);
				Object devid=findById.get("devid");
		        System.out.println("xiongxiongshishabi====="+devid);
				Object manifest=findById.get("manifest");
				if(findById==null)
				{
					JsonObject object=new JsonObject();
					object.addProperty("status",Constant.RESULT_PARAM_INVALID);
					out.println(object);
				}
				else
				{
					JsonValidator js=new JsonValidator();
					PageSpider ps=new PageSpider(manifestURL);
					
					if(js.validate(ps.data))
					{
						System.out.println("---ps.data--"+ps.data);
						 Map<String,Object> rtn = JsonUtil.parseJSONMap(ps.data);
					
			//		JSONObject ob=JSONObject.fromObject(manifestURL);
				//	Object launch_path=ob.get("launch_path");
					//JSONObject data=ob.getJSONObject("developer");  
				//	Object url=data.get("url");
					
					DBObject update = new BasicDBObject();
//					update.put("manifestURL", manifestURL);
//					update.put("devid",new ObjectId(id));
					ObjectId[] arr=new ObjectId[2];
					JsonObject manifestURLObject=new JsonObject();
					arr[0]=new ObjectId(id);
					manifestURLObject.addProperty(arr[0].toString(),manifestURL);
					if(devid!=null){
						arr[1]=new ObjectId(devid.toString());
						if(manifest==null){
							manifestURLObject.addProperty(arr[1].toString(), "");
						}else{
							manifestURLObject.addProperty(arr[1].toString(), manifest.toString());
						}
						
					}
					else{
						arr[1]=null;
					}
					update.put("devids", arr);
					update.put("manifests",manifestURLObject.toString());
					
//					update.put("url",rtn.get("launch_url").toString());
					update.put("status",Constant.STATUS_ARBITRATION);
					
					DBObject setValue = new BasicDBObject();
					setValue.put("$set", update);
					
					appsCollection.update(queryById,setValue);
					JsonObject object=new JsonObject();
					object.addProperty("status",Constant.RESULT_SUCCESS);
					out.println(object);
					}
					 else
					 {
						   out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
					 }
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
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
