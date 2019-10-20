package com.ack.common;

import com.alibaba.fastjson.JSONObject;
import com.ack.utils.PropertiesUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.*;

public class RedisPool {

	private static Log log = LogFactory.getLog(RedisPool.class);

	static ShardedJedisPool pool;
	static ShardedJedis jedis;
	private static JedisPoolConfig config;// Jedis客户端池配置

	static {
		List<JedisShardInfo> list = new LinkedList<>();
		String servers = PropertiesUtils.getProperty("redis_server");
		for (String server : servers.split(",")) {
			if (server.indexOf("@") > -1) {
				JedisShardInfo jedisShardInfo = new JedisShardInfo(server);
				list.add(jedisShardInfo);
			} else {
				String[] host = server.split(":", 2);
				JedisShardInfo jedisShardInfo = new JedisShardInfo(host[0], host[1]);
				list.add(jedisShardInfo);
			}

		}
		config = new JedisPoolConfig();
		config.setMaxTotal(60000);
		config.setMaxIdle(1000); // 对象最大空闲时间
		config.setMaxWaitMillis(10000);
		// 对获取的connection进行validateObject校验，默认false
		config.setTestOnBorrow(false);
		// 在进行returnObject对返回的connection进行validateObject校验，默认false
		config.setTestOnReturn(false);
		// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		config.setTimeBetweenEvictionRunsMillis(60000);
		// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		config.setMinEvictableIdleTimeMillis(120000);
		// 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		config.setNumTestsPerEvictionRun(-1);

		pool = new ShardedJedisPool(config, list, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
		try {
			jedis = pool.getResource();
		} catch (Exception e) {

			log.error("redis获取不到资源，重启");
		}
	}

	private static ShardedJedis getResource() {
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
		} catch (Exception e) {
			log.error("#RedisPool() getResource异常：", e);
		}
		return jds;
	}

