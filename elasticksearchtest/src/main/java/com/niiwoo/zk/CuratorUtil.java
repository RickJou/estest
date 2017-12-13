package com.niiwoo.zk;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

public class CuratorUtil {
	private static Logger log = Logger.getLogger(CuratorUtil.class);
	/** zookeeper地址 */
	private static String zookeeperConnectAddr = "192.168.188.11:2181";
	/** session超时时间 */
	private static int sessionOuttime = 5000;// ms
	/** 初试1s */
	private static int connectWaitTime = 1000;
	/** 重试10次 */
	private static int maxRetries = 10;

	private static CuratorFramework cf = null;

	private static final Object lock = new Object();

	/**
	 * 连接zk
	 */
	private static void connectZk() {
		if (null == cf) {
			synchronized (lock) {
				if (null == cf) {
					// 1 重试策略：初试时间为1s 重试10次
					RetryPolicy retryPolicy = new ExponentialBackoffRetry(connectWaitTime, maxRetries);
					// 2 通过工厂创建连接
					cf = CuratorFrameworkFactory.builder().connectString(zookeeperConnectAddr).sessionTimeoutMs(sessionOuttime).retryPolicy(retryPolicy).build();
					// 3 开启连接
					cf.start();
					log.info("连接到zookeeper" + zookeeperConnectAddr);
				}
			}
		}
	}

	/**
	 * 对指定路径添加分布式锁,如果未能获取到锁,则返回null; 如果成功则返回加锁成功的InterProcessMutex实例,
	 * 需要在完成业务后的try-catch-finally中执行interProcessMutex.release()释放锁;
	 * 
	 * @param lockpath
	 * @return
	 * @throws Exception
	 */
	public static InterProcessMutex lockPath(String lockpath) throws Exception {
		connectZk();
		InterProcessMutex ipmLock = new InterProcessMutex(cf, lockpath);
		if (ipmLock.acquire(5, TimeUnit.SECONDS)) {// max wait 5 second
			return ipmLock;
		}
		return null;
	}

	/**
	 * 创建指定路径持久化节点
	 * 
	 * @param nodePath
	 * @param nodeValue
	 */
	public static void createNodes(String nodePath, String nodeValue) {
		try {
			connectZk();
			cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodePath, nodeValue.getBytes());
			log.info("crate zk node " + nodePath + ":" + nodeValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 如果节点存在,则修改其值,否则创建节点
	 * 
	 * @param nodePath
	 * @param nodeValue
	 */
	public static void saveNodes(String nodePath, String nodeValue) {
		try {
			connectZk();
			if (CuratorUtil.notExitsNode(nodePath)) {
				CuratorUtil.createNodes(nodePath, nodeValue);
				log.info("crate zk node " + nodePath + ":" + nodeValue);
			} else {
				String zkValue = CuratorUtil.getNodeValue(nodePath);
				// node exits then update node value
				if (!StringUtils.equals(zkValue, nodeValue)) {
					CuratorUtil.setNodes(nodePath, nodeValue);
					log.info("update zk node " + nodePath + ":" + nodeValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setNodes(String nodePath, String nodeValue) {
		try {
			connectZk();
			cf.setData().forPath(nodePath, nodeValue.getBytes());
			log.info("set zk node " + nodePath + ":" + nodeValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查看节点是否存在
	 * 
	 * @param nodePath
	 * @return true:存在;false:不存在;
	 */
	public static boolean exitsNode(String nodePath) {
		try {
			connectZk();
			return cf.checkExists().forPath(nodePath) == null ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查看节点是否存在
	 * 
	 * @param nodePath
	 * @return true:不存在;false:存在;
	 */
	public static boolean notExitsNode(String nodePath) {
		return !exitsNode(nodePath);
	}

	/**
	 * 删除节点
	 * 
	 * @param nodePath
	 */
	public static void deleteNode(String nodePath) {
		try {
			connectZk();
			cf.delete().deletingChildrenIfNeeded().forPath(nodePath);
			log.info("delete zk node " + nodePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查看节点数据
	 * 
	 * @param nodePath
	 * @return
	 */
	public static String getNodeValue(String nodePath) {
		try {
			connectZk();
			return new String(cf.getData().forPath(nodePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取该路径下所有子节点名称
	 * 
	 * @param nodePath
	 * @return 没有子节点:null
	 */
	public static List<String> getChildNodes(String nodePath) {
		try {
			connectZk();
			List<String> child = cf.getChildren().forPath(nodePath);
			return child == null || child.isEmpty() ? null : child;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * create cache node 触发事件为创建节点和更新节点，此操作并不触发删除节点。
	 * 
	 * @param nodePath
	 * @return
	 */
	public static NodeCache creatCacheNode(String nodePath) {
		try {
			connectZk();
			NodeCache nc = new NodeCache(cf, nodePath, false);
			nc.start(true);
			return nc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * create path children cache
	 * 注意:在节点建立连接时,如果监听的节点下面有子节点,那麽将会触发监听器,触发类型为:CHILD_ADDED 触发事件为新建、修改、删除
	 * 
	 * @param nodePath
	 * @return
	 */
	public static PathChildrenCache creatPathChildrenCache(String nodePath) {
		try {
			connectZk();
			PathChildrenCache pcc = new PathChildrenCache(cf, nodePath, true);
			pcc.start(StartMode.POST_INITIALIZED_EVENT);
			return pcc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 监控 指定节点和节点下的所有的节点的变化--无限监听
	 * 
	 * @param path
	 * @return
	 */
	public static TreeCache getTreeCaheListen(String path) {
		try {
			connectZk();
			TreeCache tc = new TreeCache(cf, path);
			return tc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getConnectWaitTime() {
		return connectWaitTime;
	}

	public static void setConnectWaitTime(int connectWaitTime) {
		CuratorUtil.connectWaitTime = connectWaitTime;
	}

	public static int getMaxRetries() {
		return maxRetries;
	}

	public static void setMaxRetries(int maxRetries) {
		CuratorUtil.maxRetries = maxRetries;
	}

	public static String getZookeeperConnectAddr() {
		return zookeeperConnectAddr;
	}

	public static void setZookeeperConnectAddr(String zookeeperConnectAddr) {
		CuratorUtil.zookeeperConnectAddr = zookeeperConnectAddr;
	}

	public static int getSessionOuttime() {
		return sessionOuttime;
	}

	public static void setSessionOuttime(int sessionOuttime) {
		CuratorUtil.sessionOuttime = sessionOuttime;
	}
}
