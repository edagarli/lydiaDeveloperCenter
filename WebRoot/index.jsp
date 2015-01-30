<%@ page language="java"
	import="java.util.*,java.net.URL,com.mongodb.*,com.baidu.bae.api.util.BaeEnv,java.net.*"
	pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>Hello World</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	<%
			String server = "mongo.duapp.com";
		// String port = "27017";
		String port = "8908";
			String host = server + ":" + port;
			String user = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
			String password = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
		String databaseName = "QeQYNsEGXuEEMBeWudwq";
		MongoClient mongoClient = new MongoClient(new ServerAddress(host),
				Arrays.asList(MongoCredential.createMongoCRCredential(user,
						databaseName, password.toCharArray())),
				new MongoClientOptions.Builder().cursorFinalizerEnabled(
						false).build());
		DB testDB = mongoClient.getDB(databaseName);
		testDB.authenticate(user, password.toCharArray());
		String tableName = "apps";
		out.println(testDB.getCollection(tableName).find());
		out.println(testDB.getCollection(tableName).count());
		out.println(testDB.getCollection(tableName).insert(
				new BasicDBObject()));
		out.println(testDB.getCollection(tableName).getStats());
		out.println("BAE MongoDB run ok!");
	%>
</body>
</html>

