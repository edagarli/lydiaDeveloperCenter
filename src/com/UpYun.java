package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class UpYun {

	/** Ĭ�ϵı����ʽ */
	private static final String UTF8 = "UTF-8";

	/** SKD�汾�� */
	private final String VERSION = "2.0";

	/** ·���ķָ�� */
	private final String SEPARATOR = "/";

	private final String AUTHORIZATION = "Authorization";
	private final String DATE = "Date";
	private final String CONTENT_LENGTH = "Content-Length";
	private final String CONTENT_MD5 = "Content-MD5";
	private final String CONTENT_SECRET = "Content-Secret";
	private final String MKDIR = "mkdir";

	private final String X_UPYUN_WIDTH = "x-upyun-width";
	private final String X_UPYUN_HEIGHT = "x-upyun-height";
	private final String X_UPYUN_FRAMES = "x-upyun-frames";
	private final String X_UPYUN_FILE_TYPE = "x-upyun-file-type";
	private final String X_UPYUN_FILE_SIZE = "x-upyun-file-size";
	private final String X_UPYUN_FILE_DATE = "x-upyun-file-date";

	private final String METHOD_HEAD = "HEAD";
	private final String METHOD_GET = "GET";
	private final String METHOD_PUT = "PUT";
	private final String METHOD_DELETE = "DELETE";

	/**
	 * �������������Զ�ѡ������:v0.api.upyun.com
	 */
	public static final String ED_AUTO = "v0.api.upyun.com";
	/**
	 * ���Ž����:v1.api.upyun.com
	 */
	public static final String ED_TELECOM = "v1.api.upyun.com";
	/**
	 * ��ͨ��ͨ�����:v2.api.upyun.com
	 */
	public static final String ED_CNC = "v2.api.upyun.com";
	/**
	 * �ƶ���ͨ�����:v3.api.upyun.com
	 */
	public static final String ED_CTT = "v3.api.upyun.com";

	// Ĭ�ϲ�����debugģʽ
	public boolean debug = false;
	// Ĭ�ϵĳ�ʱʱ�䣺30��
	private int timeout = 30 * 1000;
	// Ĭ��Ϊ�Զ�ʶ������
	private String apiDomain = ED_AUTO;
	// ���ϴ��ļ��� Content-MD5 ֵ
	private String contentMD5 = null;
	// ���ϴ��ļ���"������Կ"
	private String fileSecret = null;
	// �ռ���
	protected String bucketName = null;
	// ����Ա��
	protected String userName = null;
	// ����Ա����
	protected String password = null;

	// ͼƬ��Ϣ�Ĳ���
	protected String picWidth = null;
	protected String picHeight = null;
	protected String picFrames = null;
	protected String picType = null;

	// �ļ���Ϣ�Ĳ���
	protected String fileType = null;
	protected String fileSize = null;
	protected String fileDate = null;

	/**
	 * ��ʼ�� UpYun �洢�ӿ�
	 * 
	 * @param bucketName
	 *            �ռ�����
	 * @param userName
	 *            ����Ա����
	 * @param password
	 *            ���룬����ҪMD5����
	 * @return UpYun object
	 */
	public UpYun(String bucketName, String userName, String password) {
		this.bucketName = bucketName;
		this.userName = userName;
		this.password = md5(password);
	}

	/**
	 * �л� API �ӿڵ����������
	 * <p>
	 * ��ѡ������<br>
	 * 1) UpYun.ED_AUTO(v0.api.upyun.com)��Ĭ�ϣ��������������Զ�ѡ������ <br>
	 * 2) UpYun.ED_TELECOM(v1.api.upyun.com)�����Ž����<br>
	 * 3) UpYun.ED_CNC(v2.api.upyun.com)����ͨ��ͨ�����<br>
	 * 4) UpYun.ED_CTT(v3.api.upyun.com)���ƶ���ͨ�����
	 * 
	 * @param domain
	 *            ���������
	 */
	public void setApiDomain(String domain) {
		this.apiDomain = domain;
	}

	/**
	 * �鿴��ǰ�����������
	 * 
	 * @return
	 */
	public String getApiDomain() {
		return apiDomain;
	}

	/**
	 * �������ӳ�ʱʱ�䣬Ĭ��Ϊ30��
	 * 
	 * @param second
	 *            ������60��Ϊһ���ӳ�ʱ
	 */
	public void setTimeout(int second) {
		this.timeout = second * 1000;
	}

	/**
	 * �鿴��ǰ�ĳ�ʱʱ��
	 * 
	 * @return
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * �鿴��ǰ�Ƿ���debugģʽ
	 * 
	 * @return
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * �����Ƿ���debugģʽ
	 * 
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * ���ô��ϴ��ļ��� Content-MD5 ֵ
	 * <p>
	 * ��������Ʒ�����յ����ļ�MD5ֵ���û����õĲ�һ�£����ر� 406 Not Acceptable ����
	 * 
	 * @param md5Value
	 *            �ļ� MD5 У��������
	 */
	public void setContentMD5(String md5Value) {
		this.contentMD5 = md5Value;
	}

	/**
	 * ���ô��ϴ��ļ���"������Կ"
	 * <p>
	 * ע�⣺<br>
	 * ��֧��ͼƬ�գ�������Կ���޷�����ԭ�ļ�URLֱ�ӷ��ʣ���� URL ������� ������ͼ�����־��+��Կ�� ���з���
	 * <p>
	 * ����:<br>
	 * �������ͼ�����־��Ϊ"!"����ԿΪ"bac"���ϴ��ļ�·��Ϊ"/folder/test.jpg"��<br>
	 * ��ô��ͼƬ�Ķ�����ʵ�ַΪ��http://�ռ����� /folder/test.jpg!bac
	 * 
	 * @param secret
	 *            ��Կ�ַ���
	 */
	public void setFileSecret(String secret) {
		this.fileSecret = secret;
	}

	public String getPicWidth() {
		return picWidth;
	}

	public String getPicHeight() {
		return picHeight;
	}

	public String getPicFrames() {
		return picFrames;
	}

	public String getPicType() {
		return picType;
	}

	/**
	 * ��ȡ��ǰSDK�İ汾��
	 * 
	 * @return SDK�汾��
	 */
	public String version() {
		return VERSION;
	}

	/**
	 * ��ȡ����ռ��ռ����
	 * 
	 * @param path
	 *            Ŀ��·��
	 * @return �ռ�ռ������ʧ��ʱ���� -1
	 */
	public long getBucketUsage() {
		long usage = -1;

		String result = HttpAction(METHOD_GET, formatPath("/") + "/?usage");

		if (!isEmpty(result)) {

			try {
				usage = Long.parseLong(result.trim());
			} catch (NumberFormatException e) {
			}
		} 

		return usage;
	}

	/**
	 * ��ȡĳ����Ŀ¼��ռ����
	 * 
	 * @param path
	 *            Ŀ��·��
	 * @return �ռ�ռ������ʧ��ʱ���� -1
	 */
	@Deprecated
	public long getFolderUsage(String path) {

		long usage = -1;

		String result = HttpAction(METHOD_GET, formatPath(path) + "/?usage");

		if (!isEmpty(result)) {

			try {
				usage = Long.parseLong(result.trim());
			} catch (NumberFormatException e) {
			}
		}

		return usage;
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param datas
	 *            �ļ�����
	 * 
	 * @return true or false
	 */
	public boolean writeFile(String filePath, byte[] datas) {
		return writeFile(filePath, datas, false, null);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param datas
	 *            �ļ�����
	 * @param auto
	 *            �Ƿ��Զ���������Ŀ¼(���10��)
	 * 
	 * @return true or false
	 */
	public boolean writeFile(String filePath, byte[] datas, boolean auto) {
		return writeFile(filePath, datas, auto, null);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param datas
	 *            �ļ�����
	 * @param auto
	 *            �Ƿ��Զ���������Ŀ¼(���10��)
	 * @param params
	 *            �������
	 * 
	 * @return true or false
	 */
	public boolean writeFile(String filePath, byte[] datas, boolean auto,
			Map<String, String> params) {

		return HttpAction(METHOD_PUT, formatPath(filePath), datas, null, auto,
				params) != null;
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param String
	 *            datas �ļ�����
	 * 
	 * @return true or false
	 */
	public boolean writeFile(String filePath, String datas) {
		return writeFile(filePath, datas, false, null);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param String
	 *            datas �ļ�����
	 * @param auto
	 *            �Ƿ��Զ���������Ŀ¼(���10��)
	 * 
	 * @return true or false
	 */
	public boolean writeFile(String filePath, String datas, boolean auto) {
		return writeFile(filePath, datas, auto, null);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param String
	 *            datas �ļ�����
	 * @param auto
	 *            �Ƿ��Զ���������Ŀ¼(���10��)
	 * @param params
	 *            �������
	 * 
	 * @return true or false
	 */
	public boolean writeFile(String filePath, String datas, boolean auto,
			Map<String, String> params) {

		boolean result = false;

		try {
			result = writeFile(filePath, datas.getBytes(UTF8), auto, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param file
	 *            ���ϴ����ļ�
	 * 
	 * @return true or false
	 * @throws IOException
	 */
	public boolean writeFile(String filePath, File file) throws IOException {
		return writeFile(filePath, file, false, null);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param file
	 *            ���ϴ����ļ�
	 * @param auto
	 *            �Ƿ��Զ���������Ŀ¼(���10��)
	 * 
	 * @return true or false
	 * @throws IOException
	 */
	public boolean writeFile(String filePath, File file, boolean auto)
			throws IOException {
		return writeFile(filePath, file, auto, null);
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param file
	 *            ���ϴ����ļ�
	 * @param auto
	 *            �Ƿ��Զ���������Ŀ¼(���10��)
	 * @param params
	 *            �������
	 * 
	 * @return true or false
	 * @throws IOException
	 */
	public boolean writeFile(String filePath, File file, boolean auto,
			Map<String, String> params) throws IOException {

		filePath = formatPath(filePath);

		InputStream is = null;
		OutputStream os = null;
		HttpURLConnection conn = null;

		try {
			// ��ȡ���ϴ����ļ�
			is = new FileInputStream(file);

			// ��ȡ����
			URL url = new URL("http://" + apiDomain + filePath);
			conn = (HttpURLConnection) url.openConnection();

			// ���ñ�Ҫ����
			conn.setConnectTimeout(timeout);
			conn.setRequestMethod(METHOD_PUT);
			conn.setUseCaches(false);
			conn.setDoOutput(true);

			// ����ʱ��
			conn.setRequestProperty(DATE, getGMTDate());
			// ����ǩ��
			conn.setRequestProperty(AUTHORIZATION,
					sign(conn, filePath, is.available()));

			// �����ļ��� MD5 ����
			if (!isEmpty(this.contentMD5)) {
				conn.setRequestProperty(CONTENT_MD5, this.contentMD5);
				this.contentMD5 = null;
			}

			// �����ļ��ķ�����Կ
			if (!isEmpty(this.fileSecret)) {
				conn.setRequestProperty(CONTENT_SECRET, this.fileSecret);
				this.fileSecret = null;
			}

			// �Ƿ��Զ���������Ŀ¼
			if (auto) {
				conn.setRequestProperty(MKDIR, "true");
			}

			// ���ö���Ĳ�������ͼƬ����ͼ��
			if (params != null && !params.isEmpty()) {

				for (Map.Entry<String, String> param : params.entrySet()) {
					conn.setRequestProperty(param.getKey(), param.getValue());
				}
			}

			// ��������
			conn.connect();

			os = conn.getOutputStream();
			byte[] data = new byte[4096];
			int temp = 0;

			// �ϴ��ļ�����
			while ((temp = is.read(data)) != -1) {
				os.write(data, 0, temp);
			}

			// ��ȡ���ص���Ϣ
			getText(conn, false);

			// �ϴ��ɹ�
			return true;

		} catch (IOException e) {
			if (debug)
				e.printStackTrace();

			// �ϴ�ʧ��
			return false;

		} finally {

			if (os != null) {
				os.close();
				os = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}
	
	public void rename(String file, String toFile)
	{

		File toBeRenamed = new File(file);
		// ���Ҫ���������ļ��Ƿ���ڣ��Ƿ����ļ�
		if (!toBeRenamed.exists() || toBeRenamed.isDirectory())
		{

			System.out.println("File does not exist: " + file);
			return;
		}

		File newFile = new File(toFile);

		// �޸��ļ���
		if (toBeRenamed.renameTo(newFile))
		{
			System.out.println("File has been renamed.");
		} else
		{
			System.out.println("Error renmaing file");
		}

	}
	
	
	public boolean renameFile(String filePath,String newFilePath, boolean auto) throws IOException {

		filePath = formatPath(filePath);

		InputStream is = null;
		OutputStream os = null;
		HttpURLConnection conn = null;

		try {
			// ��ȡ���ϴ����ļ�

			// ��ȡ����
			URL url = new URL("http://" + apiDomain + filePath);
			conn = (HttpURLConnection) url.openConnection();

			// ���ñ�Ҫ����
			conn.setConnectTimeout(timeout);
			conn.setRequestMethod(METHOD_PUT);
			conn.setUseCaches(false);
			conn.setDoOutput(true);

			// ����ʱ��
			conn.setRequestProperty(DATE, getGMTDate());
			// ����ǩ��
			conn.setRequestProperty(AUTHORIZATION,
					sign(conn, filePath, is.available()));

			// �����ļ��� MD5 ����
			if (!isEmpty(this.contentMD5)) {
				conn.setRequestProperty(CONTENT_MD5, this.contentMD5);
				this.contentMD5 = null;
			}

			// �����ļ��ķ�����Կ
			if (!isEmpty(this.fileSecret)) {
				conn.setRequestProperty(CONTENT_SECRET, this.fileSecret);
				this.fileSecret = null;
			}

			// �Ƿ��Զ���������Ŀ¼
			if (auto) {
				conn.setRequestProperty(MKDIR, "true");
			}

		/*	// ���ö���Ĳ�������ͼƬ����ͼ��
			if (params != null && !params.isEmpty()) {

				for (Map.Entry<String, String> param : params.entrySet()) {
					conn.setRequestProperty(param.getKey(), param.getValue());
				}
			}*/
			// ��������
			conn.connect();
			
			rename("http://" + apiDomain + filePath, "http://" + apiDomain + newFilePath);

			// �ϴ��ɹ�
			return true;

		} catch (IOException e) {
			if (debug)
				e.printStackTrace();

			// �ϴ�ʧ��
			return false;

		} finally {

			if (os != null) {
				os.close();
				os = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}
	

	/**
	 * ��ȡ�ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * 
	 * @return �ļ����� �� null
	 */
	public String readFile(String filePath) {
		return HttpAction(METHOD_GET, formatPath(filePath));
	}

	/**
	 * ��ȡ�ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * @param file
	 *            ��ʱ�ļ�
	 * 
	 * @return true or false
	 */
	public boolean readFile(String filePath, File file) {

		String result = HttpAction(METHOD_GET, formatPath(filePath), null,
				file, false);

		return "".equals(result);
	}

	/**
	 * ��ȡ�ļ���Ϣ
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * 
	 * @return �ļ���Ϣ �� null
	 */
	public Map<String, String> getFileInfo(String filePath) {

		HttpAction(METHOD_HEAD, formatPath(filePath));

		// �ж��Ƿ�����ļ���Ϣ
		if (isEmpty(fileType) && isEmpty(fileSize) && isEmpty(fileDate)) {
			return null;
		}

		Map<String, String> mp = new HashMap<String, String>();
		mp.put("type", fileType);
		mp.put("size", fileSize);
		mp.put("date", fileDate);

		return mp;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param filePath
	 *            �ļ�·���������ļ�����
	 * 
	 * @return true or false
	 */
	public boolean deleteFile(String filePath) {

		return HttpAction(METHOD_DELETE, formatPath(filePath)) != null;
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param path
	 *            Ŀ¼·��
	 * 
	 * @return true or false
	 */
	public boolean mkDir(String path) {
		return mkDir(path, false);
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param path
	 *            Ŀ¼·��
	 * @param auto
	 *            �Ƿ��Զ���������Ŀ¼(���10��)
	 * 
	 * @return true or false
	 */
	public boolean mkDir(String path, boolean auto) {

		Map<String, String> params = new HashMap<String, String>(1);
		params.put(PARAMS.KEY_MAKE_DIR.getValue(), "true");

		String result = HttpAction(METHOD_PUT, formatPath(path), null, null,
				auto, params);

		return result != null;
	}

	/**
	 * ��ȡĿ¼�б�
	 * 
	 * @param path
	 *            Ŀ¼·��
	 * 
	 * @return List<FolderItem> �� null
	 */
	public List<FolderItem> readDir(String path) {

		String result = HttpAction(METHOD_GET, formatPath(path) + SEPARATOR);

		if (isEmpty(result))
			return null;

		List<FolderItem> list = new LinkedList<FolderItem>();

		String[] datas = result.split("\n");

		for (int i = 0; i < datas.length; i++) {
			if (datas[i].indexOf("\t") > 0) {
				list.add(new FolderItem(datas[i]));
			}
		}
		return list;
	}
	
	public List<FolderItem> readDir2(String path,String filename) {

		String result = HttpAction(METHOD_GET, formatPath(path) + SEPARATOR);

		if (isEmpty(result))
			return null;

		List<FolderItem> list = new LinkedList<FolderItem>();

		String[] datas = result.split("\n");

		for (int i = 0; i < datas.length; i++) {
			if (datas[i].indexOf("\t") > 0) {
				FolderItem folderItem=new FolderItem(datas[i]);
				if(folderItem.name.equals("filename"))
				{
					list.add(new FolderItem(datas[i]));
				}
			}
		}
		return list;
	}

	/**
	 * ɾ��Ŀ¼
	 * 
	 * @param path
	 *            Ŀ¼·��
	 * 
	 * @return true or false
	 */
	public boolean rmDir(String path) {
		return HttpAction(METHOD_DELETE, formatPath(path)) != null;
	}

	/**
	 * ��ȡ�ϴ��ļ������Ϣ����ͼƬ�ռ��з������ݣ�
	 * 
	 * @param key
	 *            ��Ϣ�ֶ�����x-upyun-width��x-upyun-height��x-upyun-frames��x-upyun-file
	 *            -type��
	 * 
	 * @return value or NULL
	 * @deprecated
	 */
	public String getWritedFileInfo(String key) {

		if (isEmpty(picWidth))
			return null;

		if (X_UPYUN_WIDTH.equals(key))
			return picWidth;
		if (X_UPYUN_HEIGHT.equals(key))
			return picHeight;
		if (X_UPYUN_FRAMES.equals(key))
			return picFrames;
		if (X_UPYUN_FILE_TYPE.equals(key))
			return picType;

		return null;
	}

	/**
	 * ���ַ������� MD5 ����
	 * 
	 * @param str
	 *            �������ַ���
	 * 
	 * @return ���ܺ��ַ���
	 */
	public static String md5(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes(UTF8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		byte[] encodedValue = md5.digest();
		int j = encodedValue.length;
		char finalValue[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte encoded = encodedValue[i];
			finalValue[k++] = hexDigits[encoded >> 4 & 0xf];
			finalValue[k++] = hexDigits[encoded & 0xf];
		}

		return new String(finalValue);
	}

	/**
	 * ���ļ����� MD5 ����
	 * 
	 * @param file
	 *            �����ܵ��ļ�
	 * 
	 * @return �ļ����ܺ�� MD5 ֵ
	 * @throws IOException
	 */
	public static String md5(File file) throws IOException {
		FileInputStream is = new FileInputStream(file);
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			int n = 0;
			byte[] buffer = new byte[1024];
			do {
				n = is.read(buffer);
				if (n > 0) {
					md5.update(buffer, 0, n);
				}
			} while (n != -1);
			is.skip(0);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			is.close();
		}

		byte[] encodedValue = md5.digest();

		int j = encodedValue.length;
		char finalValue[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte encoded = encodedValue[i];
			finalValue[k++] = hexDigits[encoded >> 4 & 0xf];
			finalValue[k++] = hexDigits[encoded & 0xf];
		}

		return new String(finalValue);
	}

	/**
	 * ��ȡ GMT ��ʽʱ���
	 * 
	 * @return GMT ��ʽʱ���
	 */
	private String getGMTDate() {
		SimpleDateFormat formater = new SimpleDateFormat(
				"EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		formater.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formater.format(new Date());
	}

	/**
	 * ����ǩ��
	 * 
	 * @param conn
	 *            ����
	 * @param uri
	 *            �����ַ
	 * @param length
	 *            ��������Body���ݳ���
	 * 
	 * @return ǩ���ַ���
	 */
	private String sign(HttpURLConnection conn, String uri, long length) {
		String sign = conn.getRequestMethod() + "&" + uri + "&"
				+ conn.getRequestProperty(DATE) + "&" + length + "&" + password;
		return "UpYun " + userName + ":" + md5(sign);
	}

	/**
	 * ���Ӵ����߼�
	 * 
	 * @param method
	 *            ����ʽ {GET, POST, PUT, DELETE}
	 * @param uri
	 *            �����ַ
	 * 
	 * @return ���������ַ������� null
	 */
	private String HttpAction(String method, String uri) {
		return HttpAction(method, uri, null, null, false);
	}

	/**
	 * ���Ӵ����߼�
	 * 
	 * @param method
	 *            ����ʽ {GET, POST, PUT, DELETE}
	 * @param uri
	 *            �����ַ
	 * @param datas
	 *            ���������跢�����ݣ���Ϊ null��
	 * @param outFile
	 *            �ļ�����������Ϊ null��
	 * @param auto
	 *            �Զ���������Ŀ¼(���10��)
	 * 
	 * @return ���������ַ������� null
	 */
	private String HttpAction(String method, String uri, byte[] datas,
			File outFile, boolean auto) {

		return HttpAction(method, uri, datas, outFile, auto, null);
	}
	
	
	/**
	 * rename filename
	 */
	
	public boolean renameFile()
	{
		boolean flag=false;
		
	    
		
		
		
		return flag;
	}
	
	
	

	/**
	 * ���Ӵ����߼�
	 * 
	 * @param method
	 *            ����ʽ {GET, POST, PUT, DELETE}
	 * @param uri
	 *            �����ַ
	 * @param datas
	 *            ���������跢�����ݣ���Ϊ null��
	 * @param outFile
	 *            �ļ�����������Ϊ null��
	 * @param auto
	 *            �Զ���������Ŀ¼(���10��)
	 * @param params
	 *            �������
	 * 
	 * @return ���������ַ������� null
	 */
	private String HttpAction(String method, String uri, byte[] datas,
			File outFile, boolean auto, Map<String, String> params) {

		String result = null;

		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;

		try {
			// ��ȡ����
			URL url = new URL("http://" + apiDomain + uri);
			conn = (HttpURLConnection) url.openConnection();
			System.out.println("1");

			// ���ñ�Ҫ����
			conn.setConnectTimeout(timeout);
			conn.setRequestMethod(method);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			System.out.println("2");

			// ����ʱ��
			conn.setRequestProperty(DATE, getGMTDate());

			// �Ƿ��Զ���������Ŀ¼
			System.out.println("3");
			if (auto) {
				conn.setRequestProperty(MKDIR, "true");
			}
			
			System.out.println("4");

			long contentLength = 0;
			if (datas == null)
				conn.setRequestProperty(CONTENT_LENGTH, "0");
			else {
				contentLength = datas.length;
				conn.setRequestProperty(CONTENT_LENGTH,
						String.valueOf(datas.length));
				System.out.println("5");

				// �����ļ��� MD5 ����
				if (!isEmpty(this.contentMD5)) {
					conn.setRequestProperty(CONTENT_MD5, this.contentMD5);
					this.contentMD5 = null;
				}
				System.out.println("6");
				// �����ļ��ķ�����Կ
				if (!isEmpty(this.fileSecret)) {
					conn.setRequestProperty(CONTENT_SECRET, this.fileSecret);
					this.fileSecret = null;
				}
				System.out.println("7");
			}

			// ����ǩ��
			conn.setRequestProperty(AUTHORIZATION,
					sign(conn, uri, contentLength));
			System.out.println("8");

			// �Ƿ��Ǵ����ļ�Ŀ¼
			boolean isFolder = false;

			// ���ö���Ĳ�������ͼƬ����ͼ��
			if (params != null && !params.isEmpty()) {

				isFolder = !isEmpty(params.get(PARAMS.KEY_MAKE_DIR.getValue()));

				for (Map.Entry<String, String> param : params.entrySet()) {
					conn.setRequestProperty(param.getKey(), param.getValue());
				}
			}
			
			System.out.println("9");
		

			// ��������
			conn.connect();
			
			System.out.println("10");

			if (datas != null) {
				os = conn.getOutputStream();
				os.write(datas);
				System.out.println("jjjjj----------------"+datas.toString().getBytes());
				os.flush();
			}

			if (isFolder) {
				os = conn.getOutputStream();
				os.flush();
			}
            System.out.println("doajhdfjs0---------------------------");
			if (outFile == null) {

				result = getText(conn, METHOD_HEAD.equals(method));

			} else {
				result = "";

				os = new FileOutputStream(outFile);
				byte[] data = new byte[4096];
				int temp = 0;

				is = conn.getInputStream();

				while ((temp = is.read(data)) != -1) {
					os.write(data, 0, temp);
				}
			}
		} catch (IOException e) {
			if (debug)
				e.printStackTrace();

			// ����ʧ��
			return null;

		} finally {

			try {
				if (os != null) {
					os.close();
					os = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}

		return result;
	}
	
	
	

	/**
	 * �����������ķ�������
	 * 
	 * @param conn
	 * 
	 * @return �ַ���
	 */
	private String getText(HttpURLConnection conn, boolean isHeadMethod)
			throws IOException {

		StringBuilder text = new StringBuilder();
		fileType = null;

		InputStream is = null;
		InputStreamReader sr = null;
		BufferedReader br = null;

		int code = conn.getResponseCode();
		System.out.println("ssss----"+code);

		try {
			is = code >= 400 ? conn.getErrorStream() : conn.getInputStream();

			if (!isHeadMethod) {
				sr = new InputStreamReader(is);
				br = new BufferedReader(sr);

				char[] chars = new char[4096];
				int length = 0;

				while ((length = br.read(chars)) != -1) {
					text.append(chars, 0, length);
				}
			}
			if (200 == code && conn.getHeaderField(X_UPYUN_WIDTH) != null) {
				picWidth = conn.getHeaderField(X_UPYUN_WIDTH);
				picHeight = conn.getHeaderField(X_UPYUN_HEIGHT);
				picFrames = conn.getHeaderField(X_UPYUN_FRAMES);
				picType = conn.getHeaderField(X_UPYUN_FILE_TYPE);
			} else {
				picWidth = picHeight = picFrames = picType = null;
			}

			if (200 == code && conn.getHeaderField(X_UPYUN_FILE_TYPE) != null) {
				fileType = conn.getHeaderField(X_UPYUN_FILE_TYPE);
				fileSize = conn.getHeaderField(X_UPYUN_FILE_SIZE);
				fileDate = conn.getHeaderField(X_UPYUN_FILE_DATE);
			} else {
				fileType = fileSize = fileDate = null;
			}
		} finally {
			if (br != null) {
				br.close();
				br = null;
			}
			if (sr != null) {
				sr.close();
				sr = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
		}

		if (isHeadMethod) {
			if (code >= 400)
				return null;
			return "";
		}

		if (code >= 400)
			throw new IOException(text.toString());

		return text.toString();
	}

	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 * 
	 * @param str
	 * @return �Ƿ�Ϊ��
	 */
	private boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * ��ʽ��·��������ȥ��ǰ��Ŀո�ȷ����"/"��ͷ��������"/�ռ���"
	 * <p>
	 * ���չ��ɵĸ�ʽ��"/�ռ���/�ļ�·��"
	 * 
	 * @param path
	 *            Ŀ¼·�����ļ�·��
	 * @return ��ʽ�����·��
	 */
	private String formatPath(String path) {

		if (!isEmpty(path)) {

			// ȥ��ǰ��Ŀո�
			path = path.trim();

			// ȷ��·����"/"��ͷ
			if (!path.startsWith(SEPARATOR)) {
				return SEPARATOR + bucketName + SEPARATOR + path;
			}
		}

		return SEPARATOR + bucketName + path;
	}

	public class FolderItem {
		// �ļ���
		public String name;

		// �ļ����� {file, folder}
		public String type;

		// �ļ���С
		public long size;

		// �ļ�����
		public Date date;

		public FolderItem(String data) {
			String[] a = data.split("\t");
			if (a.length == 4) {
				this.name = a[0];
				this.type = ("N".equals(a[1]) ? "File" : "Folder");
				try {
					this.size = Long.parseLong(a[2].trim());
				} catch (NumberFormatException e) {
					this.size = -1;
				}
				long da = 0;
				try {
					da = Long.parseLong(a[3].trim());
				} catch (NumberFormatException e) {
				}
				this.date = new Date(da * 1000);
			}
		}

		@Override
		public String toString() {
			return "time = " + date + "  size = " + size + "  type = " + type
					+ "  name = " + name;
		}
	}

	/**
	 * ������������ļ�ֵ�Ͳ���ֵ
	 */
	public enum PARAMS {

		/**
		 * ����ͼ����
		 * <p>
		 * ʹ�ó������ϴ�ͼƬʱ�����豣��ԭͼ����ֻ��ĳ���ض���С������ͼ������˵�û�ͷ��
		 * <p>
		 * ˵�����ò���������� KEY_X_GMKERL_VALUE ʹ�ã�������Ч�����⣬ʹ�øò����󽫲�����ԭͼ���мɡ�
		 * <p>
		 * ��ѡ������<br>
		 * 1)VALUE_FIX_MAX("fix_max")��"�޶���ߣ��̱�����Ӧ"<br>
		 * 2)VALUE_FIX_MIN("fix_min")��"�޶���̱ߣ���������Ӧ"<br>
		 * 3)VALUE_FIX_WIDTH_OR_HEIGHT("fix_width_or_height")��"�޶���Ⱥ͸߶�"<br>
		 * 4)VALUE_FIX_WIDTH("fix_width")��"�޶���ȣ��߶�����Ӧ"<br>
		 * 5)VALUE_FIX_HEIGHT("fix_height")��"�޶��߶ȣ��������Ӧ"<br>
		 * 6)VALUE_FIX_BOTH("fix_both")��"�̶���Ⱥ͸߶�"<br>
		 * 7)VALUE_FIX_SCALE("fix_scale")��"�ȱ�������"<br>
		 * 8)VALUE_SQUARE("square")��"����ͼ���̶��߹̶���"<br>
		 * 
		 * @see ����������http://wiki.upyun.com/index.php?title=����ͼ��ʽ������
		 */
		KEY_X_GMKERL_TYPE("x-gmkerl-type"),

		/**
		 * ����ͼ����ֵ
		 * <p>
		 * ˵�����ò���������� KEY_X_GMKERL_TYPE ʹ�ã�������Ч�������ֵ��Ҫ���� KEY_X_GMKERL_TYPE ������
		 */
		KEY_X_GMKERL_VALUE("x-gmkerl-value"),

		/**
		 * ����ͼ������ͼƬѹ��������Ĭ�� 95
		 * <p>
		 * ʹ�ó������û��ϴ��߱���ͼƬ��������ҵ��������̫��������ͼƬʱ���������øò��������ļ�����Ĵ�С���Ӷ����ٿռ��ʹ������
		 * <p>
		 * ˵����ʹ�øò����󽫲�����ԭͼ���мɡ�
		 */
		KEY_X_GMKERL_QUALITY("x-gmkerl-quality"),

		/**
		 * ͼƬ�񻯣�Ĭ���񻯣�true��
		 * <p>
		 * ʹ�ó�����ͼƬ���������̫�����ʹ�øò���ģ����Ե�����ͼƬ�������Ȼ��߽���̶ȣ�ʹͼƬ�ض������ɫ�ʸ���������
		 * <p>
		 * ˵�����񻯲������ܵģ�������ʹͼƬ����ʵ�����⣬Ҳ�޷�ͨ���񻯴ﵽԭͼ��Ч����
		 */
		KEY_X_GMKERL_UNSHARP("x-gmkerl-unsharp"),

		/**
		 * ����ͼ�汾
		 * <p>
		 * ʹ�ó��������ٴ���ԭͼ�������Զ��������ͼ��
		 * <p>
		 * ˵����ʹ�øò���ǰ��Ҫ����������ͼ�汾�ţ����⣬ʹ�øò����󽫲�����ԭͼ���мɡ�
		 * 
		 * @see http://wiki.upyun.com/index.php?title=��δ����Զ�������ͼ
		 */
		KEY_X_GMKERL_THUMBNAIL("x-gmkerl-thumbnail"),

		/**
		 * ͼƬ��ת
		 * <p>
		 * ʹ�ó��������ϴ���ͼƬ������б�ģ�ʹ�øò�������ֱ�ӽ���ǿ�ƵĻ��Զ��ķ�����
		 * <p>
		 * ˵����ֻ����"auto"��"90"��"180"��"270"���ֲ���������"auto"��������ͼƬ EXIF
		 * �е���Ϣ�����Զ���������ͼƬû�� EXIF ��Ϣ����ò�����Ч�����⣬ʹ�øò����󽫲�����ԭͼ���мɡ�
		 * 
		 * @see http://wiki.upyun.com/index.php?title=ͼƬ��ת
		 */
		KEY_X_GMKERL_ROTATE("x-gmkerl-rotate"),

		/**
		 * ͼƬ�ü�
		 * <p>
		 * ʹ�ó�����ֻ��Ҫ������ϴ�ͼƬ��ĳһ�����֣������û��ϴ�ͷ��ͼƬ���вü���
		 * <p>
		 * ˵����������ʽΪx,y,width,height������Ҫ���� x >= 0 && y >=0 && width > 0 && height
		 * > 0
		 * 
		 * @see http://wiki.upyun.com/index.php?title=ͼƬ�ü�
		 */
		KEY_X_GMKERL_CROP("x-gmkerl-crop"),

		/**
		 * �Ƿ���exif��Ϣ
		 * <p>
		 * ʹ�ó���������ԭͼ����EXIF��Ϣ�����ϴ�ͼƬʱ�ֽ����ˡ��ƻ��Դ���������ü������ԡ��Զ���汾�ȣ���
		 * upyunĬ�ϻ�ɾ��ԭͼ��EXIF��Ϣ�� ��ʱ����ò������Ա���ԭͼ��EXIF��Ϣ����������Ӧ�ô�����ͼ�л�ȡ����ĵ�����Ϣ��
		 * <p>
		 * ˵����������"�ƻ��Դ���"�Ĳ���ʹ��ʱ��Ч�������������Ч������key��Ӧ��ֵ������Ϊ"true"ʱ��Ч��
		 */
		KEY_X_GMKERL_EXIF_SWITCH("x-gmkerl-exif-switch"),

		/**
		 * ����Ŀ¼
		 * <p>
		 * ˵����SDK�ڲ�ʹ��
		 */
		KEY_MAKE_DIR("folder"),

		/**
		 * ����ͼ����֮ "�޶���ߣ��̱�����Ӧ"������Ϊ����ֵ����: 150
		 */
		VALUE_FIX_MAX("fix_max"),
		/**
		 * ����ͼ����֮ "�޶���̱ߣ���������Ӧ"������Ϊ����ֵ����: 150
		 */
		VALUE_FIX_MIN("fix_min"),
		/**
		 * ����ͼ����֮ "�޶���Ⱥ͸߶�"������Ϊ����ֵ����: 150x130
		 */
		VALUE_FIX_WIDTH_OR_HEIGHT("fix_width_or_height"),
		/**
		 * ����ͼ����֮ "�޶���ȣ��߶�����Ӧ"������Ϊ����ֵ����: 150
		 */
		VALUE_FIX_WIDTH("fix_width"),
		/**
		 * ����ͼ����֮ "�޶��߶ȣ��������Ӧ"������Ϊ����ֵ����: 150
		 */
		VALUE_FIX_HEIGHT("fix_height"),
		/**
		 * ����ͼ����֮ "����ͼ���̶��߹̶���"������Ϊ����ֵ����: 150
		 */
		VALUE_SQUARE("square"),
		/**
		 * ����ͼ����֮ "�̶���Ⱥ͸߶�"������Ϊ����ֵ����: 150x130
		 */
		VALUE_FIX_BOTH("fix_both"),
		/**
		 * ����ͼ����֮ "�ȱ�������"������Ϊ����ֵ��1-99������: 50
		 */
		VALUE_FIX_SCALE("fix_scale"),

		/**
		 * ͼƬ��ת֮ "�Զ�����"
		 */
		VALUE_ROTATE_AUTO("auto"),
		/**
		 * ͼƬ��ת֮ "��ת90��"
		 */
		VALUE_ROTATE_90("90"),
		/**
		 * ͼƬ��ת֮ "��ת180��"
		 */
		VALUE_ROTATE_180("180"),
		/**
		 * ͼƬ��ת֮ "��ת270��"
		 */
		VALUE_ROTATE_270("270");

		private final String value;

		private PARAMS(String val) {
			value = val;
		}

		public String getValue() {
			return value;
		}
	}
}
