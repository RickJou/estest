package com.niiwoo.zk;

import com.niiwoo.msg.AbstractDataMsg;

public class ZkToLocalStore {
	public static String LISTEN_BASE_PATH="/tc-ioms-qmp/listen";
	
	public static AbstractDataMsg STORE = new AbstractDataMsg();

	
	/**
	 * 查看节点
	 * @param path
	 * @param value
	 * @return
	 */
	public static String getPath(String path,String value){
		return STORE.getString(path);
	}
	
	public static void addPath(String path,String value){
		STORE.setProperties(path,value);
	}
	
	/**
	 * 添加节点
	 * @param path
	 * @param value
	 */
	public static void updatePath(String path,String value){
		STORE.setProperties(path,value);
	}
	
	/**
	 * 删除节点
	 * @param path
	 */
	public static void removePath(String path){
		STORE.remove(path);
	}
	
}
