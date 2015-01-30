package com.lydia.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PageSpider implements Runnable {

	HttpURLConnection httpUrlConnection;
	InputStream inputStream;
	BufferedReader bufferedReader;
	public String url;
	
	public String data="";

	public PageSpider(String url) {

	//	try {
		//	url = "http://cdn.lydiabox.com/apps/lydiagapdemo/manifest.webapp";
		//} catch (Exception e) {
		//	e.printStackTrace();
	//	}
		try {
			httpUrlConnection = (HttpURLConnection) new URL(url)
					.openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("---------start-----------");

		Thread thread = new Thread(this);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("----------end------------");
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		try {
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			inputStream = httpUrlConnection.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			String string;
			while ((string = bufferedReader.readLine()) != null) {
				//System.out.println(new String(string.getBytes(), "utf-8"));
				data+=new String(string.getBytes(), "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
				inputStream.close();
				httpUrlConnection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
//		new PageSpider();
	}
}