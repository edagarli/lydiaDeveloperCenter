package com.lydia.util;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SyncHttp {

	private static final Log LOG = LogFactory.getLog(SyncHttp.class);
	private static final int CONNECTION_TIME = 1000 * 30;

	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public String httpGet(String url, String params) {

		String response = "";
		if (!params.equals("") && params != null) {
			url = url + "?" + params;
		}
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,
				CONNECTION_TIME);
		try {
			int statusCode = client.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream in = getMethod.getResponseBodyAsStream();
				response = getData(in);
			} else {
				LOG.debug("Get Method StatusCode:" + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
			client = null;
		}
		return response;
	}

	public String httpPost(String url, List<Parameter> params) throws Exception {
		String response = "";
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.addParameter("Connection", "Keep-Alive");
		postMethod.addParameter("Charset", "UTF-8");
		postMethod.addParameter("Content-Type",
				"application/x-www-form-urlencoded");
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,
				CONNECTION_TIME);
		if (!params.equals("") && params != null) {
			postMethod.setRequestBody(buildNameValuePair(params));
		}
		int statusCode = client.executeMethod(postMethod);
		if (statusCode == HttpStatus.SC_OK) {
			InputStream in = postMethod.getResponseBodyAsStream();
			response = this.getData(in);
		} else {
			LOG.debug("Post Method StatusCode:" + statusCode);
		}
		postMethod.releaseConnection();
		client = null;
		return response;
	}

	/**
	 * ??HttpPost?Name-Value???
	 * 
	 * @param list
	 * @return
	 */
	private NameValuePair[] buildNameValuePair(List<Parameter> list) {
		int length = list.size();
		NameValuePair[] pais = new NameValuePair[length];
		for (int i = 0; i < length; i++) {
			Parameter param = list.get(i);
			System.out.println("===="+param.getName()+"====="+param.getValue());
			pais[i] = new NameValuePair(param.getName(), param.getValue());
		}
		return pais;
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private String getData(InputStream in) throws Exception {
		StringBuffer sb = new StringBuffer();
		int len = -1;
		byte[] bytes = new byte[1024];

		while ((len = in.read(bytes)) != -1) {
			sb.append(new String(bytes));
		}
		String data = sb.toString();
		return data;
	}
}