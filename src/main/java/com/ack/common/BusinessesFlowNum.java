/**
 *
 */
package com.ack.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import com.ack.enums.RespCode;

/**
 * 业务流水号，生成服务
 * @author chenzhao @date Oct 16, 2019
 */
public class BusinessesFlowNum {

	static ReentrantLock lock = new ReentrantLock();

	/**
	 * 
	 * @author chenzhao @date Nov 28, 2018
	 * @param prefix     流水号前缀
	 * @param flowNumKey 流水号redis缓存key {不同流水号使用不同key，防止重复}
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String getNum(String prefix, String flowNumKey) throws Exception {
		Long flowNum = 1L;
		Date dt = new Date();
		dt.setHours(23);
		dt.setMinutes(59);
		dt.setSeconds(59);

		try {
			lock.lock();
			if (RedisPool.exists(flowNumKey)) {
				flowNum = RedisPool.incr(flowNumKey);
				if (new Date().getHours() != 23) {// 除23点不更新，其它时间更新redis超时时间{防止缓存超时时间穿透到第2天}
					RedisPool.pexpireAt(flowNumKey, dt.getTime());
				}
			} else {
//				RedisPool.set(redisNumKey, Integer.parseInt(DateUtil.calcInterval(new Date(),dt)/1000+""), 1);
				RedisPool.set(flowNumKey, 1);
				RedisPool.pexpireAt(flowNumKey, dt.getTime());
			}
		} catch (Exception e) {
			throw new ServiceException(RespCode.FAILURE);
		} finally {
			lock.unlock();
		}
		return prefix + new SimpleDateFormat("yyyyMMdd").format(new Date()) + String.format("%08d", flowNum);
	}

}
