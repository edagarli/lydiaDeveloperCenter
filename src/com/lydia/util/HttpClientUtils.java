package com.lydia.util;
import  java.io.BufferedReader;   
import  java.io.IOException;   
import  java.io.InputStreamReader;   
import  java.util.Map;   
  
import  org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;   
import  org.apache.commons.httpclient.Header;   
import  org.apache.commons.httpclient.HttpClient;   
import  org.apache.commons.httpclient.HttpException;   
import  org.apache.commons.httpclient.HttpMethod;   
import  org.apache.commons.httpclient.HttpStatus;   
import  org.apache.commons.httpclient.methods.GetMethod;   
import  org.apache.commons.httpclient.methods.PostMethod;   
import  org.apache.commons.httpclient.params.HttpMethodParams;   
  
/**  
 * <p>  
 * HTTP������  
 *  �����:Commons-httpclient.jar,commons-codec-1.3.jar  
 * ѧϰ�μ���ַ: https://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/  
 * </p>  
 *   
 * @author tw 2009-07-16  
 *   
 */   
public   class  HttpClientUtils {   
       
       
     public   static   void  main(String arg[]) throws  Exception {   
        String url =  "https://www.99bill.com/webapp/receiveDrawbackAction.do" ;   
         //getDoGetURL2(url,"utf-8");//����ok   
         //getDoGetURL(url,"utf-8");//����ok   
        getDoPostResponseDataByURL(url,  null ,  "utf-8" ,  true );  //����ok   
    }   
  
