package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;

import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.lydia.util.RedisUtil;
import com.lydia.util.Token;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class AppSearch extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public AppSearch() {
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
	
	
	//Ä£ºý²éÑ¯
	private BasicDBObject getLikeStr(String findStr) {
	  Pattern pattern = Pattern.compile("^.*" + findStr + ".*$", Pattern.CASE_INSENSITIVE);
	  return new BasicDBObject("$regex", pattern);
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
		if (Token.isTokenStringValid(
				request.getParameter(Token.TOKEN_STRING_NAME),
				request.getSession()))
		{
			DBCollection appsCollection = MongoDBUtil
					.getTestCollection(Constant.APPS);
			DBObject queryById = new BasicDBObject();
			queryById.put("name", getLikeStr(request.getParameter("name")));
			DBCursor apps=appsCollection.find(queryById);
			
			DBObject query = new BasicDBObject();
			ObjectId devid = new ObjectId(request.getParameter("devid"));
			query.put("devid", devid);
			DBCollection appsAnotehrCollection = MongoDBUtil
					.getTestTwoCollection(Constant.APPS);
			DBCursor findByIdTwo = appsAnotehrCollection.find(query);
			
			if(apps==null)
			{
				out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
						+ "}");
				return;
			}
			else
			{
				try
				{	
					Iterator<DBObject> iterTwo=null;
					boolean flag;
					Iterator<DBObject> iter = apps.iterator();
					String str = "[";
					while (iter.hasNext())
					{
						flag=true;
						DBObject db = iter.next();
						String id=db.get("_id").toString();
						String name = db.get("name").toString();
						if(findByIdTwo!=null){
							iterTwo = findByIdTwo.iterator();
							if(iterTwo!=null){
								while (iterTwo.hasNext()&&flag){
									DBObject dbTwo = iterTwo.next();
									String nameTwo = dbTwo.get("name").toString();
									System.out.println("nameTwo+++"+nameTwo);
									System.out.println("name+++"+name);
									if(nameTwo.equals(name)){
										flag=false;
								    }
								}
							}
						}
						
						if(flag){
							str = str + "{\"_id\":\"" + id + "\",\"name\":\"" + name
									+"\"},";
						}
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
