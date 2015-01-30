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
	// ����ǰ�����ú�������������
	public static final String BUCKET_NAME = "lydia";
	public static final String USER_NAME = "lizhi";
	public static final String USER_PWD = "lizhishishabi";
	/** ��Ŀ¼ */
	public static final String DIR_ROOT = "/";
	/** Ŀ¼�� */
	public static final String FOLDER_NAME = "images";
	/** �ϴ���upyun���ļ��� */
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
			// ������ļ����������ڴ��JSPҳ���д��ݹ������ļ�
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// ���û����С������ļ����ڻ����Сʱ�����Ȱ��ļ��ŵ�������
			factory.setSizeThreshold(4 * 1024);
			// �����ϴ��ļ��ı���·��
			factory.setRepository(f);

			// ����Servlet�ϴ�����
			ServletFileUpload upload = new ServletFileUpload(factory);
			// ȡ�������ϴ��ļ�����Ϣ
			List<FileItem> list = upload.parseRequest(request);
			Iterator<FileItem> iter = list.iterator();
			while (iter.hasNext())
			{
				FileItem item = iter.next();
				// ������յ��Ĳ�������һ����ͨ��(��text��)��Ԫ�أ���ôִ���������
				if (!item.isFormField())
				{
					String fieldName = item.getFieldName();// ��ô˱�Ԫ�ص�name����
					String fileName = item.getName();// ����ļ�������·��
					String contentType = item.getContentType();// ����ļ�����
					long fileSize = item.getSize();// ����ļ���С
					// ���ļ�������·���н�ȡ���ļ���
					fileName = fileName.substring(
							fileName.lastIndexOf("\\") + 1, fileName.length());

					// �ж��Ƿ���ͼƬ�ϴ�
					if (!("".equals(fileName)) && !(fileName == null))
					{

						// ����ϴ����ļ�����ͼƬ����ô���ϴ�
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
						// �����ļ��ı���·�����Է�ֹ�ļ��������������
						if (!uf.exists())
						{
							uf.mkdir();
						}

						// �����·��Ϊ���浽���ݿ���photo�ֶε�·��
						String insertDB = filepath + "/" + fileName;
						System.out.println("�ļ�·����" + insertDB + ":"
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
						
						// ����ڸ��ļ������Ѿ�����ͬ���ļ�����ô����ɾ��֮�������´�����ֻ�������ϴ�һ����Ƭ�������
						
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

		// ���ش��ϴ���ͼƬ�ļ�
		File file = new File(valuess);

		// Ҫ����upyun����ļ�·��
		String filePath = DIR_ROOT + FOLDER_NAME + "/" + file_name;

		// ��������ͼ�Ĳ���
		Map<String, String> params = new HashMap<String, String>();

		// ��������ͼ���ͣ������������ͼ����ֵ��KEY_VALUE��ʹ�ã�������Ч
		params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(),
				PARAMS.VALUE_FIX_BOTH.getValue());

		// ��������ͼ����ֵ�������������ͼ���ͣ�KEY_TYPE��ʹ�ã�������Ч
		params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), "128x128");

		// ��������ͼ��������Ĭ�� 95
		params.put(PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

		// ��������ͼ���񻯣�Ĭ���񻯣�true��
		params.put(PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");

		// ���� upyun ��̨���ù�����ͼ�汾�ţ��������������ͼ�İ汾����
		// ע�⣺ֻ�д�������ͼ�汾���ƣ��Żᰴ�����ò�����������ͼ��������Ч
		params.put(PARAMS.KEY_X_GMKERL_THUMBNAIL.getValue(), "small");

		/*
		 * �ϴ�����1���ı�����ֱ���ϴ�
		 */
		boolean result1 = upyun.writeFile(filePath, file, false, params);
		System.out.println("1.�ϴ� " + filePath + isSuccess(result1));
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
		return result ? " �ɹ�" : " ʧ��";
	}
}
