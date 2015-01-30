package test;

import redis.clients.jedis.Jedis;

public class TestJedis
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// ����
				Jedis jedis = new Jedis("localhost", 6379);
				
				// ���ַ����Ĳ���

				// ������Ϊapple��key��ֵ
				jedis.set("apple", "www.apple.com");
				// ��������Ϊapple��key��value
				// System.out.println(jedis.get("apple"));

				// ��value׷�ӵ�keyԭ��ֵ��ĩβ
				// System.out.println(jedis.append("db", "mysql"));
				// System.out.println(jedis.get("db"));

				// ��key�����ڣ�����key��Ϊvalue
				// System.out.println(jedis.exists("myphone"));
				// System.out.println(jedis.append("myphone", "nokia"));

				// ��list����

				// ��student1���뵽teacher�б�ı�ͷ
				// System.out.println(jedis.lpush("teacher", "student1"));
				// ��student2���뵽teacher�б�ı�ͷ
				// System.out.println(jedis.lpush("teacher","student2"));
				// ����teacher�б�ĳ���
				// System.out.println(jedis.llen("teacher"));
				// ����teacher�б��е�Ԫ��,�±��0��ʼ
				// System.out.println(jedis.lrange("teacher", 0, -1));

				// java�������
				/*
				 * List<String> list=jedis.lrange("teacher", 0, -1); for (Iterator
				 * iterator = list.iterator(); iterator.hasNext();) { String string =
				 * (String) iterator.next(); System.out.println(string); }
				 */

				// set����

				// ������Ϊdept��set�����Ԫ��,�ɹ�����1��ʧ�ܷ���0,����
				System.out.println(jedis.sadd("dept", "����"));
				System.out.println(jedis.sadd("dept", "����"));
				jedis.sadd("dept", "����2");
				jedis.sadd("dept", "����3");
				jedis.sadd("dept", "����4");
				jedis.sadd("dept", "����5");
				jedis.sadd("dept", "����6");
				// ����dept������Ԫ��
				// System.out.println(jedis.smembers("dept"));
				// ����

				/*
				 * Set<String> set=jedis.smembers("dept"); for (Iterator iterator =
				 * set.iterator(); iterator.hasNext();) { String string = (String)
				 * iterator.next(); System.out.println(string); }
				 */
				// ����dept�е�Ԫ�ظ���
				System.out.println(jedis.scard("dept"));
				// ɾ�����������Ԫ��
				System.out.println("ɾ��ĳ��Ԫ��" + jedis.srem("dept", "����"));
				System.out.println(jedis.smembers("dept"));

				// ���򼯣�������⣬���򼯣����������set���ϣ�

				/*
				 * //��class�����Ԫ�أ�100�������� System.out.println(jedis.zadd("class", 100,
				 * "����")); System.out.println(jedis.zadd("class", 100, "����dd"));
				 * //��"����"Ԫ�ش��ڣ�����88�滻��ǰ������ jedis.zadd("class", 88, "����");
				 * //����"����"Ԫ�ص�scopeֵ System.out.println(jedis.zscore("class","����"));
				 * System.out.println(jedis.zadd("class", 12, "����"));
				 * //��ʾclass�����е�Ԫ��(Ԫ�ذ�scope��С��������)
				 * System.out.println(jedis.zrange("class", 0, -1)); //����class��Ԫ�صĸ���
				 * System.out.println(jedis.zcard("class")); jedis.zrem("class",
				 * "����dd"); System.out.println(jedis.zrange("class", 0, -1));
				 * //��ʾclass�����е�Ԫ��(Ԫ�ذ�scope�Ӵ�С����)
				 * System.out.println(jedis.zrevrange("class", 0, -1));
				 */
        
        
	}
}