     /**  
     * <p>httpClient��get����ʽ</p>  
     * @param url = "https://www.99bill.com/webapp/receiveDrawbackAction.do";  
     * @param charset = ="utf-8";  
     * @return  
     * @throws Exception  
     */   
     public   static  String getDoGetURL(String url, String charset) throws  Exception {   
  
        HttpClient client =  new  HttpClient();   
        GetMethod method1 =  new  GetMethod(url);   
  
         if  ( null  == url || !url.startsWith( "http" )) {   
             throw   new  Exception( "�����ַ��ʽ����" );   
        }   
  
         // ��������ı��뷽ʽ   
         if  ( null  != charset) {   
            method1.addRequestHeader( "Content-Type" ,   
                     "application/x-www-form-urlencoded; charset="  + charset);   
        }  else  {   
            method1.addRequestHeader( "Content-Type" ,   
                     "application/x-www-form-urlencoded; charset="  +  "utf-8" );   
        }   
         int  statusCode = client.executeMethod(method1);   
  
         if  (statusCode != HttpStatus.SC_OK) { // ��ӡ���������ص�״̬   
            System.out.println( "Method failed: "  + method1.getStatusLine());   
        }   
         // ������Ӧ��Ϣ   
         byte [] responseBody = method1.getResponseBodyAsString().getBytes(method1.getResponseCharSet());   
         // �ڷ�����Ӧ��Ϣʹ�ñ���(utf-8��gb2312)   
        String response =  new  String(responseBody,  "utf-8" );   
        System.out.println( "------------------response:" +response);   
         // �ͷ�����   
        method1.releaseConnection();   
         return  response;   
    }   
       
       
     /**  
     * <p>httpClient��get����ʽ2</p>  
     * @param url = "https://www.99bill.com/webapp/receiveDrawbackAction.do";  
     * @param charset = ="utf-8";  
     * @return  
     * @throws Exception  
     */   
     public   static  String getDoGetURL2(String url, String charset)   
             throws  Exception {   
         /*  
         * ʹ�� GetMethod ������һ�� URL ��Ӧ����ҳ,ʵ�ֲ���:   
         * 1:����һ�� HttpClinet ����������Ӧ�Ĳ�����  
         * 2:����һ�� GetMethod ����������Ӧ�Ĳ�����   
         * 3:�� HttpClinet ���ɵĶ�����ִ�� GetMethod ���ɵ�Get ������   
         * 4:������Ӧ״̬�롣   
         * 5:����Ӧ���������� HTTP ��Ӧ���ݡ�   
         * 6:�ͷ����ӡ�  
         */   
           
         /* 1 ���� HttpClinet �������ò��� */   
        HttpClient httpClient =  new  HttpClient();   
         // ���� Http ���ӳ�ʱΪ5��   
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout( 5000 );   
  
         /* 2 ���� GetMethod �������ò��� */   
        GetMethod getMethod =  new  GetMethod(url);   
         // ���� get ����ʱΪ 5 ��   
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,  5000 );   
         // �����������Դ����õ���Ĭ�ϵ����Դ�����������   
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new  DefaultHttpMethodRetryHandler());   
           
        String response = "" ;   
         /* 3 ִ�� HTTP GET ���� */   
         try  {   
             int  statusCode = httpClient.executeMethod(getMethod);   
             /* 4 �жϷ��ʵ�״̬�� */   
             if  (statusCode != HttpStatus.SC_OK) {   
                System.err.println( "Method failed: " + getMethod.getStatusLine());   
            }   
  
             /* 5 ���� HTTP ��Ӧ���� */   
             // HTTP��Ӧͷ����Ϣ������򵥴�ӡ   
            Header[] headers = getMethod.getResponseHeaders();   
             for  (Header h : headers)   
                System.out.println(h.getName() +  "------------ "  + h.getValue());   
               
             // ��ȡ HTTP ��Ӧ���ݣ�����򵥴�ӡ��ҳ����   
             byte [] responseBody = getMethod.getResponseBody(); // ��ȡΪ�ֽ�����   
            response =  new  String(responseBody, charset);   
            System.out.println( "----------response:" +response);   
               
             // ��ȡΪ InputStream������ҳ������������ʱ���Ƽ�ʹ��   
             //InputStream response = getMethod.getResponseBodyAsStream();   
               
        }  catch  (HttpException e) {   
             // �����������쳣��������Э�鲻�Ի��߷��ص�����������   
            System.out.println( "Please check your provided http address!" );   
            e.printStackTrace();   
        }  catch  (IOException e) {   
             // ���������쳣   
            e.printStackTrace();   
        }  finally  {   
             /* 6 .�ͷ����� */   
            getMethod.releaseConnection();   
        }   
         return  response;   
    }   
       
     /**   
     * <p>ִ��һ��HTTP POST���󣬷���������Ӧ��HTML</p>   
     *   
     * @param url       �����URL��ַ   
     * @param params    ����Ĳ�ѯ����,����Ϊnull   
     * @param charset   �ַ���   
     * @param pretty    �Ƿ�����   
     * @return          ����������Ӧ��HTML   
     */     
     public   static  String getDoPostResponseDataByURL(String url,   
            Map<String, String> params, String charset,  boolean  pretty) {   
           
        StringBuffer response =  new  StringBuffer();   
           
        HttpClient client =  new  HttpClient();   
        HttpMethod method =  new  PostMethod(url);   
           
         //����Http Post����    
         if  (params !=  null ) {   
            HttpMethodParams p =  new  HttpMethodParams();   
             for  (Map.Entry<String, String> entry : params.entrySet()) {   
                p.setParameter(entry.getKey(), entry.getValue());   
            }   
            method.setParams(p);   
        }   
         try  {   
            client.executeMethod(method);   
             if  (method.getStatusCode() == HttpStatus.SC_OK) {   
                 //��ȡΪ InputStream������ҳ������������ʱ���Ƽ�ʹ��   
                BufferedReader reader =  new  BufferedReader(   
                         new  InputStreamReader(method.getResponseBodyAsStream(),   
                                charset));   
                String line;   
                 while  ((line = reader.readLine()) !=  null ) {   
                     if  (pretty)   
                        response.append(line).append(System.getProperty( "line.separator" ));   
                     else   
                        response.append(line);   
                }   
                reader.close();   
            }   
        }  catch  (IOException e) {   
            System.out.println( "ִ��HTTP Post����"  + url +  "ʱ�������쳣��" );   
            e.printStackTrace();   
        }  finally  {   
            method.releaseConnection();   
        }   
        System.out.println( "--------------------" +response.toString());   
         return  response.toString();   
    }    
  
       
}  