package md5;

import java.security.MessageDigest;

/**
 * MD5加密�?
 * 
 * @author Cream
 * 
 */
public class Md5Util
{

	/**
	 * MD5加密方法 终�?
	 * 
	 * @param mess
	 *            �?��密字符串
	 * @return
	 */
	public final static String md5(String mess)
	{
		try
		{
			byte[] btInput = mess.getBytes();
			// 把字符串按MD5加密
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			// 把加密后的字符转换成16进制字符
			for (int i = 0; i < md.length; i++)
			{
				int val = ((int) md[i]) & 0xff;
				if (val < 16) sb.append("0");
				sb.append(Integer.toHexString(val));

			}
			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String args[]){
		
	}
}
