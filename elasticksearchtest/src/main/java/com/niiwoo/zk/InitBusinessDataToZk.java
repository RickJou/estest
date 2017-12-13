package com.niiwoo.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.niiwoo.util.DateUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class InitBusinessDataToZk extends ZkToLocalStore {
	// 汇总分析时间节点数据路径
	private static String DAY_BEFORE_YESTERDAY_MONITOR_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/monitor/" + DateUtil.getDayBeforeYesterDay();
	//private static String YESTERDAY_MONITOR_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/monitor/" + DateUtil.getYesterDay();
	private static String TODAY_MONITOR_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/monitor/" + DateUtil.gettoDay();
	private static String TOMORROW_MONITOR_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/monitor/" + DateUtil.getTomorrowDay();
	
	// 预警信息分析时间节点数据路径
	private static String DAY_BEFORE_YESTER_DAY_WARNING_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/warning/" + DateUtil.getDayBeforeYesterDay();
	//private static String YESTERDAY_WARNING_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/warning/" + DateUtil.getYesterDay();
	private static String TODAY_WARNING_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/warning/" + DateUtil.gettoDay();
	private static String TOMORROW_WARNING_TIME_NODE_PATH = ZkToLocalStore.LISTEN_BASE_PATH + "/warning/" + DateUtil.getTomorrowDay();

	/**
	 * 初始化监控时间节点数据到zk
	 */
	public static void initMonitorAndWarningDataToZK() {
		// 初始化今天的分析时间节点和预警时间节点
		initTimeNodeDataToZkForNotExits(TODAY_MONITOR_TIME_NODE_PATH);
		initTimeNodeDataToZkForNotExits(TODAY_WARNING_TIME_NODE_PATH);
		// 初始化明天的分析时间节点和预警时间节点
		initTimeNodeDataToZkForNotExits(TOMORROW_MONITOR_TIME_NODE_PATH);
		initTimeNodeDataToZkForNotExits(TOMORROW_WARNING_TIME_NODE_PATH);
		//删除前天的时间节点
		deleteDayBeforeYesterDayTimeNode(DAY_BEFORE_YESTERDAY_MONITOR_TIME_NODE_PATH);
		deleteDayBeforeYesterDayTimeNode(DAY_BEFORE_YESTER_DAY_WARNING_TIME_NODE_PATH);
	}

	/**
	 * 如果前天的时间节点存在,则删除该节点
	 * @param nodePath
	 */
	private static void deleteDayBeforeYesterDayTimeNode(String nodePath){
		if(CuratorUtil.exitsNode(nodePath)){
			CuratorUtil.deleteNode(nodePath);
		}
	}
	
	/**
	 * 没有则创建时间点
	 * @param path
	 */
	private static void initTimeNodeDataToZkForNotExits(String nodePath) {
		if (CuratorUtil.exitsNode(nodePath)) {
			return;
		}
		List<Map> timeNode = new ArrayList<Map>(60);
		for (int j = 0; j < 60; j++) {
			for (int k = 0; k < 30; k++) {

				Map temp = new HashMap(1);
				temp.put("time", j + "m" + k + "s");
				temp.put("state", "untreated");
				timeNode.add(temp);
			}
		}
		String initTimeStr = JSON.toJSONString(timeNode);
		for (int i = 0; i < 24; i++) {
			CuratorUtil.createNodes(nodePath + "/" + i, initTimeStr);
		}
	}

}
