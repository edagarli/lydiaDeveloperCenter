package com.lydia.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferedReaderFile {
 
	public static String getResult(String url) {
		 
		BufferedReader br = null;
		
		StringBuffer sb=new StringBuffer();
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(url));
 
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				sb.append(sCurrentLine);
			}
			System.out.println("ssss==="+sb);
			
			return sb.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public static void main(String args[]){
		getResult("http://cdn.lydiabox.com/apps/lydiagapdemo/manifest.webapp");
	}
}