	/**
	 * ******************************************** method name : get description :
	 *
	 * @return : String
	 ********************************************/
	@SuppressWarnings("deprecation")
	public static String get(String key) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			return jds.get(key);
		} catch (Exception e) {
			log.error("#RedisPool() get异常：", e);
			if (jds != null) {
				pool.returnBrokenResource(jds);
			}
		} finally {
			// pool.returnResource(jds);
			returnResource(pool, jds);
		}
		return null;

	}

	@SuppressWarnings("deprecation")
	public static Long increment(String key) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			return jds.incr(key);
		} catch (Exception e) {
			log.error("#RedisPool() get异常：", e);
			if (jds != null) {
				pool.returnBrokenResource(jds);
			}
		} finally {
			// pool.returnResource(jds);
			returnResource(pool, jds);
		}
		return null;

	}

	/**
	 * ******************************************** method name : set description :
	 * 
	 ********************************************/
	@SuppressWarnings("deprecation")
	public static void set(String key, int seconds, String value) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			jds.setex(key, seconds, value);
		} catch (Exception e) {
			log.error("#RedisPool() set异常：", e);
			if (jds != null) {
				pool.returnBrokenResource(jds);
			}
		} finally {
			// pool.returnResource(jds);
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : getByte
	 * description :
	 * 
	 * @return : byte[] *******************************************
	 */
	public static byte[] getByte(byte[] key) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			return jds.get(key);
		} catch (Exception e) {
			log.error("#RedisPool() getByte异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return null;

	}

	/**
	 * ******************************************** method name : setByte
	 * description :
	 * 
	 * *******************************************
	 */
	public static void setByte(byte[] key, int seconds, byte[] value) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			jds.setex(key, seconds, value);
		} catch (Exception e) {
			log.error("#RedisPool() setByte异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	public static void setByte(byte[] key, byte[] value) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			jds.setnx(key, value);
		} catch (Exception e) {
			log.error("#RedisPool() setByte异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	public static void set(String key, int seconds, Object obj) {
		if (obj instanceof String) {
			set(key, seconds, obj.toString());
		} else {
			set(key, seconds, JSONObject.toJSONString(obj));
		}
	}

	public static void set(String key, Object obj) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			if (obj instanceof String) {
				jds.set(key, obj.toString());
			} else {
				jds.set(key, JSONObject.toJSONString(obj));
			}
		} catch (Exception e) {
			log.error("#RedisPool() setByte异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	public static void set(String key, String obj) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			jds.set(key, obj);
		} catch (Exception e) {
			log.error("#RedisPool() setByte异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : rpush description
	 * : 在list尾部扩展一个元素 ******************************************
	 */
	public static void rpush(String key, Object obj) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			jds.rpush(key, JSONObject.toJSONString(obj));
		} catch (Exception e) {
			log.error("#RedisPool() rpush异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : rpush description
	 * :
	 ********************************************/
	public static void rpush(String key, Object obj, int seconds) {
		rpush(key, obj);
		expire(key, seconds);
	}

	/**
	 * ******************************************** method name : lrange description
	 * : 取得list在指定范围内的元素
	 * 
	 * @return : void
	 ********************************************/
	public static List<String> lrange(String key, long start, long end) {
		ShardedJedis jds = null;
		List<String> list = new ArrayList<String>();
		try {
			jds = getResource();
			list = jds.lrange(key, start, end);
		} catch (Exception e) {
			log.error("#RedisPool() lrange异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return list;
	}

	/**
	 * ******************************************** method name : expire description
	 * : 设置过期时间
	 * 
	 ********************************************/
	public static void expire(String key, int seconds) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			jds.expire(key, seconds);
		} catch (Exception e) {
			log.error("#RedisPool() expire异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : del description :
	 * 
	 ********************************************/
	public static void del(String key) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			jds.del(key);
		} catch (Exception e) {
			log.error("#RedisPool() del异常：", e);
		} finally {
			// pool.returnResource(jds);
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : returnResource
	 * description : 返还到连接池
	 * 
	 ********************************************/
	@SuppressWarnings("deprecation")
	public static void returnResource(ShardedJedisPool pool, ShardedJedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	/**
	 * ******************************************** method name : incr description :
	 * 
	 * @return : Long
	 ********************************************/
	public static Long incr(String key) {
		Long result = null;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.incr(key);
		} catch (Exception e) {
			log.error("#RedisPool() incr异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	/**
	 * ******************************************** method name : hset description :
	 * 
	 ********************************************/
	public static void hset(String key, String field, String value) {
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			jds.hset(key, field, value);
		} catch (Exception e) {
			log.error("#RedisPool() hset异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : hset description :
	 * 
	 ********************************************/
	public static void hset(String key, String field, Object value, int seconds) {
		hset(key, field, JSONObject.toJSONString(value));
		expire(key, seconds);
	}

	/**
	 * ******************************************** method name : hdel description :
	 * 
	 ********************************************/
	public static void hdel(String key, String fields) {
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			jds.hdel(key, fields);
		} catch (Exception e) {
			log.error("#RedisPool() hdel异常：", e);
		} finally {
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : hgetall
	 * description :
	 * 
	 ********************************************/
	public static Map<String, String> hgetall(String key) {
		ShardedJedis jds = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			jds = pool.getResource();
			map = jds.hgetAll(key);
			return map;
		} catch (Exception e) {
			log.error("#RedisPool() hdel异常：", e);
			return map;
		} finally {
			returnResource(pool, jds);
		}
	}

	/**
	 * ******************************************** method name : hget description :
	 * 
	 * @return : String
	 ********************************************/
	public static String hget(String key, String fields) {
		ShardedJedis jds = null;
		String value = "";
		try {
			jds = pool.getResource();
			value = jds.hget(key, fields);
			return value;
		} catch (Exception e) {
			log.error("#RedisPool() hdel异常：", e);
			return value;
		} finally {
			returnResource(pool, jds);
		}
	}

	@SuppressWarnings("deprecation")
	public void testBasicString() {
		for (int i = 0; i < 5; i++) {
			ShardedJedis jds = null;
			try {
				jds = pool.getResource();
				// jds.setex(key, seconds, value)
			} catch (Exception e) {
				log.error(e);
			} finally {
				pool.returnResource(jds);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void delKeys(String cacheType) {
		ShardedJedis jds = null;
		try {
			jds = getResource();
			// ShardedJedis jedis = RedisPool.getResource();
			Collection<Jedis> jedisC = jds.getAllShards();
			Iterator<Jedis> iter = jedisC.iterator();
			long count = 0;
			while (iter.hasNext()) {
				Jedis _jedis = iter.next();
				Set<String> keys = _jedis.keys("*" + cacheType + "*");
				if (keys.size() > 0) {
					count += _jedis.del(keys.toArray(new String[keys.size()]));
				}
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			pool.returnResource(jds);
		}
	}

	/**
	 * ******************************************** method name : incrBy description
	 * : 线程安全的累加
	 * 
	 * @return : Long *******************************************
	 */
	public static Long incrBy(String key, long num) {
		Long result = null;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.incrBy(key, num);
		} catch (Exception e) {
			log.error("#RedisPool() incrBy异常：", e);

		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	/**
	 * add by liJain 线程安全的递减
	 * 
	 * @param key key
	 * @param num num
	 * @return Long
	 */
	public static Long decrBy(String key, long num) {
		Long result = null;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.decrBy(key, num);
		} catch (Exception e) {
			log.error("#RedisPool() incrBy异常：", e);

		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	/**
	 * 设置全局锁
	 * 
	 * @param key   key
	 * @param value value
	 * @return Long
	 */
	public static Long setnx(String key, String value) {
		Long result = null;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.setnx(key, value);
		} catch (Exception e) {
			log.error("#RedisPool() incrBy异常：", e);

		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key
	 *
	 * @return boolean
	 */
	public static boolean exists(String key) {
		boolean result = false;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.exists(key);
		} catch (Exception e) {
			log.error("#RedisPool() exists异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	public static Long sAdd(String key, String value) {
		Long result = 0L;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.sadd(key, value);
		} catch (Exception e) {
			log.error("#RedisPool() sAdd异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	public static Boolean sismember(String key, String value) {
		Boolean result = false;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.sismember(key, value);
		} catch (Exception e) {
			log.error("#RedisPool() sismember异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	public static Set<String> smembers(String key) {
		Set<String> result = null;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.smembers(key);
		} catch (Exception e) {
			log.error("#RedisPool() smembers异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	public static Long ttl(String key) {
		Long result = 0L;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.ttl(key);
		} catch (Exception e) {
			log.error("#RedisPool()ttl异常：", e);

		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

	public static Long pexpireAt(String key, long millisecondsTimestamp) {
		Long result = null;
		ShardedJedis jds = null;
		try {
			jds = pool.getResource();
			result = jds.pexpireAt(key, millisecondsTimestamp);
		} catch (Exception e) {
			log.error("#RedisPool() pexpireAt异常：", e);
		} finally {
			returnResource(pool, jds);
		}
		return result;
	}

}
