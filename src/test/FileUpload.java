package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUpload extends HttpServlet
{

	/**
	 * Constructor of the object.
	 */
	public FileUpload()
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
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
			throws ServletException, IOException
	{

		try
		{
			ServletFileUpload upload = new ServletFileUpload();

			FileItemIterator iterator = upload.getItemIterator(request);

			StringBuilder sb = new StringBuilder("{\"result\": [");

			while (iterator.hasNext())
			{
				sb.append("{");
				FileItemStream item = iterator.next();
				sb.append("\"fieldName\":\"").append(item.getFieldName())
						.append("\",");
				if (item.getName() != null)
				{
					sb.append("\"name\":\"").append(item.getName())
							.append("\",");
				}
				if (item.getName() != null)
				{
					sb.append("\"size\":\"")
							.append(fileSize(item.openStream())).append("\"");
				}
				else
				{
					sb.append("\"value\":\"").append(read(item.openStream()))
							.append("\"");
				}
				sb.append("}");
				if (iterator.hasNext())
				{
					sb.append(",");
				}
			}
			sb.append("]}");
			response.setContentType("application/json");
			PrintWriter printWriter = new PrintWriter(response.getOutputStream());
			try
			{
				printWriter.print(sb.toString());
				printWriter.flush();
			}
			finally
			{
				printWriter.close();
			}
		}
		catch (Exception ex)
		{
			throw new ServletException(ex);
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
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
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return length;

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
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
			}
		}
		return sb.toString();

	}
}
