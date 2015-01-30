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
import com.lydia.bean.Developer;
import com.lydia.bean.Info;
import com.lydia.bean.ManifestUrl;
import com.lydia.bean.Result;
import com.lydia.bean.ShortUrl;
import com.lydia.util.Constant;
import com.lydia.util.JsonUtil;
import com.lydia.util.JsonValidator;
import com.lydia.util.PageSpider;
import com.lydia.util.Parameter;
import com.lydia.util.SyncHttp;

public class GetManifestURL extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetManifestURL() {
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
					 System.out.println("dkhfka==-="+su.getExpand());
					 ManifestUrl manifestUrl=new ManifestUrl();
					 manifestUrl.setStatus(Constant.RESULT_SUCCESS);
					 manifestUrl.setInfo(su.getExpand().substring(7));
					 String jsonStr = JsonUtil.toJson(manifestUrl);  
					 out.println(jsonStr);
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
