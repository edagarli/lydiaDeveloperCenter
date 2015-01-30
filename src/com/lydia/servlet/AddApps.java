package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.UpYun;
import com.bucket.UploadIcon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class AddApps extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public AddApps()
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (Token.isTokenStringValid(
				request.getParameter(Token.TOKEN_STRING_NAME),
				request.getSession()))
		{
			System.out.println("===alibaba==");
			String name = request.getParameter("name");
			String url = request.getParameter("url");
			String description = request.getParameter("description");
			String icon_src = request.getParameter("icon_src");
			String category = request.getParameter("category");
			String devid = request.getParameter("devid");
			String manifest = request.getParameter("manifest");
			String keywords = request.getParameter("keywords");
			int keyInt = keywords.split(",").length;
			System.out.println("===alibaba=3=");
			System.err.println("===="+name);
			System.err.println("===="+description);
			System.err.println("===="+icon_src);
			System.err.println("===="+category);
			System.err.println("===="+devid);
			System.err.println("===="+manifest);
			if (name == null || description == null || icon_src == null
					|| category == null || devid == null || manifest == null
					)
			{
				out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
						+ "}");
				System.out.println("===alibaba2==");
			} else if (name.equals("") || description.equals("")
					|| icon_src.equals("") || category.equals("")
					|| devid.equals("") || manifest.equals("")
					)
			{
				out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
						+ "}");
			}
			else if(keywords==null||keyInt==0)
	    	{
	    		JsonObject object=new JsonObject();
				object.addProperty("status",Constant.RESULT_PARAM_INVALID);
				System.out.println("---object--"+object);
				out.println(object);	
				System.out.println("===alibaba4==");
	    	}
			else
			{
				DBCollection appsCollection = MongoDBUtil
						.getTestCollection(Constant.APPS);
				try
				{
					DBObject queryByName = new BasicDBObject();
					queryByName.put("name", request.getParameter("name"));
					DBObject findByName = appsCollection.findOne(queryByName);
					DBObject queryByUrl = new BasicDBObject();
					queryByUrl.put("url", request.getParameter("url"));
					DBObject findByUrl = appsCollection.findOne(queryByUrl);
					

					if (findByUrl == null && findByName == null)
					{
						try
						{
							/*
							 * DBObject byAppId = new BasicDBObject();
							 * byAppId.put("_id", appId);
							 */
							DBObject apps = new BasicDBObject();
							apps.put("name", request.getParameter("name"));
							apps.put("url", request.getParameter("url"));
							apps.put("description",
									request.getParameter("description"));
							apps.put("icon_src",
									request.getParameter("icon_src"));
							apps.put("category",
									request.getParameter("category"));
							System.out.println("platforms===="+request.getParameter("platforms[ios]"));
							String[] arr;
							
							if(request.getParameter("platforms[ios]")!=null||request.getParameter("platforms[android]")!=null)
							{
								arr=new String[2];
								if(request.getParameter("platforms[ios]")!=null&&request.getParameter("platforms[android]")!=null){
									arr[0]="ios";
									arr[1]="android";
								}
								else if(request.getParameter("platforms[ios]")!=null&&request.getParameter("platforms[android]")==null){
									arr[0]="ios";
								}
								else if(request.getParameter("platforms[ios]")==null&&request.getParameter("platforms[android]")!=null){
									arr[0]="android";
								}
								apps.put("platforms",arr);
							}
							apps.put("keywords", keywords);
							apps.put("manifest", manifest);
							apps.put("devid",
									new ObjectId(request.getParameter("devid")));
							DBCollection userCollection = MongoDBUtil.getTestCollection(Constant.DEVELOPER);
							DBObject queryById = new BasicDBObject();
							queryById.put("_id",new ObjectId(devid));
							DBObject findById = userCollection.findOne(queryById);
							apps.put("author", findById.get("name")
									.toString());
							apps.put("downloads", 0);
							apps.put("status", 0); //
							apps.put("date", new Date());

							// DBObject setValue = new BasicDBObject();
							// setValue.put("$set", apps);

							// appsCollection.update(byAppId, setValue);
							appsCollection.save(apps);
							
							
							DBObject queryBy = new BasicDBObject();
							queryByName.put("name", request.getParameter("name"));
							DBObject findByN= appsCollection.findOne(queryByName);
							
							String getAppId = findByN.get("_id").toString();
							
							System.out.println("kashfk-======"+getAppId);

							DBObject byAppId = new BasicDBObject();
							byAppId.put("_id", getAppId);

							DBObject update = new BasicDBObject();
							update.put("icon_src", getAppId + ".png");

							DBObject setValue = new BasicDBObject();
							setValue.put("$set", update);

							appsCollection.update(byAppId, setValue);

							UpYun upyun = new UpYun(UploadIcon.BUCKET_NAME,
									UploadIcon.USER_NAME, UploadIcon.USER_PWD);

							// List<FolderItem> fileItems = upyun.readDir2(
							// UploadIcon.DIR_ROOT
							// + UploadIcon.FOLDER_NAME + "/",
							// icon_src);

							// FolderItem fo = fileItems.get(0);
							System.out.println("====="+UploadIcon.DIR_ROOT
									+ UploadIcon.FOLDER_NAME + "/" + icon_src.substring(31));
//							upyun.renameFile(UploadIcon.DIR_ROOT
//									+ UploadIcon.FOLDER_NAME + "/" + icon_src.substring(31),
//									UploadIcon.DIR_ROOT
//											+ UploadIcon.FOLDER_NAME + "/"
//											+ getAppId + ".png", false);

							out.println("{\"status\":"
									+ Constant.RESULT_SUCCESS + "}");
							return;
						} catch (Exception e)
						{
							e.printStackTrace();
							out.println("{\"status\":"
									+ Constant.RESULT_INTERNAL_ERROR + "}");
							return;
						}
					}else
					{
						out.println("{\"status\":"
								+ Constant.RESULT_PARAM_INVALID + "}");
						System.out.println("===alibaba5==");
					}

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
			System.out.println("===alibaba=6=");
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
