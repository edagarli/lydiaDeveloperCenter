package test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;


public class TestConnect
{
      public static void main(String args[])
      {
    	  Jedis jedis=new Jedis("server.jrooy.com",6379);
    	  jedis.connect();
    	  System.out.println(jedis.select(7));
    	  
    	  Set keys = jedis.keys("*");//列出所有的key，查找特定的key如：redis.keys("foo")   
          Iterator t1=keys.iterator() ;   
          while(t1.hasNext())
          {   
              Object obj1=t1.next();   
              System.out.println(obj1);
              System.out.println("==uuid=="+jedis.hkeys(obj1.toString()).size());
          }   
      }
}
