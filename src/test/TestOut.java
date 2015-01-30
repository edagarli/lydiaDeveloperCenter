package test;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.bae.api.util.BaeEnv;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class TestOut extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doGet(req, resp);
		
		resp.setContentType("text/plain");
		resp.getWriter().println("test java bae version 1 of app ");
		resp.getWriter().println("update 0");
		resp.getWriter().println("update 0");
		
		resp.getWriter().println("update 0");
		resp.getWriter().println("update 0");
		resp.getWriter().println("update 0");
		
			//String server = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_MONGO_IP);	
		String server= "mongo.duapp.com";
			resp.getWriter().println("update 11");
			
			String port = "8908";//BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_MONGO_PORT);
			String host = server + ":" + port;
			String user = "EBgGEPmR2TtSkoc0A7Cd9evF";//BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
			String password ="tbVfZ7e3DhTSZsr4tVq6TM05no9PG5q0";// BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
			String databaseName = "MfMIHorUMvXgCefqisVr";		
			MongoClient mongoClient = new MongoClient(new ServerAddress(host),
	                        Arrays.asList(MongoCredential.createMongoCRCredential(user, databaseName, password.toCharArray())),
	                        new MongoClientOptions.Builder().cursorFinalizerEnabled(false).build());
			DB testDB = mongoClient.getDB(databaseName);
			testDB.authenticate(user, password.toCharArray());
			String tableName = "apps";
			resp.getWriter().println(testDB.getCollection(tableName).find());
			resp.getWriter().println(testDB.getCollection(tableName).count());
			resp.getWriter().println(testDB.getCollection(tableName).insert(new BasicDBObject()));
			resp.getWriter().println(testDB.getCollection(tableName).getStats());
			resp.getWriter().println("BAE MongoDB run ok!");
	}
}
