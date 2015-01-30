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

	private String host = "smtp.qq.com"; // �ʼ�����������
	private String from = "no-reply@lydiabox.com"; // ������
	private String to = "root@pwhack.me"; // �ռ���
	private String subject = "Lydia Web App Store��Ҫ��֤�����ʼ�"; // �ʼ�����
	private String content = "hello! i am lizhi!"; // �ʼ�����
	private String userName = "no-reply@lydiabox.com"; // �û���
	private String password = "justhackerlizhi"; // ����

	public static void main(String[] args) throws Exception
	{
		new EmailSender().sendMail("www19940501@126.com");
	}

	/**
	 * ��������
	 * 
	 * @param content
	 *            �ʼ�����
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * �����ռ���
	 * 
	 * @param to
	 *            �ռ��˵�ַ
	 */
	public void setTo(String to)
	{
		this.to = to;
	}

	/**
	 * 
	 * param n ȡ��������ĸ���
	 * 
	 * param begin ����������Ŀ�ʼ��
	 * 
	 * param end ������������յ�
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
	 * ����email
	 * 
	 * @throws MessagingException
	 * @throws Exception
	 */
	public String sendMail(String emailBox) throws MessagingException, Exception
	{

		Properties props = new Properties();
		props.put("mail.smtp.host", host); // ָ��SMTP������
		props.put("mail.smtp.auth", "true"); // ָ���Ƿ���ҪSMTP��֤

		Session mailSession = Session.getDefaultInstance(props);

		MimeMessage message = new MimeMessage(mailSession);
		MimeMultipart mp = new MimeMultipart();
		
		message.setFrom(new InternetAddress(from)); // ������ 
		
		to=emailBox;
				
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // �ռ���

		message.setSubject(subject,"utf-8"); // �ʼ�����

		content = String.valueOf(generateDifNums(1,1000,9999).toString());

	    // ��������
	    BodyPart body = new MimeBodyPart();
	    body.setContent("��֤��Ϊ:"+content+" [Lydia Web App Store]", "text/plain;charset=utf-8"); // ������ע�����ñ���
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