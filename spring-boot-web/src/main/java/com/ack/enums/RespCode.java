package com.ack.enums;

import java.util.Objects;

/**
 * 响应异常码统一管理表(业务异常码规约，前2位：模块编号，后3位：异常编号
 * @author chen.zhao (chenzhao)  @DATE: 2018年3月5日
 */
public enum RespCode {
	//#公共模块 编码前缀：00
	/**
	 * 操作成功
	 */
    SUCCESS("1", "操作成功"),
    /**
	 * resultfulAPI操作成功
	 */
    API_SUCCESS("0000", "操作成功"),
    /**
     * 操作失败
     */
    FAILURE("0", "操作失败"),
    /**
     * 参数非法
     */
    PARAM_ILLEGAL("00001","参数非法"),
    
    PARAM_INCOMPLETE("00002","必要参数残缺"),
    /**
	 * 外部服务请求超时
	 */
	RESTFUL_REQ_TIMEOUT("00005", "外部服务请求超时"),
	/**
	 *外部服务请求业务异常,响应报文：%s
	 */
	RESTFUL_REQ_SERVICEERROR("00006", "%s"),
	/**
	 *外部服务响应报文为空
	 */
	RESTFUL_REQ_RESPERROR("00007", "外部服务响应报文为空"),
	/**
	 *主键服务异常
	 */
	SEQUENCE_SERVICE_EXCEPTION("00018","主键服务异常"),
    
    //#积分管理  编码前缀：01
    /**
     * 一个积分池只能从属于一个商户
     */
    POOL_ONLY("01001","商户下，该积分池已经存在"),
    /**
     * 积分池有效期区间非法
     */
    POOL_VALID_DATE("01002","积分池有效期区间非法"),
    /**
     * 手机号非法
     */
    POOL_PHONE("01003","手机号非法"),
    /**
	 * 短信服务异常
	 */
	SMGSERVICE_ERROR("00013","短信服务异常"),
	/**
	 * 短信模板ID不存在
	 */
	SMGTEMLATE_NOTFOUND("00014","短信模板ID不存在"),
	/**
	 *短信服务参数校验不通过
	 */
	SMGPARAM_CHECHERROR("00015","短信服务参数校验不通过"),
	/**
	 *短信发送繁忙
	 */
	SMGSTIME_BUSY("00016","短信发送繁忙"),
	/**
	 *调用短信服务异常
	 */
	INVOKE_SMGSERVICE_ERROR("00017","调用短信服务异常"),
    /**
     * 预警通知邮箱非法
     */
    POOL_EMAIL("01004","预警通知邮箱非法"),
    /**
     * 描述不得超过300字符
     */
    POOL_DESC("01005","描述不得超过300字符"),
    
    
    
    //#注减分管理 编码前缀：02
    
    /**
     * 预付款信息不能为空
     */
    ADDPOINTS_PAYINFO("02002","预付款信息不能为空"),
    
    //#冻结解冻管理 编码前缀：03
    /**
     * 冻结积分不得超过可用积分
     */
    ADDPOINTS_VAL_OVERFLOW("03001","冻结积分不得超过可用积分"),
    /**
     * 解冻积分不得超过冻结积分
     */
    ADDPOINTS_UNFROZENVAL_OVERFLOW("03002","解冻积分不得超过冻结积分"),
    
    //#场景关联 编码前缀：04
    /**
     * 积分池与场景已经关联成功
     */
    POINTS_TXN_ISASSOCIATED("04006","积分池与场景已经关联成功"),
    /**
     * 积分池与场景关联已经申请成功
     */
    POINTS_TXN_ISREQ("04007","积分池与场景关联已经申请成功"),
    /**
     * 场景关联记录已存在
     */
    POINTS_TXN_ISEXIST("04008",""),
     
    //#审核管理 编码前缀：05
    
    //#积分交易服务 编码前缀：06
    /**
     * 活动场景未关联积分池
     */
    POINTS_TXN_SOURCE_POOL("06001","活动场景未关联积分池"),
    /**
     * 场景关联状态异常
     */
    POINTS_TXN_ASSOCIATED_POOL_STATUS("06002","场景关联状态异常"),
    /**
     * 积分池状态异常
     */
    POINTS_TXN_POOL_STATUS("06003","积分池状态异常"),
    /**
     * 积分池有效期异常
     */
    POINTS_TXN_POOL_VALID("06004","积分池有效期异常"),
    /**
     * 积分池可用积分不足
     */
    POINTS_TXN_USABLEPOINTS("06005","积分池可用积分不足"),
    /**
     * 积分交易重复
     */
    POINTS_TXN_REPEAT("06006","积分交易重复"),
    /**
     * 退分交易溢出
     */
    POINTS_TXN_RESIDUALPOINTS("06007","退分交易溢出"),
    /**
     * 退分-源交易流水不存在
     */
    POINTS_TXN_ORIGINTXN_INVALID("06008","退分-源交易流水不存在"),
    /**
     * 积分交易放行
     */
    POINTS_TXN_PERMIT_THROUGH("0000","积分交易放行"),
    
    /**
     * 验证码错误
     */
    AUTH_VALID_ERROR("07001","验证码错误"),
    /**
     * 身份认证授权失败
     */
    AUTH_PHONE_ERROR("07002","身份认证授权失败"),
    /**
     * 身份认证失败，会话user_id缺失
     */
    AUTH_USERID_ERROR("07003","身份认证失败，会话user_id缺失"),
    /**
     * 验证码已发送，请等待重试
     */
    AUTH_CODE_REPEAT("07003","验证码已发送，请等待重试"),
    /**
     * 鉴权过滤器URL拦截pattern未配置
     */
    AUTH_FILTER_PATTEN("07004","鉴权过滤器URL拦截pattern未配置"),
    
    END("0","未知错误");
   

    private String code;
    private String msg;

    RespCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RespCode getRespByCode(String code) {
        if(code == null){ return null;}
        for (RespCode resp: values()) {
            if (resp.getCode().equals(code)){
                return resp;
            }
        }
        throw new IllegalArgumentException("无效的code值!code:"+code);
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
