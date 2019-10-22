package com.ack.enums;

import java.util.Objects;

/**
 * 响应异常码统一管理表(业务异常码规约，前2位：模块编号，后3位：异常编号
 * 
 * @author chenzhao @date Oct 16, 2019
 */
public enum RespCode {
	// #公共模块==编码前缀：00----------------------------------------------------------------------------------------------
	/**
	 * "0", "success"
	 */
	SUCCESS("0", "success"),

	/**
	 * 参数非法
	 */
	PARAM_ILLEGAL("00001", "参数非法"), PARAM_INCOMPLETE("00002", "必要参数[%s]残缺"),

	REQ_SIGN_ERROR("00003", "验签失败"),

	REQ_SIGN_ERROR_DEBUG("00003", "验签失败，%s，MD5原字符串:%s"),

	REQ_VERSION_ERROR("00004", "请求服务version异常，不存在此版本服务"),
	/**
	 * 外部服务请求超时
	 */
	RESTFUL_REQ_TIMEOUT("00005", "外部服务请求超时"),
	/**
	 * 外部服务请求业务异常,响应报文：%s
	 */
	RESTFUL_REQ_SERVICEERROR("00006", "%s"),
	/**
	 * 外部服务响应报文为空
	 */
	RESTFUL_REQ_RESPERROR("00007", "外部服务响应报文为空"),
	/**
	 * "00008", "用户未登录"
	 */
	user_unLogin("00008", "用户未登录"),
	// #大屏监控+整体监控==编码前缀：01----------------------------------------------------------------------------------------------
	/**
	 * "01001", "查询时间区间过长，不得超过1天"
	 */
	monitor_timeinterval_out("01001", "查询时间区间过长，不得超过1天"),

	// #运行管理==编码前缀：04----------------------------------------------------------------------------------------------
	/**
	 * "04001", "模式参数保存失败，未全部成功"
	 */
	operator_save_error("04001", "模式参数保存失败，未全部成功"),
	/**
	 * "04004", "设备通讯失败，指令未成功"
	 */
	operator_control_error("04004", "设备通讯失败，指令未成功"),
	/**
	 * "04009", "设备类型不存在"
	 */
	operator_equType_unexist("04009", "设备类型不存在"),
	/**
	 * "04013", "设备配置未初始化"
	 */
	operator_equ_unInit("04013", "设备配置未初始化"),

	// #系统用户权限管理==编码前缀：08----------------------------------------------------------------------------------------------
	/**
	 *"08001", "密码格式不正确,密码包含 数字,英文,字符中的两种以上，长度6-20"
	 */
	user_pwd_formatError("08001", "密码格式不正确,密码包含 数字,英文,字符中的两种以上，长度6-20"),
	/**
	 * "08005", "原密码不正确"
	 */
	user_pwd_error("08005", "原密码不正确"),
	/**
	 * "08006", "用户已被禁用，请联系管理员"
	 */
	user_islimited("08006", "用户已被禁用，请联系管理员"),
	/**
	 * "08007", "用户已被删除，请联系管理员"
	 */
	user_isDeleted("08007", "用户已被删除，请联系管理员"),
	/**
	 * "08009", "原密码和新密码不能相同"
	 */
	user_pwd_exists("08009", "原密码和新密码不能相同"),
	/**
	 * "08011", "用户已存在"
	 */
	user_exists("08011", "用户已存在"),
	/**
	 * "08015", "用户只支持单角色"
	 */
	user_only_singleRole("08015", "用户只支持单角色"),
	/**
	 * "08017", "登录失败，用户角色权限未配置"
	 */
	user_role_unExists("08017", "登录失败，用户角色权限未配置"),
	/**
	 * "08019", "角色名称已存在，不能重复"
	 */
	role_exists("08019", "角色名称已存在，不能重复"),

	// #最后一个结束----------------------------------------------------------------------------------
	/**
	 * "9", "服务繁忙"
	 */
	FAILURE("9", "服务繁忙");

	private String code;
	private String msg;

	RespCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static RespCode getRespByCode(String code) {
		if (code == null) {
			return null;
		}
		for (RespCode resp : values()) {
			if (resp.getCode().equals(code)) {
				return resp;
			}
		}
		throw new IllegalArgumentException("无效的code值!code:" + code);
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public boolean isSuccess(String code) {
		return Objects.equals(code, this.code);
	}
}
