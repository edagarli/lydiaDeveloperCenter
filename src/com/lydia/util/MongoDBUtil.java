package com.lydia.util;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBUtil
{
	public static DBCollection getTestCollection(String collectName)
	{
		DBCollection collection = null;

		// String server = "localhost";
		String server = "10.171.195.69";
		// String port = "27017";
		String port = "27017";
		String host = server + ":" + port;
	    String user = "lizhi";
		//String user = "EBgGEPmR2TtSkoc0A7Cd9evF";
		String password = "lizhishishabi";
		//String password = "tbVfZ7e3DhTSZsr4tVq6TM05no9PG5q0";
		//String databaseName = "MfMIHorUMvXgCefqisVr";
		String databaseName="reviewDb";
		MongoClient mongoClient;
		try
		{
			mongoClient = new MongoClient(new ServerAddress(host),
					Arrays.asList(MongoCredential.createMongoCRCredential(user,
							databaseName, password.toCharArray())),
					new MongoClientOptions.Builder().cursorFinalizerEnabled(
							false).build());
			DB testDB = mongoClient.getDB(databaseName);
			testDB.authenticate(user, password.toCharArray());
			collection=testDB.getCollection(collectName);//少了这行代码，导致一直空指针
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return collection;
	}
	
	public static DBCollection getTestTwoCollection(String collectName)
	{
		DBCollection collection = null;

		// String server = "localhost";
		String server = "10.171.195.69";
		// String port = "27017";
		String port = "27017";
		String host = server + ":" + port;
	    String user = "lizhi";
		//String user = "EBgGEPmR2TtSkoc0A7Cd9evF";
		String password = "lizhishishabi";
		//String password = "tbVfZ7e3DhTSZsr4tVq6TM05no9PG5q0";
		//String databaseName = "MfMIHorUMvXgCefqisVr";
		String databaseName="reviewDb";
		MongoClient mongoClient;
		try
		{
			mongoClient = new MongoClient(new ServerAddress(host),
					Arrays.asList(MongoCredential.createMongoCRCredential(user,
							databaseName, password.toCharArray())),
					new MongoClientOptions.Builder().cursorFinalizerEnabled(
							false).build());
			DB testDB = mongoClient.getDB(databaseName);
			testDB.authenticate(user, password.toCharArray());
			collection=testDB.getCollection(collectName);//少了这行代码，导致一直空指针
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return collection;
	}
	
	public static DBCollection getCollection(String collectName)
	{
		DBCollection collection = null;
		try
		{
			String server = "localhost";
			// String server = "mongo.duapp.com";
			int port = 27017;
			// String port = "8908";
			String host = server + ":" + port;
			String user = "lizhi";
			// String user = "xjvCGNlaG0xFxyiapNrI536G";
			String password = "016211";
			// String password = "60qbKlWqEaHUewCTjpn1d8rFp5wTHnuq";
			// String databaseName = "MfMIHorUMvXgCefqisVr";
			String databaseName = "lydia";
			Mongo conn = new Mongo(server, port);// 建立数据库连接
			DB testDB = conn.getDB(databaseName);// 取得test数据库
			testDB.authenticate(user, password.toCharArray());
			//DB testDB = mongoClient.getDB(databaseName);
			collection = testDB.getCollection(collectName);
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// out.println(testDB.getCollection(tableName).find());
		return collection;
	}
}
