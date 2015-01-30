package com.bucket;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import md5.Md5Util;
import net.sf.json.JSONArray;

import com.lydia.util.Constant;

public class Upyun extends HttpServlet
{
	/**
	 * Constructor of the object.
	 */
	public Upyun()
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
		try
		{
			String form_api_secret = "QRdmINCmg9r85clHFDQCyeJfaeg=";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("expiration", new Date().getTime() + 600);
			map.put("x-gmkerl-type", "fix_both");
			map.put("x-gmkerl-value", "180x180");
			JSONArray json = JSONArray.fromObject(map);
			String policy = new sun.misc.BASE64Encoder().encode(json.toString().getBytes());
			String signature = Md5Util.md5(policy.concat(form_api_secret));
			Map<String,String> hash=new HashMap<String,String>();
			hash.put("policy",policy);
			hash.put("signature",signature);
			JSONArray j=JSONArray.fromObject(hash);
			out.println("{\"status\":\""+Constant.RESULT_SUCCESS+"\",\"result\":"+j+"}");
		}
		catch (Exception e)
		{
			out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR + "}");
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
