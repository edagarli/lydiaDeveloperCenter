package com.lydia;

import java.net.UnknownHostException;
import java.util.Arrays;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class SysListener implements ServletContextListener 
{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	   //String server = "localhost";
		String server = "mongo.duapp.com";
	   // String port = "27017";
		String port = "8908";
		String host = server + ":" + port;
//		String user = "lizhi";
	    String user = "EBgGEPmR2TtSkoc0A7Cd9evF";
	//	String password = "016211";
		String password = "tbVfZ7e3DhTSZsr4tVq6TM05no9PG5q0";
		String databaseName = "MfMIHorUMvXgCefqisVr";
	//	String databaseName="lydia";
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(new ServerAddress(host),
					Arrays.asList(MongoCredential.createMongoCRCredential(user,
							databaseName, password.toCharArray())),
					new MongoClientOptions.Builder().cursorFinalizerEnabled(
							false).build());
			DB testDB = mongoClient.getDB(databaseName);
			testDB.authenticate(user, password.toCharArray());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
