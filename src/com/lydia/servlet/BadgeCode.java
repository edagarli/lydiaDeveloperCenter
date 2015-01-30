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

import com.lydia.bean.Badge;
import com.lydia.bean.ShortUrl;
import com.lydia.util.BufferedReaderFile;
import com.lydia.util.Constant;
import com.lydia.util.JsonUtil;
import com.lydia.util.JsonValidator;
import com.lydia.util.PageSpider;
import com.lydia.util.Parameter;
import com.lydia.util.SyncHttp;
import com.lydia.util.Token;

public class BadgeCode extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public BadgeCode() {
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
//		if (Token.isTokenStringValid(
//				request.getParameter(Token.TOKEN_STRING_NAME),
//				request.getSession()))
//		{
			String url=request.getParameter("url");
			System.out.println("-------"+url);
			if(url==null||url.equals("")||!url.endsWith("manifest.webapp"))
			{
				out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
			}
			else
			{
				JsonValidator js=new JsonValidator();
				PageSpider ps=new PageSpider(url);
				System.out.println("===2==");
				if(js.validate(ps.data))
				{
					System.out.println("===3==");
					   Map<String,Object> rtn = JsonUtil.parseJSONMap(ps.data);
					   if(JsonUtil.parseJSONMap(JsonUtil.toJson(rtn.get("icons"))).get("128").toString().startsWith("http"))
					    {
							try {
								System.out.println("===4==");
								List<Parameter> pList=new ArrayList<Parameter>();
								Parameter p=new Parameter();
								p.setName("url");
								p.setValue(url);
								pList.add(p);
								System.out.println("===5==");
								System.out.println("----sss=====");
								String reponse=new SyncHttp().httpPost("http://0.0.0.0:8080/j/shorten",pList);
								System.out.println("====reponse=="+reponse);
							    ShortUrl su=JsonUtil.fromJson(reponse,ShortUrl.class);
							    Badge badge=new Badge();
							    badge.setStatus(Constant.RESULT_SUCCESS);
							    badge.setInstallPage("http://lydiabox.com/apps/"+su.getShorten().substring(20));
							    String jsonStr = JsonUtil.toJson(badge);  
							    out.println(jsonStr);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR+"}");
							}
					    }
					   else
					   {
						   out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
					   }
				
				}
				else
				{
					out.println("{\"status\":"+Constant.RESULT_PARAM_INVALID+"}");
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
