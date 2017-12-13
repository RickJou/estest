package com.niiwoo.msg.exception;


public enum ExcpParamCheck implements IBizException {
	
	/**
	 * 请求参数%s不能为空
	 */
	REQ_PARAM_IS_BLANK("00101", "参数%s不能为空"),
	/**
	 * 参数必须为%s其一
	 */
	PARAM_MUST_EQUALS("00102","参数必须为%s之一"),
	
	/**
	 * 参数必须为%s其一
	 */
	PARAM_MUST_INSTANCE_OF_STRING("00103","参数必须java.lang.String类型"),
	/**
	 * 参数转换为json格式失败
	 */
	PARAM_FORMART_TO_JSON_ERROR("00104","无法解析json指定路径参数值"),
	/**
	 * 多层次查找参数返回的类型不支持
	 */
	PARAM_MUlti_leve_fine_class_type_err("00105","多层次查找参数返回的类型不支持")
	;

	public String code;
	public String msg;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private ExcpParamCheck(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
