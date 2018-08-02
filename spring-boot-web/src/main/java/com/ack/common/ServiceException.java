package com.ack.common;

import com.ack.enums.RespCode;

/**
 * 业务异常
 * 
 * @author chen.zhao (chenzhao) @DATE: 2018年3月5日
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 3653415555548581494L;

	private String code;

	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@SuppressWarnings("unused")
	private ServiceException() {
	}

	public ServiceException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public ServiceException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public ServiceException(RespCode respCode) {
		super(respCode.getCode() + respCode.getMsg());
		this.code = respCode.getCode();
		this.msg = respCode.getMsg();
	}

	public ServiceException(RespCode respCode, Object... moreMsg) {
		super(respCode.getCode() + String.format(respCode.getMsg(), moreMsg));
		this.code = respCode.getCode();
		try {
			msg = String.format(respCode.getMsg(), moreMsg);
		} catch (Exception e) {
			msg = respCode.getMsg();
		}
	}
	/**
	 * 
	 * @param externalServiceCode 外部系统响应异常码
	 * @param respCode 全局异常枚举
	 * @param moreMsg 占位符替换数据
	 */
	public ServiceException(String externalServiceCode,RespCode respCode, Object... moreMsg) {
		super(respCode.getCode()+externalServiceCode + String.format(respCode.getMsg(), moreMsg));
		this.code = respCode.getCode()+externalServiceCode;
		try {
			msg = String.format(respCode.getMsg(), moreMsg);
		} catch (Exception e) {
			msg = respCode.getMsg();
		}
	}
}
