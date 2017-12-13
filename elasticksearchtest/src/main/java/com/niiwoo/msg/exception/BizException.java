package com.niiwoo.msg.exception;

import org.apache.log4j.Logger;

public class BizException extends RuntimeException {
	Logger log = Logger.getLogger(BizException.class);
	private static final long serialVersionUID = -5875371379845226068L;
	private String code;
	private String msg;

	
	public BizException(IBizException Excp) {
		this.code=Excp.getCode();
		this.msg=Excp.getMsg();
	}


	public BizException(IBizException Excp,String...args) {
		String msg = String.format(Excp.getMsg(), args);
		this.code =Excp.getCode();
		this.msg=msg;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public String getMsg(){
		return this.msg;
	}

	@Override
	public void printStackTrace() {
		log.error("错误码:"+this.getCode()+"错误信息:"+this.getMsg());
		super.printStackTrace();
	}
	
	
}
