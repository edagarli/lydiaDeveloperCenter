package com.lydia.util;

import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisUtil
{
	  public static Jedis jedis=null;
	
      public static Set getJedisSet()
      {
    	  
    	  jedis=new Jedis("server.jrooy.com",6379);
    	  jedis.connect();
    	  System.out.println(jedis.select(7));
    	  Set keys = jedis.keys("*");//�г����е�key�������ض���key�磺redis.keys("foo")   
    	  return keys;
      }
}
