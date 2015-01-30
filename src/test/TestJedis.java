package test;

import redis.clients.jedis.Jedis;

public class TestJedis
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// 连接
				Jedis jedis = new Jedis("localhost", 6379);
				
				// 对字符串的操作

				// 给名称为apple的key赋值
				jedis.set("apple", "www.apple.com");
				// 返回名称为apple的key的value
				// System.out.println(jedis.get("apple"));

				// 将value追加到key原来值的末尾
				// System.out.println(jedis.append("db", "mysql"));
				// System.out.println(jedis.get("db"));

				// 若key不存在，即把key设为value
				// System.out.println(jedis.exists("myphone"));
				// System.out.println(jedis.append("myphone", "nokia"));

				// 对list操作

				// 将student1插入到teacher列表的表头
				// System.out.println(jedis.lpush("teacher", "student1"));
				// 将student2插入到teacher列表的表头
				// System.out.println(jedis.lpush("teacher","student2"));
				// 返回teacher列表的长度
				// System.out.println(jedis.llen("teacher"));
				// 返回teacher列表中的元素,下表从0开始
				// System.out.println(jedis.lrange("teacher", 0, -1));

				// java命令遍历
				/*
				 * List<String> list=jedis.lrange("teacher", 0, -1); for (Iterator
				 * iterator = list.iterator(); iterator.hasNext();) { String string =
				 * (String) iterator.next(); System.out.println(string); }
				 */

				// set操作

				// 向名称为dept的set中添加元素,成功返回1，失败返回0,无序
				System.out.println(jedis.sadd("dept", "张三"));
				System.out.println(jedis.sadd("dept", "老马"));
				jedis.sadd("dept", "老马2");
				jedis.sadd("dept", "老马3");
				jedis.sadd("dept", "老马4");
				jedis.sadd("dept", "老马5");
				jedis.sadd("dept", "老马6");
				// 返回dept的所有元素
				// System.out.println(jedis.smembers("dept"));
				// 遍历

				/*
				 * Set<String> set=jedis.smembers("dept"); for (Iterator iterator =
				 * set.iterator(); iterator.hasNext();) { String string = (String)
				 * iterator.next(); System.out.println(string); }
				 */
				// 返回dept中的元素个数
				System.out.println(jedis.scard("dept"));
				// 删除【老马】这个元素
				System.out.println("删除某个元素" + jedis.srem("dept", "老马"));
				System.out.println(jedis.smembers("dept"));

				// 有序集（个人理解，有序集，就是有序的set集合）

				/*
				 * //向class中添加元素，100用于排序 System.out.println(jedis.zadd("class", 100,
				 * "老马")); System.out.println(jedis.zadd("class", 100, "老马dd"));
				 * //若"老马"元素存在，则用88替换以前的数据 jedis.zadd("class", 88, "老马");
				 * //返回"老马"元素的scope值 System.out.println(jedis.zscore("class","老马"));
				 * System.out.println(jedis.zadd("class", 12, "老张"));
				 * //显示class中所有的元素(元素按scope从小到大排列)
				 * System.out.println(jedis.zrange("class", 0, -1)); //返回class中元素的个数
				 * System.out.println(jedis.zcard("class")); jedis.zrem("class",
				 * "老马dd"); System.out.println(jedis.zrange("class", 0, -1));
				 * //显示class中所有的元素(元素按scope从大到小排列)
				 * System.out.println(jedis.zrevrange("class", 0, -1));
				 */
        
        
	}
}
