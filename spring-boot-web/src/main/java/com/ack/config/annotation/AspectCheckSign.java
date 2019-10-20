package com.ack.config.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ack.common.RedisPool;
import com.ack.common.ServiceException;
import com.ack.constants.RedisCons;
import com.ack.enums.RespCode;
import com.ack.utils.PropertiesUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 签名校验（！！！！！！！注意：此验签要求control方法的@requestBody
 * 的修饰对象必须为JSONObject类型，否则参与验签的数据会和前端不一致）
 * 
 * @author chenzhao @date Oct 16, 2019
 */
@Aspect
@Component
public class AspectCheckSign {

	private Logger log = LoggerFactory.getLogger(AspectCheckSign.class);

//	@Pointcut("@within(com.ems.config.annotation.CheckSign)") // 匹配加注解的类/方法
//	public void checkSign() {
//	}

	/**
	 * 验签+可选验登陆
	 * 
	 * @author chenzhao @date Oct 18, 2019
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Around("@annotation(CheckSign)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		CheckSign checkSign = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(CheckSign.class);
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest req = sra.getRequest();
		String sign = req.getHeader("sign");
		String token = req.getHeader("token");
		String timestamp = req.getHeader("timestamp");
		Object[] args = pjp.getArgs();
		Parameter[] params = method.getParameters();
		// #校验登录
		checkLogin(method, checkSign, token);
		for (int i = 0; i < args.length; i++) {
			Parameter parameter = params[i];
			StringBuffer s1 = new StringBuffer();
			if (parameter.isAnnotationPresent(RequestBody.class)) {
				if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(token) || StringUtils.isEmpty(timestamp)) {
					throw new ServiceException(RespCode.PARAM_INCOMPLETE, "sign,token,timestamp(http请求头中)");
				}
				JSONObject reqJson;
				if (args[i] instanceof JSONObject) {// JSONObject
					reqJson = (JSONObject) args[i];
				} else if (args[i] instanceof String) {// String
					reqJson = JSONObject.parseObject((String) args[i]);
				} else {// java Object
					throw new ServiceException("#requestBody只能用JSONObject类型；用实体类对象，会导致对象的空属性参与加签，导致参与加签参数和前端不同");
				}
				log.info(">>>check api sign,#sign:[{}],#token:[{}],#timestamp:[{}]\n#requestBody:[{}]", sign, token,
						timestamp, reqJson);
				TreeMap<String, Object> reqTreeMap = new TreeMap<>(reqJson.toJavaObject(Map.class));// TreeMap自动根据key升序排序
				for (Iterator iterator = reqTreeMap.entrySet().iterator(); iterator.hasNext();) {
					Entry entry = (Entry) iterator.next();
					s1.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				}
				s1.append("timestamp").append("=").append(timestamp);// 加时间戳
				String localSign = DigestUtils.md5DigestAsHex(s1.toString().getBytes());
				log.debug("#localSign:[{}] #未MD5前的原始字符串s1:[{}]", localSign, s1);
				if (!sign.equalsIgnoreCase(localSign)) {
					if (log.isDebugEnabled()) {// 如果是测试环境，则直接响应正确签名，方便测试
						throw new ServiceException(RespCode.REQ_SIGN_ERROR_DEBUG, localSign, s1);
					}
					throw new ServiceException(RespCode.REQ_SIGN_ERROR);
				}
			} else {
				log.debug("#未注解arg,[{}]:[{}]", params[i], args[i]);
			}
		}
		log.debug(">>>api proceedInterval,#api[{}],#interval:[{}]", method, System.currentTimeMillis() - startTime);
		// #go on
		Object obj = pjp.proceed();
		return obj;
	}

	private void checkLogin(Method method, CheckSign checkSign, String token) {
		if (StringUtils.isEmpty(token)) {
			throw new ServiceException(RespCode.PARAM_INCOMPLETE, "token");
		}
		if (checkSign.isCheckLogin()) {
			JSONObject loginUser = JSONObject.parseObject(RedisPool.get(RedisCons.loginToken + token));
			if (loginUser == null) {
				throw new ServiceException(RespCode.user_unLogin);
			} else {// 每次登录验证通过延长会话有效期
				RedisPool.expire(RedisCons.loginToken + token,
						Integer.parseInt(PropertiesUtils.getProperty("loginSessionTimeout", "1800")));
			}
		} else {
			log.debug("不校验登陆：{}", method.getName());
		}
	}

	public static String base64(String str) {
		String strBase64 = Base64.getEncoder().encodeToString(str.getBytes());
		System.out.println("#str加密后:" + strBase64);
		byte[] bytes = Base64.getDecoder().decode(strBase64);
		System.out.println("#str加解密后:" + new String(bytes));

		return null;
	}

	public static void main(String[] args) {
		base64("撒旦解放了手机发了几位老人家");
	}

}