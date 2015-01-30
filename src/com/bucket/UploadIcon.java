package com.bucket;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bson.types.ObjectId;

import com.UpYun;
import com.UpYun.PARAMS;
import com.lydia.util.Constant;
import com.lydia.util.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UploadIcon extends HttpServlet
{
	// 运行前先设置好以下三个参数
	public static final String BUCKET_NAME = "lydia";
	public static final String USER_NAME = "lizhi";
	public static final String USER_PWD = "lizhishishabi";
	/** 根目录 */
	public static final String DIR_ROOT = "/";
	/** 目录名 */
	public static final String FOLDER_NAME = "images";
	/** 上传到upyun的文件名 */
	public static String file_name = "";
	public static String valuess = "";
    public static UpYun upyun = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public UploadIcon()
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
	@SuppressWarnings("unused")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		boolean kkkk=false;
		try
		{
			String projectpath = this.getServletContext().getRealPath(
					"/images/");
			File f = new File(projectpath);
			if (!f.exists())
			{
				f.mkdir();
			}
			// 构造出文件工厂，用于存放JSP页面中传递过来的文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
			factory.setSizeThreshold(4 * 1024);
			// 设置上传文件的保存路径
			factory.setRepository(f);

			// 产生Servlet上传对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 取得所有上传文件的信息
			List<FileItem> list = upload.parseRequest(request);
			Iterator<FileItem> iter = list.iterator();
			while (iter.hasNext())
			{
				FileItem item = iter.next();
				// 如果接收到的参数不是一个普通表单(例text等)的元素，那么执行下面代码
				if (!item.isFormField())
				{
					String fieldName = item.getFieldName();// 获得此表单元素的name属性
					String fileName = item.getName();// 获得文件的完整路径
					String contentType = item.getContentType();// 获得文件类型
					long fileSize = item.getSize();// 获得文件大小
					// 从文件的完整路径中截取出文件名
					fileName = fileName.substring(
							fileName.lastIndexOf("\\") + 1, fileName.length());

					// 判断是否有图片上传
					if (!("".equals(fileName)) && !(fileName == null))
					{

						// 如果上传的文件不是图片，那么不上传
						String allImgExt = ".png|";
						String extName = fileName.substring(
								fileName.lastIndexOf("."), fileName.length());
						if (allImgExt.indexOf(extName + "|") == -1)
						{
							out.println("{\"status\":"
									+ Constant.RESULT_PARAM_INVALID + "}");
							kkkk=true;
							break;
						}

						String filepath = projectpath + "\\" + fieldName;
						File uf = new File(filepath);
						// 更改文件的保存路径，以防止文件重名的现象出现
						if (!uf.exists())
						{
							uf.mkdir();
						}

						// 此输出路径为保存到数据库中photo字段的路径
						String insertDB = filepath + "/" + fileName;
						System.out.println("文件路径：" + insertDB + ":"
								+ insertDB.length());
						valuess = insertDB;
						
						File uploadedFile = new File(filepath, fileName);
						
					
						
						file_name = String.valueOf(System.currentTimeMillis())
								+ ".png";
						
					/*	DBCollection appsCollection=MongoDBUtil.getTestCollection(Constant.APPS);
						
						DBObject apps = new BasicDBObject();
						
						apps.put("icon_src", file_name);
						
						appsCollection.save(apps);
						
						DBObject queryById = new BasicDBObject();
						queryById.put("icon_src",file_name);
						
						DBObject findById = appsCollection.findOne(queryById);
						
						if(findById.get("_id")!=null)
						{
							fileName=findById.get("_id")+".png";							
						}*/
						
						System.out.println("---filename---"+fileName);
						
						// 如果在该文件夹中已经有相同的文件，那么将其删除之后再重新创建（只适用于上传一张照片的情况）
						
						if (uploadedFile.exists())
						{ 	
							uploadedFile.delete();
						}
						item.write(uploadedFile);
						
						System.out.println("=====image===");
						BufferedImage bi = ImageIO.read(uploadedFile);

						if (bi.getWidth() != 128 || bi.getHeight() != 128)
						{
							out.println("{\"status\":"
									+ Constant.RESULT_PARAM_INVALID + "}");
							kkkk=true;
							break;
						}
					}
				}
			}
		    if(!kkkk)
		    {
		    	upyun = new UpYun(BUCKET_NAME, USER_NAME, USER_PWD);
				upyun.setDebug(true);
				boolean test = testWriteFile();
				if (test)
				{
					out.println("{\"status\":" + Constant.RESULT_SUCCESS
							+ ",\"url\":\"" + "http://cdn.lydiabox.com/images/"
							+ file_name + "\"}");
				} 
				else
				{
					out.println("{\"status\":" + Constant.RESULT_PARAM_INVALID
							+ "}");
				}
		    }

		} catch (Exception e)
		{
			out.println("{\"status\":" + Constant.RESULT_INTERNAL_ERROR + "}");
			System.out.println(e.getStackTrace());
			e.printStackTrace();
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

	protected int fileSize(InputStream stream)
	{
		int length = 0;
		try
		{
			byte[] buffer = new byte[2048];
			int size;
			while ((size = stream.read(buffer)) != -1)
			{
				length += size;
			}
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return length;

	}

	public static boolean testWriteFile() throws IOException
	{

		// 本地待上传的图片文件
		File file = new File(valuess);

		// 要传到upyun后的文件路径
		String filePath = DIR_ROOT + FOLDER_NAME + "/" + file_name;

		// 设置缩略图的参数
		Map<String, String> params = new HashMap<String, String>();

		// 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
		params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(),
				PARAMS.VALUE_FIX_BOTH.getValue());

		// 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
		params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), "128x128");

		// 设置缩略图的质量，默认 95
		params.put(PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

		// 设置缩略图的锐化，默认锐化（true）
		params.put(PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");

		// 若在 upyun 后台配置过缩略图版本号，则可以设置缩略图的版本名称
		// 注意：只有存在缩略图版本名称，才会按照配置参数制作缩略图，否则无效
		params.put(PARAMS.KEY_X_GMKERL_THUMBNAIL.getValue(), "small");

		/*
		 * 上传方法1：文本内容直接上传
		 */
		boolean result1 = upyun.writeFile(filePath, file, false, params);
		System.out.println("1.上传 " + filePath + isSuccess(result1));
		return result1;
	}

	protected String read(InputStream stream)
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		try
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		} finally
		{
			try
			{
				reader.close();
			} catch (IOException e)
			{
			}
		}
		return sb.toString();

	}

	private static String isSuccess(boolean result)
	{
		return result ? " 成功" : " 失败";
	}
}
