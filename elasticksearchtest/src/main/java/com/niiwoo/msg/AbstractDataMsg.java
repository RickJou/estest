package com.niiwoo.msg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.niiwoo.msg.exception.BizException;
import com.niiwoo.msg.exception.ExcpParamCheck;

/**
 * 所有方法传参类的抽象类
 * 
 * @author alan
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AbstractDataMsg extends HashMap implements IMsg,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String businessCode = UUID.randomUUID().toString();

	/**
	 * create DataMsg for Json String
	 * 
	 * @param jsonString
	 * @return
	 */
	public static DataMsg parseJson(String jsonString) {
		return JSON.parseObject(jsonString, DataMsg.class);
	}

	/**
	 * get value by key
	 * 
	 * @param key
	 * @return
	 */
	public Object getProperties(String key) {
		return getPathParamValue(key);
	}

	/**
	 * get value by HashMap
	 * @param key
	 * @return
	 */
	public HashMap getHashMap(String key) {
		Object value = getPathParamValue(key);
		return value == null ? null : (HashMap) value;
	}
	
	/**
	 * get value by AbstractMsg
	 * @param key
	 * @return
	 */
	public AbstractDataMsg getAbstractDataMsg(String key) {
		Object value = getPathParamValue(key);
		return value == null ? null : (AbstractDataMsg) value;
	}

	/**
	 * get value by ArrayList<HashMap>
	 * @param key
	 * @return
	 */
	public List<HashMap> getListHashMap(String key) {
		Object value = getPathParamValue(key);
		if (value == null) {
			return new ArrayList<HashMap>();
		} else if (value instanceof com.alibaba.fastjson.JSONArray) {
			JSONArray arr = (JSONArray) value;
			return JSON.parseObject(arr.toJSONString(), ArrayList.class);
		} else if (value instanceof List) {
			return (List<HashMap>) value;
		}
		return null;
	}
	
	public List<AbstractDataMsg> getListMsg(String key) {
		Object value = getPathParamValue(key);
		if (value == null) {
			return new ArrayList<AbstractDataMsg>();
		} else if (value instanceof com.alibaba.fastjson.JSONArray) {
			JSONArray arr = (JSONArray) value;
			return JSON.parseObject(arr.toJSONString(), ArrayList.class);
		} else if (value instanceof List) {
			return (List<AbstractDataMsg>) value;
		}
		return null;
	}

	/**
	 * get String type value by key
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		Object value = getPathParamValue(key);
		return value == null ? null : value.toString();
	}

	public String getString(String key, String defaultValue) {
		Object value = getPathParamValue(key);
		return value == null ? defaultValue : value.toString();
	}

	public int getInt(String key) {
		Object value = getPathParamValue(key);
		return value == null ? 0 : Integer.parseInt(value.toString());
	}

	public int getInt(String key, int defaultValue) {
		Object value = getPathParamValue(key);
		return (value == null || value.toString().trim().length() == 0) ? defaultValue : Integer.parseInt(value.toString());
	}

	public Double getDouble(String key) {
		Object value = getPathParamValue(key);
		return (value == null || value.toString().trim().length() == 0) ? 0 : Double.parseDouble(value.toString());
	}

	public Double getDouble(String key, Double defaultValue) {
		Object value = getPathParamValue(key);
		return (value == null || value.toString().trim().length() == 0) ? defaultValue : Double.parseDouble(value.toString());
	}

	/**
	 * 深层次参数查找
	 * 
	 * @param path
	 * @return
	 */
	private Object getPathParamValue(String path) {
		if (path.indexOf(".") != -1) {
			String[] multiKey = path.split("\\.");
			return getMultiValueForMap(this.getProperties(), multiKey, 0);
		} else {
			return this.get(path);
		}
	}
	
	
	/**
	 * 删除指定路径的值
	 * @param path
	 * @return
	 */
	public Object remove(String path) {
		if (path.indexOf(".") != -1) {
			String[] multiKey = path.split("\\.");
			return removeMultiValueForMap(this.getProperties(), multiKey, 0);
		} else {
			return this.get(path);
		}
	}
	
	/**
	 * 多层次递归路径删除并返回值
	 * @param map
	 * @param keys
	 * @param index
	 * @return
	 */
	private Object removeMultiValueForMap(Map map, String[] keys, int index){
		if (index == keys.length - 1) {
			return map.remove(keys[index]);
		}
		Object obj = map.get(keys[index]);
		if (obj instanceof String) {// fastjson 未多层次转换
			try {
				HashMap innerStrToMap = JSON.parseObject((String) obj, HashMap.class);
				return getMultiValueForMap(innerStrToMap, keys, ++index);
			} catch (Exception ex) {
				throw new BizException(ExcpParamCheck.PARAM_FORMART_TO_JSON_ERROR);
			}
		} else if (obj instanceof Map) {
			return getMultiValueForMap((Map) obj, keys, ++index);
		} else if(obj ==null){
			return null;
		}else {
			throw new BizException(ExcpParamCheck.PARAM_FORMART_TO_JSON_ERROR);
		}
	}

	/**
	 * 多层次递归路径查找值
	 * 
	 * @param map
	 * @param keys
	 * @param index
	 * @return
	 */
	private Object getMultiValueForMap(Map map, String[] keys, int index) {
		if (index == keys.length - 1) {
			return map.get(keys[index]);
		}
		Object obj = map.get(keys[index]);
		if (obj instanceof String) {// fastjson 未多层次转换
			try {
				HashMap innerStrToMap = JSON.parseObject((String) obj, HashMap.class);
				return getMultiValueForMap(innerStrToMap, keys, ++index);
			} catch (Exception ex) {
				throw new BizException(ExcpParamCheck.PARAM_FORMART_TO_JSON_ERROR);
			}
		} else if (obj instanceof Map) {
			return getMultiValueForMap((Map) obj, keys, ++index);
		} else if(obj ==null){
			return null;
		}else {
			throw new BizException(ExcpParamCheck.PARAM_FORMART_TO_JSON_ERROR);
		}
	}

	/**
	 * 获得Collection<Map<String,Object>>类型的参数. 如:[{
	 * 'url':'xxx.xx.jpg','type':'img'},{'url':'http://www.xxxx.com/a.jsp','type':'ty
	 * p e ' : ' h 5 ' } ] 则可以使用ArrayList<HashMap<String,Object>>进行接收
	 * 
	 * @param <T>
	 * @param <E>
	 * 
	 * @param paramName
	 * @param T
	 * @return
	 */
	public <T> T getCollectionObject(String paramName, Collection T) {
		String arrayStr = getString(paramName);
		if (arrayStr != null && arrayStr.length() > 0) {
			return (T) JSON.parseObject(arrayStr, T.getClass());
		}
		return null;
	}

	public AbstractDataMsg renameKeyName(String oldKeyName, String newKeyName) {
		this.put(newKeyName, this.get(oldKeyName));
		this.remove(oldKeyName);
		return this;
	}

	public boolean getBoolean(String key) {
		Object value = this.get(key);
		if (value instanceof Boolean) {
			return (boolean) value;
		}
		throw new RuntimeException(key + "not instanceof Boolean");
	}

	/**
	 * 查看key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		return this.getProperties(key) == null ? false : true;
	}

	/**
	 * set value and key
	 * 
	 * @param key
	 * @param value
	 */
	public AbstractDataMsg setProperties(String key, Object value) {
		this.put(key, value);
		return this;
	}

	/**
	 * put map to properties
	 * 
	 * @param map
	 */
	public void setPropertiesAll(Map<String, Object> map) {
		this.putAll(map);
	}

	/**
	 * get properties for map
	 * 
	 * @return
	 */
	public HashMap<String, Object> getProperties() {
		return this;
	}

	/**
	 * set properties for map
	 * 
	 * @param properties
	 */
	public void setProperties(HashMap<String, Object> properties) {
		this.putAll(properties);
	}

	/**
	 * get businessCode
	 * 
	 * @return
	 */
	public String getBusinessCode() {
		return businessCode;
	}

	/**
	 * set businessCode
	 * 
	 * @param businessCode
	 */
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public void clear() {
		this.clear();
	}
	
	/**
	 * DataMsg to Json String
	 * 
	 * @return
	 */
	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	public AbstractDataMsg() {
		super();
	}

}