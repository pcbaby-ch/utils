/**
 *
 */
package com.ack.common;


import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import com.ack.config.RedisPool;
import com.ack.enums.RespCode;
import com.ack.util.DateUtil;


/**
 * 业务流水号，生成服务
 * @author chen.zhao @DATE: Aug 2, 2018
 */
public class BusinessesFlowNum {
	
	private static String redisNumKey	="pointsPresent_txnOrderSeq";
	private static String redisRpidKey = "pointsPresent_rpid";
	
	static ReentrantLock lock=new ReentrantLock();
	
	@SuppressWarnings("deprecation")
	public static  String getNum() throws Exception{
		Long flowNum=1L;
		Date dt=new Date();
		dt.setHours(23);
		dt.setMinutes(59);
		dt.setSeconds(59);
		
		try {
			lock.lock();
			if(RedisPool.exists(redisNumKey)){
				flowNum=RedisPool.incr(redisNumKey);
				RedisPool.pexpireAt(redisNumKey, dt.getTime());
			}else{
//				RedisPool.set(redisNumKey, Integer.parseInt(DateUtil.calcInterval(new Date(),dt)/1000+""), 1);
				RedisPool.set(redisNumKey, 1);
				RedisPool.pexpireAt(redisNumKey, dt.getTime());
			}
		} catch (Exception e) {
			throw new ServiceException(RespCode.FAILURE);
		}finally {
			lock.unlock();
		}
		return "P"+DateUtil.dfyyyyMMdd.format(new Date())+String.format("%08d", flowNum);
	}

	@SuppressWarnings("deprecation")
	public static  String getRpid() throws Exception{
		Long flowNum=1L;
		Date dt=new Date();
		dt.setHours(23);
		dt.setMinutes(59);
		dt.setSeconds(59);

		try {
			lock.lock();
			if(RedisPool.exists(redisRpidKey)){
				flowNum=RedisPool.incr(redisRpidKey);
			}else{
				RedisPool.set(redisRpidKey, Integer.parseInt(DateUtil.calcInterval(new Date(),dt)/1000+""), 1);
			}
		} catch (Exception e) {
			throw new ServiceException(RespCode.FAILURE);
		}finally {
			lock.unlock();
		}
		String date = DateUtil.dfyyyyMMdd.format(new Date());
		return "R"+date+String.format("%08d", flowNum).substring(2,date.length());
	}

}
