package com.niiwoo.msg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.niiwoo.msg.exception.BizException;
import com.niiwoo.msg.exception.ExcpParamCheck;
import com.niiwoo.util.DateUtil;

@SuppressWarnings("rawtypes")
public class DataMsg extends AbstractDataMsg {

	private static final long serialVersionUID = 1L;
	private List<Map> records = new ArrayList<Map>();

	public DataMsg() {
		super();
	}

	public void addRecord(Map record) {
		records.add(record);
	}

	public List<Map> getRecords() {
		return records;
	}

	public void setRecords(List<Map> records) {
		this.records = records;
	}

	/**
	 * 必要的业务参数不能为空
	 * 
	 * @param dataInput
	 * @param params
	 */
	public DataMsg paramNotNull(List<String> params) {
		for (String string : params) {
			if (StringUtils.isBlank(getString(string))) {
				throw new BizException(ExcpParamCheck.REQ_PARAM_IS_BLANK, string);
			}
		}
		return this;
	}

	public DataMsg paramMustEquals(String key, String... values) {
		if (!Arrays.asList(values).contains(this.getString(key))) {
			throw new BizException(ExcpParamCheck.PARAM_MUST_EQUALS, values.toString());
		}
		return this;
	}

	private transient boolean condition = false;

	/**
	 * 如果此参数等于默认值,则标记为true,语法采用流式编程
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public DataMsg ifParamEquals(String key, Object defaultValue) {
		if (defaultValue.equals(getProperties(key))) {
			condition = true;
			return this;
		}
		condition = false;
		return this;
	}

	/**
	 * 如果此参数不为空,则标记为true,语法采用流式编程
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public DataMsg ifParamNotNull(String key) {
		if (getProperties(key) != null) {
			condition = true;
			return this;
		}
		condition = false;
		return this;
	}

	/**
	 * 如果标记为true则验证参数不能为空
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public void thenParamNotNull(String... keys) {
		if (condition) {
			condition = false;
			paramNotNull(Arrays.asList(keys));
		}
	}

	public DataMsg paramNotNull(String... args) {
		paramNotNull(Arrays.asList(args));
		return this;
	}

	public void setSuccessOutPutMsg() {
		this.setProperties("rescode", "00000");
		this.setProperties("restimestamp", DateUtil.getTime());
	}

	/**
	 * 系统中所有list<map>结构都以resultlist为名称返回
	 * 
	 * @param records
	 */
	public void setCommonRecord(List<Map> records) {
		this.records = records;
		this.setProperties("resultlist", records);
	}
	
}
