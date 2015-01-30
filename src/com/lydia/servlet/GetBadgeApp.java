package com.lydia.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.lydia.bean.Developer;
import com.lydia.bean.Info;
import com.lydia.bean.Result;
import com.lydia.bean.ShortUrl;
import com.lydia.util.Constant;
import com.lydia.util.JsonUtil;
import com.lydia.util.JsonValidator;
import com.lydia.util.PageSpider;
import com.lydia.util.Parameter;
import com.lydia.util.SyncHttp;

public class GetBadgeApp extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public GetBadgeApp() {
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
			String url=request.getParameter("p");
			System.out.println("-------"+url);
			
			if(url==null||url.equals(""))
			{
				out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
			}
			else
			{
				List<Parameter> pList=new ArrayList<Parameter>();
				Parameter p=new Parameter();
				p.setName("shorten");
				p.setValue(url);
				pList.add(p);
				String reponse;
				try {
					reponse = new SyncHttp().httpPost("http://0.0.0.0:8080/j/expand",pList);
				    ShortUrl su=JsonUtil.fromJson(reponse,ShortUrl.class);
					JsonValidator js=new JsonValidator();
					PageSpider ps=new PageSpider(su.getExpand());
					 Info info=new Info();
					 info.setManifestURL(su.getExpand());
					 System.out.println("dkhfka==-="+su.getExpand());
					if(js.validate(ps.data))
					{
						 //json字符串转换成Map对象  
//				        Map<String,Object> rtn = JsonUtil.fromJson(ps.data, new TypeToken<Map<String,Object>>() {
//						}.getType());
//				        for(Entry<String, Object> entry : rtn.entrySet()){
//				        	String key=entry.getKey();
//				        	if("name".equals(key))
//				        	{
//				        		info.setName(rtn.get("name").toString());
//				        	}
//				        	
//				        	if("developer".equals(key))
//				        	{
//				        		System.out.println(entry.getValue());
//				        	    Developer developer = JsonUtil.fromJson(JsonUtil.toJson(entry.getValue()), Developer.class);
//				        	    info.setDeveloper(developer.getName());
//				        	}
//				        	if("icons".equals(key))
//				        	{
//				        		System.out.println("xiaoliz="+JsonUtil.toJson(rtn.get("icons")));
//				        		//info.setIconURL(ps.data.substring(0,13)+JsonUtil.toJson(entry.getValue()).substring(8));
//				        	}
//				        }
						
					    Map<String,Object> rtn = JsonUtil.parseJSONMap(ps.data);
						info.setName(rtn.get("name").toString());
					    Developer developer = JsonUtil.fromJson(JsonUtil.toJson(rtn.get("developer")), Developer.class);
					    info.setDeveloper(developer.getName());
					    System.out.println("xiaoliz="+JsonUtil.toJson(rtn.get("icons")));
					
					 //   System.out.println("sss="+JsonUtil.parseJSONMap(JsonUtil.toJson(rtn.get("icons"))).get("128").toString());
					    
					    if(JsonUtil.parseJSONMap(JsonUtil.toJson(rtn.get("icons"))).get("128").toString().startsWith("http"))
					    {
					    	  info.setIconURL(JsonUtil.parseJSONMap(JsonUtil.toJson(rtn.get("icons"))).get("128").toString());
					    	  Result r=new Result();
						      r.setStatus(Constant.RESULT_SUCCESS);
						      r.setInfo(info);
						      String jsonStr = JsonUtil.toJson(r);  
							  out.println(jsonStr);
					    }
					    else
					    {
					    	out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
					    	return;
					    }
					    
					}
					else
					{
						out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR+"}");
				}
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
