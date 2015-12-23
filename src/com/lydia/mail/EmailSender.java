package com.lydia.mail;

import java.util.Properties;
import java.util.Random;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender
{

	private String host = "smtp.qq.com"; // 邮件主机服务器
	private String from = "no-reply@lydiabox.com"; // 发件人
	private String to = "root@pwhack.me"; // 收件人
	private String subject = "Lydia Web App Store需要验证您的邮件"; // 邮件标题
	private String content = "hello! i am lizhi!"; // 邮件内容
	private String userName = "no-reply@lydiabox.com"; // 用户名
	private String password = "*******"; // 密码

	public static void main(String[] args) throws Exception
	{
		new EmailSender().sendMail("www19940501@126.com");
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            邮件内容
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * 设置收件人
	 * 
	 * @param to
	 *            收件人地址
	 */
	public void setTo(String to)
	{
		this.to = to;
	}

	/**
	 * 
	 * param n 取出随机数的个数
	 * 
	 * param begin 随机数产生的开始数
	 * 
	 * param end 随机数产生的终点
	 **/

	public static String generateDifNums(int n, int begin, int end)
	{

		int length = end - begin + 1;

		int[] seed = new int[length];

		for (int i = 0; i < length; i++)
		{

			seed[i] = begin + i;

		}

		int[] ranArr = new int[n];

		Random ran = new Random();

		for (int i = 0; i < n; i++)
		{

			int j = ran.nextInt(length - i);

			ranArr[i] = seed[j];

			seed[j] = seed[length - 1 - i];

		}
		
		String s="";
		
		for(int i=0;i<n;i++)
		{
            s+=String.valueOf(ranArr[i]);			
		}
		
		return s;
	}

	/**
	 * 发送email
	 * 
	 * @throws MessagingException
	 * @throws Exception
	 */
	public String sendMail(String emailBox) throws MessagingException, Exception
	{

		Properties props = new Properties();
		props.put("mail.smtp.host", host); // 指定SMTP服务器
		props.put("mail.smtp.auth", "true"); // 指定是否需要SMTP验证

		Session mailSession = Session.getDefaultInstance(props);

		MimeMessage message = new MimeMessage(mailSession);
		MimeMultipart mp = new MimeMultipart();
		
		message.setFrom(new InternetAddress(from)); // 发件人 
		
		to=emailBox;
				
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 收件人

		message.setSubject(subject,"utf-8"); // 邮件主题

		content = String.valueOf(generateDifNums(1,1000,9999).toString());

	    // 设置正文
	    BodyPart body = new MimeBodyPart();
	    body.setContent("验证码为:"+content+" [Lydia Web App Store]", "text/plain;charset=utf-8"); // ！！！注意设置编码
	    mp.addBodyPart(body);
	    message.setContent(mp);
		
		 message.saveChanges();

		Transport transport = null;
		transport = mailSession.getTransport("smtp");
		transport.connect(host, userName, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		
		return content;
	}

}
